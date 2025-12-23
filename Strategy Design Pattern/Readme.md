# Payment System - Strategy Design Pattern

A simple demonstration of the Strategy Design Pattern in Java for handling different payment methods.

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

This project demonstrates the Strategy Design Pattern through a payment system that supports UPI, Debit Card, and Credit Card payments. Each payment method is implemented as a separate strategy that can be switched at runtime.

## Design Pattern

**Pattern Type:** Behavioral Design Pattern

**Intent:** Define a family of algorithms, encapsulate each one, and make them interchangeable.

## Problem Statement

A payment system needs to support multiple payment methods (UPI, Debit Card, Credit Card). Each method has different processing logic. The challenge is to implement this without using multiple if-else statements and make it easy to add new payment methods.

**Challenges:**
- Different payment methods have different logic
- Payment method should be selectable at runtime
- Adding new methods shouldn't require changing existing code

## Solution

The Strategy Pattern provides:

- **Strategy Interface (PaymentStrategy)**: Common interface for all payment methods
- **Concrete Strategies**: UPI, Debit Card, Credit Card implementations
- **Context (PaymentProcessor)**: Uses the strategy to process payments
- **Client**: Selects and uses the payment method

## Project Structure

```
src/
├── PaymentStrategy.java       # Strategy interface
├── UPIPayment.java           # UPI implementation
├── DebitCardPayment.java     # Debit Card implementation
├── CreditCardPayment.java    # Credit Card implementation
├── PaymentProcessor.java     # Context class
└── Main.java                 # Demo application
```

## Class Diagram

```
┌──────────────────┐
│ PaymentStrategy  │ (Interface)
├──────────────────┤
│ + pay(amount)    │
└──────────────────┘
         ▲
         │ implements
         │
    ┌────┴────┬────────────┬──────────────┐
    │         │            │              │
┌───┴──────┐  ┌──────┴────────┐  ┌───┴────────────┐
│UPIPayment│  │DebitCardPayment│  │CreditCardPayment│
├──────────┤  ├───────────────┤  ├────────────────┤
│ - upiId  │  │ - cardNumber  │  │ - cardNumber   │
├──────────┤  ├───────────────┤  ├────────────────┤
│+ pay()   │  │ + pay()       │  │ + pay()        │
└──────────┘  └───────────────┘  └────────────────┘
                             ▲
                             │ uses
                             │
                    ┌────────┴────────┐
                    │PaymentProcessor │
                    ├─────────────────┤
                    │ - strategy      │
                    ├─────────────────┤
                    │ + setStrategy() │
                    │ + processPayment()│
                    └─────────────────┘
```

## Implementation Details

### 1. Strategy Interface

```java
public interface PaymentStrategy {
    void pay(double amount);
}
```

### 2. UPI Payment Strategy

```java
public class UPIPayment implements PaymentStrategy {
    private String upiId;
    
    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using UPI ID: " + upiId);
    }
}
```

### 3. Debit Card Payment Strategy

```java
public class DebitCardPayment implements PaymentStrategy {
    private String cardNumber;
    
    public DebitCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + 
            " using Debit Card ending in " + 
            cardNumber.substring(cardNumber.length() - 4));
    }
}
```

### 4. Credit Card Payment Strategy

```java
public class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    
    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + 
            " using Credit Card ending in " + 
            cardNumber.substring(cardNumber.length() - 4));
    }
}
```

### 5. Payment Processor (Context)

```java
public class PaymentProcessor {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    public void processPayment(double amount) {
        if (paymentStrategy == null) {
            System.out.println("Please select a payment method");
            return;
        }
        paymentStrategy.pay(amount);
    }
}
```

### 6. Demo Application

```java
public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        double amount = 1500.00;
        
        // Pay using UPI
        System.out.println("--- Payment using UPI ---");
        processor.setPaymentStrategy(new UPIPayment("user@paytm"));
        processor.processPayment(amount);
        
        // Pay using Debit Card
        System.out.println("\n--- Payment using Debit Card ---");
        processor.setPaymentStrategy(new DebitCardPayment("1234567890123456"));
        processor.processPayment(amount);
        
        // Pay using Credit Card
        System.out.println("\n--- Payment using Credit Card ---");
        processor.setPaymentStrategy(new CreditCardPayment("9876543210987654"));
        processor.processPayment(amount);
    }
}
```

## Example Output

```
--- Payment using UPI ---
Paid ₹1500.0 using UPI ID: user@paytm

--- Payment using Debit Card ---
Paid ₹1500.0 using Debit Card ending in 3456

--- Payment using Credit Card ---
Paid ₹1500.0 using Credit Card ending in 7654
```

## Key Benefits

- **Flexibility**: Easy to switch between payment methods at runtime
- **Extensibility**: New payment methods can be added without changing existing code
- **Clean Code**: Eliminates multiple if-else statements
- **Testability**: Each payment method can be tested independently

## When to Use

- When you have multiple ways to perform an operation
- When you want to avoid multiple conditional statements
- When you need to change behavior at runtime
- When you want to add new behaviors without modifying existing code