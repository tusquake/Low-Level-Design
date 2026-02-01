import java.util.*;

interface Cache<K, V> {
    V get(K key);
    void put(K key, V value);
}

class CacheEntry<V> {
    V value;
    long timestamp;
    int frequency;

    CacheEntry(V value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
        this.frequency = 1;
    }
}

interface EvictionStrategy<K> {
    K evictKey();
    void keyAccessed(K key);
    void keyAdded(K key);
    void keyRemoved(K key);
}

class LRUEvictionStrategy<K> implements EvictionStrategy<K> {

    private final LinkedHashSet<K> order = new LinkedHashSet<>();

    public K evictKey() {
        return order.iterator().next();
    }

    public void keyAccessed(K key) {
        order.remove(key);
        order.add(key);
    }

    public void keyAdded(K key) {
        order.add(key);
    }

    public void keyRemoved(K key) {
        order.remove(key);
    }
}

class MRUEvictionStrategy<K> implements EvictionStrategy<K> {

    private final LinkedList<K> order = new LinkedList<>();

    public K evictKey() {
        return order.getLast();
    }

    public void keyAccessed(K key) {
        order.remove(key);
        order.addLast(key);
    }

    public void keyAdded(K key) {
        order.addLast(key);
    }

    public void keyRemoved(K key) {
        order.remove(key);
    }
}

class LFUEvictionStrategy<K> implements EvictionStrategy<K> {

    private final Map<K, Integer> frequencyMap = new HashMap<>();

    public K evictKey() {
        return Collections.min(frequencyMap.entrySet(),
                Map.Entry.comparingByValue()).getKey();
    }

    public void keyAccessed(K key) {
        frequencyMap.put(key, frequencyMap.get(key) + 1);
    }

    public void keyAdded(K key) {
        frequencyMap.put(key, 1);
    }

    public void keyRemoved(K key) {
        frequencyMap.remove(key);
    }
}

class RandomEvictionStrategy<K> implements EvictionStrategy<K> {

    private final List<K> keys = new ArrayList<>();
    private final Random random = new Random();

    public K evictKey() {
        return keys.get(random.nextInt(keys.size()));
    }

    public void keyAccessed(K key) {}

    public void keyAdded(K key) {
        keys.add(key);
    }

    public void keyRemoved(K key) {
        keys.remove(key);
    }
}

class InMemoryCache<K, V> implements Cache<K, V> {

    private final Map<K, CacheEntry<V>> store = new HashMap<>();
    private final EvictionStrategy<K> evictionStrategy;
    private final int capacity;
    private final long ttlMillis;

    public InMemoryCache(int capacity, long ttlMillis, EvictionStrategy<K> evictionStrategy) {
        this.capacity = capacity;
        this.ttlMillis = ttlMillis;
        this.evictionStrategy = evictionStrategy;
    }

    public V get(K key) {
        CacheEntry<V> entry = store.get(key);
        if (entry == null || isExpired(entry)) {
            remove(key);
            return null;
        }
        entry.frequency++;
        evictionStrategy.keyAccessed(key);
        return entry.value;
    }

    public void put(K key, V value) {
        if (store.size() >= capacity) {
            K evictKey = evictionStrategy.evictKey();
            remove(evictKey);
        }
        CacheEntry<V> entry = new CacheEntry<>(value);
        store.put(key, entry);
        evictionStrategy.keyAdded(key);
    }

    private boolean isExpired(CacheEntry<V> entry) {
        return ttlMillis > 0 &&
                System.currentTimeMillis() - entry.timestamp > ttlMillis;
    }

    private void remove(K key) {
        store.remove(key);
        evictionStrategy.keyRemoved(key);
    }
}

public class CacheDemo {
    public static void main(String[] args) {

        Cache<String, String> cache =
                new InMemoryCache<>(
                        3,
                        5000,
                        new LRUEvictionStrategy<>()
                );

        cache.put("A", "Apple");
        cache.put("B", "Banana");
        cache.put("C", "Cherry");

        cache.get("A");

        cache.put("D", "Date");
        System.out.println(cache.get("B"));
    }
}




