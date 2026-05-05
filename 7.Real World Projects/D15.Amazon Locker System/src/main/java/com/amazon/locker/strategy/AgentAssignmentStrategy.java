package com.amazon.locker.strategy;

import java.util.List;

public interface AgentAssignmentStrategy {
    String assignAgent(List<String> availableAgents);
}
