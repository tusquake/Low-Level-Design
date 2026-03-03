# 📌 Cache Aside Pattern

## 1️⃣ Definition (Interview Ready)
The **Cache Aside Pattern** (also known as Lazy Loading) is a caching strategy where the application is responsible for managing the cache and the data store. The application code first checks the cache for data; if it's not there, it fetches it from the database, stores it in the cache, and then returns it.

- **Operation**: 
  1. Check Cache. 
  2. If Miss: Load from DB -> Write to Cache.
  3. If Hit: Return cached data.
- **Problem it solves**: Improves read performance and reduces load on the primary database by serving subsequent requests from memory.

---

## 2️⃣ Real-World Analogy
Think of a **Student studying for an exam**.
- **The Desk (Cache)**: Where you keep the 2-3 books you are currently reading. Access is instant.
- **The Bookshelf (Database)**: Where all your 100+ books are kept. It takes time to stand up, find the book, and bring it to the desk.

When you need a fact:
1. You look at your **Desk** first.
2. If it's not there, you go to the **Bookshelf**, find the book, bring it to your **Desk**, and keep it there so you don't have to walk back next time.

---

## 3️⃣ When to Use (Practical Scenarios)
- **Read-Heavy Workloads**: Applications like News portals, Social media profiles, or Product catalogs where data is read much more often than it's updated.
- **General Purpose Caching**: The most common strategy for Redis or Memcached in web applications.
- **Resilience**: If the cache fails, the application can still function by falling back to the database (though performance will drop).

---

## 4️⃣ When NOT to Use
- **Frequently Changing Data**: If data changes every few seconds, the cache will be constantly invalidated, making it useless or even harmful.
- **Write-Intensive Apps**: If you write more than you read, managing cache consistency adds overhead without much benefit.
- **Data that must be 100% Real-time**: If your application cannot tolerate even a few seconds of stale data.

---

## 5️⃣ Structure Diagram (Textual UML)
```text
      [ Application ]
        /        \
(1) Check     (2) Fallback to DB
    |              |
    v              v
 [ Cache ] <--- [ Database ]
           (3) Update Cache
```

---

## 6️⃣ Complete Real Java Code Example
### Service Implementation
```java
public class UserService {
    private Cache cache;
    private Database db;

    public User getUser(int userId) {
        // Step 1: Try reading from cache
        User user = cache.get(userId);
        
        if (user != null) {
            System.out.println("Cache Hit!");
            return user;
        }

        // Step 2: Cache Miss - read from DB
        System.out.println("Cache Miss! Fetching from DB...");
        user = db.findById(userId);

        // Step 3: Populate cache for next time
        if (user != null) {
            cache.put(userId, user);
        }

        return user;
    }

    public void updateUser(User user) {
        // Update the primary source
        db.save(user);
        
        // Invalidate the cache to prevent stale data
        cache.evict(user.getId());
    }
}
```

---

## 7️⃣ How It Is Used in Spring Boot / Real Projects
In Spring Boot, this is primarily handled using the **`@Cacheable`** and **`@CacheEvict`** annotations.

### Spring Example
```java
@Service
public class ProductService {

    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        // This code only runs on a Cache Miss
        return productRepository.findById(id).orElseThrow();
    }

    @CacheEvict(value = "products", key = "#product.id")
    public void updateProduct(Product product) {
        productRepository.save(product);
    }
}
```

---

## 8️⃣ Interview Questions
### Basic
1. What is the Cache Aside pattern?
2. What happens during a "Cache Miss"?
3. What is the difference between Cache Aside and Read-Through caching?

### Intermediate
1. How do you handle data consistency in Cache Aside? (Answer: Invalidate or Update the cache when the DB is updated).
2. What is a "Cache Stampede" or "Thundering Herd" problem?
3. What is a TTL (Time To Live)?

### Advanced (Scenario-based)
1. If the database update succeeds but the cache eviction fails, how do you handle the inconsistency? (Answer: Use a retry queue, or set a short TTL as a safety net).
2. How would you choose between "Invalidating" the cache vs "Updating" the cache on a write?

### Trick Question
- **Q**: Does Cache Aside require the cache to be always synchronized with the DB?
- **A**: **No.** It is "Lazy". The cache is only updated when a read occurs. This is why we use TTLs or manual eviction to prevent stale data.

---

## 9️⃣ Common Interview Follow-Up Questions
- **Eviction Policies**: LRU (Least Recently Used), LFU, FIFO.
- **Cold Starts**: How to "warm up" the cache before a high-traffic event (like a flash sale).
- **Redis vs. Memcached**: When to use which for this pattern.

---

## 🔟 Pros and Cons
### Pros
- ✅ **Resilience**: Cache failure doesn't crash the app.
- ✅ **Efficiency**: Only requested data is cached (saves memory).
- ✅ **Flexibility**: Works with different types of data stores and caches.

### Cons
- ❌ **Stale Data**: High risk if invalidation logic is missing.
- ❌ **Lazy Loading Delay**: The very first request for an item is always slow (Cache Miss).
- ❌ **Implementation Overhead**: Application code must handle two data sources.

---

## 1️⃣1️⃣ One-Line Revision Summary
Cache Aside is a "Lazy" caching strategy where the app first checks the cache, falls back to the DB on a miss, and updates the cache for future requests.
