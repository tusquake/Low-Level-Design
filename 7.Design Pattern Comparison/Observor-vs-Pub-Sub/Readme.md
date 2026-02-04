# Observer + Pub-Sub Pattern: Event-Driven Systems

## Real-World Scenario: Order Tracking System (Amazon/Flipkart)

**Problem:**
You're building an e-commerce order tracking system that needs to:
1. **Notify multiple services when order status changes** (Observer Pattern)
    - Email service sends confirmation
    - SMS service sends delivery updates
    - Analytics tracks order flow
2. **Decouple services using events** (Pub-Sub Pattern)
    - Publishers don't know subscribers
    - Add/remove services without changing code
    - Centralized event broker

---

## Architecture Overview

```
Order Status Change
    ↓
Observer Pattern (Direct notification)
    OR
    ↓
Pub-Sub Pattern (Through event broker)
    ↓
All Subscribers Notified
```

---

## Pattern Comparison

| Aspect | Observer | Pub-Sub |
|--------|----------|---------|
| **Coupling** | Subject knows observers | Publisher doesn't know subscribers |
| **Communication** | Direct | Through broker/event bus |
| **Mediator** | No mediator | Event broker acts as mediator |
| **Flexibility** | Moderate | High |
| **Use Case** | Tight-knit components | Distributed systems |
| **Example** | GUI events | Microservices communication |

---

## PART 1: OBSERVER PATTERN

### Problem
When an order status changes, multiple services (Email, SMS, Analytics) need to be notified without tight coupling.

### Solution
Define a one-to-many dependency so when one object changes state, all dependents are notified automatically.

---

## Implementation - Observer Pattern

### 1. Observer Interface

```java
interface IObserver {
    void update(String status);
}
```

### 2. Concrete Observers

```java
class SMSService implements IObserver {

    @Override
    public void update(String status) {
        System.out.println("SMS sent: Status is: " + status);
    }
}

class EmailService implements IObserver {

    @Override
    public void update(String status) {
        System.out.println("Email sent: Status is: " + status);
    }
}
```

### 3. Subject Interface

```java
interface Subject {
    void attach(IObserver observer);
    void detach(IObserver observer);
    void notifyObservers();
}
```

### 4. Concrete Subject

```java
class Order implements Subject {

    private List<IObserver> observers = new ArrayList<>();
    private String status;

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();  // Automatically notify when state changes
    }

    public String getStatus() {
        return status;
    }

    public void attach(IObserver observer) {
        observers.add(observer);
    }

    public void detach(IObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update(status);
        }
    }
}
```

### 5. Usage

```java
class ObserverDemo {
    public static void main(String[] args) {
        Order order = new Order();

        order.attach(new EmailService());
        order.attach(new SMSService());

        order.setStatus("SHIPPED");
        order.setStatus("DELIVERED");
    }
}
```

### Output
```
Email sent: Status is: SHIPPED
SMS sent: Status is: SHIPPED
Email sent: Status is: DELIVERED
SMS sent: Status is: DELIVERED
```

---

## Benefits - Observer Pattern

**Loose Coupling**
- Subject only knows observers implement the interface
- Observers don't know about each other

**Dynamic Relationships**
- Add/remove observers at runtime
- No compile-time dependencies

**Broadcast Communication**
- One change notifies multiple observers
- Automatic propagation

**Open/Closed Principle**
- Add new observers without modifying subject
- Extend functionality easily

---

## How Observer Pattern Works

### Execution Flow:

```
1. Create Order (Subject)
   ↓
2. Attach EmailService (Observer)
   ↓
3. Attach SMSService (Observer)
   ↓
4. order.setStatus("SHIPPED")
   ↓
5. Order.notifyObservers() called
   ↓
6. For each observer in list:
   - EmailService.update("SHIPPED")
   - SMSService.update("SHIPPED")
```

### Visual Representation:

