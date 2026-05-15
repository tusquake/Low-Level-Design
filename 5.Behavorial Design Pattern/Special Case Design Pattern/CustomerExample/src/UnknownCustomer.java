public class UnknownCustomer implements Customer {
    @Override
    public String getName() {
        return "Guest User";
    }

    @Override
    public double getDiscount() {
        return 0.0; // No discount for guests
    }

    @Override
    public void showDashboard() {
        System.out.println("Welcome Guest! Please log in to see your orders.");
    }
}
