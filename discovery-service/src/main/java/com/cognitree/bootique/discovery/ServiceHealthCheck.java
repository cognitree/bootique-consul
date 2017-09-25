package com.cognitree.bootique.discovery;

/**
 * Health check definition used by discovery agent to get the health check handler and determine service health
 */
public interface ServiceHealthCheck {

    ServiceHealthCheckHandler getServiceHealthCheckHandler();

    interface ServiceHealthCheckHandler {
        boolean isServiceUp();
    }
}
