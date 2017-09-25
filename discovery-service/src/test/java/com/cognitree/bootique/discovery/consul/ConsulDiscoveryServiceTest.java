package com.cognitree.bootique.discovery.consul;

import com.cognitree.bootique.discovery.Service;
import com.cognitree.bootique.discovery.ServiceHealthCheck;
import com.cognitree.bootique.discovery.ConsulDiscoveryModule;
import com.cognitree.bootique.discovery.DiscoveryException;
import io.bootique.BQRuntime;
import io.bootique.test.junit.BQTestFactory;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test the consul discovery service
 * <p>
 * Note: It may behave differently if service with name "test" is already exists in the consul
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConsulDiscoveryServiceTest {

    public ConsulDiscoveryService consulDiscoveryService;

    @Rule
    public BQTestFactory testFactory = new BQTestFactory();

    @Before
    public void setup() {
        BQRuntime runtime = testFactory
                .app("--config=classpath:config.yml")
                .module(ConsulDiscoveryModule.class)
                .createRuntime();

        consulDiscoveryService = runtime.getInstance(ConsulDiscoveryService.class);
    }

    @Test
    public void testARegisterService() {
        final ServiceHealthCheck ttlCheck = ConsulServiceHealthCheck.createTtlCheck(30, () -> true);
        try {
            Service service = new Service("test", "localhost", 8080);
            consulDiscoveryService.register(service, ttlCheck);
        } catch (DiscoveryException e) {
            Assert.fail();
        }
    }

    @Test
    public void testBGetRegisteredService() {
        final List<Service> test = consulDiscoveryService.getServiceInstances("test");
        Assert.assertEquals(1, test.size());
        Service service = new Service("test", "localhost", 8080);
        Assert.assertEquals(service, test.get(0));
    }

    @Test
    public void testBSetServiceListener() throws Exception {
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        consulDiscoveryService.addServiceListener("test", (services) -> {
            atomicInteger.incrementAndGet();
        });
        try {
            Service service = new Service("test", "192.168.1.1", 8080);
            final ServiceHealthCheck ttlCheck = ConsulServiceHealthCheck.createTtlCheck(30, () -> true);
            consulDiscoveryService.register(service, ttlCheck);
        } catch (DiscoveryException e) {
            Assert.fail();
        }
        // Add a sleep for service listener to be invoked
        Thread.sleep(1000);
        System.out.println(atomicInteger.get());
    }

    @Test
    public void testCDeregisterService() throws Exception {
        List<Service> services = consulDiscoveryService.getServiceInstances("test");
        Assert.assertEquals(2, services.size());

        Service service = new Service("test", "192.168.1.1", 8080);
        consulDiscoveryService.deregister(service);

        services = consulDiscoveryService.getServiceInstances("test");
        // validate if registered service count is back to 1 and also the service registered is having address as localhost
        Assert.assertEquals(1, services.size());
        Service oldService = new Service("test", "localhost", 8080);
        Assert.assertEquals(oldService, services.get(0));
    }

}
