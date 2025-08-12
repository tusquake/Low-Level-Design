package Payment;

public class UPIPayment implements PaymentStrategy{
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Upi payment of $" + amount);
    }
}
