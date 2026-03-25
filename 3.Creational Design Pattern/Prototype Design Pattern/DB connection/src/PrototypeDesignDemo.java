import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 1. Prototype Interface
interface ConnectionPrototype extends Cloneable {
    ConnectionPrototype clone();
    void connect();
}

// 2. Concrete Prototype - Database Connection
class DatabaseConnection implements ConnectionPrototype {
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

// 3. Concrete Prototype - Document Template
class DocumentTemplate implements Cloneable {
    private String templateName;
    private String header;
    private String footer;
    private List<String> sections;

    public DocumentTemplate(String templateName) {
        this.templateName = templateName;
        this.sections = new ArrayList<>();

        // Expensive template loading
        System.out.println("Loading template: " + templateName + " from disk...");
        loadTemplate();
    }

    private void loadTemplate() {
        this.header = "Standard Company Header";
        this.footer = "© 2024 Company Name";
        sections.add("Title Section");
        sections.add("Content Section");
    }

    @Override
    public DocumentTemplate clone() {
        try {
            DocumentTemplate cloned = (DocumentTemplate) super.clone();
            cloned.sections = new ArrayList<>(this.sections);
            System.out.println("Cloning template: " + templateName);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public void customizeForClient(String clientName) {
        this.header = "Document for " + clientName;
        this.sections.add("Client-specific section for " + clientName);
    }

    public void generate() {
        System.out.println("=== Generated Document ===");
        System.out.println("Header: " + header);
        System.out.println("Sections: " + sections);
        System.out.println("Footer: " + footer);
        System.out.println("========================");
    }
}

// 4. Demo Application
public class PrototypeDesignDemo {
    public static void main(String[] args) {
        System.out.println("=== Database Connection Prototype ===");

        DatabaseConnection prodConnection = new DatabaseConnection(
                "prod.company.com", 5432, "main_db", "admin");
        prodConnection.addProperty("ssl", "true");

        DatabaseConnection testConnection = (DatabaseConnection) prodConnection.clone();
        testConnection.setDatabase("test_db");
        testConnection.setUsername("test_user");

        DatabaseConnection devConnection = (DatabaseConnection) prodConnection.clone();
        devConnection.setDatabase("dev_db");
        devConnection.setUsername("dev_user");

        prodConnection.connect();
        testConnection.connect();
        devConnection.connect();

        System.out.println("\n=== Document Template Prototype ===");

        DocumentTemplate contractTemplate = new DocumentTemplate("Standard Contract");

        DocumentTemplate clientAContract = contractTemplate.clone();
        clientAContract.customizeForClient("Client A");

        DocumentTemplate clientBContract = contractTemplate.clone();
        clientBContract.customizeForClient("Client B");

        clientAContract.generate();
        clientBContract.generate();

        System.out.println("=== Performance Benefits ===");
        System.out.println("✓ Expensive setup done once");
        System.out.println("✓ Fast cloning for variations");
        System.out.println("✓ Independent customization");
    }
}
