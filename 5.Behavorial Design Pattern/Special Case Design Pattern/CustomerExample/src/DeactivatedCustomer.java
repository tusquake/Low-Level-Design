public class DeactivatedCustomer implements Customer {
    private String name;

    public DeactivatedCustomer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getDiscount() {
        return 0.0; // Deactivated users get no discount
    }

    @Override
    public void showDashboard() {
        System.out.println("Your account is deactivated. Please contact support.");
    }
}
