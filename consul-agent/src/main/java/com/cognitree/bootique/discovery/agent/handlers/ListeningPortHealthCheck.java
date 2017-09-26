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