# Composition Over Inheritance: Building Flexible Systems

## The Principle

**"Favor Composition over Inheritance"** - Gang of Four

Instead of extending classes (is-a), compose objects (has-a) for better flexibility.

---

## The Problem: Inheritance Gone Wrong

### Bad Design - Tight Inheritance

```java
class Engine {
    public void start() {
        System.out.println("Engine started");
    }
}

class PetrolEngine extends Engine {
    public void start() {
        System.out.println("Petrol engine started");
    }
}

class DieselEngine extends Engine {
    public void start() {
        System.out.println("Diesel engine started");
    }
}

class ElectricEngine extends Engine {
    public void start() {
        System.out.println("Electric engine started");
    }
}

// THE PROBLEM: Car IS-A PetrolEngine (wrong relationship!)
class Car extends PetrolEngine {
    public void drive() {
        System.out.println("Driving car");
    }
}
```

### Usage
```java
public class BadDesign {
    public static void main(String[] args) {
        Car car = new Car();
        car.start();   // Petrol engine started
        car.drive();   // Driving car
    }
}
```

---

## Why This is BAD

### Problem 1: Wrong Relationship
```java
class Car extends PetrolEngine { }

// This says: "Car IS-A PetrolEngine"
// But reality: "Car HAS-A Engine"
```

A Car is NOT an engine. A Car HAS an engine. Wrong abstraction!

### Problem 2: Locked to One Engine
```java
Car car = new Car();  // Always petrol engine!
// Want diesel? Create new DieselCar class!
// Want electric? Create new ElectricCar class!
```

### Problem 3: Can't Switch Engines
```java
Car car = new Car();  // Petrol forever
// Can't switch to diesel or electric at runtime
// Car is HARDCODED to be a PetrolEngine
```

### Problem 4: Class Explosion
```
Car extends PetrolEngine
DieselCar extends DieselEngine
ElectricCar extends ElectricEngine
HybridCar extends ??? (Which engine to extend?)

What about:
- TurboEngine?
- V6Engine?
- V8Engine?

Need: Car, DieselCar, ElectricCar, TurboCar, V6Car, V8Car, HybridCar...
```

### Problem 5: Can't Have Multiple Engines
```java
// What if car has both petrol and electric? (Hybrid)
class HybridCar extends PetrolEngine, ElectricEngine { }
// ERROR: Java doesn't allow multiple inheritance!
```

---

## The Solution: Composition

### Good Design - Flexible Composition

```java
// Engine interface
interface Engines {
    void start();
}

// Concrete implementations
class PetrolEngines implements Engines {
    public void start() {
        System.out.println("Petrol engine started");
    }
}

class DieselEngines implements Engines {
    public void start() {
        System.out.println("Diesel engine started");
    }
}

class ElectricEngines implements Engines {
    public void start() {
        System.out.println("Electric engine started");
    }
}

// Car HAS-A Engine (composition!)
class Cars {
    private Engines engine;  // has-a relationship

    public Cars(Engines engine) {
        this.engine = engine;
    }

    public void drive() {
        engine.start();
        System.out.println("Car is driving");
    }
    
    // Bonus: Can change engine at runtime!
    public void setEngine(Engines newEngine) {
        this.engine = newEngine;
    }
}
```

### Usage
```java
public class GoodDesign {
    public static void main(String[] args) {
        
        // Petrol car
        Cars petrolCar = new Cars(new PetrolEngines());
        petrolCar.drive();
        
        // Diesel car
        Cars dieselCar = new Cars(new DieselEngines());
        dieselCar.drive();
        
        // Electric car
        Cars electricCar = new Cars(new ElectricEngines());
        electricCar.drive();
        
        // Hybrid car (both engines!)
        Cars hybridCar = new Cars(new PetrolEngines());
        hybridCar.drive();
        hybridCar.setEngine(new ElectricEngines());  // Switch to electric!
        hybridCar.drive();
    }
}
```

### Output
```
Petrol engine started
Car is driving
Diesel engine started
Car is driving
Electric engine started
Car is driving
Petrol engine started
Car is driving
Electric engine started
Car is driving
```

---

## Why This is GOOD

### Benefit 1: Correct Relationship
```java
class Cars {
    private Engines engine;  // Car HAS-A Engine ✓
}
```

