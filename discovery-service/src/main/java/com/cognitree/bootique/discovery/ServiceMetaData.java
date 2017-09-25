package com.cognitree.bootique.discovery;

import java.util.List;

/**
 * Meta information about a service used while registering and querying a service from discovery agent
 */
public class ServiceMetaData {
    private List<String> serviceTags;
    private List<String> nodeMetaData;

    public List<String> getServiceTags() {
        return serviceTags;
    }

    public void setServiceTags(List<String> serviceTags) {
        this.serviceTags = serviceTags;
    }

    public List<String> getNodeMetaData() {
        return nodeMetaData;
    }

    public void setNodeMetaData(List<String> nodeMetaData) {
        this.nodeMetaData = nodeMetaData;
    }
}
