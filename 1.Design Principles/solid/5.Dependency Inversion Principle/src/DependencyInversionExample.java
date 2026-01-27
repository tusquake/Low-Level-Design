public class DependencyInversionExample {
    public static void main(String[] args) {
        Payment p = new UPI();
        PaymentService service = new PaymentService(p);
        service.makePayment();
    }
}

class PaymentService {
    private Payment payment;

    public PaymentService(Payment payment) {
        this.payment = payment;
    }

    public void makePayment() {
        payment.pay();
    }
}

interface Payment{
    void pay();
}

class COD implements Payment{

    @Override
    public void pay() {
        System.out.println("Paying though Cash");
    }
}

class UPI implements Payment{

    @Override
    public void pay() {
        System.out.println("Paying through UPI");
    }
}