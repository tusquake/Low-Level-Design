package com.flipmed.system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlot {
    private LocalTime startTime;
    private LocalTime endTime;

    @Override
    public String toString() {
        return startTime + "-" + endTime;
    }
}
