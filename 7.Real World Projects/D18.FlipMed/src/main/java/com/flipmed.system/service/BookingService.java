package com.flipmed.system.service;

import com.flipmed.system.exception.FlipMedException;
import com.flipmed.system.model.*;
import com.flipmed.system.repository.AppointmentRepo;
import com.flipmed.system.repository.DoctorRepo;
import com.flipmed.system.repository.PatientRepo;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingService {
    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;

    public BookingService(AppointmentRepo appointmentRepo, DoctorRepo doctorRepo, PatientRepo patientRepo) {
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    public void bookSlot(String patientId, String doctorId, String startTime) {
        Patient patient = patientRepo.findById(patientId).orElseThrow(() -> new FlipMedException("Patient not found"));
        Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new FlipMedException("Doctor not found"));

        // 1. Overlap Check for Patient
        boolean hasOverlap = appointmentRepo.findAll().stream()
                .filter(a -> a.getPatient().getId().equals(patientId) && a.getStatus() == Appointment.AppointmentStatus.CONFIRMED)
                .anyMatch(a -> a.getTimeSlot().getStartTime().toString().equals(startTime));

        if (hasOverlap) {
            throw new FlipMedException("Overlap detected! Patient already has a booking at " + startTime);
        }

        // 2. Check if Doctor has the slot available
        TimeSlot targetSlot = doctor.getAvailableSlots().stream()
                .filter(s -> s.getStartTime().toString().equals(startTime))
                .findFirst()
                .orElseThrow(() -> new FlipMedException("Slot not available for doctor"));

        // 3. Check if already booked
        Appointment existing = appointmentRepo.getConfirmedAppointment(doctorId, startTime);
        if (existing != null) {
            appointmentRepo.addToWaitlist(doctorId, startTime, patient);
            System.out.println("Slot already booked. Patient " + patient.getName() + " added to Waitlist.");
            return;
        }

        // 4. Confirm Booking
        Appointment appointment = Appointment.builder()
                .id(UUID.randomUUID().toString())
                .doctor(doctor)
                .patient(patient)
                .timeSlot(targetSlot)
                .status(Appointment.AppointmentStatus.CONFIRMED)
                .build();

        appointmentRepo.book(appointment);
        System.out.println("Booking confirmed for " + patient.getName() + " with Dr. " + doctor.getName());
    }

    public void cancelBooking(String doctorId, String startTime) {
        Appointment confirmed = appointmentRepo.getConfirmedAppointment(doctorId, startTime);
        if (confirmed == null) {
            throw new FlipMedException("No confirmed booking found for this slot");
        }

        System.out.println("Cancelling booking for " + confirmed.getPatient().getName());
        appointmentRepo.removeConfirmed(doctorId, startTime);

        // Process Waitlist
        Patient nextInLine = appointmentRepo.pollWaitlist(doctorId, startTime);
        if (nextInLine != null) {
            Appointment newBooking = Appointment.builder()
                    .id(UUID.randomUUID().toString())
                    .doctor(confirmed.getDoctor())
                    .patient(nextInLine)
                    .timeSlot(confirmed.getTimeSlot())
                    .status(Appointment.AppointmentStatus.CONFIRMED)
                    .build();
            appointmentRepo.book(newBooking);
            System.out.println("Waitlist Alert: Slot automatically booked for " + nextInLine.getName());
        }
    }

    public void viewPatientAppointments(String patientId) {
        System.out.println("\nAppointments for Patient ID: " + patientId);
        appointmentRepo.findAll().stream()
                .filter(a -> a.getPatient().getId().equals(patientId))
                .forEach(a -> System.out.println(a.getDoctor().getName() + " [" + a.getTimeSlot() + "]"));
    }

    public void viewDoctorAppointments(String doctorId) {
        System.out.println("\nAppointments for Doctor ID: " + doctorId);
        appointmentRepo.findAll().stream()
                .filter(a -> a.getDoctor().getId().equals(doctorId))
                .forEach(a -> System.out.println(a.getPatient().getName() + " [" + a.getTimeSlot() + "]"));
    }
}