```
        Order (Subject)
        [status = "SHIPPED"]
              |
    +---------+---------+
    |                   |
EmailService      SMSService
(Observer)        (Observer)
    |                   |
update("SHIPPED")  update("SHIPPED")
```

---

## PART 2: PUB-SUB PATTERN

### Problem
In distributed systems, services shouldn't directly depend on each other. Need a centralized event broker.

### Solution
Use an Event Broker (message queue) to decouple publishers from subscribers completely.

---

## Implementation - Pub-Sub Pattern

### 1. Event Class

```java
class OrderEvent {
    public final String status;

    public OrderEvent(String status) {
        this.status = status;
    }
}
```

### 2. Subscriber Interface

```java
interface Subscriber {
    void onEvent(OrderEvent event);
}
```

### 3. Concrete Subscribers

```java
class AnalyticsService implements Subscriber {
    public void onEvent(OrderEvent event) {
        System.out.println("Analytics processed: " + event.status);
    }
}

class NotificationService implements Subscriber {
    public void onEvent(OrderEvent event) {
        System.out.println("Notification sent: " + event.status);
    }
}
```

### 4. Event Broker (Central Hub)

```java
class EventBroker {

    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    public void subscribe(String topic, Subscriber sub) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(sub);
    }

    public void publish(String topic, OrderEvent event) {
        if (subscribers.containsKey(topic)) {
            for (Subscriber sub : subscribers.get(topic)) {
                sub.onEvent(event);
            }
        }
    }
}
```

### 5. Usage

```java
public class PubSubDemo {
    public static void main(String[] args) {
        EventBroker broker = new EventBroker();

        broker.subscribe("ORDER_STATUS", new AnalyticsService());
        broker.subscribe("ORDER_STATUS", new NotificationService());

        broker.publish("ORDER_STATUS", new OrderEvent("DELIVERED"));
    }
}
```

### Output
```
Analytics processed: DELIVERED
Notification sent: DELIVERED
```

---

## Benefits - Pub-Sub Pattern

**Complete Decoupling**
- Publishers don't know subscribers exist
- Subscribers don't know publishers exist
- Both only know the event broker

**Topic-Based Routing**
- Subscribe to specific topics/events
- Receive only relevant notifications

**Scalability**
- Add unlimited publishers/subscribers
- Broker handles distribution

**Asynchronous Communication**
- Can implement async event processing
- Non-blocking operations

---

## How Pub-Sub Pattern Works

### Execution Flow:

```
1. Create EventBroker
   ↓
2. AnalyticsService subscribes to "ORDER_STATUS"
   ↓
3. NotificationService subscribes to "ORDER_STATUS"
   ↓
4. Publisher publishes OrderEvent to "ORDER_STATUS"
   ↓
5. Broker finds all subscribers for "ORDER_STATUS"
   ↓
6. Broker calls onEvent() on each subscriber
```

### Visual Representation:

```
    Publisher                Event Broker               Subscribers
        |                         |                          |
        |--publish("ORDER")------>|                          |
        |                         |                          |
        |                    [topic: ORDER]                  |
        |                    - AnalyticsService              |
        |                    - NotificationService           |
        |                         |                          |
        |                         |---onEvent()------------->| Analytics
        |                         |---onEvent()------------->| Notification
```

---

## Observer vs Pub-Sub: Deep Comparison

### Observer Pattern

**Structure:**
```java
// Subject knows its observers
class Order {
    List<IObserver> observers;  // Direct reference
    
    void setStatus(String status) {
        notifyObservers();  // Calls observers directly
    }
}
```

**Characteristics:**
- Subject maintains list of observers
- Direct method calls
- Synchronous by default
- Tighter coupling

### Pub-Sub Pattern

**Structure:**
```java
// Publisher doesn't know subscribers
class Publisher {
    void updateOrder(String status) {
        broker.publish("ORDER", new Event(status));  // No knowledge of subscribers
    }
}

// Subscriber doesn't know publishers
class Subscriber {
    void init() {
        broker.subscribe("ORDER", this);  // No knowledge of publishers
    }
}
```

