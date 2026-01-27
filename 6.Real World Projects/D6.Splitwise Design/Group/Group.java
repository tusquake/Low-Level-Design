package org.example.Group;

import org.example.Expense.Expense;
import org.example.Controllers.ExpenseController;
import org.example.Expense.ExpenseSplitType;
import org.example.Split.Split;
import org.example.User.User;

import java.util.ArrayList;
import java.util.List;

public class Group {

    String groupId;
    String groupName;
    List<User> groupMembers;

    List<Expense> expenseList;

    ExpenseController expenseController;

    public Group(){
        groupMembers = new ArrayList<>();
        expenseList = new ArrayList<>();
        expenseController = new ExpenseController();
    }

    //add member to group
    public void addMember(User member) {
        if (groupMembers.contains(member)) {
            System.out.println("User " + member.getUserName() + " is already a member of the group!");
        } else {
            groupMembers.add(member);
            System.out.println("User " + member.getUserName() + " added to the group.");
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Expense> getExpenses() { // ✅ ADD THIS METHOD
        return expenseList;
    }

    public List<User> getGroupMembers() {
        return groupMembers;
    }


    public Expense createExpense(String expenseId, String description, double expenseAmount,
                                 List<Split> splitDetails, ExpenseSplitType splitType, User paidByUser) {

        Expense expense = expenseController.createExpense(expenseId, description, expenseAmount, splitDetails, splitType, paidByUser);
        expenseList.add(expense);
        return expense;
    }





}
