import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OrderEvent {
    public final String status;

    public OrderEvent(String status) {
        this.status = status;
    }
}

interface Subscriber {
    void onEvent(OrderEvent event);
}


class EventBroker {

    private Map<String, List<Subscriber>> subscribers = new HashMap<>();

    public void subscribe(String topic, Subscriber sub) {
        subscribers.computeIfAbsent(topic, k -> new ArrayList<>()).add(sub);
    }

    public void publish(String topic, OrderEvent event) {
        if (subscribers.containsKey(topic)) {
            for (Subscriber sub : subscribers.get(topic)) {
                sub.onEvent(event);
            }
        }
    }
}

class AnalyticsService implements Subscriber {
    public void onEvent(OrderEvent event) {
        System.out.println("Analytics processed: " + event.status);
    }
}

class NotificationService implements Subscriber {
    public void onEvent(OrderEvent event) {
        System.out.println("Notification sent: " + event.status);
    }
}

public class PubSubDemo {
    public static void main(String[] args) {
        EventBroker broker = new EventBroker();

        broker.subscribe("ORDER_STATUS", new AnalyticsService());
        broker.subscribe("ORDER_STATUS", new NotificationService());

        broker.publish("ORDER_STATUS", new OrderEvent("DELIVERED"));
    }
}





