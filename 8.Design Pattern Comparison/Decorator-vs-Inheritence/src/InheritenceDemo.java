abstract class INotification {
    public void send() {
        System.out.println("Sending notification");
    }
}

class LoggedNotification extends INotification {
    @Override
    public void send() {
        System.out.println("Logging...");
        super.send();
    }
}

class EncryptedNotification extends INotification {
    @Override
    public void send() {
        System.out.println("Encrypting...");
        super.send();
    }
}

class LoggedEncryptedNotification extends INotification {
    @Override
    public void send() {
        System.out.println("Logging...");
        System.out.println("Encrypting...");
        super.send();
    }
}


public class InheritenceDemo {
    public static void main(String[] args) {
        INotification notification = new EncryptedNotification();
        notification.send();

        notification = new LoggedNotification();
        notification.send();

        notification = new LoggedEncryptedNotification();
        notification.send();
    }
}
