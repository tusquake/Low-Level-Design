# Idempotency Overview

Idempotency is a design principle that ensures performing the same operation multiple times produces the same result as performing it once. It is foundational to building reliable, fault-tolerant distributed systems and APIs.

## Core Concept

**"Executing the same request more than once has the same effect as executing it exactly once."**

Don't assume a request will arrive exactly once. Design operations so that retries, duplicates, and replays are safe.

## Problem Statement

Networks fail, timeouts occur, and clients retry. Without idempotency, a retry of a successful-but-unacknowledged request causes the operation to execute twice — charging a user twice, sending duplicate emails, or creating duplicate records.

### Bad Example (Violating Idempotency)

```java
// No duplicate detection — every call deducts money
public class PaymentService {

    public void pay(String user, int amount) {
        System.out.println("Deducted " + amount + " from " + user);
        // No guard — retrying this call charges the user again
    }
}

// Caller
service.pay("Tushar", 1000);
service.pay("Tushar", 1000); // duplicate — charged twice!
```

### Good Example (Following Idempotency)

```java
// Request ID tracks whether this operation has already been processed
public class PaymentService {

    private Set<String> processed = new HashSet<>();

    public void pay(String requestId, String user, int amount) {
        if (processed.contains(requestId)) {
            System.out.println("Payment already processed");
            return;
        }
        System.out.println("Deducted " + amount + " from " + user);
        processed.add(requestId);
    }
}

// Caller
service.pay("req123", "Tushar", 1000);
service.pay("req123", "Tushar", 1000); // duplicate — safely ignored
```

## Key Examples

### 1. Idempotency Keys in Payment APIs

**Bad:**
```java
// No deduplication — every POST creates a new charge
@PostMapping("/charge")
public Response charge(@RequestBody ChargeRequest req) {
    paymentGateway.charge(req.getUserId(), req.getAmount());
    return Response.ok();
}
```

**Good:**
```java
// Idempotency key prevents duplicate charges
@PostMapping("/charge")
public Response charge(
        @RequestHeader("Idempotency-Key") String key,
        @RequestBody ChargeRequest req) {

    if (idempotencyStore.exists(key)) {
        return idempotencyStore.getResult(key); // return cached response
    }

    Response result = paymentGateway.charge(req.getUserId(), req.getAmount());
    idempotencyStore.save(key, result);
    return result;
}
```

### 2. HTTP Methods

**Bad:**
```java
// POST used for an operation that should be idempotent
// Every retry creates a new resource
POST /orders/123/cancel
```

**Good:**
```java
// PUT and DELETE are naturally idempotent
// Cancelling an already-cancelled order is a no-op
PUT /orders/123/status  { "status": "cancelled" }

// DELETE is safe to retry
DELETE /orders/123
```

### 3. Database Upserts

**Bad:**
```sql
-- Fails or duplicates if run twice
INSERT INTO subscriptions (user_id, plan)
VALUES (42, 'pro');
```

**Good:**
```sql
-- Safe to run multiple times — same result every time
INSERT INTO subscriptions (user_id, plan)
VALUES (42, 'pro')
ON CONFLICT (user_id) DO UPDATE SET plan = EXCLUDED.plan;
```

### 4. Message Queue Consumers

**Bad:**
```java
// Processing the same message twice sends two emails
public void onMessage(OrderConfirmedEvent event) {
    emailService.sendConfirmation(event.getUserEmail(), event.getOrderId());
}
```

**Good:**
```java
// Check before acting — message queues can deliver more than once
public void onMessage(OrderConfirmedEvent event) {
    if (eventLog.alreadyProcessed(event.getMessageId())) {
        return;
    }
    emailService.sendConfirmation(event.getUserEmail(), event.getOrderId());
    eventLog.markProcessed(event.getMessageId());
}
```

## Benefits

1. **Safe Retries**: Clients can retry failed requests without fear of side effects
2. **Fault Tolerance**: Network timeouts and partial failures don't corrupt state
3. **Simpler Client Logic**: Clients don't need complex retry-once guarantees
4. **Predictable Systems**: Duplicate events from queues, webhooks, or schedules are harmless
5. **Easier Debugging**: Re-running an operation for investigation produces no new side effects

## Common Violations

### 1. Missing Request Identity
```java
// NO: no way to detect a duplicate
public void processOrder(String userId, int amount) {
    createOrder(userId, amount);
    chargeUser(userId, amount);
    sendConfirmation(userId);
}
```

