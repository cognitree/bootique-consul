package com.cognitree.bootique.discovery.consul;

import com.cognitree.bootique.discovery.Service;
import com.cognitree.bootique.discovery.DiscoveryException;
import com.cognitree.bootique.discovery.ServiceHealthCheck;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.ConsulException;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.NotRegisteredException;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import com.orbitz.consul.model.health.ServiceHealth;
import com.orbitz.consul.option.ImmutableQueryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Registers a service with consul and starts a runnable to push service status if required by the check
 * <p>
 * For e.g.: TTL check in consul
 * </p>
 */
class ConsulRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(ConsulRegistrar.class);

    private final Map<String, Service> registeredServicesMap = new HashMap<>();
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
    // keeps track of scheduled task for checks which requires their status to be pushed to consul
    private final Map<String, ScheduledFuture> runningTasks = new HashMap<>();

    private AgentClient agentClient;
    private HealthClient healthClient;

    ConsulRegistrar(AgentClient agentClient, HealthClient healthClient) {
        this.agentClient = agentClient;
        this.healthClient = healthClient;
    }

    void registerService(Service service, ServiceHealthCheck healthCheck,
                         ConsulServiceMetaData serviceMetaData) throws DiscoveryException {
        if (isRegistered(service)) {
            logger.error("Service {} already registered with consul", service);
            throw new DiscoveryException("service already registered");
        } else if (!isRegisteredWithConsul(service)) {
            registerService(service, (ConsulServiceHealthCheck) healthCheck, serviceMetaData);
        }
        if (((ConsulServiceHealthCheck) healthCheck).getCheckType() == ConsulServiceHealthCheck.CheckType.ttl) {
            final String ttlString = ((ConsulServiceHealthCheck) healthCheck).getCheck().getTtl().get();
            // In case ttl is set to 30 second the ttl string is represented as "30s" instead of 30
            // re parse the ttl string, check RegCheck for more detail
            long pingInterval = Integer.parseInt(
                    ttlString.substring(0, ttlString.length() - 1)) / 2;
            logger.info("Health check configured for service {} is of type ttl," +
                    " scheduling task at interval of {}s to push service status", service, pingInterval);
            final ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(() ->
                    sendHeartbeat(service, (ConsulServiceHealthCheck) healthCheck), 0, pingInterval, TimeUnit.SECONDS);
            runningTasks.put(service.getId(), scheduledFuture);
        }
    }

    private boolean isRegistered(Service service) {
        return registeredServicesMap.containsKey(service.getId());
    }


    private void registerService(Service service, ConsulServiceHealthCheck healthCheck,
                                 ConsulServiceMetaData serviceMetaData) throws DiscoveryException {
        logger.info("Registering service with Consul. Service name {} Service id {}",
                service.getName(), service.getId());
        if (agentClient != null) {
            final Registration.RegCheck regCheck = healthCheck.getCheck();
            Registration registration = ImmutableRegistration.builder()
                    .id(service.getId())
                    .name(service.getName())
                    .address(service.getAddress())
                    .port(service.getPort())
                    .addChecks(regCheck)
                    .build();

            if (serviceMetaData != null) {
                final ImmutableQueryOptions queryOptions = buildQueryOpt(serviceMetaData);
                agentClient.register(registration, queryOptions);
            } else {
                agentClient.register(registration);
            }
            registeredServicesMap.put(service.getId(), service);
        } else {
            logger.error("Consul not initialized.");
            throw new DiscoveryException("Consul not initialized.");
        }
    }

    private ImmutableQueryOptions buildQueryOpt(ConsulServiceMetaData serviceMetaData) {
        final ImmutableQueryOptions.Builder queryOptBuilder = ImmutableQueryOptions.builder();

        if (serviceMetaData.getDataCenter() != null) {
            queryOptBuilder.datacenter(serviceMetaData.getDataCenter());
        }

        if (serviceMetaData.getNearDataCenter() != null) {
            queryOptBuilder.near(serviceMetaData.getNearDataCenter());
        }

        if (serviceMetaData.getServiceTags() != null) {
            queryOptBuilder.addAllTag(serviceMetaData.getServiceTags());
        }

        if (serviceMetaData.getNodeMetaData() != null) {
            queryOptBuilder.addAllNodeMeta(serviceMetaData.getNodeMetaData());
        }

        return queryOptBuilder.build();
    }

    private void sendHeartbeat(Service service, ConsulServiceHealthCheck healthCheck) {
        logger.info("Sending heartbeat.");
        try {
            final ServiceHealthCheck.ServiceHealthCheckHandler healthCheckHandler =
                    healthCheck.getServiceHealthCheckHandler();
            if (healthCheckHandler != null && healthCheckHandler.isServiceUp()) {
                agentClient.pass(service.getId());
            } else {
                agentClient.fail(service.getId());
            }
        } catch (NotRegisteredException e) {
            logger.warn("Received NotRegisteredException from Consul AgentClient when sending heartbeat. " +
                            "Either the service was never registered or removed from consul due to deregister" +
                            " critical service timeout. Cleaning up service with name {} id {}, scheduler",
                    service.getName(), service.getId());
            registeredServicesMap.remove(service.getId());
            final ScheduledFuture scheduledFuture = runningTasks.get(service.getId());
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }
    }

    private boolean isRegisteredWithConsul(Service service) throws DiscoveryException {
        if (healthClient != null) {
            List<ServiceHealth> serviceInstances;
            try {
                serviceInstances = healthClient
                        .getAllServiceInstances(service.getName()).getResponse();
            } catch (ConsulException e) {
                logger.error("Error retrieving healthy instances from Consul. Cannot determine, if service is " +
                        "already registered. ConsulException: ", e);
                throw new DiscoveryException("Error retrieving healthy instances from Consul", e.getCause());
            }

            final List<Service> services = serviceInstances.stream()
                    .map(this::getConsulService).collect(Collectors.toList());
            if (services.stream().filter(consulService -> consulService.getId().equals(service.getId())).count() > 0)
                return true;
        } else {
            logger.error("Consul not initialized");
            throw new DiscoveryException("Consul not initialized");
        }
        return false;
    }

    private Service getConsulService(ServiceHealth serviceHealth) {
        final com.orbitz.consul.model.health.Service service = serviceHealth.getService();
        return new Service(service.getService(), service.getAddress(), service.getPort());
    }
}
