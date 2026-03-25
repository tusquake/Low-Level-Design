# Abstraction

## What is it?
Abstraction is the process of hiding the internal complexity of a system and exposing only the essential features to the user. It helps in reducing programming complexity and effort.

## Simple Analogy
**Driving a Car:**
-   You know that pressing the **Brake** stops the car and the **Accelerator** speeds it up.
-   You do **not** need to know the internal combustion details, the brake fluid pressure, or the gear ratios.
-   The dashboard and pedals are the **Abstract Interface** provided to the driver.

## How to achieve it in Java?

### 1. **Abstract Classes (0 to 100% abstraction)**
A class declared with the `abstract` keyword. It can have both abstract methods (without body) and concrete methods (with body).
```java
abstract class Vehicle {
    abstract void start(); // Abstract
    void stop() { System.out.println("Stopping..."); } // Concrete
}
```

### 2. **Interfaces (100% abstraction)**
A blueprint of a class. It only contains abstract methods (until Java 8, which added default and static methods). It enforces a contract on the implementing classes.
```java
interface Shape {
    void draw(); // Implicitly abstract
}
```

## Abstract Class vs Interface

| Feature | Abstract Class | Interface |
|---------|----------------|-----------|
| **Methods** | Can have both abstract & concrete | Primarily abstract (mostly) |
| **Variables** | Can have final, non-final, static | Only public static final |
| **Inheritance** | A class can extend only ONE | A class can implement MANY |
| **Relationship** | "IS-A" | "IS-CAPABLE-OF" |

---

## Relation with Spring Boot

Abstraction is fundamental to how Spring Boot works:

### 1. **Interface-based Programming**
In a typical Spring app, we define an interface for our Service and an implementation.
```java
public interface UserService { ... }
@Service
public class UserServiceImpl implements UserService { ... }
```
This allows us to swap the implementation (abstraction) without changing the Controller.

### 2. **Spring Data Repositories**
`JpaRepository` is an abstraction. You don't write the SQL or JDBC code to save a user; Spring provides the implementation under the hood.

### 3. **RestTemplate & WebClient**
These are abstractions over HTTP calls. You don't need to know how the raw TCP/IP sockets or HTTP headers are managed; you just use `getForObject()` or `exchange()`.

### 4. **Standardized Interfaces**
Spring uses standard interfaces like `Serializable`, `Cloneable`, or its own `FactoryBean` to provide a consistent way to handle objects regardless of their complexity.
