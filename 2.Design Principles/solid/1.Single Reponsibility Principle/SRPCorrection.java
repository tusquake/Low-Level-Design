import java.util.ArrayList;
import java.util.List;

/**
 * Single Responsibility Principle (SRP) - CORRECTION
 * Each class has only one responsibility.
 */

// 1. Manages items and business logic
class ShoppingCart {
    private List<Product> items = new ArrayList<>();

    public void addProduct(Product product) {
        items.add(product);
    }

    public double calculateTotalPrice() {
        return items.stream().mapToDouble(p -> p.price).sum();
    }

    public List<Product> getItems() {
        return items;
    }
}

// 2. Handles the presentation/printing logic
class InvoicePrinter {
    public void printInvoice(ShoppingCart cart) {
        System.out.println("Invoice:");
        for (Product item : cart.getItems()) {
            System.out.println(item.name + ": " + item.price);
        }
        System.out.println("Total: " + cart.calculateTotalPrice());
    }
}

// 3. Handles the data persistence logic
class CartRepository {
    public void save(ShoppingCart cart) {
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

public class SRPCorrection {
    public static void main(String[] args) {
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Product("Laptop", 1200.00));
        cart.addProduct(new Product("Mouse", 25.00));

        // Use dedicated classes for different responsibilities
        InvoicePrinter printer = new InvoicePrinter();
        printer.printInvoice(cart);

        CartRepository repository = new CartRepository();
        repository.save(cart);
    }
}
