package com.flipmed.system.strategy;

import com.flipmed.system.model.Doctor;
import java.util.List;

public interface RankingStrategy {
    void sort(List<DoctorSearchItem> items);
}
