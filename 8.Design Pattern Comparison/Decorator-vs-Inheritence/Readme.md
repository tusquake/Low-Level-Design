# Decorator Pattern: Why Composition Over Inheritance

## Real-World Scenario: Notification System (Slack/Teams/Email)

**Problem:**
You're building a notification system that needs to:
1. Send basic notifications
2. Add optional features dynamically:
    - Logging
    - Encryption
    - Compression
    - Rate limiting
    - Retry logic

**❌ Bad Approach: Inheritance**
- Need classes for every combination
- LoggedNotification, EncryptedNotification, LoggedEncryptedNotification...
- 3 features = 8 classes, 4 features = 16 classes!

**✅ Good Approach: Decorator Pattern**
- Wrap notifications with feature layers
- Any combination without class explosion
- Add/remove features at runtime

---

## The Class Explosion Problem

### With Inheritance (BAD)

```
BasicNotification
├── LoggedNotification
├── EncryptedNotification
├── CompressedNotification
├── LoggedEncryptedNotification
├── LoggedCompressedNotification
├── EncryptedCompressedNotification
└── LoggedEncryptedCompressedNotification

Just 3 features = 8 classes!
Add one more feature = 16 classes!
```

### With Decorator (GOOD)

```
BasicNotification
Decorators:
├── LoggingDecorator
├── EncryptionDecorator
└── CompressionDecorator

3 features = 4 classes total!
Add one more feature = 5 classes total!
```

---

## PART 1: THE INHERITANCE PROBLEM

### Bad Implementation (Inheritance)

```java
abstract class INotification {
    public void send() {
        System.out.println("Sending notification");
    }
}

class LoggedNotification extends INotification {
    @Override
    public void send() {
        System.out.println("Logging...");
        super.send();
    }
}

class EncryptedNotification extends INotification {
    @Override
    public void send() {
        System.out.println("Encrypting...");
        super.send();
    }
}

// Need separate class for combination!
class LoggedEncryptedNotification extends INotification {
    @Override
    public void send() {
        System.out.println("Logging...");
        System.out.println("Encrypting...");
        super.send();
    }
}
```

### Usage
```java
public class InheritanceDemo {
    public static void main(String[] args) {
        INotification notification = new EncryptedNotification();
        notification.send();

        notification = new LoggedNotification();
        notification.send();

        notification = new LoggedEncryptedNotification();
        notification.send();
    }
}
```

### Output
```
Encrypting...
Sending notification
Logging...
Sending notification
Logging...
Encrypting...
Sending notification
```

---

## ❌ Problems with Inheritance Approach

### 1. Class Explosion
```
2 features = 4 classes
3 features = 8 classes
4 features = 16 classes
5 features = 32 classes
n features = 2^n classes!
```

### 2. Code Duplication
```java
// Same logging code repeated everywhere!
class LoggedEncryptedNotification {
    System.out.println("Logging...");  // Duplicated!
    System.out.println("Encrypting...");
}

class LoggedCompressedNotification {
    System.out.println("Logging...");  // Duplicated again!
    System.out.println("Compressing...");
}
```

### 3. Inflexible at Runtime
```java
// Cannot change features after object creation
INotification notification = new LoggedNotification();
// Cannot add encryption now! Must create new object
```

### 4. Violates Open/Closed Principle
```java
// Adding new feature requires modifying existing classes
// or creating many new combination classes
```

---

## PART 2: THE DECORATOR SOLUTION

### Good Implementation (Decorator Pattern)

### 1. Component Interface

```java
interface Notification {
    void send();
}
```

### 2. Concrete Component (Base)

```java
class BasicNotification implements Notification {
    public void send() {
        System.out.println("Sending notification");
    }
}
```

### 3. Base Decorator

```java
abstract class NotificationDecorator implements Notification {
    protected Notification notification;

    public NotificationDecorator(Notification notification) {
        this.notification = notification;
    }
}
```

**Key Point:** Decorator implements same interface as component AND contains a component!

### 4. Concrete Decorators

