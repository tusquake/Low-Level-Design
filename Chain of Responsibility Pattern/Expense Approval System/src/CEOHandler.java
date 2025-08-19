public class CEOHandler extends ExpenseHandler {
    @Override
    public void approveExpense(String expense, double amount) {
        if (amount <= 100000) {
            System.out.println("CEO approved: " + expense + " ($" + amount + ")");
        } else {
            System.out.println("Expense requires board approval: " + expense + " ($" + amount + ")");
        }
    }
}