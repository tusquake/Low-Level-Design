# In-Memory Cache Implementation

A flexible in-memory cache system with multiple eviction strategies and TTL support.

## Features

- **Multiple Eviction Strategies**: LRU, MRU, LFU, Random
- **TTL Support**: Automatic expiration of cached entries
- **Generic Implementation**: Works with any key-value types
- **Pluggable Design**: Easy to add custom eviction strategies

## Eviction Strategies

### LRU (Least Recently Used)
Evicts the item that hasn't been accessed for the longest time.

**Use Case**: Web browser cache
```
Cache: [Page A, Page B, Page C]
User visits: Page A
Cache becomes: [Page B, Page C, Page A]
New page arrives → Page B gets evicted (least recently used)
```

### MRU (Most Recently Used)
Evicts the item that was accessed most recently.

**Use Case**: Database query cache where recent queries are less likely to repeat
```
Cache: [Query 1, Query 2, Query 3]
User runs: Query 3
New query arrives → Query 3 gets evicted (most recently used)
```

### LFU (Least Frequently Used)
Evicts the item with the lowest access count.

**Use Case**: Video streaming cache
```
Popular video: accessed 1000 times
Regular video: accessed 10 times
Unpopular video: accessed 2 times
→ Unpopular video gets evicted first
```

### Random
Evicts a random item.

**Use Case**: Simple cache where access patterns are unpredictable
```
Cache full → randomly pick any item to evict
Fast, no tracking overhead
```

## Basic Usage

```java
// Create cache with capacity 3, TTL 5 seconds, LRU eviction
Cache<String, String> cache = new InMemoryCache<>(
    3,                              // capacity
    5000,                           // TTL in milliseconds
    new LRUEvictionStrategy<>()     // eviction strategy
);

// Add items
cache.put("A", "Apple");
cache.put("B", "Banana");
cache.put("C", "Cherry");

// Access item (updates LRU order)
cache.get("A");  // Returns "Apple"

// Add 4th item - triggers eviction
cache.put("D", "Date");

// "B" was evicted (least recently used)
cache.get("B");  // Returns null
```

## Real-World Examples

### Example 1: E-commerce Product Cache (LRU)
```java
Cache<String, Product> productCache = new InMemoryCache<>(
    1000,                           // Store 1000 products
    600000,                         // 10 minute TTL
    new LRUEvictionStrategy<>()
);

// Popular products stay in cache
productCache.put("iPhone15", iphoneProduct);
productCache.put("MacBook", macbookProduct);

// Accessing keeps them fresh
productCache.get("iPhone15");  // Frequently accessed, stays cached

// Rarely viewed products get evicted automatically
productCache.put("ObscureGadget", gadget);  // Will be evicted soon
```

### Example 2: User Session Cache (TTL-based)
```java
Cache<String, UserSession> sessionCache = new InMemoryCache<>(
    10000,                          // 10,000 concurrent users
    1800000,                        // 30 minute session timeout
    new LRUEvictionStrategy<>()
);

// User logs in
sessionCache.put("user123", session);

// Session automatically expires after 30 minutes of inactivity
Thread.sleep(1800001);
sessionCache.get("user123");  // Returns null (expired)
```

### Example 3: API Response Cache (LFU)
```java
Cache<String, String> apiCache = new InMemoryCache<>(
    500,                            // Cache 500 responses
    300000,                         // 5 minute TTL
    new LFUEvictionStrategy<>()
);

// Popular endpoints stay cached
apiCache.put("/api/trending", trendingData);     // Accessed 1000x
apiCache.put("/api/popular", popularData);       // Accessed 500x
apiCache.put("/api/rarely-used", rareData);      // Accessed 2x

// Rarely-used endpoint gets evicted first when cache is full
```

### Example 4: DNS Cache (Random)
```java
Cache<String, String> dnsCache = new InMemoryCache<>(
    1000,                           // Cache 1000 DNS lookups
    3600000,                        // 1 hour TTL
    new RandomEvictionStrategy<>()
);

// Fast eviction without tracking access patterns
dnsCache.put("google.com", "142.250.185.46");
dnsCache.put("github.com", "140.82.121.4");
```

## How It Works

1. **Put Operation**:
    - If cache is full, evict one item using the strategy
    - Add new item to cache
    - Update eviction strategy tracking

2. **Get Operation**:
    - Check if item exists and not expired (TTL)
    - If expired, remove and return null
    - If valid, update access tracking and return value

3. **TTL Expiration**:
    - Each entry has a timestamp
    - On access, check if `current_time - timestamp > TTL`
    - Expired entries are automatically removed

## Performance Characteristics

| Strategy | Get | Put | Space Overhead |
|----------|-----|-----|----------------|
| LRU      | O(1)| O(1)| O(n) - LinkedHashSet |
| MRU      | O(1)| O(1)| O(n) - LinkedList |
| LFU      | O(1)| O(n)| O(n) - HashMap |
| Random   | O(1)| O(1)| O(n) - ArrayList |

## When to Use Each Strategy

**LRU** → Most common, works well for general-purpose caching
- Web pages, images, file systems
- Recently accessed items likely to be accessed again

**MRU** → Specific use cases with sequential access patterns
- Database scans, log file processing
- Most recent item unlikely to be needed again soon

**LFU** → When frequency matters more than recency
- Popular content (videos, articles)
- Predictable access patterns

**Random** → Simplest, when you don't know the pattern
- Uniform access distribution
- Minimal overhead needed

## Limitations

- No persistence (in-memory only)
- No distributed caching
- Single-threaded (no concurrent access protection)
- TTL cleanup is lazy (on access, not proactive)

## Extending the Cache

Add a new eviction strategy by implementing `EvictionStrategy<K>`:

```java
class CustomEvictionStrategy<K> implements EvictionStrategy<K> {
    public K evictKey() {
        // Your eviction logic
    }
    
    public void keyAccessed(K key) {
        // Track access
    }
    
    public void keyAdded(K key) {
        // Track addition
    }
    
    public void keyRemoved(K key) {
        // Clean up tracking
    }
}
```