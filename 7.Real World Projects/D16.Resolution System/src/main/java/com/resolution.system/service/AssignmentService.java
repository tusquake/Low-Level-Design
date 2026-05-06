package com.resolution.system.service;

import com.resolution.system.model.Agent;
import com.resolution.system.model.Issue;
import com.resolution.system.model.enums.IssueStatus;
import com.resolution.system.repository.AgentRepo;
import com.resolution.system.repository.IssueRepo;
import com.resolution.system.strategy.AssignmentStrategy;
import java.util.Optional;

public class AssignmentService {
    private final IssueRepo issueRepo;
    private final AgentRepo agentRepo;
    private AssignmentStrategy strategy;

    public AssignmentService(IssueRepo issueRepo, AgentRepo agentRepo, AssignmentStrategy strategy) {
        this.issueRepo = issueRepo;
        this.agentRepo = agentRepo;
        this.strategy = strategy;
    }

    public void setStrategy(AssignmentStrategy strategy) {
        this.strategy = strategy;
    }

    public void assignIssue(String issueId) {
        Optional<Issue> issueOpt = issueRepo.getById(issueId);
        if (issueOpt.isEmpty()) {
            System.out.println("Issue not found: " + issueId);
            return;
        }

        Issue issue = issueOpt.get();
        if (issue.getStatus() != IssueStatus.OPEN && issue.getStatus() != IssueStatus.WAITING) {
            System.out.println("Issue already assigned or resolved: " + issueId);
            return;
        }

        Optional<Agent> agentOpt = strategy.assignIssue(agentRepo.getAll(), issue);
        if (agentOpt.isPresent()) {
            Agent agent = agentOpt.get();
            
            // Assigning
            agent.setAssignedIssueId(issue.getId());
            issue.setAssignedAgentId(agent.getId());
            issue.setStatus(IssueStatus.IN_PROGRESS);
            
            // Sync history/state
            agent.getHistory().add(issue.getId());
            
            agentRepo.save(agent);
            issueRepo.save(issue);
            
            System.out.println("Issue " + issueId + " assigned to Agent " + agent.getName());
        } else {
            issue.setStatus(IssueStatus.WAITING);
            issueRepo.save(issue);
            System.out.println("No free agent available with expertise for issue " + issueId + ". Marked as WAITING.");
        }
    }

    public void completeIssue(String issueId, String resolution) {
        issueRepo.getById(issueId).ifPresent(issue -> {
            issue.setStatus(IssueStatus.RESOLVED);
            issue.setResolution(resolution);
            
            if (issue.getAssignedAgentId() != null) {
                agentRepo.getById(issue.getAssignedAgentId()).ifPresent(agent -> {
                    agent.setAssignedIssueId(null); // Agent is free now
                    agentRepo.save(agent);
                });
            }
            issueRepo.save(issue);
            System.out.println("Issue " + issueId + " resolved.");
        });
    }
}
