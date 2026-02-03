# Factory Method + Abstract Factory Pattern: Restaurant Order System

## Real-World Scenario: Pizza Hut/Domino's Order Management

**Problem:**
You're building a restaurant ordering system that needs to:
1. **Create different types of products easily** (Factory Method Pattern)
    - Different pizza varieties based on customer choice
    - Simple object creation without complex logic
2. **Create complete meal combinations** (Abstract Factory Pattern)
    - Veg meals (Veg Pizza + Coke + Fries)
    - Non-Veg meals (Chicken Pizza + Pepsi + Wings)
    - Ensure products in a meal are compatible

---

## Architecture Overview

```
Customer Request
    ↓
Factory Method (creates single product)
    OR
    ↓
Abstract Factory (creates product families)
    ↓
Final Products
```

---

## Pattern Comparison

| Aspect | Factory Method | Abstract Factory |
|--------|---------------|------------------|
| **Creates** | Single product | Family of related products |
| **Complexity** | Simple | More complex |
| **Use Case** | One type of object | Multiple related objects |
| **Example** | Create a pizza | Create complete meal (pizza + drink + side) |

---

## PART 1: FACTORY METHOD PATTERN

### Problem
Creating different pizza types requires lots of `if-else` or `switch` statements scattered throughout code.

### Solution
Centralize object creation in a factory class.

---

## Implementation - Factory Method

### 1. Product Interface

```java
interface Pizza {
    void bake();
}
```

### 2. Concrete Products

```java
class VegPizza implements Pizza {
    public void bake() {
        System.out.println("Baking Veg Pizza");
    }
}

class CheesePizza implements Pizza {
    public void bake() {
        System.out.println("Baking Cheese Pizza");
    }
}
```

### 3. Factory Class

```java
class PizzaFactory {
    public static Pizza getPizza(String type) {
        if (type.equalsIgnoreCase("veg"))
            return new VegPizza();
        else if (type.equalsIgnoreCase("cheese"))
            return new CheesePizza();
        return null;
    }
}
```

### 4. Usage

```java
public class FactoryDemo {
    public static void main(String[] args) {
        Pizza pizza = PizzaFactory.getPizza("cheese");
        pizza.bake();
    }
}
```

### Output
```
Baking Cheese Pizza
```

---

## Benefits - Factory Method

✅ **Centralized Creation Logic**
- All object creation in one place
- Easy to maintain and modify

✅ **Loose Coupling**
- Client doesn't know about concrete classes
- Works with interface only

✅ **Easy to Extend**
- Add new pizza types without changing client code

---

## PART 2: ABSTRACT FACTORY PATTERN

### Problem
Need to create families of related products that work together (Veg meal vs Non-Veg meal).

### Solution
Create factory interfaces that produce families of related objects.

---

## Implementation - Abstract Factory

### 1. Product Interfaces

```java
interface Pizzas {
    void bake();
}

interface Drink {
    void pour();
}

interface Side {
    void prepare();
}
```

### 2. Concrete Products - Veg Family

```java
class VegPizzas implements Pizzas {
    public void bake() {
        System.out.println("Baking Veg Pizzas");
    }
}

class Coke implements Drink {
    public void pour() {
        System.out.println("Pouring Coke");
    }
}

class Fries implements Side {
    public void prepare() {
        System.out.println("Preparing Fries");
    }
}
```

### 3. Concrete Products - Non-Veg Family

```java
class ChickenPizzas implements Pizzas {
    public void bake() {
        System.out.println("Baking Chicken Pizzas");
    }
}

class Pepsi implements Drink {
    public void pour() {
        System.out.println("Pouring Pepsi");
    }
}

class Wings implements Side {
    public void prepare() {
        System.out.println("Preparing Chicken Wings");
    }
}
```

### 4. Abstract Factory Interface

```java
interface MealFactory {
    Pizzas createPizzas();
    Drink createDrink();
    Side createSide();
}
```

### 5. Concrete Factories

```java
class VegMealFactory implements MealFactory {
    public Pizzas createPizzas() {
        return new VegPizzas();
    }
    public Drink createDrink() {
        return new Coke();
    }
    public Side createSide() {
        return new Fries();
    }
}

class NonVegMealFactory implements MealFactory {
    public Pizzas createPizzas() {
        return new ChickenPizzas();
    }
    public Drink createDrink() {
        return new Pepsi();
    }
    public Side createSide() {
        return new Wings();
    }
}
```

### 6. Usage

```java
public class AbstractFactory {
    public static void main(String[] args) {

        MealFactory factory = new VegMealFactory();

        Pizzas pizzas = factory.createPizzas();
        Drink drink = factory.createDrink();
        Side side = factory.createSide();

        pizzas.bake();
        drink.pour();
        side.prepare();
    }
}
```

### Output
```
Baking Veg Pizzas
Pouring Coke
Preparing Fries
```

---

## Benefits - Abstract Factory

✅ **Product Family Consistency**
- Ensures related products are used together
- Veg meal always gets veg-compatible items

✅ **Easy to Switch Families**
- Change one line: `new VegMealFactory()` → `new NonVegMealFactory()`
- Entire meal family changes

✅ **Isolation of Concrete Classes**
- Client works with interfaces only
- Concrete product classes are hidden

---

## Real-World Flow Comparison

### Factory Method Flow
```
Customer: "I want a cheese pizza"
    ↓
PizzaFactory.getPizza("cheese")
    ↓
Creates CheesePizza object
    ↓
pizza.bake()
```

### Abstract Factory Flow
```
Customer: "I want a veg meal"
    ↓
Create VegMealFactory
    ↓
Factory creates: VegPizza + Coke + Fries
    ↓
Execute all actions
```

