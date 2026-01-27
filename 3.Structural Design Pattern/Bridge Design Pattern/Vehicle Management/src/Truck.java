public class Truck extends Vehicle {
    public Truck(Engine engine) {
        super(engine);
    }

    @Override
    public void drive() {
        System.out.println("Truck: Starting heavy-duty drive");
        startEngine();
        accelerateEngine();
        System.out.println("Truck: Carrying heavy load");
    }

    @Override
    public void park() {
        System.out.println("Truck: Parking at loading dock");
        stopEngine();
    }
}

