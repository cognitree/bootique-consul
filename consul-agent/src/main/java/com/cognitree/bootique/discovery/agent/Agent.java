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
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;

import java.util.Properties;

@BQConfig("Configure service to register with consul")
public class Agent {

    public enum CheckType {
        ttl, http, tcp
    }

    private Service service;
    private long checkInterval;
    private CheckType checkType;
    private Properties checkProperties = new Properties();

    public Service getService() {
        return service;
    }

    @BQConfigProperty("service definition must include name, address and port field")
    public void setService(Service service) {
        this.service = service;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    @BQConfigProperty("service health check interval in seconds")
    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    @BQConfigProperty("service check type to register with consul")
    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public Properties getCheckProperties() {
        return checkProperties;
    }

    @BQConfigProperty("properties specific to check type")
    public void setCheckProperties(Properties checkProperties) {
        this.checkProperties = checkProperties;
    }
}
