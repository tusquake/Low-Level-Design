import java.util.HashMap;
import java.util.Map;

public class RemoteDataService implements DataService {
    private String serverUrl;
    private Map<String, Map<String, Object>> remoteData;

    public RemoteDataService(String serverUrl) {
        this.serverUrl = serverUrl;
        initializeData();
    }

    private void initializeData() {
        remoteData = new HashMap<>();

        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "John Doe");
        user1.put("email", "john@example.com");
        remoteData.put("user1", user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Jane Smith");
        user2.put("email", "jane@example.com");
        remoteData.put("user2", user2);
    }

    @Override
    public Map<String, Object> getUserData(String userId) throws Exception {
        System.out.println("Making remote API call to " + serverUrl + "/users/" + userId);
        simulateNetworkDelay();

        if (remoteData.containsKey(userId)) {
            return new HashMap<>(remoteData.get(userId));
        } else {
            throw new Exception("User " + userId + " not found");
        }
    }

    @Override
    public boolean updateUserData(String userId, Map<String, Object> data) throws Exception {
        System.out.println("Making remote API call to update " + serverUrl + "/users/" + userId);
        simulateNetworkDelay();

        if (remoteData.containsKey(userId)) {
            remoteData.get(userId).putAll(data);
            return true;
        }
        return false;
    }

    private void simulateNetworkDelay() {
        try {
            Thread.sleep(1000); // Simulate network delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
