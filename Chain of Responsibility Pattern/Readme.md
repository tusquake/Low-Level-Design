# Chain of Responsibility Design Pattern Overview

The Chain of Responsibility pattern is a behavioral design pattern that allows an object to pass a request along a chain of potential handlers until one of them handles the request. Each handler decides either to process the request or to pass it to the next handler in the chain.

## Problem Statement

In traditional approaches, when multiple objects can handle a request, the client needs to know which specific object to call. This creates tight coupling between the client and handlers, making the system rigid and difficult to modify.

### Without Chain of Responsibility Pattern

Consider a support ticket system where different severity levels require different handlers:

```
if (ticket.severity == LOW) {
    level1Support.handle(ticket);
} else if (ticket.severity == MEDIUM) {
    level2Support.handle(ticket);
} else if (ticket.severity == HIGH) {
    level3Support.handle(ticket);
}
```

**Issues:**
- Client must know all handlers and their capabilities
- Adding new handler requires modifying client code
- Handler order is hardcoded
- Tight coupling between client and handlers

## Solution

The Chain of Responsibility pattern creates a chain of handler objects. Each handler contains a reference to the next handler. When a request comes in, each handler decides whether to process it or pass it along the chain.

## Architecture

```
Request → Handler1 → Handler2 → Handler3 → ... → HandlerN
          (Check)    (Check)    (Check)         (Check)
             ↓          ↓          ↓               ↓
          Process    Process    Process         Process
             or         or         or              or
          Pass       Pass       Pass            Pass
```

## Implementation

### Core Components

**Handler Interface**
```java
interface SupportHandler {
    void setNext(SupportHandler next);
    void handleRequest(Ticket ticket);
}
```

**Abstract Handler (Optional Base Class)**
```java
abstract class BaseSupportHandler implements SupportHandler {
    protected SupportHandler nextHandler;
    
    @Override
    public void setNext(SupportHandler next) {
        this.nextHandler = next;
    }
    
    protected void passToNext(Ticket ticket) {
        if (nextHandler != null) {
            nextHandler.handleRequest(ticket);
        } else {
            System.out.println("No handler available for this request");
        }
    }
}
```

**Concrete Handlers**
```java
class Level1Support extends BaseSupportHandler {
    @Override
    public void handleRequest(Ticket ticket) {
        if (ticket.getSeverity() == Severity.LOW) {
            System.out.println("Level 1: Handling low severity ticket");
            // Process ticket
        } else {
            System.out.println("Level 1: Passing to next handler");
            passToNext(ticket);
        }
    }
}

class Level2Support extends BaseSupportHandler {
    @Override
    public void handleRequest(Ticket ticket) {
        if (ticket.getSeverity() == Severity.MEDIUM) {
            System.out.println("Level 2: Handling medium severity ticket");
            // Process ticket
        } else {
            System.out.println("Level 2: Passing to next handler");
            passToNext(ticket);
        }
    }
}

class Level3Support extends BaseSupportHandler {
    @Override
    public void handleRequest(Ticket ticket) {
        if (ticket.getSeverity() == Severity.HIGH) {
            System.out.println("Level 3: Handling high severity ticket");
            // Process ticket
        } else {
            System.out.println("Level 3: Cannot handle this ticket");
            passToNext(ticket);
        }
    }
}
```

**Ticket Class**
```java
enum Severity { LOW, MEDIUM, HIGH, CRITICAL }

class Ticket {
    private String description;
    private Severity severity;
    
    public Ticket(String description, Severity severity) {
        this.description = description;
        this.severity = severity;
    }
    
    public Severity getSeverity() {
        return severity;
    }
    
    public String getDescription() {
        return description;
    }
}
```

### Usage

```java
// Build the chain
SupportHandler level1 = new Level1Support();
SupportHandler level2 = new Level2Support();
SupportHandler level3 = new Level3Support();

level1.setNext(level2);
level2.setNext(level3);

// Client sends requests to the chain
Ticket ticket1 = new Ticket("Password reset", Severity.LOW);
Ticket ticket2 = new Ticket("Application crash", Severity.MEDIUM);
Ticket ticket3 = new Ticket("Server outage", Severity.HIGH);

level1.handleRequest(ticket1);
level1.handleRequest(ticket2);
level1.handleRequest(ticket3);
```

## Benefits

1. **Decoupling**: Client doesn't need to know which handler will process the request
2. **Flexibility**: Easy to add or remove handlers dynamically
3. **Single Responsibility**: Each handler focuses on specific request types
4. **Runtime Configuration**: Chain can be modified at runtime
5. **Open/Closed Principle**: Add new handlers without modifying existing code

## Drawbacks

1. **No Guarantee**: Request might not be handled if no suitable handler exists
2. **Performance**: May traverse entire chain before finding handler
3. **Debugging**: Can be harder to trace execution flow

## When to Use

Apply the Chain of Responsibility pattern when:

- Multiple objects can handle a request, but the handler isn't known in advance
- You want to issue a request to one of several objects without specifying the receiver explicitly
- The set of handlers should be specified dynamically
- You want to avoid coupling the sender to the receiver
- Processing order matters and should be enforced

## Real-World Examples

1. **Event Bubbling** in UI frameworks (DOM event propagation)
2. **Logging Frameworks** (console → file → remote server)
3. **Middleware Chains** in web frameworks (authentication → validation → processing)
4. **Exception Handling** (try-catch blocks)
5. **Approval Workflows** (manager → director → VP → CEO)

## Class Diagram

```
┌─────────────────┐
│  SupportHandler │◁───────────┐
├─────────────────┤             │
│ + setNext()     │             │
│ + handleRequest()│            │
└─────────────────┘             │
        △                       │
        │                       │
  ┌─────┴─────┐                │
  │           │                │
┌─┴──────┐  ┌─┴──────┐   ┌────┴─────┐
│Level1  │  │Level2  │   │ Level3   │
│Support │  │Support │   │ Support  │
└────────┘  └────────┘   └──────────┘
```

## Output Example

```
Processing Ticket: Password reset
Level 1: Handling low severity ticket
--------------------------------
Processing Ticket: Application crash
Level 1: Passing to next handler
Level 2: Handling medium severity ticket
--------------------------------
Processing Ticket: Server outage
Level 1: Passing to next handler
Level 2: Passing to next handler
Level 3: Handling high severity ticket
--------------------------------
Processing Ticket: Data center fire
Level 1: Passing to next handler
Level 2: Passing to next handler
Level 3: Cannot handle this ticket
No handler available for this request
```

## Variations

### Pure vs Impure Chain

**Pure Chain**: Each handler either processes the request OR passes it (not both)

**Impure Chain**: Handlers can process the request AND pass it to the next handler (like middleware)

### Early Termination

Handlers can stop the chain by not calling the next handler once a request is processed.

## Conclusion

The Chain of Responsibility pattern provides an elegant solution for handling requests when multiple objects might process them. It promotes loose coupling, enhances flexibility, and makes it easy to add new handlers without modifying existing code. This pattern is particularly useful in scenarios where the processing order matters and handlers need to be configured dynamically.