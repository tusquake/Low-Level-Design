package com.resolution.system.model;

import com.resolution.system.model.enums.IssueStatus;
import com.resolution.system.model.enums.IssueType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Issue {
    private String id;
    private String transactionId;
    private IssueType issueType;
    private String subject;
    private String description;
    private String email;
    private IssueStatus status;
    private String resolution;
    private String assignedAgentId;
}
