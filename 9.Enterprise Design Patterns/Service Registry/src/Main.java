import java.util.HashMap;
import java.util.Map;

class ServiceRegistry {

    private static Map<String, String> registry = new HashMap<>();

    public static void register(String serviceName, String address) {
        registry.put(serviceName, address);
        System.out.println(serviceName + " registered at " + address);
    }

    public static String discover(String serviceName) {
        return registry.get(serviceName);
    }
}

class PaymentService {

    private String serviceAddress;

    public PaymentService(String address) {
        this.serviceAddress = address;
        ServiceRegistry.register("PAYMENT_SERVICE", address);
    }

    public void processPayment(double amount) {
        System.out.println("Processing payment: " + amount);
    }
}

class OrderService {

    public void createOrder(double amount) {

        // Discover Payment Service dynamically
        String paymentServiceAddress = ServiceRegistry.discover("PAYMENT_SERVICE");

        if (paymentServiceAddress == null) {
            System.out.println("Payment Service not available!");
            return;
        }

        System.out.println("Calling Payment Service at: " + paymentServiceAddress);
        System.out.println("Order created for amount: " + amount);
    }
}

public class Main {

    public static void main(String[] args) {

        // Payment service starts and registers
        PaymentService paymentService = new PaymentService("http://localhost:8041");

        // Order service tries to call payment service
        OrderService orderService = new OrderService();
        orderService.createOrder(1500);
    }
}