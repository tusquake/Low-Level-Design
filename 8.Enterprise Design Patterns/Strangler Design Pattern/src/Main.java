class OldPaymentSystem {

    public void process(double amount) {
        System.out.println("Processing payment in LEGACY system: " + amount);
    }
}

class NewPaymentService {

    public void process(double amount) {
        System.out.println("Processing payment in NEW microservice: " + amount);
    }
}

class PaymentRouter {

    private OldPaymentSystem oldSystem = new OldPaymentSystem();
    private NewPaymentService newService = new NewPaymentService();

    public void processPayment(double amount) {

        if (amount > 1000) {
            // gradually moving high value transactions to new system
            newService.process(amount);
        } else {
            oldSystem.process(amount);
        }
    }
}

public class Main {

    public static void main(String[] args) {

        PaymentRouter router = new PaymentRouter();

        router.processPayment(500);   // legacy
        router.processPayment(2000);  // new system
    }
}