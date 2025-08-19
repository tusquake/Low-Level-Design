public abstract class ExpenseHandler {
    protected ExpenseHandler nextHandler;

    public void setNext(ExpenseHandler handler) {
        this.nextHandler = handler;
    }

    public abstract void approveExpense(String expense, double amount);
}