### Benefit 2: No Class Explosion
```java
// One Car class handles ALL engine types!
Cars petrolCar = new Cars(new PetrolEngines());
Cars dieselCar = new Cars(new DieselEngines());
Cars electricCar = new Cars(new ElectricEngines());
Cars turboCar = new Cars(new TurboEngines());
// Same Car class, different engines
```

### Benefit 3: Runtime Flexibility
```java
Cars car = new Cars(new PetrolEngines());
car.drive();  // Petrol

car.setEngine(new ElectricEngines());  // Switch engine!
car.drive();  // Electric
```

### Benefit 4: Easy to Extend
```java
// Add new engine type? Just create new class!
class TurboEngines implements Engines {
    public void start() {
        System.out.println("Turbo engine started");
    }
}

// No changes to Car class!
Cars turboCar = new Cars(new TurboEngines());
```

### Benefit 5: Multiple Behaviors
```java
class Cars {
    private Engines engine;
    private Transmission transmission;  // Can have multiple!
    private GPS gps;
    private AirConditioning ac;
    
    // Compose multiple objects!
}
```

---

## Side-by-Side Comparison

| Aspect | Inheritance (BAD) | Composition (GOOD) |
|--------|------------------|-------------------|
| **Relationship** | Car IS-A PetrolEngine | Car HAS-A Engine |
| **Flexibility** | Locked to one engine | Any engine |
| **Runtime change** | No | Yes |
| **Adding new engine** | New Car subclass | New Engine class |
| **Number of classes** | Car, DieselCar, ElectricCar... | Just Car + Engines |
| **Multiple features** | Can't (no multiple inheritance) | Can (compose many objects) |
| **Testability** | Hard (inherit everything) | Easy (inject mock engine) |

---

## Real-World Example: Notification System

### Bad - Inheritance
```java
class Notification { }
class EmailNotification extends Notification { }
class SMSNotification extends Notification { }

// Want both email AND SMS?
class EmailAndSMSNotification extends ??? // Can't extend both!

// What about Email + SMS + Push?
// Need: EmailNotification, SMSNotification, PushNotification,
//       EmailSMSNotification, EmailPushNotification,
//       SMSPushNotification, EmailSMSPushNotification
// 7 classes for 3 notification types!
```

### Good - Composition
```java
interface NotificationSender {
    void send(String message);
}

class EmailSender implements NotificationSender { }
class SMSSender implements NotificationSender { }
class PushSender implements NotificationSender { }

class Notification {
    private List<NotificationSender> senders;  // Compose multiple!
    
    public void send(String message) {
        for (NotificationSender sender : senders) {
            sender.send(message);
        }
    }
}

// Usage
Notification notification = new Notification();
notification.addSender(new EmailSender());
notification.addSender(new SMSSender());
notification.addSender(new PushSender());
notification.send("Hello");  // Sends via all three!
```

---

## When to Use Each

### Use Inheritance When:
- True IS-A relationship exists
- Example: Dog IS-A Animal ✓

```java
class Animal {
    void breathe() { }
}

class Dog extends Animal {
    void bark() { }  // Dog IS truly an Animal
}
```

### Use Composition When:
- HAS-A relationship
- Need flexibility
- Want to change behavior at runtime
- Example: Car HAS-A Engine ✓

```java
class Car {
    private Engine engine;  // Car HAS an Engine
}
```

---

## Common Interview Questions

### Q1: Why is "Car extends PetrolEngine" bad design?

**A:** "It's bad because:

**1. Wrong Relationship:**
```java
class Car extends PetrolEngine { }
// Says: Car IS-A PetrolEngine
// Reality: Car HAS-A Engine
```

**2. Locked In:**
```java
Car car = new Car();  // ALWAYS petrol
// Want diesel? Need to create DieselCar class
// Want electric? Need to create ElectricCar class
```

**3. Can't Switch:**
```java
// Can't do this with inheritance:
car.setEngine(new DieselEngine());
```

**Better design:**
```java
class Car {
    private Engine engine;  // HAS-A
    
    public Car(Engine engine) {
        this.engine = engine;
    }
}

// Now flexible!
Car petrolCar = new Car(new PetrolEngine());
Car dieselCar = new Car(new DieselEngine());
```"

---

### Q2: What's the difference between IS-A and HAS-A?

