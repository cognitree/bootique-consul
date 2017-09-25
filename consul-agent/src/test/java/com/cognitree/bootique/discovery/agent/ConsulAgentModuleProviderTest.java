package com.cognitree.bootique.discovery.agent;

import io.bootique.test.junit.BQModuleProviderChecker;
import org.junit.Test;

public class ConsulAgentModuleProviderTest {

    @Test
    public void testPresentInJar() {
        BQModuleProviderChecker.testPresentInJar(ConsulAgentModuleProvider.class);
    }

    @Test
    public void testMetadata() {
        BQModuleProviderChecker.testMetadata(ConsulAgentModuleProvider.class);
    }
}
