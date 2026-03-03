# 📌 Event-Driven Architecture (EDA)

## 1️⃣ Definition (Interview Ready)
**Event-Driven Architecture (EDA)** is a software architecture pattern where the flow of the program is determined by events—actions such as a user click, a message from another service, or a sensor output.

- **Publisher (Producer)**: The component that detects the event and "broadcasts" it. It doesn't know who will receive it.
- **Consumer**: The component that listens for specific events and reacts to them.
- **Event**: A significant change in state (e.g., `Order_Placed`, `User_Registered`).
- **Problem it solves**: Decouples services so they don't need to call each other directly (Request-Response). This improves scalability and resilience.

---

## 2️⃣ Real-World Analogy
Think of a **Radio Station**.
- The **Radio Station (Publisher)** broadcasts music and news as "events" over a specific frequency.
- The station doesn't know how many people are listening or who they are.
- Anyone with a **Radio (Consumer)** tuned to that frequency can hear the broadcast and react (dance, pay attention, or ignore it).
- If the listener's radio is off, the station keeps broadcasting anyway.

---

## 3️⃣ When to Use (Practical Scenarios)
- **Asynchronous Workflows**: Sending a confirmation email after an order is placed without making the user wait.
- **Microservices Communication**: Reducing direct dependencies between services (e.g., Order service doesn't need to check if the Loyalty Points service is up).
- **Real-time Analytics**: Buffering and processing data streams (e.g., stock market tracking, IoT sensors).
- **Replica/Cache Updates**: Triggering a cache invalidation or updating a read-database when a record changes.

---

## 4️⃣ When NOT to Use
- **Simple CRUD Apps**: If you only have one service and one database.
- **Strict Sequence with High Latency Sensitivity**: If Step B MUST happen immediately after Step A and any delay is unacceptable.
- **Debugging Complexity**: If you have 50 services and it's hard to trace which service triggered what, the "Spaghetti of Events" can become a nightmare.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
[ Producer ] --(Publishes ItemAddedEvent)--> [ Event Broker / Bus ]
                                               |
        +--------------------------------------+-----------------------+
        |                                      |                       |
        v                                      v                       v
[ Inventory Consumer ]                [ Analytics Consumer ]       [ Email Consumer ]
```

---

## 6️⃣ Complete Real Java Code Example
### 1. The Event Object
```java
public class OrderPlacedEvent {
    private String orderId;
    public OrderPlacedEvent(String id) { this.orderId = id; }
    public String getOrderId() { return orderId; }
}
```

### 2. The Publisher
```java
public class OrderService {
    private EventBus eventBus;

    public void checkout(String orderId) {
        System.out.println("Processing Order: " + orderId);
        // Business logic here...
        
        // Broadcast the event
        eventBus.publish(new OrderPlacedEvent(orderId));
    }
}
```

### 3. The Consumers
```java
public class EmailService {
    @Subscribe
    public void onOrderPlaced(OrderPlacedEvent event) {
        System.out.println("Email Sent for " + event.getOrderId());
    }
}

public class ShippingService {
    @Subscribe
    public void onOrderPlaced(OrderPlacedEvent event) {
        System.out.println("Package scheduled for " + event.getOrderId());
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
Spring provides a built-in event mechanism called **`ApplicationEventPublisher`**.

### Spring Internal Events
```java
@Component
public class MyPublisher {
    @Autowired
    private ApplicationEventPublisher publisher;

    public void doSomething() {
        publisher.publishEvent(new CustomEvent(this, "Data"));
    }
}

@Component
public class MyListener {
    @EventListener
    public void handleEvent(CustomEvent event) {
        // React here
    }
}
```
For cross-service events, we use **Spring Cloud Stream** with **Kafka** or **RabbitMQ**.

---

## 8️⃣ Interview Questions
### Basic
1. What is an Event-Driven Architecture?
2. What is the role of an Event Broker?
3. Mention three popular message brokers for EDA. (Answer: Kafka, RabbitMQ, ActiveMQ).

### Intermediate
1. Difference between **Push-based** and **Pull-based** event consumption?
2. What are "Idempotent Consumers"? Why are they important in EDA?
3. Explain the "Exactly-Once" vs "At-Least-Once" delivery semantics.

### Advanced (Scenario-based)
1. How do you handle "Event Versioning" when the structure of an event changes but old consumers are still running? (Answer: Use Avro/Protobuf with Schema Registry or maintain multiple event types).
2. What is **Event Sourcing**? How is it different from EDA? (Answer: Event Sourcing uses events as the source of truth/state, while EDA uses events for communication).

### Trick Question
- **Q**: Does EDA eliminate all tight coupling?
- **A**: **No.** While it eliminates **Temporal coupling** (services don't need to be up at the same time), it introduces **Schema coupling** (consumers depend on the event's data structure).

---

## 9️⃣ Common Interview Follow-Up Questions
- **Message Ordering**: How to ensure events are processed in the correct order (e.g., Kafka Partitions).
- **Backpressure**: What happens if the consumers are too slow to keep up with the events?
- **Dead Letter Queues (DLQ)**: Handling failed event processing.

---

## 🔟 Pros and Cons
### Pros
- ✅ **Loose Coupling**: Services are independent.
- ✅ **High Scalability**: Producers and consumers scale independently.
- ✅ **Extensibility**: Add new consumers without changing the producer.

### Cons
- ❌ **Complexity**: Distributed tracing and debugging is harder.
- ❌ **Consistency**: Usually implies eventual consistency.
- ❌ **Overhead**: Managing a message broker infrastructure.

---

## 1️⃣1️⃣ One-Line Revision Summary
EDA uses events to trigger asynchronous actions across decoupled services, enabling high scalability and independent evolution of system components.
