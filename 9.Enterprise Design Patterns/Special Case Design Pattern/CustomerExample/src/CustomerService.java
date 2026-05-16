import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static final Map<String, String> db = new HashMap<>();

    static {
        db.put("101", "Tushar");
        db.put("102", "Alice");
        db.put("103", "DEACTIVATED_Bob"); // Simulated deactivated user
    }

    public static Customer getCustomer(String id) {
        String name = db.get(id);

        if (name == null) {
            // Special Case 1: Person not in system
            return new UnknownCustomer();
        }

        if (name.startsWith("DEACTIVATED_")) {
            // Special Case 2: Person is in system but is deactivated
            return new DeactivatedCustomer(name.replace("DEACTIVATED_", ""));
        }

        // Normal Case: Real active customer
        return new RealCustomer(name);
    }
}
