# High Cohesion & Loose Coupling: Writing Better Code

## The Golden Rules of Software Design

**High Cohesion** = Each class does ONE thing well
**Loose Coupling** = Classes don't depend directly on each other

---

## The Problem: Tightly Coupled Code

### Bad Example - Violation

```java
class OrderServiceViolation {
    public void placeOrder(String orderId) {
        System.out.println("Placing order: " + orderId);
        
        // Directly doing email notification
        System.out.println("Sending email for order: " + orderId);
        
        // Directly doing logging
        System.out.println("Writing log to file for order: " + orderId);
    }
}
```

### Problems:

**Low Cohesion:**
- OrderService doing 3 jobs: placing orders, sending emails, writing logs
- Violates Single Responsibility Principle

**Tight Coupling:**
- To add SMS notification → Must modify OrderService class
- To change email format → Must modify OrderService class
- Can't test order placement without triggering email/logging

**Real Impact:**
```java
// Want to add SMS? Must change this class!
// Want different notification for VIP customers? Must change this class!
// Want to disable email? Must change this class!
```

---

## The Solution: High Cohesion + Loose Coupling

### Good Example

### 1. Interface (Abstraction)

```java
interface NotificationService {
    void notifyUser(String orderId);
}
```

### 2. Concrete Implementations (High Cohesion)

```java
class EmailService implements NotificationService {
    public void notifyUser(String orderId) {
        System.out.println("Sending email for order: " + orderId);
    }
}

class LoggingService implements NotificationService {
    public void notifyUser(String orderId) {
        System.out.println("Writing log to file for order: " + orderId);
    }
}

class SMSService implements NotificationService {
    public void notifyUser(String orderId) {
        System.out.println("Sending SMS for order: " + orderId);
    }
}
```

**High Cohesion:** Each class has ONE responsibility
- EmailService only sends emails
- LoggingService only writes logs
- SMSService only sends SMS

### 3. OrderService (Loose Coupling)

```java
class OrderServiceGood {
    private NotificationService notificationService;

    // Dependency Injection
    public OrderServiceGood(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void placeOrder(String orderId) {
        System.out.println("Placing order: " + orderId);
        notificationService.notifyUser(orderId);  // Delegates notification
    }
}
```

**Loose Coupling:**
- OrderService depends on interface, not concrete class
- Can inject ANY notification service
- Easy to test, extend, modify

### 4. Usage

```java
public class OrderServiceCorrect {
    public static void main(String[] args) {
        
        // Use email notification
        NotificationService email = new EmailService();
        OrderServiceGood orderService1 = new OrderServiceGood(email);
        orderService1.placeOrder("ORDER456");
        
        // Switch to logging (no code change in OrderService!)
        NotificationService log = new LoggingService();
        OrderServiceGood orderService2 = new OrderServiceGood(log);
        orderService2.placeOrder("ORDER789");
        
        // Add SMS notification (no code change in OrderService!)
        NotificationService sms = new SMSService();
        OrderServiceGood orderService3 = new OrderServiceGood(sms);
        orderService3.placeOrder("ORDER999");
    }
}
```

---

## Benefits

### High Cohesion Benefits:

**Easy to Understand**
```java
// Clear what each class does
EmailService → Sends emails (nothing else)
LoggingService → Writes logs (nothing else)
```

**Easy to Maintain**
```java
// Need to change email format? 
// Only modify EmailService, nothing else breaks
```

**Easy to Test**
```java
// Test EmailService independently
@Test
void testEmailService() {
    EmailService email = new EmailService();
    email.notifyUser("TEST123");
}
```

### Loose Coupling Benefits:

**Easy to Extend**
```java
// Add new notification type? Just create new class
class PushNotificationService implements NotificationService {
    public void notifyUser(String orderId) {
        System.out.println("Sending push notification: " + orderId);
    }
}
// No changes to OrderService!
```

**Easy to Replace**
```java
// Switch from email to SMS
NotificationService notification = new SMSService();
OrderServiceGood orderService = new OrderServiceGood(notification);
```

**Easy to Mock for Testing**
```java
// Test OrderService without actually sending emails
class MockNotificationService implements NotificationService {
    public void notifyUser(String orderId) {
        // Do nothing (for testing)
    }
}

@Test
void testOrderService() {
    NotificationService mock = new MockNotificationService();
    OrderServiceGood service = new OrderServiceGood(mock);
    service.placeOrder("TEST");  // No emails sent!
}
```

---

## Side-by-Side Comparison

| Aspect | Tightly Coupled (BAD) | Loosely Coupled (GOOD) |
|--------|----------------------|------------------------|
| **Cohesion** | Low (does many things) | High (does one thing) |
| **Coupling** | Tight (direct dependencies) | Loose (depends on interface) |
| **Adding SMS** | Modify OrderService | Create SMSService class |
| **Testing** | Hard (sends real emails) | Easy (inject mock) |
| **Changing email** | Modify OrderService | Modify EmailService only |
| **Code Changes** | Ripple across system | Isolated to one class |

---

