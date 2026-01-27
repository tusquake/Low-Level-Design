package org.example.Split.SplitStrategies;

import org.example.Split.ExpenseSplitStrategy;
import org.example.Split.Split;

import java.util.List;

public class EqualExpenseSplit implements ExpenseSplitStrategy {
    //Ensures all users contribute equally
    @Override
    public void validateSplitRequest(List<Split> splitList, double totalAmount) {
        double amountShouldBePresent = totalAmount / splitList.size();
        for (Split split : splitList) {
            if (split.getAmountOwe() != amountShouldBePresent) {
                throw new IllegalArgumentException("Each person should have an equal split");
            }
        }
    }
}

