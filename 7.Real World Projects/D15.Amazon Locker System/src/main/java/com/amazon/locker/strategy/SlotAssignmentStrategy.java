package com.amazon.locker.strategy;

import com.amazon.locker.model.Slot;
import com.amazon.locker.model.enums.LockerSize;
import java.util.List;

public interface SlotAssignmentStrategy {
    Slot findSlot(List<Slot> slots, LockerSize packageSize);
}
