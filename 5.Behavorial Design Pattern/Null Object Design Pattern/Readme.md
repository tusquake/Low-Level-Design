# Null Object Design Pattern

The Null Object Pattern is used to avoid null checks in the client code. Instead of returning `null` when an object is not found or is not applicable, we return a "Null Object" that implements the same interface but does nothing or provides default safe behavior.

## Problem
In many cases, methods return `null` to indicate the absence of an object. This forces the caller to perform a null check before calling any methods on the returned object.
```java
Vehicle v = factory.getVehicle("BIKE");
if (v != null) {
    v.getTankCapacity();
}
```

## Solution
Create an object that represents the "Nothing" or "Null" state. This object implements the same interface as the real objects but provides neutral/safe default behavior.
```java
Vehicle v = factory.getVehicle("BIKE"); // Returns NullVehicle instead of null
v.getTankCapacity(); // Works fine, returns 0
```

## Key Benefits
1. **Reduces NullPointerExceptions**: Since you're not dealing with `null` references.
2. **Cleaner Code**: Removes repetitive `if (obj != null)` checks.
3. **Symmetry**: The client treats real objects and the null object exactly the same way.

## Structure in this Example
- `Vehicle`: The interface defining the behavior.
- `Car`: A real implementation.
- `NullVehicle`: The null object implementation (returns 0 for capacities).
- `VehicleFactory`: Returns `NullVehicle` instead of `null` when a type is unknown.
- `Main`: Demonstrates that no null checks are needed.
