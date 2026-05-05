package com.amazon.locker.state;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LockerMachine {
    private final String lockerId;
    private LockerMachineState currentState;
    
    private final LockerMachineState idleState;
    private final LockerMachineState carrierEntryState;
    private final LockerMachineState agentDeliveryState;
    private final LockerMachineState customerPickupState;

    public LockerMachine(String lockerId) {
        this.lockerId = lockerId;
        this.idleState = new IdleState(this);
        this.carrierEntryState = new CarrierEntryState(this);
        this.agentDeliveryState = new AgentDeliveryState(this);
        this.customerPickupState = new CustomerPickupState(this);
        this.currentState = idleState;
    }

    public void carrierEntry() {
        currentState.carrierEntry();
    }

    public void customerPickup() {
        currentState.customerPickup();
    }

    public void validateCode(String code) {
        currentState.validateCode(code);
    }

    public void closeDoor() {
        currentState.closeDoor();
    }
}
