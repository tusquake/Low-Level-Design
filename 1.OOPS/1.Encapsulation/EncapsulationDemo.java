/**
 * Encapsulation Demo
 * 
 * Bundling data and methods into a single unit (BankAccount)
 * and controlling access through public methods.
 */
class BankAccount {
    // 1. Data is strictly PRIVATE
    private String accountNumber;
    private double balance;
    private String accountHolder;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        // 2. Initial balance must be non-negative
        if (initialBalance >= 0) {
            this.balance = initialBalance;
        } else {
            this.balance = 0;
            System.out.println("Warning: Initial balance cannot be negative. Set to 0.");
        }
    }

    // 3. GETTERS (Controlled Read Access)
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    // 4. SETTERS with VALIDATION (Controlled Write Access)
    public void setAccountHolder(String accountHolder) {
        if (accountHolder != null && !accountHolder.isEmpty()) {
            this.accountHolder = accountHolder;
        } else {
            System.out.println("Error: Invalid name provided.");
        }
    }

    // 5. BUSINESS METHODS (Controlled modification)
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
        } else {
            System.out.println("Error: Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawn: $" + amount);
        } else {
            System.out.println("Error: Insufficient balance or invalid amount.");
        }
    }
}

public class EncapsulationDemo {
    public static void main(String[] args) {
        System.out.println("=== ENCAPSULATION DEMO ===\n");

        BankAccount myAccount = new BankAccount("ACC-123", "Alice", 1000.0);

        // Accessing data through methods
        System.out.println("Account Holder: " + myAccount.getAccountHolder());
        System.out.println("Initial Balance: $" + myAccount.getBalance());

        // Modifying data safely
        myAccount.deposit(500);
        myAccount.withdraw(200);

        // Attempting invalid modifications (blocked by validation logic)
        myAccount.withdraw(2000); // Insufficient balance
        myAccount.setAccountHolder(""); // Invalid name

        System.out.println("\nFinal Balance: $" + myAccount.getBalance());

        // Note: We CANNOT do 'myAccount.balance = 999999;' because it's private!
        // This is the core of Encapsulation.
    }
}
