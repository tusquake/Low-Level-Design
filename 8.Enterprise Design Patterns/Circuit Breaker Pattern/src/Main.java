class CircuitBreaker {

    private int failureCount = 0;
    private final int failureThreshold = 3;

    private final long openTimeout = 3000; // 3 seconds
    private long lastFailureTime = 0;

    private State state = State.CLOSED;

    enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    public String callExternalService() {

        if (state == State.OPEN) {

            if (System.currentTimeMillis() - lastFailureTime > openTimeout) {
                state = State.HALF_OPEN;
                System.out.println("Circuit moved to HALF_OPEN");
            } else {
                return "Service unavailable (Circuit OPEN)";
            }
        }

        try {
            String response = externalServiceCall();

            reset();
            return response;

        } catch (RuntimeException e) {

            recordFailure();
            return "Service call failed";
        }
    }

    private String externalServiceCall() {

        try {
            System.out.println("Calling external service...");
            Thread.sleep(3000); // simulate 3 sec delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        throw new RuntimeException("Service Down");

        // For success simulation:
        // return "Success Response";
    }

    private void recordFailure() {
        failureCount++;
        lastFailureTime = System.currentTimeMillis();

        if (failureCount >= failureThreshold) {
            state = State.OPEN;
            System.out.println("Circuit moved to OPEN");
        }
    }

    private void reset() {
        failureCount = 0;
        state = State.CLOSED;
        System.out.println("Circuit moved to CLOSED");
    }
}

class PaymentService {

    private CircuitBreaker circuitBreaker;

    public PaymentService(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    public void processPayment() {
        System.out.println(circuitBreaker.callExternalService());
        System.out.println("-----------------------------");
    }
}

public class Main {

    public static void main(String[] args) throws InterruptedException {

        CircuitBreaker circuitBreaker = new CircuitBreaker();
        PaymentService paymentService = new PaymentService(circuitBreaker);

        // 3 failures -> should OPEN
        paymentService.processPayment();
        paymentService.processPayment();
        paymentService.processPayment();

        // This call should immediately fail (OPEN state)
        paymentService.processPayment();

        // Wait 4 seconds to allow HALF_OPEN transition
        Thread.sleep(4000);

        // Try again after timeout
        paymentService.processPayment();
    }
}