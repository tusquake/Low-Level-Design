package org.example.Split.SplitStrategies;

import org.example.Split.ExpenseSplitStrategy;
import org.example.Split.Split;

import java.util.List;

public class UnequalExpenseSplit implements ExpenseSplitStrategy {
    //Should ensure total sum matches
    @Override
    public void validateSplitRequest(List<Split> splitList, double totalAmount) {
        double sum = 0;
        for (Split split : splitList) {
            sum += split.getAmountOwe();
        }
        if (sum != totalAmount) {
            throw new IllegalArgumentException("Split amounts do not match the total amount");
        }
    }
}
