class PaymentService {
    public void pay(String user, int amount) {
        System.out.println("Deducted " + amount + " from " + user);
    }
}


public class IdempotencyViolation {
    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        paymentService.pay("Tushar",1000);
        paymentService.pay("Tushar", 1000);
    }
}