import java.util.HashMap;
import java.util.Map;

class Order {

    private String orderId;
    private double amount;

    public Order(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }
}


class OrderRepository {

    private Map<String, Order> orderDatabase = new HashMap<>();

    public void save(Order order) {
        orderDatabase.put(order.getOrderId(), order);
        System.out.println("Order saved in Order DB");
    }

    public Order findById(String orderId) {
        return orderDatabase.get(orderId);
    }
}

class OrderService {

    private OrderRepository repository = new OrderRepository();

    public void createOrder(String orderId, double amount) {
        Order order = new Order(orderId, amount);
        repository.save(order);

        System.out.println("Order created: " + orderId);
    }
}

class Payment {

    private String paymentId;
    private String orderId;
    private double amount;

    public Payment(String paymentId, String orderId, double amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }
}

class PaymentRepository {

    private Map<String, Payment> paymentDatabase = new HashMap<>();

    public void save(Payment payment) {
        paymentDatabase.put(payment.getPaymentId(), payment);
        System.out.println("Payment saved in Payment DB");
    }
}

class PaymentService {

    private PaymentRepository repository = new PaymentRepository();

    public void processPayment(String paymentId, String orderId, double amount) {

        // IMPORTANT:
        // Payment service does NOT access OrderRepository directly.

        Payment payment = new Payment(paymentId, orderId, amount);
        repository.save(payment);

        System.out.println("Payment processed with amount "+ amount + " for order: " + orderId);
    }
}

public class Main {

    public static void main(String[] args) {

        OrderService orderService = new OrderService();
        PaymentService paymentService = new PaymentService();

        orderService.createOrder("ORD1", 1000);
        paymentService.processPayment("PAY1", "ORD1", 1000);
    }
}