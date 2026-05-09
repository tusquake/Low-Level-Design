package com.flipmed.system;

import com.flipmed.system.model.TimeSlot;
import com.flipmed.system.model.enums.Specialization;
import com.flipmed.system.repository.*;
import com.flipmed.system.service.*;
import com.flipmed.system.strategy.*;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class FlipMedDemo {
    public static void main(String[] args) {
        // 1. Initialize Infrastructure
        DoctorRepo doctorRepo = new DoctorRepo();
        PatientRepo patientRepo = new PatientRepo();
        AppointmentRepo appointmentRepo = new AppointmentRepo();

        DoctorService doctorService = new DoctorService(doctorRepo);
        PatientService patientService = new PatientService(patientRepo);
        SearchService searchService = new SearchService(doctorRepo);
        BookingService bookingService = new BookingService(appointmentRepo, doctorRepo, patientRepo);

        System.out.println("--- FlipMed Doctor Booking System Demo ---");

        // 2. Register Doctors and Patients
        doctorService.registerDoctor("D1", "Curious", Specialization.CARDIOLOGIST, 4.5);
        doctorService.registerDoctor("D2", "Dreadful", Specialization.DERMATOLOGIST, 3.8);
        doctorService.registerDoctor("D3", "Daredevil", Specialization.CARDIOLOGIST, 4.9);

        patientService.registerPatient("P1", "PatientA");
        patientService.registerPatient("P2", "PatientB");
        patientService.registerPatient("P3", "PatientC");

        // 3. Declare Slots
        System.out.println("\nStep 1: Declaring availability...");
        doctorService.addAvailability("D1", Arrays.asList(
                new TimeSlot(LocalTime.of(9, 30), LocalTime.of(10, 0)),
                new TimeSlot(LocalTime.of(10, 30), LocalTime.of(11, 0))
        ));
        doctorService.addAvailability("D3", Arrays.asList(
                new TimeSlot(LocalTime.of(9, 30), LocalTime.of(10, 0))
        ));

        // 4. Search and Sort
        System.out.println("\nStep 2: Searching for Cardiologists (Sorted by Rating DESC)");
        List<DoctorSearchItem> cardiologists = searchService.searchBySpecialization(
                Specialization.CARDIOLOGIST, new RatingRankingStrategy());
        
        cardiologists.forEach(item -> 
            System.out.println("Dr. " + item.getDoctor().getName() + 
                               " | Rating: " + item.getDoctor().getRating() + 
                               " | Slot: " + item.getTimeSlot())
        );

        // 5. Booking Flow
        System.out.println("\nStep 3: Booking initial slots...");
        bookingService.bookSlot("P1", "D3", "09:30"); // P1 books D3 at 9:30

        // 6. Overlap Scenario
        System.out.println("\nStep 4: Demonstrating Overlap Scenario (P1 trying to book another doctor at same time)");
        try {
            bookingService.bookSlot("P1", "D1", "09:30"); 
        } catch (Exception e) {
            System.out.println("Expected Error: " + e.getMessage());
        }

        // 7. Waitlist Scenario
        System.out.println("\nStep 5: Demonstrating Waitlist (P2 and P3 trying to book Dr. Curious at 10:30)");
        bookingService.bookSlot("P2", "D1", "10:30"); // Confirmed
        bookingService.bookSlot("P3", "D1", "10:30"); // Waitlisted

        // 8. Cancellation and Waitlist Promotion
        System.out.println("\nStep 6: Cancellation triggering Waitlist Promotion");
        bookingService.cancelBooking("D1", "10:30"); // P2 cancels, P3 should get it

        // 9. View Final Appointments
        System.out.println("\nStep 7: Viewing final appointments...");
        bookingService.viewPatientAppointments("P3");
        bookingService.viewDoctorAppointments("D1");
    }
}
