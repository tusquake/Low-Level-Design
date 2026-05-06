package com.resolution.system.service;

import com.resolution.system.model.Agent;
import com.resolution.system.model.enums.IssueType;
import com.resolution.system.repository.AgentRepo;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AgentService {
    private final AgentRepo agentRepo;

    public AgentService(AgentRepo agentRepo) {
        this.agentRepo = agentRepo;
    }

    public Agent addAgent(String email, String name, Set<IssueType> expertise) {
        Agent agent = Agent.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .name(name)
                .expertise(expertise)
                .build();
        agentRepo.save(agent);
        return agent;
    }

    public void viewAgentsWorkHistory() {
        Collection<Agent> agents = agentRepo.getAll();
        for (Agent agent : agents) {
            System.out.println("Agent: " + agent.getName() + " (" + agent.getEmail() + ")");
            System.out.println("Expertise: " + agent.getExpertise());
            System.out.println("Work History: " + agent.getHistory());
            System.out.println("-----------------------------------");
        }
    }
}
