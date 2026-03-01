import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Order {

    private String orderId;

    public Order(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

class OutboxEvent {

    private String eventId;
    private String payload;
    private boolean published;

    public OutboxEvent(String eventId, String payload) {
        this.eventId = eventId;
        this.payload = payload;
        this.published = false;
    }

    public String getEventId() {
        return eventId;
    }

    public String getPayload() {
        return payload;
    }

    public boolean isPublished() {
        return published;
    }

    public void markPublished() {
        this.published = true;
    }
}


class Database {

    public List<Order> orders = new ArrayList<>();
    public List<OutboxEvent> outbox = new ArrayList<>();

    public void saveOrder(Order order) {
        orders.add(order);
        System.out.println("Order saved in DB");
    }

    public void saveOutboxEvent(OutboxEvent event) {
        outbox.add(event);
        System.out.println("Event saved in OUTBOX");
    }
}


class OrderService {

    private Database database;

    public OrderService(Database database) {
        this.database = database;
    }

    public void createOrder(String orderId) {

        // Simulating single transaction
        Order order = new Order(orderId);
        database.saveOrder(order);

        OutboxEvent event = new OutboxEvent(
                UUID.randomUUID().toString(),
                "OrderCreated:" + orderId
        );

        database.saveOutboxEvent(event);
    }
}

class EventPublisher {

    private Database database;

    public EventPublisher(Database database) {
        this.database = database;
    }

    public void publishEvents() {

        for (OutboxEvent event : database.outbox) {

            if (!event.isPublished()) {

                System.out.println("Publishing event: " + event.getPayload());

                // Simulate successful publish
                event.markPublished();
            }
        }
    }
}

public class Main {

    public static void main(String[] args) {

        Database database = new Database();

        OrderService orderService = new OrderService(database);
        EventPublisher publisher = new EventPublisher(database);

        // Step 1: Create Order
        orderService.createOrder("ORD-777");

        System.out.println();

        // Step 2: Later, publisher publishes events
        publisher.publishEvents();
    }
}