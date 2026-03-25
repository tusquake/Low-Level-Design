# Template Method + Strategy Pattern: Document Processing System

## Real-World Scenario: Enterprise Document Management

**Problem:**
You're building a document processing system that needs to:
1. **Follow a fixed workflow for all documents** (Template Method Pattern)
    - Read → Process → Save (same order always)
    - Some steps are common, some vary by document type
2. **Use different algorithms interchangeably** (Strategy Pattern)
    - Different payment methods (Credit Card, UPI, PayPal)
    - Different validation strategies (Invoice validation, Resume validation)
    - Switch algorithms at runtime

---

## Architecture Overview

```
Template Method: Defines the skeleton
    ↓
Strategy: Pluggable behavior within steps
    ↓
Complete flexible workflow
```

---

## Pattern Comparison

| Aspect | Template Method | Strategy |
|--------|----------------|----------|
| **Purpose** | Define algorithm skeleton | Define family of algorithms |
| **Control** | Parent class controls flow | Client chooses algorithm |
| **Inheritance** | Uses inheritance | Uses composition |
| **Flexibility** | Fixed structure, variable steps | Completely swappable |
| **Example** | Data processing pipeline | Payment method selection |

---

## PART 1: TEMPLATE METHOD PATTERN

### Problem
Multiple data processors (CSV, XML, JSON) follow the same workflow but have different implementations for certain steps.

### Solution
Define the skeleton of an algorithm in a base class, let subclasses override specific steps.

---

## Implementation - Template Method

### 1. Abstract Template Class

```java
abstract class DataProcessor {

    // Template Method - defines the algorithm skeleton
    public final void process() {
        readData();      // Step 1: varies by subclass
        processData();   // Step 2: varies by subclass
        saveData();      // Step 3: common implementation
    }

    protected abstract void readData();
    protected abstract void processData();

    protected void saveData() {
        System.out.println("Saving data to database");
    }
}
```

**Key Points:**
- `process()` is `final` - cannot be overridden
- Template method defines the order
- Some methods are abstract (must override)
- Some methods have default implementation (optional override)

### 2. Concrete Implementations

```java
class CSVProcessor extends DataProcessor {

    protected void readData() {
        System.out.println("Reading CSV file");
    }

    protected void processData() {
        System.out.println("Processing CSV data");
    }
}

class XMLProcessor extends DataProcessor {

    protected void readData() {
        System.out.println("Reading XML file");
    }

    protected void processData() {
        System.out.println("Processing XML data");
    }
}
```

### 3. Usage

```java
public class TemplateDemo {
    public static void main(String[] args) {
        DataProcessor csvProcessor = new CSVProcessor();
        csvProcessor.process();  // Executes: read → process → save

        DataProcessor xmlProcessor = new XMLProcessor();
        xmlProcessor.process();  // Same order, different implementation
    }
}
```

### Output
```
Reading CSV file
Processing CSV data
Saving data to database
Reading XML file
Processing XML data
Saving data to database
```

---

## Benefits - Template Method

✅ **Code Reuse**
- Common logic in one place
- No duplication of workflow structure

✅ **Consistent Algorithm Structure**
- All subclasses follow same steps
- Guaranteed execution order

✅ **Hollywood Principle**
- "Don't call us, we'll call you"
- Parent class controls the flow

✅ **Protected Variations**
- Vary parts of algorithm without changing structure

---

## PART 2: STRATEGY PATTERN

### Problem
Need to use different algorithms (payment methods, sorting algorithms, validation rules) interchangeably at runtime.

### Solution
Define a family of algorithms, encapsulate each one, and make them interchangeable.

---

## Implementation - Strategy Pattern

### 1. Strategy Interface

```java
interface PaymentStrategy {
    void pay(int amount);
}
```

### 2. Concrete Strategies

```java
class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class UPIPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}
```

### 3. Context Class

```java
class PaymentService {
    PaymentStrategy paymentStrategy;

    public PaymentService(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public void makePayment(int amount) {
        paymentStrategy.pay(amount);
    }
}
```

### 4. Usage

```java
public class StrategyDemo {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService(new UPIPayment());
        paymentService.makePayment(500);

        paymentService = new PaymentService(new CreditCardPayment());
        paymentService.makePayment(600);
    }
}
```

### Output
```
Paid 500 using UPI
Paid 600 using Credit Card
```

