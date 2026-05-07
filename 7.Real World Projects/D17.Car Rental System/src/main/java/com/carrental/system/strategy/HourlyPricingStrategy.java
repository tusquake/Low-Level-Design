package com.carrental.system.strategy;

import com.carrental.system.model.Vehicle;
import java.time.Duration;
import java.time.LocalDateTime;

public class HourlyPricingStrategy implements PricingStrategy {
    @Override
    public double calculatePrice(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {
        long minutes = Duration.between(startTime, endTime).toMinutes();
        // Round up logic: e.g. 43 minutes = 1 hour, 61 minutes = 2 hours
        long hours = (long) Math.ceil(minutes / 60.0);
        return hours * vehicle.getPricePerHour();
    }
}
