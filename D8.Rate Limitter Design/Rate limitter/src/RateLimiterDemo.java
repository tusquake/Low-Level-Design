import java.util.*;

// ----------------------- Rate Limiter Mini Project -----------------------
public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("===== Fixed Window Rate Limiter =====");
        FixedWindowRateLimiter fixedLimiter = new FixedWindowRateLimiter(3, 10); // 3 requests per 10 seconds
        testLimiter(fixedLimiter);

        System.out.println("\n===== Sliding Window Rate Limiter =====");
        SlidingWindowRateLimiter slidingLimiter = new SlidingWindowRateLimiter(3, 10); // 3 requests per 10 seconds
        testLimiter(slidingLimiter);

        System.out.println("\n===== Token Bucket Rate Limiter =====");
        TokenBucketRateLimiter tokenBucketLimiter = new TokenBucketRateLimiter(3, 10); // 3 tokens per 10 seconds
        testLimiter(tokenBucketLimiter);
    }

    // Helper method to test limiter
    private static void testLimiter(RateLimiter limiter) throws InterruptedException {
        String userId = "user1";
        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.isAllowed(userId);
            System.out.println("Request " + i + ": " + (allowed ? "Allowed" : "Blocked"));
            Thread.sleep(2000); // wait 2 seconds between requests
        }
    }

    // ----------------------- RateLimiter Interface -----------------------
    interface RateLimiter {
        boolean isAllowed(String userId);
    }

    // ----------------------- Fixed Window -----------------------
    static class FixedWindowRateLimiter implements RateLimiter {
        private final int maxRequests;
        private final long windowSizeMillis;
        private final Map<String, UserRequest> userRequests;

        public FixedWindowRateLimiter(int maxRequests, long windowSizeSeconds) {
            this.maxRequests = maxRequests;
            this.windowSizeMillis = windowSizeSeconds * 1000;
            this.userRequests = new HashMap<>();
        }

        @Override
        public boolean isAllowed(String userId) {
            long currentTime = System.currentTimeMillis();
            long windowStart = currentTime / windowSizeMillis * windowSizeMillis;

            UserRequest req = userRequests.getOrDefault(userId, new UserRequest(windowStart, 0));

            if (req.windowStart == windowStart) {
                if (req.count < maxRequests) {
                    req.count++;
                    userRequests.put(userId, req);
                    return true;
                } else {
                    return false;
                }
            } else {
                req.windowStart = windowStart;
                req.count = 1;
                userRequests.put(userId, req);
                return true;
            }
        }

        static class UserRequest {
            long windowStart;
            int count;

            UserRequest(long windowStart, int count) {
                this.windowStart = windowStart;
                this.count = count;
            }
        }
    }

    // ----------------------- Sliding Window -----------------------
    static class SlidingWindowRateLimiter implements RateLimiter {
        private final int maxRequests;
        private final long windowSizeMillis;
        private final Map<String, Deque<Long>> userRequests;

        public SlidingWindowRateLimiter(int maxRequests, long windowSizeSeconds) {
            this.maxRequests = maxRequests;
            this.windowSizeMillis = windowSizeSeconds * 1000;
            this.userRequests = new HashMap<>();
        }

        @Override
        public boolean isAllowed(String userId) {
            long currentTime = System.currentTimeMillis();
            userRequests.putIfAbsent(userId, new LinkedList<>());
            Deque<Long> requests = userRequests.get(userId);

            while (!requests.isEmpty() && requests.peekFirst() <= currentTime - windowSizeMillis) {
                requests.pollFirst();
            }

            if (requests.size() < maxRequests) {
                requests.addLast(currentTime);
                return true;
            } else {
                return false;
            }
        }
    }

    // ----------------------- Token Bucket -----------------------
    static class TokenBucketRateLimiter implements RateLimiter {
        private final int maxTokens;
        private final long refillIntervalMillis;
        private final Map<String, Bucket> buckets;

        public TokenBucketRateLimiter(int maxTokens, long refillIntervalSeconds) {
            this.maxTokens = maxTokens;
            this.refillIntervalMillis = refillIntervalSeconds * 1000;
            this.buckets = new HashMap<>();
        }

        @Override
        public boolean isAllowed(String userId) {
            long currentTime = System.currentTimeMillis();
            buckets.putIfAbsent(userId, new Bucket(maxTokens, currentTime));
            Bucket bucket = buckets.get(userId);

            // Refill tokens
            long elapsed = currentTime - bucket.lastRefillTime;
            int refill = (int) (elapsed / refillIntervalMillis) * maxTokens;
            if (refill > 0) {
                bucket.tokens = Math.min(maxTokens, bucket.tokens + refill);
                bucket.lastRefillTime = currentTime;
            }

            if (bucket.tokens > 0) {
                bucket.tokens--;
                return true;
            } else {
                return false;
            }
        }

        static class Bucket {
            int tokens;
            long lastRefillTime;

            Bucket(int tokens, long lastRefillTime) {
                this.tokens = tokens;
                this.lastRefillTime = lastRefillTime;
            }
        }
    }
}
