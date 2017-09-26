A standalone agent which can be used to register third party services like Kafka, Mongodb, Cassandra etc.. and configure a health check for the same. 

In most cases the service will register itself and configure a health check to update the status in consul (using [Bootique service discovery module](../discovery-service)), but in some cases application (service like Kafka, Cassandra etc) won't be able to register itself with consul. 
Lets say for Kafka one need to run a separate process just to register kafka broker with consul and enable health check to update the status. 
With this module there is no need to write code to register service and configure a health check for this application. 
The agent will take care of registering a service with a defined check and will keep the service status updated with consul

### USAGE

Add a section **discovery** to configure consul discovery service module and **consul-agent** to configure agent to register services in config.yaml file.

```
discovery:
  agentUrl: http://localhost:8500
  
consul-agent :
  agents:
    - service:
        name : kafka
        address : localhost
        port : 9092
      checkType : ttl
      checkInterval : 30
      checkProperties:
        statusCheck: listeningPort
    - service:
        name : mongo
        address : localhost
        port : 27017
      checkType : ttl
      checkInterval : 30
```

Here we are registering kafka and mongo services in consul with health check type as ttl, `checkProperties` defines properties specific to a check type. See below for available options

#### Sample agent config

**For ttl check**

```
consul-agent :
  agents:
    - service:
        name : servicename
        address : localhost
        port : 8080
      checkType : ttl
      checkInterval : 30
      checkProperties:
        # type of check to perform to get the service status. 
        #Currently we only support listeningPort (check if service port is open) which is set by default
        statusCheck : listeningPort
```

**For http check**

```
consul-agent :
  agents:
    - service:
        name : servicename
        address : localhost
        port : 8080
      checkType : ttl
      checkInterval : 30
      checkProperties:
        # url to get the service status, used by consul to check the service status (Mandatory)
        url : http://example.com
```

**For tcp check**

```
consul-agent :
  agents:
    - service:
        name : servicename
        address : localhost
        port : 8080
      checkType : ttl
      checkInterval : 30
      checkProperties:
        # tcp url to get the service status, used by consul to check the service status (Mandatory)
        tcp : example.com:22
```

### Running the agent

```
mvn clean package
java -jar consul-agent-1.0-jar-with-dependencies.jar --consul-agent --config=config.yaml
```