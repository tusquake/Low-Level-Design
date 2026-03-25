# Encapsulation

## What is it?
Encapsulation is the bundling of data (variables) and the methods that operate on that data into a single unit (class), while hiding the internal implementation details from the outside world.

## Simple Analogy
**A Capsule (Medicine):**
- You take the medicine (the "data") without needing to know the exact chemicals inside.
- The capsule (the "class") protects the medicine and only lets it interact with your body through a controlled release mechanism (the "methods").

## Key Rules
1.  **Strictly Private Data**: All class variables should be `private`.
2.  **Public Accessors/Mutators**: Use `public` Getters and Setters to control how data is read or changed.
3.  **Validation**: Setters can include logic to prevent invalid data (e.g., setting a negative age).
4.  **Read-Only/Write-Only**: By providing only a Getter (and no Setter), you can make a property read-only.

## Why use it?
- **Security**: Protects the internal state from unauthorized changes.
- **Flexibility**: You can change the internal data type (e.g., from `int` to `long`) without breaking the code that uses your class.
- **Maintainability**: Centralized place for validation logic.

## Real-World Use
- **ATM Machine**: You can only interact with your balance through specific methods (Withdraw, Deposit). You cannot directly change the number in the bank's database.
- **Car Engine**: You use the pedals and steering wheel; you don't manually spray fuel into the cylinders.

---

## Relation with Spring Boot

Encapsulation is a core pillar in Spring Boot development:

### 1. **DTOs (Data Transfer Objects)**
We use private fields with Getters/Setters in DTOs to securely move data between layers (Controller -> Service -> Repository).

### 2. **Service Layer**
The Service layer encapsulates complex business logic. The Controller doesn't need to know *how* a user is saved or *how* a password is hashed; it just calls a public method.

### 3. **Dependency Injection**
Spring encapsulates the creation and wiring of Beans. You don't see the `new` keyword for your services; Spring handles the internal complexity and gives you a ready-to-use object.

### 4. **@ConfigurationProperties**
Instead of using `@Value` everywhere, we encapsulate related properties into a single Java class. This provides type-safe access to configuration.

### 5. **JPA Entities**
Entities encapsulate database logic. We use `@Column` and `@Id` to map private fields to database tables, maintaining a clean object-oriented interface.
