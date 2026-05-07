package com.carrental.system.model;

import com.carrental.system.model.enums.VehicleType;

public class Car extends Vehicle {
    public Car(String id, String licensePlate, double pricePerHour) {
        super(id, licensePlate, pricePerHour, VehicleType.CAR);
    }
}
