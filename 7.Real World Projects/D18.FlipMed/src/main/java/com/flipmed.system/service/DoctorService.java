package com.flipmed.system.service;

import com.flipmed.system.model.Doctor;
import com.flipmed.system.model.TimeSlot;
import com.flipmed.system.model.enums.Specialization;
import com.flipmed.system.repository.DoctorRepo;
import java.util.List;

public class DoctorService {
    private final DoctorRepo doctorRepo;

    public DoctorService(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public Doctor registerDoctor(String id, String name, Specialization spec, double rating) {
        Doctor doctor = Doctor.builder()
                .id(id)
                .name(name)
                .specialization(spec)
                .rating(rating)
                .build();
        doctorRepo.save(doctor);
        return doctor;
    }

    public void addAvailability(String doctorId, List<TimeSlot> slots) {
        doctorRepo.findById(doctorId).ifPresent(doctor -> {
            doctor.getAvailableSlots().addAll(slots);
            doctorRepo.save(doctor);
        });
    }

    public List<Doctor> getAllDoctors() {
        return List.copyOf(doctorRepo.findAll());
    }
}
