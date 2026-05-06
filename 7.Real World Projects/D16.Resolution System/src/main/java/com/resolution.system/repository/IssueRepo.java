package com.resolution.system.repository;

import com.resolution.system.model.Issue;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class IssueRepo {
    private final Map<String, Issue> issues = new ConcurrentHashMap<>();

    public void save(Issue issue) {
        issues.put(issue.getId(), issue);
    }

    public Optional<Issue> getById(String id) {
        return Optional.ofNullable(issues.get(id));
    }

    public Collection<Issue> getAll() {
        return issues.values();
    }
}
