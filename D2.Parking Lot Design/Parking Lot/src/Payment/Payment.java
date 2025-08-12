package Payment;

public class Payment {
    private double amount;

    private PaymentStrategy paymentStrategy;

    public Payment(double amount, PaymentStrategy paymentStrategy){
        this.amount = amount;
        this.paymentStrategy = paymentStrategy;
    }

    public void processPaymentt(){
        if(amount>0){
            paymentStrategy.processPayment(amount);
        } else {
            System.out.println("Invalid payment amount");
        }
    }
}
