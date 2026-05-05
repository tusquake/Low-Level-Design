package com.amazon.locker.state;

public class AgentDeliveryState implements LockerMachineState {
    private final LockerMachine machine;

    public AgentDeliveryState(LockerMachine machine) {
        this.machine = machine;
    }

    @Override
    public void carrierEntry() {
        System.out.println("Wait for delivery completion.");
    }

    @Override
    public void customerPickup() {
        System.out.println("Wait for delivery completion.");
    }

    @Override
    public void validateCode(String code) {
        System.out.println("Action not allowed while slot is open.");
    }

    @Override
    public void closeDoor() {
        System.out.println("Door closed. OTP generated and sent to customer. Transitioning to IdleState.");
        machine.setCurrentState(machine.getIdleState());
    }
}
