public class ManagerHandler extends ExpenseHandler {
    @Override
    public void approveExpense(String expense, double amount) {
        if (amount <= 5000) {
            System.out.println("Manager approved: " + expense + " ($" + amount + ")");
        } else if (nextHandler != null) {
            nextHandler.approveExpense(expense, amount);
        }
    }
}