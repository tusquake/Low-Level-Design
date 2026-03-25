# Polymorphism

## What is it?
Polymorphism is derived from the Greek words *Poly* (many) and *Morph* (forms). It is the ability of an object to take on many forms. Most commonly, it allows a single interface to be used for a general class of actions.

## Simple Analogy
**A Person:**
-   At **home**, you are a *Family Member*.
-   At **work**, you are an *Employee*.
-   In a **shopping mall**, you are a *Customer*.
-   The **Person** is the same, but the **behavior** changes depending on the situation.

## Key Types in Java

### 1. **Compile-time Polymorphism (Static Binding)**
Achieved through **Method Overloading**. The compiler knows which method to call based on the number and type of arguments.
```java
void add(int a, int b) { ... }
void add(double a, double b) { ... }
```

### 2. **Runtime Polymorphism (Dynamic Binding)**
Achieved through **Method Overriding**. The JVM decides which method to call at runtime based on the actual object type, not the reference type.
```java
Animal myDog = new Dog();
myDog.makeSound(); // Calls Dog's version, even if reference is Animal.
```

## Key Concepts
-   **Upcasting**: Casting a child object to a parent reference (`Parent p = new Child();`).
-   **Dynamic Method Dispatch**: The mechanism by which a call to an overridden method is resolved at runtime.

---

## Relation with Spring Boot

Polymorphism is the backbone of Spring's flexibility and testability:

### 1. **Dependency Injection (DI) with Interfaces**
Spring injects the correct implementation at runtime. You depend on the interface, not the concrete class.
```java
@Autowired
private NotificationService service; // Can be EmailService or SMSService
```

### 2. **@Primary and @Qualifier**
When multiple beans of the same type (Polymorphism) exist, Spring uses these annotations to decide which "form" to inject.

### 3. **Method Overloading in Repositories**
Spring Data JPA allows you to overload find methods implicitly via query derivation or explicitly in custom implementations.

### 4. **Aspect-Oriented Programming (AOP)**
Spring uses Polymorphism (via Dynamic Proxies or CGLIB) to wrap your beans with "aspects" like logging or transaction management.

### 5. **Rest Templates & Message Converters**
Spring handles different response types (JSON, XML, String) polymorphically using a list of `HttpMessageConverters`.
