# Interface Segregation Principle (ISP)

> "Clients should not be forced to depend upon interfaces that they do not use." — Robert C. Martin

## 📌 Concept
The **Interface Segregation Principle** (ISP) is the 'I' in SOLID. It states that larger interfaces should be split into smaller, more specific ones. This ensures that implementing classes only need to concern themselves with the methods that are relevant to them.

### ❌ Violation
In the `ISPViolation` example, the `SmartDevice` interface is a "fat" interface that includes `print()`, `scan()`, and `fax()`. The `BasicPrinter` class is forced to implement all three methods, even though it can only print. This leads to empty or exception-throwing methods, which is a clear violation of ISP.

### ✅ Correction
In the `ISPCorrection` example, the interfaces are split into `Printer`, `Scanner`, and `Fax`.
-   `MultiFunctionPrinter`: Implements all relevant interfaces.
-   `EconomicPrinter`: Implements only the `Printer` interface.

Now, the `EconomicPrinter` is not forced to depend on `scan()` or `fax()` methods, making the system more decoupled and easier to maintain.

---

## 💡 Interview Questions

### 1. What is the main benefit of ISP?
**Answer:** The main benefit of ISP is that it prevents classes from being forced to implement methods they don't need. This leads to **loose coupling**, **better readability**, and **easier testing**. It also reduces the impact of changes to one part of the system on other unrelated parts.

### 2. How does ISP relate to SRP?
**Answer:** ISP is similar to SRP but for interfaces. While SRP focuses on the responsibility of a class, ISP focuses on the interface provided to the client. A class might have multiple responsibilities (SRP violation), and its interface might be too broad (ISP violation).

### 3. What is a "Fat Interface"?
**Answer:** A "Fat Interface" is an interface that has too many methods, often covering multiple unrelated functionalities. This forces implementing classes to provide empty implementations or throw exceptions for methods they don't support.

### 4. Can you have too many small interfaces?
**Answer:** Yes, over-applying ISP can lead to an explosion of interfaces, which can make the code harder to navigate. The key is to group related methods together based on the needs of the clients.

---

## 🚀 How to Run
```bash
javac ISPViolation.java
java ISPViolation

javac ISPCorrection.java
java ISPCorrection
```
