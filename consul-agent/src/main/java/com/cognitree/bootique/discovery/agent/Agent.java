package com.cognitree.bootique.discovery.agent;

import com.cognitree.bootique.discovery.Service;
import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;

import java.util.Properties;

@BQConfig("Configure service to register with consul")
public class Agent {

    public enum CheckType {
        ttl, http, tcp
    }

    private Service service;
    private long checkInterval;
    private CheckType checkType;
    private Properties checkProperties = new Properties();

    public Service getService() {
        return service;
    }

    @BQConfigProperty("service definition must include name, address and port field")
    public void setService(Service service) {
        this.service = service;
    }

    public long getCheckInterval() {
        return checkInterval;
    }

    @BQConfigProperty("service health check interval in seconds")
    public void setCheckInterval(long checkInterval) {
        this.checkInterval = checkInterval;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    @BQConfigProperty("service check type to register with consul")
    public void setCheckType(CheckType checkType) {
        this.checkType = checkType;
    }

    public Properties getCheckProperties() {
        return checkProperties;
    }

    @BQConfigProperty("properties specific to check type")
    public void setCheckProperties(Properties checkProperties) {
        this.checkProperties = checkProperties;
    }
}
