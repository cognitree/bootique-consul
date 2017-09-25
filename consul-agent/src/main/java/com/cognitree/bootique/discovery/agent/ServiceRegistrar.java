/*
 * Copyright 2017 Cognitree Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.cognitree.bootique.discovery.agent;

import com.cognitree.bootique.discovery.Service;
import com.cognitree.bootique.discovery.ServiceHealthCheck;
import com.cognitree.bootique.discovery.agent.handlers.ListeningPortHealthCheck;
import com.cognitree.bootique.discovery.consul.ConsulDiscoveryService;
import com.cognitree.bootique.discovery.consul.ConsulServiceHealthCheck;
import com.cognitree.bootique.discovery.DiscoveryException;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

/**
 * register all the services defined by agent config with the consul
 */
public class ServiceRegistrar {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistrar.class);
    @Inject
    private AgentConfig agentConfig;
    @Inject
    private ConsulDiscoveryService consulDiscoveryService;

    public void register() {
        final List<Agent> agents = agentConfig.getAgents();
        for (Agent agent : agents) {
            final Service service = agent.getService();
            final Agent.CheckType checkType = agent.getCheckType();
            final long checkInterval = agent.getCheckInterval();
            final Properties checkProperties = agent.getCheckProperties();
            logger.info("Registering service {} with check type {} having check interval {}, check properties {}",
                    service, checkType, checkInterval, checkProperties);
            registerService(service, checkInterval, checkType, checkProperties);
        }
    }

    private void registerService(Service service, long checkInterval,
                                 Agent.CheckType checkType, Properties checkProperties) {
        final ServiceHealthCheck healthCheck;
        switch (checkType) {
            case ttl:
                final String statusCheck = checkProperties.getProperty("statusCheck", "listeningPort");
                ServiceHealthCheck.ServiceHealthCheckHandler serviceHealthCheckHandler;
                switch (statusCheck) {
                    case "listeningPort":
                    default:
                        serviceHealthCheckHandler
                                = new ListeningPortHealthCheck(service.getAddress(), service.getPort());
                        break;
                }
                healthCheck = ConsulServiceHealthCheck.createTtlCheck(checkInterval, serviceHealthCheckHandler);
                break;
            case http:
                final String url = checkProperties.getProperty("url");
                healthCheck = ConsulServiceHealthCheck.createHttpCheck(url, checkInterval);
                break;
            case tcp:
                final String tcp = checkProperties.getProperty("tcp");
                healthCheck = ConsulServiceHealthCheck.createTcpCheck(tcp, checkInterval);
                break;
            default:
                healthCheck = null;
        }

        if (healthCheck != null) {
            try {
                consulDiscoveryService.register(service, healthCheck);
            } catch (DiscoveryException e) {
                logger.error("Error registering service {} with consul", service, e);
            }
        } else {
            logger.error("No health check found with type {}", checkType);
        }
    }
}