```java
class LoggingDecorator extends NotificationDecorator {

    public LoggingDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        System.out.println("Logging...");
        notification.send();  // Delegate to wrapped object
    }
}

class EncryptionDecorator extends NotificationDecorator {

    public EncryptionDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        System.out.println("Encrypting...");
        notification.send();  // Delegate to wrapped object
    }
}
```

### 5. Usage (The Magic!)

```java
public class DecoratorDemo {
    public static void main(String[] args) {
        // Create any combination dynamically!
        Notification notification = new EncryptionDecorator(
                new LoggingDecorator(
                        new BasicNotification()
                ));
        notification.send();
    }
}
```

### Output
```
Encrypting...
Logging...
Sending notification
```

---

## ✅ Benefits of Decorator Pattern

### 1. No Class Explosion
```
3 features with Inheritance: 8 classes
3 features with Decorator: 4 classes (1 base + 3 decorators)

10 features with Inheritance: 1024 classes!
10 features with Decorator: 11 classes!
```

### 2. Flexible Combinations
```java
// Any combination without creating new classes!

// Just logging
Notification n1 = new LoggingDecorator(new BasicNotification());

// Just encryption
Notification n2 = new EncryptionDecorator(new BasicNotification());

// Both (order matters!)
Notification n3 = new LoggingDecorator(
                      new EncryptionDecorator(
                          new BasicNotification()));

// Reverse order
Notification n4 = new EncryptionDecorator(
                      new LoggingDecorator(
                          new BasicNotification()));
```

### 3. Runtime Flexibility
```java
// Can add/remove decorators dynamically
Notification notification = new BasicNotification();

if (needsLogging) {
    notification = new LoggingDecorator(notification);
}

if (needsEncryption) {
    notification = new EncryptionDecorator(notification);
}

notification.send();
```

### 4. Single Responsibility
```java
// Each decorator has ONE job
LoggingDecorator → Only handles logging
EncryptionDecorator → Only handles encryption
```

---

## How Decorator Works (Step-by-Step)

### Example: Encrypted + Logged + Basic Notification

```java
Notification notification = new EncryptionDecorator(
                                new LoggingDecorator(
                                    new BasicNotification()
                                ));
notification.send();
```

### Execution Flow:

```
1. notification.send() called
   ↓
2. EncryptionDecorator.send()
   → Prints "Encrypting..."
   → Calls wrapped object's send()
   ↓
3. LoggingDecorator.send()
   → Prints "Logging..."
   → Calls wrapped object's send()
   ↓
4. BasicNotification.send()
   → Prints "Sending notification"
   ↓
Output:
Encrypting...
Logging...
Sending notification
```

### Visual Representation:

```
┌─────────────────────────┐
│  EncryptionDecorator    │
│  ┌───────────────────┐  │
│  │ LoggingDecorator  │  │
│  │  ┌─────────────┐  │  │
│  │  │   Basic     │  │  │
│  │  │ Notification│  │  │
│  │  └─────────────┘  │  │
│  └───────────────────┘  │
└─────────────────────────┘

Each layer adds its behavior, then delegates
```

---

## Real-World Examples

### 1. Java I/O Streams (Classic Decorator!)

```java
// Reading a file with buffering and compression
InputStream stream = new BufferedInputStream(
                        new GZIPInputStream(
                            new FileInputStream("file.txt")
                        ));

// Each decorator adds functionality!
// FileInputStream → Basic file reading
// GZIPInputStream → Adds decompression
// BufferedInputStream → Adds buffering
```

### 2. Coffee Shop (Starbucks Example)

```java
interface Coffee {
    double cost();
    String description();
}

class BasicCoffee implements Coffee {
    public double cost() { return 5.0; }
    public String description() { return "Basic Coffee"; }
}

class MilkDecorator extends CoffeeDecorator {
    public double cost() { return coffee.cost() + 1.5; }
    public String description() { return coffee.description() + ", Milk"; }
}

class SugarDecorator extends CoffeeDecorator {
    public double cost() { return coffee.cost() + 0.5; }
    public String description() { return coffee.description() + ", Sugar"; }
}

// Usage
Coffee myCoffee = new SugarDecorator(
                      new MilkDecorator(
                          new BasicCoffee()
                      ));
// Cost: $7.0, Description: "Basic Coffee, Milk, Sugar"
```

