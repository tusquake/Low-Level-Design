# Special Case Design Pattern

The Special Case pattern is an extension of the Null Object pattern. While the Null Object pattern handles the "absence" of an object by doing nothing, the Special Case pattern handles **multiple specific conditions** with specialized objects that implement the same interface.

## Problem
You often have complex logic with multiple `if-else` or `switch` statements to handle different types of edge cases (e.g., Guest User, Deactivated User, Expired Subscription).
```java
Customer c = service.getCustomer(id);
if (c == null) {
    // Handle unknown
} else if (c.isDeactivated()) {
    // Handle deactivated
} else {
    // Handle normal
}
```

## Solution
Create specific classes for each "special case." These classes implement the same interface as the real object but encapsulate the logic for that specific edge case.

## Comparison: Null Object vs. Special Case
- **Null Object**: "There is nothing here, so I'll provide a do-nothing object." (One case)
- **Special Case**: "There is something specific here (Guest, Deactivated, etc.), so I'll provide an object that knows how to behave in that situation." (Multiple cases)

## Benefits
1. **Polymorphism**: The client code treats all cases exactly the same.
2. **Encapsulation**: The logic for "Deactivated" behavior is inside the `DeactivatedCustomer` class, not scattered in `if` statements throughout the UI.
3. **Reduces Complexity**: Removes deep nested conditional logic.
