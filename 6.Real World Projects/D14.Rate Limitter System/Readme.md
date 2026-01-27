# Rate Limiter - Low Level Design

## Overview
A rate limiter controls the number of requests a client can make within a specific time window. This implementation uses the Strategy Pattern to support multiple rate limiting algorithms, with Factory Pattern for object creation.

## System Architecture

```
Client
  ↓
RateLimiter
  ↓
RateLimitStrategy (interface)
  ├── TokenBucketStrategy
  ├── LeakyBucketStrategy
  ├── FixedWindowStrategy
  └── SlidingWindowLogStrategy
```

## Rate Limiting Algorithms

### 1. Token Bucket
**Description**: Tokens are added to a bucket at a fixed rate. Each request consumes one token.

**Characteristics**:
- Allows burst traffic up to bucket capacity
- Tokens refill at constant rate
- Most flexible algorithm

**Best for**: APIs that need to handle occasional traffic bursts

**Time Complexity**: O(1) per request

**Pros**:
- Handles burst traffic smoothly
- Memory efficient

**Cons**:
- More complex implementation
- Requires tracking refill time

### 2. Leaky Bucket
**Description**: Requests are processed at a fixed rate regardless of incoming traffic.

**Characteristics**:
- Processes requests at constant rate
- Excess requests are queued or rejected
- Smooths out traffic spikes

**Best for**: Systems requiring consistent output rate

**Time Complexity**: O(1) per request

**Pros**:
- Ensures steady request processing
- Protects downstream systems

**Cons**:
- Does not handle burst traffic well
- May drop requests during spikes

### 3. Fixed Window
**Description**: Counter resets at fixed time intervals.

**Characteristics**:
- Simple counter per time window
- Resets completely at window boundary
- Easy to implement

**Best for**: Simple rate limiting requirements

**Time Complexity**: O(1) per request

**Pros**:
- Simple implementation
- Low memory usage

**Cons**:
- Boundary issue: 2x requests possible at window edges
- Not suitable for strict rate limiting

### 4. Sliding Window Log
**Description**: Maintains timestamps of all requests within the window.

**Characteristics**:
- Stores request timestamps
- Removes old timestamps on each request
- Most accurate algorithm

**Best for**: Strict rate limiting requirements

**Time Complexity**: O(N) per request, where N is window size

**Pros**:
- No boundary issues
- Very accurate

**Cons**:
- High memory usage
- Slower for large windows

## Design Patterns Used

### Strategy Pattern
- Allows switching between different rate limiting algorithms
- Each algorithm implements the `RateLimitStrategy` interface
- Enables runtime algorithm selection

### Factory Pattern
- Centralizes object creation logic
- `RateLimiterFactory` creates appropriate strategy instances
- Makes code maintainable and extensible

## SOLID Principles

**Single Responsibility Principle (SRP)**:
- Each strategy handles only one algorithm
- RateLimiter handles only delegation
- Factory handles only object creation

**Open/Closed Principle (OCP)**:
- Open for extension: Add new strategies without modifying existing code
- Closed for modification: Existing strategies remain unchanged

**Dependency Inversion Principle (DIP)**:
- RateLimiter depends on abstraction (RateLimitStrategy interface)
- Not dependent on concrete implementations

## Core Components

### RateLimitStrategy Interface
```java
public interface RateLimitStrategy {
    boolean allowRequest(String userId);
}
```

### RateLimiter
Client-facing class that delegates to strategy
```java
public class RateLimiter {
    private final RateLimitStrategy strategy;
    
    public RateLimiter(RateLimitStrategy strategy) {
        this.strategy = strategy;
    }
    
    public boolean isAllowed(String userId) {
        return strategy.allowRequest(userId);
    }
}
```

### RateLimiterFactory
Creates strategy instances based on type
```java
public class RateLimiterFactory {
    public static RateLimitStrategy getStrategy(String type) {
        // Returns appropriate strategy instance
    }
}
```

## Concurrency Handling

### Thread Safety Mechanisms
- **AtomicLong / AtomicInteger**: Lock-free atomic operations for counters
- **ConcurrentHashMap**: Thread-safe per-user storage
- **Synchronized blocks**: Used only where necessary (e.g., window updates)

### Why Atomic Operations?
- Non-blocking
- Better performance than locks
- Ensures counter accuracy in multi-threaded environment

## Distributed System Considerations

### Single Node vs Distributed

**Single Node**:
- In-memory counters (AtomicInteger, ConcurrentHashMap)
- Works only for single JVM/server
- Fast and simple

**Distributed**:
- Centralized storage required (Redis)
- Use Redis atomic operations: INCR, EXPIRE
- Lua scripts for complex operations
- Ensures consistency across multiple servers

