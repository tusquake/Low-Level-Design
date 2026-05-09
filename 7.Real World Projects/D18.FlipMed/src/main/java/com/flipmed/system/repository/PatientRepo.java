package com.flipmed.system.repository;

import com.flipmed.system.model.Patient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PatientRepo {
    private final Map<String, Patient> patients = new HashMap<>();

    public void save(Patient patient) {
        patients.put(patient.getId(), patient);
    }

    public Optional<Patient> findById(String id) {
        return Optional.ofNullable(patients.get(id));
    }
}
