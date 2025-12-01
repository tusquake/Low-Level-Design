# Bridge Design Pattern

## Overview

The Bridge pattern is a structural design pattern that **decouples abstraction from implementation**, allowing both to vary independently. It achieves this by using composition over inheritance.

## Problem Statement

Traditional inheritance-based approaches lead to class explosion when dealing with multiple independent dimensions of variation.

### Without Bridge Pattern

Consider a vehicle system with different vehicle types and engine types:

```
ElectricCar
DieselCar
PetrolCar
ElectricBike
DieselBike
PetrolBike
ElectricTruck
DieselTruck
PetrolTruck
```

**Issue**: Adding one vehicle type requires creating N engine variants. Adding one engine type requires creating M vehicle variants.

**Result**: N × M class combinations leading to maintenance complexity and rigid design.

## Solution

The Bridge pattern separates the hierarchy into two independent dimensions:

1. **Abstraction** (Vehicle): Defines high-level operations
2. **Implementation** (Engine): Defines low-level operations

The abstraction contains a reference to the implementation, using composition instead of inheritance.

## Architecture

```
Abstraction Layer
├── Vehicle (Abstract)
    ├── Car
    ├── Bike
    └── Truck

Implementation Layer
├── Engine (Interface)
    ├── ElectricEngine
    ├── DieselEngine
    └── PetrolEngine
```

## Implementation

### Core Components

**Engine Interface** (Implementation)
```java
interface Engine {
    void start();
    void stop();
    void accelerate();
}
```

**Concrete Implementations**
```java
class ElectricEngine implements Engine { /* ... */ }
class DieselEngine implements Engine { /* ... */ }
```

**Vehicle Abstraction**
```java
abstract class Vehicle {
    protected final Engine engine;
    
    public Vehicle(Engine engine) {
        this.engine = engine;
    }
    
    public abstract void drive();
    public abstract void park();
}
```

**Refined Abstractions**
```java
class Car extends Vehicle { /* ... */ }
class Bike extends Vehicle { /* ... */ }
```

## Usage

```java
// Runtime flexibility - mix and match any combination
Vehicle electricCar = new Car(new ElectricEngine());
Vehicle dieselBike = new Bike(new DieselEngine());

electricCar.drive();  // Car with electric engine behavior
dieselBike.drive();   // Bike with diesel engine behavior
```

## Benefits

- **Extensibility**: Add new vehicles or engines independently
- **Runtime Flexibility**: Change implementation dynamically
- **Maintainability**: Reduces class proliferation
- **Single Responsibility**: Each class has one reason to change
- **Open/Closed Principle**: Open for extension, closed for modification

## When to Use

Apply the Bridge pattern when:

- You have multiple dimensions that vary independently
- You want to avoid permanent binding between abstraction and implementation
- You need to switch implementations at runtime
- Changes in implementation should not affect client code
- You want to share implementation among multiple objects

## Class Diagram

```
┌─────────────┐          ┌──────────────┐
│   Vehicle   │◆───────▷│    Engine    │
├─────────────┤          ├──────────────┤
│ - engine    │          │ + start()    │
│ + drive()   │          │ + stop()     │
│ + park()    │          │ + accelerate()│
└─────────────┘          └──────────────┘
       △                        △
       │                        │
   ┌───┴───┐           ┌────────┴──────┐
   │       │           │               │
┌──┴──┐ ┌──┴──┐   ┌────┴─────┐ ┌───────┴──┐
│ Car │ │Bike │   │Electric  │ │ Diesel   │
└─────┘ └─────┘   │Engine    │ │Engine    │
                  └──────────┘ └──────────┘
```

## Output

```
Starting Car
Electric Engine Starting
Electric Engine Accelerating
Car moving at constant speed
------------------------------------
Stopping Car
Electric Engine Stopping
Car Parked
------------------------------------
Starting Bike
Diesel Engine Starting
Diesel Engine Accelerating
Bike moving at constant speed
------------------------------------
Stopping Bike
Diesel Engine Stopping
Bike Parked
```

## Conclusion

The Bridge pattern provides a robust solution for managing complexity when dealing with orthogonal hierarchies. It promotes loose coupling, enhances testability, and provides the flexibility to evolve both abstractions and implementations independently.