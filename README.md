# Low Level Design (LLD) in Java

A comprehensive collection of **SOLID principle implementations**, **Design Patterns**, and **System Design case studies** in Java.  
This repository is aimed at learners, interview preparation, and developers who want to strengthen their design skills with clean, extensible code.

---

## 📌 Why This Repository?

- To **understand** and **practice** low-level design concepts in Java.  
- To learn how to write **maintainable** and **scalable** code following SOLID principles.  
- To explore **design patterns** with working examples.  
- To apply these patterns in **real-world mini projects** (e.g., Parking Lot, Notification Service, Splitwise).  

---

## 📂 Repository Structure

- `solid/` → Examples of SOLID principles.  
- `patterns/` → Creational, Structural, and Behavioral design patterns.  
- `systemdesign/` → Mini-projects demonstrating real-world case studies.  
- `docs/` → UML diagrams, notes, and explanations (if available).  

---

## 🧱 SOLID Principles (with Examples)

The **SOLID** principles are the foundation of clean and maintainable OOP design. Each folder contains Java examples that demonstrate the principle in action.

### 1. **Single Responsibility Principle (SRP)**
> *A class should have only one reason to change.*  
- Each class should do **only one thing** (focus on a single responsibility).  
- Helps reduce coupling and makes code easier to test.  
- Example: A `ReportPrinter` class should only handle printing, while a `ReportGenerator` handles generating data.

---

### 2. **Open/Closed Principle (OCP)**
> *Software entities should be open for extension, but closed for modification.*  
- You should be able to add new behavior **without changing existing code**.  
- Achieved via interfaces, abstract classes, and polymorphism.  
- Example: Payment processors where adding a new payment method (e.g., UPI, PayPal) should not break or modify existing code.

---

### 3. **Liskov Substitution Principle (LSP)**
> *Objects of a superclass should be replaceable with objects of a subclass without breaking functionality.*  
- Subclasses should behave consistently with the parent class.  
- Avoid creating subclasses that violate parent contracts.  
- Example: If `Bird` has a method `fly()`, then `Penguin` (which cannot fly) should not inherit from `Bird` in a way that breaks the behavior.

---

### 4. **Interface Segregation Principle (ISP)**
> *Clients should not be forced to depend on methods they don’t use.*  
- Instead of one large interface, split into smaller, more specific ones.  
- Makes classes easier to implement and reduces unused code.  
- Example: Instead of a single `IMachine` interface with `print()`, `scan()`, `fax()`, create separate interfaces like `IPrinter`, `IScanner`.

---

### 5. **Dependency Inversion Principle (DIP)**
> *Depend on abstractions, not on concretions.*  
- High-level modules should not depend on low-level modules, both should depend on abstractions.  
- Promotes flexibility and decouples code.  
- Example: A `NotificationService` should depend on a `Notifier` interface, not directly on classes like `EmailNotifier` or `SMSNotifier`.

---

## 🎨 Design Patterns

The repo also includes Java implementations of common **Design Patterns**.

### 🔹 Creational Patterns
Focus on **object creation** while keeping system flexible:
- **Singleton** – ensure only one instance exists.  
- **Factory Method** – delegate object creation to subclasses.  
- **Abstract Factory** – create families of related objects.  
- **Builder** – construct complex objects step by step.  
- **Prototype** – clone existing objects.  

---

### 🔹 Structural Patterns
Organize classes & objects to form larger structures:
- **Adapter** – bridge incompatible interfaces.  
- **Decorator** – add responsibilities dynamically.  
- **Composite** – treat individual objects and groups uniformly.  
- **Facade** – provide a simplified interface to a subsystem.  
- **Proxy** – control access to another object.  
- **Bridge** – decouple abstraction from implementation.  
- **Flyweight** – share common state to save memory.  

---

### 🔹 Behavioral Patterns
Focus on **communication between objects**:
- **Observer** – notify multiple objects of state changes.  
- **Strategy** – define a family of algorithms, choose at runtime.  
- **Command** – encapsulate requests as objects.  
- **State** – allow an object to change behavior when state changes.  
- **Template Method** – define skeleton of algorithm, let subclasses refine steps.  
- **Chain of Responsibility** – pass requests along handlers until processed.  
- **Iterator** – provide sequential access without exposing structure.  
- **Memento** – capture and restore object state.  

---

## ⚙️ System Design Mini-Projects

Practical, real-world style examples applying principles & patterns together:
- **Parking Lot System**  
- **Notification Service (Email/SMS/Push)**  
- **Splitwise Clone**  
- **BookMyShow (Movie Ticket Booking)**  
- **ATM Machine Simulation**  
- **Elevator System**  

Each project has:
- Problem statement.  
- Key assumptions & constraints.  
- UML diagrams (if available).  
- Java implementation.  

---

## 🚀 Quick Start

```bash
# Clone the repository
git clone https://github.com/tusquake/Low-Level-Design.git
cd Low-Level-Design

# Compile all classes
javac -d out src/**/*.java

# Run an example
java -cp out solid.srp.ReportPrinterDemo
java -cp out patterns.behavioral.observer.ObserverDemo