**Characteristics:**
- Broker maintains subscribers
- Indirect through broker
- Can be asynchronous
- Loose coupling

---

## When to Use Which Pattern?

### Use Observer When:
- **Components are in the same application**
- **Synchronous updates** needed
- **Direct relationship** is acceptable
- **Simple notification** mechanism
- Example: GUI event handling, form validation

### Use Pub-Sub When:
- **Distributed systems** (microservices)
- **Complete decoupling** required
- **Multiple event types** (topics)
- **Asynchronous processing** needed
- Example: Event-driven architecture, messaging systems

---

## Real-World Examples

### Observer Pattern

**Stock Market App:**
```java
interface StockObserver {
    void update(String stock, double price);
}

class StockTicker implements Subject {
    void setPrice(String stock, double price) {
        notifyObservers(stock, price);
    }
}

// Observers: PriceDisplay, AlertService, ChartUpdater
```

**YouTube Subscriptions:**
```java
class YouTubeChannel implements Subject {
    void uploadVideo(Video video) {
        notifySubscribers(video);  // All subscribers notified
    }
}

// Observers: Subscriber1, Subscriber2, Subscriber3
```

**Weather Station:**
```java
class WeatherStation implements Subject {
    void setTemperature(float temp) {
        notifyObservers(temp);
    }
}

// Observers: PhoneDisplay, WebDisplay, StatisticsDisplay
```

### Pub-Sub Pattern

**E-Commerce Platform:**
```java
// Order Service publishes "ORDER_PLACED"
// Subscribers: Inventory, Payment, Shipping, Email, Analytics
broker.publish("ORDER_PLACED", orderEvent);
```

**Social Media:**
```java
// User posts a photo
broker.publish("POST_CREATED", postEvent);

// Subscribers: Timeline, Notifications, Search, Analytics
```

**Logging System:**
```java
// Different services publish logs
broker.publish("ERROR_LOG", errorEvent);
broker.publish("INFO_LOG", infoEvent);

// Subscribers: FileLogger, DatabaseLogger, AlertService
```

---

## Complete Example: E-Commerce Order System

### Enhanced Observer Example

```java
interface OrderObserver {
    void update(Order order);
}

class EmailNotifier implements OrderObserver {
    public void update(Order order) {
        System.out.println("Email: Order " + order.getId() + " is " + order.getStatus());
    }
}

class SMSNotifier implements OrderObserver {
    public void update(Order order) {
        System.out.println("SMS: Order " + order.getId() + " is " + order.getStatus());
    }
}

class InventoryUpdater implements OrderObserver {
    public void update(Order order) {
        System.out.println("Inventory: Updating stock for order " + order.getId());
    }
}

class AnalyticsTracker implements OrderObserver {
    public void update(Order order) {
        System.out.println("Analytics: Tracking " + order.getStatus() + " event");
    }
}

class Order implements Subject {
    private String id;
    private String status;
    private List<OrderObserver> observers = new ArrayList<>();
    
    public Order(String id) {
        this.id = id;
    }
    
    public void setStatus(String status) {
        this.status = status;
        System.out.println("\n--- Order " + id + " status changed to: " + status + " ---");
        notifyObservers();
    }
    
    public String getId() { return id; }
    public String getStatus() { return status; }
    
    public void attach(OrderObserver observer) {
        observers.add(observer);
    }
    
    public void detach(OrderObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.update(this);
        }
    }
}

// Usage
public class EnhancedObserverDemo {
    public static void main(String[] args) {
        Order order = new Order("ORD-123");
        
        order.attach(new EmailNotifier());
        order.attach(new SMSNotifier());
        order.attach(new InventoryUpdater());
        order.attach(new AnalyticsTracker());
        
        order.setStatus("CONFIRMED");
        order.setStatus("SHIPPED");
        order.setStatus("DELIVERED");
    }
}
```

