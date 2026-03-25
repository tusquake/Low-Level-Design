public class DRYViolationExample {

    public static void main(String[] args) {

        double productPrice = 1000;
        double servicePrice = 500;

        double productFinalPrice = productPrice + (productPrice * 0.10);
        double serviceFinalPrice = servicePrice + (servicePrice * 0.10);

        System.out.println("Product Final Price: " + productFinalPrice);
        System.out.println("Service Final Price: " + serviceFinalPrice);
    }
}
