class UserRepository {

    public String findUserById(int id) {
        return "User-" + id;  // Simulating DB call
    }
}

class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public String getUserDetails(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException("Invalid ID");
        }

        return repository.findUserById(id);
    }
}

class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public void getUser(int id) {
        String user = service.getUserDetails(id);
        System.out.println("Response: " + user);
    }
}

public class Main {

    public static void main(String[] args) {

        UserRepository repository = new UserRepository();
        UserService service = new UserService(repository);
        UserController controller = new UserController(service);

        controller.getUser(1);
        controller.getUser(2);
    }
}