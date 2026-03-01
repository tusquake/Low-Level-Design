interface NotificationService {
    void send(String message);
}

class EmailNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}

class SMSNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class OrderService {

    private NotificationService notificationService;

    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void placeOrder() {
        System.out.println("Order placed successfully");
        notificationService.send("Your order is confirmed");
    }
}

public class Main {

    public static void main(String[] args) {

        NotificationService emailService = new EmailNotificationService();
        OrderService order1 = new OrderService(emailService);
        order1.placeOrder();

        System.out.println();

        NotificationService smsService = new SMSNotificationService();
        OrderService order2 = new OrderService(smsService);
        order2.placeOrder();
    }
}