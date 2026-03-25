import java.util.ArrayList;
import java.util.List;

interface Event {
}

class OrderPlacedEvent implements Event {

    private String orderId;

    public OrderPlacedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

interface EventListener {
    void onEvent(Event event);
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

class OrderService {

    private EventBus eventBus;

    public OrderService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void placeOrder(String orderId) {
        System.out.println("Order placed: " + orderId);

        // Publish event
        eventBus.publish(new OrderPlacedEvent(orderId));
    }
}

class EmailService implements EventListener {

    @Override
    public void onEvent(Event event) {

        if (event instanceof OrderPlacedEvent) {
            OrderPlacedEvent e = (OrderPlacedEvent) event;
            System.out.println("Sending Email for Order: " + e.getOrderId());
        }
    }
}

class InventoryService implements EventListener {

    @Override
    public void onEvent(Event event) {

        if (event instanceof OrderPlacedEvent) {
            OrderPlacedEvent e = (OrderPlacedEvent) event;
            System.out.println("Updating Inventory for Order: " + e.getOrderId());
        }
    }
}

public class Main {

    public static void main(String[] args) {

        EventBus eventBus = new EventBus();

        // Register consumers
        eventBus.register(new EmailService());
        eventBus.register(new InventoryService());

        OrderService orderService = new OrderService(eventBus);

        // Place order
        orderService.placeOrder("ORD-101");
    }
}