**A:** "IS-A uses inheritance, HAS-A uses composition.

**IS-A (Inheritance):**
```java
class Dog extends Animal { }
// Dog IS-A Animal (true relationship)
// Dog inherits all Animal behaviors
```

**HAS-A (Composition):**
```java
class Car {
    private Engine engine;  // Car HAS-A Engine
}
// Car contains an Engine
// Car can change engines
```

**Rule of thumb:**
- If you can say 'X is a type of Y' → Inheritance
- If you can say 'X has a Y' → Composition

**Examples:**
- Dog IS-A Animal → Inheritance ✓
- Car IS-A Engine → Wrong! ✗
- Car HAS-A Engine → Composition ✓"

---

### Q3: Give a real example where composition is better than inheritance.

**A:** "In a game I worked on, we had characters with abilities:

**Bad - Inheritance:**
```java
class Character { }
class FlyingCharacter extends Character { }
class SwimmingCharacter extends Character { }
// Want flying AND swimming? Can't extend both!

// Ended up with:
class FlyingCharacter { }
class SwimmingCharacter { }
class FlyingSwimmingCharacter { }  // Duplicate code!
class FlyingInvisibleCharacter { }
class SwimmingInvisibleCharacter { }
class FlyingSwimmingInvisibleCharacter { }
// Too many classes!
```

**Good - Composition:**
```java
interface Ability {
    void use();
}

class FlyAbility implements Ability { }
class SwimAbility implements Ability { }
class InvisibilityAbility implements Ability { }

class Character {
    private List<Ability> abilities;
    
    public void addAbility(Ability ability) {
        abilities.add(ability);
    }
    
    public void useAbilities() {
        for (Ability ability : abilities) {
            ability.use();
        }
    }
}

// Usage - ANY combination!
Character hero = new Character();
hero.addAbility(new FlyAbility());
hero.addAbility(new SwimAbility());
hero.addAbility(new InvisibilityAbility());
// One Character class, unlimited combinations!
```

Benefits:
- 1 Character class vs 20+ subclasses
- Add abilities at runtime
- Easy to add new abilities"

---

### Q4: Can you use both inheritance AND composition together?

**A:** "Yes! Use each where appropriate.

**Example: Payment System**
```java
// Inheritance for shared behavior (IS-A)
abstract class Payment {
    protected double amount;
    
    public abstract void process();
    
    protected void log() {
        System.out.println("Payment logged");
    }
}

class CreditCardPayment extends Payment {  // IS-A Payment
    public void process() { /* ... */ }
}

// Composition for flexibility (HAS-A)
class PaymentProcessor {
    private Payment payment;           // HAS-A Payment
    private Logger logger;             // HAS-A Logger
    private FraudDetector detector;    // HAS-A FraudDetector
    
    public PaymentProcessor(Payment payment, Logger logger, FraudDetector detector) {
        this.payment = payment;
        this.logger = logger;
        this.detector = detector;
    }
    
    public void execute() {
        detector.check(payment);
        payment.process();
        logger.log(payment);
    }
}
```

**When to use which:**
- Inheritance: When there's a true IS-A relationship
- Composition: When you need flexibility or HAS-A relationship
- **Default**: Favor composition, use inheritance sparingly"

---

## Quick Decision Guide

```
Ask: "Does X IS-A Y make sense?"

NO → Use Composition
    Car HAS-A Engine ✓
    Player HAS-A Weapon ✓
    House HAS-A Door ✓

YES → Check: "Will X always be a Y?"
    
    YES → Maybe Inheritance
        Dog IS-A Animal ✓
        Circle IS-A Shape ✓
    
    NO → Use Composition
        Employee IS-A Manager? 
        (Not always! Employee might become Manager)
        → Use Composition ✓
```

---

## Key Takeaways

1. **Inheritance** = IS-A relationship (rigid, compile-time)
2. **Composition** = HAS-A relationship (flexible, runtime)
3. **Default to composition** unless true IS-A relationship exists
4. **Composition** allows behavior changes at runtime
5. **Composition** prevents class explosion

**Remember:**
```
Inheritance: "Be careful, once you extend, you're stuck"
Composition: "Flexible like LEGO blocks, build anything"
```

**Golden Rule:** If in doubt, use composition!
