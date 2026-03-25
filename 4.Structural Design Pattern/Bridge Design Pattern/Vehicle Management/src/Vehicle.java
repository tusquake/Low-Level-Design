public abstract class Vehicle {
    protected Engine engine;

    public Vehicle(Engine engine) {
        this.engine = engine;
    }

    public abstract void drive();
    public abstract void park();

    // Bridge methods
    protected void startEngine() {
        engine.start();
    }

    protected void stopEngine() {
        engine.stop();
    }

    protected void accelerateEngine() {
        engine.accelerate();
    }
}
