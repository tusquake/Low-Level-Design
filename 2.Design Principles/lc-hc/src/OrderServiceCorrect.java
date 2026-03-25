interface NotificationService {
    void notifyUser(String orderId);
}

class EmailService implements NotificationService {
    public void notifyUser(String orderId) {
        System.out.println("Sending email for order: " + orderId);
    }
}

class LoggingService implements NotificationService {
    public void notifyUser(String orderId) {
        System.out.println("Writing log to file for order: " + orderId);
    }
}

class OrderServiceGood {

    private NotificationService notificationService;

    public OrderServiceGood(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void placeOrder(String orderId) {
        System.out.println("Placing order: " + orderId);
        notificationService.notifyUser(orderId);
    }
}



public class OrderServiceCorrect {
    public static void main(String[] args) {
        NotificationService email = new EmailService();
        OrderServiceGood goodServiceEmail = new OrderServiceGood(email);
        goodServiceEmail.placeOrder("ORDER456");
        NotificationService log = new LoggingService();
        OrderServiceGood goodServiceLog = new OrderServiceGood(log);
        goodServiceLog.placeOrder("ORDER789");
    }
}