## Real-World Example

### E-Commerce System

**Tightly Coupled (BAD):**
```java
class OrderProcessor {
    void processOrder(Order order) {
        // Save to database
        database.save(order);
        
        // Send email
        emailServer.send(order.customerEmail, "Order confirmed");
        
        // Update inventory
        inventory.reduce(order.items);
        
        // Send to shipping
        shippingService.ship(order);
        
        // Generate invoice
        invoiceGenerator.create(order);
    }
}
// Changing email breaks OrderProcessor!
// Testing requires real database, email server, etc!
```

**Loosely Coupled (GOOD):**
```java
class OrderProcessor {
    private DatabaseService db;
    private NotificationService notifier;
    private InventoryService inventory;
    private ShippingService shipping;
    private InvoiceService invoice;
    
    // All injected through constructor
    
    void processOrder(Order order) {
        db.save(order);
        notifier.notify(order);
        inventory.update(order);
        shipping.ship(order);
        invoice.generate(order);
    }
}
// Each service is independent
// Easy to test with mocks
// Easy to swap implementations
```

---

## Common Interview Questions

### Q1: What is the difference between High Cohesion and Loose Coupling?

**A:** "They're related but different:

**High Cohesion** = How focused a class is
- Each class should do ONE thing well
- Example: EmailService only sends emails

**Loose Coupling** = How independent classes are
- Classes shouldn't depend directly on each other
- Example: OrderService depends on NotificationService interface, not EmailService class

**Together:**
```java
// High Cohesion: EmailService has one job
class EmailService implements NotificationService {
    void notifyUser(String id) { /* only email logic */ }
}

// Loose Coupling: OrderService depends on interface
class OrderService {
    NotificationService notifier;  // Interface, not concrete class!
}
```"

---

### Q2: How does Dependency Injection help with loose coupling?

**A:** "Dependency Injection means passing dependencies from outside instead of creating them inside.

**Without DI (Tight Coupling):**
```java
class OrderService {
    private EmailService email = new EmailService();  // Hardcoded!
    // Can ONLY use EmailService, can't switch to SMS
}
```

**With DI (Loose Coupling):**
```java
class OrderService {
    private NotificationService notifier;
    
    public OrderService(NotificationService notifier) {
        this.notifier = notifier;  // Injected from outside
    }
    // Can use ANY notification service!
}
```

Benefits:
- Can inject EmailService, SMSService, or MockService
- Easy to test (inject mock)
- No code change needed to switch implementations"

---

### Q3: Give a real example of tight coupling causing problems.

**A:** "In a payment system I worked on:

**Tight Coupling Problem:**
```java
class PaymentProcessor {
    void processPayment(Payment payment) {
        // Directly using PayPal API
        PayPalAPI.charge(payment.amount, payment.card);
    }
}
```

**What went wrong:**
- Company wanted to add Stripe payment
- Had to modify PaymentProcessor class (risky!)
- Broke existing PayPal payments during deployment
- Couldn't test without hitting real PayPal API

**Loose Coupling Solution:**
```java
interface PaymentGateway {
    void charge(double amount, String card);
}

class PayPalGateway implements PaymentGateway { }
class StripeGateway implements PaymentGateway { }

class PaymentProcessor {
    PaymentGateway gateway;  // Injected
    
    void processPayment(Payment payment) {
        gateway.charge(payment.amount, payment.card);
    }
}
```

**Results:**
- Added Stripe without touching PaymentProcessor
- Easy to test with MockGateway
- Zero downtime deployment"

---

### Q4: How do you identify tight coupling in code?

**A:** "Look for these red flags:

**1. Using 'new' keyword everywhere:**
```java
class Service {
    void doWork() {
        Database db = new Database();  // Tight coupling!
        EmailService email = new EmailService();  // Tight coupling!
    }
}
```

**2. Long methods doing multiple things:**
```java
void processOrder() {
    // 50 lines of different responsibilities
    // Database, email, inventory, shipping...
}
```

**3. Hard to test:**
```java
// Can't test without real database
class UserService {
    Database db = new Database();  // Always hits real DB!
}
```

**4. Changes ripple across classes:**
- Change email format → must modify 5 classes
- Add new payment method → must modify 10 classes

**Fix:** Use interfaces, dependency injection, and single responsibility."

---

## Quick Rules

**Achieve High Cohesion:**
- One class = One responsibility
- If class name has "and" (EmailAndLoggingService), split it
- Small, focused classes

**Achieve Loose Coupling:**
- Depend on interfaces, not concrete classes
- Use dependency injection
- Program to an interface

**Remember:**
```
High Cohesion = Do ONE thing
Loose Coupling = Depend on ABSTRACTIONS
```

---

## Key Takeaways

1. **High Cohesion** makes code focused and maintainable
2. **Loose Coupling** makes code flexible and testable
3. **Use interfaces** to depend on abstractions
4. **Use dependency injection** to inject dependencies
5. **Each class should have ONE reason to change**

**Golden Rule:** "If changing one thing breaks many things, you have tight coupling!"
