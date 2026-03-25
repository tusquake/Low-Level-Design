# 📌 Database-per-Service Pattern

## 1️⃣ Definition (Interview Ready)
The **Database-per-Service Pattern** is a microservices pattern where each service has its own private data store. Other services can only access this data via the service's API; they are strictly forbidden from connecting directly to the database of another service.

- **Objective**: To ensure that services are loosely coupled and can be developed, deployed, and scaled independently.
- **Problem it solves**: Prevents the "Shared Database" anti-pattern, where a change in one service's table schema can break 10 other services. 

---

## 2️⃣ Real-World Analogy
Think of **Private Bank Accounts**.
- Suppose you and your coworkers are all part of the same "Company" (Application).
- **Shared DB (Old way)**: Everyone's salary is put into one giant box. Anyone can take money out or accidentally spill coffee on everyone's money. If the box breaks, nobody gets paid.
- **Database-per-Service (New way)**: Every employee has their own **Personal Bank Account (Private DB)**. 
- If you want to know how much money your coworker has, you must **Ask them (API Call)**. You cannot just walk into their bank and look at their balance.

If your coworker switches from one bank to another (e.g., MySQL to MongoDB), it doesn't affect your ability to ask them for their balance.

---

## 3️⃣ When to Use (Practical Scenarios)
- **Microservices Architecture**: When building a system where different services have different data needs (e.g., Order service needs SQL for transactions, while Product Search needs Elasticsearch).
- **Independent Scaling**: If the "Product" service has 1 million reads per second, you can scale its database separately from the "User" service database.
- **Polyglot Persistence**: Using the best database technology for each specific task.
- **Large Teams**: Allowing Team A to change their database schema without needing approval or coordination from Teams B, C, and D.

---

## 4️⃣ When NOT to Use
- **Monolithic Applications**: A single shared database is much simpler and faster.
- **Strong ACID Requirements across Services**: If you need real-time, 100% atomic transactions across multiple services (Distributed transactions are hard and slow).
- **Small Teams**: Managing 20 databases for 20 services requires significant DevOps overhead (DBA, Patches, Backups).

---

## 5️⃣ Structure Diagram (Textual UML)
```text
[ Service A ] ----> [ Database A (MySQL) ]
      |
(API Call)
      |
      v
[ Service B ] ----> [ Database B (MongoDB) ]
```

---

## 6️⃣ Complete Real Java Code Example
### Architecture Principle (Enforced by code separation)
Each service is in its own project/package with its own configuration.

**Order Service (Project 1)**
```java
public class OrderService {
    @Autowired
    private OrderRepo repository; // Connects to "orders_db"

    public void placeOrder(Order order) {
        repository.save(order);
        // If I need user info, I call UserService, I don't query "users_table"
    }
}
```

**User Service (Project 2)**
```java
public class UserService {
    @Autowired
    private UserRepo repository; // Connects to "users_db"

    public User getProfile(String id) {
        return repository.findById(id);
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In Spring Boot, each service usually has its own `application.yml` with unique `spring.datasource.url`.

### Cross-Service Queries (The Challenge)
Since you can't `JOIN` tables across different databases, you have two choices:
1. **API Composition**: The Gateway calls Service A and Service B and merges the results in memory.
2. **CQRS / View Materialization**: Create a separate "Read View" that aggregates data from multiple services via events.

---

## 8️⃣ Interview Questions
### Basic
1. **What is the Database-per-Service pattern?**
   - **Answer**: It's a microservices pattern where each service owns its own private database. No other service can access this database directly; they must use the service's API to get or change data.

2. **Why shouldn't microservices share a single database?**
   - **Answer**: 
     - **Tight Coupling**: A schema change in one service could break another.
     - **Single Point of Failure**: If the shared DB goes down, the entire system crashes.
     - **Scaling**: A high-load service's DB needs might negatively impact a low-load service.

3. **What is Polyglot Persistence?**
   - **Answer**: It's the practice of using different database technologies for different services based on their specific needs (e.g., MySQL for Orders, MongoDB for Product Catalogs, Redis for Sessions).

### Intermediate
1. **How do you handle a "JOIN" when data is split across two databases?**
   - **Answer**: 
     - **API Composition**: The Gateway/Client calls both Service A and Service B and joins the data in memory.
     - **CQRS**: Maintain a separate "read-only" database that pre-aggregates and joins the data from multiple services via events.

2. **How do you maintain data consistency without distributed transactions?**
   - **Answer**: By using the **Saga Pattern** (a sequence of local transactions) and **Eventual Consistency**. If a step in the sequence fails, compensating transactions are triggered to "undo" previous steps.

3. **What is the impact of this pattern on reporting and analytics?**
   - **Answer**: Since data is scattered, you can't run a simple SQL query across the whole system. You usually need an **ETL/ELT** process to stream data into a centralized **Data Warehouse** or **Data Lake** for reporting.

### Advanced (Scenario-based)
1. **You are migrating a monolith with 1 shared DB to microservices. Should you split the DB first or the code first?**
   - **Answer**: Split the **Code first**. Create "vertical slices" of the application that only access their own tables, even if they stay in the same physical DB. Once the code is decoupled, physically moving the data into separate databases becomes much easier and less risky.

2. **What happens if Service A needs 50% of the data from Service B in every request?**
   - **Answer**: This is a sign of **Tight Data Coupling**. You should either:
     - Merge the two services into one.
     - Replicate the necessary data from Service B to Service A via events.
     - Re-evaluate the boundaries of your microservices.

### Trick Question
- **Q**: Can two services share the same database *instance* but use different *schemas*?
- **A**: **Yes**, this is a "Logical" version of the pattern. It's a good middle ground for small teams to avoid infrastructure overhead while maintaining schema isolation. However, for true high availability and independent scaling, separate physical instances are better.

---

## 9️⃣ Common Interview Follow-Up Questions
- **CAP Theorem**: Consistency vs. Availability in distributed databases.
- **Sagas vs. 2PC (Two-Phase Commit)**: Why 2PC is avoided in microservices.
- **Foreign Keys**: How do you handle relationships without DB-level foreign key constraints? (Answer: Logical IDs and Application-level validation).

---

## 🔟 Pros and Cons
### Pros
- ✅ **Independence**: Teams can change schemas without breaking others.
- ✅ **Fault Isolation**: A failure in one DB doesn't crash the whole system.
- ✅ **Scalability**: Optimize and scale each DB based on its specific load.

### Cons
- ❌ **Complexity**: Maintaining many databases (backups, security, monitoring).
- ❌ **Query Challenges**: No SQL JOINs across services.
- ❌ **Data Integrity**: Maintaining consistency across services is difficult.

---

## 1️⃣1️⃣ One-Line Revision Summary
Database-per-service ensures microservice autonomy by giving each service its own private database, accessible only through APIs.
