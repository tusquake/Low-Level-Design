public class Motorcycle extends Vehicle {
    public Motorcycle(Engine engine) {
        super(engine);
    }

    @Override
    public void drive() {
        System.out.println("Motorcycle: Quick start");
        startEngine();
        accelerateEngine();
        System.out.println("Motorcycle: Weaving through traffic");
    }

    @Override
    public void park() {
        System.out.println("Motorcycle: Compact parking");
        stopEngine();
    }
}