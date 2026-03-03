# 📌 CQRS (Command Query Responsibility Segregation)

## 1️⃣ Definition (Interview Ready)
**CQRS** is an architectural pattern that separates the models for reading and writing data. 

- **Command**: Operations that change the state of the system (Create, Update, Delete). They should not return data (except perhaps an ID or status).
- **Query**: Operations that retrieve data from the system. They should not modify the state.
- **Problem it solves**: In complex systems, the same data model is often used for both reads and writes, leading to performance bottlenecks, complicated validation logic, and scaling issues. CQRS allows you to optimize read and write paths independently.

---

## 2️⃣ Real-World Analogy
Think of a **Library**.
- **The Librarian (Command Side)**: Handles books coming in, registration, and shelf organization. They ensure the integrity of the collection.
- **The Catalog/Index (Query Side)**: A separate system (like a digital portal) that allows you to search for books by title, author, or genre. It is optimized for fast searching and doesn't care about the logistics of how the books are stored on the shelf.

Updating the shelf (Command) eventually reflects in the Catalog (Query), keeping them separated but synchronized.

---

## 3️⃣ When to Use (Practical Scenarios)
- **High-Read Scalability**: When your application has a massive read-to-write ratio (e.g., Social Media feeds, E-commerce product listings).
- **Complex Domain Logic**: When the validation rules for saving data are very different from how you need to display that data.
- **Microservices Architecture**: Keeping the write-optimized database separate from a read-optimized View store (like Elasticsearch or Redis).
- **Event Sourcing**: CQRS is almost always used when implementing Event Sourcing to rebuild the "Current State" for queries.

---

## 4️⃣ When NOT to Use
- **Simple CRUD Applications**: If your read and write models are identical, CQRS adds unnecessary complexity.
- **Small Teams**: It requires maintaining two models (and often two databases), which increases development and maintenance overhead.
- **Strict Real-Time Consistency**: If the read side must be perfectly consistent with the write side *instantly*, the eventual consistency model of many CQRS implementations may be a challenge.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
Client 
  |
  +------> CommandService ----> Write DB (Optimized for Updates)
  |           |
  |      (Sync/Async Event)
  |           |
  +------> QueryService <---- Read DB (Optimized for Searching)
```

---

## 6️⃣ Complete Real Java Code Example
### The Domain Model
```java
class User {
    private int id;
    private String name;
    // Constructor, Getters
}
```

### Command Side (Writes)
```java
class UserCommandService {
    private Map<Integer, User> writeDatabase;

    public void createUser(int id, String name) {
        // Complex validation logic here
        writeDatabase.put(id, new User(id, name));
        System.out.println("User Created in Write Store");
    }
}
```

### Query Side (Reads)
```java
class UserQueryService {
    private Map<Integer, User> readDatabase; // Could be a flattened view

    public User getUser(int id) {
        System.out.println("Fetching User from Read Store");
        return readDatabase.get(id);
    }
}
```

### Event-Driven Synchronization (Typical in Enterprise)
```java
public class EventBus {
    public void publish(Object event) { /* Notify Read Side */ }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In Spring Boot, we often use libraries like **Axon Framework** for full CQRS/Event Sourcing, or simply separate our Repository layers.

### Spring Data Strategy
- **CommandService**: Uses `@Transactional` and a standard JPA Repository to save entities.
- **QueryService**: Might use a different Repository that returns DTOs (Data Transfer Objects) directly via Projections or even hits a separate NoSQL database like MongoDB.

---

## 8️⃣ Interview Questions
### Basic
1. What does CQRS stand for?
2. Why should we separate the Read side from the Write side?
3. Does CQRS require two different databases? (Answer: No, it can be two different sets of classes/APIs hitting the same DB, but separate DBs are common for scaling).

### Intermediate
1. Explain the difference between a DTO and a Command in CQRS.
2. How do you handle synchronization between the Command and Query databases?
3. What is "Eventual Consistency" in the context of CQRS?

### Advanced (Scenario-based)
1. You have a requirement where a user creates a profile and must see it immediately on the next screen. How would you handle this with a CQRS architecture that uses an async read store? (Answer: Client-side optimistic UI, or passing the Write version to the Read request).
2. How does CQRS improve performance in a microservices environment?

### Trick Question
- **Q**: Is CQRS an implementation of the Mediator pattern?
- **A**: Often yes (using tools like MediatR in .NET or Handlers in Java), but CQRS is an **architectural pattern** for state management, while Mediator is a **design pattern** for object communication.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Event Sourcing vs. CQRS**: CQRS doesn't *require* Event Sourcing, but Event Sourcing *requires* CQRS.
- **Latency**: Discuss the latency between a write operation and its visibility in the query store.
- **Error Handling**: What happens if the write succeeds but the query store update fails? (Mention Outbox Pattern or Retries).

---

## 🔟 Pros and Cons
### Pros
- ✅ **Independent Scaling**: Scale reads separately from writes.
- ✅ **Optimized Schemas**: Read DB can be denormalized for speed.
- ✅ **Security**: Easier to manage who can change data vs. who can see it.

### Cons
- ❌ **Complexity**: More code, more moving parts.
- ❌ **Data Lag**: Read store might be slightly behind the write store.

---

## 1️⃣1️⃣ One-Line Revision Summary
CQRS separates the "Change" logic from the "Read" logic to handle high-load systems and complex domain requirements independently.
