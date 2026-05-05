package com.amazon.locker.state;

public interface LockerMachineState {
    void carrierEntry();
    void customerPickup();
    void validateCode(String code);
    void closeDoor();
}
