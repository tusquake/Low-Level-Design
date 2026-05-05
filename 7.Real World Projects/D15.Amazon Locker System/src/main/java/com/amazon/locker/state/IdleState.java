package com.amazon.locker.state;

public class IdleState implements LockerMachineState {
    private final LockerMachine machine;

    public IdleState(LockerMachine machine) {
        this.machine = machine;
    }

    @Override
    public void carrierEntry() {
        System.out.println("Carrier identified. Switching to CarrierEntryState.");
        machine.setCurrentState(machine.getCarrierEntryState());
    }

    @Override
    public void customerPickup() {
        System.out.println("Customer identified. Switching to CustomerPickupState.");
        machine.setCurrentState(machine.getCustomerPickupState());
    }

    @Override
    public void validateCode(String code) {
        System.out.println("Invalid action in Idle state.");
    }

    @Override
    public void closeDoor() {
        System.out.println("Door is already closed.");
    }
}
