import java.util.ArrayList;
import java.util.List;

class OrderCreatedEvents {
    private String orderId;

    public OrderCreatedEvents(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

class PaymentProcessedEvent {
    private String orderId;

    public PaymentProcessedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

class PaymentFailedEvent {
    private String orderId;

    public PaymentFailedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

class InventoryReservedEvent {
    private String orderId;

    public InventoryReservedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}

interface EventListener {
    void onEvent(Object event);
}

class EventBus {

    private List<EventListener> listeners = new ArrayList<>();

    public void register(EventListener listener) {
        listeners.add(listener);
    }

    public void publish(Object event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}

class OrderServices implements EventListener {

    private EventBus eventBus;

    public OrderServices(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void createOrder(String orderId) {
        System.out.println("Order created: " + orderId);
        eventBus.publish(new OrderCreatedEvents(orderId));
    }

    @Override
    public void onEvent(Object event) {

        if (event instanceof PaymentFailedEvent) {
            PaymentFailedEvent e = (PaymentFailedEvent) event;
            System.out.println("Order cancelled due to payment failure: " + e.getOrderId());
        }

        if (event instanceof InventoryReservedEvent) {
            InventoryReservedEvent e = (InventoryReservedEvent) event;
            System.out.println("Order completed successfully: " + e.getOrderId());
        }
    }
}

class PaymentServices implements EventListener {

    private EventBus eventBus;

    public PaymentServices(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onEvent(Object event) {

        if (event instanceof OrderCreatedEvents) {

            OrderCreatedEvents e = (OrderCreatedEvents) event;
            System.out.println("Processing payment for: " + e.getOrderId());

            boolean paymentSuccess = false; // simulate success

            if (paymentSuccess) {
                eventBus.publish(new PaymentProcessedEvent(e.getOrderId()));
            } else {
                eventBus.publish(new PaymentFailedEvent(e.getOrderId()));
            }
        }
    }
}

class InventoryServices implements EventListener {

    private EventBus eventBus;

    public InventoryServices(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void onEvent(Object event) {

        if (event instanceof PaymentProcessedEvent) {

            PaymentProcessedEvent e = (PaymentProcessedEvent) event;
            System.out.println("Reserving inventory for: " + e.getOrderId());

            boolean inventoryAvailable = true; // simulate success

            if (inventoryAvailable) {
                eventBus.publish(new InventoryReservedEvent(e.getOrderId()));
            }
        }
    }
}

public class SagaChoreographyDemo {

    public static void main(String[] args) {

        EventBus eventBus = new EventBus();

        OrderServices OrderServices = new OrderServices(eventBus);
        PaymentServices PaymentServices = new PaymentServices(eventBus);
        InventoryServices InventoryServices = new InventoryServices(eventBus);

        eventBus.register(OrderServices);
        eventBus.register(PaymentServices);
        eventBus.register(InventoryServices);

        OrderServices.createOrder("ORD-9001");
    }
}
