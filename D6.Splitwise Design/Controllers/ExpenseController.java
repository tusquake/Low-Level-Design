package org.example.Controllers;
import java.util.List;


import org.example.Expense.Expense;
import org.example.Expense.ExpenseSplitType;

import org.example.Split.ExpenseSplitStrategy;
import org.example.Split.Split;
import org.example.Split.SplitStrategies.EqualExpenseSplit;
import org.example.Split.SplitStrategies.PercentageExpenseSplit;
import org.example.Split.SplitStrategies.UnequalExpenseSplit;
import org.example.User.User;


public class ExpenseController {

    BalanceSheetController balanceSheetController;
    public ExpenseController(){
        balanceSheetController = new BalanceSheetController();
    }

    public Expense createExpense(String expenseId, String description, double expenseAmount, List<Split> splitDetails, ExpenseSplitType splitType, User paidByUser){

        ExpenseSplitStrategy expenseSplit = switch (splitType) {
            case EQUAL -> new EqualExpenseSplit();
            case UNEQUAL -> new UnequalExpenseSplit();
            case PERCENTAGE -> new PercentageExpenseSplit();
            default -> throw new IllegalArgumentException("Invalid split type");
        };



        expenseSplit.validateSplitRequest(splitDetails, expenseAmount);

        Expense expense = new Expense(expenseId, expenseAmount, description, paidByUser, splitType, splitDetails);

        balanceSheetController.updateUserExpenseBalanceSheet(paidByUser, splitDetails, expenseAmount);

        return expense;
    }
}
