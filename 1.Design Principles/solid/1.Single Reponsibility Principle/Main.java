public class Main {
    public static void main(String[] args) {
        Product p1 = new Product("Book", 12.99);
        Product p2 = new Product("Pen", 1.49);

        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(p1);
        cart.addProduct(p2);

        CartInvoicePrinter printer = new CartInvoicePrinter();
        printer.printInvoice(cart);

        CartDBStorage dbStorage = new CartDBStorage();
        dbStorage.saveToDB(cart);
    }
}
