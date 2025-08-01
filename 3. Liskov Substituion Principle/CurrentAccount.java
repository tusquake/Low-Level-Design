public class CurrentAccount extends WithdrawableAccount {
    @Override
    public void deposit(double amount) {
        System.out.println("Depositing into Current Account: " + amount);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawing from Current Account: " + amount);
    }
}
