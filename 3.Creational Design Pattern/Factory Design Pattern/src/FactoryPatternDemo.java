enum NotificationType {
    EMAIL,
    SMS,
    PUSH
}

interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("Sending Email: " + message);
    }
}

class SmsNotification implements Notification {
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

class PushNotification implements Notification {
    public void send(String message) {
        System.out.println("Sending Push Notification: " + message);
    }
}


class NotificationFactory {

    public static Notification createNotification(NotificationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }

        switch (type) {
            case EMAIL:
                return new EmailNotification();
            case SMS:
                return new SmsNotification();
            case PUSH:
                return new PushNotification();
            default:
                throw new IllegalArgumentException("Unknown notification type: " + type);
        }
    }
}

public class FactoryPatternDemo {

    public static void main(String[] args) {

        Notification notification =
                NotificationFactory.createNotification(NotificationType.EMAIL);

        notification.send("Welcome to our app!");

        Notification sms =
                NotificationFactory.createNotification(NotificationType.SMS);

        sms.send("Your OTP is 1234");
    }
}