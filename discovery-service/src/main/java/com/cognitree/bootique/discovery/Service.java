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

import com.fasterxml.jackson.annotation.JsonProperty;

public class Service {
    private String name;
    private String id;
    private String address;
    private int port;

    public Service(@JsonProperty("name") String name,
                   @JsonProperty("address") String address,
                   @JsonProperty("port") int port) {
        this.name = name;
        this.address = address;
        this.port = port;
        this.id = name + "-" + address + "-" + port;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "Service{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", port=" + port +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Service service = (Service) o;

        if (port != service.port) return false;
        if (name != null ? !name.equals(service.name) : service.name != null) return false;
        if (id != null ? !id.equals(service.id) : service.id != null) return false;
        return address != null ? address.equals(service.address) : service.address == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + port;
        return result;
    }
}
