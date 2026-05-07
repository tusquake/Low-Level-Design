package com.carrental.system.model;

import com.carrental.system.model.enums.VehicleType;

public class SUV extends Vehicle {
    public SUV(String id, String licensePlate, double pricePerHour) {
        super(id, licensePlate, pricePerHour, VehicleType.SUV);
    }
}