### 3. Text Formatting

```java
Text text = new BoldDecorator(
                new ItalicDecorator(
                    new UnderlineDecorator(
                        new PlainText("Hello")
                    )));

text.render();
// Output: <b><i><u>Hello</u></i></b>
```

### 4. Window GUI Components

```java
Window window = new ScrollBarDecorator(
                    new BorderDecorator(
                        new BasicWindow()
                    ));
```

---

## Complete Example: Enhanced Notification System

```java
// Additional decorators
class CompressionDecorator extends NotificationDecorator {
    public CompressionDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        System.out.println("Compressing...");
        notification.send();
    }
}

class RetryDecorator extends NotificationDecorator {
    private int maxRetries;

    public RetryDecorator(Notification notification, int maxRetries) {
        super(notification);
        this.maxRetries = maxRetries;
    }

    @Override
    public void send() {
        System.out.println("Adding retry logic (max " + maxRetries + " retries)");
        notification.send();
    }
}

class RateLimitDecorator extends NotificationDecorator {
    public RateLimitDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        System.out.println("Checking rate limits...");
        notification.send();
    }
}
```

### Usage with Multiple Decorators

```java
public class AdvancedNotificationDemo {
    public static void main(String[] args) {
        
        // Simple notification
        Notification simple = new BasicNotification();
        simple.send();
        
        System.out.println("\n--- With Logging ---");
        Notification logged = new LoggingDecorator(new BasicNotification());
        logged.send();
        
        System.out.println("\n--- Complex Notification ---");
        Notification complex = new RateLimitDecorator(
                                   new RetryDecorator(
                                       new CompressionDecorator(
                                           new EncryptionDecorator(
                                               new LoggingDecorator(
                                                   new BasicNotification()
                                               )
                                           )
                                       ), 3
                                   )
                               );
        complex.send();
    }
}
```

### Output

```
Sending notification

--- With Logging ---
Logging...
Sending notification

--- Complex Notification ---
Checking rate limits...
Adding retry logic (max 3 retries)
Compressing...
Encrypting...
Logging...
Sending notification
```

---

## Inheritance vs Decorator: Side-by-Side

| Aspect | Inheritance | Decorator |
|--------|------------|-----------|
| **Flexibility** | Compile-time | Runtime |
| **Combinations** | Need class for each | Mix and match freely |
| **Classes Needed** | 2^n for n features | n+1 for n features |
| **Code Reuse** | Limited | Excellent |
| **Adding Features** | Create new subclass | Wrap with decorator |
| **Removing Features** | Create new subclass | Unwrap decorator |
| **Order Matters** | No | Yes |
| **Type Safety** | Compile-time | Compile-time |

---

## When to Use Decorator Pattern

### ✅ Use Decorator When:
- Need to add responsibilities to objects **dynamically**
- Want to avoid **class explosion** from combinations
- Features can be **combined in any way**
- Responsibilities should be **reversible** (can unwrap)
- Want to **extend functionality** without modifying existing code

### ❌ Don't Use Decorator When:
- Need to add many **small objects** (memory overhead)
- Order of decoration doesn't matter (might use other patterns)
- Have only **one or two** features to add (overkill)
- Need to **modify core behavior** (use subclassing instead)

---

## Common Mistakes

### ❌ Mistake 1: Not Implementing Component Interface

```java
// BAD: Decorator doesn't implement Notification
class LoggingDecorator {
    private Notification notification;
    // Cannot be used polymorphically!
}

// GOOD: Implements the interface
class LoggingDecorator implements Notification {
    private Notification notification;
    // Can be used anywhere Notification is expected
}
```

### ❌ Mistake 2: Forgetting to Delegate

