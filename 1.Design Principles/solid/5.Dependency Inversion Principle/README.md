# Dependency Inversion Principle (DIP)

> "Depend upon abstractions, not concretions." — Robert C. Martin

## 📌 Concept
The **Dependency Inversion Principle** (DIP) is the 'D' in SOLID. It states that:
1.  High-level modules should not depend on low-level modules. Both should depend on abstractions.
2.  Abstractions should not depend on details. Details should depend on abstractions.

### ❌ Violation
In the `DIPViolation` example, the `ShoppingMall` (high-level) is directly dependent on the `DebitCard` (low-level). This means that if we want to add a `CreditCard`, we have to modify the `ShoppingMall` class, which is a clear violation of DIP.

### ✅ Correction
In the `DIPCorrection` example, we use the `BankCard` interface as an abstraction.
-   `DebitCard`: Implements the `BankCard` interface.
-   `CreditCard`: Implements the `BankCard` interface.
-   `ShoppingMall`: Depends only on the `BankCard` interface.

Now, we can pass any type of bank card to the `ShoppingMall` through its constructor (Dependency Injection), and the `ShoppingMall` class doesn't need to change when a new card type is added.

---

## 💡 Interview Questions

### 1. What is the main goal of DIP?
**Answer:** The goal of DIP is to decouple high-level and low-level modules, making the system more flexible and easier to maintain. By depending on abstractions, changes in low-level details don't affect high-level business logic.

### 2. How is DIP achieved?
**Answer:** DIP is typically achieved using **interfaces** and **abstract classes** to define contracts between modules. **Dependency Injection (DI)** is a common technique used to provide concrete implementations of these abstractions at runtime.

### 3. Is DIP the same as Dependency Injection (DI)?
**Answer:** No. DIP is a **design principle** focused on decoupling modules through abstractions. DI is a **design pattern** used to implement DIP by providing dependencies to a class rather than having the class create them itself.

### 4. What are the benefits of using DIP?
**Answer:** The benefits include **loose coupling**, **easier testing** (since abstractions can be mocked), **improved maintainability**, and **enhanced flexibility** for adding new implementations without modifying existing high-level code.

---

## 🚀 How to Run
```bash
javac DIPViolation.java
java DIPViolation

javac DIPCorrection.java
java DIPCorrection
```
