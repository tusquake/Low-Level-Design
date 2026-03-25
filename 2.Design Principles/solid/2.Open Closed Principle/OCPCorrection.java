/**
 * Open/Closed Principle (OCP) - CORRECTION
 * The code is open for extension but closed for modification.
 * We can add new storage types without changing existing classes.
 */

// 1. Defining an interface for persistence
interface InvoiceRepository {
    void save(Invoice invoice);
}

// 2. Different implementations for different storage types
class MySQLInvoiceRepository implements InvoiceRepository {
    @Override
    public void save(Invoice invoice) {
        System.out.println("Saving to MySQL DB: " + invoice.id);
    }
}

class FileInvoiceRepository implements InvoiceRepository {
    @Override
    public void save(Invoice invoice) {
        System.out.println("Saving to File: " + invoice.id);
    }
}

// New storage types can be added without modifying existing code
class MongoInvoiceRepository implements InvoiceRepository {
    @Override
    public void save(Invoice invoice) {
        System.out.println("Saving to MongoDB: " + invoice.id);
    }
}

class Invoice {
    String id;
    Invoice(String id) { this.id = id; }
}

// 3. PersistenceManager works with the interface, not concrete classes
class PersistenceManager {
    private InvoiceRepository repository;

    public PersistenceManager(InvoiceRepository repository) {
        this.repository = repository;
    }

    public void persist(Invoice invoice) {
        this.repository.save(invoice);
    }
}

public class OCPCorrection {
    public static void main(String[] args) {
        Invoice invoice = new Invoice("INV-101");

        // We can easily switch the persistence mechanism without changing the manager
        InvoiceRepository repository = new MySQLInvoiceRepository();
        PersistenceManager manager = new PersistenceManager(repository);
        manager.persist(invoice);

        // Switch to File storage without modifying the manager
        repository = new FileInvoiceRepository();
        manager = new PersistenceManager(repository);
        manager.persist(invoice);
    }
}
