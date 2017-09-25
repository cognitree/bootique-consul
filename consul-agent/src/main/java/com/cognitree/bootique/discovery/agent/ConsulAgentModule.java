package com.cognitree.bootique.discovery.agent;

import com.cognitree.bootique.discovery.DiscoveryException;
import com.google.inject.Binder;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.BQCoreModule;
import io.bootique.ConfigModule;
import io.bootique.config.ConfigurationFactory;

public class ConsulAgentModule extends ConfigModule {

    @Override
    public void configure(Binder binder) {
        BQCoreModule.extend(binder).addCommand(AgentCommand.class);
    }

    @Singleton
    @Provides
    AgentConfig createAgentConfig(ConfigurationFactory configurationFactory) throws DiscoveryException {
        return configurationFactory.config(AgentConfig.class, "consul-agent");
    }
}
