interface Notification {
    void send();
}

class BasicNotification implements Notification {
    public void send() {
        System.out.println("Sending notification");
    }
}

abstract class NotificationDecorator implements Notification {
    protected Notification notification;

    public NotificationDecorator(Notification notification) {
        this.notification = notification;
    }
}

class LoggingDecorator extends NotificationDecorator {

    public LoggingDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        System.out.println("Logging...");
        notification.send();
    }
}

class EncryptionDecorator extends NotificationDecorator {

    public EncryptionDecorator(Notification notification) {
        super(notification);
    }

    @Override
    public void send() {
        System.out.println("Encrypting...");
        notification.send();
    }
}

public class DecoratorDemo {
    public static void main(String[] args) {
        Notification notification = new EncryptionDecorator(
                new LoggingDecorator(
                        new BasicNotification()
                ));
        notification.send();
    }
}
