# Notification System - Factory Design Pattern

A simple demonstration of the Factory Design Pattern in Java for creating different types of notifications.

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

This project demonstrates the Factory Design Pattern through a notification system that creates SMS, Email, and Push notifications. The factory handles object creation logic, keeping the client code simple and decoupled.

## Design Pattern

**Pattern Type:** Creational Design Pattern

**Intent:** Define an interface for creating objects, but let subclasses decide which class to instantiate.

## Problem Statement

A notification system needs to send different types of notifications (SMS, Email, Push). Creating these objects directly in client code leads to tight coupling and makes it difficult to add new notification types.

**Challenges:**
- Client code shouldn't know about concrete notification classes
- Object creation logic should be centralized
- Adding new notification types shouldn't require changing client code

## Solution

The Factory Pattern provides:

- **Product Interface (Notification)**: Common interface for all notifications
- **Concrete Products**: SMS, Email, Push notification implementations
- **Factory (NotificationFactory)**: Creates notification objects based on type
- **Client**: Uses factory to get notifications without knowing concrete classes

## Project Structure

```
src/
├── Notification.java          # Product interface
├── SMSNotification.java       # Concrete product
├── EmailNotification.java     # Concrete product
├── PushNotification.java      # Concrete product
├── NotificationFactory.java   # Factory class
└── Main.java                  # Demo application
```

## Class Diagram

```
┌─────────────────┐
│  Notification   │ (Interface)
├─────────────────┤
│ + send(message) │
└─────────────────┘
         ▲
         │ implements
         │
    ┌────┴────┬────────────┬──────────────┐
    │         │            │              │
┌───┴────────────┐  ┌──────┴──────────┐  ┌───┴────────────┐
│SMSNotification │  │EmailNotification│  │PushNotification│
├────────────────┤  ├─────────────────┤  ├────────────────┤
│ + send()       │  │ + send()        │  │ + send()       │
└────────────────┘  └─────────────────┘  └────────────────┘
                             ▲
                             │ creates
                             │
                    ┌────────┴────────────┐
                    │NotificationFactory  │
                    ├─────────────────────┤
                    │+ createNotification()│
                    └─────────────────────┘
```

## Implementation Details

### 1. Product Interface

```java
public interface Notification {
    void send(String message);
}
```

### 2. SMS Notification

```java
public class SMSNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("SMS Notification: " + message);
    }
}
```

### 3. Email Notification

```java
public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Email Notification: " + message);
    }
}
```

### 4. Push Notification

```java
public class PushNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Push Notification: " + message);
    }
}
```

### 5. Notification Factory

```java
public class NotificationFactory {
    
    public Notification createNotification(String type) {
        if (type == null || type.isEmpty()) {
            return null;
        }
        
        switch (type.toUpperCase()) {
            case "SMS":
                return new SMSNotification();
            case "EMAIL":
                return new EmailNotification();
            case "PUSH":
                return new PushNotification();
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }
}
```

### 6. Demo Application

```java
public class Main {
    public static void main(String[] args) {
        NotificationFactory factory = new NotificationFactory();
        
        // Create and send SMS notification
        Notification sms = factory.createNotification("SMS");
        sms.send("Your OTP is 123456");
        
        // Create and send Email notification
        Notification email = factory.createNotification("EMAIL");
        email.send("Welcome to our platform!");
        
        // Create and send Push notification
        Notification push = factory.createNotification("PUSH");
        push.send("You have a new message");
        
        // Try invalid type
        try {
            Notification invalid = factory.createNotification("FAX");
            invalid.send("Test message");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

## How to Run

### Compilation

```bash
# Navigate to the src directory
cd src

# Compile all Java files
javac *.java

# Run the main class
java Main
```

### Using an IDE
1. Open the project in your Java IDE
2. Run the `Main.java` file

## Example Output

```
SMS Notification: Your OTP is 123456
Email Notification: Welcome to our platform!
Push Notification: You have a new message
Error: Unknown notification type: FAX
```

## Key Benefits

- **Encapsulation**: Object creation logic is centralized in one place
- **Loose Coupling**: Client code doesn't depend on concrete classes
- **Easy Extension**: New notification types can be added easily
- **Single Responsibility**: Factory handles creation, classes handle their own logic

## When to Use

- When you don't know the exact types of objects needed beforehand
- When object creation logic is complex
- When you want to centralize object creation
- When you need to decouple object creation from usage