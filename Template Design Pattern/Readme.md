# Template Design Pattern: Beverage Preparation System

## What is Template Pattern?

Template pattern defines the skeleton of an algorithm in a base class, letting subclasses override specific steps without changing the algorithm's structure.

**Real-World:** Recipe where main steps are fixed, but ingredients vary (Coffee vs Tea).

---

## Problem

**Without Template:**
```java
class Coffee {
    void make() {
        boilWater();
        brewCoffee();
        pourInCup();
        addSugar();
        addMilk();
    }
}

class Tea {
    void make() {
        boilWater();      // Duplicated
        steepTea();
        pourInCup();      // Duplicated
        addLemon();
    }
}
```

**With Template:**
```java
abstract class Beverage {
    final void prepare() {
        boilWater();      // Common
        brew();           // Varies
        pourInCup();      // Common
        addCondiments();  // Varies
    }
}
```

---

## Implementation

### 1. Abstract Template Class

```java
abstract class BeverageTemplate {
    
    // Template method - defines algorithm skeleton
    public final void prepareBeverage() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        }
        serve();
    }
    
    // Common steps (implemented in base class)
    private void boilWater() {
        System.out.println("Boiling water...");
    }
    
    private void pourInCup() {
        System.out.println("Pouring into cup...");
    }
    
    private void serve() {
        System.out.println("Your beverage is ready! Enjoy!");
        System.out.println("---");
    }
    
    // Abstract steps (subclasses must implement)
    protected abstract void brew();
    protected abstract void addCondiments();
    
    // Hook method (optional override)
    protected boolean customerWantsCondiments() {
        return true;
    }
}
```

### 2. Concrete Class - Coffee

```java
class Coffee extends BeverageTemplate {
    
    @Override
    protected void brew() {
        System.out.println("Brewing coffee grounds...");
    }
    
    @Override
    protected void addCondiments() {
        System.out.println("Adding sugar and milk");
    }
}
```

### 3. Concrete Class - Tea

```java
class Tea extends BeverageTemplate {
    
    @Override
    protected void brew() {
        System.out.println("Steeping tea leaves...");
    }
    
    @Override
    protected void addCondiments() {
        System.out.println("Adding lemon");
    }
}
```

### 4. Concrete Class - Hot Chocolate

```java
class HotChocolate extends BeverageTemplate {
    
    @Override
    protected void brew() {
        System.out.println("Mixing chocolate powder...");
    }
    
    @Override
    protected void addCondiments() {
        System.out.println("Adding whipped cream and marshmallows");
    }
    
    @Override
    protected boolean customerWantsCondiments() {
        return false; // Skip condiments
    }
}
```

### 5. Demo

```java
public class BeverageShop {
    public static void main(String[] args) {
        
        System.out.println("=== Making Coffee ===");
        BeverageTemplate coffee = new Coffee();
        coffee.prepareBeverage();
        
        System.out.println("\n=== Making Tea ===");
        BeverageTemplate tea = new Tea();
        tea.prepareBeverage();
        
        System.out.println("\n=== Making Hot Chocolate (No Condiments) ===");
        BeverageTemplate hotChocolate = new HotChocolate();
        hotChocolate.prepareBeverage();
    }
}
```

---

## Output

```
=== Making Coffee ===
Boiling water...
Brewing coffee grounds...
Pouring into cup...
Adding sugar and milk
Your beverage is ready! Enjoy!
---

=== Making Tea ===
Boiling water...
Steeping tea leaves...
Pouring into cup...
Adding lemon
Your beverage is ready! Enjoy!
---

=== Making Hot Chocolate (No Condiments) ===
Boiling water...
Mixing chocolate powder...
Pouring into cup...
Your beverage is ready! Enjoy!
---
```

---

## Key Concepts

### 1. Template Method
```java
public final void prepareBeverage() {
    // Algorithm skeleton - cannot be overridden (final)
}
```

### 2. Abstract Methods
```java
protected abstract void brew();
// Subclasses MUST implement
```

### 3. Hook Methods
```java
protected boolean customerWantsCondiments() {
    return true; // Optional override
}
```

### 4. Concrete Methods
```java
private void boilWater() {
    // Common implementation
}
```

---

## Benefits

**Code Reuse:** Common steps defined once

**Control:** Base class controls algorithm structure

**Flexibility:** Subclasses customize specific steps

**Avoid Duplication:** Eliminates repeated code

---

## Real-World Examples

**Data Processing Pipeline:**
```java
abstract class DataProcessor {
    final void process() {
        readData();
        validateData();
        transformData();
        saveData();
    }
}
```

**Testing Frameworks:**
```java
abstract class TestCase {
    final void runTest() {
        setUp();
        executeTest();
        tearDown();
    }
}
```

**Game Development:**
```java
abstract class Game {
    final void play() {
        initialize();
        startPlay();
        endPlay();
    }
}
```

**Build Systems:** Maven lifecycle (validate → compile → test → package)

---

## Class Diagram

```
┌───────────────────────┐
│  BeverageTemplate     │
├───────────────────────┤
│ + prepareBeverage()   │ ◄── Template Method (final)
│ - boilWater()         │ ◄── Concrete
│ - pourInCup()         │ ◄── Concrete
│ # brew()              │ ◄── Abstract
│ # addCondiments()     │ ◄── Abstract
│ # customerWants...()  │ ◄── Hook
└───────────┬───────────┘
            △
            │
    ┌───────┴────────┐
    │                │
┌───┴────┐     ┌────┴───┐
│ Coffee │     │  Tea   │
├────────┤     ├────────┤
│+ brew()│     │+ brew()│
└────────┘     └────────┘
```

---

## Template vs Strategy

| Template Pattern | Strategy Pattern |
|-----------------|------------------|
| Uses inheritance | Uses composition |
| Base class controls flow | Client chooses strategy |
| Compile-time binding | Runtime binding |
| Example: Beverage preparation | Example: Payment methods |

---

## When to Use

- Multiple classes have similar algorithms with minor differences
- Want to control algorithm structure
- Common behavior should be in one place
- Want to avoid code duplication

---

## Quick Summary

| Aspect | Description |
|--------|-------------|
| **Problem** | Duplicated algorithm structure across classes |
| **Solution** | Define skeleton in base class, vary steps in subclasses |
| **Benefit** | Code reuse, controlled customization |
| **Use Case** | Data processing, test frameworks, game loops |