---

## Benefits - Strategy Pattern

✅ **Runtime Flexibility**
- Switch algorithms at runtime
- No need to change context class

✅ **Open/Closed Principle**
- Add new strategies without modifying existing code

✅ **Eliminates Conditionals**
- No `if-else` or `switch` for algorithm selection

✅ **Testability**
- Easy to test each strategy independently

---

## PART 3: COMBINING BOTH PATTERNS

### Real-World Scenario: Document Processing with Validation

**Problem:**
- Need a **fixed workflow** (Template Method): Read → Validate → Save → Notify
- Need **different validation algorithms** (Strategy): Invoice validation vs Resume validation

### Solution: Use Both Patterns Together

---

## Combined Implementation

### 1. Strategy Interface for Validation

```java
interface ValidationStrategy {
    void validate(String document);
}

class InvoiceValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(String document) {
        System.out.println("Validating tax and invoice fields");
    }
}

class ResumeValidationStrategy implements ValidationStrategy {
    @Override
    public void validate(String document) {
        System.out.println("Validating keywords and experience");
    }
}
```

### 2. Template Method with Strategy Integration

```java
abstract class DocumentProcessor {

    protected ValidationStrategy validationStrategy;

    protected DocumentProcessor(ValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy;
    }

    // Template Method - fixed workflow
    public final void process(String document) {
        read(document);
        validationStrategy.validate(document); // Strategy pattern here!
        save(document);
        notifyUser();
    }

    protected void read(String document) {
        System.out.println("Reading document");
    }

    protected abstract void save(String document); // Varies by subclass

    protected void notifyUser() {
        System.out.println("Notifying user after processing");
    }
}
```

### 3. Concrete Processors

```java
class InvoiceProcessor extends DocumentProcessor {

    public InvoiceProcessor(ValidationStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void save(String document) {
        System.out.println("Saving invoice to billing system");
    }
}

class ResumeProcessor extends DocumentProcessor {

    public ResumeProcessor(ValidationStrategy strategy) {
        super(strategy);
    }

    @Override
    protected void save(String document) {
        System.out.println("Saving resume to HR system");
    }
}
```

### 4. Usage

```java
public class StrategyTemplate {
    public static void main(String[] args) {
        ValidationStrategy invoiceValidation = new InvoiceValidationStrategy();
        DocumentProcessor invoiceProcessor = new InvoiceProcessor(invoiceValidation);
        invoiceProcessor.process("invoice.pdf");

        ValidationStrategy resumeValidation = new ResumeValidationStrategy();
        DocumentProcessor resumeProcessor = new ResumeProcessor(resumeValidation);
        resumeProcessor.process("resume.pdf");
    }
}
```

### Output
```
Reading document
Validating tax and invoice fields
Saving invoice to billing system
Notifying user after processing
Reading document
Validating keywords and experience
Saving resume to HR system
Notifying user after processing
```

---

## How Patterns Work Together

### Template Method Provides:
- **Fixed workflow structure**: Read → Validate → Save → Notify
- **Guaranteed order** of operations
- **Common implementations** (read, notify)

### Strategy Provides:
- **Pluggable validation** algorithms
- **Runtime flexibility** to choose validation strategy
- **Easy to add** new validation types

### Together:
```
Template Method: Defines the skeleton (when to validate)
    +
Strategy: Defines validation behavior (how to validate)
    =
Flexible yet structured system
```

---

## Real-World Flow

```
Process Invoice Document
    ↓
Template: read(document)
    ↓
Strategy: invoiceValidation.validate(document)  ← Pluggable!
    ↓
Template: save(document) - invoice-specific
    ↓
Template: notifyUser()
```

---

## When to Use Which Pattern?

### Use Template Method When:
- You have a **fixed sequence of steps**
- Some steps are common, some vary
- Want to **prevent subclasses from changing order**
- Example: Data import (read → validate → transform → save)

### Use Strategy When:
- You have **multiple algorithms** for the same task
- Want to **switch algorithms at runtime**
- Want to **avoid conditional statements**
- Example: Payment methods, sorting algorithms

### Use Both Together When:
- Need **fixed workflow** with **pluggable behaviors**
- Example: Document processing with different validations

---

## Running the Code

