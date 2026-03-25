interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class UPIPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

class PaymentService{
    PaymentStrategy paymentStrategy;

    public PaymentService(PaymentStrategy paymentStrategy){
        this.paymentStrategy = paymentStrategy;
    }

    public void makePayment(int amount){
        paymentStrategy.pay(amount);
    }
}

public class StategyDemo{
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService(new UPIPayment());
        paymentService.makePayment(500);

        paymentService = new PaymentService(new CreditCardPayment());
        paymentService.makePayment(600);
    }
}

