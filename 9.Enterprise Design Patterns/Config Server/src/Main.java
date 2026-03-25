import java.util.HashMap;
import java.util.Map;

class ConfigServer {

    private static Map<String, String> configs = new HashMap<>();

    static {
        configs.put("ORDER_DB_URL", "jdbc:mysql://prod-server:3306/orderdb");
        configs.put("PAYMENT_TIMEOUT", "5000");
    }

    public static String getConfig(String key) {
        return configs.get(key);
    }
}

class OrderService {

    private String dbUrl;

    public OrderService() {
        this.dbUrl = ConfigServer.getConfig("ORDER_DB_URL");
    }

    public void start() {
        System.out.println("Connecting to DB at: " + dbUrl);
    }
}

public class Main {

    public static void main(String[] args) {

        OrderService orderService = new OrderService();
        orderService.start();
    }
}