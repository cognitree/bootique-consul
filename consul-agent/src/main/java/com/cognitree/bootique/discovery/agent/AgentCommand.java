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

package com.cognitree.bootique.discovery.agent;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.bootique.cli.Cli;
import io.bootique.command.CommandOutcome;
import io.bootique.command.CommandWithMetadata;
import io.bootique.meta.application.CommandMetadata;

public class AgentCommand extends CommandWithMetadata {

    private final Provider<ServiceRegistrar> serviceRegistrar;

    @Inject
    public AgentCommand(Provider<ServiceRegistrar> serviceRegistrar) {
        super(createMetadata());
        this.serviceRegistrar = serviceRegistrar;
    }

    private static CommandMetadata createMetadata() {
        return CommandMetadata.builder("consul-agent").description("Starts consul agent " +
                "registering services defined in config file.").build();
    }

    @Override
    public CommandOutcome run(Cli cli) {
        serviceRegistrar.get().register();
        return CommandOutcome.succeeded();
    }
}
