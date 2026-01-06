# Decorator + Adapter Pattern: Food Delivery App

## Real-World Scenario: Swiggy/Zomato Integration

**Problem:**
You're building a food delivery app that needs to:
1. **Integrate with different restaurant systems** (Adapter Pattern)
   - Some restaurants have modern APIs
   - Some have legacy systems with different interfaces
2. **Add extra features to orders dynamically** (Decorator Pattern)
   - Gift wrapping
   - Priority delivery
   - Insurance
   - Contactless delivery

---

## Architecture Overview

```
Customer Order
    ↓
Adapter (converts different restaurant APIs)
    ↓
Base Order
    ↓
Decorators (add features: gift wrap, priority, insurance)
    ↓
Final Order
```

---

## Implementation

### 1. Target Interface (What Our App Needs)

```java
public interface Order {
    String getDescription();
    double getCost();
    void placeOrder();
}
```

---

## PART 1: BASE ORDERS

### 2. Modern Restaurant (Already Compatible)

```java
public class ModernRestaurantOrder implements Order {
    private String itemName;
    private double price;
    
    public ModernRestaurantOrder(String itemName, double price) {
        this.itemName = itemName;
        this.price = price;
    }
    
    @Override
    public String getDescription() {
        return itemName;
    }
    
    @Override
    public double getCost() {
        return price;
    }
    
    @Override
    public void placeOrder() {
        System.out.println("Order placed through Modern API");
    }
}
```

---

## PART 2: ADAPTER (For Legacy Restaurants)

### 3. Legacy Restaurant (Incompatible Interface)

```java
// Old restaurant system with different method names
public class LegacyRestaurantSystem {
    public void submitFoodRequest(String food, double amount) {
        System.out.println("Legacy System: Received order for " + food + " - $" + amount);
    }
    
    public String getFoodName() {
        return "Legacy Food Item";
    }
    
    public double getFoodPrice() {
        return 15.0;
    }
}
```

### 4. Adapter (Makes Legacy Compatible)

```java
public class LegacyRestaurantAdapter implements Order {
    private LegacyRestaurantSystem legacySystem;
    private String itemName;
    private double price;
    
    public LegacyRestaurantAdapter(LegacyRestaurantSystem legacySystem, 
                                   String itemName, double price) {
        this.legacySystem = legacySystem;
        this.itemName = itemName;
        this.price = price;
    }
    
    @Override
    public String getDescription() {
        return itemName;
    }
    
    @Override
    public double getCost() {
        return price;
    }
    
    @Override
    public void placeOrder() {
        // Adapt our interface to legacy interface
        legacySystem.submitFoodRequest(itemName, price);
    }
}
```

---

## PART 3: DECORATORS (Add Features)

### 5. Base Decorator

```java
public abstract class OrderDecorator implements Order {
    protected Order order;
    
    public OrderDecorator(Order order) {
        this.order = order;
    }
    
    @Override
    public String getDescription() {
        return order.getDescription();
    }
    
    @Override
    public double getCost() {
        return order.getCost();
    }
    
    @Override
    public void placeOrder() {
        order.placeOrder();
    }
}
```

### 6. Concrete Decorators

```java
// Add Gift Wrapping
public class GiftWrapDecorator extends OrderDecorator {
    public GiftWrapDecorator(Order order) {
        super(order);
    }
    
    @Override
    public String getDescription() {
        return order.getDescription() + " + Gift Wrap";
    }
    
    @Override
    public double getCost() {
        return order.getCost() + 2.0;
    }
    
    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Adding gift wrapping");
    }
}

// Add Priority Delivery
public class PriorityDeliveryDecorator extends OrderDecorator {
    public PriorityDeliveryDecorator(Order order) {
        super(order);
    }
    
    @Override
    public String getDescription() {
        return order.getDescription() + " + Priority Delivery";
    }
    
    @Override
    public double getCost() {
        return order.getCost() + 5.0;
    }
    
    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Marking as priority delivery (30 mins)");
    }
}

// Add Insurance
public class InsuranceDecorator extends OrderDecorator {
    public InsuranceDecorator(Order order) {
        super(order);
    }
    
    @Override
    public String getDescription() {
        return order.getDescription() + " + Insurance";
    }
    
    @Override
    public double getCost() {
        return order.getCost() + 1.5;
    }
    
    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Adding order insurance");
    }
}

// Add Contactless Delivery
public class ContactlessDecorator extends OrderDecorator {
    public ContactlessDecorator(Order order) {
        super(order);
    }
    
    @Override
    public String getDescription() {
        return order.getDescription() + " + Contactless";
    }
    
    @Override
    public double getCost() {
        return order.getCost(); // Free
    }
    
    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Enabled contactless delivery");
    }
}
```

---

## Usage Demo