### Output
```
--- Order ORD-123 status changed to: CONFIRMED ---
Email: Order ORD-123 is CONFIRMED
SMS: Order ORD-123 is CONFIRMED
Inventory: Updating stock for order ORD-123
Analytics: Tracking CONFIRMED event

--- Order ORD-123 status changed to: SHIPPED ---
Email: Order ORD-123 is SHIPPED
SMS: Order ORD-123 is SHIPPED
Inventory: Updating stock for order ORD-123
Analytics: Tracking SHIPPED event

--- Order ORD-123 status changed to: DELIVERED ---
Email: Order ORD-123 is DELIVERED
SMS: Order ORD-123 is DELIVERED
Inventory: Updating stock for order ORD-123
Analytics: Tracking DELIVERED event
```

### Enhanced Pub-Sub Example

```java
class OrderEvent {
    private String orderId;
    private String status;
    private String customerId;
    
    public OrderEvent(String orderId, String status, String customerId) {
        this.orderId = orderId;
        this.status = status;
        this.customerId = customerId;
    }
    
    public String getOrderId() { return orderId; }
    public String getStatus() { return status; }
    public String getCustomerId() { return customerId; }
}

interface EventSubscriber {
    void onEvent(OrderEvent event);
}

class EmailService implements EventSubscriber {
    public void onEvent(OrderEvent event) {
        System.out.println("[Email] Sending to customer " + event.getCustomerId() + 
                          ": Order " + event.getOrderId() + " is " + event.getStatus());
    }
}

class SMSService implements EventSubscriber {
    public void onEvent(OrderEvent event) {
        System.out.println("[SMS] Sending to customer " + event.getCustomerId() + 
                          ": Order " + event.getOrderId() + " is " + event.getStatus());
    }
}

class InventoryService implements EventSubscriber {
    public void onEvent(OrderEvent event) {
        if (event.getStatus().equals("CONFIRMED")) {
            System.out.println("[Inventory] Reserving items for order " + event.getOrderId());
        }
    }
}

class ShippingService implements EventSubscriber {
    public void onEvent(OrderEvent event) {
        if (event.getStatus().equals("CONFIRMED")) {
            System.out.println("[Shipping] Creating shipment for order " + event.getOrderId());
        }
    }
}

class AnalyticsService implements EventSubscriber {
    public void onEvent(OrderEvent event) {
        System.out.println("[Analytics] Recording event: " + event.getStatus() + 
                          " for order " + event.getOrderId());
    }
}

class EventBroker {
    private Map<String, List<EventSubscriber>> subscribers = new HashMap<>();
    
    public void subscribe(String topic, EventSubscriber subscriber) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(subscriber);
        System.out.println("[Broker] " + subscriber.getClass().getSimpleName() + 
                          " subscribed to " + topic);
    }
    
    public void unsubscribe(String topic, EventSubscriber subscriber) {
        if (subscribers.containsKey(topic)) {
            subscribers.get(topic).remove(subscriber);
        }
    }
    
    public void publish(String topic, OrderEvent event) {
        System.out.println("\n[Broker] Publishing to topic: " + topic);
        if (subscribers.containsKey(topic)) {
            for (EventSubscriber subscriber : subscribers.get(topic)) {
                subscriber.onEvent(event);
            }
        }
    }
}

// Usage
public class EnhancedPubSubDemo {
    public static void main(String[] args) {
        EventBroker broker = new EventBroker();
        
        // Services subscribe to topics
        broker.subscribe("ORDER_EVENTS", new EmailService());
        broker.subscribe("ORDER_EVENTS", new SMSService());
        broker.subscribe("ORDER_EVENTS", new InventoryService());
        broker.subscribe("ORDER_EVENTS", new ShippingService());
        broker.subscribe("ORDER_EVENTS", new AnalyticsService());
        
        // Publish events
        broker.publish("ORDER_EVENTS", 
                      new OrderEvent("ORD-123", "CONFIRMED", "CUST-456"));
        
        broker.publish("ORDER_EVENTS", 
                      new OrderEvent("ORD-123", "SHIPPED", "CUST-456"));
        
        broker.publish("ORDER_EVENTS", 
                      new OrderEvent("ORD-123", "DELIVERED", "CUST-456"));
    }
}
```

