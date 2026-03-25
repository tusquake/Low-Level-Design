/**
 * Abstraction Demo
 * 
 * Shows how to use Interface and Abstract Class to hide complexity.
 */

// 1. Interface (100% Abstraction - The "Contract")
interface StorageService {
    void uploadFile(String fileName);
    void deleteFile(String fileName);
}

// 2. Abstract Class (0-100% Abstraction - The "Blueprint")
abstract class CloudProvider {
    protected String providerName;

    public CloudProvider(String providerName) {
        this.providerName = providerName;
    }

    // Concrete method (Common logic)
    public void displayInfo() {
        System.out.println("Using Cloud Provider: " + providerName);
    }

    // Abstract method (Must be implemented by children)
    public abstract void connect();
}

// 3. Concrete Implementation
class AWSProvider extends CloudProvider implements StorageService {
    
    public AWSProvider() {
        super("Amazon Web Services");
    }

    @Override
    public void connect() {
        System.out.println("Connecting to AWS S3 endpoints...");
    }

    @Override
    public void uploadFile(String fileName) {
        System.out.println("Uploading " + fileName + " to AWS S3 bucket.");
    }

    @Override
    public void deleteFile(String fileName) {
        System.out.println("Deleting " + fileName + " from AWS S3 bucket.");
    }
}

class AzureProvider extends CloudProvider implements StorageService {
    
    public AzureProvider() {
        super("Microsoft Azure");
    }

    @Override
    public void connect() {
        System.out.println("Connecting to Azure Blob Storage...");
    }

    @Override
    public void uploadFile(String fileName) {
        System.out.println("Uploading " + fileName + " to Azure Container.");
    }

    @Override
    public void deleteFile(String fileName) {
        System.out.println("Deleting " + fileName + " from Azure Container.");
    }
}

public class AbstractionDemo {
    public static void main(String[] args) {
        System.out.println("=== ABSTRACTION DEMO ===\n");

        // 4. Abstraction in action: Using the Interface/Abstract Class reference
        StorageService storage;

        System.out.println("--- Scenario 1: Switching to AWS ---");
        AWSProvider aws = new AWSProvider();
        aws.displayInfo();
        aws.connect();
        storage = aws; // Upcasting to Interface
        storage.uploadFile("portrait.jpg");

        System.out.println("\n--- Scenario 2: Switching to Azure ---");
        AzureProvider azure = new AzureProvider();
        azure.displayInfo();
        azure.connect();
        storage = azure; // Upcasting to Interface
        storage.uploadFile("backup.zip");

        System.out.println("\nSummary: The user of 'StorageService' doesn't need to know the internal implementation of AWS or Azure!");
    }
}
