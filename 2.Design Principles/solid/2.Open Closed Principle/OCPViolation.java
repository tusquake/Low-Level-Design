/**
 * Open/Closed Principle (OCP) - VIOLATION
 * This class is not closed for modification. 
 * Every time a new storage type is added, this class must be modified.
 */
class InvoicePersistence {
    public void saveToMySQL() {
        System.out.println("Saving to MySQL DB...");
    }

    // Violation: Adding new methods for each new storage type
    public void saveToFile() {
        System.out.println("Saving to File...");
    }

    // Violation: Every time a new database like MongoDB is added, 
    // we have to modify this existing class.
    public void saveToMongoDB() {
        System.out.println("Saving to MongoDB...");
    }
}

public class OCPViolation {
    public static void main(String[] args) {
        InvoicePersistence persistence = new InvoicePersistence();
        
        // Hardcoded logic for different storage types
        persistence.saveToMySQL();
        persistence.saveToFile();
        persistence.saveToMongoDB();
    }
}
