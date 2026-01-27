import java.util.HashMap;
import java.util.Map;

public class RemoteProxyDemo {
    public static void main(String[] args) {
        System.out.println("=== Remote Proxy Demo ===");

        // Create proxy
        DataService service = new RemoteDataServiceProxy("https://api.example.com");

        try {
            // First call - will hit remote service
            System.out.println("First call:");
            Map<String, Object> userData = service.getUserData("user1");
            System.out.println("Retrieved: " + userData);
            System.out.println();

            // Second call - will use cache
            System.out.println("Second call (within cache timeout):");
            userData = service.getUserData("user1");
            System.out.println("Retrieved: " + userData);
            System.out.println();

            // Update data
            System.out.println("Updating user data:");
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("email", "newemail@example.com");
            boolean success = service.updateUserData("user1", updateData);
            System.out.println("Update successful: " + success);
            System.out.println();

            // Verify update
            System.out.println("Verifying update (should use cache):");
            userData = service.getUserData("user1");
            System.out.println("Retrieved: " + userData);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
