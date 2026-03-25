# Open/Closed Principle (OCP)

> "Software entities should be open for extension, but closed for modification." — Bertrand Meyer

## 📌 Concept
The **Open/Closed Principle** (OCP) is the 'O' in SOLID. It means you should be able to add new functionality without changing the existing code. This is usually achieved using **interfaces** and **abstract classes**.

### ❌ Violation
In the `OCPViolation` examples, if we want to add a new storage mechanism (like saving to MongoDB), we have to modify the existing `InvoicePersistence` class. This is risky because modifying working code can introduce bugs.

### ✅ Correction
In the `OCPCorrection` examples, we use an `InvoiceRepository` interface.
-   `MySQLInvoiceRepository`: Implements saving to MySQL.
-   `FileInvoiceRepository`: Implements saving to a File.
-   `MongoInvoiceRepository`: Implements saving to MongoDB.

If we need a new storage type, we just create a new class that implements `InvoiceRepository`. We **extend** the system without **modifying** the `PersistenceManager` or any existing repository implementation.

---

## 💡 Interview Questions

### 1. What does "Closed for Modification" mean?
**Answer:** It means that once a module or class has been developed and tested, its source code should not be changed to add new features. This prevents regression bugs in already working functionality.

### 2. How is OCP typically implemented in Java?
**Answer:** OCP is usually implemented using **polymorphism**. By coding to an interface or an abstract class rather than a concrete implementation, you can swap or add new implementations at runtime without changing the consuming code.

### 3. Does OCP mean you should never modify a class?
**Answer:** No. Bug fixes and refactoring for clarity are valid reasons to modify a class. OCP specifically addresses adding *new features or behaviors*.

### 4. What is the main drawback of over-applying OCP?
**Answer:** Over-applying OCP can lead to an excessive number of interfaces and small classes, which can make the code more complex and harder to navigate for simple tasks. It's important to apply it only where extension is truly likely.

---

## 🚀 How to Run
```bash
javac OCPExample.java
java OCPExample
```
