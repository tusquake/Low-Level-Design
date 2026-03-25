public class DieselEngine implements Engine {
    @Override
    public void start() {
        System.out.println("Diesel engine: Compression start");
    }

    @Override
    public void stop() {
        System.out.println("Diesel engine: Shutdown");
    }

    @Override
    public void accelerate() {
        System.out.println("Diesel engine: Turbo acceleration");
    }
}
