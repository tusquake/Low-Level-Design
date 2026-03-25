class PaymentGateway {

    public void charge(double amount) {
        throw new RuntimeException("Payment gateway is down!");
    }
}

class PaymentService {

    private PaymentGateway gateway = new PaymentGateway();

    public void process(double amount) {

        try {
            gateway.charge(amount);
            System.out.println("Payment successful: " + amount);
        } catch (Exception e) {

            // Fail Safe fallback
            System.out.println("Gateway failed. Marking payment as PENDING.");
            saveAsPending(amount);
        }
    }

    private void saveAsPending(double amount) {
        System.out.println("Saved payment as pending for amount: " + amount);
    }
}

public class FailSafe {

    public static void main(String[] args) {

        PaymentService service = new PaymentService();
        service.process(1000);
    }
}