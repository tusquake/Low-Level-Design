# 📌 Layered Architecture (N-Tier)

## 1️⃣ Definition (Interview Ready)
**Layered Architecture** is one of the most common architectural patterns. It organizes an application into a set of horizontal layers, each having a specific responsibility and role. A layer can only communicate with the layer directly below it (Closed Layer) or sometimes layers further down (Open Layer).

- **The Standard Layers**:
  1. **Presentation (UI)**: Handles user interaction and API responses.
  2. **Business (Service)**: Handles core logical processing and rules.
  3. **Persistence (DAL)**: Handles data mapping and database access.
  4. **Database**: The physical storage of data.
- **Problem it solves**: Prevents "Spaghetti Code" by enforcing a strict separation of concerns, making the system easier to understand and maintain.

---

## 2️⃣ Real-World Analogy
Think of a **Restaurant Service**.
- **The Waiter (Presentation Layer)**: Interacts with the customer, takes the order, and returns the food. The waiter doesn't know how to cook.
- **The Chef (Business Layer)**: Receives the order and applies the recipes (logic). The chef doesn't know how to wash the dishes or where the ingredients come from.
- **The Kitchen Assistant (Persistence Layer)**: Fetches the raw ingredients from the pantry based on the chef's request.
- **The Pantry (Database)**: Where all the ingredients are physically stored.

Each person has a clear job, and they always talk to each other in a specific order (Customer -> Waiter -> Chef -> Assistant -> Pantry).

---

## 3️⃣ When to Use (Practical Scenarios)
- **Standard Web Applications**: Most common for enterprise applications, e-commerce, and internal management tools.
- **New Projects**: A great starting point for most applications due to its simplicity and familiarity.
- **Small to Medium Organizations**: Where developers need a standard way of organizing code that everyone understands.
- **Strict Compliance Apps**: Where you need to ensure that database logic can never be bypassed by the UI directly.

---

## 4️⃣ When NOT to Use
- **Simple Microservices**: If a service only has 5 classes, splitting them into 3 layers might be redundant.
- **Highly Complex Domains**: Where the logic is very intricate; patterns like **Domain Driven Design (DDD)** or **Hexagonal Architecture** might be better to prevent the "Anemic Domain Model".
- **Performance Critical Systems**: If the overhead of mapping data between 3-4 separate layers (DTO -> Entity -> Data) is too high.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
[ Presentation Layer (Controller) ]
           |
           v
[ Business Layer (Service) ]
           |
           v
[ Persistence Layer (Repository/DAO) ]
           |
           v
[ Database Layer (SQL/NoSQL) ]
```

---

## 6️⃣ Complete Real Java Code Example
### 1. Presentation Layer
```java
public class OrderController {
    private OrderService service; // Depends on Service

    public void checkout(int id) {
        String result = service.placeOrder(id);
        System.out.println("Return to UI: " + result);
    }
}
```

### 2. Business Layer
```java
public class OrderService {
    private OrderRepository repository; // Depends on Repository

    public String placeOrder(int id) {
        if (id < 0) throw new IllegalArgumentException("Invalid ID");
        // Business logic: check stock, apply discount, etc.
        return repository.save(id);
    }
}
```

### 3. Persistence Layer
```java
public class OrderRepository {
    public String save(int id) {
        // Technical logic: SQL Query, Connection Management
        return "Order-" + id + " saved in MySQL";
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
Spring Boot projects almost always follow this pattern by convention:

- **`@RestController`**: Presentation Layer.
- **`@Service`**: Business Layer.
- **`@Repository`**: Persistence Layer.

### Flow of Data
1. Request hits `@RestController`.
2. Controller calls `@Service`.
3. Service calls `@Repository`.
4. Repository uses `@Entity` to talk to the DB.

---

## 8️⃣ Interview Questions
### Basic
1. What is Layered Architecture?
2. What are the four typical layers of an enterprise application?
3. What is the main benefit of this pattern? (Answer: Separation of concerns and ease of development).

### Intermediate
1. Explain the difference between an **Open Layer** and a **Closed Layer**.
2. What is "Leaky Abstraction" in the context of layers?
3. What is an **Anemic Domain Model**? (Answer: When the business layer has all the logic and domain objects are just data holders/POJOs).

### Advanced (Scenario-based)
1. How do you handle cross-cutting concerns like Logging or Security in a layered architecture? (Answer: Use AOP - Aspect Oriented Programming or Filters/Interceptors).
2. What happens if the Presentation layer starts calling the Repository layer directly? (Answer: It breaks the pattern, making the system harder to test and maintaining a "Tight Coupling" between UI and DB).

### Trick Question
- **Q**: Is Layered Architecture the same as MVC?
- **A**: **No.** MVC is a pattern for the **Presentation Layer** (how UI and logic interact), while Layered Architecture is a pattern for the **Entire System** organization.

---

## 9️⃣ Common Interview Follow-Up Questions
- **DIP (Dependency Inversion)**: How do we use Interfaces to make layers swap-able?
- **DTOs vs. Entities**: Which layer should handle which object?
- **Circular Dependencies**: What happens if Service A calls Service B and Service B calls Service A?

---

## 🔟 Pros and Cons
### Pros
- ✅ **Simplicity**: Very easy to learn and implement.
- ✅ **Consistency**: Most developers are familiar with it.
- ✅ **Scalability (Development)**: Different teams can work on different layers.

### Cons
- ❌ **Tight Coupling**: Layers often become highly dependent on each other.
- ❌ **"Sinkhole" Anti-pattern**: When most requests just pass through all layers without doing any work.
- ❌ **Monolithic Tendency**: It often leads to large, difficult-to-scale monoliths.

---

## 1️⃣1️⃣ One-Line Revision Summary
Layered architecture organizes an application into horizontal tiers, ensuring that each layer has a distinct responsibility and communicates only with the layer below it.
