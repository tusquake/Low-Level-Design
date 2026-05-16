public class VehicleFactory {
    public static Vehicle getVehicle(String type) {
        if ("CAR".equalsIgnoreCase(type)) {
            return new Car();
        }
        // Instead of returning null, return a Null Object
        return new NullVehicle();
    }
}
