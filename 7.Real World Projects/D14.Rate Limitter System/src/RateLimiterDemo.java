import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

interface RateLimitStrategy {
    boolean allowRequest(String userId);
}

class TokenBucketStrategy implements RateLimitStrategy {
    private final long capacity;
    private final long refillRate;
    private final ConcurrentHashMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    static class Bucket {
        AtomicLong tokens;
        long lastRefillTime;

        Bucket(long capacity) {
            this.tokens = new AtomicLong(capacity);
            this.lastRefillTime = System.nanoTime();
        }
    }

    public TokenBucketStrategy(long capacity, long refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    @Override
    public boolean allowRequest(String userId) {
        Bucket bucket = buckets.computeIfAbsent(userId, k -> new Bucket(capacity));
        refill(bucket);

        if (bucket.tokens.get() > 0) {
            bucket.tokens.decrementAndGet();
            return true;
        }
        return false;
    }

    private void refill(Bucket bucket) {
        long now = System.nanoTime();
        long elapsed = (now - bucket.lastRefillTime) / 1_000_000_000;

        if (elapsed > 0) {
            long tokensToAdd = elapsed * refillRate;
            long newTokens = Math.min(capacity, bucket.tokens.get() + tokensToAdd);
            bucket.tokens.set(newTokens);
            bucket.lastRefillTime = now;
        }
    }
}

class RateLimiter {
    private final RateLimitStrategy strategy;

    public RateLimiter(RateLimitStrategy strategy) {
        this.strategy = strategy;
    }

    public boolean isAllowed(String userId) {
        return strategy.allowRequest(userId);
    }
}

public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
        // Create rate limiter: 10 tokens, refill 2 tokens per second
        RateLimitStrategy strategy = new TokenBucketStrategy(10, 2);
        RateLimiter limiter = new RateLimiter(strategy);

        System.out.println("Config: 10 tokens, refill 2 tokens/sec\n");

        // Test 1: Rapid requests (should hit limit)
        System.out.println("Test 1: Sending 15 rapid requests for user1");
        int allowed = 0;
        for (int i = 1; i <= 15; i++) {
            boolean result = limiter.isAllowed("user1");
            System.out.println("Request " + i + ": " + (result ? "ALLOWED" : "REJECTED"));
            if (result) allowed++;
        }
        System.out.println("Result: " + allowed + "/15 allowed\n");

        // Test 2: Wait and retry (tokens refilled)
        System.out.println("Test 2: Waiting 3 seconds for refill...");
        Thread.sleep(3000);

        System.out.println("Sending 8 more requests for user1");
        allowed = 0;
        for (int i = 1; i <= 8; i++) {
            boolean result = limiter.isAllowed("user1");
            System.out.println("Request " + i + ": " + (result ? "ALLOWED" : "REJECTED"));
            if (result) allowed++;
        }
        System.out.println("Result: " + allowed + "/8 allowed\n");

        // Test 3: Different user (separate limit)
        System.out.println("Test 3: Sending 12 requests for user2");
        allowed = 0;
        for (int i = 1; i <= 12; i++) {
            boolean result = limiter.isAllowed("user2");
            System.out.println("Request " + i + ": " + (result ? "ALLOWED" : "REJECTED"));
            if (result) allowed++;
        }
        System.out.println("Result: " + allowed + "/12 allowed");
    }
}