```java
public class FoodDeliveryApp {
    public static void main(String[] args) {
        
        System.out.println("=== ORDER 1: Modern Restaurant ===");
        // Order from modern restaurant
        Order order1 = new ModernRestaurantOrder("Chicken Biryani", 12.0);
        System.out.println("Base: " + order1.getDescription() + " = $" + order1.getCost());
        
        // Add gift wrap
        order1 = new GiftWrapDecorator(order1);
        System.out.println("After: " + order1.getDescription() + " = $" + order1.getCost());
        
        // Add priority delivery
        order1 = new PriorityDeliveryDecorator(order1);
        System.out.println("Final: " + order1.getDescription() + " = $" + order1.getCost());
        
        order1.placeOrder();
        
        
        System.out.println("\n=== ORDER 2: Legacy Restaurant (Using Adapter) ===");
        // Order from legacy restaurant through adapter
        LegacyRestaurantSystem legacySystem = new LegacyRestaurantSystem();
        Order order2 = new LegacyRestaurantAdapter(legacySystem, "Butter Chicken", 15.0);
        System.out.println("Base: " + order2.getDescription() + " = $" + order2.getCost());
        
        // Add insurance
        order2 = new InsuranceDecorator(order2);
        System.out.println("After: " + order2.getDescription() + " = $" + order2.getCost());
        
        // Add contactless
        order2 = new ContactlessDecorator(order2);
        System.out.println("Final: " + order2.getDescription() + " = $" + order2.getCost());
        
        order2.placeOrder();
        
        
        System.out.println("\n=== ORDER 3: Complex Order ===");
        // Complex order with all features
        Order order3 = new ContactlessDecorator(
                            new InsuranceDecorator(
                                new PriorityDeliveryDecorator(
                                    new GiftWrapDecorator(
                                        new ModernRestaurantOrder("Paneer Tikka", 10.0)
                                    )
                                )
                            )
                        );
        
        System.out.println("Description: " + order3.getDescription());
        System.out.println("Total Cost: $" + order3.getCost());
        order3.placeOrder();
    }
}
```

---

## Output

```
=== ORDER 1: Modern Restaurant ===
Base: Chicken Biryani = $12.0
After: Chicken Biryani + Gift Wrap = $14.0
Final: Chicken Biryani + Gift Wrap + Priority Delivery = $19.0
Order placed through Modern API
  → Adding gift wrapping
  → Marking as priority delivery (30 mins)

=== ORDER 2: Legacy Restaurant (Using Adapter) ===
Base: Butter Chicken = $15.0
After: Butter Chicken + Insurance = $16.5
Final: Butter Chicken + Insurance + Contactless = $16.5
Legacy System: Received order for Butter Chicken - $15.0
  → Adding order insurance
  → Enabled contactless delivery

=== ORDER 3: Complex Order ===
Description: Paneer Tikka + Gift Wrap + Priority Delivery + Insurance + Contactless
Total Cost: $18.5
Order placed through Modern API
  → Adding gift wrapping
  → Marking as priority delivery (30 mins)
  → Adding order insurance
  → Enabled contactless delivery
```

---

## How Patterns Work Together

### Adapter Pattern:
**Problem:** Different restaurants have different APIs
**Solution:** Adapter converts legacy restaurant interface to our app's interface
**Benefit:** Can integrate any restaurant regardless of their system

### Decorator Pattern:
**Problem:** Need to add optional features (gift wrap, priority, insurance) dynamically
**Solution:** Wrap orders with decorator layers
**Benefit:** Any combination of features without creating separate classes

### Together:
1. **Adapter** makes incompatible restaurants compatible
2. **Decorator** adds features on top of any order (modern or legacy)
3. Both work seamlessly together

---

## Real-World Flow

```
Customer selects "Butter Chicken" from Old Restaurant
    ↓
App uses LegacyRestaurantAdapter (Adapter Pattern)
    ↓
Customer adds "Gift Wrap" option
    ↓
App wraps with GiftWrapDecorator (Decorator Pattern)
    ↓
Customer adds "Priority Delivery"
    ↓
App wraps with PriorityDeliveryDecorator (Decorator Pattern)
    ↓
Final order processed with all features
```

---

## Key Benefits

**Adapter:**
- Integrate restaurants with any API
- No need to modify restaurant systems
- Add new restaurant types easily

**Decorator:**
- Add features dynamically at checkout
- Any combination of features
- No class explosion (no need for PriorityGiftWrapOrder, PriorityInsuranceOrder, etc.)

**Together:**
- Handle any restaurant (Adapter)
- Add any features (Decorator)
- Flexible and maintainable system

---

## Other Real-World Examples

**E-commerce Platform:**
- Adapter: Integrate different payment gateways (PayPal, Stripe, Razorpay)
- Decorator: Add gift wrapping, express shipping, insurance

**Streaming Service:**
- Adapter: Support different video formats (MP4, AVI, MKV)
- Decorator: Add subtitles, HD quality, offline download

**Hotel Booking:**
- Adapter: Integrate different hotel systems (old PMS, new APIs)
- Decorator: Add breakfast, airport pickup, late checkout

---

## Interview Answer

**Q: When would you use Adapter and Decorator together?**

**A:** "I'd use them together when I need to:

1. **Integrate incompatible systems** (Adapter)
2. **Add optional features dynamically** (Decorator)

For example, in a food delivery app:
- **Adapter** lets us integrate restaurants with different APIs - modern REST APIs or legacy SOAP systems - without changing their code
- **Decorator** lets customers add features like gift wrap, priority delivery, or insurance to any order dynamically

The Adapter makes everything compatible first, then Decorators add features on top. This gives us flexibility to work with any restaurant and offer any combination of features without creating hundreds of order classes."

---

## Quick Summary

| Pattern | Purpose | When to Use |
|---------|---------|-------------|
| **Adapter** | Make incompatible interfaces work together | Integrating legacy/third-party systems |
| **Decorator** | Add features dynamically without changing code | Optional features, dynamic behavior |
| **Together** | Unified interface + Flexible features | Complex systems with multiple sources and optional add-ons |