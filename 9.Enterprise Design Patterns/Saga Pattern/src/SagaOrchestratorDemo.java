class OrderCreatedEvent {

    private String orderId;

    public OrderCreatedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

class PaymentService {

    public boolean processPayment(String orderId) {
        System.out.println("Processing payment for order: " + orderId);
        return true; // simulate success
    }

    public void refundPayment(String orderId) {
        System.out.println("Refunding payment for order: " + orderId);
    }
}

class InventoryService {

    public boolean reserveInventory(String orderId) {
        System.out.println("Reserving inventory for order: " + orderId);
        return true; // simulate failure
    }

    public void releaseInventory(String orderId) {
        System.out.println("Releasing inventory for order: " + orderId);
    }
}

class NotificationService {

    public boolean sendNotification(String orderId) {
        System.out.println("Sending Notification for order: " + orderId);
        return false;
    }
}

class OrderService {

    public void createOrder(String orderId) {
        System.out.println("Order created: " + orderId);
    }

    public void cancelOrder(String orderId) {
        System.out.println("Order cancelled: " + orderId);
    }
}

class OrderSagaOrchestrator {

    private PaymentService paymentService;
    private InventoryService inventoryService;
    private OrderService orderService;
    private NotificationService notificationService;

    public OrderSagaOrchestrator(PaymentService paymentService,
                                 InventoryService inventoryService,
                                 OrderService orderService,NotificationService notificationService) {
        this.paymentService = paymentService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.notificationService = notificationService;
    }

    public void execute(String orderId) {

        orderService.createOrder(orderId);

        boolean paymentSuccess = paymentService.processPayment(orderId);
        if (!paymentSuccess) {
            orderService.cancelOrder(orderId);
            return;
        }

        boolean inventorySuccess = inventoryService.reserveInventory(orderId);
        if (!inventorySuccess) {
            paymentService.refundPayment(orderId);
            orderService.cancelOrder(orderId);
            return;
        }

        boolean notificationSuccess = notificationService.sendNotification(orderId);
        if (!notificationSuccess) {
            paymentService.refundPayment(orderId);
            inventoryService.releaseInventory(orderId);
            orderService.cancelOrder(orderId);
            return;
        }

        System.out.println("Saga completed successfully for order: " + orderId);
    }
}

public class SagaOrchestratorDemo {

    public static void main(String[] args) {

        PaymentService paymentService = new PaymentService();
        InventoryService inventoryService = new InventoryService();
        OrderService orderService = new OrderService();
        NotificationService notificationService = new NotificationService();

        OrderSagaOrchestrator orchestrator =
                new OrderSagaOrchestrator(paymentService, inventoryService, orderService, notificationService);

        orchestrator.execute("ORD-5001");
    }
}