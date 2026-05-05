package com.amazon.locker.state;

public class CustomerPickupState implements LockerMachineState {
    private final LockerMachine machine;

    public CustomerPickupState(LockerMachine machine) {
        this.machine = machine;
    }

    @Override
    public void carrierEntry() {
        System.out.println("Customer is using the machine. Please wait.");
    }

    @Override
    public void customerPickup() {
        System.out.println("Already in Customer Pickup mode.");
    }

    @Override
    public void validateCode(String otp) {
        System.out.println("OTP " + otp + " validated. Opening slot for pickup.");
        // In a real system, this would trigger opening the physical door
    }

    @Override
    public void closeDoor() {
        System.out.println("Door closed. Slot marked as available. Transitioning to IdleState.");
        machine.setCurrentState(machine.getIdleState());
    }
}
