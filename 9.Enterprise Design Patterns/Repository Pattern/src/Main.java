import java.util.HashMap;
import java.util.Map;

class User {

    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

interface UserRepository {

    User findById(int id);

    void save(User user);
}

class InMemoryUserRepository implements UserRepository {

    private Map<Integer, User> database = new HashMap<>();

    @Override
    public User findById(int id) {
        return database.get(id);
    }

    @Override
    public void save(User user) {
        database.put(user.getId(), user);
    }
}

class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void createUser(int id, String name) {
        User user = new User(id, name);
        repository.save(user);
    }

    public void getUser(int id) {
        User user = repository.findById(id);
        if (user != null) {
            System.out.println("User: " + user.getName());
        } else {
            System.out.println("User not found");
        }
    }
}

public class Main {

    public static void main(String[] args) {

        UserRepository repository = new InMemoryUserRepository();
        UserService service = new UserService(repository);

        service.createUser(1, "Tushar");
        service.getUser(1);
    }
}