package com.amazon.locker.strategy;

import java.util.List;
import java.util.Random;

public class RandomAgentAssignmentStrategy implements AgentAssignmentStrategy {
    private final Random random = new Random();

    @Override
    public String assignAgent(List<String> availableAgents) {
        if (availableAgents == null || availableAgents.isEmpty()) {
            return null;
        }
        return availableAgents.get(random.nextInt(availableAgents.size()));
    }
}
