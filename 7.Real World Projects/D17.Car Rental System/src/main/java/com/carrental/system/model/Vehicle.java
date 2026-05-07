package com.carrental.system.model;

import com.carrental.system.model.enums.VehicleType;
import lombok.Getter;
import lombok.Setter;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public abstract class Vehicle {
    private final String id;
    private final String licensePlate;
    private final double pricePerHour;
    private final VehicleType vehicleType;
    private final AtomicBoolean isBooked;

    public Vehicle(String id, String licensePlate, double pricePerHour, VehicleType vehicleType) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.pricePerHour = pricePerHour;
        this.vehicleType = vehicleType;
        this.isBooked = new AtomicBoolean(false);
    }

    public boolean reserve() {
        return isBooked.compareAndSet(false, true);
    }

    public void release() {
        isBooked.set(false);
    }

    public boolean isAvailable() {
        return !isBooked.get();
    }
}
