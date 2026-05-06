# Resolution System Design - Interview Guide

## 30-Second Explanation

"I designed a customer resolution system for high-volume transaction platforms like PhonePe. The system manages the lifecycle of customer complaints (Issues) and assigns them to agents based on their specific expertise and availability. It uses the **Strategy Design Pattern** for flexible assignment logic and the **Repository Pattern** for in-memory data persistence. Key features include automated workload management (one issue per agent), a waitlist for busy agents, and a complete resolution history for performance auditing."

---

## Questions to Ask Interviewer

### Functional Requirements
1. What are the issue categories? (Payment, Mutual Fund, Gold, etc.)
2. Can an agent have multiple expertise?
3. How many issues can an agent handle simultaneously? (Requirement: One)
4. What happens if all eligible agents are busy? (Requirement: WAITING status)
5. Do we need to support filters for searching issues?

### Non-Functional Requirements
1. Is thread safety required for concurrent issue creation and assignment?
2. How should we handle distributed environments (multiple app instances)?
3. What is the expected load (issues per second)?

---

## Core Components

### Class Design

```text
IssueService
  - createIssue(transactionId, type, subject, description, email)
  - getIssues(filter)

AgentService
  - addAgent(name, email, expertise)
  - viewAgentsWorkHistory()

AssignmentService (Orchestrator)
  - assignIssue(issueId)
  - completeIssue(issueId, resolution)

Repositories
  - IssueRepo (Map<Id, Issue>)
  - AgentRepo (Map<Id, Agent>)

AssignmentStrategy (Interface)
  - assignIssue(agents, issue)
```

### Enums
```java
IssueType: PAYMENT_RELATED, MUTUAL_FUND_RELATED, GOLD_RELATED, INSURANCE_RELATED
IssueStatus: OPEN, IN_PROGRESS, RESOLVED, WAITING
```

---

## System Flow

### 1. Issue Logging
```text
Customer logs complaint -> IssueService creates Issue with OPEN status -> 
Saved in IssueRepo.
```

### 2. Automated Assignment
```text
AssignmentService triggers assignIssue -> Strategy finds free Agent with expertise -> 
Issue status becomes IN_PROGRESS -> Agent is marked as BUSY.
```

### 3. Resolution
```text
Agent updates status to RESOLVED -> AssignmentService marks Agent as FREE -> 
Agent is now available for next waiting issue.
```

---

## Design Patterns Used

### 1. Strategy Pattern
**AssignmentStrategy**: Decouples the assignment algorithm from the service.
- `DefaultAssignmentStrategy`: Assigns to the first free agent with matching expertise.
- `LoadBalancedStrategy` (Future): Assigns to the agent with the least history of work.

### 2. Repository Pattern
Used for in-memory data management, providing a clean API (`save`, `getById`, `getAll`) for services without exposing the underlying Map structure.

---

## Expected Cross-Questions

### Q1: How do you handle agents with multiple expertise?

**Answer**:
The `Agent` entity uses a `Set<IssueType> expertise`. The `AssignmentStrategy` filters agents using `agent.getExpertise().contains(issue.getIssueType())`.

### Q2: What if an agent is already working on an issue?

**Answer**:
The `Agent` class has an `isFree()` method that checks if `assignedIssueId` is null. The assignment logic strictly filters only free agents.

### Q3: How do you handle the "Waiting" queue?

**Answer**:
If no agent is found, the issue status is updated to `WAITING`. A background task or an event-driven mechanism can re-trigger `assignIssue` when an agent becomes free.

### Q4: How to scale this for millions of issues?

**Answer**:
- **Message Queues**: Use Kafka/RabbitMQ to handle incoming issues and process assignment asynchronously.
- **Distributed Caching**: Use Redis for agent availability states.
- **Database Sharding**: Shard issues by `transactionId` or `customerId`.

---

## SOLID Principles Applied

1.  **SRP**: `IssueService` manages data; `AssignmentService` manages logic.
2.  **OCP**: New assignment strategies can be added without modifying the service.
3.  **LSP**: Any implementation of `AssignmentStrategy` is interchangeable.
4.  **ISP**: Repository interfaces provide only necessary data access methods.
5.  **DIP**: Services depend on the `AssignmentStrategy` interface.

---

## Summary - One Line Answer

"I designed a resolution system using Strategy and Repository patterns to automate issue assignment to agents based on expertise and availability, ensuring efficient workload management and auditability."
