# 📌 Repository Pattern

## 1️⃣ Definition (Interview Ready)
The **Repository Pattern** is a structural pattern that mediates between the domain and data mapping layers, acting like an in-memory collection of domain objects. It hides the details of how data is stored and retrieved (SQL, NoSQL, APIs) behind a clean, collection-like interface.

- **Purpose**: To decouple the business logic from the data access technology.
- **Problem it solves**: Prevents the business layer from being cluttered with database-specific code (SQL queries, Connection management) and makes the code more testable.

---

## 2️⃣ Real-World Analogy
Think of a **Warehouse Manager**.
- The **CEO (Business Logic)** wants to know "How many Blue T-shirts do we have?".
- The CEO doesn't care if the shirts are in Aisle 4, Box 9, or in a separate cold-storage facility.
- The **Warehouse Manager (Repository)** knows exactly where they are. He handles the logistics and just hands the "Blue T-shirts" (Domain Objects) to the CEO.

If the warehouse moves from a physical building to a cloud-based fulfillment center, the CEO's question remains the same, and only the Manager's internal process changes.

---

## 3️⃣ When to Use (Practical Scenarios)
- **Spring Data JPA**: Whenever you create an `interface extends JpaRepository`, you are using this pattern.
- **Domain Driven Design (DDD)**: To manage Aggregate roots and persist them.
- **Multiple Data Sources**: When you need to switch between a MySQL database and a Mock database for testing.
- **Unit Testing**: It allows you to mock the repository easily without needing a real database connection.

---

## 4️⃣ When NOT to Use
- **Simple Scripts**: If you are just writing a quick script to update one row, a Repository layer is overkill.
- **Active Record Pattern (sometimes)**: If your framework (like Ruby on Rails or older Hibernate) already ties the data access to the object, a separate repository might add redundant layers.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
Business Logic (Service)
      |
      v
<< Interface >> 
 UserRepository
      |
      +------> SQLUserRepository (Implementation)
      |
      +------> InMemoryUserRepository (for Tests)
```

---

## 6️⃣ Complete Real Java Code Example
### 1. The Interface (Contract)
```java
public interface UserRepository {
    User findById(Long id);
    void save(User user);
    void delete(Long id);
}
```

### 2. Implementation (Specific Technology)
```java
public class SqlUserRepository implements UserRepository {
    // Hidden DB details
    private JdbcTemplate jdbcTemplate; 

    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", id);
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO users...");
    }
}
```

### 3. Usage in Service
```java
public class UserService {
    private final UserRepository repository; // Injected

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void promoteToAdmin(Long userId) {
        User user = repository.findById(userId);
        user.setRole("ADMIN");
        repository.save(user);
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In Spring Boot, the pattern is so common it's built-in.

```java
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // You don't even write the implementation! 
    // Spring generates it at runtime.
    List<Order> findByStatus(String status); 
}
```

---

## 8️⃣ Interview Questions
### Basic
1. What is the goal of the Repository Pattern?
2. How does it help in unit testing?
3. Where does the Repository layer sit in a 3-tier architecture?

### Intermediate
1. What is the difference between a **DAO (Data Access Object)** and a **Repository**? (Answer: DAO is usually 1:1 with DB tables, while Repository is 1:1 with Domain Aggregates).
2. Can a Repository return a DTO? (Answer: Generally, it should return Domain Entities, but custom methods can return DTOs for performance).
3. How do you handle pagination in a Repository?

### Advanced (Scenario-based)
1. You are migrating from a Monolith with a single SQL DB to a Microservice with Redis + MongoDB. How does the Repository pattern help?
2. How do you implement a "Specification Pattern" with a Repository to handle complex dynamic queries?

### Trick Question
- **Q**: Is `JpaRepository` an implementation of the Repository Pattern?
- **A**: It is an **abstraction** that enables the pattern. The actual implementation is provided by Spring Data JPA (using `SimpleJpaRepository`).

---

## 9️⃣ Common Interview Follow-Up Questions
- **Decoupling**: Discuss why the service shouldn't know about `RowMapper` or `ResultSet`.
- **Transactions**: Should the transaction start in the Service or the Repository? (Answer: Usually the Service).

---

## 🔟 Pros and Cons
### Pros
- ✅ **Clean Code**: Business logic is free from persistence noise.
- ✅ **Maintainability**: Centralized data access logic.
- ✅ **Testability**: Easily mock repositories using libraries like Mockito.

### Cons
- ❌ **Abstration Overhead**: Sometimes it's just a thin wrapper over the DB driver.
- ❌ **Learning Curve**: New developers might find the extra layers confusing initially.

---

## 1️⃣1️⃣ One-Line Revision Summary
Repository pattern provides a collection-like interface to access domain objects while hiding the underlying storage technology.
