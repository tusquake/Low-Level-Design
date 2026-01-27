public class BankClient {

    public void doDeposit(Account account, double amount) {
        account.deposit(amount);
    }

    public void doWithdraw(WithdrawableAccount account, double amount) {
        account.withdraw(amount);
    }

    public static void main(String[] args) {
        BankClient client = new BankClient();

        Account fixed = new FixedDepositAccount();
        WithdrawableAccount savings = new SavingsAccount();
        WithdrawableAccount current = new CurrentAccount();

        client.doDeposit(fixed, 1000);
        client.doDeposit(savings, 500);
        client.doWithdraw(savings, 100);

        client.doDeposit(current, 1500);
        client.doWithdraw(current, 400);

        // ❌ client.doWithdraw(fixed, 200); → won't compile, which is good (FixedDeposit doesn't support withdraw)
    }
}
