package com.carrental.system.model;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Branch {
    private final String id;
    private final String name;
    private final String city;
    private final List<Vehicle> inventory = new ArrayList<>();

    public void addVehicle(Vehicle vehicle) {
        inventory.add(vehicle);
    }

    public void removeVehicle(Vehicle vehicle) {
        inventory.remove(vehicle);
    }
}
