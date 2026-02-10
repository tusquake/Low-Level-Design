class Engine {
    public void start() {
        System.out.println("Engine started");
    }
}

class PetrolEngine extends Engine {
    public void start() {
        System.out.println("Petrol engine started");
    }
}

class DieselEngine extends Engine {
    public void start() {
        System.out.println("Diesel engine started");
    }
}

class ElectricEngine extends Engine {
    public void start() {
        System.out.println("Electric engine started");
    }
}

class Car extends PetrolEngine {
    public void drive() {
        System.out.println("Driving car");
    }
}

public class badDesign{
    public static void main(String[] args) {
        Car car = new Car();
        car.start();
        car.drive();
    }
}
