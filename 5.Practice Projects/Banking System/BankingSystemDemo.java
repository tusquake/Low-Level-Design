import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

enum TransactionType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER_IN,
    TRANSFER_OUT
}

class Transaction {
    private String id;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;

    public Transaction(TransactionType type, double amount) {
        this.id = UUID.randomUUID().toString().substring(0, 8);
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String prefix = (type == TransactionType.DEPOSIT || type == TransactionType.TRANSFER_IN) ? "+" : "-";
        return String.format("[%s] %-12s: %s$%.2f (ID: %s)",
                timestamp.format(formatter), type, prefix, amount, id);
    }
}

abstract class Account {
    protected String accountNumber;
    protected String ownerName;
    protected double balance;
    protected List<Transaction> transactions;

    public Account(String accountNumber, String ownerName) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public void deposit(double amount) {
        if (amount <= 0)
            return;
        balance += amount;
        transactions.add(new Transaction(TransactionType.DEPOSIT, amount));
    }

    public boolean withdraw(double amount) {
        if (amount <= 0 || balance < amount)
            return false;
        balance -= amount;
        transactions.add(new Transaction(TransactionType.WITHDRAWAL, amount));
        return true;
    }

    public void addTransferIn(double amount) {
        balance += amount;
        transactions.add(new Transaction(TransactionType.TRANSFER_IN, amount));
    }

    public void addTransferOut(double amount) {
        balance -= amount;
        transactions.add(new Transaction(TransactionType.TRANSFER_OUT, amount));
    }

    public void displayHistory() {
        System.out.println("\n--- Transaction History for " + ownerName + " (" + accountNumber + ") ---");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
        System.out.println("Final Balance: $" + balance);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }
}

class SavingsAccount extends Account {
    private double interestRate;

    public SavingsAccount(String accountNumber, String ownerName, double interestRate) {
        super(accountNumber, ownerName);
        this.interestRate = interestRate;
    }

    public void applyInterest() {
        double interest = balance * (interestRate / 100);
        deposit(interest);
        System.out.println("Interest of $" + interest + " applied to Savings!");
    }
}

class Bank {
    private Map<String, Account> accounts = new HashMap<>();

    public void createAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Account created for " + account.ownerName);
    }

    public void deposit(String accNum, double amount) {
        Account acc = accounts.get(accNum);
        if (acc != null)
            acc.deposit(amount);
    }

    public void withdraw(String accNum, double amount) {
        Account acc = accounts.get(accNum);
        if (acc != null) {
            if (!acc.withdraw(amount)) {
                System.out.println("Insufficient funds for " + accNum);
            }
        }
    }

    public void transfer(String fromAccNum, String toAccNum, double amount) {
        Account from = accounts.get(fromAccNum);
        Account to = accounts.get(toAccNum);

        if (from != null && to != null && from.getBalance() >= amount) {
            from.addTransferOut(amount);
            to.addTransferIn(amount);
            System.out.println("Transfer of $" + amount + " successful!");
        } else {
            System.out.println("Transfer failed!");
        }
    }

    public void showHistory(String accNum) {
        Account acc = accounts.get(accNum);
        if (acc != null)
            acc.displayHistory();
    }
}

public class BankingSystemDemo {
    public static void main(String[] args) {
        System.out.println("=== SIMPLE BANKING SYSTEM ===\n");

        Bank bank = new Bank();

        // Create accounts
        SavingsAccount savings = new SavingsAccount("S001", "Alice", 2.5);
        Account current = new Account("C001", "Bob") {
        }; // Anonymous class for "Standard" account

        bank.createAccount(savings);
        bank.createAccount(current);

        // Transactions
        bank.deposit("S001", 1000);
        bank.deposit("C001", 500);

        bank.withdraw("S001", 200);
        bank.transfer("S001", "C001", 300);

        // Apply interest to savings
        savings.applyInterest();

        // Display results
        bank.showHistory("S001");
        bank.showHistory("C001");
    }
}
