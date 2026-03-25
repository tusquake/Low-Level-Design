import java.util.ArrayList;
import java.util.List;

interface Vehicle{
    void getType();
}

class Bike implements Vehicle{

    @Override
    public void getType() {
        System.out.println("Bike Booked!");
    }
}

class Sedan implements Vehicle{

    @Override
    public void getType() {
        System.out.println("Sedan Booked");
    }
}

class VehicleFactory{
    public static Vehicle getVehicle(String type) {
        if(type.equalsIgnoreCase("BIKE")) return new Bike();
        if(type.equalsIgnoreCase("SEDAN")) return new Sedan();
        return null;
    }
}

interface PricingStrategy {
    double calculateFare(double distance);
}

class NormalPricing implements PricingStrategy {
    public double calculateFare(double distance) {
        return distance * 10;
    }
}

class SurgePricing implements PricingStrategy {
    public double calculateFare(double distance) {
        return distance * 20;
    }
}

interface PaymentStrategy {
    void pay(double amount);
}

class UpiPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via UPI: " + amount);
    }
}

class CardPayment implements PaymentStrategy {
    public void pay(double amount) {
        System.out.println("Paid via Card: " + amount);
    }
}

interface RideObserver {
    void update(String status);
}

class UserApp implements RideObserver {
    public void update(String status) {
        System.out.println("User notified: Ride " + status);
    }
}

class DriverApp implements RideObserver {
    public void update(String status) {
        System.out.println("Driver notified: Ride " + status);
    }
}

interface RideState {
    void next(Ride ride);
}

class RequestedState implements RideState {
    public void next(Ride ride) {
        System.out.println("Ride accepted");
        ride.setState(new OngoingState());
    }
}

class OngoingState implements RideState {
    public void next(Ride ride) {
        System.out.println("Ride completed");
        ride.setState(new CompletedState());
    }
}

class CompletedState implements RideState {
    public void next(Ride ride) {
        System.out.println("Ride already completed");
    }
}

class MatchingService {
    private static MatchingService instance = new MatchingService();

    private MatchingService(){}

    public static MatchingService getInstance() {
        return instance;v
    }

    public void findDriver() {
        System.out.println("Driver matched!");
    }
}

class Ride {
    private PricingStrategy pricingStrategy;
    private PaymentStrategy paymentStrategy;
    private RideState state;

    private List<RideObserver> observers = new ArrayList<>();

    public Ride(PricingStrategy pricingStrategy, PaymentStrategy paymentStrategy) {
        this.pricingStrategy = pricingStrategy;
        this.paymentStrategy = paymentStrategy;
        this.state = new RequestedState();
    }

    public void addObserver(RideObserver obs) {
        observers.add(obs);
    }

    public void notifyAllObservers(String msg) {
        for(RideObserver o : observers) {
            o.update(msg);
        }
    }

    public void setState(RideState state) {
        this.state = state;
    }

    public void nextState() {
        state.next(this);
    }

    public void startRide(double distance) {
        MatchingService.getInstance().findDriver();
        notifyAllObservers("started");

        double fare = pricingStrategy.calculateFare(distance);
        System.out.println("Fare: " + fare);

        paymentStrategy.pay(fare);
    }
}


public class Main {
    public static void main(String[] args) {

        // Factory
        Vehicle v = VehicleFactory.getVehicle("SEDAN");
        v.getType();

        // Strategies
        PricingStrategy pricing = new SurgePricing();
        PaymentStrategy payment = new UpiPayment();

        Ride ride = new Ride(pricing, payment);

        // Observer
        ride.addObserver(new UserApp());
        ride.addObserver(new DriverApp());

        // Start ride
        ride.startRide(10);

        // State change
        ride.nextState();
        ride.nextState();
        ride.nextState();
    }
}
