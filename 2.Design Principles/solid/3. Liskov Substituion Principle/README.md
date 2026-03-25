# Liskov Substitution Principle (LSP)

> "Objects in a program should be replaceable with instances of their subtypes without altering the correctness of that program." — Barbara Liskov

## 📌 Concept
The **Liskov Substitution Principle** (LSP) is the 'L' in SOLID. It states that a subclass should be able to stand in for its superclass without breaking the system. This means that a subclass should not override a method with a different behavior than expected by the superclass.

### ❌ Violation
In the `LSPViolation` example, the `Bird` class has a `fly()` method. The `Ostrich` class inherits from `Bird` but cannot fly. When we call `fly()` on an `Ostrich` object, it throws an exception, which violates the contract of the `Bird` class and can lead to runtime errors.

### ✅ Correction
In the `LSPCorrection` example, we use interfaces to separate bird behaviors.
-   `Bird`: Base interface for all birds (e.g., `eat()`).
-   `FlyingBird`: Extension of `Bird` for birds that can fly (e.g., `fly()`).
-   `Sparrow`: Implements `FlyingBird`.
-   `Ostrich`: Implements only `Bird`.

Now, we can only call `fly()` on objects that implement `FlyingBird`, ensuring that we never try to make an ostrich fly and thus maintaining substitution safety.

---

## 💡 Interview Questions

### 1. What is the main goal of LSP?
**Answer:** The goal of LSP is to ensure that a subclass can be used instead of its superclass without any issues. It promotes **polymorphism** and makes the code more robust and easier to maintain.

### 2. How can you detect an LSP violation?
**Answer:** Look for subclasses that throw `UnsupportedOperationException` for methods inherited from a superclass, or subclasses that have empty methods for inherited functionality. These are clear signs of an LSP violation.

### 3. Does LSP mean you should never use inheritance?
**Answer:** No. LSP means you should use inheritance only when the subclass truly follows the contract of the superclass (the "is-a" relationship). If a subclass doesn't support some functionality of the superclass, it might be better to use **interfaces** or **composition**.

### 4. What is the relationship between LSP and the Open/Closed Principle?
**Answer:** LSP is often considered a prerequisite for OCP. If subclasses can't be substituted for their superclass, then any code that uses the superclass would need to be modified when a new subclass is added, which violates OCP.

---

## 🚀 How to Run
```bash
javac LSPViolation.java
java LSPViolation

javac LSPCorrection.java
java LSPCorrection
```
