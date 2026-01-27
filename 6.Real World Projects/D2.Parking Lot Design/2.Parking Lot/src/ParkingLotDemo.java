import java.util.*;
import java.time.LocalDateTime;
import java.time.Duration;

enum VehicleType {
    MOTORCYCLE,
    CAR,
    TRUCK,
    BUS
}

enum SpotType {
    COMPACT,
    REGULAR,
    LARGE,
    ELECTRIC
}

enum SpotStatus {
    AVAILABLE,
    OCCUPIED,
    RESERVED,
    OUT_OF_SERVICE
}

abstract class Vehicle {
    private String licensePlate;
    private VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}

class Car extends Vehicle {
    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}

class Truck extends Vehicle {
    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}

class Bus extends Vehicle {
    public Bus(String licensePlate) {
        super(licensePlate, VehicleType.BUS);
    }
}

class ParkingSpot {
    private String spotId;
    private SpotType type;
    private SpotStatus status;
    private Vehicle parkedVehicle;
    private int floor;

    public ParkingSpot(String spotId, SpotType type, int floor) {
        this.spotId = spotId;
        this.type = type;
        this.floor = floor;
        this.status = SpotStatus.AVAILABLE;
        this.parkedVehicle = null;
    }

    // Check if this spot can accommodate the vehicle
    public boolean canFitVehicle(Vehicle vehicle) {
        if (status != SpotStatus.AVAILABLE) {
            return false;
        }
!
        switch (vehicle.getType()) {
            case MOTORCYCLE:
                return true;
            case CAR:
                return type == SpotType.REGULAR || type == SpotType.LARGE || type == SpotType.ELECTRIC;
            case TRUCK:
                return type == SpotType.LARGE;
            case BUS:
                return type == SpotType.LARGE;
            default:
                return false;
        }
    }

    public boolean parkVehicle(Vehicle vehicle) {
        if (canFitVehicle(vehicle)) {
            this.parkedVehicle = vehicle;
            this.status = SpotStatus.OCCUPIED;
            return true;
        }
        return false;
    }

    public void removeVehicle() {
        this.parkedVehicle = null;
        this.status = SpotStatus.AVAILABLE;
    }

    public String getSpotId() { return spotId; }
    public SpotType getType() { return type; }
    public SpotStatus getStatus() { return status; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
    public int getFloor() { return floor; }

    @Override
    public String toString() {
        return "Spot[" + spotId + ", " + type + ", " + status + ", Floor:" + floor + "]";
    }
}

class ParkingTicket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot spot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amount;

    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
        this.amount = 0.0;
    }

    public long getParkingDuration() {
        LocalDateTime end = (exitTime != null) ? exitTime : LocalDateTime.now();
        return Duration.between(entryTime, end).toMinutes();
    }

    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return "Ticket[" + ticketId + ", " + vehicle.getLicensePlate() +
                ", Spot:" + spot.getSpotId() + ", Entry:" + entryTime + "]";
    }
}

interface ParkingStrategy {
    ParkingSpot findSpot(List<ParkingSpot> spots, Vehicle vehicle);
}

class NearestSpotStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingSpot> spots, Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.canFitVehicle(vehicle)) {
                return spot;
            }
        }
        return null;
    }
}

class LowestFloorStrategy implements ParkingStrategy {
    @Override
    public ParkingSpot findSpot(List<ParkingSpot> spots, Vehicle vehicle) {
        return spots.stream()
                .filter(spot -> spot.canFitVehicle(vehicle))
                .min(Comparator.comparingInt(ParkingSpot::getFloor))
                .orElse(null);
    }
}

interface PricingStrategy {
    double calculatePrice(ParkingTicket ticket);
}

class HourlyPricing implements PricingStrategy {
    private static final double RATE_PER_HOUR = 20.0;

    @Override
    public double calculatePrice(ParkingTicket ticket) {
        long minutes = ticket.getParkingDuration();
        double hours = Math.ceil(minutes / 60.0);
        return hours * RATE_PER_HOUR;
    }
}

class VehicleTypePricing implements PricingStrategy {
    @Override
    public double calculatePrice(ParkingTicket ticket) {
        long hours = (long) Math.ceil(ticket.getParkingDuration() / 60.0);

        switch (ticket.getVehicle().getType()) {
            case MOTORCYCLE: return hours * 10.0;
            case CAR:        return hours * 20.0;
            case TRUCK:      return hours * 40.0;
            case BUS:        return hours * 50.0;
            default:         return hours * 20.0;
        }
    }
}

class ParkingLot {
    private String name;
    private List<ParkingSpot> spots;
    private Map<String, ParkingTicket> activeTickets;
    private ParkingStrategy parkingStrategy;
    private PricingStrategy pricingStrategy;
    private int ticketCounter;

