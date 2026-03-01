interface PaymentPort {
    void processPayment(double amount);
}

class PaymentService {

    private PaymentPort paymentPort;

    public PaymentService(PaymentPort paymentPort) {
        this.paymentPort = paymentPort;
    }

    public void makePayment(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        System.out.println("Validating payment in domain layer");
        paymentPort.processPayment(amount);
    }
}

class StripePaymentAdapter implements PaymentPort {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment via Stripe: " + amount);
    }
}

class PayPalPaymentAdapter implements PaymentPort {

    @Override
    public void processPayment(double amount) {
        System.out.println("Processing payment via PayPal: " + amount);
    }
}

public class Main {

    public static void main(String[] args) {

        // Using Stripe
        PaymentPort stripeAdapter = new StripePaymentAdapter();
        PaymentService paymentService1 = new PaymentService(stripeAdapter);
        paymentService1.makePayment(500);

        System.out.println();

        // Switching to PayPal (no change in domain logic)
        PaymentPort paypalAdapter = new PayPalPaymentAdapter();
        PaymentService paymentService2 = new PaymentService(paypalAdapter);
        paymentService2.makePayment(1000);
    }
}