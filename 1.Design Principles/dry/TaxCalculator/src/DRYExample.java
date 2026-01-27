public class DRYExample {

    public static void main(String[] args) {

        double productPrice = 1000;
        double servicePrice = 500;

        System.out.println("Product Final Price: " + calculateFinalPrice(productPrice));
        System.out.println("Service Final Price: " + calculateFinalPrice(servicePrice));
    }

    private static double calculateFinalPrice(double price) {
        return price + (price * 0.10);
    }
}
