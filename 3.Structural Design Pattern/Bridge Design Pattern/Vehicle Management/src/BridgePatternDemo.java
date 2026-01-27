public class BridgePatternDemo {
    public static void main(String[] args) {
        // Different combinations of vehicles and engines

        // Electric car
        Vehicle electricCar = new Car(new ElectricEngine());
        System.out.println("=== Electric Car ===");
        electricCar.drive();
        electricCar.park();

        System.out.println();

        // Diesel truck
        Vehicle dieselTruck = new Truck(new DieselEngine());
        System.out.println("=== Diesel Truck ===");
        dieselTruck.drive();
        dieselTruck.park();

        System.out.println();

        // Petrol motorcycle
        Vehicle petrolBike = new Motorcycle(new PetrolEngine());
        System.out.println("=== Petrol Motorcycle ===");
        petrolBike.drive();
        petrolBike.park();

        System.out.println();

        // Electric truck (new combination)
        Vehicle electricTruck = new Truck(new ElectricEngine());
        System.out.println("=== Electric Truck ===");
        electricTruck.drive();
        electricTruck.park();
    }
}