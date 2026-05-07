package com.carrental.system.strategy;

import com.carrental.system.model.Vehicle;
import java.time.Duration;
import java.time.LocalDateTime;

public interface PricingStrategy {
    double calculatePrice(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime);
}