```java
// BAD: Doesn't call wrapped object
class LoggingDecorator extends NotificationDecorator {
    public void send() {
        System.out.println("Logging...");
        // Missing: notification.send();
    }
}

// GOOD: Delegates to wrapped object
class LoggingDecorator extends NotificationDecorator {
    public void send() {
        System.out.println("Logging...");
        notification.send();  // Important!
    }
}
```

### ❌ Mistake 3: Creating Decorator Base Class Too Late

```java
// BAD: Each decorator duplicates wrapper logic
class LoggingDecorator implements Notification {
    private Notification notification;
    public LoggingDecorator(Notification n) { this.notification = n; }
}
class EncryptionDecorator implements Notification {
    private Notification notification;  // Duplicated!
    public EncryptionDecorator(Notification n) { this.notification = n; }
}

// GOOD: Base decorator handles common logic
abstract class NotificationDecorator implements Notification {
    protected Notification notification;
    public NotificationDecorator(Notification n) { this.notification = n; }
}
```

---

## Design Principles Applied

### Open/Closed Principle
- **Open** for extension: Add new decorators without modifying existing code
- **Closed** for modification: BasicNotification never changes

### Single Responsibility Principle
- Each decorator has **one job**: logging, encryption, compression, etc.
- No god classes with multiple responsibilities

### Liskov Substitution Principle
- Decorated objects can be used anywhere the base component is expected
- `Notification notification = new LoggingDecorator(new BasicNotification())`

### Favor Composition Over Inheritance
- Decorator uses **composition** (wrapping) instead of inheritance
- More flexible than rigid inheritance hierarchies

---

## Interview Questions & Answers

### Q1: Why is Decorator better than Inheritance?

**A:** "Decorator is better than inheritance when you need to combine multiple features because:

1. **No class explosion**: With inheritance, 3 features need 8 classes (2^3). With Decorator, you need only 4 classes (1 base + 3 decorators).

2. **Runtime flexibility**: You can add/remove features dynamically. With inheritance, combinations are fixed at compile-time.

3. **Any combination**: Can mix decorators in any order. Inheritance requires separate classes for each combination.

Example: For a notification system with logging, encryption, and compression:
- **Inheritance**: Need LoggedEncryptedCompressed, LoggedCompressed, EncryptedCompressed, etc.
- **Decorator**: Just wrap: `new LoggingDecorator(new EncryptionDecorator(new BasicNotification()))`"

---

### Q2: Explain how the Decorator pattern works with an example.

**A:** "The Decorator pattern wraps an object with additional behaviors while keeping the same interface.

**How it works:**
1. Create a base component (BasicNotification)
2. Create decorators that wrap the component
3. Each decorator adds its behavior, then delegates to the wrapped object

**Example:**
```java
Notification n = new EncryptionDecorator(
                     new LoggingDecorator(
                         new BasicNotification()
                     ));
n.send();
```

**Execution flow:**
1. EncryptionDecorator: 'Encrypting...' → calls wrapped object
2. LoggingDecorator: 'Logging...' → calls wrapped object
3. BasicNotification: 'Sending notification'

Think of it like wrapping a gift: each layer adds something, and you can add/remove layers without changing the gift inside."

---

### Q3: What's the difference between Decorator and Proxy pattern?

**A:** "Both wrap objects, but their purposes differ:

**Decorator:**
- **Purpose**: Add new functionality
- **Number**: Can have multiple decorators
- **Example**: Add logging, encryption, compression to notifications

**Proxy:**
- **Purpose**: Control access or add infrastructure concerns
- **Number**: Usually one proxy
- **Example**: Lazy loading, access control, caching

**Code difference:**
```java
// Decorator: Enhances behavior
class LoggingDecorator extends NotificationDecorator {
    public void send() {
        System.out.println('Logging...');  // NEW functionality
        notification.send();
    }
}

// Proxy: Controls access
class NotificationProxy implements Notification {
    public void send() {
        if (hasPermission()) {  // Controls access
            realNotification.send();
        }
    }
}
```

Decorator enhances what an object does. Proxy controls when/how it's accessed."

