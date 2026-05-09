package com.flipmed.system.strategy;

import java.util.Comparator;
import java.util.List;

public class RatingRankingStrategy implements RankingStrategy {
    @Override
    public void sort(List<DoctorSearchItem> items) {
        items.sort(Comparator.comparingDouble((DoctorSearchItem item) -> item.getDoctor().getRating()).reversed());
    }
}