---

## Common Mistakes

### Observer Pattern Mistakes

**Mistake 1: Memory Leaks (Not Detaching)**

```java
// BAD: Observer never removed
class UI {
    void init() {
        order.attach(this);
        // UI closed but still attached - memory leak!
    }
}

// GOOD: Properly detach
class UI {
    void init() {
        order.attach(this);
    }
    
    void cleanup() {
        order.detach(this);  // Important!
    }
}
```

**Mistake 2: Observers Modifying Subject During Notification**

```java
// BAD: Can cause ConcurrentModificationException
class Observer implements IObserver {
    void update(String status) {
        subject.attach(new AnotherObserver());  // Modifying during iteration!
    }
}

// GOOD: Modify after notification
class Observer implements IObserver {
    void update(String status) {
        // Schedule modification for later
        scheduleLater(() -> subject.attach(new AnotherObserver()));
    }
}
```

**Mistake 3: Tight Coupling Through Update Method**

```java
// BAD: Observer depends on specific subject type
interface Observer {
    void update(Order order);  // Tight coupling to Order!
}

// GOOD: Generic update method
interface Observer {
    void update(Object data);  // Or use generics
}
```

### Pub-Sub Pattern Mistakes

**Mistake 1: Not Unsubscribing**

```java
// BAD: Service subscribes but never unsubscribes
class Service {
    void init() {
        broker.subscribe("EVENTS", this);
        // Service destroyed but still subscribed
    }
}

// GOOD: Unsubscribe when done
class Service {
    void init() {
        broker.subscribe("EVENTS", this);
    }
    
    void shutdown() {
        broker.unsubscribe("EVENTS", this);
    }
}
```

**Mistake 2: No Error Handling**

```java
// BAD: One subscriber's exception breaks all
void publish(String topic, Event event) {
    for (Subscriber sub : subscribers.get(topic)) {
        sub.onEvent(event);  // Exception breaks loop!
    }
}

// GOOD: Isolate subscriber errors
void publish(String topic, Event event) {
    for (Subscriber sub : subscribers.get(topic)) {
        try {
            sub.onEvent(event);
        } catch (Exception e) {
            log.error("Subscriber error", e);
        }
    }
}
```

---

## Design Principles Applied

### Observer Pattern

**Dependency Inversion Principle**
- Subject depends on Observer interface, not concrete observers
- Observers depend on Subject interface

**Open/Closed Principle**
- Open for extension: Add new observers
- Closed for modification: Subject code unchanged

**Single Responsibility**
- Subject manages state
- Observers react to changes
- Each has one reason to change

### Pub-Sub Pattern

**Loose Coupling**
- Publishers and subscribers completely decoupled
- Only know about the broker and event format

**Separation of Concerns**
- Broker handles message routing
- Publishers handle business logic
- Subscribers handle reactions

---

## Interview Questions & Answers

### Q1: What's the difference between Observer and Pub-Sub patterns?

**A:** "The main differences are:

**Observer Pattern:**
- Subject directly knows its observers
- Observers register with the subject
- Communication is synchronous and direct
- Used within single application

**Pub-Sub Pattern:**
- Publisher doesn't know subscribers
- Central broker mediates all communication
- Can be asynchronous
- Used in distributed systems

**Example:**
```java
// Observer: Direct relationship
order.attach(emailService);  // Order knows emailService

// Pub-Sub: Through broker
broker.subscribe('ORDER', emailService);  // Order doesn't know emailService
broker.publish('ORDER', event);
```

