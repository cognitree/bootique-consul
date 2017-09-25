package com.cognitree.bootique.discovery;

import java.util.List;

public interface ServiceListener {
    void notify(List<Service> services);
}