---

### Q4: Can you give a real-world use case where you'd use Decorator?

**A:** "Yes, in a logging framework for microservices:

**Problem:** Different services need different logging features:
- Service A: Just basic logs
- Service B: Logs + timestamps
- Service C: Logs + timestamps + request IDs
- Service D: Logs + encryption (for sensitive data)

**Without Decorator:**
```java
// Need separate logger class for each combination!
BasicLogger, TimestampLogger, RequestIDLogger,
TimestampRequestIDLogger, EncryptedTimestampLogger...
```

**With Decorator:**
```java
// Service A
Logger logger = new BasicLogger();

// Service B
Logger logger = new TimestampDecorator(new BasicLogger());

// Service C
Logger logger = new RequestIDDecorator(
                    new TimestampDecorator(
                        new BasicLogger()));

// Service D
Logger logger = new EncryptionDecorator(
                    new TimestampDecorator(
                        new BasicLogger()));
```

**Benefits:**
- Each service configures exactly what it needs
- Add/remove features without code changes
- New features (like rate limiting) just need one new decorator class"

---

## Pattern Variations

### 1. Decorator with Additional Methods

```java
class MetricsDecorator extends NotificationDecorator {
    private int sendCount = 0;

    public void send() {
        sendCount++;
        System.out.println("Metrics: Total sends = " + sendCount);
        notification.send();
    }

    // Additional method
    public int getSendCount() {
        return sendCount;
    }
}
```

### 2. Transparent Decorator (Forwards All Methods)

```java
interface Notification {
    void send();
    void cancel();
}

abstract class NotificationDecorator implements Notification {
    protected Notification notification;

    public void send() { notification.send(); }
    public void cancel() { notification.cancel(); }  // Forward all methods
}
```

### 3. Conditional Decorator

```java
class ConditionalLoggingDecorator extends NotificationDecorator {
    private boolean loggingEnabled;

    public void send() {
        if (loggingEnabled) {
            System.out.println("Logging...");
        }
        notification.send();
    }

    public void enableLogging() { loggingEnabled = true; }
    public void disableLogging() { loggingEnabled = false; }
}
```

---

## Comparison with Other Patterns

### Decorator vs Adapter

```java
// Adapter: Changes interface
class NotificationAdapter implements NewNotificationInterface {
    private OldNotification oldNotification;
    // Makes old notification work with new interface
}

// Decorator: Same interface, adds behavior
class LoggingDecorator implements Notification {
    private Notification notification;
    // Adds logging to notification
}
```

### Decorator vs Strategy

```java
// Strategy: Swaps algorithm
class NotificationSender {
    private SendStrategy strategy;  // Can swap entire send algorithm
}

// Decorator: Adds layers
Notification n = new LoggingDecorator(
                     new EncryptionDecorator(
                         new BasicNotification()));  // Wraps with layers
```

---

## Quick Reference

### Structure
```
Component (interface)
    ├── ConcreteComponent (basic implementation)
    └── Decorator (abstract)
            ├── ConcreteDecoratorA
            └── ConcreteDecoratorB
```

### Implementation Checklist
- [ ] Create component interface
- [ ] Create concrete component (base)
- [ ] Create abstract decorator implementing component interface
- [ ] Abstract decorator contains component reference
- [ ] Create concrete decorators extending abstract decorator
- [ ] Each decorator adds behavior + delegates to wrapped object

### Key Points
- **Same interface** as component
- **Contains** a component (composition)
- **Adds** behavior before/after delegation
- Can **chain** multiple decorators
- **Order matters** in decoration

---

## Summary

| Approach | Classes for 3 Features | Flexibility | Code Reuse |
|----------|------------------------|-------------|------------|
| **Inheritance** | 8 classes | Low | Medium |
| **Decorator** | 4 classes | High | High |

**Bottom Line:**
- Inheritance → Class explosion, inflexible
- Decorator → Minimal classes, maximum flexibility
- Use Decorator when you need dynamic feature combinations

**Remember:** Favor composition over inheritance!

---
