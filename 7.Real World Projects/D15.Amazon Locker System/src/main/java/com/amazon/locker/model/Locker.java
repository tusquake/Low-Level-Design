package com.amazon.locker.model;

import lombok.Data;
import java.util.List;

@Data
public class Locker {
    private final String lockerId;
    private final String zipCode;
    private final List<Slot> slots;

    public Locker(String lockerId, String zipCode, List<Slot> slots) {
        this.lockerId = lockerId;
        this.zipCode = zipCode;
        this.slots = slots;
    }
}
