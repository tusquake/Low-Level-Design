package com.carrental.system.repository;

import com.carrental.system.model.Branch;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BranchRepository {
    private final List<Branch> branches = new ArrayList<>();

    public void addBranch(Branch branch) {
        branches.add(branch);
    }

    public Optional<Branch> findById(String id) {
        return branches.stream().filter(b -> b.getId().equals(id)).findFirst();
    }

    public List<Branch> getAll() {
        return branches;
    }
}