**Use Observer** for tightly-knit components in the same app (like GUI events).
**Use Pub-Sub** for loosely-coupled distributed services (like microservices)."

---

### Q2: How does the Observer pattern prevent memory leaks?

**A:** "Memory leaks happen when observers aren't properly removed:

**The Problem:**
```java
class Observer implements IObserver {
    void init() {
        subject.attach(this);  // Adds reference
    }
    // If observer is destroyed but not detached,
    // subject still holds reference - memory leak!
}
```

**The Solution:**
1. **Always detach when done:**
```java
void cleanup() {
    subject.detach(this);
}
```

2. **Use weak references (advanced):**
```java
class Subject {
    List<WeakReference<IObserver>> observers;
    // Garbage collector can remove observers automatically
}
```

3. **Proper lifecycle management:**
```java
class Observer {
    void onDestroy() {
        subject.detach(this);  // Always cleanup
    }
}
```

The key is: **If you attach, you must detach**."

---

### Q3: Can you give a real-world scenario where you'd use Observer pattern?

**A:** "Yes, in a stock trading application:

**Scenario:** Multiple displays need to update when stock prices change.

**Without Observer (BAD):**
```java
class StockTicker {
    void updatePrice(String stock, double price) {
        // Tight coupling to all displays!
        priceDisplay.update(stock, price);
        chartDisplay.update(stock, price);
        alertDisplay.update(stock, price);
        // Adding new display requires changing this code!
    }
}
```

**With Observer (GOOD):**
```java
class StockTicker implements Subject {
    void updatePrice(String stock, double price) {
        this.price = price;
        notifyObservers();  // All observers auto-updated
    }
}

// Observers
class PriceDisplay implements Observer { }
class ChartDisplay implements Observer { }
class AlertDisplay implements Observer { }

// Usage
stockTicker.attach(new PriceDisplay());
stockTicker.attach(new ChartDisplay());
stockTicker.attach(new AlertDisplay());
// Add new display without changing StockTicker!
```

**Benefits:**
- Add/remove displays without modifying StockTicker
- Each display handles its own update logic
- Displays are decoupled from each other"

---

### Q4: When would you use Pub-Sub over Observer?

**A:** "Use Pub-Sub when you need:

**1. Complete Decoupling:**
```java
// Observer: Services know about Order
order.attach(inventoryService);
order.attach(shippingService);

// Pub-Sub: Services don't know about Order
broker.subscribe('ORDER', inventoryService);
broker.subscribe('ORDER', shippingService);
// Order service can be deployed separately!
```

**2. Multiple Event Types (Topics):**
```java
broker.subscribe('ORDER_CREATED', inventoryService);
broker.subscribe('ORDER_SHIPPED', shippingService);
broker.subscribe('ORDER_DELIVERED', emailService);
// Different services listen to different events
```

**3. Distributed Systems:**
```java
// Services can be on different servers
broker.publish('ORDER', event);  // Goes through message queue
// RabbitMQ, Kafka, Redis Pub/Sub
```

**4. Asynchronous Processing:**
```java
// Pub-Sub can process events asynchronously
broker.publish('ORDER', event);  // Returns immediately
// Subscribers process in background
```

**Real Example:** In a microservices e-commerce platform, when an order is placed:
- Order Service publishes 'ORDER_CREATED' event
- Inventory Service subscribes to reserve items
- Payment Service subscribes to process payment
- Shipping Service subscribes to create shipment
- Email Service subscribes to send confirmation

Each service is independent, can be deployed separately, and doesn't know about the others."

---

## Pattern Variations

### Push vs Pull Observer

**Push Model (Used in Examples):**
```java
class Subject {
    void notifyObservers() {
        for (Observer obs : observers) {
            obs.update(this.status);  // Pushes data
        }
    }
}
```

