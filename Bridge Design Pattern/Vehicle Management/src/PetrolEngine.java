public class PetrolEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Petrol engine: Ignition start");
    }

    @Override
    public void stop() {
        System.out.println("Petrol engine: Engine off");
    }

    @Override
    public void accelerate() {
        System.out.println("Petrol engine: Revving up");
    }
}
