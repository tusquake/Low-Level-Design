# Law of Demeter Overview

The Law of Demeter (LoD) is a design guideline for object-oriented software that states a method should only talk to its immediate collaborators, never to strangers it reaches through them. Also known as the **Principle of Least Knowledge**.

## Core Concept

**"Only talk to your immediate friends, never to strangers."**

Don't reach through one object to call methods on another. Ask the object you already have to do the work for you.

## Problem Statement

Developers often chain method calls to navigate deep object graphs to get what they need. This tightly couples code to the internal structure of unrelated objects, meaning any internal change can cascade into breakage far away.

### Bad Example (Violating LoD)

```java
// Reaching through customer → wallet → money
public class Cashier {
    public void charge(Customer customer, double amount) {
        double balance = customer.getWallet().getMoney().getAmount();
        if (balance >= amount) {
            customer.getWallet().getMoney().deduct(amount);
        }
    }
}
```

### Good Example (Following LoD)

```java
// Cashier only talks to Customer directly
public class Cashier {
    public void charge(Customer customer, double amount) {
        customer.deductFunds(amount); // delegate — Customer handles its own internals
    }
}
```

## Key Examples

### 1. Navigation Chain Violation

**Bad:**
```java
// Order knows about Department's internals
public class Order {
    public String getManagerEmail(Employee employee) {
        return employee
            .getDepartment()
            .getManager()
            .getContactInfo()
            .getEmail();     // four hops — highly coupled
    }
}
```

**Good:**
```java
// Each class exposes only what callers need
public class Employee {
    public String getDepartmentManagerEmail() {
        return department.getManagerEmail(); // one hop
    }
}

public class Department {
    public String getManagerEmail() {
        return manager.getEmail(); // one hop
    }
}

// Caller is clean
String email = employee.getDepartmentManagerEmail();
```

### 2. Service Locator / Context Object Chains

**Bad:**
```java
// Drilling into application context
public void processOrder(AppContext ctx) {
    Logger logger = ctx.getServices()
                       .getLoggingService()
                       .getDefaultLogger();
    logger.log("Processing order...");
}
```

**Good:**
```java
// Inject dependencies directly
public void processOrder(Logger logger) {
    logger.log("Processing order...");
}
```

### 3. Wrapper Bloat (Over-Correction)

**Bad:**
```java
// Blindly adding pass-through methods just to satisfy LoD
public class Customer {
    public String getWalletCurrency() { return wallet.getCurrency(); }
    public double getWalletBalance()  { return wallet.getBalance();  }
    public boolean isWalletEmpty()    { return wallet.isEmpty();     }
    // Customer is now just a thin proxy for Wallet's entire API
}
```

**Good:**
```java
// Expose meaningful behaviour, not mirrored internals
public class Customer {
    public boolean canAfford(double amount) {
        return wallet.getBalance() >= amount;
    }

    public void deductFunds(double amount) {
        wallet.deduct(amount);
    }
}
```

### 4. Data Transfer Objects — The Exception

```java
// Fine — Address is a pure data structure with no behaviour
String city = order.getShippingAddress().getCity();

// Also fine: chaining on the same stream pipeline
List<String> names = employees.stream()
    .filter(Employee::isActive)
    .map(Employee::getName)
    .collect(Collectors.toList());
```

## Benefits

1. **Reduced Coupling**: Changes to internal structures don't cascade outward
2. **Better Encapsulation**: Internal structures stay truly internal
3. **Easier Testing**: Mock one direct dependency, not a whole object graph
4. **Simpler Refactoring**: Moving or renaming internals has limited impact on callers
5. **Better Readability**: Methods express intent at one level of abstraction

## Common Violations

### 1. The Train Wreck
```java
// NO: chaining through unrelated objects
double balance = customer.getWallet().getMoney().getAmount();
```

### 2. Deep Context Navigation
```java
// NO: reaching into a global context graph
Logger log = app.getContext().getServices().getLogging().getDefault();
```

### 3. Test Setup That Mirrors the Problem
```java
// NO: deeply nested mocks are a symptom of LoD violations
when(mock.getA().getB().getC().getValue()).thenReturn(42);
```

## When to Add Delegation Methods

Add a delegating method on an object only when:

1. **Behaviour belongs there**: The operation is logically part of that object's responsibility
2. **Multiple callers need it**: The same navigation appears in more than one place
3. **Hiding internals adds value**: The caller truly has no business knowing the internal structure
4. **It expresses intent**: The method name communicates something meaningful beyond navigation

## LoD in Practice

### Example: E-Commerce Order Processing

**Before LoD:**
```java
public class OrderProcessor {
    public void process(Order order) {
        String country = order.getCustomer()
                             .getShippingAddress()
                             .getCountry()
                             .getCode();

        double tax = order.getCustomer()
                         .getShippingAddress()
                         .getCountry()
                         .getTaxRate()
                         .getCurrent();
    }
}
```

**After LoD:**
```java
public class OrderProcessor {
    public void process(Order order) {
        String countryCode = order.getShippingCountryCode();
        double taxRate     = order.getApplicableTaxRate();
    }
}

public class Order {
    public String getShippingCountryCode() {
        return customer.getShippingCountryCode();
    }
    public double getApplicableTaxRate() {
        return customer.getShippingTaxRate();
    }
}
```

## LoD vs Fluent Interfaces

**LoD doesn't mean:**
- No method chaining ever
- No stream/collection pipelines
- No fluent builders
- Wrapping every internal in a pass-through method

**LoD means:**
- Don't chain across ownership boundaries to invoke behaviour
- Don't rely on another object's internal structure
- Don't write code that breaks when a stranger's internals change
- Don't force callers to know more than they need to

## Balancing LoD with Good Design

### Good: Meaningful Delegation
```java
public class Customer {
    public boolean canAfford(double amount) {
        return wallet.getBalance() >= amount; // Customer owns this logic
    }
}
```

### Bad: Hollow Proxy
```java
public class Customer {
    // Adds nothing — just exposes wallet's internals under a different name
    public double getWalletBalance() {
        return wallet.getBalance();
    }
}
```

## Red Flags (LoD Violations)

- Method chains with more than one dot accessing behaviour (`a.getB().doSomething()`)
- Tests that require deeply nested mock setup
- Cascading compile errors when one class's internals change
- Constructor parameters that are only used to navigate further
- God objects that aggregate everything so others can drill in
- "I need X, so I'll grab the thing that has the thing that has X"

## Quotes

> "Only talk to your immediate friends." — Ian Holland, originator of the Law of Demeter (1987)

> "Each unit should have only limited knowledge about other units: only units closely related to the current unit." — Karl Lieberherr & Ian Holland

> "A chain of method calls is like a chain of dependencies. Every link is a place where a change can break your code." — Robert C. Martin

## Relationship with Other Principles

| Principle | Connection to LoD |
|-----------|-------------------|
| **YAGNI** | Both reduce unnecessary coupling and complexity |
| **KISS** | LoD enforces simplicity in object interactions |
| **SRP** | Single responsibility limits what an object knows |
| **OCP** | Loose coupling makes extension easier |
| **DIP** | Depend on interfaces, not deep object graphs |

## Conclusion

The Law of Demeter is about designing objects that are polite. Instead of reaching across the room to grab what you need, you ask the person nearest you to get it for you. This indirection is not overhead — it is encapsulation in action.

Apply it to behaviour-bearing objects to keep your classes loosely coupled, your tests simple, and your refactoring safe. Relax it for pure data structures where strict adherence adds boilerplate without benefit.

**Remember: The object graph you reach through today is the coupling you debug tomorrow.**