    public ParkingLot(String name, ParkingStrategy parkingStrategy, PricingStrategy pricingStrategy) {
        this.name = name;
        this.spots = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.parkingStrategy = parkingStrategy;
        this.pricingStrategy = pricingStrategy;
        this.ticketCounter = 1;
    }

    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }

    public ParkingTicket parkVehicle(Vehicle vehicle) {
        ParkingSpot spot = parkingStrategy.findSpot(spots, vehicle);

        if (spot == null) {
            System.out.println("Sorry, no available spots for " + vehicle.getType());
            return null;
        }

        spot.parkVehicle(vehicle);

        String ticketId = "TKT-" + String.format("%05d", ticketCounter++);
        ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot);
        activeTickets.put(ticketId, ticket);

        System.out.println("Vehicle parked successfully!");
        System.out.println("Ticket: " + ticketId + " | Spot: " + spot.getSpotId());

        return ticket;
    }

    public double exitVehicle(String ticketId) {
        ParkingTicket ticket = activeTickets.get(ticketId);

        if (ticket == null) {
            System.out.println("Invalid ticket!");
            return 0.0;
        }

        ticket.setExitTime(LocalDateTime.now());

        double amount = pricingStrategy.calculatePrice(ticket);
        ticket.setAmount(amount);

        ticket.getSpot().removeVehicle();

        activeTickets.remove(ticketId);

        System.out.println("\n--- Parking Bill ---");
        System.out.println("Ticket: " + ticketId);
        System.out.println("Vehicle: " + ticket.getVehicle().getLicensePlate());
        System.out.println("Duration: " + ticket.getParkingDuration() + " minutes");
        System.out.println("Amount: Rs. " + amount);
        System.out.println("--------------------\n");

        return amount;
    }

    public int getAvailableSpots() {
        return (int) spots.stream()
                .filter(spot -> spot.getStatus() == SpotStatus.AVAILABLE)
                .count();
    }

    public void displayStatus() {
        System.out.println("\n=== " + name + " Status ===");
        System.out.println("Total Spots: " + spots.size());
        System.out.println("Available: " + getAvailableSpots());
        System.out.println("Occupied: " + (spots.size() - getAvailableSpots()));
        System.out.println("Active Vehicles: " + activeTickets.size());
        System.out.println("========================\n");
    }
}

public class ParkingLotDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== PARKING LOT SYSTEM ===\n");

        ParkingLot lot = new ParkingLot(
                "City Center Parking",
                new NearestSpotStrategy(),
                new VehicleTypePricing()
        );

        lot.addSpot(new ParkingSpot("G-101", SpotType.COMPACT, 0));
        lot.addSpot(new ParkingSpot("G-102", SpotType.COMPACT, 0));

        lot.addSpot(new ParkingSpot("G-201", SpotType.REGULAR, 0));
        lot.addSpot(new ParkingSpot("G-202", SpotType.REGULAR, 0));
        lot.addSpot(new ParkingSpot("G-203", SpotType.REGULAR, 0));

        lot.addSpot(new ParkingSpot("F1-301", SpotType.LARGE, 1));
        lot.addSpot(new ParkingSpot("F1-302", SpotType.LARGE, 1));

        lot.displayStatus();

        System.out.println("--- Vehicles Entering ---");
        Vehicle bike1 = new Motorcycle("KA-01-1234");
        Vehicle car1 = new Car("KA-02-5678");
        Vehicle car2 = new Car("KA-03-9012");
        Vehicle truck1 = new Truck("KA-04-3456");

        ParkingTicket ticket1 = lot.parkVehicle(bike1);
        ParkingTicket ticket2 = lot.parkVehicle(car1);
        ParkingTicket ticket3 = lot.parkVehicle(car2);
        ParkingTicket ticket4 = lot.parkVehicle(truck1);

        lot.displayStatus();

        System.out.println("--- Simulating 2 hours parking ---");
        Thread.sleep(2000);

        System.out.println("--- Vehicles Exiting ---");
        lot.exitVehicle(ticket1.getTicketId());
        lot.exitVehicle(ticket2.getTicketId());

        lot.displayStatus();

        System.out.println("--- New Vehicle Entering ---");
        Vehicle car3 = new Car("KA-05-7890");
        ParkingTicket ticket5 = lot.parkVehicle(car3);

        lot.displayStatus();

        System.out.println("--- Remaining Vehicles Exiting ---");
        lot.exitVehicle(ticket3.getTicketId());
        lot.exitVehicle(ticket4.getTicketId());
        lot.exitVehicle(ticket5.getTicketId());

        lot.displayStatus();
    }
}

// ========================================
// DESIGN PATTERNS USED:
// ========================================
// 1. Strategy Pattern - ParkingStrategy, PricingStrategy
// 2. Factory Pattern - Can be added for Vehicle creation
// 3. Singleton Pattern - Can be used for ParkingLot
// 4. State Pattern - SpotStatus represents state
//
// SOLID PRINCIPLES:
// ========================================
// 1. SRP - Each class has single responsibility
// 2. OCP - Open for extension (new strategies), closed for modification
// 3. LSP - Vehicle subclasses are substitutable
// 4. ISP - Interfaces are specific to their use
// 5. DIP - Depends on abstractions (ParkingStrategy, PricingStrategy)
// ========================================