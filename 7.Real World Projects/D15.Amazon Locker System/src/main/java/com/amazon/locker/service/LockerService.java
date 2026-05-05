package com.amazon.locker.service;

import com.amazon.locker.model.Locker;
import com.amazon.locker.model.Slot;
import com.amazon.locker.model.enums.LockerSize;
import com.amazon.locker.strategy.SlotAssignmentStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class LockerService {
    private final List<Locker> lockers = new ArrayList<>();
    private final Map<String, Slot> packageToSlotMap = new ConcurrentHashMap<>();
    private final SlotAssignmentStrategy slotAssignmentStrategy;

    public LockerService(SlotAssignmentStrategy slotAssignmentStrategy) {
        this.slotAssignmentStrategy = slotAssignmentStrategy;
    }

    public void addLocker(Locker locker) {
        lockers.add(locker);
    }

    public List<Locker> findEligibleLockers(String zipCode, LockerSize packageSize) {
        return lockers.stream()
                .filter(l -> l.getZipCode().equals(zipCode))
                .filter(l -> l.getSlots().stream()
                        .anyMatch(s -> s.isAvailable() && s.getSize().ordinal() >= packageSize.ordinal()))
                .collect(Collectors.toList());
    }

    public Slot reserveSlot(Locker locker, LockerSize packageSize, String packageId) {
        // Double-check locking or AtomicBoolean inside Slot.reserve() handles the race condition
        Slot slot = slotAssignmentStrategy.findSlot(locker.getSlots(), packageSize);
        if (slot != null && slot.reserve()) {
            packageToSlotMap.put(packageId, slot);
            return slot;
        }
        return null;
    }

    public Slot getSlotByPackageId(String packageId) {
        return packageToSlotMap.get(packageId);
    }

    public void releaseSlot(String packageId) {
        Slot slot = packageToSlotMap.remove(packageId);
        if (slot != null) {
            slot.release();
        }
    }
}
