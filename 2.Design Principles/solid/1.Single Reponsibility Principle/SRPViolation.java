import java.util.ArrayList;
import java.util.List;

/**
 * Single Responsibility Principle (SRP) - VIOLATION
 * This class has multiple reasons to change:
 * 1. Logic for managing items
 * 2. Logic for printing invoices
 * 3. Logic for database persistence
 */
class ShoppingCart {
    private List<Product> items = new ArrayList<>();

    public void addProduct(Product product) {
        items.add(product);
    }

    public double calculateTotalPrice() {
        return items.stream().mapToDouble(p -> p.price).sum();
    }

    // Violation: The ShoppingCart class should not be responsible for printing
    public void printInvoice() {
        System.out.println("Invoice:");
        for (Product item : items) {
            System.out.println(item.name + ": " + item.price);
        }
        System.out.println("Total: " + calculateTotalPrice());
    }

    // Violation: The ShoppingCart class should not be responsible for persistence
    public void saveToDatabase() {
        System.out.println("Saving cart to database...");
    }
}

class Product {
    String name;
    double price;

    Product(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

public class SRPViolation {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Product("Laptop", 1200.00));
        cart.addProduct(new Product("Mouse", 25.00));

        // One class doing everything
        cart.printInvoice();
        cart.saveToDatabase();
    }
}
