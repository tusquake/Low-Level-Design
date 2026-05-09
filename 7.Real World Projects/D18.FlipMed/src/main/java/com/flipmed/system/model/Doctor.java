package com.flipmed.system.model;

import com.flipmed.system.model.enums.Specialization;
import lombok.Builder;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Doctor {
    private String id;
    private String name;
    private Specialization specialization;
    private double rating;
    @Builder.Default
    private List<TimeSlot> availableSlots = new ArrayList<>();
}
