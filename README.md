# Low-Level Design (LLD) Patterns & Principles

A comprehensive repository of **Design Patterns**, **SOLID Principles**, and **System Design implementations** in Java. This collection serves as a practical guide for mastering object-oriented design, preparing for technical interviews, and building scalable applications.

---

## Table of Contents

- [About](#about)
- [Repository Structure](#repository-structure)
- [SOLID Principles](#solid-principles)
- [Design Patterns](#design-patterns)
- [System Design Projects](#system-design-projects)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [Resources](#resources)

---

## About

This repository provides:

- **Practical implementations** of 23 Gang of Four design patterns
- **SOLID principles** with real-world examples
- **Complete system designs** (Parking Lot, Splitwise, BookMyShow, etc.)
- **Interview-ready code** with detailed documentation
- **Progressive learning path** from basics to advanced concepts

**Target Audience:** Software engineers preparing for interviews, developers learning design patterns, and anyone interested in writing maintainable, scalable code.

---

## Repository Structure

```
Low-Level-Design/
│
├── SOLID Principles/
│   ├── 1. Single Responsibility Principle/
│   ├── 2. Open Closed Principle/
│   └── 3. Liskov Substitution Principle/
│
├── Design Patterns/
│   ├── Creational/
│   │   ├── Factory Design Pattern/
│   │   ├── Prototype Design Pattern/
│   │   └── ...
│   │
│   ├── Structural/
│   │   ├── Adapter Design Pattern/
│   │   ├── Bridge Design Pattern/
│   │   ├── Decorator Design Pattern/
│   │   ├── Facade Design Pattern/
│   │   ├── Flyweight Design Pattern/
│   │   ├── Proxy Design Pattern/
│   │   └── Composite Design Pattern/
│   │
│   └── Behavioral/
│       ├── Observer Design Pattern/
│       ├── Strategy Design Pattern/
│       ├── Command Design Pattern/
│       ├── Chain of Responsibility Pattern/
│       ├── State Design Pattern/
│       ├── Template Design Pattern/
│       ├── Iterator Design Pattern/
│       └── Memento Design Pattern/
│
├── System Design Projects/
│   ├── D1. Docs Design/
│   ├── D2. Parking Lot Design/
│   ├── D3. Instagram Design/
│   ├── D4. Notification Design/
│   ├── D5. Payment Gateway System/
│   ├── D6. Splitwise Design/
│   ├── D7. Dating App/
│   ├── D8. Rate Limiter Design/
│   ├── D9. URL Shortener/
│   ├── D10. Online Ticket Booking System/
│   ├── D11. News Feed System/
│   └── D12. Caching System/
│
├── Real-World Applications/
│   ├── Food Delivery/
│   ├── Search System/
│   └── Subscription System/
│
└── Documentation/
    └── Concepts in JS/
```

---

## SOLID Principles

Foundational principles for writing clean, maintainable object-oriented code.

### 1. Single Responsibility Principle (SRP)

**Definition:** A class should have only one reason to change.

**Example:** Separating report generation from report printing.

```java
// Bad - Multiple responsibilities
class Report {
    void generateReport() { }
    void printReport() { }
    void saveToDatabase() { }
}

// Good - Single responsibility
class ReportGenerator {
    void generateReport() { }
}
class ReportPrinter {
    void printReport() { }
}
class ReportRepository {
    void saveToDatabase() { }
}
```

**Benefits:** Easier testing, better maintainability, reduced coupling

---

### 2. Open/Closed Principle (OCP)

**Definition:** Software entities should be open for extension but closed for modification.

**Example:** Adding new payment methods without modifying existing code.

```java
interface PaymentProcessor {
    void processPayment(double amount);
}

class CreditCardProcessor implements PaymentProcessor {
    void processPayment(double amount) { }
}

class UPIProcessor implements PaymentProcessor {
    void processPayment(double amount) { }
}

// Add new processor without modifying existing code
class PayPalProcessor implements PaymentProcessor {
    void processPayment(double amount) { }
}
```

**Benefits:** Reduced risk of breaking existing functionality, easier to add features

---

### 3. Liskov Substitution Principle (LSP)

**Definition:** Objects of a superclass should be replaceable with objects of subclasses without breaking functionality.

**Example:** Proper inheritance hierarchy.

```java
// Bad - Violates LSP
class Bird {
    void fly() { }
}
class Penguin extends Bird {
    void fly() { throw new UnsupportedOperationException(); }
}

// Good - Follows LSP
interface Bird {
    void eat();
}
interface FlyingBird extends Bird {
    void fly();
}
class Sparrow implements FlyingBird { }
class Penguin implements Bird { }
```

**Benefits:** Predictable behavior, safer polymorphism

---

### 4. Interface Segregation Principle (ISP)

**Definition:** Clients should not be forced to depend on methods they don't use.

**Example:** Splitting large interfaces into specific ones.

```java
// Bad - Fat interface
interface Machine {
    void print();
    void scan();
    void fax();
}

// Good - Segregated interfaces
interface Printer {
    void print();
}
interface Scanner {
    void scan();
}
interface FaxMachine {
    void fax();
}

class MultiFunctionPrinter implements Printer, Scanner, FaxMachine { }
class SimplePrinter implements Printer { }
```

**Benefits:** More flexible implementations, easier to maintain

---

### 5. Dependency Inversion Principle (DIP)

**Definition:** Depend on abstractions, not concretions.

**Example:** Using interfaces for dependencies.

```java
// Bad - Depends on concrete class
class NotificationService {
    private EmailSender emailSender = new EmailSender();
}

// Good - Depends on abstraction
interface MessageSender {
    void send(String message);
}

class NotificationService {
    private MessageSender sender;
    
    NotificationService(MessageSender sender) {
        this.sender = sender;
    }
}

class EmailSender implements MessageSender { }
class SMSSender implements MessageSender { }
```

**Benefits:** Loose coupling, easier testing with mocks, flexible implementations

---

## Design Patterns

### Creational Patterns

Focus on object creation mechanisms.

| Pattern | Purpose | Use Case |
|---------|---------|----------|
| **Factory** | Object creation without specifying exact class | Database connections (MySQL, PostgreSQL, MongoDB) |
| **Prototype** | Clone existing objects | Creating similar game characters |
| **Builder** | Construct complex objects step-by-step | Building HTTP requests with optional parameters |
| **Singleton** | Ensure only one instance exists | Logger, Configuration manager |

---

### Structural Patterns

Deal with object composition and relationships.

| Pattern | Purpose | Use Case |
|---------|---------|----------|
| **Adapter** | Make incompatible interfaces work together | Integrating legacy payment systems |
| **Bridge** | Decouple abstraction from implementation | Different vehicles with different engines |
| **Decorator** | Add responsibilities dynamically | Adding features to coffee orders |
| **Facade** | Simplified interface to complex subsystem | Home theater system control |
| **Flyweight** | Share common state to reduce memory | Text editor character rendering |
| **Proxy** | Control access to another object | Lazy loading images, access control |
| **Composite** | Treat individual and composite objects uniformly | File system hierarchy |

---

### Behavioral Patterns

Focus on communication between objects.

| Pattern | Purpose | Use Case |
|---------|---------|----------|
| **Observer** | Notify multiple objects of state changes | YouTube channel subscriptions |
| **Strategy** | Define family of algorithms, choose at runtime | Payment methods, sorting algorithms |
| **Command** | Encapsulate requests as objects | Text editor undo/redo |
| **Chain of Responsibility** | Pass request through chain of handlers | Support ticket escalation |
| **State** | Object changes behavior when state changes | Order status (Pending, Shipped, Delivered) |
| **Template Method** | Define algorithm skeleton, customize steps | Data processing pipeline |
| **Iterator** | Sequential access without exposing structure | Iterating through collections |
| **Memento** | Capture and restore object state | Game save points |

---

## System Design Projects

Real-world applications demonstrating multiple patterns working together.

### D1. Docs Design (Google Docs Clone)

**Patterns Used:** Observer, Command, Memento

**Features:** Real-time collaboration, undo/redo, auto-save

---

### D2. Parking Lot Design

**Patterns Used:** Factory, Strategy, Observer

**Features:** Multiple vehicle types, dynamic pricing, spot allocation

---

### D3. Instagram Design

**Patterns Used:** Observer, Composite, Strategy

**Features:** Posts, comments, likes, feed generation

---

### D4. Notification Design

**Patterns Used:** Strategy, Factory, Template Method

**Features:** Email, SMS, Push notifications with preferences

---

### D5. Payment Gateway System

**Patterns Used:** Strategy, Factory, Chain of Responsibility

**Features:** Multiple payment methods, fraud detection, retries

---

### D6. Splitwise Design

**Patterns Used:** Strategy, Observer, Command

**Features:** Expense splitting algorithms, notifications, settlement

---

### D7. Dating App

**Patterns Used:** Strategy, Observer, State

**Features:** Profile matching, notifications, conversation states

---

### D8. Rate Limiter Design

**Patterns Used:** Strategy, Proxy

**Features:** Token bucket, sliding window, user/IP-based limits

---

### D9. URL Shortener

**Patterns Used:** Factory, Strategy

**Features:** Short URL generation, analytics, expiration

---

### D10. Online Ticket Booking System

**Patterns Used:** State, Observer, Command

**Features:** Seat selection, booking flow, notifications

---

### D11. News Feed System

**Patterns Used:** Observer, Strategy, Composite

**Features:** Post ranking, personalization, real-time updates

---

### D12. Caching System

**Patterns Used:** Proxy, Strategy, Decorator

**Features:** LRU, LFU, TTL-based eviction policies

---

## Getting Started

### Prerequisites

- Java 8 or higher
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- Basic understanding of OOP concepts

### Running Examples

```bash
# Clone the repository
git clone https://github.com/tusquake/Low-Level-Design.git
cd Low-Level-Design

# Navigate to a pattern
cd "Observer Design Pattern"

# Compile
javac *.java

# Run
java Main
```

### Project Structure

Each pattern/project folder contains:
- `Main.java` - Runnable example
- `README.md` - Detailed explanation
- Supporting classes organized by responsibility

---

## Learning Path

### Beginner

1. Start with SOLID Principles
2. Learn Creational Patterns (Factory, Singleton)
3. Practice Structural Patterns (Adapter, Decorator)

### Intermediate

4. Master Behavioral Patterns (Observer, Strategy, Command)
5. Study pattern combinations
6. Implement simple projects (Notification System)

### Advanced

7. Complete system designs (Parking Lot, Splitwise)
8. Learn when NOT to use patterns
9. Practice interview questions

---

## Pattern Combinations

Common combinations seen in production systems:

| Combination | Example | Use Case |
|-------------|---------|----------|
| Strategy + Factory + Orchestrator | Search System | Different search algorithms with runtime selection |
| Observer + Command | Text Editor | Undo/redo with UI updates |
| Adapter + Decorator | Food Delivery | Legacy integrations with dynamic features |
| Factory + Singleton + Builder | Email Service | Single manager, provider selection, complex config |
| Chain of Responsibility + Strategy | Authentication | Multiple checks with different auth methods |

---

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-pattern`)
3. Commit your changes (`git commit -m 'Add State pattern example'`)
4. Push to the branch (`git push origin feature/new-pattern`)
5. Open a Pull Request

### Contribution Ideas

- Add missing design patterns
- Improve documentation
- Add more real-world examples
- Create UML diagrams
- Add unit tests

---

## Interview Preparation Tips

### Common Questions

1. **Explain a design pattern you've used in production**
   - Choose Observer, Strategy, or Factory
   - Explain problem, solution, and benefits

2. **When would you use Pattern X over Pattern Y?**
   - Understand tradeoffs
   - Provide specific scenarios

3. **Design a system using multiple patterns**
   - Start with requirements
   - Identify varying aspects
   - Apply appropriate patterns

---

## Contact

**Author:** Tushar Seth

**GitHub:** [@tusquake](https://github.com/tusquake)
**Linkedin:** [@Tushar Seth](https://www.linkedin.com/in/sethtushar111/)

For questions or suggestions, please open an issue or reach out via GitHub.

---

**Star this repository** if you find it helpful for your learning journey!
