import java.util.HashMap;
import java.util.Map;

public class DatabaseConnection implements ConnectionPrototype {
    private String host;
    private int port;
    private String database;
    private String username;
    private Map<String, String> properties;

    public DatabaseConnection(String host, int port, String database, String username) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.properties = new HashMap<>();

        System.out.println("Creating DB connection - Loading SSL certificates...");
        try {
            Thread.sleep(200); // Expensive operation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private DatabaseConnection(DatabaseConnection original) {
        this.host = original.host;
        this.port = original.port;
        this.database = original.database;
        this.username = original.username;
        this.properties = new HashMap<>(original.properties);
        System.out.println("Cloning DB connection - Fast!");
    }

    @Override
    public ConnectionPrototype clone() {
        return new DatabaseConnection(this);
    }

    @Override
    public void connect() {
        System.out.println("Connected to " + database + " at " + host + ":" + port +
                " as " + username);
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addProperty(String key, String value) {
        properties.put(key, value);
    }
}
