# Design Patterns in Action: Ride-Sharing System (Uber/Ola)

## Real-World Scenario: Building Uber/Ola

This is a complete ride-sharing system that combines **6 design patterns** to solve real problems:

1. **Factory** - Create different vehicle types
2. **Strategy** - Swap pricing and payment methods
3. **Observer** - Notify users and drivers
4. **State** - Manage ride lifecycle
5. **Singleton** - Single driver matching service

---

## System Architecture

```
User books ride
    ↓
Factory creates vehicle (Bike/Sedan)
    ↓
Strategy calculates fare (Normal/Surge)
    ↓
Singleton matches driver
    ↓
Observer notifies User & Driver
    ↓
State manages ride flow (Requested → Ongoing → Completed)
    ↓
Strategy processes payment (UPI/Card)
```

---

## Complete Implementation

### 1. Factory Pattern - Vehicle Creation

**Problem:** Create different vehicle types based on user selection

```java
interface Vehicle {
    void getType();
}

class Bike implements Vehicle {
    public void getType() {
        System.out.println("Bike Booked!");
    }
}

class Sedan implements Vehicle {
    public void getType() {
        System.out.println("Sedan Booked");
    }
}

class VehicleFactory {
    public static Vehicle getVehicle(String type) {
        if(type.equalsIgnoreCase("BIKE")) return new Bike();
        if(type.equalsIgnoreCase("SEDAN")) return new Sedan();
        return null;
    }
}
```

**Why:** Centralized vehicle creation, easy to add new vehicle types

---

### 2. Strategy Pattern - Pricing & Payment

**Problem:** Different pricing during normal/surge hours, multiple payment methods

```java
// Pricing Strategy
interface PricingStrategy {
    double calculateFare(double distance);
}

class NormalPricing implements PricingStrategy {
    public double calculateFare(double distance) {
        return distance * 10;
    }
}

class SurgePricing implements PricingStrategy {
    public double calculateFare(double distance) {
        return distance * 20;  // 2x during surge
    }
}

// Payment Strategy
interface PaymentStrategy {
    void pay(double amount);
}

class UpiPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via UPI: " + amount);
    }
}

class CardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via Card: " + amount);
    }
}
```

**Why:** Switch pricing/payment methods at runtime without changing code

---

### 3. Observer Pattern - Notifications

**Problem:** Notify user and driver when ride status changes

```java
interface RideObserver {
    void update(String status);
}

class UserApp implements RideObserver {
    public void update(String status) {
        System.out.println("User notified: Ride " + status);
    }
}

class DriverApp implements RideObserver {
    public void update(String status) {
        System.out.println("Driver notified: Ride " + status);
    }
}
```

**Why:** Multiple services get notified automatically when ride status changes

---

### 4. State Pattern - Ride Lifecycle

**Problem:** Ride goes through different states with different behaviors

```java
interface RideState {
    void next(Ride ride);
}

class RequestedState implements RideState {
    public void next(Ride ride) {
        System.out.println("Ride accepted");
        ride.setState(new OngoingState());
    }
}

class OngoingState implements RideState {
    public void next(Ride ride) {
        System.out.println("Ride completed");
        ride.setState(new CompletedState());
    }
}

class CompletedState implements RideState {
    public void next(Ride ride) {
        System.out.println("Ride already completed");
    }
}
```

**Why:** Clean state transitions, each state knows what comes next

---

### 5. Singleton Pattern - Driver Matching Service

**Problem:** Only one driver matching service should exist system-wide

```java
class MatchingService {
    private static MatchingService instance = new MatchingService();

    private MatchingService() {}  // Private constructor

    public static MatchingService getInstance() {
        return instance;
    }

    public void findDriver() {
        System.out.println("Driver matched!");
    }
}
```

**Why:** Global access point, ensures single instance managing all driver matching

---

### 6. The Ride Class - Combining Everything

```java
class Ride {
    private PricingStrategy pricingStrategy;
    private PaymentStrategy paymentStrategy;
    private RideState state;
    private List<RideObserver> observers = new ArrayList<>();

    public Ride(PricingStrategy pricingStrategy, PaymentStrategy paymentStrategy) {
        this.pricingStrategy = pricingStrategy;
        this.paymentStrategy = paymentStrategy;
        this.state = new RequestedState();
    }

    public void addObserver(RideObserver obs) {
        observers.add(obs);
    }

    public void notifyAllObservers(String msg) {
        for(RideObserver o : observers) {
            o.update(msg);
        }
    }

    public void setState(RideState state) {
        this.state = state;
    }

    public void nextState() {
        state.next(this);
    }

    public void startRide(double distance) {
        MatchingService.getInstance().findDriver();  // Singleton
        notifyAllObservers("started");               // Observer
        
        double fare = pricingStrategy.calculateFare(distance);  // Strategy
        System.out.println("Fare: " + fare);
        
        paymentStrategy.pay(fare);  // Strategy
    }
}
```

---

### Usage Example

```java
public class Main {
    public static void main(String[] args) {
        
        // Factory: Select vehicle
        Vehicle v = VehicleFactory.getVehicle("SEDAN");
        v.getType();

        // Strategy: Set pricing and payment
        PricingStrategy pricing = new SurgePricing();
        PaymentStrategy payment = new UpiPayment();

        Ride ride = new Ride(pricing, payment);

        // Observer: Add notifications
        ride.addObserver(new UserApp());
        ride.addObserver(new DriverApp());

        // Start ride
        ride.startRide(10);

        // State: Progress through states
        ride.nextState();  // Requested → Ongoing
        ride.nextState();  // Ongoing → Completed
        ride.nextState();  // Already completed
    }
}
```

---

### Output

