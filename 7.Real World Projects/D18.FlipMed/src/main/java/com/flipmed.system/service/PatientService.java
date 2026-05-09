package com.flipmed.system.service;

import com.flipmed.system.model.Patient;
import com.flipmed.system.repository.PatientRepo;

public class PatientService {
    private final PatientRepo patientRepo;

    public PatientService(PatientRepo patientRepo) {
        this.patientRepo = patientRepo;
    }

    public Patient registerPatient(String id, String name) {
        Patient patient = Patient.builder().id(id).name(name).build();
        patientRepo.save(patient);
        return patient;
    }
}
