package com.flipmed.system.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Appointment {
    private String id;
    private Doctor doctor;
    private Patient patient;
    private TimeSlot timeSlot;
    private AppointmentStatus status;

    public enum AppointmentStatus {
        CONFIRMED,
        WAITLISTED,
        CANCELLED
    }
}
