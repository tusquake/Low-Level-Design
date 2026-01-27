class User {
    String name;
    String email;
}

class UserService {

    public void saveUser(User user) {
        validate(user);
        encryptData(user);
        auditLog(user);
        sendEmail(user);
        saveToDatabase(user);
    }

    private void validate(User user) {
        System.out.println("Validating user");
    }

    private void encryptData(User user) {
        System.out.println("Encrypting data");
    }

    private void auditLog(User user) {
        System.out.println("Auditing user");
    }

    private void sendEmail(User user) {
        System.out.println("Sending welcome email");
    }

    private void saveToDatabase(User user) {
        System.out.println("Saving user to DB");
    }
}

public class YAGNIViolationExample {

    public static void main(String[] args) {
        User user = new User();
        user.name = "Tushar";
        user.email = "tushar@email.com";

        new UserService().saveUser(user);
    }
}
