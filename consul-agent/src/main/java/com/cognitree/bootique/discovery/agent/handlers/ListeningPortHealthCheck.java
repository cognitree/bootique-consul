package com.cognitree.bootique.discovery.agent.handlers;

import com.cognitree.bootique.discovery.ServiceHealthCheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;

public class ListeningPortHealthCheck implements ServiceHealthCheck.ServiceHealthCheckHandler {
    private static final Logger logger = LoggerFactory.getLogger(ListeningPortHealthCheck.class);

    private final String host;
    private final int port;

    public ListeningPortHealthCheck(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean isServiceUp() {
        try (Socket s = new Socket(host, port)) {
            logger.info("isServiceUp ({}): able to connect to {}:{}...", true, host, port);
            return true;
        } catch (Exception e) {
            logger.info("isServiceUp ({}): unable to connect to {}:{}...", false, host, port);
            return false;
        }
    }
}