### Redis Implementation Strategy
```
1. Use Redis counters with TTL
2. Atomic operations: INCR, DECR, EXPIRE
3. Lua scripts for multi-step operations
4. Key format: "ratelimit:{userId}:{algorithm}"
```

### Preventing Redis Bottleneck
- Local cache with periodic sync
- Lua scripts to reduce round trips
- Key sharding across Redis cluster
- Connection pooling

## Rate Limiting Scope Options

### By User ID
- Per-user request limits
- Requires authentication

### By IP Address
- Anonymous rate limiting
- Can be bypassed with proxies

### By API Key
- Service-to-service rate limiting
- More secure than IP-based

### By Session
- Temporary rate limits
- Useful for temporary restrictions

## Configuration Parameters

### Token Bucket
- **capacity**: Maximum tokens in bucket
- **refillRatePerSecond**: Tokens added per second

### Leaky Bucket
- **capacity**: Queue size
- **leakRatePerSecond**: Requests processed per second

### Fixed Window
- **limit**: Maximum requests per window
- **windowSizeMillis**: Time window in milliseconds

### Sliding Window Log
- **limit**: Maximum requests per window
- **windowSizeMillis**: Time window in milliseconds

## Usage Example

```java
// Create strategy using factory
RateLimitStrategy strategy = RateLimiterFactory.getStrategy("TOKEN_BUCKET");

// Initialize rate limiter
RateLimiter limiter = new RateLimiter(strategy);

// Check if request is allowed
if (limiter.isAllowed("user123")) {
    // Process request
} else {
    // Reject request (HTTP 429 Too Many Requests)
}
```

## Interview Talking Points

### 30-Second Explanation
"A rate limiter restricts the number of requests a client can make in a given time window. I designed it using Strategy Pattern so different algorithms like Token Bucket or Fixed Window can be plugged in. Object creation is handled by a Factory, and in distributed systems counters are stored in Redis for consistency."

### Key Questions to Ask Interviewer
1. Is rate limiting per user, IP, or API key?
2. Should burst traffic be allowed?
3. Is this single node or distributed?
4. What should happen when limit is exceeded - block or throttle?

### Expected Follow-up Questions

**Q: Why Token Bucket over Fixed Window?**
- Allows burst traffic
- Smoother rate control
- Better user experience

**Q: How do you make it non-blocking?**
- Atomic counters instead of locks
- No synchronized blocks on hot path
- Lock-free data structures

**Q: How to prevent Redis being a bottleneck?**
- Local cache with Redis sync
- Lua scripts to reduce network calls
- Sharding keys across Redis cluster

**Q: Which SOLID principles are followed?**
- OCP: New algorithms via Strategy
- SRP: Counting, decision, storage separated
- DIP: Depend on abstractions, not concrete classes

## Error Handling

### Request Rejection
- Return HTTP 429 (Too Many Requests)
- Include Retry-After header
- Log rejected requests for monitoring

### Strategy Failure
- Fallback to default strategy
- Fail open vs fail closed decision
- Circuit breaker pattern for Redis failures

## Testing Strategy

### Unit Tests
- Test each strategy independently
- Verify counter accuracy
- Test boundary conditions

### Concurrency Tests
- Multiple threads accessing same user
- Race condition verification
- Load testing

### Integration Tests
- Redis integration
- Distributed scenario testing
- Failover testing

## Performance Considerations

### Memory Usage
- Token Bucket: O(number of users)
- Leaky Bucket: O(number of users)
- Fixed Window: O(number of users)
- Sliding Window Log: O(number of users × requests per window)

### CPU Usage
- Token Bucket: O(1) per request
- Leaky Bucket: O(1) per request
- Fixed Window: O(1) per request
- Sliding Window Log: O(N) per request

## Monitoring and Metrics

### Key Metrics
- Request acceptance rate
- Request rejection rate
- Average token count
- Window utilization
- Redis latency (distributed setup)

### Alerts
- Sudden spike in rejections
- Redis connection failures
- Strategy failures

## Future Enhancements

1. **Sliding Window Counter**: Hybrid approach combining fixed window and sliding log
2. **Dynamic Rate Limits**: Adjust limits based on system load
3. **Hierarchical Rate Limiting**: Multiple tiers (user, IP, global)
4. **Rate Limit Warming**: Gradual increase for new users
5. **Distributed Tracing**: Track rate limit decisions across services

## References

### Algorithm Selection Guide
- **High burst tolerance needed**: Token Bucket
- **Strict output rate required**: Leaky Bucket
- **Simple implementation needed**: Fixed Window
- **Accurate limiting required**: Sliding Window Log

### Common Use Cases
- API Gateway rate limiting
- DDoS protection
- Resource quota management
- Fair usage enforcement
