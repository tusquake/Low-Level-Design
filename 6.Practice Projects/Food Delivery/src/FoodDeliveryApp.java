interface Order{
    String getDescription();
    double getCost();
    void placeOrder();
}

class ModernRestaurantOrder implements Order{
    private String itemName;
    private double price;

    public ModernRestaurantOrder(String itemName, double price) {
        this.itemName = itemName;
        this.price = price;
    }

    @Override
    public String getDescription() {
        return itemName;
    }

    @Override
    public double getCost() {
        return price;
    }

    @Override
    public void placeOrder() {
        System.out.println("Order placed through Modern API");
    }
}

class LegacyRestaurantSystem {
    public void submitFoodRequest(String food, double amount) {
        System.out.println("Legacy System: Received order for " + food + " - $" + amount);
    }

    public String getFoodName() {
        return "Legacy Food Item";
    }

    public double getFoodPrice() {
        return 15.0;
    }
}

class LegacyRestaurantAdapter implements Order {
    private LegacyRestaurantSystem legacySystem;
    private String itemName;
    private double price;

    public LegacyRestaurantAdapter(LegacyRestaurantSystem legacySystem,
                                   String itemName, double price) {
        this.legacySystem = legacySystem;
        this.itemName = itemName;
        this.price = price;
    }

    @Override
    public String getDescription() {
        return itemName;
    }

    @Override
    public double getCost() {
        return price;
    }

    @Override
    public void placeOrder() {
        // Adapt our interface to legacy interface
        legacySystem.submitFoodRequest(itemName, price);
    }
}

abstract class OrderDecorator implements Order {
    protected Order order;

    public OrderDecorator(Order order) {
        this.order = order;
    }

    @Override
    public String getDescription() {
        return order.getDescription();
    }

    @Override
    public double getCost() {
        return order.getCost();
    }

    @Override
    public void placeOrder() {
        order.placeOrder();
    }
}

// Add Gift Wrapping
class GiftWrapDecorator extends OrderDecorator {
    public GiftWrapDecorator(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Gift Wrap";
    }

    @Override
    public double getCost() {
        return order.getCost() + 2.0;
    }

    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Adding gift wrapping");
    }
}

// Add Priority Delivery
class PriorityDeliveryDecorator extends OrderDecorator {
    public PriorityDeliveryDecorator(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Priority Delivery";
    }

    @Override
    public double getCost() {
        return order.getCost() + 5.0;
    }

    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Marking as priority delivery (30 mins)");
    }
}

// Add Insurance
class InsuranceDecorator extends OrderDecorator {
    public InsuranceDecorator(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Insurance";
    }

    @Override
    public double getCost() {
        return order.getCost() + 1.5;
    }

    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Adding order insurance");
    }
}

// Add Contactless Delivery
class ContactlessDecorator extends OrderDecorator {
    public ContactlessDecorator(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + " + Contactless";
    }

    @Override
    public double getCost() {
        return order.getCost(); // Free
    }

    @Override
    public void placeOrder() {
        order.placeOrder();
        System.out.println("  → Enabled contactless delivery");
    }
}

public class FoodDeliveryApp {
    public static void main(String[] args) {

        System.out.println("=== ORDER 1: Modern Restaurant ===");
        // Order from modern restaurant
        Order order1 = new ModernRestaurantOrder("Chicken Biryani", 12.0);
        System.out.println("Base: " + order1.getDescription() + " = $" + order1.getCost());

        // Add gift wrap
        order1 = new GiftWrapDecorator(order1);
        System.out.println("After: " + order1.getDescription() + " = $" + order1.getCost());

        // Add priority delivery
        order1 = new PriorityDeliveryDecorator(order1);
        System.out.println("Final: " + order1.getDescription() + " = $" + order1.getCost());

        order1.placeOrder();


        System.out.println("\n=== ORDER 2: Legacy Restaurant (Using Adapter) ===");
        // Order from legacy restaurant through adapter
        LegacyRestaurantSystem legacySystem = new LegacyRestaurantSystem();
        Order order2 = new LegacyRestaurantAdapter(legacySystem, "Butter Chicken", 15.0);
        System.out.println("Base: " + order2.getDescription() + " = $" + order2.getCost());

        // Add insurance
        order2 = new InsuranceDecorator(order2);
        System.out.println("After: " + order2.getDescription() + " = $" + order2.getCost());

        // Add contactless
        order2 = new ContactlessDecorator(order2);
        System.out.println("Final: " + order2.getDescription() + " = $" + order2.getCost());

        order2.placeOrder();


        System.out.println("\n=== ORDER 3: Complex Order ===");
        // Complex order with all features
        Order order3 = new ContactlessDecorator(
                new InsuranceDecorator(
                        new PriorityDeliveryDecorator(
                                new GiftWrapDecorator(
                                        new ModernRestaurantOrder("Paneer Tikka", 10.0)
                                )
                        )
                )
        );

        System.out.println("Description: " + order3.getDescription());
        System.out.println("Total Cost: $" + order3.getCost());
        order3.placeOrder();
    }
}
