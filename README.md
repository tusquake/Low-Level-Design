# Low-Level Design (LLD) Patterns & Principles

A comprehensive repository of **Design Patterns**, **Design Principles**, and **System Design implementations** in Java. This collection serves as a practical guide for mastering object-oriented design, preparing for technical interviews, and building scalable applications.

---

## Table of Contents

- [About](#about)
- [Repository Structure](#repository-structure)
- [Design Principles](#design-principles)
- [Design Patterns](#design-patterns)
   - [Creational Patterns](#creational-patterns)
   - [Structural Patterns](#structural-patterns)
   - [Behavioral Patterns](#behavioral-patterns)
- [Practice Projects](#practice-projects)
- [Real-World Projects](#real-world-projects)
- [Getting Started](#getting-started)
- [Contributing](#contributing)
- [Contact](#contact)

---

## About

This repository provides:

- **Practical implementations** of 23 Gang of Four design patterns
- **Design principles** (SOLID, KISS, YAGNI, DRY) with real-world examples
- **Complete system designs** (TV Remote Control, Coffee Shop, etc.)
- **Interview-ready code** with detailed documentation
- **Progressive learning path** from basics to advanced concepts

**Target Audience:** Software engineers preparing for interviews, developers learning design patterns, and anyone interested in writing maintainable, scalable code.

---

## Repository Structure

```
Low-Level-Design/
│
├── 1. Design Principles/
│   ├── SOLID Principles/
│   │   ├── 1. Single Responsibility Principle/
│   │   ├── 2. Open Closed Principle/
│   │   ├── 3. Liskov Substitution Principle/
│   │   ├── 4. Interface Segregation Principle/
│   │   └── 5. Dependency Inversion Principle/
│   │
│   └── Other Principles/
│       ├── KISS (Keep It Simple, Stupid)/
│       ├── YAGNI (You Aren't Gonna Need It)/
│       └── DRY (Don't Repeat Yourself)/
│
├── 2. Creational Design Pattern/
│   ├── Factory Design Pattern/
│   ├── Abstract Factory Design Pattern/
│   ├── Builder Design Pattern/
│   ├── Prototype Design Pattern/
│   └── Singleton Design Pattern/
│
├── 3. Structural Design Pattern/
│   ├── Adapter Design Pattern/
│   ├── Bridge Design Pattern/
│   ├── Composite Design Pattern/
│   ├── Decorator Design Pattern/
│   ├── Facade Design Pattern/
│   ├── Flyweight Design Pattern/
│   └── Proxy Design Pattern/
│
├── 4. Behavioral Design Pattern/
│   ├── Chain of Responsibility/
│   ├── Command Design Pattern/
│   ├── Interpreter Design Pattern/
│   ├── Iterator Design Pattern/
│   ├── Mediator Design Pattern/
│   ├── Memento Design Pattern/
│   ├── Observer Design Pattern/
│   ├── State Design Pattern/
│   ├── Strategy Design Pattern/
│   ├── Template Method Design Pattern/
│   └── Visitor Design Pattern/
│
├── 5. Practice Projects/
│   └── (Mini implementations and exercises)/
│
├── 6. Real World Projects/
│   ├── Food Delivery System/
│   ├── Search System/
│   └── Subscription System/
│
└── Concepts in JS/
    └── (JavaScript implementations of concepts)/
```

---

## Design Principles

### SOLID Principles

Foundational principles for writing clean, maintainable object-oriented code.

#### 1. Single Responsibility Principle (SRP)

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

#### 2. Open/Closed Principle (OCP)

**Definition:** Software entities should be open for extension but closed for modification.

**Example:** Adding new payment methods without modifying existing code.

```java
interface PaymentProcessor {
    void processPayment(double amount);
}

class CreditCardProcessor implements PaymentProcessor {
    void processPayment(double amount) { }
}

// Add new processor without modifying existing code
class PayPalProcessor implements PaymentProcessor {
    void processPayment(double amount) { }
}
```

---

#### 3. Liskov Substitution Principle (LSP)

**Definition:** Objects of a superclass should be replaceable with objects of subclasses without breaking functionality.

---

#### 4. Interface Segregation Principle (ISP)

**Definition:** Clients should not be forced to depend on methods they don't use.

---

#### 5. Dependency Inversion Principle (DIP)

**Definition:** Depend on abstractions, not concretions.

---

### Other Core Principles

#### KISS (Keep It Simple, Stupid)

**Definition:** Simplicity should be a key goal in design, and unnecessary complexity should be avoided.

**Key Takeaway:** Write code that's easy to understand, maintain, and debug. Avoid over-engineering.

---

#### YAGNI (You Aren't Gonna Need It)

**Definition:** Don't add functionality until it's actually needed.

**Key Takeaway:** Avoid speculative development and focus on current requirements.

---

#### DRY (Don't Repeat Yourself)

**Definition:** Every piece of knowledge must have a single, unambiguous representation within a system.

**Key Takeaway:** Eliminate code duplication by extracting common logic into reusable components.

---

## Design Patterns

### Creational Patterns

Focus on object creation mechanisms.

| Pattern | Purpose | Example Use Case |
|---------|---------|------------------|
| **Factory** | Create objects without specifying exact class | Database connections (MySQL, PostgreSQL) |
| **Abstract Factory** | Create families of related objects | UI themes (Windows, Mac, Linux) |
| **Builder** | Construct complex objects step-by-step | HTTP requests with optional parameters |
| **Prototype** | Clone existing objects | Creating similar game characters |
| **Singleton** | Ensure only one instance exists | Logger, Configuration manager |

**Example Projects:**
- Database Connection Factory
- Vehicle Factory System
- HTTP Request Builder

---

### Structural Patterns

Deal with object composition and relationships.

| Pattern | Purpose | Example Use Case |
|---------|---------|------------------|
| **Adapter** | Make incompatible interfaces work together | Legacy payment system integration |
| **Bridge** | Decouple abstraction from implementation | Different vehicles with different engines |
| **Composite** | Treat individual and composite objects uniformly | File system hierarchy |
| **Decorator** | Add responsibilities dynamically | Coffee shop order customization |
| **Facade** | Simplified interface to complex subsystem | Home theater system control |
| **Flyweight** | Share common state to reduce memory | Text editor character rendering |
| **Proxy** | Control access to another object | Lazy loading, access control |

**Example Projects:**
- Coffee Shop (Decorator)
- File System (Composite)
- Image Loader (Proxy)

---

### Behavioral Patterns

Focus on communication between objects and responsibility distribution.

| Pattern | Purpose | Example Use Case |
|---------|---------|------------------|
| **Chain of Responsibility** | Pass request through chain of handlers | Support ticket escalation system |
| **Command** | Encapsulate requests as objects | TV Remote Control, Text editor undo/redo |
| **Interpreter** | Evaluate language expressions | Calculator, Expression evaluator |
| **Iterator** | Sequential access without exposing structure | Collection traversal |
| **Mediator** | Centralize complex communications | Chat room, Air traffic control |
| **Memento** | Capture and restore object state | Game save points, Document versions |
| **Observer** | Notify multiple objects of state changes | YouTube subscriptions, Stock price updates |
| **State** | Change behavior when state changes | Order status tracking, Vending machine |
| **Strategy** | Define family of algorithms | Payment methods, Sorting algorithms |
| **Template Method** | Define algorithm skeleton | Data processing pipeline |
| **Visitor** | Separate algorithms from objects | Tax calculation, Shopping cart |

**Example Projects:**
- TV Remote Control System (Command)
- Support Ticket System (Chain of Responsibility)
- Coffee Shop (Decorator)
- Expression Evaluator (Interpreter)

---

## Practice Projects

Mini implementations to practice individual patterns:

1. **TV Remote Control** - Command Pattern
2. **Coffee Shop System** - Decorator Pattern
3. **Calculator** - Interpreter Pattern
4. **Support Ticket System** - Chain of Responsibility
5. **Document Editor** - Memento, Command
6. **Notification System** - Observer, Strategy

---

## Real-World Projects

Complete system designs combining multiple patterns:

### 1. Food Delivery System

**Patterns Used:** Factory, Strategy, Observer, State

**Features:**
- Restaurant and menu management
- Order processing with multiple states
- Multiple payment methods
- Real-time order tracking
- Delivery assignment

---

### 2. Search System

**Patterns Used:** Strategy, Factory, Decorator

**Features:**
- Multiple search algorithms
- Search result ranking
- Filters and facets
- Caching layer

---

### 3. Subscription System

**Patterns Used:** State, Strategy, Observer

**Features:**
- Multiple subscription tiers
- Billing cycle management
- Auto-renewal handling
- Notification system

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
cd "4.Behavorial Design Pattern/Command Design Pattern"

# Compile and run
javac *.java
java Main
```

### Project Structure

Each pattern folder contains:
- **README.md** - Comprehensive pattern documentation
- **src/** - Source code with examples
- **Main.java** - Runnable demonstration
- Supporting classes organized by responsibility

---

## Learning Path

### Beginner (Week 1-2)

1. Start with **Design Principles** (SOLID, KISS, YAGNI, DRY)
2. Learn **Creational Patterns** (Factory, Singleton, Builder)
3. Study **Basic Structural Patterns** (Adapter, Decorator)

### Intermediate (Week 3-4)

4. Master **Behavioral Patterns** (Observer, Strategy, Command)
5. Study **Advanced Structural Patterns** (Composite, Proxy)
6. Implement **Practice Projects** (TV Remote, Coffee Shop)

### Advanced (Week 5+)

7. Complete **Real-World Projects** (Food Delivery, Search System)
8. Learn **Pattern Combinations** and when to use them
9. Practice **Interview Questions** and system design

---

## Pattern Combinations

Common combinations seen in production systems:

| Combination | Example | Use Case |
|-------------|---------|----------|
| **Strategy + Factory** | Payment System | Multiple payment processors with runtime selection |
| **Observer + Command** | Text Editor | Undo/redo with UI updates |
| **Decorator + Facade** | API Gateway | Request enhancement with simplified interface |
| **Chain of Responsibility + Strategy** | Authentication | Multiple validation steps with different auth methods |
| **State + Observer** | Order Tracking | State transitions with notifications |

---

## Key Features

✅ **Comprehensive Documentation** - Each pattern has detailed README with:
- Problem statement
- Solution approach
- Implementation details
- Benefits and drawbacks
- Real-world examples
- Class diagrams

✅ **Runnable Examples** - Every pattern includes working code demonstrations

✅ **Interview-Ready** - Code structured for technical interview scenarios

✅ **Progressive Complexity** - Start simple, build to complex real-world systems

✅ **Best Practices** - Follows Java coding standards and design principles

---

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-pattern`)
3. Follow existing documentation structure
4. Add comprehensive README.md for new patterns
5. Include runnable examples
6. Commit your changes (`git commit -m 'Add Mediator pattern'`)
7. Push to the branch (`git push origin feature/new-pattern`)
8. Open a Pull Request

### Contribution Ideas

- Add missing design patterns
- Improve existing documentation
- Add more real-world examples
- Create UML diagrams
- Add unit tests
- Translate to other languages

---

## Interview Preparation Tips

### Common Questions

**1. Explain a design pattern you've used in production**
- Choose patterns you understand deeply (Observer, Strategy, Factory)
- Explain: Problem → Solution → Benefits → Trade-offs

**2. When would you use Pattern X over Pattern Y?**
- Understand key differences and trade-offs
- Provide specific scenarios for each

**3. Design a system using multiple patterns**
- Start with requirements gathering
- Identify areas that vary
- Apply appropriate patterns
- Explain pattern interactions

### Study Approach

1. **Understand the Problem** - Why does this pattern exist?
2. **Learn the Structure** - Key components and relationships
3. **Implement from Scratch** - Don't just copy code
4. **Modify Examples** - Add features to existing implementations
5. **Teach Others** - Explain patterns to solidify understanding

---

## Resources

### Books
- "Design Patterns: Elements of Reusable Object-Oriented Software" - Gang of Four
- "Head First Design Patterns" - Freeman & Freeman
- "Refactoring: Improving the Design of Existing Code" - Martin Fowler

### Online Resources
- [Refactoring Guru](https://refactoring.guru/design-patterns)
- [Source Making](https://sourcemaking.com/design_patterns)
- [Java Design Patterns](https://java-design-patterns.com/)

---

## Contact

**Author:** Tushar Seth

**GitHub:** [@tusquake](https://github.com/tusquake)

**LinkedIn:** [@Tushar Seth](https://www.linkedin.com/in/sethtushar111/)

For questions, suggestions, or collaboration:
- Open an [issue](https://github.com/tusquake/Low-Level-Design/issues)
- Submit a [pull request](https://github.com/tusquake/Low-Level-Design/pulls)
- Reach out via LinkedIn

---

## License

This repository is created by [Tushar Seth](https://www.linkedin.com/in/sethtushar111/) and open source and available for educational purposes.

---

**⭐ Star this repository** if you find it helpful for your learning journey!

**🤝 Contribute** to help others learn design patterns!
