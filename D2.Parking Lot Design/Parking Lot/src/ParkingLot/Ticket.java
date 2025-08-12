package ParkingLot;

import vehicles.Vehicle;

import java.time.LocalDateTime;

public class Ticket {

    private ParkingSpot parkingSpot;

    private Vehicle vehicle;

    private LocalDateTime startTime;

    public Ticket(ParkingSpot parkingSpot, Vehicle vehicle){
        this.parkingSpot = parkingSpot;
        this.vehicle = vehicle;
        this.startTime = LocalDateTime.now();
    }
}