---

## When to Use Which Pattern?

### Use Factory Method When:
- Creating **single objects**
- Simple product variations
- Object creation logic is straightforward
- Example: Choose pizza type

### Use Abstract Factory When:
- Creating **families of related objects**
- Products must be used together
- Need to switch entire product families
- Example: Complete meal combos

---

## Running the Code

```bash
# Factory Method Pattern
javac FactoryDemo.java
java FactoryDemo

# Abstract Factory Pattern
javac AbstractFactory.java
java AbstractFactory
```

---

## Real-World Examples

### Factory Method Pattern

**E-commerce Payment:**
```java
PaymentFactory.getPayment("creditcard")  // Creates CreditCardPayment
PaymentFactory.getPayment("paypal")      // Creates PayPalPayment
```

**Document Creation:**
```java
DocumentFactory.getDocument("pdf")   // Creates PDFDocument
DocumentFactory.getDocument("word")  // Creates WordDocument
```

**Logger Creation:**
```java
LoggerFactory.getLogger("file")      // Creates FileLogger
LoggerFactory.getLogger("console")   // Creates ConsoleLogger
```

### Abstract Factory Pattern

**UI Theme System:**
```java
// Dark Theme Factory creates: DarkButton + DarkTextbox + DarkCheckbox
// Light Theme Factory creates: LightButton + LightTextbox + LightCheckbox
```

**Database Connection:**
```java
// MySQL Factory creates: MySQLConnection + MySQLCommand + MySQLDataReader
// Oracle Factory creates: OracleConnection + OracleCommand + OracleDataReader
```

**Gaming Platform:**
```java
// PlayStation Factory creates: PSController + PSGraphics + PSAudio
// Xbox Factory creates: XboxController + XboxGraphics + XboxAudio
```

---

## Common Mistakes

### ❌ Factory Method Mistakes

```java
// BAD: Client creating objects directly
Pizza pizza = new CheesePizza();  // Tight coupling!

// GOOD: Using factory
Pizza pizza = PizzaFactory.getPizza("cheese");
```

### ❌ Abstract Factory Mistakes

```java
// BAD: Mixing product families
Pizzas pizza = vegFactory.createPizzas();
Drink drink = nonVegFactory.createDrink();  // Inconsistent!

// GOOD: Using same factory
MealFactory factory = new VegMealFactory();
Pizzas pizza = factory.createPizzas();
Drink drink = factory.createDrink();  // Consistent family
```

---

## Advanced: Combining Both Patterns

```java
// Use Abstract Factory to get meal factory
MealFactory mealFactory = MealFactoryProvider.getFactory("veg");

// Use Factory Method within to create specific items
Pizzas pizza = mealFactory.createPizzas();
```

This gives you:
- **Factory Method** for simple object creation
- **Abstract Factory** for related object families

---

## Interview Questions & Answers

### Q1: What's the difference between Factory Method and Abstract Factory?

**A:** "Factory Method creates single objects, while Abstract Factory creates families of related objects.

For example:
- **Factory Method**: PizzaFactory creates just pizzas (Veg, Cheese, Chicken)
- **Abstract Factory**: MealFactory creates complete meals (Pizza + Drink + Side together)

Use Factory Method when you need one type of product. Use Abstract Factory when products must work together as a set."

---

### Q2: Why use factories instead of `new` keyword?

**A:** "Factories provide several benefits:

1. **Loose Coupling**: Client doesn't depend on concrete classes
2. **Centralized Logic**: All creation logic in one place
3. **Easy to Extend**: Add new products without changing client code
4. **Consistency**: Abstract Factory ensures related products work together

For example, if I add PepperoniPizza, I only modify PizzaFactory, not every place that creates pizzas."

---

### Q3: Can you give a real-world scenario where you'd use Abstract Factory?

**A:** "In a cross-platform UI application:

```java
interface GUIFactory {
    Button createButton();
    Checkbox createCheckbox();
    Textbox createTextbox();
}

class WindowsFactory implements GUIFactory {
    // Creates Windows-style UI components
}

class MacFactory implements GUIFactory {
    // Creates Mac-style UI components
}
```

When the app runs on Windows, use WindowsFactory to create all UI components. On Mac, use MacFactory. This ensures all UI elements match the platform's look and feel. You can't accidentally mix Windows buttons with Mac checkboxes."

---

## Design Principles Applied

### Single Responsibility Principle
- Factory classes have one job: create objects
- Client classes focus on using objects, not creating them

### Open/Closed Principle
- Open for extension: Add new products easily
- Closed for modification: Client code doesn't change

### Dependency Inversion Principle
- Client depends on interfaces (Pizza, MealFactory)
- Not on concrete classes (CheesePizza, VegMealFactory)

---

## Quick Reference

### Factory Method Pattern
```
Purpose: Create single objects
Participants: Product, ConcreteProduct, Factory
When: Simple object creation with variations
Example: PizzaFactory.getPizza("cheese")
```

### Abstract Factory Pattern
```
Purpose: Create product families
Participants: AbstractFactory, ConcreteFactory, Products
When: Related products must work together
Example: MealFactory creates Pizza + Drink + Side
```

---

## Summary

| Feature | Factory Method | Abstract Factory |
|---------|---------------|------------------|
| **Scope** | Single product | Product family |
| **Complexity** | Simple | Complex |
| **Methods** | One creation method | Multiple creation methods |
| **Guarantee** | Creates one type | Ensures compatibility |
| **Example** | Pizza factory | Meal combo factory |

**Bottom Line:**
- Need one product? → **Factory Method**
- Need related products? → **Abstract Factory**
- Both reduce coupling and improve maintainability!

---