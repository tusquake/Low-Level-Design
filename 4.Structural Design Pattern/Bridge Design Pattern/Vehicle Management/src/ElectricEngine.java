public class ElectricEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Electric engine: Silent start");
    }

    @Override
    public void stop() {
        System.out.println("Electric engine: Power down");
    }

    @Override
    public void accelerate() {
        System.out.println("Electric engine: Smooth acceleration");
    }
}
