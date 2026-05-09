package com.flipmed.system.repository;

import com.flipmed.system.model.Appointment;
import com.flipmed.system.model.Patient;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AppointmentRepo {
    // Map<DoctorID + StartTime, Appointment>
    private final Map<String, Appointment> confirmedAppointments = new HashMap<>();
    
    // Map<DoctorID + StartTime, Queue<Patient>>
    private final Map<String, Queue<Patient>> waitlists = new HashMap<>();

    public String generateKey(String doctorId, String startTime) {
        return doctorId + ":" + startTime;
    }

    public void book(Appointment appointment) {
        String key = generateKey(appointment.getDoctor().getId(), 
                                 appointment.getTimeSlot().getStartTime().toString());
        confirmedAppointments.put(key, appointment);
    }

    public void addToWaitlist(String doctorId, String startTime, Patient patient) {
        String key = generateKey(doctorId, startTime);
        waitlists.computeIfAbsent(key, k -> new LinkedList<>()).offer(patient);
    }

    public Patient pollWaitlist(String doctorId, String startTime) {
        String key = generateKey(doctorId, startTime);
        Queue<Patient> queue = waitlists.get(key);
        return (queue != null) ? queue.poll() : null;
    }

    public Appointment getConfirmedAppointment(String doctorId, String startTime) {
        return confirmedAppointments.get(generateKey(doctorId, startTime));
    }

    public void removeConfirmed(String doctorId, String startTime) {
        confirmedAppointments.remove(generateKey(doctorId, startTime));
    }

    public Collection<Appointment> findAll() {
        return confirmedAppointments.values();
    }
}
