package com.cognitree.bootique.discovery.consul;

import com.cognitree.bootique.discovery.*;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.ConsulException;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.cache.ServiceHealthCache;
import com.orbitz.consul.model.health.ServiceHealth;
import com.orbitz.consul.option.ImmutableQueryOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A discovery service implementation to register and query services from consul
 */
public class ConsulDiscoveryService implements DiscoveryService {
    private static final Logger logger = LoggerFactory.getLogger(ConsulDiscoveryService.class);
    private static final long DEFAULT_WATCH_WAIT_TIMEOUT = 30;

    private final AgentClient agentClient;
    private final HealthClient healthClient;
    private final ConsulRegistrar consulRegistrar;

    public ConsulDiscoveryService(ConsulDiscoveryServiceConfig consulServiceConfig)
            throws DiscoveryException {
        URL consulAgentUrl = null;
        try {
            consulAgentUrl = new URL(consulServiceConfig.getAgentUrl());
        } catch (MalformedURLException e) {
            logger.warn("Provided Consul Agent URL {} is not valid. Defaulting to http://localhost:8500",
                    consulServiceConfig.getAgentUrl());
            try {
                consulAgentUrl = new URL("http://localhost:8500");
            } catch (MalformedURLException e1) {
                logger.error("Exception building default consul agent url ", e);
            }
        }

        logger.info("Connecting to Consul Agent at: {}", consulAgentUrl);

        final long watchWaitTimeout = consulServiceConfig.getWatchWaitTimeout() != null ? consulServiceConfig.getWatchWaitTimeout()
                : DEFAULT_WATCH_WAIT_TIMEOUT;

        final Consul consul;

        try {
            final Consul.Builder builder = Consul.builder();

            final String userName = consulServiceConfig.getUserName();
            final String password = consulServiceConfig.getPassword();
            if (userName != null &&
                    password != null) {
                builder.withBasicAuth(userName, password);
            }

            consul = builder
                    // TODO set ssl context
                    //.withSslContext(sslContext);
                    .withUrl(consulAgentUrl)
                    .withPing(false)
                    .withReadTimeoutMillis(watchWaitTimeout * 1000)
                    .build();

            consul.agentClient().ping();
        } catch (ConsulException e) {
            logger.error("Error connecting to Consul", e);
            throw new DiscoveryException("Error connecting to Consul", e);
        }

        this.agentClient = consul.agentClient();
        this.healthClient = consul.healthClient();
        this.consulRegistrar = new ConsulRegistrar(agentClient, healthClient);
    }

    /**
     * register service @service with the consul
     *
     * @param service     service to register
     * @param healthCheck type of health check to configure for service
     * @throws DiscoveryException
     */
    @Override
    public void register(Service service, ServiceHealthCheck healthCheck) throws DiscoveryException {
        this.register(service, healthCheck, null);
    }

    /**
     * register service @service with the consul
     *
     * @param service         service to register
     * @param healthCheck     type of health check to configure for service
     * @param serviceMetaData metadata to associate with the service while registering
     * @throws DiscoveryException
     */
    @Override
    public void register(Service service, ServiceHealthCheck healthCheck,
                         ServiceMetaData serviceMetaData) throws DiscoveryException {
        consulRegistrar.registerService(service, healthCheck, (ConsulServiceMetaData) serviceMetaData);
    }

    /**
     * deregister a service from consul
     *
     * @param service service to deregister
     */
    @Override
    public void deregister(Service service) {
        if (agentClient != null) {
            agentClient.deregister(service.getId());
        }
    }

    /**
     * get all healthy service instances from consul with service name @serviceName
     */
    @Override
    public List<Service> getServiceInstances(String serviceName) {
        return getServiceInstances(serviceName, null);
    }

    /**
     * get all healthy service instances from consul with service name @serviceName and having metadata @serviceMetaData
     */
    @Override
    public List<Service> getServiceInstances(String serviceName, ServiceMetaData serviceMetaData) {
        logger.info("Performing service lookup on Consul Agent.");
        List<ServiceHealth> serviceHealths;
        try {
            if (serviceMetaData != null) {
                final ImmutableQueryOptions queryOptions = buildQueryOpt((ConsulServiceMetaData) serviceMetaData);
                serviceHealths = healthClient.getHealthyServiceInstances(serviceName, queryOptions)
                        .getResponse();
            } else {
                serviceHealths = healthClient.getHealthyServiceInstances(serviceName)
                        .getResponse();
            }
            return serviceHealths.stream().map(this::getConsulService).collect(Collectors.toList());
        } catch (ConsulException e) {
            logger.error("Error retrieving healthy service instances from Consul", e);
        }
        return Collections.emptyList();
    }

    private ImmutableQueryOptions buildQueryOpt(ConsulServiceMetaData serviceMetaData) {
        final ImmutableQueryOptions.Builder queryOptBuilder = ImmutableQueryOptions.builder();

        if (serviceMetaData.getDataCenter() != null) {
            queryOptBuilder
                    .datacenter(serviceMetaData.getDataCenter());
        }

        if (serviceMetaData.getNearDataCenter() != null) {
            queryOptBuilder
                    .near(serviceMetaData.getNearDataCenter());
        }


        if (serviceMetaData.getServiceTags() != null) {
            queryOptBuilder
                    .addAllTag(serviceMetaData.getServiceTags());
        }

        if (serviceMetaData.getNodeMetaData() != null) {
            queryOptBuilder
                    .addAllNodeMeta(serviceMetaData.getNodeMetaData());
        }

        return queryOptBuilder
                .build();
    }

    /**
     * register a listener for service with name @serviceName
     * </p>
     * listener will be called if there's an update on service with name {serviceName}
     *
     * @param serviceName     service to register listener to
     * @param serviceListener listener to invoke
     */
    @Override
    public void addServiceListener(String serviceName, ServiceListener serviceListener) throws Exception {
        ServiceHealthCache svHealth = ServiceHealthCache.newCache(healthClient, serviceName);

        svHealth.addListener(updatedServices -> {
            logger.info("Service instances for service {} refreshed.", serviceName);

            final Collection<ServiceHealth> serviceHealths = updatedServices.values();
            final List<Service> services = serviceHealths.stream()
                    .map(this::getConsulService).collect(Collectors.toList());
            serviceListener.notify(services);
        });

        svHealth.start();
    }

    private Service getConsulService(ServiceHealth serviceHealth) {
        final com.orbitz.consul.model.health.Service service = serviceHealth.getService();
        return new Service(service.getService(), service.getAddress(), service.getPort());
    }
}
