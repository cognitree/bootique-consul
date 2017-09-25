/*
 * Copyright 2017 Cognitree Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

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
