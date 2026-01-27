# Cache Eviction Policies - Low Level Design Document

## Table of Contents
1. [System Overview](#system-overview)
2. [Design Patterns Used](#design-patterns-used)
3. [Core Components](#core-components)
4. [Eviction Strategies](#eviction-strategies)
5. [Implementation Details](#implementation-details)
6. [Performance Analysis](#performance-analysis)

---

## System Overview

A cache is a high-speed data storage layer that stores a subset of data to serve future requests faster. When the cache reaches its capacity, an eviction policy determines which item to remove.

### Key Requirements
- Fixed capacity constraint
- Fast read/write operations (O(1) preferred)
- Pluggable eviction strategies
- Thread-safe operations (future enhancement)

---

## Design Patterns Used

### 1. **Strategy Pattern**
The core design uses the Strategy pattern to make eviction policies interchangeable.

```
Interface: EvictionPolicy
Implementations: LRU, LFU, FIFO, LIFO, MRU, Random, RoundRobin, 2Q
```

**Benefits:**
- Open/Closed Principle: Add new policies without modifying existing code
- Single Responsibility: Each policy handles only its eviction logic
- Easy testing and maintenance

### 2. **Generic Programming**
Uses Java generics for type safety and reusability.

```java
Cache<K, V>  // Works with any key-value type
```

---

## Core Components

### 1. CacheEntry
Metadata wrapper for each cache item.

```
Fields:
- key: The cache key
- value: The cached data
- timestamp: When item was added
- lastAccessTime: Last access timestamp
- frequency: Number of times accessed
```

### 2. EvictionPolicy Interface
Contract that all eviction strategies must implement.

```
Methods:
- onGet(K key): Called when item is accessed
- onPut(K key): Called when item is added
- evict(): Returns key to evict
- remove(K key): Remove key from policy tracking
```

### 3. Cache Class
Main cache implementation that delegates eviction to the policy.

```
Responsibilities:
- Store key-value pairs
- Check capacity before adding
- Delegate eviction decision to policy
- Update metadata on access
```

---

## Eviction Strategies

### 1. LRU (Least Recently Used)

**Strategy:** Evict the item that hasn't been accessed for the longest time.

**Data Structure:** LinkedHashMap with access-order

**Real-World Analogy:**
```
Your desk with 3 spaces for books:
- Math book (not touched in 5 days)
- English book (used yesterday)
- Science book (used today)

When you need History book:
→ Remove Math (least recently used)

Result: [English, Science, History]
```

**When to Use:**
- General-purpose caching
- Web page caching
- Database query results
- Most workloads with temporal locality

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

---

### 2. LFU (Least Frequently Used)

**Strategy:** Evict the item accessed the fewest times.

**Data Structure:** HashMap + TreeMap (frequency → keys)

**Real-World Analogy:**
```
Apps on your phone (5GB limit):
- WhatsApp: opened 500 times
- Calculator: opened 20 times
- Old Game: opened 3 times
- News App: opened 150 times

Storage full, need new app:
→ Delete Old Game (lowest frequency)

Keep frequently used apps!
```

**When to Use:**
- Music/video streaming (popular content)
- CDN caching
- Web analytics
- Workloads where frequency matters more than recency

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

**Challenge:** Solves cache pollution from one-time accesses better than LRU.

---

### 3. FIFO (First In First Out)

**Strategy:** Evict the oldest item regardless of usage.

**Data Structure:** Queue

**Real-World Analogy:**
```
Movie theater queue (3 people max):
Person A (entered 1pm) ─┐
Person B (entered 2pm)  ├─ Queue
Person C (entered 3pm) ─┘

Person D wants to enter at 4pm:
→ Person A leaves (first in, first out)

Queue: [B, C, D]

Doesn't matter if A was VIP!
```

**When to Use:**
- Simple scenarios
- When age matters more than usage
- Predictable eviction needed
- Circular buffers

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

**Limitation:** Doesn't consider access patterns at all.

---

### 4. LIFO (Last In First Out)

**Strategy:** Evict the most recently added item.

**Data Structure:** Stack

**Real-World Analogy:**
```
Stack of plates in your kitchen:
┌─────────┐
│ Plate C │ ← Top (just added)
├─────────┤
│ Plate B │
├─────────┤
│ Plate A │ ← Bottom
└─────────┘

Need space for new plate:
→ Remove Plate C (top/newest)

Stack: [A, B]
```

**When to Use:**
- Rare in practice for caching
- Undo operations
- Expression evaluation
- Depth-first search scenarios

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

---

### 5. MRU (Most Recently Used)

**Strategy:** Evict the item that was just accessed.

**Data Structure:** Stack/Deque

**Real-World Analogy:**
```
Newspaper recycling:
- Monday's paper (old)
- Tuesday's paper
- Wednesday's paper (just read)

Recycling rule: Remove what you just read!
→ Remove Wednesday's paper (most recent)

Keep: [Monday, Tuesday]

Logic: If you just read it, you won't need it again soon.
```

**When to Use:**
- Database index scans
- Sequential file access
- When repeated access is unlikely
- Cyclic patterns

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

---

### 6. Random Replacement

**Strategy:** Randomly select any item to evict.

**Data Structure:** ArrayList

**Real-World Analogy:**
```
Lottery system for parking spots (3 spots):
- Car A (Spot 1)
- Car B (Spot 2)
- Car C (Spot 3)

New car arrives:
→ Draw random number... Car B selected!
→ Car B must leave

Result: [A, C, New Car]

Fair but unpredictable!
```

**When to Use:**
- Simplest implementation
- When no access pattern exists
- Hardware caches
- Fair eviction needed

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

**Benefit:** No overhead for tracking access patterns.

---

### 7. Round Robin

**Strategy:** Evict items in circular sequential order.

**Data Structure:** ArrayList + Pointer

**Real-World Analogy:**
```
Kids taking turns on a swing (3 swings):
Round 1: [Alice, Bob, Charlie]
         ↑ pointer

Alice's turn is up → Alice leaves
Round 2: [Bob, Charlie, New Kid]
         ↑ pointer

Bob's turn is up → Bob leaves
Round 3: [Charlie, New Kid, Another Kid]
         ↑ pointer

Everyone gets equal time!
```

**When to Use:**
- Fair resource allocation
- Load balancing
- Time-sharing systems
- When fairness is critical

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

**Benefit:** Predictable and fair eviction.

---

### 8. 2Q (Two Queue)

**Strategy:** Two-level system with trial period and main storage.

**Data Structures:**
- A1in: FIFO queue (probation)
- Am: LRU queue (main cache)
- A1out: Ghost queue (tracking)

**Real-World Analogy:**
```
Company hiring process:

╔════════════════════════════════════╗
║  A1in (Probation - 3 months)       ║
║  New employees on trial            ║
╚════════════════════════════════════╝
         ↓ (if accessed again)
╔════════════════════════════════════╗
║  Am (Permanent Staff)              ║
║  Proven, frequently needed         ║
╚════════════════════════════════════╝

╔════════════════════════════════════╗
║  A1out (Alumni List)               ║
║  Remember who left during trial    ║
╚════════════════════════════════════╝

Flow:
1. John (new) → A1in (probation)
2. John called again → Am (promoted!)
3. Mike (new) → A1in (probation)
4. Mike never called → Removed, added to A1out
5. Mike needed again → Directly to Am (proven useful!)

Benefit: One-time accesses don't pollute main cache!
```

**When to Use:**
- Database buffer pools
- File system caches
- Workloads with scan patterns
- Protection against cache pollution

**Time Complexity:**
- Get: O(1)
- Put: O(1)
- Evict: O(1)

**Key Advantage:** Better than LRU for workloads with sequential scans.

---

## Implementation Details

### Cache Class Flow

```
put(key, value):
  1. Check if key exists → Update value
  2. Check if cache full → Call policy.evict()
  3. Create CacheEntry
  4. Add to store
  5. Call policy.onPut(key)

get(key):
  1. Check if key exists → Return null
  2. Update lastAccessTime
  3. Increment frequency
  4. Call policy.onGet(key)
  5. Return value
```

### Policy Responsibilities

Each policy must track its own state:
- LRU: Maintains access order
- LFU: Maintains frequency counts
- FIFO: Maintains insertion order
- 2Q: Maintains three separate queues

---

## Performance Analysis

| Policy | Get | Put | Evict | Space | Best Use Case |
|--------|-----|-----|-------|-------|---------------|
| LRU | O(1) | O(1) | O(1) | O(n) | General purpose |
| LFU | O(1) | O(1) | O(1) | O(n) | Frequency matters |
| FIFO | O(1) | O(1) | O(1) | O(n) | Simple, predictable |
| LIFO | O(1) | O(1) | O(1) | O(n) | Stack operations |
| MRU | O(1) | O(1) | O(1) | O(n) | Sequential scans |
| Random | O(1) | O(1) | O(1) | O(n) | No pattern |
| Round Robin | O(1) | O(1) | O(1) | O(n) | Fair allocation |
| 2Q | O(1) | O(1) | O(1) | O(n) | Scan resistance |

---

## Comparison Summary

### When to Choose Which Policy?

**LRU (Most Popular)**
- Default choice for 80% of use cases
- Balanced performance
- Leverages temporal locality

**LFU**
- Content delivery networks
- Popular items should stay
- Music/video streaming

**FIFO**
- Simple requirements
- Predictable behavior needed
- Age-based expiration

**2Q**
- Database systems
- Protection from scans
- Mixed workload patterns

**Random**
- Simplest implementation
- Hardware constraints
- No clear access pattern

**Round Robin**
- Fair resource allocation
- Load balancing
- Multiple users/tenants

---

## Example Scenarios

### Scenario 1: Web Browser Cache (LRU)
```
User visits: Home → About → Contact → Home
Cache: [Home, About, Contact]

Visit Products page:
→ Evict About (least recent)
Cache: [Contact, Home, Products]
```

### Scenario 2: Music App (LFU)
```
Songs: 
- Favorite Song (played 100x)
- New Song (played 2x)
- Old Song (played 1x)

New song arrives:
→ Evict Old Song (least frequent)
Keep: [Favorite (100x), New (2x)]
```

### Scenario 3: Video Scanning (2Q)
```
User scrolls through 100 videos (one-time view):
- All go to A1in (trial)
- Get evicted without polluting main cache
- User rewatches a video → Promoted to Am
- Frequently watched videos stay in Am
```

---

## Conclusion

The Strategy pattern allows flexible cache implementations with pluggable eviction policies. Each policy has specific strengths:

- **LRU**: Best general-purpose choice
- **LFU**: When frequency matters
- **2Q**: Protection from cache pollution
- **FIFO/Random**: Simplicity
- **Round Robin**: Fairness

Choose based on your workload characteristics and requirements.