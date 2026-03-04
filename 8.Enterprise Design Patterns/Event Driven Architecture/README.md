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
1. **What is an Event-Driven Architecture?**
   - **Answer**: It's an architecture where services communicate by publishing and consuming events. Instead of a service calling another service directly, it broadcasts that "something happened," and any interested service can react to it.

2. **What is the role of an Event Broker?**
   - **Answer**: It acts as the "Middleman" or "Post Office." It receives events from publishers, stores them (often temporarily), and ensures they are delivered to the correct consumers.

3. **Mention three popular message brokers for EDA.**
   - **Answer**: **Apache Kafka**, **RabbitMQ**, and **Amazon SNS/SQS**.

### Intermediate
1. **Difference between Push-based and Pull-based event consumption?**
   - **Answer**: 
     - **Push-based**: The broker actively sends the event to the consumer as soon as it arrives (e.g., RabbitMQ). Good for low latency.
     - **Pull-based**: The consumer asks the broker for new events when it's ready (e.g., Kafka). Good for handling large bursts of data without overwhelming the consumer (Backpressure).

2. **What are "Idempotent Consumers"? Why are they important in EDA?**
   - **Answer**: An idempotent consumer is one that can process the same event multiple times without changing the result beyond the first time. They are crucial because most brokers only guarantee "At-Least-Once" delivery, meaning a consumer might receive the same event twice due to network retries.

3. **Explain the "Exactly-Once" vs "At-Least-Once" delivery semantics.**
   - **Answer**: 
     - **At-Least-Once**: Guarantees the message is delivered, but might be delivered multiple times.
     - **Exactly-Once**: Guarantees the message is delivered exactly one time. This is much harder to achieve and often requires support from both the broker and the consumer logic.

### Advanced (Scenario-based)
1. **How do you handle "Event Versioning" when the structure of an event changes but old consumers are still running?**
   - **Answer**: 
     - Use a **Schema Registry** (like Confluent Schema Registry) to manage backward/forward compatibility.
     - Add a `version` field to the event header.
     - Ensure the consumer code is designed to ignore unknown fields (Loose coupling).

2. **What is Event Sourcing? How is it different from EDA?**
   - **Answer**: 
     - **EDA**: Uses events for **Communication** between services. The "Current State" is still stored in a traditional database.
     - **Event Sourcing**: Uses events as the **Source of Truth**. To find the "Current State" of an object, you re-play all its historical events from the beginning.

### Trick Question
- **Q**: Does EDA eliminate all tight coupling?
- **A**: **No.** It eliminates **Temporal coupling** (Service A doesn't need Service B to be online at the same time). However, it introduces **Schema coupling** (Service B must understand the exact data format produced by Service A). If Service A changes its event format without warning, Service B will break.

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
