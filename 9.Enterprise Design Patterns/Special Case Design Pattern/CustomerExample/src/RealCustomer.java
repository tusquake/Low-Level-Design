public class RealCustomer implements Customer {
    private String name;

    public RealCustomer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDiscount() {
        return 10.0; // 10% discount for real customers
    }

    @Override
    public void showDashboard() {
        System.out.println("Welcome back, " + name + "! Here are your active orders.");
    }
}
