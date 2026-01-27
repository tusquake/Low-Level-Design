# State Design Pattern: Order Management System

## What is State Pattern?

State pattern allows an object to change its behavior when its internal state changes. The object appears to change its class.

**Real-World:** Order status (Pending → Confirmed → Shipped → Delivered) where each state has different allowed actions.

---

## Problem

**Without State Pattern:**
```java
class Order {
    String status;
    
    void ship() {
        if (status.equals("PENDING")) {
            System.out.println("Cannot ship pending order");
        } else if (status.equals("CONFIRMED")) {
            status = "SHIPPED";
        } else if (status.equals("SHIPPED")) {
            System.out.println("Already shipped");
        }
        // Complex if-else for every method
    }
}
```

**With State Pattern:**
```java
order.ship();  // Behavior changes based on current state
```

---

## Implementation

### 1. State Interface

```java
public interface OrderState {
    void confirm(Order order);
    void ship(Order order);
    void deliver(Order order);
    void cancel(Order order);
    String getStatus();
}
```

### 2. Concrete States

```java
// Pending State
public class PendingState implements OrderState {
    
    @Override
    public void confirm(Order order) {
        System.out.println("Order confirmed! Processing payment...");
        order.setState(new ConfirmedState());
    }
    
    @Override
    public void ship(Order order) {
        System.out.println("Cannot ship. Order is still pending.");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("Cannot deliver. Order is still pending.");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order cancelled.");
        order.setState(new CancelledState());
    }
    
    @Override
    public String getStatus() {
        return "PENDING";
    }
}

// Confirmed State
public class ConfirmedState implements OrderState {
    
    @Override
    public void confirm(Order order) {
        System.out.println("Order is already confirmed.");
    }
    
    @Override
    public void ship(Order order) {
        System.out.println("Order shipped! Tracking number: TRK123456");
        order.setState(new ShippedState());
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("Cannot deliver. Order not yet shipped.");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order cancelled. Refund initiated.");
        order.setState(new CancelledState());
    }
    
    @Override
    public String getStatus() {
        return "CONFIRMED";
    }
}

// Shipped State
public class ShippedState implements OrderState {
    
    @Override
    public void confirm(Order order) {
        System.out.println("Order is already confirmed and shipped.");
    }
    
    @Override
    public void ship(Order order) {
        System.out.println("Order is already shipped.");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("Order delivered! Thank you for shopping.");
        order.setState(new DeliveredState());
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Cannot cancel. Order is already shipped.");
    }
    
    @Override
    public String getStatus() {
        return "SHIPPED";
    }
}

// Delivered State
public class DeliveredState implements OrderState {
    
    @Override
    public void confirm(Order order) {
        System.out.println("Order is already delivered.");
    }
    
    @Override
    public void ship(Order order) {
        System.out.println("Order is already delivered.");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("Order is already delivered.");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Cannot cancel. Order is already delivered.");
    }
    
    @Override
    public String getStatus() {
        return "DELIVERED";
    }
}

// Cancelled State
public class CancelledState implements OrderState {
    
    @Override
    public void confirm(Order order) {
        System.out.println("Cannot confirm. Order is cancelled.");
    }
    
    @Override
    public void ship(Order order) {
        System.out.println("Cannot ship. Order is cancelled.");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("Cannot deliver. Order is cancelled.");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("Order is already cancelled.");
    }
    
    @Override
    public String getStatus() {
        return "CANCELLED";
    }
}
```

### 3. Context (Order)

```java
public class Order {
    private OrderState state;
    private String orderId;
    
    public Order(String orderId) {
        this.orderId = orderId;
        this.state = new PendingState(); // Initial state
    }
    
    public void setState(OrderState state) {
        this.state = state;
        System.out.println("  → State changed to: " + state.getStatus());
    }
    
    public void confirm() {
        state.confirm(this);
    }
    
    public void ship() {
        state.ship(this);
    }
    
    public void deliver() {
        state.deliver(this);
    }
    
    public void cancel() {
        state.cancel(this);
    }
    
    public String getStatus() {
        return state.getStatus();
    }
    
    public String getOrderId() {
        return orderId;
    }
}
```

### 4. Demo

