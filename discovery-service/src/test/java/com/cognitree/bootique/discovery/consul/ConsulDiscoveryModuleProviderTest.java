package com.cognitree.bootique.discovery.consul;

import com.cognitree.bootique.discovery.ConsulDiscoveryModuleProvider;
import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class ConsulDiscoveryModuleProviderTest {

    @Test
    public void testPresentInJar() {
        BQModuleProviderChecker.testPresentInJar(ConsulDiscoveryModuleProvider.class);
    }

    @Test
    public void testMetadata() {
        BQModuleProviderChecker.testMetadata(ConsulDiscoveryModuleProvider.class);
    }
}
