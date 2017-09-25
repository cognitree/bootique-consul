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
