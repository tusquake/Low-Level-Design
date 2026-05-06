package com.resolution.system;

import com.resolution.system.model.Agent;
import com.resolution.system.model.Issue;
import com.resolution.system.model.enums.IssueType;
import com.resolution.system.repository.AgentRepo;
import com.resolution.system.repository.IssueRepo;
import com.resolution.system.service.AgentService;
import com.resolution.system.service.AssignmentService;
import com.resolution.system.service.IssueService;
import com.resolution.system.strategy.DefaultAssignmentStrategy;
import java.util.Set;

public class ResolutionSystemDemo {
    public static void main(String[] args) {
        // 1. Setup Infrastructure
        IssueRepo issueRepo = new IssueRepo();
        AgentRepo agentRepo = new AgentRepo();
        
        IssueService issueService = new IssueService(issueRepo);
        AgentService agentService = new AgentService(agentRepo);
        AssignmentService assignmentService = new AssignmentService(issueRepo, agentRepo, new DefaultAssignmentStrategy());

        System.out.println("--- Resolution System Demo ---");

        // 2. Add Agents
        System.out.println("Step 1: Adding Agents with expertise...");
        Agent agent1 = agentService.addAgent("agent1@phonepe.com", "Rahul", Set.of(IssueType.PAYMENT_RELATED, IssueType.GOLD_RELATED));
        Agent agent2 = agentService.addAgent("agent2@phonepe.com", "Sneha", Set.of(IssueType.MUTUAL_FUND_RELATED, IssueType.INSURANCE_RELATED));
        
        // 3. Create Issues
        System.out.println("\nStep 2: Customers creating issues...");
        Issue issue1 = issueService.createIssue("T101", IssueType.PAYMENT_RELATED, "Payment Failed", "Money deducted but not credited", "cust1@gmail.com");
        Issue issue2 = issueService.createIssue("T102", IssueType.MUTUAL_FUND_RELATED, "MF Purchase Error", "Unable to buy SIP", "cust2@gmail.com");
        Issue issue3 = issueService.createIssue("T103", IssueType.GOLD_RELATED, "Gold Delivery", "Gold not delivered yet", "cust3@gmail.com");

        // 4. Assign Issues
        System.out.println("\nStep 3: Assigning issues...");
        assignmentService.assignIssue(issue1.getId()); // Should go to Rahul
        assignmentService.assignIssue(issue2.getId()); // Should go to Sneha
        assignmentService.assignIssue(issue3.getId()); // Rahul is busy, should wait

        // 5. Resolve an issue to free up an agent
        System.out.println("\nStep 4: Resolving issue 1...");
        assignmentService.completeIssue(issue1.getId(), "Payment reversed successfully.");

        // 6. Try assigning the waiting issue again
        System.out.println("\nStep 5: Re-assigning waiting gold issue...");
        assignmentService.assignIssue(issue3.getId()); // Now Rahul is free

        // 7. View Work History
        System.out.println("\nStep 6: Viewing Agent Work History...");
        agentService.viewAgentsWorkHistory();
    }
}
