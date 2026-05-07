package com.carrental.system.factory;

import com.carrental.system.model.Car;
import com.carrental.system.model.SUV;
import com.carrental.system.model.Vehicle;
import com.carrental.system.model.enums.VehicleType;

public class VehicleFactory {
    public static Vehicle createVehicle(String id, String licensePlate, double pricePerHour, VehicleType type) {
        return switch (type) {
            case CAR -> new Car(id, licensePlate, pricePerHour);
            case SUV -> new SUV(id, licensePlate, pricePerHour);
            default -> throw new IllegalArgumentException("Unknown vehicle type");
        };
    }
}
