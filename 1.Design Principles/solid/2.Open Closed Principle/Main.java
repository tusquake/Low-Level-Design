public class Main {
    public static void main(String[] args) {
        Cart cart = new Cart();
        cart.addProduct(new Product("Laptop", 999.99));

        Storage storage = new FileStorage(); // or DBStorage, or FileStorage
        storage.save(cart);
    }
}
