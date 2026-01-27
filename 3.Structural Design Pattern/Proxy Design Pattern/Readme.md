# Proxy Design Pattern - Complete Guide

## Table of Contents
- [Overview](#overview)
- [What is the Proxy Pattern?](#what-is-the-proxy-pattern)
- [When to Use](#when-to-use)
- [Types of Proxies](#types-of-proxies)
- [Structure](#structure)
- [Implementation Examples](#implementation-examples)
- [Pros and Cons](#pros-and-cons)
- [Real-World Use Cases](#real-world-use-cases)
- [Best Practices](#best-practices)

---

## Overview

The Proxy Design Pattern is a structural design pattern that provides a surrogate or placeholder for another object to control access to it. Think of it as a middleman that stands between the client and the real object, adding extra functionality without modifying the original object.

**Category**: Structural Design Pattern  
**Difficulty**: Intermediate  
**Also Known As**: Surrogate

---

## What is the Proxy Pattern?

A proxy acts as an intermediary that controls access to the real object. It implements the same interface as the real object, making it transparent to clients. The proxy can add additional behavior before or after forwarding requests to the real object.

### Simple Analogy
Imagine a receptionist at a doctor's office:
- You (client) don't directly walk into the doctor's room
- The receptionist (proxy) checks your appointment, manages the schedule, and controls access
- The doctor (real object) focuses only on medical work

---

## When to Use

Use the Proxy Pattern when you need to:

1. **Control access** to an object (protection proxy)
2. **Delay expensive object creation** until it's actually needed (virtual proxy)
3. **Add caching** to improve performance (caching proxy)
4. **Log or monitor** operations on an object (logging proxy)
5. **Represent objects** in different address spaces (remote proxy)
6. **Manage object lifecycle** and resources (smart proxy)

---

## Types of Proxies

### 1. Virtual Proxy (Lazy Loading)
Controls when expensive objects are created and initialized.

**Real-World Example**: YouTube thumbnails load instantly, but videos only load when you click play.

**Use Case**: Large images, heavy documents, or resource-intensive objects.

```java
// Object is created only when needed
VideoProxy proxy = new VideoProxy("large_video.mp4");
// ... later when actually needed
proxy.play(); // Now the real video loads
```

### 2. Protection Proxy (Access Control)
Controls access based on permissions or credentials.

**Real-World Example**: Bank account - only the owner can withdraw money.

**Use Case**: Implementing authentication, authorization, and access control.

```java
BankAccount account = new ProtectedBankAccount();
account.withdraw(100, "Alice");  // Allowed - owner
account.withdraw(100, "Hacker"); // Denied - not owner
```

### 3. Caching Proxy (Performance Optimization)
Stores results of expensive operations and returns cached results when possible.

**Real-World Example**: Google search caches popular queries for instant results.

**Use Case**: Database queries, API calls, expensive computations.

```java
Database db = new CachingDatabaseProxy();
db.query("Java tutorials"); // Fetches from database
db.query("Java tutorials"); // Returns from cache - instant!
```

### 4. Logging Proxy (Audit Trail)
Records all operations performed on an object for monitoring and debugging.

**Real-World Example**: ATM security cameras recording every transaction.

**Use Case**: Debugging, monitoring, compliance, audit trails.

```java
ATM atm = new LoggingATM();
atm.withdrawCash(200); // Logs: timestamp, operation, result
```

### 5. Remote Proxy (Location Transparency)
Represents objects that exist in different address spaces (different machines, processes).

**Real-World Example**: TV remote control - controls a TV in another room.

**Use Case**: Distributed systems, microservices, RPC (Remote Procedure Call).

```java
SmartTV remote = new RemoteTVControl();
remote.turnOn(); // Sends signal to actual TV in different location
```

### 6. Smart Proxy (Reference Counting)
Adds additional housekeeping operations like reference counting, object lifecycle management.

**Real-World Example**: Shared Uber ride - tracks number of passengers, cleans up when empty.

**Use Case**: Memory management, resource cleanup, shared resources.

```java
SmartDocumentProxy doc = new SmartDocumentProxy("file.pdf");
doc.open();  // Increments counter, loads if first user
doc.close(); // Decrements counter, cleans up if last user
```

---

## 🏗️ Structure

```
┌─────────────┐
│   Client    │
└──────┬──────┘
       │
       │ uses
       ↓
┌─────────────┐
│  <<interface>>│
│   Subject   │
└──────┬──────┘
       │
       │ implements
       ├────────────────────┐
       ↓                    ↓
┌─────────────┐      ┌─────────────┐
│    Proxy    │─────→│ RealSubject │
└─────────────┘      └─────────────┘
   (controls)          (real work)
```

### Components

1. **Subject (Interface)**: Common interface for RealSubject and Proxy
2. **RealSubject**: The actual object that does the real work
3. **Proxy**: Maintains a reference to RealSubject and controls access to it
4. **Client**: Works with objects through the Subject interface

---

## Implementation Examples

### Basic Implementation Pattern

```java
// Step 1: Define the interface
interface Subject {
    void operation();
}

// Step 2: Implement the real subject
class RealSubject implements Subject {
    @Override
    public void operation() {
        System.out.println("RealSubject: Performing actual operation");
    }
}

// Step 3: Implement the proxy
class Proxy implements Subject {
    private RealSubject realSubject;
    
    @Override
    public void operation() {
        // Add pre-processing
        if (realSubject == null) {
            realSubject = new RealSubject();
        }
        
        // Delegate to real subject
        realSubject.operation();
        
        // Add post-processing
    }
}

// Step 4: Client usage
public class Client {
    public static void main(String[] args) {
        Subject subject = new Proxy();
        subject.operation();
    }
}
```

### Advanced Example: Image Loader with Virtual Proxy

```java
interface Image {
    void display();
}

class HighResImage implements Image {
    private String filename;
    
    public HighResImage(String filename) {
        this.filename = filename;
        loadFromDisk(); // Expensive operation
    }
    
    private void loadFromDisk() {
        System.out.println("Loading " + filename + " from disk...");
    }
    
    @Override
    public void display() {
        System.out.println("Displaying " + filename);
    }
}

class ImageProxy implements Image {
    private HighResImage realImage;
    private String filename;
    
    public ImageProxy(String filename) {
        this.filename = filename;
    }
    
    @Override
    public void display() {
        if (realImage == null) {
            realImage = new HighResImage(filename); // Lazy loading
        }
        realImage.display();
    }
}
```

---

## Pros and Cons

### Advantages

✔️ **Open/Closed Principle**: Add functionality without changing existing code  
✔️ **Single Responsibility**: Proxy handles cross-cutting concerns separately  
✔️ **Performance**: Can improve performance through caching and lazy loading  
✔️ **Security**: Centralized access control  
✔️ **Transparency**: Client doesn't know it's working with a proxy  
✔️ **Flexibility**: Can swap proxies without affecting clients

### Disadvantages

❌ **Complexity**: Additional layer of abstraction  
❌ **Response Time**: May introduce latency (except for caching proxy)  
❌ **Code Duplication**: Proxy and real object must implement same interface  
❌ **Maintenance**: Need to keep proxy and real object in sync

---

## Real-World Use Cases

### Industry Applications

1. **Hibernate/JPA** - Lazy loading of database entities
2. **Spring Framework** - AOP (Aspect-Oriented Programming) proxies
3. **Java RMI** - Remote method invocation
4. **Virtual Machines** - Virtualization layers
5. **CDN Services** - Content delivery networks cache resources
6. **API Gateways** - Rate limiting, authentication, logging
7. **Image Libraries** - Progressive image loading
8. **ORM Tools** - Database connection pooling

### Specific Examples

**Netflix**: Uses caching proxies to serve popular content from edge servers

**AWS CloudFront**: Acts as a caching proxy for content distribution

**Nginx**: Reverse proxy server for load balancing and caching

**Java Dynamic Proxies**: Used extensively in frameworks for AOP and interceptors

---

## Best Practices

### Do's

✅ Use Virtual Proxy for expensive objects that might not be needed  
✅ Use Protection Proxy when you need fine-grained access control  
✅ Cache frequently accessed, rarely changed data  
✅ Keep proxy interface identical to real object  
✅ Consider thread safety for shared proxies  
✅ Log important operations for debugging and monitoring

### Don'ts

❌ Don't add business logic to proxies - keep them focused on their specific concern  
❌ Don't overuse - simple direct access is sometimes better  
❌ Don't forget to handle errors in the proxy layer  
❌ Don't create deep proxy chains - keeps things simple  
❌ Don't ignore performance implications of the proxy layer

---