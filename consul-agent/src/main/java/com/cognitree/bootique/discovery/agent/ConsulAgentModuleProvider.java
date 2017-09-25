package com.cognitree.bootique.discovery.agent;

import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class ConsulAgentModuleProvider implements BQModuleProvider {
    @Override
    public Module module() {
        return new ConsulAgentModule();
    }

    @Override
    public Map<String, Type> configs() {
        return Collections.singletonMap("consul-agent", AgentConfig.class);
    }

    @Override
    public BQModule.Builder moduleBuilder() {
        return BQModuleProvider.super
                .moduleBuilder()
                .description("Integrates consul agent to register service with consul in the application.");
    }
}
