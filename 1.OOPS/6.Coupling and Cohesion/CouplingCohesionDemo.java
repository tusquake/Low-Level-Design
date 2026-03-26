/**
 * Coupling and Cohesion Demo
 * 
 * Shows how to achieve Low Coupling and High Cohesion.
 */

// --- 1. COHESION (Internal Strength) ---

// BAD: Low Cohesion (Fat Class)
class UserRegistrationFat {
    public void registerUser(String name) {
        System.out.println("Processing registration for " + name);
        System.out.println("Saving to Database..."); // DB Logic
        System.out.println("Sending Welcome Email..."); // Email Logic
        System.out.println("Generating PDF Report..."); // Reporting Logic
    }
}

// GOOD: High Cohesion (Focused Classes)
class UserRepository {
    public void save(String name) { System.out.println("Saving " + name + " to DB."); }
}

class EmailService {
    public void sendWelcome(String name) { System.out.println("Sending Email to " + name); }
}

class UserRegistrationService {
    private UserRepository repo = new UserRepository();
    private EmailService email = new EmailService();

    public void register(String name) {
        System.out.println("Registering: " + name);
        repo.save(name);
        email.sendWelcome(name);
    }
}

// --- 2. COUPLING (Inter-Dependency) ---

// BAD: Tight Coupling (Direct dependency on concrete class)
class MySQLEngine {
    public void runQuery() { System.out.println("Executing MySQL Query..."); }
}

class DatabaseClientTight {
    private MySQLEngine engine = new MySQLEngine();

    public void doWork() {
        engine.runQuery(); // If I want to switch to PostgreSQL, I must change this class code.
    }
}

// GOOD: Loose Coupling (Dependency on Interface)
interface DatabaseEngine {
    void runQuery();
}

class PostgreSQLEngine implements DatabaseEngine {
    public void runQuery() { System.out.println("Executing PostgreSQL Query (Loose Coupling)..."); }
}

class DatabaseClientLoose {
    private DatabaseEngine engine;

    // Passing the dependency (Constructor Injection)
    public DatabaseClientLoose(DatabaseEngine engine) {
        this.engine = engine;
    }

    public void doWork() {
        engine.runQuery(); // This class doesn't know WHICH engine it's using!
    }
}

public class CouplingCohesionDemo {
    public static void main(String[] args) {
        System.out.println("=== COUPLING & COHESION DEMO ===\n");

        System.out.println("--- 1. High Cohesion Example ---");
        UserRegistrationService regService = new UserRegistrationService();
        regService.register("Tushar");

        System.out.println("\n--- 2. Loose Coupling Example ---");
        // We can easily swap engines without changing DatabaseClientLoose
        DatabaseEngine engine = new PostgreSQLEngine();
        DatabaseClientLoose client = new DatabaseClientLoose(engine);
        client.doWork();

        System.out.println("\nSummary: High Cohesion makes classes easier to maintain. Loose Coupling makes them easier to swap and test!");
    }
}
