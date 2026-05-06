package com.resolution.system.service;

import com.resolution.system.model.Issue;
import com.resolution.system.model.enums.IssueStatus;
import com.resolution.system.model.enums.IssueType;
import com.resolution.system.repository.IssueRepo;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class IssueService {
    private final IssueRepo issueRepo;

    public IssueService(IssueRepo issueRepo) {
        this.issueRepo = issueRepo;
    }

    public Issue createIssue(String transactionId, IssueType issueType, String subject, String description, String email) {
        Issue issue = Issue.builder()
                .id(UUID.randomUUID().toString())
                .transactionId(transactionId)
                .issueType(issueType)
                .subject(subject)
                .description(description)
                .email(email)
                .status(IssueStatus.OPEN)
                .build();
        issueRepo.save(issue);
        return issue;
    }

    public Collection<Issue> getIssues(IssueStatus status) {
        return issueRepo.getAll().stream()
                .filter(i -> i.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void updateIssue(String issueId, IssueStatus status, String resolution) {
        issueRepo.getById(issueId).ifPresent(issue -> {
            issue.setStatus(status);
            if (resolution != null) {
                issue.setResolution(resolution);
            }
            issueRepo.save(issue);
        });
    }

    public void resolveIssue(String issueId, String resolution) {
        updateIssue(issueId, IssueStatus.RESOLVED, resolution);
    }
}
