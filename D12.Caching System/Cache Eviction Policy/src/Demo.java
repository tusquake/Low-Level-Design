import java.util.*;

class CacheEntry<K, V> {
    K key;
    V value;
    long timestamp;
    long lastAccessTime;
    int frequency;

    CacheEntry(K key, V value) {
        this.key = key;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
        this.lastAccessTime = this.timestamp;
        this.frequency = 1;
    }
}

interface EvictionPolicy<K, V> {
    void onGet(K key);
    void onPut(K key);
    K evict();
    void remove(K key);
}

class LRUPolicy<K, V> implements EvictionPolicy<K, V> {
    private LinkedHashMap<K, Boolean> order = new LinkedHashMap<>(16, 0.75f, true);

    public void onGet(K key) {
        order.get(key);
    }

    public void onPut(K key) {
        order.put(key, true);
    }

    public K evict() {
        return order.isEmpty() ? null : order.keySet().iterator().next();
    }

    public void remove(K key) {
        order.remove(key);
    }
}

class LFUPolicy<K, V> implements EvictionPolicy<K, V> {
    private Map<K, Integer> freq = new HashMap<>();
    private Map<Integer, LinkedHashSet<K>> freqMap = new HashMap<>();
    private int minFreq = 0;

    public void onGet(K key) {
        if (!freq.containsKey(key)) return;

        int f = freq.get(key);
        freqMap.get(f).remove(key);

        if (freqMap.get(f).isEmpty()) {
            freqMap.remove(f);
            if (minFreq == f) minFreq++;
        }

        freq.put(key, f + 1);
        freqMap.computeIfAbsent(f + 1, k -> new LinkedHashSet<>()).add(key);
    }

    public void onPut(K key) {
        freq.put(key, 1);
        freqMap.computeIfAbsent(1, k -> new LinkedHashSet<>()).add(key);
        minFreq = 1;
    }

    public K evict() {
        if (!freqMap.containsKey(minFreq)) return null;
        return freqMap.get(minFreq).iterator().next();
    }

    public void remove(K key) {
        if (!freq.containsKey(key)) return;
        int f = freq.remove(key);
        freqMap.get(f).remove(key);
        if (freqMap.get(f).isEmpty()) freqMap.remove(f);
    }
}

class FIFOPolicy<K, V> implements EvictionPolicy<K, V> {
    private Queue<K> queue = new LinkedList<>();

    public void onGet(K key) {}

    public void onPut(K key) {
        queue.offer(key);
    }

    public K evict() {
        return queue.poll();
    }

    public void remove(K key) {
        queue.remove(key);
    }
}

class LIFOPolicy<K, V> implements EvictionPolicy<K, V> {
    private Deque<K> stack = new LinkedList<>();

    public void onGet(K key) {}

    public void onPut(K key) {
        stack.push(key);
    }

    public K evict() {
        return stack.poll();
    }

    public void remove(K key) {
        stack.remove(key);
    }
}

class MRUPolicy<K, V> implements EvictionPolicy<K, V> {
    private Deque<K> stack = new LinkedList<>();

    public void onGet(K key) {
        stack.remove(key);
        stack.push(key);
    }

    public void onPut(K key) {
        stack.push(key);
    }

    public K evict() {
        return stack.poll();
    }

    public void remove(K key) {
        stack.remove(key);
    }
}

class RandomPolicy<K, V> implements EvictionPolicy<K, V> {
    private List<K> keys = new ArrayList<>();
    private Random random = new Random();

    public void onGet(K key) {}

    public void onPut(K key) {
        keys.add(key);
    }

    public K evict() {
        if (keys.isEmpty()) return null;
        return keys.remove(random.nextInt(keys.size()));
    }

    public void remove(K key) {
        keys.remove(key);
    }
}

class RoundRobinPolicy<K, V> implements EvictionPolicy<K, V> {
    private List<K> keys = new ArrayList<>();
    private int pointer = 0;

    public void onGet(K key) {}

    public void onPut(K key) {
        keys.add(key);
    }

