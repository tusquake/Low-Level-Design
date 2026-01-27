# Simple Logger System

A thread-safe, asynchronous logging system implementing Strategy and Singleton design patterns.

## Problem Statement

Design a logging system that:
- Supports multiple log levels (INFO, ERROR, WARN)
- Handles concurrent logging from multiple threads
- Processes logs asynchronously without blocking the caller
- Ensures single instance of logger service across the application

## Design Decisions

### 1. **Strategy Pattern**
- `LoggerStrategy` interface defines logging contract
- Separate implementations for each log level (InfoLogger, ErrorLogger, WarnLogger)
- Easy to extend with new log levels without modifying existing code

### 2. **Factory Pattern**
- `LoggerFactory` creates appropriate logger based on log level
- Centralizes object creation logic
- Reduces coupling between client and concrete logger classes

### 3. **Singleton Pattern**
- `LoggerService` uses double-checked locking for thread-safe singleton
- Ensures single executor service manages all logging operations
- Lazy initialization with volatile keyword for memory visibility

### 4. **Asynchronous Processing**
- Fixed thread pool (2 threads) handles log operations
- Non-blocking logging - caller doesn't wait for I/O operations
- Prevents performance degradation in high-throughput scenarios

## Class Diagram

```
┌─────────────┐
│  LogLevel   │ (Enum)
└─────────────┘

┌──────────────────┐
│ LoggerStrategy   │ (Interface)
├──────────────────┤
│ + log(String)    │
└──────────────────┘
         △
         │ implements
    ┌────┴────┬────────┬──────────┐
    │         │        │          │
┌───────┐ ┌───────┐ ┌───────┐    │
│ Info  │ │ Error │ │ Warn  │    │
│Logger │ │Logger │ │Logger │    │
└───────┘ └───────┘ └───────┘    │
                                  │
                           ┌──────────────┐
                           │LoggerFactory │
                           ├──────────────┤
                           │+ getLogger() │
                           └──────────────┘
                                  │
                                  │ uses
                           ┌──────────────┐
                           │LoggerService │
                           ├──────────────┤
                           │- executor    │
                           │+ getInstance()│
                           │+ log()       │
                           └──────────────┘
```

## Usage

```java
// Get singleton instance
LoggerService logger = LoggerService.getInstance();

// Log messages (non-blocking)
logger.log(LogLevel.INFO, "Application started");
logger.log(LogLevel.ERROR, "Null pointer exception");
logger.log(LogLevel.WARN, "Memory usage high");
```

## Key Features

✅ **Thread-Safe**: Double-checked locking singleton  
✅ **Asynchronous**: Non-blocking log operations  
✅ **Extensible**: Easy to add new log levels  
✅ **Decoupled**: Strategy pattern separates concerns  
✅ **Resource Efficient**: Fixed thread pool prevents thread explosion

## Current Limitations & Improvements

### Known Issues
1. **No Graceful Shutdown**: Executor service not properly closed
2. **Missing Timestamps**: Logs don't include time information
3. **No File Support**: Only console output
4. **Hard-coded Thread Pool**: Size (2) is not configurable
5. **No Log Formatting**: Basic message output only
6. **Memory Risk**: Unbounded task queue can cause OOM under high load

### Suggested Enhancements

```java
// 1. Add shutdown hook
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    executor.shutdown();
    executor.awaitTermination(5, TimeUnit.SECONDS);
}));

// 2. Add timestamp formatting
private String formatMessage(String message) {
    return String.format("[%s] %s", 
        LocalDateTime.now().format(formatter), message);
}

// 3. Make thread pool size configurable
private LoggerService(int poolSize) {
    this.executor = Executors.newFixedThreadPool(poolSize);
}

// 4. Add bounded queue to prevent OOM
new ThreadPoolExecutor(2, 2, 0L, TimeUnit.MILLISECONDS,
    new ArrayBlockingQueue<>(1000),
    new ThreadPoolExecutor.CallerRunsPolicy());
```

## Interview Discussion Points

### Why This Design?

**Q: Why separate strategies for each log level?**  
A: Allows different behaviors per level (e.g., ERROR could email admins, INFO just logs). Follows Open/Closed Principle.

**Q: Why use Factory pattern?**  
A: Decouples client from concrete implementations. Makes it easy to add caching or pooling later.

**Q: Why Singleton for LoggerService?**  
A: Centralized control over logging resources. Prevents multiple executor services consuming threads unnecessarily.

**Q: Why asynchronous logging?**  
A: Main thread doesn't block on I/O. Critical for high-performance applications where logging shouldn't slow down business logic.

**Q: Why fixed thread pool instead of cached?**  
A: Prevents unbounded thread creation. In production, we'd tune pool size based on logging throughput.

### Trade-offs

| Decision | Benefit | Cost |
|----------|---------|------|
| Async logging | Better performance | Log ordering not guaranteed |
| Singleton | Resource efficiency | Global state, harder to test |
| Fixed pool | Predictable resources | May bottleneck under high load |
| Strategy pattern | Flexibility | More classes to maintain |

## Complexity Analysis

- **Time Complexity**: O(1) for log operation (just queue submission)
- **Space Complexity**: O(n) where n = queued log messages
- **Thread Safety**: ✅ Achieved via synchronized singleton + thread pool

## Production Readiness Checklist

- [ ] Add graceful shutdown mechanism
- [ ] Implement log rotation for file appenders
- [ ] Add configuration file support
- [ ] Include structured logging (JSON format)
- [ ] Add log level filtering
- [ ] Implement bounded queue with rejection policy
- [ ] Add metrics/monitoring
- [ ] Include unit tests with concurrent scenarios

**Note**: This is a simplified implementation for demonstration. Production systems should use established frameworks like SLF4J + Logback for robust logging.