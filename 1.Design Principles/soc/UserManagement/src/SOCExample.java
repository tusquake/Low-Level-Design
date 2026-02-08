class UserController {

    private UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public void createUser(String name) {
        userService.createUser(name);
    }
}


class UserService {

    private UserRepository userRepository;
    private EmailService emailService;

    public UserService() {
        this.userRepository = new UserRepository();
        this.emailService = new EmailService();
    }

    public void createUser(String name) {

        if (name == null || name.isEmpty()) {
            System.out.println("Invalid user name");
            return;
        }

        System.out.println("Creating user: " + name);

        userRepository.saveUser(name);
        emailService.sendEmail(name);
    }
}


class EmailService {

    public void sendEmail(String name) {
        System.out.println("Email sent to user: " + name);
    }
}


class UserRepository {

    public void saveUser(String name) {
        System.out.println("User saved in database: " + name);
    }
}

public class SOCExample {
    public static void main(String[] args) {

        UserController controller = new UserController();

        controller.createUser("Tushar");
        controller.createUser("");
    }
}

