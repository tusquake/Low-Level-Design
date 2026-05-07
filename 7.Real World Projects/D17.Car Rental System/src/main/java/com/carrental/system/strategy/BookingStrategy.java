package com.carrental.system.strategy;

import com.carrental.system.model.Vehicle;
import java.util.List;

public interface BookingStrategy {
    Vehicle selectVehicle(List<Vehicle> availableVehicles);
}
