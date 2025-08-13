package gates;

import ParkingLot.ParkingLot;
import ParkingLot.ParkingSpot;
import vehicles.Vehicle;
import vehicles.VehicleFactory;

import java.util.Scanner;

public class EntranceGate {

    private ParkingLot parkingLot;

    public EntranceGate(ParkingLot parkingLot){
        this.parkingLot = parkingLot;
    }

    public void processEntrance(){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the Vehicle License plate: ");
        String licensePlate = sc.next();
        System.out.println("Enter the Vehicle type(Car or Bike): ");
        String vehicleType = sc.next();

        Vehicle vehicle = VehicleFactory.createVehicle(vehicleType, licensePlate);
        if(vehicle == null){
            System.out.println("Invalid vehicle type! Only Car and Bike are allowed");
            return;
        }

        ParkingSpot spot = parkingLot.parkVehicle(vehicle);
        if(spot!= null){
            System.out.println("Vehicle parked successfully in spot:"+spot.getSpotNumber());
        }else{
            System.out.println("No available spots for the vehicle type");
        }
    }
}
