package vehicles;

public class CarVehicle extends Vehicle{

    private static final double RATE = 10.0;

    public CarVehicle(String licensePlate, String vehicleType) {
        super(licensePlate, "Car");
    }

    @Override
    public double calculateFee(int hoursStayed) {
        return hoursStayed * RATE;
    }
}
