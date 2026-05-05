package com.amazon.locker.state;

public class CarrierEntryState implements LockerMachineState {
    private final LockerMachine machine;

    public CarrierEntryState(LockerMachine machine) {
        this.machine = machine;
    }

    @Override
    public void carrierEntry() {
        System.out.println("Already in Carrier Entry mode.");
    }

    @Override
    public void customerPickup() {
        System.out.println("Carrier is using the machine. Please wait.");
    }

    @Override
    public void validateCode(String barcode) {
        System.out.println("Barcode " + barcode + " scanned. Opening slot for delivery.");
        // In a real system, this would trigger opening the physical door
        machine.setCurrentState(machine.getAgentDeliveryState());
    }

    @Override
    public void closeDoor() {
        System.out.println("Door is already closed.");
    }
}
