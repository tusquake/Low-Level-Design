package org.example.Split.SplitStrategies;

import org.example.Split.ExpenseSplitStrategy;
import org.example.Split.Split;

import java.util.List;

public class PercentageExpenseSplit implements ExpenseSplitStrategy {
    //Should check percentages add to 100%
    @Override
    public void validateSplitRequest(List<Split> splitList, double totalAmount) {
        double totalPercentage = 0;
        for (Split split : splitList) {
            totalPercentage += split.getAmountOwe();
        }
        if (totalPercentage != totalAmount) {
            throw new IllegalArgumentException("Total percentage must sum up to 100%");
        }
    }
}

