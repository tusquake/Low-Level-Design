package com.resolution.system.strategy;

import com.resolution.system.model.Agent;
import com.resolution.system.model.Issue;
import java.util.Collection;
import java.util.Optional;

public class DefaultAssignmentStrategy implements AssignmentStrategy {
    @Override
    public Optional<Agent> assignIssue(Collection<Agent> agents, Issue issue) {
        return agents.stream()
                .filter(Agent::isFree)
                .filter(agent -> agent.getExpertise().contains(issue.getIssueType()))
                .findFirst();
    }
}
