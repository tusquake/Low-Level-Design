public class ExpenseApprovalSystem {
    public static void main(String[] args) {
        // Create approval chain
        ExpenseHandler teamLead = new TeamLeadHandler();
        ExpenseHandler manager = new ManagerHandler();
        ExpenseHandler director = new DirectorHandler();
        ExpenseHandler ceo = new CEOHandler();

        // Build the chain: TeamLead → Manager → Director → CEO
        teamLead.setNext(manager);
        manager.setNext(director);
        director.setNext(ceo);

        // Submit various expenses
        teamLead.approveExpense("Office supplies", 500);      // Team Lead
        teamLead.approveExpense("New laptop", 2500);          // Manager
        teamLead.approveExpense("Conference booth", 15000);   // Director
        teamLead.approveExpense("Office renovation", 75000);  // CEO
        teamLead.approveExpense("Building purchase", 500000); // Board approval
    }
}
