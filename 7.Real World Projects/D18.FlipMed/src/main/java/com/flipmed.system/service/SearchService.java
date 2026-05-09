package com.flipmed.system.service;

import com.flipmed.system.model.Doctor;
import com.flipmed.system.model.enums.Specialization;
import com.flipmed.system.repository.DoctorRepo;
import com.flipmed.system.strategy.DoctorSearchItem;
import com.flipmed.system.strategy.RankingStrategy;
import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private final DoctorRepo doctorRepo;

    public SearchService(DoctorRepo doctorRepo) {
        this.doctorRepo = doctorRepo;
    }

    public List<DoctorSearchItem> searchBySpecialization(Specialization spec, RankingStrategy strategy) {
        List<DoctorSearchItem> results = new ArrayList<>();
        
        for (Doctor doctor : doctorRepo.findAll()) {
            if (doctor.getSpecialization() == spec) {
                doctor.getAvailableSlots().forEach(slot -> 
                    results.add(new DoctorSearchItem(doctor, slot))
                );
            }
        }

        strategy.sort(results);
        return results;
    }
}