### 2. Retry Without Deduplication
```java
// NO: client retries blindly — server has no guard
for (int i = 0; i < 3; i++) {
    try {
        client.post("/send-email", payload);
        break;
    } catch (TimeoutException e) {
        // Retrying — server may have already succeeded
    }
}
```

### 3. Non-Idempotent State Transitions
```java
// NO: calling cancel twice produces an error instead of a no-op
public void cancelOrder(String orderId) {
    Order order = repo.find(orderId);
    if (order.getStatus() == CANCELLED) {
        throw new IllegalStateException("Already cancelled"); // should be a no-op
    }
    order.setStatus(CANCELLED);
}
```

## When Idempotency is Critical

Implement idempotency explicitly when:

1. **Financial transactions**: Payments, refunds, credits — any money movement
2. **External side effects**: Sending emails, SMS, push notifications
3. **Queue/event consumers**: At-least-once delivery guarantees mean duplicates will arrive
4. **Webhooks**: Providers often retry on non-2xx responses or timeouts
5. **Distributed workflows**: Multi-step processes where any step can be retried

## Idempotency in Practice

### Example: Order Cancellation Service

**Before:**
```java
public class OrderService {
    public void cancel(String orderId) {
        Order order = repo.find(orderId);
        order.setStatus(CANCELLED);        // throws if already cancelled
        refundService.refund(order);       // double refund on retry!
        emailService.sendCancellation();   // duplicate email on retry!
        repo.save(order);
    }
}
```

**After:**
```java
public class OrderService {
    public void cancel(String orderId) {
        Order order = repo.find(orderId);

        if (order.getStatus() == CANCELLED) {
            return; // already done — safe no-op
        }

        order.setStatus(CANCELLED);
        refundService.refundIfNotAlreadyRefunded(order);
        emailService.sendCancellationIfNotSent(order);
        repo.save(order);
    }
}
```

## Idempotency vs At-Most-Once

**Idempotency doesn't mean:**
- Requests are only processed once
- You don't need logging or audit trails
- All operations can be made idempotent for free
- Idempotency keys last forever

**Idempotency means:**
- Processing the same request N times equals processing it once
- The system converges to the same state regardless of retry count
- Side effects are guarded, not suppressed
- Callers can safely retry without custom deduplication logic

## Balancing Idempotency with Good Design

### Good: Guard at the boundary, delegate inside
```java
public void pay(String requestId, String user, int amount) {
    if (processed.contains(requestId)) {
        return; // clean early exit
    }
    processPayment(user, amount);   // internal logic unchanged
    processed.add(requestId);
}
```

### Bad: Scattered deduplication logic
```java
public void pay(String requestId, String user, int amount) {
    processPayment(user, amount);       // side effects happen first
    if (!processed.contains(requestId)) {
        processed.add(requestId);       // guard added as afterthought
    }
}
```

## Red Flags (Idempotency Violations)

- Mutation operations on `POST` endpoints with no idempotency key
- "Deduct", "charge", "send", "create" methods with no duplicate guard
- Retry logic on the client with no deduplication on the server
- State transitions that throw errors instead of treating re-application as a no-op
- Event consumers with no processed-message log
- Distributed workflows with no checkpoint or correlation ID

## Quotes

> "At-least-once delivery is the norm. Idempotency is how you survive it." — Pat Helland

> "Design for failure. Then design your retry strategy. Then make your operations idempotent." — Werner Vogels

> "An idempotent operation is one that has no additional effect if it is called more than once with the same input parameters." — IETF RFC 7231

## Relationship with Other Principles

| Principle | Connection to Idempotency |
|-----------|--------------------------|
| **Law of Demeter** | Both reduce unexpected side effects across boundaries |
| **YAGNI** | Add idempotency where retries can actually happen, not everywhere |
| **SRP** | Deduplication logic belongs at the boundary, not scattered through business logic |
| **CQS** | Commands change state; idempotency ensures repeated commands converge |
| **Fail Fast** | Detect and reject duplicates early before side effects occur |

## Conclusion

Idempotency is not an optimisation — it is a correctness requirement in any system where operations can be retried, replayed, or delivered more than once. Networks are unreliable, clients will retry, and queues will deliver duplicates. Design for that reality from the start.

**Remember: It is not enough for an operation to succeed. It must be safe to run again.**