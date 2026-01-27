public class PrototypeDemo {
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
