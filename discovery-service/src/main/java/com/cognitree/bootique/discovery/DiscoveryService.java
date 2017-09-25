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

package com.cognitree.bootique.discovery;

import java.util.List;

public interface DiscoveryService {

    /**
     * register a service @service with the discovery agent
     *
     * @param service           service to register
     * @param healthCheck       type of health check to configure for service
     * @throws DiscoveryException
     */
    void register(Service service, ServiceHealthCheck healthCheck) throws DiscoveryException;

    /**
     * register a service @service with the discovery agent
     *
     * @param service           service to register
     * @param healthCheck       type of health check to configure for service
     * @param serviceMetaData   metadata to associate with the service while registering
     * @throws DiscoveryException
     */
    void register(Service service, ServiceHealthCheck healthCheck,
                  ServiceMetaData serviceMetaData) throws DiscoveryException;

    /**
     * deregister a service
     *
     * @param service service to deregister
     */
    void deregister(Service service);

    /**
     * get all healthy service instances from discovery agent with service name @serviceName
     */
    List<Service> getServiceInstances(String serviceName);

    /**
     * get all healthy service instances from discovery agent with service name @serviceName having metadata @serviceMetaData
     */

    List<Service> getServiceInstances(String serviceName, ServiceMetaData serviceMetaData);

    /**
     * register a listener for service with name @serviceName
     * </p>
     * listener will be called if there's an update on service with name {serviceName}
     *
     * @param serviceName     service to register listener to
     * @param serviceListener listener to invoke
     */
    void addServiceListener(String serviceName, ServiceListener serviceListener) throws Exception;

}
