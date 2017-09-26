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

import com.cognitree.bootique.discovery.consul.ConsulDiscoveryService;
import com.cognitree.bootique.discovery.consul.ConsulDiscoveryServiceConfig;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;

public class ConsulDiscoveryModule extends ConfigModule {

    @Singleton
    @Provides
    ConsulDiscoveryService createConsulDiscoveryService(ConfigurationFactory configurationFactory) throws DiscoveryException {
        final ConsulDiscoveryServiceConfig agentConfig = configurationFactory.config(ConsulDiscoveryServiceConfig.class, "discovery");
        return new ConsulDiscoveryService(agentConfig);
    }
}
