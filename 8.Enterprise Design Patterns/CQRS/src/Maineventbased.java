import java.util.*;

class Users {

    private int id;
    private String name;

    public Users(int id, String name) {
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

interface Event {
}

class UsersCreatedEvent implements Event {

    private int id;
    private String name;

    public UsersCreatedEvent(int id, String name) {
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

class EventBus {

    private List<EventListener> listeners = new ArrayList<>();

    public void register(EventListener listener) {
        listeners.add(listener);
    }

    public void publish(Event event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

class UsersCommandService {

    private Map<Integer, Users> writeDB;
    private EventBus eventBus;

    public UsersCommandService(Map<Integer, Users> writeDB, EventBus eventBus) {
        this.writeDB = writeDB;
        this.eventBus = eventBus;
    }

    public void createUsers(int id, String name) {

        Users Users = new Users(id, name);
        writeDB.put(id, Users);

        System.out.println("Users created in Write DB");

        // Publish event
        eventBus.publish(new UsersCreatedEvent(id, name));
    }
}

class UsersQueryService implements EventListener {

    private Map<Integer, Users> readDB = new HashMap<>();

    @Override
    public void onEvent(Event event) {

        if (event instanceof UsersCreatedEvent) {
            UsersCreatedEvent e = (UsersCreatedEvent) event;
            readDB.put(e.getId(), new Users(e.getId(), e.getName()));
            System.out.println("Read DB updated from event");
        }
    }

    public void getUsers(int id) {

        Users Users = readDB.get(id);

        if (Users != null) {
            System.out.println("Query Result: " + Users.getName());
        } else {
            System.out.println("Users not found in Read DB");
        }
    }
}


public class Maineventbased {

    public static void main(String[] args) {

        Map<Integer, Users> writeDB = new HashMap<>();

        EventBus eventBus = new EventBus();

        UsersQueryService queryService = new UsersQueryService();
        eventBus.register(queryService);

        UsersCommandService commandService =
                new UsersCommandService(writeDB, eventBus);
        
        commandService.createUsers(1, "Tushar");

        queryService.getUsers(1);
    }
}