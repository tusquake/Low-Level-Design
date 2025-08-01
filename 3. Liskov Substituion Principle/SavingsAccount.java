public class SavingsAccount extends WithdrawableAccount {
    @Override
    public void deposit(double amount) {
        System.out.println("Depositing into Savings Account: " + amount);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawing from Savings Account: " + amount);
    }
}
