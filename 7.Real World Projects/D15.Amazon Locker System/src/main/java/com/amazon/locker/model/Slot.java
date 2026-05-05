package com.amazon.locker.model;

import com.amazon.locker.model.enums.LockerSize;
import lombok.Data;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
public class Slot {
    private final String slotId;
    private final LockerSize size;
    private final AtomicBoolean isAvailable;
    private Package currentPackage;

    public Slot(String slotId, LockerSize size) {
        this.slotId = slotId;
        this.size = size;
        this.isAvailable = new AtomicBoolean(true);
    }

    public boolean reserve() {
        return isAvailable.compareAndSet(true, false);
    }

    public void release() {
        this.currentPackage = null;
        isAvailable.set(true);
    }

    public boolean isAvailable() {
        return isAvailable.get();
    }
}
