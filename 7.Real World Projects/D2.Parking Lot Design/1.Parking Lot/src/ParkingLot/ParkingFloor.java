package ParkingLot;

import vehicles.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {

    List<ParkingSpot> parkingSpots;

    int floorNumber;

    public ParkingFloor(int floorNumber,int numberOfCarSpots, int numberOfBikeSpots ){
        this.floorNumber = floorNumber;
        this.parkingSpots = new ArrayList<>();

        //Add Spot for cars
        for(int i=0; i< numberOfCarSpots;i++){
            this.parkingSpots.add(new CarParkingSpot(i + 1, "Car"));
        }

        //Add Spot for bikes
        for(int i=0; i< numberOfCarSpots;i++){
            this.parkingSpots.add(new BikeParkingSpot(i + 1, "Bike"));
        }
    }

    public ParkingSpot findAvailableSpot(String VehicleType){
        for(ParkingSpot spot: parkingSpots){
            if(!spot.isOccupied() && spot.getSpotType().equalsIgnoreCase(VehicleType)){
                return spot;
            }
        }
        return null;
    }

    public List<ParkingSpot> getParkingSpots() {
        return parkingSpots;
    }

}
