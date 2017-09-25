Bootique module to enable service discovery in your application via consul.

#### USAGE

Include the BOMs and then Bootique Consul discovery service

```
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>io.bootique.bom</groupId>
                    <artifactId>bootique-bom</artifactId>
                    <version>0.19</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
        </dependencyManagement>
        ...
        <dependency>
            <groupId>com.cognitree.bootique.consul</groupId>
            <artifactId>discovery-service</artifactId>
            <version>1.0</version>
        </dependency>
```

Configure parameters in the YAML by adding a section **discovery** to configure consul discovery service

```
discovery:
  agentUrl: http://localhost:8500
  userName : "guest"
  password : "password"
  watchWaitTimeout: 30
```

Now you can inject `com.cognitree.bootique.discovery.consul.ConsulDiscoveryService` instance in your application code to register/ query service instance.

```
    @Inject
    public ConsulDiscoveryService consulDiscoveryService;
```

#### Registering a service with consul

```   
        // create a check for service
        final ServiceHealthCheck ttlCheck = ConsulServiceHealthCheck.createTtlCheck(30, () -> true);
        // service to register
        Service service = new Service("test", "localhost", 8080);
        // register service
        consulDiscoveryService.register(service, ttlCheck);
```

#### Querying all healthy services from consul

```
final List<Service> test = consulDiscoveryService.getServiceInstances("test");
```

Refer [ConsulDiscoveryService](src/main/java/com/cognitree/bootique/discovery/consul/ConsulDiscoveryService.java) for available API to query and register services. For sample refer [ConsulDiscoveryServiceTest](src/test/java/com/cognitree/discovery/consul/ConsulDiscoveryServiceTest.java)