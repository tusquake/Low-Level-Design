// Engine interface
interface Engines {
    void start();
}

// Concrete implementations
class PetrolEngines implements Engines {
    public void start() {
        System.out.println("Petrol engine started");
    }
}

class DieselEngines implements Engines {
    public void start() {
        System.out.println("Diesel engine started");
    }
}

class ElectricEngines implements Engines {
    public void start() {
        System.out.println("Electric engine started");
    }
}

class Cars {
    private Engines engine; // has-a

    public Cars(Engines engine) {
        this.engine = engine;
    }

    public void drive() {
        engine.start();
        System.out.println("Car is driving");
    }
}


public class GoodDesign {
    public static void main(String[] args) {
        Cars cars = new Cars(new ElectricEngines());
        cars.drive();
    }
}

