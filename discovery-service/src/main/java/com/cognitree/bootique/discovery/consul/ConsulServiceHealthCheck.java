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

package com.cognitree.bootique.discovery.consul;

import com.cognitree.bootique.discovery.Service;
import com.cognitree.bootique.discovery.ServiceHealthCheck;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.Registration;

import static com.orbitz.consul.model.agent.Registration.RegCheck;

/**
 * Health check definition used by consul to get the health check handler and determine service health
 * <p>
 * Supported Checks : ttl, http, tcp
 * </p>
 */
public class ConsulServiceHealthCheck implements ServiceHealthCheck {

    private CheckType checkType;
    private ServiceHealthCheckHandler serviceHealthCheckHandler;
    private RegCheck check;

    private ConsulServiceHealthCheck(CheckType checkType, RegCheck check) {
        this.checkType = checkType;
        this.check = check;
    }

    /**
     * Creates a ttl check in consul
     *
     * @param ttl                       time in seconds to keep the service as healthy
     * @param serviceHealthCheckHandler the handler to be invoked by discovery service to check the service health.
     *                                  The time interval to invoke the handler is configured while registering a service.
     *                                  Check @{@link ConsulDiscoveryService#register(Service, ServiceHealthCheck)}
     *                                  for more details
     * @return
     */
    public static ServiceHealthCheck createTtlCheck(long ttl, ServiceHealthCheckHandler serviceHealthCheckHandler) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .ttl(String.format("%ss", ttl)).build();
        final ConsulServiceHealthCheck healthCheck = new ConsulServiceHealthCheck(CheckType.ttl, check);
        healthCheck.serviceHealthCheckHandler = serviceHealthCheckHandler;
        return healthCheck;
    }

    /**
     * Creates a ttl check in consul
     *
     * @param ttl                            time in seconds to keep the service as healthy
     * @param deregisterCriticalServiceAfter time after which to remove a service if unhealthy
     * @param serviceHealthCheckHandler      the handle to be invoked by discovery service to check the service health
     * @return
     */
    public static ServiceHealthCheck createTtlCheck(long ttl, long deregisterCriticalServiceAfter,
                                                    ServiceHealthCheckHandler serviceHealthCheckHandler) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .ttl(String.format("%ss", ttl))
                .deregisterCriticalServiceAfter(String.format("%ss", deregisterCriticalServiceAfter))
                .build();
        final ConsulServiceHealthCheck healthCheck = new ConsulServiceHealthCheck(CheckType.ttl, check);
        healthCheck.serviceHealthCheckHandler = serviceHealthCheckHandler;
        return healthCheck;
    }

    public static ServiceHealthCheck createHttpCheck(String url, long interval) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .http(url)
                .interval(String.format("%ss", interval))
                .build();
        return new ConsulServiceHealthCheck(CheckType.http, check);
    }

    public static ServiceHealthCheck createHttpCheck(String url, long interval, long timeout) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .http(url)
                .interval(String.format("%ss", interval))
                .timeout(String.format("%ss", timeout))
                .build();
        return new ConsulServiceHealthCheck(CheckType.http, check);
    }

    public static ServiceHealthCheck createHttpCheck(String url, long interval,
                                                     long timeout, long deregisterCriticalServiceAfter) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .http(url)
                .interval(String.format("%ss", interval))
                .timeout(String.format("%ss", timeout))
                .deregisterCriticalServiceAfter(String.format("%ss", deregisterCriticalServiceAfter))
                .build();
        return new ConsulServiceHealthCheck(CheckType.http, check);
    }

    public static ServiceHealthCheck createTcpCheck(String tcp, long interval) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .tcp(tcp)
                .interval(String.format("%ss", interval))
                .build();
        return new ConsulServiceHealthCheck(CheckType.tcp, check);
    }

    public static ServiceHealthCheck createTcpCheck(String tcp, long interval, long timeout) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .http(tcp)
                .interval(String.format("%ss", interval))
                .timeout(String.format("%ss", timeout))
                .build();
        return new ConsulServiceHealthCheck(CheckType.tcp, check);
    }

    public static ServiceHealthCheck createTcpCheck(String tcp, long interval,
                                                    long timeout, long deregisterCriticalServiceAfter) {
        final Registration.RegCheck check = ImmutableRegCheck.builder()
                .http(tcp)
                .interval(String.format("%ss", interval))
                .timeout(String.format("%ss", timeout))
                .deregisterCriticalServiceAfter(String.format("%ss", deregisterCriticalServiceAfter))
                .build();
        return new ConsulServiceHealthCheck(CheckType.tcp, check);
    }


    CheckType getCheckType() {
        return checkType;
    }

    public ServiceHealthCheckHandler getServiceHealthCheckHandler() {
        return serviceHealthCheckHandler;
    }

    RegCheck getCheck() {
        return check;
    }

    protected enum CheckType {ttl, http, tcp}
}