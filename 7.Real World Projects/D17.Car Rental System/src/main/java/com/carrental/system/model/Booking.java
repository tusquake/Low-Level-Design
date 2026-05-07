package com.carrental.system.model;

import com.carrental.system.model.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    private final String id;
    private final User user;
    private final Vehicle vehicle;
    private final Branch pickupBranch;
    private final Branch dropBranch;
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private double totalAmount;
    private BookingStatus status;
}
