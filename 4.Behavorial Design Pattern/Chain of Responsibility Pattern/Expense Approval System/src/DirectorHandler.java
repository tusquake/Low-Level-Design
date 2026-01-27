public class DirectorHandler extends ExpenseHandler {
    @Override
    public void approveExpense(String expense, double amount) {
        if (amount <= 20000) {
            System.out.println("Director approved: " + expense + " ($" + amount + ")");
        } else if (nextHandler != null) {
            nextHandler.approveExpense(expense, amount);
        }
    }
}