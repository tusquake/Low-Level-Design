# State Pattern: Vending Machine System

## Simple Analogy

**Vending Machine:**
- **No Coin State:** You can't press a product button.
- **Has Coin State:** You can select a product or eject coin.
- **Dispensing State:** Machine gives you the product (cannot press other buttons).
- **Out of Stock State:** Shows error, doesn't take coins.

The behavior of the machine changes completely based on its current internal state.

---

## Implementation

### 1. State Interface

```java
public interface VendingState {
    void insertCoin();
    void ejectCoin();
    void selectProduct();
    void dispense();
}
```

### 2. Concrete States

```java
public class NoCoinState implements VendingState {
    VendingMachine machine;
    // Handle coin insertion -> Transitions to HasCoinState
}

public class HasCoinState implements VendingState {
    VendingMachine machine;
    // Handle product selection -> Transitions to DispensingState
}

public class DispensingState implements VendingState {
    VendingMachine machine;
    // Dispense product -> Returns to NoCoinState or SoldOutState
}
```

### 3. Vending Machine (Context)

```java
public class VendingMachine {
    private VendingState currentState;
    private int count = 0;

    public void insertCoin() { currentState.insertCoin(); }
    public void ejectCoin() { currentState.ejectCoin(); }
    public void selectProduct() { 
        currentState.selectProduct(); 
        currentState.dispense(); 
    }
}
```

---

## Demo

The demo simulates a user inserting a coin, selecting a product, and the machine dispensing it. It also handles edge cases like "Sold Out" or "No Coin inserted".

```java
VendingMachine machine = new VendingMachine(2); // Start with 2 items

machine.insertCoin();
machine.selectProduct(); // Successfully buys first item
```

---

## Output

```
--- Buying First Item ---
Coin inserted.
Product selected.
A product is rolling out...

--- Trying to Eject Without Coin ---
You haven't inserted a coin.

--- Buying Second Item ---
Coin inserted.
Product selected.
A product is rolling out...
Out of products!
```

---

## Key Benefits

**State Pattern:**
- **Removes `if-else` / `switch`:** No more complex conditional logic based on the internal state.
- **Easy to Add States:** New states (like `MaintenanceState` or `BrokenState`) can be added without modifying the existing classes.
- **Clean Context**: The `VendingMachine` doesn't need to know the specific rules for each state; it just delegates the calls.

---

## Real-World Uses

- **Traffic Light System**: Transitioning between Red, Green, and Yellow.
- **Order Tracking**: Transitions between Ordered, Shipped, Delivered.
- **ATM Machine**: Transitions between Idle, Card Entered, PIN Entered, etc.
- **Game Characters**: Transitions between Patrol, In-Combat, Dead.

---

## Quick Summary

| Component | Role |
|-----------|------|
| State Interface | Defines common actions for all states |
| Concrete States | Implement logic for specific modes |
| Context (VendingMachine) | Maintains reference to the current state |
