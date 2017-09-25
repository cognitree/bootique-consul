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
