package com.cognitree.bootique.discovery;

import com.cognitree.bootique.discovery.consul.ConsulDiscoveryServiceConfig;
import com.google.inject.Module;
import io.bootique.BQModule;
import io.bootique.BQModuleProvider;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class ConsulDiscoveryModuleProvider implements BQModuleProvider {
    @Override
    public Module module() {
        return new ConsulDiscoveryModule();
    }

    @Override
    public Map<String, Type> configs() {
        return Collections.singletonMap("discovery", ConsulDiscoveryServiceConfig.class);
    }

    @Override
    public BQModule.Builder moduleBuilder() {
        return BQModuleProvider.super
                .moduleBuilder()
                .description("Integrates service discovery via consul in the application.");
    }
}
