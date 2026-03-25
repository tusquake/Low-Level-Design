# Coupling and Cohesion

In Software Architecture, these two concepts define how well your code is structured. The goal is **Low Coupling** and **High Cohesion**.

## 1. Cohesion (Internal Strength)
Cohesion measures how focused a single module (class/method) is on a single task.
-   **High Cohesion**: A class does **one thing** and does it well (e.g., a `Calculator` class only calculates).
-   **Low Cohesion**: A class is "fat" and handles unrelated tasks (e.g., a `User` class that also sends emails and saves to a database).
-   **Analogy**: A **Chef** in a professional kitchen (High Cohesion) vs a **Person who cooks, cleans, drives, and fixes the sink** (Low Cohesion).

## 2. Coupling (Inter-Dependency)
Coupling measures how dependent one module is on another.
-   **Tight Coupling**: Changing one class forces you to change another (e.g., Class A directly uses private fields of Class B).
-   **Loose Coupling**: Classes interact via interfaces or abstractions. They don't know the internal details of each other.
-   **Analogy**: **Integrated Battery** in a phone (Tight Coupling - hard to replace) vs **AA Batteries** (Loose Coupling - easy to swap).

---

## Comparison Table

| Concept | Best Practice | Goal |
|---------|---------------|------|
| **Cohesion** | **High** | Single Responsibility Principle |
| **Coupling** | **Low** | Flexibility and Ease of Testing |

---

## Relation with Spring Boot

Spring Boot is specifically designed to promote Low Coupling and High Cohesion:

### 1. **Dependency Injection (DI)**
The biggest tool for **Low Coupling**. You don't "new" your dependencies; you inject an interface. You can swap `MySQLRepository` with `PostgreSQLRepository` without changing the Service.

### 2. **Service Layer Separation**
Promotes **High Cohesion**. Controllers handle requests, Services handle business logic, and Repositories handle data. Each is highly cohesive.

### 3. **Spring Events**
Used for even **Lower Coupling**. A `UserRegistrationService` can publish a `UserRegisteredEvent`. An `EmailService` listens to it. The registration service doesn't even know the email service exists.

### 4. **Aspect-Oriented Programming (AOP)**
Moves "cross-cutting concerns" (like logging or security) out of business classes, maintaining **High Cohesion** for the core logic.
