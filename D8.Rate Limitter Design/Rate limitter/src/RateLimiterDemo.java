import java.util.*;

// ----------------------- Rate Limiter with Subscription Model -----------------------
public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {
        Scanner sc = new Scanner(System.in);

        System.out.println("Choose Rate Limiter Strategy: ");
        System.out.println("1. Fixed Window");
        System.out.println("2. Sliding Window");
        System.out.println("3. Token Bucket");
        int choice = sc.nextInt();

        // Two users with different subscription plans
        User freeUser = new User("user1", SubscriptionPlan.FREE,
                RateLimiterFactory.createRateLimiter(SubscriptionPlan.FREE, choice));

        User proUser = new User("user2", SubscriptionPlan.PRO,
                RateLimiterFactory.createRateLimiter(SubscriptionPlan.PRO, choice));

        System.out.println("\n===== Testing Rate Limiter with Subscription Plans =====");
        for (int i = 1; i <= 6; i++) {
            System.out.println("[FREE PLAN] Request " + i + ": " +
                    (freeUser.rateLimiter.isAllowed(freeUser.userId) ? "Allowed" : "Blocked"));

            System.out.println("[PRO PLAN] Request " + i + ": " +
                    (proUser.rateLimiter.isAllowed(proUser.userId) ? "Allowed" : "Blocked"));

            Thread.sleep(1000); // simulate 1s gap
        }

        sc.close();
    }

    // ----------------------- RateLimiter Interface -----------------------
    interface RateLimiter {
        boolean isAllowed(String userId);
    }

    // ----------------------- Subscription Plans -----------------------
    enum SubscriptionPlan {
        FREE(3, 10),        // 3 requests per 10s
        PRO(5, 10),         // 5 requests per 10s
        ENTERPRISE(10, 10); // 10 requests per 10s

        final int maxRequests;
        final long windowSizeSeconds;

        SubscriptionPlan(int maxRequests, long windowSizeSeconds) {
            this.maxRequests = maxRequests;
            this.windowSizeSeconds = windowSizeSeconds;
        }
    }

    // ----------------------- User Class -----------------------
    static class User {
        String userId;
        SubscriptionPlan plan;
        RateLimiter rateLimiter;

        public User(String userId, SubscriptionPlan plan, RateLimiter rateLimiter) {
            this.userId = userId;
            this.plan = plan;
            this.rateLimiter = rateLimiter;
        }
    }

    // ----------------------- RateLimiter Factory -----------------------
    static class RateLimiterFactory {
        public static RateLimiter createRateLimiter(SubscriptionPlan plan, int strategyChoice) {
            switch (strategyChoice) {
                case 1:
                    return new FixedWindowRateLimiter(plan.maxRequests, plan.windowSizeSeconds);
                case 2:
                    return new SlidingWindowRateLimiter(plan.maxRequests, plan.windowSizeSeconds);
                case 3:
                    return new TokenBucketRateLimiter(plan.maxRequests, plan.windowSizeSeconds);
                default:
                    throw new IllegalArgumentException("Invalid strategy choice");
            }
        }
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
