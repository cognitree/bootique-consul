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


import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;

@BQConfig("Configures consul discovery service")
public class ConsulDiscoveryServiceConfig {
    private String agentUrl;
    private String userName;
    private String password;
    private Long watchWaitTimeout;

    public String getAgentUrl() {
        return agentUrl;
    }

    @BQConfigProperty("consul agent to connect, defaults to http://localhost:8500")
    public void setAgentUrl(String agentUrl) {
        this.agentUrl = agentUrl;
    }

    public String getUserName() {
        return userName;
    }

    @BQConfigProperty("user name to use for basic authentication")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    @BQConfigProperty("password to use for basic authentication")
    public void setPassword(String password) {
        this.password = password;
    }

    public Long getWatchWaitTimeout() {
        return watchWaitTimeout;
    }

    @BQConfigProperty("consul read timeout for OkHttpClient in seconds")
    public void setWatchWaitTimeout(Long watchWaitTimeout) {
        this.watchWaitTimeout = watchWaitTimeout;
    }
}
