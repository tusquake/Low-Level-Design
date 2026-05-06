package com.resolution.system.repository;

import com.resolution.system.model.Agent;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AgentRepo {
    private final Map<String, Agent> agents = new ConcurrentHashMap<>();

    public void save(Agent agent) {
        agents.put(agent.getId(), agent);
    }

    public Optional<Agent> getById(String id) {
        return Optional.ofNullable(agents.get(id));
    }

    public Collection<Agent> getAll() {
        return agents.values();
    }
}
