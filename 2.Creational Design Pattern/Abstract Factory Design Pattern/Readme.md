# Meal System - Abstract Factory Design Pattern

A simple demonstration of the Abstract Factory Design Pattern in Java for creating families of related food and drink items.

## Table of Contents
- [Overview](#overview)
- [Design Pattern](#design-pattern)
- [Problem Statement](#problem-statement)
- [Solution](#solution)
- [Project Structure](#project-structure)
- [Class Diagram](#class-diagram)
- [Implementation Details](#implementation-details)
- [How to Run](#how-to-run)
- [Example Output](#example-output)

## Overview

This project demonstrates the Abstract Factory Design Pattern through a meal system that creates different types of meals (Veg and Non-Veg). Each meal consists of a Pizza, Fries, Drink, and Oregano. The abstract factory handles the creation of these related objects, ensuring they belong to the same "theme" (e.g., a Veg Pizza with Veg Fries and Coke).

## Design Pattern

**Pattern Type:** Creational Design Pattern

**Intent:** Provide an interface for creating families of related or dependent objects without specifying their concrete classes.

## Problem Statement

A restaurant system needs to create different sets of meals. A Veg Meal should consist of Veg Pizza, Veg Fries, Coke, and Regular Oregano, while a Non-Veg Meal should consist of Non-Veg Pizza, Non-Veg Fries, Pepsi, and Spicy Oregano.

**Challenges:**
- The system should be independent of how its products are created, composed, and represented.
- The system should be configured with one of multiple families of products.
- A family of related product objects is designed to be used together, and you need to enforce this constraint.

## Solution

The Abstract Factory Pattern provides:

- **Abstract Factory (MealFactory)**: Interface for operations that create abstract product objects.
- **Concrete Factory (VegMealFactory, NonVegMealFactory)**: Implements the operations to create concrete product objects.
- **Abstract Products (Pizza, Fries, Drink, Oregano)**: Interfaces for a type of product object.
- **Concrete Products**: Implement the Product interfaces (e.g., VegPizza, NonVegPizza).
- **Client**: Uses only interfaces declared by Abstract Factory and Abstract Product classes.

## Project Structure

```
src/
└── AbstractFactoryDesignDemo.java  # Contains all interfaces and implementations
```

## Class Diagram

```mermaid
classDiagram
    class MealFactory {
        <<interface>>
        +createPizza() Pizza
        +createFries() Fries
        +createDrink() Drink
        +createOregano() Oregano
    }

    class VegMealFactory {
        +createPizza() Pizza
        +createFries() Fries
        +createDrink() Drink
        +createOregano() Oregano
    }

    class NonVegMealFactory {
        +createPizza() Pizza
        +createFries() Fries
        +createDrink() Drink
        +createOregano() Oregano
    }

    class Pizza {
        <<interface>>
        +prepare()
    }

    class Fries {
        <<interface>>
        +serve()
    }

    class Drink {
        <<interface>>
        +pour()
    }

    class Oregano {
        <<interface>>
        +add()
    }

    MealFactory <|.. VegMealFactory
    MealFactory <|.. NonVegMealFactory
    
    VegMealFactory ..> VegPizza : creates
    VegMealFactory ..> VegFries : creates
    VegMealFactory ..> Coke : creates
    VegMealFactory ..> RegularOregano : creates

    NonVegMealFactory ..> NonVegPizza : creates
    NonVegMealFactory ..> NonVegFries : creates
    NonVegMealFactory ..> Pepsi : creates
    NonVegMealFactory ..> SpicyOregano : creates
```

## Implementation Details

### 1. Abstract Products

```java
interface Pizza {
    void prepare();
}

interface Fries {
    void serve();
}

interface Drink {
    void pour();
}

interface Oregano {
    void add();
}
```

### 2. Concrete Products (Veg Family)

```java
class VegPizza implements Pizza {
    public void prepare() {
        System.out.println("Preparing Veg Pizza");
    }
}

class VegFries implements Fries {
    public void serve() {
        System.out.println("Serving Veg Fries");
    }
}

class Coke implements Drink {
    public void pour() {
        System.out.println("Pouring Coke");
    }
}

class RegularOregano implements Oregano {
    public void add() {
        System.out.println("Adding Oregano");
    }
}
```

### 3. Concrete Products (Non-Veg Family)

```java
class NonVegPizza implements Pizza {
    public void prepare() {
        System.out.println("Preparing Chicken Pizza");
    }
}

class NonVegFries implements Fries {
    public void serve() {
        System.out.println("Serving Loaded Chicken Fries");
    }
}

class Pepsi implements Drink {
    public void pour() {
        System.out.println("Pouring Pepsi");
    }
}

class SpicyOregano implements Oregano {
    public void add() {
        System.out.println("Adding Spicy Oregano");
    }
}
```

### 4. Abstract Factory

```java
interface MealFactory {
    Pizza createPizza();
    Fries createFries();
    Drink createDrink();
    Oregano createOregano();
}
```

### 5. Concrete Factories

```java
class VegMealFactory implements MealFactory {
    public Pizza createPizza() { return new VegPizza(); }
    public Fries createFries() { return new VegFries(); }
    public Drink createDrink() { return new Coke(); }
    public Oregano createOregano() { return new RegularOregano(); }
}

class NonVegMealFactory implements MealFactory {
    public Pizza createPizza() { return new NonVegPizza(); }
    public Fries createFries() { return new NonVegFries(); }
    public Drink createDrink() { return new Pepsi(); }
    public Oregano createOregano() { return new SpicyOregano(); }
}
```

## How to Run

### Compilation

```bash
# Navigate to the src directory
cd src

# Compile the Java file
javac AbstractFactoryDesignDemo.java

# Run the demo class
java AbstractFactoryDesignDemo
```

## Example Output

```
Preparing Veg Pizza
Serving Veg Fries
Pouring Coke
Adding Oregano
Preparing Chicken Pizza
Serving Loaded Chicken Fries
Pouring Pepsi
Adding Spicy Oregano
```

## Key Benefits

- **Consistency**: Ensures that products from the same family are used together.
- **Isolation**: Concrete classes are isolated from the client.
- **Easy Product Exchange**: Changing a product family is easy (just use a different factory).
- **Promotes consistency among products**: Products from a family are designed to work together.

## When to Use

- When a system should be independent of how its products are created, composed, and represented.
- When a system should be configured with one of multiple families of products.
- When a family of related product objects is designed to be used together.
- When you want to provide a class library of products, and you want to reveal only their interfaces, not their implementations.
