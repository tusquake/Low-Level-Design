package org.example;

import org.example.Controllers.BalanceSheetController;
import org.example.Controllers.GroupController;
import org.example.Expense.ExpenseSplitType;
import org.example.Group.Group;
import org.example.Split.Split;
import org.example.User.User;
import org.example.User.UserController;

import java.util.ArrayList;
import java.util.List;

public class Splitwise {

    private static Splitwise instance; // Singleton instance

    private final UserController userController;
    private final GroupController groupController;
    private final BalanceSheetController balanceSheetController;

    // Private constructor to enforce Singleton
    private Splitwise() {
        userController = new UserController();
        groupController = new GroupController();
        balanceSheetController = new BalanceSheetController();
    }

    // Public method to get the singleton instance
    public static Splitwise getInstance() {
        if (instance == null) {  // First check (No locking, improves performance)
            synchronized (Splitwise.class) {  // Locking to prevent race condition
                if (instance == null) {  // Second check (Ensures only one instance)
                    instance = new Splitwise();
                }
            }
        }
        return instance;
    }


    public void runSplitwiseDemo() {
        setupUsersAndGroup();

        // Step 1: Add members to the group
        Group group = groupController.getGroup("G1001");
        group.addMember(userController.getUser("U2001")); // Bob
        group.addMember(userController.getUser("U3001")); // Charlie

        // Step 2: Create expenses within the group
        group.createExpense(
                "Exp1001", "Breakfast", 900,
                List.of(
                        new Split(userController.getUser("U1001"), 300), // Alice
                        new Split(userController.getUser("U2001"), 300), // Bob
                        new Split(userController.getUser("U3001"), 300)  // Charlie
                ),
                ExpenseSplitType.EQUAL,
                userController.getUser("U1001") // Alice created the expense
        );

        group.createExpense(
                "Exp1002", "Lunch", 500,
                List.of(
                        new Split(userController.getUser("U1001"), 400), // Alice
                        new Split(userController.getUser("U2001"), 100)  // Bob
                ),
                ExpenseSplitType.UNEQUAL,
                userController.getUser("U2001") // Bob created the expense
        );

        // Display balance sheets
        for (User user : userController.getAllUsers()) {
            balanceSheetController.showBalanceSheetOfUser(user);
        }
    }

    private void setupUsersAndGroup() {
        registerUsers();

        // Create a group by Alice
        groupController.createNewGroup("G1001", "Outing with Friends", userController.getUser("U1001"));
    }

    private void registerUsers() {
        userController.addUser(new User("U1001", "Alice"));
        userController.addUser(new User("U2001", "Bob"));
        userController.addUser(new User("U3001", "Charlie"));
    }
}
