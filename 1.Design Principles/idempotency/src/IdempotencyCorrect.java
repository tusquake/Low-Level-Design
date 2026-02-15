import java.util.HashSet;
import java.util.Set;

class PaymenttService {

    private Set<String> processed = new HashSet<>();

    public void pay(String requestId, String user, int amount) {

        if(processed.contains(requestId)) {
            System.out.println("Payment already processed");
            return;
        }

        System.out.println("Deducted " + amount + " from " + user);
        processed.add(requestId);
    }
}



public class IdempotencyCorrect {
    public static void main(String[] args) {
        PaymenttService service = new PaymenttService();

        service.pay("req123", "Tushar", 1000);
        service.pay("req123", "Tushar", 1000); // duplicate
    }
}
