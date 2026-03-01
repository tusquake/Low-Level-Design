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

class UserCommandService {

    private Map<Integer, User> database;

    public UserCommandService(Map<Integer, User> database) {
        this.database = database;
    }

    public void createUser(int id, String name) {
        database.put(id, new User(id, name));
        System.out.println("User created");
    }
}

class UserQueryService {

    private Map<Integer, User> database;

    public UserQueryService(Map<Integer, User> database) {
        this.database = database;
    }

    public void getUser(int id) {
        User user = database.get(id);
        if (user != null) {
            System.out.println("User: " + user.getName());
        } else {
            System.out.println("User not found");
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Map<Integer, User> database = new HashMap<>();

        UserCommandService commandService = new UserCommandService(database);
        UserQueryService queryService = new UserQueryService(database);

        commandService.createUser(1, "Tushar");
        queryService.getUser(1);
    }
}