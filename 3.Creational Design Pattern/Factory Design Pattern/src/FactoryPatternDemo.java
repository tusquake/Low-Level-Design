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

    public static Notification createNotification(String type) {

        if (type.equalsIgnoreCase("EMAIL")) {
            return new EmailNotification();
        }
        else if (type.equalsIgnoreCase("SMS")) {
            return new SmsNotification();
        }
        else if (type.equalsIgnoreCase("PUSH")) {
            return new PushNotification();
        }

        throw new IllegalArgumentException("Invalid notification type");
    }
}

public class FactoryPatternDemo {

    public static void main(String[] args) {

        Notification notification =
                NotificationFactory.createNotification("EMAIL");

        notification.send("Welcome to our app!");

        Notification sms =
                NotificationFactory.createNotification("SMS");

        sms.send("Your OTP is 1234");
    }
}