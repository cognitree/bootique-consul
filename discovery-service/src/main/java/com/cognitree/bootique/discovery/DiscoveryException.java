package com.cognitree.bootique.discovery;

public class DiscoveryException extends Exception {
    public DiscoveryException() {
        super();
    }

    public DiscoveryException(String message) {
        super(message);
    }

    public DiscoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
