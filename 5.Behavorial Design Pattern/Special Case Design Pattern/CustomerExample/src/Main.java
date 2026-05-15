public class Main {
    public static void main(String[] args) {
        Customer c1 = CustomerService.getCustomer("101"); // Real
        Customer c2 = CustomerService.getCustomer("103"); // Deactivated
        Customer c3 = CustomerService.getCustomer("999"); // Unknown

        displayCustomerInfo(c1);
        displayCustomerInfo(c2);
        displayCustomerInfo(c3);
    }

    private static void displayCustomerInfo(Customer customer) {
        // The calling code is simple and polymorphic.
        // It doesn't care if it's a real, unknown, or deactivated customer.
        System.out.println("Customer Name: " + customer.getName());
        System.out.println("Applied Discount: " + customer.getDiscount() + "%");
        customer.showDashboard();
        System.out.println("-------------------------------------------");
    }
}
