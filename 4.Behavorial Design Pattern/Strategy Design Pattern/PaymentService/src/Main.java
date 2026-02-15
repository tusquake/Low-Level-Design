interface PaymentStrategy {
    void pay(int amount);
}

class UpiPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

class CardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Card");
    }
}

class CashPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Cash");
    }
}

class PaymentService {

    private PaymentStrategy strategy;

    public PaymentService(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void makePayment(int amount) {
        strategy.pay(amount);
    }
}

public class Main {
    public static void main(String[] args) {

        PaymentStrategy strategy = new UpiPayment();
        PaymentService service = new PaymentService(strategy);
        service.makePayment(1000);

        strategy = new CardPayment();
        service = new PaymentService(strategy);
        service.makePayment(2000);
    }
}