```
Sedan Booked
Driver matched!
User notified: Ride started
Driver notified: Ride started
Fare: 200.0
Paid via UPI: 200.0
Ride accepted
Ride completed
Ride already completed
```

---

## Pattern Summary

| Pattern | Purpose in System | Example |
|---------|-------------------|---------|
| **Factory** | Create vehicles | Bike vs Sedan |
| **Strategy** | Swap algorithms | Normal vs Surge pricing, UPI vs Card |
| **Observer** | Notify stakeholders | Alert User & Driver |
| **State** | Manage lifecycle | Requested → Ongoing → Completed |
| **Singleton** | Single instance | One driver matching service |

---

## Common Interview Questions

### Q1: Why use Strategy pattern for pricing instead of if-else?

**A:** "Strategy pattern is better because:

**Without Strategy (BAD):**
```java
double calculateFare(String type, double distance) {
    if (type.equals("normal")) return distance * 10;
    if (type.equals("surge")) return distance * 20;
    if (type.equals("pool")) return distance * 7;
    // Adding new pricing requires changing this method!
}
```

**With Strategy (GOOD):**
```java
PricingStrategy pricing = new SurgePricing();
double fare = pricing.calculateFare(distance);
// Add new pricing? Just create new strategy class!
```

Benefits:
- Open/Closed Principle: Add new pricing without modifying existing code
- Easy to test each pricing strategy separately
- Can switch pricing at runtime"

---

### Q2: Why use State pattern for ride lifecycle?

**A:** "State pattern prevents invalid transitions and makes each state's behavior clear.

**Without State (BAD):**
```java
class Ride {
    String status = "REQUESTED";
    
    void next() {
        if (status.equals("REQUESTED")) {
            status = "ONGOING";
        } else if (status.equals("ONGOING")) {
            status = "COMPLETED";
        } else if (status.equals("COMPLETED")) {
            // What happens here? Error? Nothing?
        }
        // Complex nested if-else grows with more states!
    }
}
```

**With State (GOOD):**
```java
class OngoingState implements RideState {
    public void next(Ride ride) {
        System.out.println("Ride completed");
        ride.setState(new CompletedState());
    }
}
// Each state knows its next state. Clean and clear!
```

Benefits:
- Each state is a separate class with its own behavior
- Easy to add new states
- No complex conditionals"

---

### Q3: When would you use Singleton in real applications?

**A:** "Use Singleton when you need exactly one instance system-wide.

**Good use cases:**
- Database connection pool
- Configuration manager
- Logger
- Cache manager
- Driver matching service (like in Uber)

**Example:**
```java
// BAD: Multiple instances
ConnectionPool pool1 = new ConnectionPool();
ConnectionPool pool2 = new ConnectionPool();
// Two pools = wasted resources!

// GOOD: Single instance
ConnectionPool pool = ConnectionPool.getInstance();
// Only one pool for entire application
```

**Warning:** Singleton can make testing harder and create hidden dependencies. Use sparingly!"

---

### Q4: How do these patterns work together in this system?

**A:** "Each pattern solves a specific problem:

1. **User opens app**
    - Factory creates vehicle based on selection

2. **User books ride**
    - Strategy calculates fare (surge or normal)
    - Singleton finds driver
    - Observer notifies user and driver

3. **Ride progresses**
    - State manages: Requested → Ongoing → Completed
    - Observer notifies at each state change

4. **Ride ends**
    - Strategy processes payment (UPI or card)
    - State ensures ride is completed

They're **loosely coupled** - can change pricing without affecting notifications, can add new vehicle types without changing states, etc."

---

### Q5: What's the difference between Factory and Strategy?

**A:** "Very different purposes:

**Factory:**
- **Creates objects**
- Used once at the beginning
- Example: Create Bike or Sedan

**Strategy:**
- **Swaps behavior**
- Can change during runtime
- Example: Switch from normal to surge pricing

**Code:**
```java
// Factory: Creates object
Vehicle v = VehicleFactory.getVehicle("BIKE");

// Strategy: Changes behavior
ride.setPricing(new SurgePricing());  // Can switch anytime
```"

---

## Design Principles Applied

**Single Responsibility**
- Each class has one job
- PricingStrategy only calculates fare
- Observer only sends notifications

**Open/Closed**
- Open for extension (add new strategies, states)
- Closed for modification (don't change existing code)

**Dependency Inversion**
- Ride depends on PricingStrategy interface
- Not on concrete NormalPricing or SurgePricing classes

**Favor Composition Over Inheritance**
- Ride uses strategies (composition)
- Not inheritance from PricingBehavior class

---

## Running the Code

```bash
javac Main.java
java Main
```

---

## Quick Cheat Sheet

```
Factory     → VehicleFactory.getVehicle("BIKE")
Strategy    → new SurgePricing() / new UpiPayment()
Observer    → ride.addObserver(new UserApp())
State       → ride.nextState()
Singleton   → MatchingService.getInstance()
```

---

## Real-World Extensions

**Add more features easily:**

```java
// New vehicle type (Factory)
class SUV implements Vehicle { }

// New pricing (Strategy)
class PoolPricing implements PricingStrategy { }

// New payment (Strategy)
class WalletPayment implements PaymentStrategy { }

// New observer
class AdminDashboard implements RideObserver { }

// New state
class CancelledState implements RideState { }
```

**Each addition is just one new class!**

---

## Key Takeaways

1. **Use Factory** when you need to create different types of objects
2. **Use Strategy** when you need to swap algorithms/behaviors
3. **Use Observer** when multiple objects need to react to changes
4. **Use State** when object behavior changes based on its state
5. **Use Singleton** when you need exactly one instance

**Remember:** These patterns solve real problems. Don't use them just because you can - use them because your problem needs them!

---