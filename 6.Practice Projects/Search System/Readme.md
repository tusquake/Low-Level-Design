# Context-Aware Search System

**Design Pattern:** Strategy + Factory + Orchestrator

Build a flexible search system where search behavior changes based on user context.

---

## Problem

Search behavior varies by context:
- **Normal User** → Location-based search
- **Premium User** → Rating-based search
- **Discount Period** → Price-optimized search

**Same API. Different behavior.**

---

## Architecture

```
Client → Orchestrator → Factory → Strategy (Location/Rating/Price)
```

**Memory Hook:**
- **Strategy** → HOW (implements behavior)
- **Factory** → WHICH (selects strategy)
- **Orchestrator** → WHEN & ORDER (controls flow)

---

## Implementation

### 1. Strategy Interface

```java
public interface SearchStrategy {
    void search(String query);
}
```

### 2. Concrete Strategies

```java
public class LocationBasedSearchStrategy implements SearchStrategy {
    @Override
    public void search(String query) {
        System.out.println("Searching nearby results for: " + query);
    }
}

public class RatingBasedSearchStrategy implements SearchStrategy {
    @Override
    public void search(String query) {
        System.out.println("Searching top-rated results for: " + query);
    }
}

public class PriceOptimizedSearchStrategy implements SearchStrategy {
    @Override
    public void search(String query) {
        System.out.println("Searching lowest-price results for: " + query);
    }
}
```

### 3. Factory (Decision Maker)

```java
public class SearchStrategyFactory {
    public static SearchStrategy getStrategy(String userType, boolean discountActive) {
        if (discountActive) {
            return new PriceOptimizedSearchStrategy();
        }
        if ("PREMIUM".equalsIgnoreCase(userType)) {
            return new RatingBasedSearchStrategy();
        }
        return new LocationBasedSearchStrategy();
    }
}
```

### 4. Orchestrator (Workflow Controller)

```java
public class SearchOrchestrator {
    public void handleSearch(String query, String userType, boolean discountActive) {
        // Validation
        if (query == null || query.isEmpty()) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }
        
        // Get strategy
        SearchStrategy strategy = SearchStrategyFactory.getStrategy(userType, discountActive);
        
        // Execute
        strategy.search(query);
        
        // Post-processing
        System.out.println("Search completed successfully");
    }
}
```

### 5. Client

```java
public class SearchController {
    public static void main(String[] args) {
        SearchOrchestrator orchestrator = new SearchOrchestrator();
        
        orchestrator.handleSearch("AC Repair", "PREMIUM", false);
        // Output: Searching top-rated results for: AC Repair
    }
}
```

---

## Benefits

- **Open/Closed**: Add new strategies without modifying existing code
- **Single Responsibility**: Each strategy has one job
- **Testable**: Easy to unit test each component
- **Maintainable**: Clear separation of concerns

---

## Interview Answer

*"We use **Strategy** to isolate different search behaviors, **Factory** to decide which strategy applies at runtime, and an **Orchestrator** to manage validation, execution, and post-processing without coupling to implementations."*

---

## Extensions

### Spring Boot Version

```java
@Service
public class SearchStrategyFactory {
    private final Map<String, SearchStrategy> strategies;
    
    @Autowired
    public SearchStrategyFactory(List<SearchStrategy> strategyList) {
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(
                SearchStrategy::getStrategyName,
                Function.identity()
            ));
    }
}
```

### Map-Based Factory (No if-else)

```java
private static final Map<String, Supplier<SearchStrategy>> STRATEGY_MAP = Map.of(
    "PREMIUM", RatingBasedSearchStrategy::new,
    "DISCOUNT", PriceOptimizedSearchStrategy::new,
    "NORMAL", LocationBasedSearchStrategy::new
);
```

---

## Testing

```java
@Test
public void testPremiumUserGetsRatingStrategy() {
    SearchStrategy strategy = SearchStrategyFactory.getStrategy("PREMIUM", false);
    assertTrue(strategy instanceof RatingBasedSearchStrategy);
}
```

---

## Key Takeaways

| Component | Responsibility |
|-----------|---------------|
| Strategy | Encapsulates behavior |
| Factory | Selects strategy |
| Orchestrator | Controls flow |

**When to use:** Multiple algorithms for same operation, selected at runtime based on context.

**Real-world examples:** Payment gateways, notification systems, pricing engines, fraud detection.