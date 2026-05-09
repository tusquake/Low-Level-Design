package com.flipmed.system.repository;

import com.flipmed.system.model.Doctor;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DoctorRepo {
    private final Map<String, Doctor> doctors = new HashMap<>();

    public void save(Doctor doctor) {
        doctors.put(doctor.getId(), doctor);
    }

    public Optional<Doctor> findById(String id) {
        return Optional.ofNullable(doctors.get(id));
    }

    public Collection<Doctor> findAll() {
        return doctors.values();
    }
}
