import java.util.UUID;

class Logger {

    public static void log(String message) {
        System.out.println("[LOG] " + message);
    }

    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }
}

class MetricsCollector {

    private static int requestCount = 0;

    public static void incrementRequest() {
        requestCount++;
    }

    public static void printMetrics() {
        System.out.println("Total Requests Processed: " + requestCount);
    }
}

class Tracer {

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}

class OrderService {

    public void createOrder(double amount) {

        String traceId = Tracer.generateTraceId();

        Logger.log("TraceId: " + traceId + " - Order request received");

        MetricsCollector.incrementRequest();

        try {
            if (amount <= 0) {
                throw new IllegalArgumentException("Invalid amount");
            }

            Logger.log("TraceId: " + traceId + " - Order created for amount: " + amount);

        } catch (Exception e) {
            Logger.error("TraceId: " + traceId + " - Error: " + e.getMessage());
        }
    }
}

public class Main {

    public static void main(String[] args) {

        OrderService service = new OrderService();

        service.createOrder(1000);
        service.createOrder(-10);

        MetricsCollector.printMetrics();
    }
}