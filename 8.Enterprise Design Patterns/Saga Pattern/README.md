# 📌 Saga Pattern

## 1️⃣ Definition (Interview Ready)
The **Saga Pattern** is a failure management pattern used in microservices to manage **distributed transactions**. Since traditional ACID transactions (2PC - Two-Phase Commit) don't scale well in microservices, a Saga breaks a large transaction into a sequence of smaller, local transactions.

- **Sequence**: Each local transaction updates the database and publishes an event/message to trigger the next local transaction.
- **Compensating Transactions**: If one step fails, the Saga executes a series of "undo" operations (compensating transactions) to maintain data consistency.
- **Problem it solves**: Maintains "Eventual Consistency" across multiple microservices without locking resources across the network.

---

## 2️⃣ Real-World Analogy
Think of **Booking a Vacation**.
1. **Step 1**: Book a Flight.
2. **Step 2**: Book a Hotel.
3. **Step 3**: Book a Rental Car.

If you successfully book the flight and hotel, but **Step 3 (Rental Car) fails**, you don't just stop. You have to:
- **Undo Step 2**: Cancel the Hotel booking.
- **Undo Step 1**: Cancel the Flight booking.

This "Undo" logic is the compensating transaction of the Saga.

---

## 3️⃣ When to Use (Practical Scenarios)
- **E-commerce Order Processing**: Involving Order, Payment, and Inventory services.
- **Travel Reservation Systems**: Handling flights, hotels, and insurance.
- **Financial Transfers**: Moving money between two different banking systems.
- **Any Distributed System**: Where you need to maintain consistency across services that own their own databases.

---

## 4️⃣ When NOT to Use
- **Monolithic Applications**: Use standard database transactions (`@Transactional`) instead.
- **Tightly Coupled Services**: If services can't fail independently, your architecture might need rethinking before applying Saga.
- **Small Transactions**: If the operation only involves one or two quick database updates in the same service.

---

## 5️⃣ Structure Diagram (Textual UML)
### Choreography (Event-based)
```text
Service A --(Done)--> Service B --(Done)--> Service C
   ^                    |                    |
   +---(Fail/Undo)------+---(Fail/Undo)------+
```

### Orchestration (Command-based)
```text
      [ Orchestrator ]
       /      |      \
Service A  Service B  Service C
```

---

## 6️⃣ Complete Real Java Code Example
### Simplified Orchestrator Logic
```java
public class OrderSagaOrchestrator {
    private PaymentService payment;
    private InventoryService inventory;
    private OrderService order;

    public void execute(String orderId) {
        // Step 1: Create Order
        order.create(orderId);

        // Step 2: Payment
        boolean paySuccess = payment.charge(orderId);
        if (!paySuccess) {
            order.cancel(orderId); // Compensation
            return;
        }

        // Step 3: Inventory
        boolean invSuccess = inventory.reserve(orderId);
        if (!invSuccess) {
            payment.refund(orderId); // Compensation 1
            order.cancel(orderId);   // Compensation 2
            return;
        }

        System.out.println("Saga Completed Successfully!");
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In production, we use frameworks like **Eventuate**, **Axon**, or **Camunda** to manage Saga state.

### Using Spring Cloud Stream (Choreography)
Services listen to Kafka/RabbitMQ topics:
```java
@StreamListener(OrderChannels.INPUT)
public void handleOrderCreated(OrderCreatedEvent event) {
    // Process payment and publish PaymentProcessedEvent or PaymentFailedEvent
}
```

---

## 8️⃣ Interview Questions
### Basic
1. **What is a Saga pattern?**
   - **Answer**: A failure management pattern for distributed systems. It's a sequence of local transactions, where each transaction updates a database and publishes an event or message to trigger the next step.

2. **What is a "Compensating Transaction"?**
   - **Answer**: It's an "Undo" operation that reverses the effect of a previously successful local transaction if a subsequent step in the Saga fails.

3. **How does Saga differ from 2PC (Two-Phase Commit)?**
   - **Answer**: 2PC is a **synchronous** protocol that locks database resources across multiple services until the transaction finishes (not scalable for microservices). Saga is **asynchronous** and relies on eventual consistency without long-lived locks.

### Intermediate
1. **Explain Choreography vs. Orchestration in Sagas.**
   - **Answer**: 
     - **Choreography**: Decentralized. Each service produces and listens to events from other services. Easy for simple workflows but hard to debug for complex ones.
     - **Orchestration**: Centralized. A "Saga Manager" or "Orchestrator" tells each service what to do and when. Easier to manage and monitor but introduces a central controller.

2. **What happens if a Compensating Transaction itself fails?**
   - **Answer**: This is a critical edge case. You must use **Retries** with exponential backoff. If it still fails after many retries, the system should move the message to a **Dead Letter Queue (DLQ)** for manual intervention by an operator.

3. **Is a Saga ACID compliant?**
   - **Answer**: **No.** It lacks full **Isolation**. In a traditional transaction, other users can't see the "intermediate" state of your data. In a Saga, because each step commits to its own DB, another process could see the "Flight Booked" status before the "Hotel Booked" status, leading to "dirty reads."

### Advanced (Scenario-based)
1. **How do you handle "Idempotency" in a Saga?**
   - **Answer**: Since messages might be delivered more than once (e.g., Kafka retries), every service must check if it has already processed a specific **Transaction ID**. If it sees a duplicate, it should simply ignore it or return the previous success response instead of executing the logic again.

2. **How would you monitor the progress of a long-running Saga?**
   - **Answer**: 
     - Use a **Saga Log** or a specialized database to track the current state of each orchestrator instance.
     - Use distributed tracing tools like **Zipkin** or **Jaeger** to see the flow of events across services.

### Trick Question
- **Q**: Does Saga guarantee 100% data consistency at all times?
- **A**: **No.** It guarantees **Eventual Consistency**. There is a temporary period where data across different services might look inconsistent until all steps (or all compensation steps) are finalized.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Performance**: Impact of message broker latency.
- **Complexity**: Debugging distributed Sagas vs. monolithic transactions.
- **Frameworks**: Mentioning Axon, Seata, or Cadence/Temporal.

---

## 🔟 Pros and Cons
### Pros
- ✅ **Scalability**: No long-lived locks on database rows.
- ✅ **Resilience**: Services can fail and recover independently.
- ✅ **Decoupling**: Services only need to know about events or the orchestrator.

### Cons
- ❌ **Complexity**: Writing compensating logic is difficult.
- ❌ **Debugging**: Hard to trace a transaction across 5 different services.
- ❌ **No Isolation**: Other transactions might see "dirty" data before the Saga finishes.

---

## 1️⃣1️⃣ One-Line Revision Summary
Saga manages distributed transactions by breaking them into local steps with "undo" (compensating) logic to ensure eventual consistency.
