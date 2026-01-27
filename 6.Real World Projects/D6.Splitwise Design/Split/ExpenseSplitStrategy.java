package org.example.Split;

import java.util.List;

public interface ExpenseSplitStrategy {
    void validateSplitRequest(List<Split> splitList, double totalAmount);
}