```java
public class OrderDemo {
    public static void main(String[] args) {
        
        Order order = new Order("ORD-12345");
        
        System.out.println("=== Order Lifecycle ===\n");
        System.out.println("Current Status: " + order.getStatus());
        
        System.out.println("\n--- Action 1: Confirm Order ---");
        order.confirm();
        
        System.out.println("\n--- Action 2: Ship Order ---");
        order.ship();
        
        System.out.println("\n--- Action 3: Deliver Order ---");
        order.deliver();
        
        System.out.println("\n--- Action 4: Try to Cancel (Invalid) ---");
        order.cancel();
        
        System.out.println("\n=== New Order - Cancelled ===\n");
        Order order2 = new Order("ORD-67890");
        System.out.println("Current Status: " + order2.getStatus());
        
        System.out.println("\n--- Cancel before confirmation ---");
        order2.cancel();
        
        System.out.println("\n--- Try to ship cancelled order ---");
        order2.ship();
    }
}
```

---

## Output

```
=== Order Lifecycle ===

Current Status: PENDING

--- Action 1: Confirm Order ---
Order confirmed! Processing payment...
  → State changed to: CONFIRMED

--- Action 2: Ship Order ---
Order shipped! Tracking number: TRK123456
  → State changed to: SHIPPED

--- Action 3: Deliver Order ---
Order delivered! Thank you for shopping.
  → State changed to: DELIVERED

--- Action 4: Try to Cancel (Invalid) ---
Cannot cancel. Order is already delivered.

=== New Order - Cancelled ===

Current Status: PENDING

--- Cancel before confirmation ---
Order cancelled.
  → State changed to: CANCELLED

--- Try to ship cancelled order ---
Cannot ship. Order is cancelled.
```

---

## State Transitions

```
┌─────────┐
│ PENDING │
└────┬────┘
     │ confirm()
     ↓
┌───────────┐     cancel()     ┌───────────┐
│ CONFIRMED │ ─────────────────▶│ CANCELLED │
└─────┬─────┘                   └───────────┘
      │ ship()
      ↓
┌─────────┐
│ SHIPPED │
└────┬────┘
     │ deliver()
     ↓
┌───────────┐
│ DELIVERED │
└───────────┘
```

---

## Key Benefits

**Eliminates Conditionals:** No if-else chains for state-dependent behavior

**Single Responsibility:** Each state has its own class

**Open/Closed:** Add new states without modifying existing code

**Clear Transitions:** State changes are explicit and traceable

---

## Real-World Examples

**ATM Machine:**
- States: NoCard, HasCard, HasPin, NoCash
- Actions: insertCard, enterPin, withdrawCash

**Vending Machine:**
- States: NoCoin, HasCoin, Sold, SoldOut
- Actions: insertCoin, pressButton, dispense

**Document Workflow:**
- States: Draft, UnderReview, Approved, Published
- Actions: submit, review, approve, publish

**Traffic Light:**
- States: Red, Yellow, Green
- Actions: change (automatic transitions)

**Media Player:**
- States: Playing, Paused, Stopped
- Actions: play, pause, stop

---

## Class Diagram

```
┌─────────────┐
│   Order     │ (Context)
├─────────────┤
│ - state     │───────┐
├─────────────┤       │
│ + confirm() │       │
│ + ship()    │       │
│ + deliver() │       │
└─────────────┘       │
                      │
                      ↓
            ┌──────────────────┐
            │   OrderState     │ (Interface)
            ├──────────────────┤
            │ + confirm()      │
            │ + ship()         │
            │ + deliver()      │
            │ + cancel()       │
            └────────┬─────────┘
                     △
                     │
        ┌────────────┼────────────┐
        │            │            │
┌───────┴────┐ ┌────┴─────┐ ┌───┴────────┐
│  Pending   │ │Confirmed │ │  Shipped   │
└────────────┘ └──────────┘ └────────────┘
```

---

## State vs Strategy

| State Pattern | Strategy Pattern |
|--------------|------------------|
| Behavior changes based on internal state | Algorithm selected by client |
| State changes during lifecycle | Strategy rarely changes |
| States know about each other | Strategies independent |
| Example: Order status | Example: Payment methods |

---

## When to Use

- Object behavior depends on its state
- Operations have large conditional statements
- State transitions are well-defined
- State-specific behavior needs encapsulation

---

## Quick Summary

| Aspect | Description |
|--------|-------------|
| **Problem** | Complex if-else based on state |
| **Solution** | Encapsulate state-specific behavior in separate classes |
| **Benefit** | Clean code, easy to extend, clear transitions |
| **Use Case** | Order processing, workflow systems, game characters |