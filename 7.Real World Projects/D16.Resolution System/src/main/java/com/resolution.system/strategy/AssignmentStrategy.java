package com.resolution.system.strategy;

import com.resolution.system.model.Agent;
import com.resolution.system.model.Issue;
import java.util.Collection;
import java.util.Optional;

public interface AssignmentStrategy {
    Optional<Agent> assignIssue(Collection<Agent> agents, Issue issue);
}
