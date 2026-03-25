class UserService {

    public void register(String email) {

        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        System.out.println("Saving user to DB");
        System.out.println("Sending welcome email to " + email);
    }
}

public class FailFast {

    public static void main(String[] args) {
        UserService service = new UserService();
        service.register("tushar@gmail.com");
        service.register(null);
    }
}