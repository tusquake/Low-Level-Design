import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteDataServiceProxy implements DataService {
    private RemoteDataService remoteService;
    private Map<String, CacheEntry> cache;
    private long cacheTimeoutMs;

    public RemoteDataServiceProxy(String serverUrl) {
        this.remoteService = new RemoteDataService(serverUrl);
        this.cache = new ConcurrentHashMap<>();
        this.cacheTimeoutMs = 30000; // 30 seconds
    }

    @Override
    public Map<String, Object> getUserData(String userId) throws Exception {
        // Check cache first
        CacheEntry cacheEntry = cache.get(userId);
        if (cacheEntry != null && cacheEntry.isValid(cacheTimeoutMs)) {
            System.out.println("Returning cached data for user " + userId);
            return cacheEntry.getData();
        }

        // Cache miss or expired - fetch from remote
        try {
            Map<String, Object> data = remoteService.getUserData(userId);
            // Update cache
            cache.put(userId, new CacheEntry(data));
            System.out.println("Data cached for user " + userId);
            return data;
        } catch (Exception e) {
            System.out.println("Remote call failed: " + e.getMessage());
            // Return cached data if available, even if expired
            if (cacheEntry != null) {
                System.out.println("Returning stale cached data for user " + userId);
                return cacheEntry.getData();
            }
            throw e;
        }
    }

    @Override
    public boolean updateUserData(String userId, Map<String, Object> data) throws Exception {
        try {
            boolean success = remoteService.updateUserData(userId, data);
            if (success) {
                // Update cache with new data
                CacheEntry cacheEntry = cache.get(userId);
                if (cacheEntry != null) {
                    cacheEntry.updateData(data);
                    System.out.println("Cache updated for user " + userId);
                }
            }
            return success;
        } catch (Exception e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }
}