    public K evict() {
        if (keys.isEmpty()) return null;
        if (pointer >= keys.size()) pointer = 0;
        K key = keys.get(pointer);
        keys.remove(pointer);
        if (pointer >= keys.size() && !keys.isEmpty()) pointer = 0;
        return key;
    }

    public void remove(K key) {
        int idx = keys.indexOf(key);
        if (idx != -1) {
            keys.remove(idx);
            if (pointer > idx) pointer--;
        }
    }
}

class TwoQueuePolicy<K, V> implements EvictionPolicy<K, V> {
    private int a1Size;
    private Queue<K> a1in = new LinkedList<>();
    private LinkedHashMap<K, Boolean> am = new LinkedHashMap<>(16, 0.75f, true);
    private Queue<K> a1out = new LinkedList<>();
    private int maxA1outSize;

    TwoQueuePolicy(int capacity) {
        this.a1Size = Math.max(1, capacity / 4);
        this.maxA1outSize = capacity / 2;
    }

    public void onGet(K key) {
        if (a1in.contains(key)) {
            a1in.remove(key);
        }
        am.get(key);
    }

    public void onPut(K key) {
        if (a1out.contains(key)) {
            am.put(key, true);
            a1out.remove(key);
        } else {
            a1in.offer(key);
        }
    }

    public K evict() {
        if (a1in.size() >= a1Size) {
            K key = a1in.poll();
            if (a1out.size() >= maxA1outSize) a1out.poll();
            a1out.offer(key);
            return key;
        }

        if (!am.isEmpty()) {
            K key = am.keySet().iterator().next();
            am.remove(key);
            return key;
        }

        return a1in.poll();
    }

    public void remove(K key) {
        a1in.remove(key);
        am.remove(key);
        a1out.remove(key);
    }
}

class Cache<K, V> {
    private int capacity;
    private Map<K, CacheEntry<K, V>> store = new HashMap<>();
    private EvictionPolicy<K, V> policy;

    Cache(int capacity, EvictionPolicy<K, V> policy) {
        this.capacity = capacity;
        this.policy = policy;
    }

    public V get(K key) {
        if (!store.containsKey(key)) return null;

        CacheEntry<K, V> entry = store.get(key);
        entry.lastAccessTime = System.currentTimeMillis();
        entry.frequency++;

        policy.onGet(key);
        return entry.value;
    }

    public void put(K key, V value) {
        if (store.containsKey(key)) {
            CacheEntry<K, V> entry = store.get(key);
            entry.value = value;
            entry.lastAccessTime = System.currentTimeMillis();
            return;
        }

        if (store.size() >= capacity) {
            K evictKey = policy.evict();
            if (evictKey != null) {
                store.remove(evictKey);
            }
        }

        CacheEntry<K, V> entry = new CacheEntry<>(key, value);
        store.put(key, entry);
        policy.onPut(key);
    }

    public int size() {
        return store.size();
    }
}

class Demo {
    public static void main(String[] args) {
        Cache<String, String> lru = new Cache<>(3, new LRUPolicy<>());
        lru.put("A", "1");
        lru.put("B", "2");
        lru.put("C", "3");
        lru.get("A");
        lru.put("D", "4");
        System.out.println(lru.get("D"));

        Cache<String, String> lfu = new Cache<>(3, new LFUPolicy<>());
        lfu.put("A", "1");
        lfu.put("B", "2");
        lfu.put("C", "3");
        lfu.get("A");
        lfu.get("A");
        lfu.put("D", "4");

        Cache<String, String> fifo = new Cache<>(3, new FIFOPolicy<>());
        fifo.put("A", "1");
        fifo.put("B", "2");
        fifo.put("C", "3");
        fifo.put("D", "4");

        Cache<String, String> rr = new Cache<>(3, new RoundRobinPolicy<>());
        rr.put("A", "1");
        rr.put("B", "2");
        rr.put("C", "3");
        rr.put("D", "4");

        Cache<String, String> twoQ = new Cache<>(4, new TwoQueuePolicy<>(4));
        twoQ.put("A", "1");
        twoQ.put("B", "2");
        twoQ.get("A");
        twoQ.put("C", "3");
        twoQ.put("D", "4");
    }
}