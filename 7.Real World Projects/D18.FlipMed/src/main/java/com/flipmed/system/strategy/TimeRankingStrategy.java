package com.flipmed.system.strategy;

import java.util.Comparator;
import java.util.List;

public class TimeRankingStrategy implements RankingStrategy {
    @Override
    public void sort(List<DoctorSearchItem> items) {
        items.sort(Comparator.comparing(item -> item.getTimeSlot().getStartTime()));
    }
}
