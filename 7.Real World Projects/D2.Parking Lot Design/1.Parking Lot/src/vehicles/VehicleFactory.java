package vehicles;

public class VehicleFactory {

    public static Vehicle createVehicle(String vehicleType, String licensePlate){
        if(vehicleType.equalsIgnoreCase("Car")){
            return new CarVehicle(licensePlate,vehicleType);
        }else if(vehicleType.equalsIgnoreCase("Bike")){
            return new BikeVehicle(licensePlate,vehicleType);
        }
        return null;
    }

}
