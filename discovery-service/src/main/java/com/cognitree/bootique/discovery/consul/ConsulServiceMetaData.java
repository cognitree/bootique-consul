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

package com.cognitree.bootique.discovery.consul;

import com.cognitree.bootique.discovery.ServiceMetaData;

import java.util.List;

/**
 * Meta information about a service used while registering and querying a service from Consul
 */
public class ConsulServiceMetaData extends ServiceMetaData {
    private String dataCenter;
    private String nearDataCenter;

    public static MetaDataBuilder builder() {
        return new MetaDataBuilder();
    }

    public String getDataCenter() {
        return dataCenter;
    }

    public void setDataCenter(String dataCenter) {
        this.dataCenter = dataCenter;
    }

    public String getNearDataCenter() {
        return nearDataCenter;
    }

    public void setNearDataCenter(String nearDataCenter) {
        this.nearDataCenter = nearDataCenter;
    }

    public static class MetaDataBuilder {
        private final ConsulServiceMetaData consulServiceMetaData = new ConsulServiceMetaData();

        public MetaDataBuilder dataCenter(String dataCenter) {
            consulServiceMetaData.setDataCenter(dataCenter);
            return this;
        }

        public MetaDataBuilder nearDataCenter(String nearDataCenter) {
            consulServiceMetaData.setNearDataCenter(nearDataCenter);
            return this;
        }

        public MetaDataBuilder serviceTags(List<String> serviceTags) {
            consulServiceMetaData.setServiceTags(serviceTags);
            return this;
        }

        public MetaDataBuilder nodeMetaData(List<String> nodeMetaData) {
            consulServiceMetaData.setNodeMetaData(nodeMetaData);
            return this;
        }

        public ConsulServiceMetaData build() {
            return consulServiceMetaData;
        }
    }
}