```bash
# Template Method Pattern
javac TemplateDemo.java
java TemplateDemo

# Strategy Pattern
javac StrategyDemo.java
java StrategyDemo

# Combined Pattern
javac StrategyTemplate.java
java StrategyTemplate
```

---

## Real-World Examples

### Template Method Pattern

**Data Migration Pipeline:**
```java
abstract class DataMigration {
    final void migrate() {
        extractData();    // Abstract - varies by source
        transformData();  // Abstract - varies by format
        loadData();       // Concrete - same for all
    }
}
```

**Game AI:**
```java
abstract class GameAI {
    final void takeTurn() {
        collectResources();
        buildStructures();
        trainUnits();
        attack();  // Each AI type attacks differently
    }
}
```

**Report Generation:**
```java
abstract class ReportGenerator {
    final void generate() {
        fetchData();
        formatData();     // Varies by report type
        addHeader();
        addFooter();
        export();         // Varies by format (PDF, Excel)
    }
}
```

### Strategy Pattern

**Compression Algorithms:**
```java
interface CompressionStrategy {
    void compress(File file);
}
// Strategies: ZIP, RAR, 7Z, GZIP
```

**Pricing Strategies:**
```java
interface PricingStrategy {
    double calculatePrice(double basePrice);
}
// Strategies: RegularPrice, DiscountPrice, BlackFridayPrice
```

**Routing Algorithms:**
```java
interface RouteStrategy {
    Route findRoute(Location start, Location end);
}
// Strategies: Shortest, Fastest, EcoFriendly, Scenic
```

---

## Common Mistakes

### ❌ Template Method Mistakes

```java
// BAD: Making template method non-final
public void process() {  // Can be overridden - breaks contract!
    readData();
    processData();
}

// GOOD: Make it final
public final void process() {  // Cannot be overridden
    readData();
    processData();
}
```

```java
// BAD: Too many abstract methods
abstract class Processor {
    abstract void step1();
    abstract void step2();
    abstract void step3();  // Everything is abstract!
}

// GOOD: Balance between abstract and concrete
abstract class Processor {
    abstract void step1();        // Varies
    void step2() { /* common */ } // Reusable
    abstract void step3();        // Varies
}
```

### ❌ Strategy Mistakes

```java
// BAD: Using conditionals instead of strategy
class PaymentService {
    void pay(String type, int amount) {
        if (type.equals("upi")) { /* UPI logic */ }
        else if (type.equals("card")) { /* Card logic */ }
    }
}

// GOOD: Use strategy pattern
class PaymentService {
    PaymentStrategy strategy;
    void pay(int amount) {
        strategy.pay(amount);
    }
}
```

---

## Design Comparison Table

| Feature | Template Method | Strategy |
|---------|----------------|----------|
| **Mechanism** | Inheritance | Composition |
| **Flexibility** | Fixed structure | Fully swappable |
| **When to decide** | Compile-time (subclass) | Runtime (inject strategy) |
| **Code location** | Parent class | Separate strategy classes |
| **Use case** | When algorithm structure is stable | When algorithms frequently change |

---

## Interview Questions & Answers

### Q1: What's the key difference between Template Method and Strategy?

**A:** "The key difference is in what they allow you to vary:

- **Template Method** lets you vary **parts of an algorithm** while keeping the overall structure fixed. The parent class controls the flow.
- **Strategy** lets you vary the **entire algorithm**. The client controls which algorithm to use.

Example:
- **Template Method**: All data processors follow Read→Process→Save, but CSV reads differently than XML.
- **Strategy**: Payment can use completely different algorithms (UPI, Credit Card, Wallet) with no fixed structure.

Template Method uses inheritance, Strategy uses composition."

---

### Q2: When would you combine Template Method and Strategy patterns?

**A:** "I'd combine them when I need a fixed workflow with pluggable behaviors.

Real example: Document processing system
- **Template Method** ensures all documents go through: Read → Validate → Save → Notify
- **Strategy** allows different validation algorithms for different document types

```java
abstract class DocumentProcessor {
    final void process() {
        read();
        validationStrategy.validate();  // Strategy!
        save();
    }
}
```

This gives me:
- Guaranteed workflow order (Template Method)
- Flexible validation logic (Strategy)
- Easy to add new document types AND new validation rules"

---

### Q3: Why make the template method final?

**A:** "Making the template method `final` prevents subclasses from changing the algorithm's structure.

