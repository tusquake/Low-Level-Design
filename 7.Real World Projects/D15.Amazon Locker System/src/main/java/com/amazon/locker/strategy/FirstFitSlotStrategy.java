package com.amazon.locker.strategy;

import com.amazon.locker.model.Slot;
import com.amazon.locker.model.enums.LockerSize;
import java.util.List;

public class FirstFitSlotStrategy implements SlotAssignmentStrategy {
    @Override
    public Slot findSlot(List<Slot> slots, LockerSize packageSize) {
        for (Slot slot : slots) {
            if (slot.isAvailable() && slot.getSize().ordinal() >= packageSize.ordinal()) {
                return slot;
            }
        }
        return null;
    }
}
