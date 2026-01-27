public class CartInvoicePrinter {
    public void printInvoice(ShoppingCart cart) {
        System.out.println("----- Invoice -----");
        for (Product product : cart.getItems()) {
            System.out.println(product.getName() + ": $" + product.getPrice());
        }
        System.out.println("Total: $" + cart.calculateTotalPrice());
    }
}
