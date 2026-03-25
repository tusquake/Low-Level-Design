# 📌 Hexagonal Architecture (Ports and Adapters)

## 1️⃣ Definition (Interview Ready)
**Hexagonal Architecture**, also known as **Ports and Adapters**, is an architectural pattern that aims to create loosely coupled application components. It isolates the core business logic (the application's "inside") from outside concerns like databases, UI, and 3rd party APIs (the "outside").

- **Core (Hexagon)**: Contains the business logic and domain entities. It has no dependencies on external frameworks.
- **Ports**: Interfaces that define how the outside world can interact with the core (Input Ports) or how the core interacts with the outside (Output Ports).
- **Adapters**: Implementations that bridge the gap between a Port and a specific technology (e.g., a REST controller, a JPA repository).

---

## 2️⃣ Real-World Analogy
Think of a **USB Port** on your laptop.
- The **Laptop's Internal Circuitry (Core)** knows how to process data.
- The **USB Port (Port)** defines a standard interface for connecting devices.
- You can plug in a **Mouse, Keyboard, or External Drive (Adapters)**.

The laptop doesn't care *what* is plugged in, as long as it follows the USB standard (Interface). This allows you to upgrade your mouse (Internal technology) without changing your laptop's hardware (Business logic).

---

## 3️⃣ When to Use (Practical Scenarios)
- **Complex Domain Logic**: Where the business rules are the most important part and should be protected from technical changes.
- **Multiple Entry Points**: When your application needs to be accessed via REST API, CLI, and Messaging simultaneously.
- **Interchangeable Foundations**: If you want to be able to switch from a SQL database to a NoSQL one without rewriting business rules.
- **Testability**: When you want to unit test your core logic by mocking all "Ports" without needing a DB or Web Server.

---

## 4️⃣ When NOT to Use
- **Simple CRUD Apps**: If the application is just a pass-through to a database, Hexagonal architecture adds too much boilerplate.
- **Micro-Microservices**: If a service only has 2-3 classes, the overhead of ports/adapters is not worth it.
- **Prototyping**: When you need to ship a proof-of-concept as fast as possible.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
[ External Service / UI ] ----> [ Input Adapter (Controller) ]
                                          |
                                          v
      ( Hexagon Boundary )      << Input Port (Interface) >>
      |                          /                  \
      |            [ Core Business Logic / Domain Services ]
      |                          \                  /
      ( Hexagon Boundary )      << Output Port (Interface) >>
                                          |
                                          v
[ Database / 3rd Party API ] <--- [ Output Adapter (JPA/RestClient) ]
```

---

## 6️⃣ Complete Real Java Code Example
### 1. The Port (Output Interface)
```java
public interface PaymentPort {
    void process(double amount);
}
```

### 2. The Core Logic (Hexagon)
```java
public class PaymentService {
    private final PaymentPort paymentPort; // Dependency on Interface, not tech

    public PaymentService(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }

    public void makePayment(double amount) {
        if (amount <= 0) throw new RuntimeException("Invalid Amount");
        // Business logic here
        paymentPort.process(amount);
    }
}
```

### 3. The Adapter (Implementation)
```java
public class StripeAdapter implements PaymentPort {
    @Override
    public void process(double amount) {
        System.out.println("Calling Stripe API for amount: " + amount);
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In a typical Spring Boot project, the packages would look like this:
- `com.myapp.domain` (Core - No Spring annotations here!)
- `com.myapp.application.ports` (Interfaces)
- `com.myapp.infrastructure.adapters` (Controllers & Repositories)

### Persistence Adapter Example
A JPA implementation of a domain repository port:
```java
@Component
public class UserPersistenceAdapter implements UserOutputPort {
    @Autowired
    private JpaUserRepository jpaRepo;

    @Override
    public User save(User user) {
        UserEntity entity = UserEntity.fromDomain(user);
        return jpaRepo.save(entity).toDomain();
    }
}
```

---

## 8️⃣ Interview Questions
### Basic
1. **What is Hexagonal Architecture?**
   - **Answer**: It's an architectural pattern that isolates the core business logic (inside) from external concerns like databases, UI, and APIs (outside) using standard interfaces.

2. **What are "Ports" and "Adapters"?**
   - **Answer**: 
     - **Ports**: Interfaces that define the "contract" for how the outside world interacts with the core (Input) or vice versa (Output).
     - **Adapters**: Concrete implementations that translate between a Port and a specific technology (e.g., a Controller is an Input Adapter, a JPA Repository is an Output Adapter).

3. **How does it improve testability?**
   - **Answer**: Because the core logic only depends on interfaces (Ports), you can unit test 100% of your business rules by plugging in "Mock Adapters" without needing a real database, web server, or network connection.

### Intermediate
1. **Explain the difference between Driving (Input) and Driven (Output) adapters.**
   - **Answer**: 
     - **Driving (Input)**: These "trigger" the application (e.g., a REST API, a CLI command, or a Cron job). They call the Input Ports.
     - **Driven (Output)**: These are "triggered by" the application (e.g., a Database, an Email service, or a Message Queue). The Core calls these via Output Ports.

2. **Why should the Core have zero dependencies on external frameworks?**
   - **Answer**: To prevent "Vendor Lock-in" and ensure the business logic remains pure. If your core logic is littered with Spring or Hibernate annotations, migrating to a different framework or version becomes a massive, risky refactoring effort.

3. **How is Hexagonal Architecture different from the standard Layered Architecture?**
   - **Answer**: In **Layered Architecture**, the dependency flow is top-down (UI -> Service -> DB). In **Hexagonal**, the dependency flow is **Inward** (Infrastructure -> Domain). The Domain defines the interface (Port) that the Infrastructure must implement, effectively inverting the dependency.

### Advanced (Scenario-based)
1. **In a Hexagonal setup, how do you handle Domain events that need to be published to Kafka?**
   - **Answer**: You define an `EventPublisherPort` interface inside your Core. When the business logic finishes, it calls this port. You then create a `KafkaAdapter` in the infrastructure layer that implements this interface and handles the actual Kafka template calls.

2. **How do you map entities between the Domain layer and the Database layer without leaking technical details into the core?**
   - **Answer**: You maintain separate models: a **Domain Model** (POJO) and a **Persistence Entity** (with JPA annotations). You use a Mapper class (or MapStruct) in the Adapter layer to convert between the two. This ensures your Domain logic never knows about `@Table` or `@Column` annotations.

### Trick Question
- **Q**: Is Hexagonal Architecture the same as Clean Architecture?
- **A**: **Mostly yes.** Both share the same goal of isolating the domain. Clean Architecture (by Robert C. Martin) uses concentric circles, while Hexagonal (by Alistair Cockburn) uses the "inside/outside" metaphor, but the underlying principle of **Dependency Inversion** is identical.

---

## 9️⃣ Common Interview Follow-Up Questions
- **DIP (Dependency Inversion Principle)**: Understanding that the core defines the interface that the outside world must implement.
- **Onion Architecture**: Another similar pattern focusing on layers.
- **Mapping Overhead**: Dealing with multiple model versions (Domain, Entity, DTO).

---

## 🔟 Pros and Cons
### Pros
- ✅ **Technology Agnostic**: Switch databases or frameworks easily.
- ✅ **Testability**: Extreme ease of unit testing.
- ✅ **Focused Business Logic**: The core is pure Java/logic.

### Cons
- ❌ **Overhead**: Lots of interfaces and mapping code.
- ❌ **Complexity**: Can be confusing for developers used to simple layered apps.
- ❌ **Boilerplate**: Multiple models for the same data (Entity -> Domain -> DTO).

---

## 1️⃣1️⃣ One-Line Revision Summary
Hexagonal architecture isolates the core business logic from external technologies using standard interfaces (Ports) and their implementations (Adapters).
