# 📌 Strategy Design Pattern

## 1️⃣ Definition (Interview Ready)
The **Strategy Design Pattern** is a behavioral design pattern that defines a family of algorithms, encapsulates each one, and makes them interchangeable. It allows the algorithm to vary independently from the clients that use it.

- **When to use**: When you have multiple ways to perform a specific task (e.g., sorting, compression, payment) and want to switch between them at runtime.
- **Problem it solves**: It eliminates massive `if-else` or `switch-case` blocks that handle different logic variants, adhering to the **Open/Closed Principle**.

---

## 2️⃣ Real-World Analogy
Think of **Google Maps Navigation**.  
When you want to go from Point A to Point B, you can choose different "strategies":
- **Driving**: Fastest route by car.
- **Walking**: Shortest path via sidewalks.
- **Public Transport**: Based on bus/train timings.

The "Context" (Google Maps) remains the same, but the "Strategy" (Transport Mode) changes based on your choice.

---

## 3️⃣ When to Use (Practical Scenarios)
- **Payment Processing**: Handling multiple gateways (Stripe, PayPal, UPI) in an e-commerce backend.
- **Sorting/Filtering**: Applying different sorting algorithms based on data size or type.
- **File Exporting**: Exporting data as CSV, PDF, or Excel based on user preference.
- **Validation Logic**: Different validation rules for different user types (Admin vs. Customer).

---

## 4️⃣ When NOT to Use
- **Simple Logic**: If you only have two constant behaviors that will never change, a simple `if-else` is better. Don't overengineer.
- **Increased Complexity**: It introduces more classes and interfaces. If the application is small, this might be overkill.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
Client -> StrategyContext
              |
              v
      << Interface >>
          Strategy
        /    |     \
ConcreteA ConcreteB ConcreteC
```

---

## 6️⃣ Complete Real Java Code Example
### Interface
```java
public interface PaymentStrategy {
    void processPayment(double amount);
}
```

### Concrete Implementations
```java
public class CreditCardStrategy implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Credit Card payment of $" + amount);
    }
}

public class UpiStrategy implements PaymentStrategy {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing UPI payment of $" + amount);
    }
}
```

### Context Class
```java
public class PaymentContext {
    private PaymentStrategy strategy;

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void checkout(double amount) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set!");
        }
        strategy.processPayment(amount);
    }
}
```

### Client Usage
```java
public class Main {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        // Pay using Credit Card
        context.setStrategy(new CreditCardStrategy());
        context.checkout(150.0);

        // Switch to UPI at runtime
        context.setStrategy(new UpiStrategy());
        context.checkout(200.0);
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In Spring Boot, the Strategy pattern is often implemented using a **Map** to avoid manual `if-else` logic.

### Spring Example
```java
@Service
public class PaymentService {
    private final Map<String, PaymentStrategy> strategyMap;

    // Spring automatically injects all beans implementing PaymentStrategy into this Map
    public PaymentService(List<PaymentStrategy> strategies) {
        this.strategyMap = strategies.stream()
            .collect(Collectors.toMap(s -> s.getClass().getSimpleName(), s -> s));
    }

    public void pay(String type, double amount) {
        PaymentStrategy strategy = strategyMap.get(type);
        if (strategy == null) throw new RuntimeException("Invalid Payment Type");
        strategy.processPayment(amount);
    }
}
```

---

## 8️⃣ Interview Questions
### Basic
1. What is the main intent of the Strategy Pattern?
2. How does Strategy differ from a simple `if-else` block?
3. Which SOLID principle does the Strategy pattern primarily support? (Answer: Open/Closed Principle).

### Intermediate
1. How can you avoid passing the Strategy instance manually in a Spring Boot application?
2. What is the relationship between the Context and the Strategy?
3. Can the Strategy pattern be used with the Factory pattern? How?

### Advanced (Scenario-based)
1. In a microservices environment, how would you handle a strategy that requires external API calls vs one that is purely local?
2. How would you handle a situation where a new strategy needs to be added without redeploying the context? (Hint: Dynamic class loading or Scripting).

### Trick Question
- **Q**: Is Strategy Pattern the same as State Pattern?
- **A**: No. While the structure is similar, the **intent** is different. Strategy is about choosing "how" to do something (algorithms), while State is about "what" to do based on the object's internal state.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Difference from Factory**: Factory returns an object; Strategy uses an object.
- **Performance**: High numbers of concrete strategies can increase the memory footprint (object overhead).
- **Thread-safety**: If strategies are stateless (preferred), they are inherently thread-safe. If they hold state, you must manage it carefully (e.g., using `ThreadLocal` or prototypes).

---

## 🔟 Pros and Cons
### Pros
- ✅ **Encapsulation**: Algorithms are isolated from the client.
- ✅ **Runtime Switching**: Choose behavior dynamically.
- ✅ **Open/Closed**: Add new strategies without touching existing code.

### Cons
- ❌ **Client Awareness**: The client must know about different strategies to choose one.
- ❌ **Increased Object Count**: Every algorithm variant becomes a class.

---

## 1️⃣1️⃣ One-Line Revision Summary
Strategy pattern is about encapsulating interchangeable algorithms into separate classes to move conditional logic from the client to the architecture.
