public class Main {
    public static void main(String[] args) {
        Vehicle car = VehicleFactory.getVehicle("CAR");
        Vehicle unknown = VehicleFactory.getVehicle("BIKE");

        printVehicleDetails(car);
        printVehicleDetails(unknown);
    }

    private static void printVehicleDetails(Vehicle vehicle) {
        // No null check needed!
        System.out.println("Vehicle Tank Capacity: " + vehicle.getTankCapacity());
        System.out.println("Vehicle Seating Capacity: " + vehicle.getSeatingCapacity());
        System.out.println("------------------------------------");
    }
}
