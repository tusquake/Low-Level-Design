public class TeamLeadHandler extends ExpenseHandler {
    @Override
    public void approveExpense(String expense, double amount) {
        if (amount <= 1000) {
            System.out.println("Team Lead approved: " + expense + " ($" + amount + ")");
        } else if (nextHandler != null) {
            nextHandler.approveExpense(expense, amount);
        }
    }
}