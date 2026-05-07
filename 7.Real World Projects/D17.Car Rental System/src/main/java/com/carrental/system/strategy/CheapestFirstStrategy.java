package com.carrental.system.strategy;

import com.carrental.system.model.Vehicle;
import java.util.Comparator;
import java.util.List;

public class CheapestFirstStrategy implements BookingStrategy {
    @Override
    public Vehicle selectVehicle(List<Vehicle> availableVehicles) {
        return availableVehicles.stream()
                .min(Comparator.comparingDouble(Vehicle::getPricePerHour))
                .orElse(null);
    }
}
