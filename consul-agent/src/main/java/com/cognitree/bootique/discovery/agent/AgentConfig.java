package com.cognitree.bootique.discovery.agent;

import io.bootique.annotation.BQConfig;
import io.bootique.annotation.BQConfigProperty;

import java.util.ArrayList;
import java.util.List;

@BQConfig("Configure service to register with consul")
public class AgentConfig {
    private List<Agent> agents = new ArrayList<>();

    public List<Agent> getAgents() {
        return agents;
    }

    @BQConfigProperty
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }
}
