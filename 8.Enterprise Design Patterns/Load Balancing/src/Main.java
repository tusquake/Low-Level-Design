import java.util.Arrays;
import java.util.List;

class PaymentService {

    private String instanceName;

    public PaymentService(String instanceName) {
        this.instanceName = instanceName;
    }

    public void process(double amount) {
        System.out.println("Processed " + amount + " by " + instanceName);
    }

    public String getInstanceName() {
        return instanceName;
    }
}

// Strategy
interface LoadBalancerStrategy {
    PaymentService getNextInstance();
}

// Round Robin Strategy
class RoundRobinStrategy implements LoadBalancerStrategy {

    private List<PaymentService> instances;
    private int currentIndex = 0;

    public RoundRobinStrategy(List<PaymentService> instances) {
        this.instances = instances;
    }

    @Override
    public PaymentService getNextInstance() {
        PaymentService instance = instances.get(currentIndex);
        currentIndex = (currentIndex + 1) % instances.size();
        return instance;
    }
}

// IP Hash Strategy (example variation)
class IPLoadBalancerStrategy implements LoadBalancerStrategy {

    private List<PaymentService> instances;

    public IPLoadBalancerStrategy(List<PaymentService> instances) {
        this.instances = instances;
    }

    @Override
    public PaymentService getNextInstance() {
        int randomIndex = (int) (Math.random() * instances.size());
        return instances.get(randomIndex);
    }
}

// Context
class LoadBalancer {

    private LoadBalancerStrategy strategy;

    public LoadBalancer(LoadBalancerStrategy strategy) {
        this.strategy = strategy;
    }

    public PaymentService getInstance() {
        return strategy.getNextInstance();
    }
}

public class Main {

    public static void main(String[] args) {

        PaymentService p1 = new PaymentService("Instance-1");
        PaymentService p2 = new PaymentService("Instance-2");
        PaymentService p3 = new PaymentService("Instance-3");

        // Using Round Robin
        LoadBalancer roundRobinLB =
                new LoadBalancer(
                        new RoundRobinStrategy(Arrays.asList(p1, p2, p3)));

        for (int i = 0; i < 6; i++) {
            roundRobinLB.getInstance()
                    .process(Math.round(Math.random() * 1000));
        }

        System.out.println("---- Switching Strategy ----");

        // Switching to IP Strategy
        LoadBalancer ipLB =
                new LoadBalancer(
                        new IPLoadBalancerStrategy(Arrays.asList(p1, p2, p3)));

        for (int i = 0; i < 6; i++) {
            ipLB.getInstance()
                    .process(Math.round(Math.random() * 1000));
        }
    }
}