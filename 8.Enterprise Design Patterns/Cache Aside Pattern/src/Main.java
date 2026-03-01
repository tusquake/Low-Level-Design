import java.util.HashMap;
import java.util.Map;

class User {

    private int id;
    private String name;

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}


class Database {

    private Map<Integer, User> storage = new HashMap<>();

    public void save(User user, int id) {
        storage.put(id, user);
    }

    public User findById(int id) {
        System.out.println("Fetching from DATABASE");
        return storage.get(id);
    }
}

class Cache {

    private Map<Integer, User> cache = new HashMap<>();

    public User get(int id) {
        return cache.get(id);
    }

    public void put(int id, User user) {
        cache.put(id, user);
    }

    public void evict(int id) {
        cache.remove(id);
    }
}

class UserService {

    private Cache cache;
    private Database database;

    public UserService(Cache cache, Database database) {
        this.cache = cache;
        this.database = database;
    }

    public User getUser(int id) {

        // Step 1: Check Cache
        User user = cache.get(id);

        if (user != null) {
            System.out.println("Fetching from CACHE");
            return user;
        }

        // Step 2: Fetch from DB
        user = database.findById(id);

        if (user != null) {
            cache.put(id, user); // Step 3: Update Cache
        }

        return user;
    }

    public void updateUser(int id, String newName) {

        // Update DB
        database.save(new User(id, newName), id);

        // Invalidate Cache
        cache.evict(id);

        System.out.println("User updated and cache invalidated");
    }
}

public class Main {

    public static void main(String[] args) {

        Cache cache = new Cache();
        Database database = new Database();

        // Preload DB
        database.save(new User(1, "Tushar"), 1);

        UserService service = new UserService(cache, database);

        // First call → DB
        service.getUser(1);

        // Second call → Cache
        service.getUser(1);

        // Update user → invalidate cache
        service.updateUser(1, "Updated Tushar");

        // Fetch again → DB (because cache was evicted)
        service.getUser(1);
        service.getUser(1);
    }
}