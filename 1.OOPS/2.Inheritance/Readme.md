# Inheritance

## What is it?
Inheritance is the mechanism by which one class (**Subclass/Child**) can acquire the properties and behaviors (methods) of another class (**Superclass/Parent**). It represents an **"IS-A"** relationship.

## Simple Analogy
**Smartphones:**
-   **Electronic Device (Parent)**: Has a power button, battery, and screen.
-   **Smartphone (Child)**: **IS-A** Electronic Device. It inherits the screen and battery but adds a camera and internet connectivity.

## Key Concepts
1.  **Code Reusability**: You don't have to rewrite common logic (like `getName()`) for every class.
2.  **`extends` Keyword**: Used in Java to inherit from a class.
3.  **Method Overriding**: The child class can provide its own implementation of a parent's method (using `@Override`).
4.  **`super` Keyword**: Used to refer to the immediate parent class's properties or methods.

## Types in Java
-   **Single Inheritance**: One child, one parent.
-   **Multilevel Inheritance**: Grandparent -> Parent -> Child.
-   **Hierarchical Inheritance**: One parent, multiple children.
-   *(Note: Java does NOT support Multiple Inheritance with classes to avoid the "Diamond Problem", but achieves it via Interfaces).*

---

## Relation with Spring Boot

Inheritance is heavily used in Spring Boot for reducing boilerplate and enforcing structure:

### 1. **Spring Data JPA Repositories**
Your custom repository **inherits** from `JpaRepository` or `CrudRepository`.
```java
public interface UserRepository extends JpaRepository<User, Long> { 
    // Inherits save(), delete(), findAll() automatically!
}
```

### 2. **Base Entities (@MappedSuperclass)**
Instead of adding `id`, `createdAt`, and `updatedAt` to every entity, we put them in a `BaseEntity` and have our entities inherit from it.
```java
@MappedSuperclass
public abstract class BaseEntity {
    @Id @GeneratedValue private Long id;
    private LocalDateTime createdAt;
}
```

### 3. **Exception Handling**
Custom exceptions typically inherit from `RuntimeException`.
```java
public class UserNotFoundException extends RuntimeException { ... }
```

### 4. **Web Security**
In older Spring Security versions, you would inherit from `WebSecurityConfigurerAdapter` to customize security rules.
