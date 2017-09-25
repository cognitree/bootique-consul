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
