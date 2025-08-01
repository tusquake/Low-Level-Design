public class FixedDepositAccount extends Account {
    @Override
    public void deposit(double amount) {
        System.out.println("Depositing into Fixed Deposit Account: " + amount);
    }

    // ❌ No withdraw() — valid under LSP because FixedDepositAccount is not used where WithdrawableAccount is expected
}
