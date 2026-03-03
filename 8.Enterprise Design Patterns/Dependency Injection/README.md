# 📌 Dependency Injection (DI) Pattern

## 1️⃣ Definition (Interview Ready)
**Dependency Injection (DI)** is a design pattern used to implement **Inversion of Control (IoC)**. It allows a class to receive its dependencies from an external source (usually an **Inoc Container** like Spring) rather than creating them internally using the `new` keyword.

- **Objective**: To decouple the creation of objects from their usage.
- **Problem it solves**: Prevents "Hardcoded Dependencies" which make code difficult to test, maintain, and swap. 

---

## 2️⃣ Real-World Analogy
Think of a **Professional Chef** in a restaurant.
- **Without DI**: The Chef has to leave the kitchen, go to the farm, grow the vegetables, harvest them, and then come back to cook. The Chef is "Hardcoded" to a specific farm.
- **With DI**: The Chef stays in the kitchen. An **Assistant (DI Container)** delivers the fresh vegetables (Dependencies) to the Chef's counter. 

The Chef doesn't care which farm the vegetables came from, as long as they are "Vegetables" (Interface). This allows the restaurant to switch farms (Implementation) without the Chef changing his cooking process.

---

## 3️⃣ When to Use (Practical Scenarios)
- **Spring Framework**: Almost every `@Component` or `@Service` in Spring uses DI.
- **Unit Testing**: Essential for mocking dependencies (e.g., injecting a `MockDatabase` instead of a real one).
- **Loose Coupling**: When you want to be able to switch implementations (e.g., switching from `S3Storage` to `AzureStorage`) without changing the business logic.
- **Manageable Lifecycle**: When you want a container to handle the creation, scope (Singleton/Prototype), and destruction of objects.

---

## 4️⃣ When NOT to Use
- **Simple POJOs/DTOs**: You don't need DI for a `User` or `Address` data class; just use `new`.
- **Static Utility Classes**: If a class only has static methods (like `Math.abs()`), it doesn't need dependencies.
- **Small Scripts**: Where the overhead of setting up a DI container outweighs the benefit of the script itself.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
[ DI Container ] --(Injects)--> [ Client Class ]
                                      |
                                      v
                             << Interface >>
                            [ Dependency ]
                             /          \
                [ Concrete A ]      [ Concrete B ]
```

---

## 6️⃣ Complete Real Java Code Example
### 1. The Interface
```java
public interface MessageService {
    void sendMessage(String msg);
}
```

### 2. The Client Class (Uses DI)
```java
public class UserRegistration {
    private final MessageService msgService; // Dependency

    // Constructor Injection (Best Practice)
    public UserRegistration(MessageService service) {
        this.msgService = service;
    }

    public void register(String email) {
        System.out.println("User registered: " + email);
        msgService.sendMessage("Welcome to our platform!");
    }
}
```

### 3. The "Injector" (Manual DI)
```java
public class Main {
    public static void main(String[] args) {
        // We decide which implementation to use here
        MessageService emailService = new EmailServiceImpl();
        UserRegistration registration = new UserRegistration(emailService);
        
        registration.register("test@example.com");
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In Spring Boot, the framework handles the "Injection" for you.

### Types of Injection in Spring:
1. **Constructor Injection (Recommended)**:
   ```java
   @Service
   public class MyService {
       private final MyRepo repo;
       public MyService(MyRepo repo) { this.repo = repo; }
   }
   ```
2. **Setter Injection**: Using `@Autowired` on a setter method.
3. **Field Injection (Discouraged)**: Using `@Autowired` directly on a private field.

---

## 8️⃣ Interview Questions
### Basic
1. What is Dependency Injection?
2. What is the difference between IoC and DI? (Answer: IoC is the concept; DI is the specific implementation pattern).
3. Why is Constructor injection preferred over Field injection? (Answer: It allows for immutable dependencies, easier unit testing, and prevents `NullPointerException` at runtime).

### Intermediate
1. Mention the different ways to inject dependencies in Spring.
2. What is a **Circular Dependency**? How does Spring handle it? (Answer: When A depends on B and B depends on A. Spring handles it via Setter injection but usually fails with Constructor injection).
3. What is the **`@Qualifier`** annotation used for? (Answer: To specify which bean to inject when multiple implementations of an interface exist).

### Advanced (Scenario-based)
1. You have two implementations of any interface: `FastService` and `SafeService`. How do you ensure `FastService` is injected by default but allows for switching? (Answer: Use **`@Primary`** and **`@Qualifier`**).
2. How does DI help in making code more **Testable**? (Answer: By allowing us to inject Mock objects using frameworks like Mockito during unit tests).

### Trick Question
- **Q**: Does DI make the application faster?
- **A**: **No.** It might add a very slight overhead during application startup as the container wires beans together. However, it significantly improves **developer productivity** and **code quality**.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Bean Scopes**: Singleton, Prototype, Request, Session.
- **Post-Construct & Pre-Destroy**: Lifecycle hooks for injected beans.
- **Dependency Inversion Principle (D) in SOLID**: The underlying theory behind DI.

---

## 🔟 Pros and Cons
### Pros
- ✅ **Loose Coupling**: Classes are independent of their dependencies' implementations.
- ✅ **Testability**: Easily swap real components with mocks.
- ✅ **Maintainability**: Centralized configuration of object creation.

### Cons
- ❌ **Complexity**: Can make the code flow harder to follow for beginners.
- ❌ **Runtime Errors**: If a bean is missing, the error occurs at startup/runtime instead of compile time.
- ❌ **Boilerplate**: Sometimes requires many interfaces even for simple logic.

---

## 1️⃣1️⃣ One-Line Revision Summary
Dependency Injection is a pattern where a class's requirements are provided by an external container, ensuring loose coupling and high testability.
