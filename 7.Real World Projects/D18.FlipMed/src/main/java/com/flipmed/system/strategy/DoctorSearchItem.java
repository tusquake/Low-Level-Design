package com.flipmed.system.strategy;

import com.flipmed.system.model.Doctor;
import com.flipmed.system.model.TimeSlot;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoctorSearchItem {
    private Doctor doctor;
    private TimeSlot timeSlot;
}
