public class Car extends Vehicle {
    public Car(Engine engine) {
        super(engine);
    }

    @Override
    public void drive() {
        System.out.println("Car: Starting to drive");
        startEngine();
        accelerateEngine();
        System.out.println("Car: Driving on road");
    }

    @Override
    public void park() {
        System.out.println("Car: Parking in garage");
        stopEngine();
    }
}