**Pull Model:**
```java
interface Observer {
    void update(Subject subject);  // Receives subject reference
}

class ConcreteObserver implements Observer {
    void update(Subject subject) {
        String status = subject.getStatus();  // Pulls data
    }
}
```

### Event-Specific Observers

```java
interface OrderObserver {
    void onOrderCreated(Order order);
    void onOrderShipped(Order order);
    void onOrderDelivered(Order order);
}

class EmailService implements OrderObserver {
    void onOrderCreated(Order order) { /* send confirmation */ }
    void onOrderShipped(Order order) { /* send tracking */ }
    void onOrderDelivered(Order order) { /* send thank you */ }
}
```

---

## Advanced Topics

### Thread Safety

```java
class ThreadSafeSubject {
    private List<Observer> observers = 
        Collections.synchronizedList(new ArrayList<>());
    
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    public void notifyObservers() {
        // Create copy to avoid ConcurrentModificationException
        List<Observer> copy = new ArrayList<>(observers);
        for (Observer obs : copy) {
            obs.update(this);
        }
    }
}
```

### Priority-Based Notification

```java
class PrioritySubject {
    private Map<Integer, List<Observer>> observersByPriority = new TreeMap<>();
    
    public void attach(Observer observer, int priority) {
        observersByPriority
            .computeIfAbsent(priority, k -> new ArrayList<>())
            .add(observer);
    }
    
    public void notifyObservers() {
        // Notifies in priority order (TreeMap is sorted)
        for (List<Observer> observers : observersByPriority.values()) {
            for (Observer obs : observers) {
                obs.update(this);
            }
        }
    }
}
```

---

## Summary Table

| Feature | Observer | Pub-Sub |
|---------|----------|---------|
| **Coupling** | Subject knows observers | Complete decoupling |
| **Communication** | Direct | Through broker |
| **Sync/Async** | Typically sync | Can be async |
| **Scope** | Single application | Distributed systems |
| **Complexity** | Low | Medium |
| **Scalability** | Moderate | High |
| **Topic Support** | No | Yes |
| **Best For** | GUI events, local state changes | Microservices, event-driven architecture |

---

## Quick Reference

### Observer Pattern Structure
```
Subject (interface)
    ├── attach(Observer)
    ├── detach(Observer)
    └── notifyObservers()

ConcreteSubject implements Subject
    └── List<Observer> observers

Observer (interface)
    └── update(data)

ConcreteObserver implements Observer
    └── update(data) { /* react */ }
```

### Pub-Sub Pattern Structure
```
EventBroker
    ├── subscribe(topic, Subscriber)
    ├── unsubscribe(topic, Subscriber)
    └── publish(topic, Event)

Publisher
    └── broker.publish(topic, event)

Subscriber
    └── onEvent(event)
```

### Implementation Checklist

**Observer:**
- [ ] Define Observer interface with update method
- [ ] Define Subject interface with attach/detach/notify
- [ ] Implement ConcreteSubject with observer list
- [ ] Trigger notifyObservers() on state change
- [ ] Implement ConcreteObservers
- [ ] Remember to detach to avoid memory leaks

**Pub-Sub:**
- [ ] Create Event class
- [ ] Define Subscriber interface with onEvent method
- [ ] Implement EventBroker with topic-based routing
- [ ] Implement concrete subscribers
- [ ] Subscribe to topics
- [ ] Publish events to topics

---

## Real-World Technologies Using These Patterns

### Observer Pattern:
- **Java Swing/JavaFX:** Event listeners
- **Android:** LiveData, Observable
- **JavaScript:** Event listeners, RxJS Observables
- **C#:** IObservable, events

### Pub-Sub Pattern:
- **RabbitMQ:** Message broker
- **Apache Kafka:** Event streaming
- **Redis Pub/Sub:** In-memory messaging
- **AWS SNS/SQS:** Cloud messaging
- **Google Pub/Sub:** Cloud messaging
- **MQTT:** IoT messaging protocol

---