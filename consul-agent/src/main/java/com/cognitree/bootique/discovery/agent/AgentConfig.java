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
