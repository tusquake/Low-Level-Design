package com.resolution.system.model;

import com.resolution.system.model.enums.IssueType;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Agent {
    private String id;
    private String name;
    private String email;
    private Set<IssueType> expertise;
    private String assignedIssueId; // Currently working on
    @Builder.Default
    private List<String> history = new ArrayList<>(); // Issues resolved/worked on

    public boolean isFree() {
        return assignedIssueId == null;
    }
}
