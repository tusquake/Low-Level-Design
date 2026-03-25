# Simple Banking System

## Simple Analogy

**Banking System:**
- **Account:** Your digital wallet.
- **Transaction:** Each time you move money (Deposit, Withdraw, Transfer).
- **History:** Your bank statement showing all past moves.
- **Interest:** A "Reward" calculation for Savings accounts.

It's like having a personal ledger that keeps track of your balance and every single penny that moves in or out.

---

## Implementation

### 1. Account Types (Strategy/Inheritance)

```java
public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected List<Transaction> history;
    // ...
}

public class SavingsAccount extends Account {
    private double interestRate;
    // ...
}
```

### 2. Transaction (Command Pattern)

```java
public class Transaction {
    private String id;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
}
```

### 3. Bank (The Orchestrator)

```java
public class Bank {
    private Map<String, Account> accounts;
    
    public void deposit(String accNum, double amount) { ... }
    public void transfer(String fromAcc, String toAcc, double amount) { ... }
}
```

---

## Demo

The demo simulates creating accounts, performing transactions (deposit, withdraw, transfer), and printing the transaction history/final balance.

```java
Bank myBank = new Bank();
myBank.createAccount("ACC001", AccountType.SAVINGS);

myBank.deposit("ACC001", 1000);
myBank.withdraw("ACC001", 200);
myBank.displayHistory("ACC001");
```

---

## Output

```
--- Transaction History for ACC001 ---
[2026-03-23 09:00] DEPOSIT: +$1000.0
[2026-03-23 09:05] WITHDRAWAL: -$200.0
Final Balance: $800.0
```

---

## Key Benefits

- **Encapsulation**: Keeps balance private; only allows modification via specific methods.
- **Scalability**: Easy to add a "Credit Card" account or a "Loan" account later.
- **Traceability**: Every transaction is recorded and searchable.

---

## Real-World Uses

- **Personal Banking Apps**: Chase, Wells Fargo, etc.
- **E-Wallets**: PayPal, Venmo, Paytm.
- **Stock Trading**: Managing cash balances for buying/selling shares.

---

## Quick Summary

| Component | Role |
|-----------|------|
| Account | Holds data (balance, owner) |
| Transaction | Record of a single movement |
| Bank | Logic for transfers and business rules |