```java
// final ensures this order cannot be changed
public final void process() {
    readData();
    processData();
    saveData();
}
```

Without `final`, a subclass could override and change the order or skip steps:
```java
// BAD - if process() wasn't final
public void process() {
    saveData();      // Wrong order!
    readData();
    // Missing processData()!
}
```

The `final` keyword enforces the contract: 'You can customize HOW each step works, but not WHICH steps run or WHEN.'"

---

### Q4: Can you give a real project where you'd use Strategy pattern?

**A:** "Yes, in an e-commerce checkout system:

**Problem:** Different payment methods (Credit Card, PayPal, UPI, Wallet) have completely different processing logic.

**Bad Approach:**
```java
if (paymentType == "card") {
    // Card processing
} else if (paymentType == "upi") {
    // UPI processing
} // Gets messy with 10+ payment methods!
```

**Strategy Pattern Solution:**
```java
interface PaymentStrategy {
    boolean processPayment(double amount);
}

class CardPayment implements PaymentStrategy { }
class UPIPayment implements PaymentStrategy { }
class WalletPayment implements PaymentStrategy { }

class Checkout {
    void pay(PaymentStrategy strategy, double amount) {
        strategy.processPayment(amount);
    }
}
```

Benefits:
- Easy to add new payment methods (just create new strategy class)
- No modification to Checkout class (Open/Closed Principle)
- Each payment method is independently testable
- Can switch payment methods at runtime"

---

## Design Principles Applied

### Template Method

**Inversion of Control (Hollywood Principle)**
- "Don't call us, we'll call you"
- Parent class calls subclass methods

**DRY (Don't Repeat Yourself)**
- Common code in parent class
- No duplication across subclasses

### Strategy

**Open/Closed Principle**
- Open for extension (add new strategies)
- Closed for modification (no changes to context)

**Single Responsibility**
- Each strategy has one job
- Context delegates to strategy

### Both

**Favor Composition Over Inheritance**
- Strategy uses composition
- More flexible than Template Method's inheritance

---

## Advanced: Dynamic Strategy Switching

```java
class PaymentService {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void pay(int amount) {
        strategy.pay(amount);
    }
}

// Usage
PaymentService service = new PaymentService(new UPIPayment());
service.pay(100);  // Pays with UPI

service.setStrategy(new CreditCardPayment());
service.pay(200);  // Pays with Credit Card
```

**Benefit:** Switch strategies without creating new objects!

---

## Pattern Selection Guide

```
Do you need a fixed sequence of steps?
    YES → Consider Template Method
    NO  → ↓

Do you need to switch algorithms at runtime?
    YES → Use Strategy
    NO  → ↓

Do you need BOTH fixed workflow AND pluggable behaviors?
    YES → Use Template Method + Strategy
```

---

## Quick Reference

### Template Method Pattern
```
Purpose: Define algorithm skeleton
Structure: Abstract class with template method
Customization: Override specific steps
Control: Parent class controls flow
Example: process() { read(); transform(); save(); }
```

### Strategy Pattern
```
Purpose: Encapsulate algorithms
Structure: Interface + concrete strategies
Customization: Inject different strategy
Control: Client chooses strategy
Example: PaymentService(new UPIPayment())
```

### Combined Usage
```
Purpose: Fixed workflow + pluggable behaviors
Structure: Template class with injected strategies
Benefit: Best of both patterns
Example: DocumentProcessor with ValidationStrategy
```

---

## Summary Table

| Aspect | Template Method | Strategy | Combined |
|--------|----------------|----------|----------|
| **What varies** | Steps in algorithm | Entire algorithm | Steps + behaviors |
| **How to vary** | Subclass override | Inject strategy | Both |
| **Flexibility** | Limited | High | Balanced |
| **Code reuse** | High (inheritance) | Low | High |
| **Complexity** | Low | Low | Medium |
| **Best for** | Stable workflows | Swappable algorithms | Complex systems |

---

## Real-World Analogies

### Template Method
**Making Coffee/Tea:**
- Boil water (common)
- Brew (varies: coffee beans vs tea leaves)
- Pour in cup (common)
- Add condiments (varies: sugar/milk vs lemon)

**Structure is same, some steps differ.**

### Strategy
**Travel to Airport:**
- Drive car (fast, expensive)
- Take bus (slow, cheap)
- Call Uber (medium, convenient)

**Completely different approaches to same goal.**

---