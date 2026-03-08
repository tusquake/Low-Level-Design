# Single Responsibility Principle (SRP)

> "A class should have one, and only one, reason to change." — Robert C. Martin

## 📌 Concept
The **Single Responsibility Principle** (SRP) is the 'S' in SOLID. It states that a class should be responsible for only one part of the functionality provided by the software, and that responsibility should be entirely encapsulated by the class.

### ❌ Violation
In the `SRPViolation` examples, a single class (like `ShoppingCart`) might be responsible for:
1.  **Managing items** (logic)
2.  **Displaying the invoice** (presentation)
3.  **Saving to a database** (persistence)

If the database schema changes, the `ShoppingCart` class must change. If the invoice format changes, the `ShoppingCart` class must change. This leads to **tight coupling** and makes the code harder to maintain and test.

### ✅ Correction
In the `SRPCorrection` examples, responsibilities are split into separate classes:
-   `ShoppingCart`: Manages items and business logic.
-   `InvoicePrinter`: Handles the presentation/printing logic.
-   `CartRepository`: Handles the data persistence logic.

Now, a change in how we save to the database only affects `CartRepository`, not the core shopping cart logic.

---

## 💡 Interview Questions

### 1. What is the primary benefit of SRP?
**Answer:** The primary benefits are **loosely coupled** and **highly cohesive** classes. It makes the system easier to understand, maintain, and test because changes to one responsibility don't affect others.

### 2. How do you identify a violation of SRP?
**Answer:** Look for "God Classes" or classes that have multiple reasons to change. If you find yourself using keywords like "and" to describe what a class does (e.g., "This class calculates totals **and** saves to DB"), it's likely a violation. Another sign is a class with too many dependencies from different layers (e.g., UI, DB, logic).

### 3. Does SRP apply only to classes?
**Answer:** No, the principle of single responsibility can be applied at multiple levels: methods, classes, packages, and even microservices. A method should do one thing, a class should represent one concept, and a microservice should own one business capability.

### 4. Can SRP lead to too many small classes?
**Answer:** Yes, over-applying SRP can lead to "fragmentation" where the logic is scattered across many tiny classes, making the overall flow hard to follow. The key is to find the right balance between cohesion and complexity.

---

## 🚀 How to Run
```bash
javac SRPExample.java
java SRPExample
```
