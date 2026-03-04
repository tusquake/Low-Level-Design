# 📌 Circuit Breaker Pattern

## 1️⃣ Definition (Interview Ready)
The **Circuit Breaker Pattern** is a stability pattern used in distributed systems to prevent a failure in one service from cascading to others. It automatically "trips" or opens when a service is failing, stopping further requests until the service is healthy again.

- **Purpose**: To handle faults that are likely to take a variable amount of time to recover from.
- **The States**: 
  - **CLOSED**: Requests flow normally. Failures are tracked.
  - **OPEN**: Requests fail immediately without calling the backend service.
  - **HALF-OPEN**: A limited number of test requests are allowed to check if the service has recovered.

---

## 2️⃣ Real-World Analogy
Think of an **Electrical Circuit Breaker** in your home.
- If there is a power surge or a short circuit (Service Failure), the breaker **trips (OPEN)** to protect your appliances (The rest of the system).
- You can't just flip it back immediately if the short is still there. You wait, fix the issue, and then try to **reset (HALF-OPEN/CLOSED)** it.

---

## 3️⃣ When to Use (Practical Scenarios)
- **External API Calls**: When calling a 3rd party service (e.g., Payment Gateway, SMS Provider) that is experiencing downtime.
- **Database Overload**: When the DB is slow or unresponsive to prevent exhausting connection pools.
- **Network Latency**: To avoid blocking threads in the calling service when a target service is slow to respond.
- **Microservices Chaining**: To prevent a bottleneck in Service C from crashing Service A through Service B.

---

## 4️⃣ When NOT to Use
- **Local Error Handling**: Don't use it for simple exceptions (like NullPointerException) that are local to your code.
- **Short-lived Faults**: For occasional network glitches, a simple **Retry Pattern** with exponential backoff might be more appropriate.
- **Small Applications**: It adds more architectural overhead than a simple timeout might solve for non-distributed systems.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
[ Client ] 
    |
    v
[ Circuit Breaker Proxy ] --(Failure Threshold reached?)--> [ OPEN ]
    |                                                      (Return Failure)
    +---(Service Healthy?)---> [ CLOSED ] 
                                (Call Service)
```

---

## 6️⃣ Complete Real Java Code Example
### Circuit Breaker States
```java
public enum State { CLOSED, OPEN, HALF_OPEN }
```

### The Circuit Breaker Implementation
```java
public class CircuitBreaker {
    private int failures = 0;
    private final int threshold = 3;
    private State state = State.CLOSED;
    private long lastFailureTime;

    public String callService(Supplier<String> serviceCall) {
        if (state == State.OPEN) {
            if (System.currentTimeMillis() - lastFailureTime > 5000) {
                state = State.HALF_OPEN; // Try again after 5s
            } else {
                return "Fallback: Service Temporarily Unavailable";
            }
        }

        try {
            String result = serviceCall.get();
            reset();
            return result;
        } catch (Exception e) {
            recordFailure();
            return "Fallback: Service Call Failed";
        }
    }

    private void recordFailure() {
        failures++;
        lastFailureTime = System.currentTimeMillis();
        if (failures >= threshold) state = State.OPEN;
    }

    private void reset() {
        failures = 0;
        state = State.CLOSED;
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In modern Java development, **Resilience4j** is the standard library, replacing the older **Netflix Hystrix**.

### Spring Resilience4j Config
```yaml
resilience4j:
  circuitbreaker:
    instances:
      paymentService:
        registerHealthIndicator: true
        slidingWindowSize: 10
        failureRateThreshold: 50
        waitDurationInOpenState: 10000
```
*Key features:* `failureRateThreshold` (trip at 50% failure) and `waitDurationInOpenState` (how long to stay OPEN).

---

## 8️⃣ Interview Questions
### Basic
1. **What are the three states of a Circuit Breaker?**
   - **Answer**: 
     - **CLOSED**: Default state. Requests are allowed.
     - **OPEN**: Failure threshold reached. Requests fail immediately (Fail-Fast).
     - **HALF-OPEN**: Waiting period over. A few requests are allowed to test service health.

2. **How does a Circuit Breaker prevent cascading failures?**
   - **Answer**: By "tripping" the circuit when a downstream service fails, it prevents the upstream service from hanging or exhausting its own resources (like thread pools) while waiting for a response that will never come or will take too long.

3. **What is the difference between an OPEN and CLOSED state?**
   - **Answer**: In the **CLOSED** state, the circuit breaker allows all requests to pass through to the target service. In the **OPEN** state, it intercepts all requests and immediately returns a failure or fallback, without even attempting to call the target service.

### Intermediate
1. **What triggers the transition from OPEN to HALF-OPEN?**
   - **Answer**: A **Timeout**. After the circuit enters the OPEN state, it stays there for a configured duration (e.g., 60 seconds). Once that time passes, it automatically transitions to HALF-OPEN to "test the waters."

2. **How do you decide the failure threshold for a production service?**
   - **Answer**: It depends on the SLA (Service Level Agreement). Common strategies include a **percentage-based threshold** (e.g., trip if >50% of the last 100 requests failed) or a **count-based threshold** (trip after 5 consecutive failures).

3. **What is a "Fallback" mechanism in the context of a Circuit Breaker?**
   - **Answer**: It's the alternative logic that executes when the circuit is OPEN or when a call fails. Examples include returning cached data, a default "stub" response, or a "System busy" message to the user.

### Advanced (Scenario-based)
1. **In a high-traffic system (e.g., 10,000 TPS), how would you configure the Circuit Breaker to avoid tripping on every small network spike?**
   - **Answer**: 
     - Use a **Sliding Window** (Time-based or Count-based) so the decision is based on a representative sample of traffic, not a single failure.
     - Set a **Minimum Number of Calls** before the failure rate is even calculated (to avoid tripping on the very first request failing).
     - Use a **Failure Rate Threshold** rather than a fixed failure count.

2. **How would you handle a "Half-Open" state if 5 out of 10 requests succeed but the other 5 fail?**
   - **Answer**: Most modern libraries (like Resilience4j) allow you to configure this. Usually, if the failure rate in HALF-OPEN stays above the threshold, the circuit transitions back to **OPEN**. If the success rate is high enough, it transitions back to **CLOSED**.

### Trick Question
- **Q**: Does a Circuit Breaker solve the root cause of service failure?
- **A**: **No.** It is a "Safety Fuse." It protects the *rest* of the system from the failure and gives the failing service time to recover or allows the dev team time to investigate. The root cause (e.g., a bug, DB crash) must still be fixed manually or via auto-scaling.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Circuit Breaker vs. Retry**: When to use which?
- **Monitoring**: Importance of dashboards (e.g., Grafana) to see the state of circuits.
- **Bulkhead Pattern**: Often used alongside Circuit Breaker to isolate resources.

---

## 🔟 Pros and Cons
### Pros
- ✅ **System Resilience**: Keeps the overall system alive during partial outages.
- ✅ **Resource Conservation**: Saves threads and memory from being wasted on failing calls.
- ✅ **Immediate Feedback**: Failing fast is better than making the user wait 30 seconds for a timeout.

### Cons
- ❌ **Complexity**: Hard to test all state transitions correctly.
- ❌ **Parameter Tuning**: Setting thresholds too low or too high can cause issues (tripping too often or not enough).

---

## 1️⃣1️⃣ One-Line Revision Summary
Circuit Breaker stops a failing service from bringing down the entire system by failing fast and allowing the failing service time to recover.
