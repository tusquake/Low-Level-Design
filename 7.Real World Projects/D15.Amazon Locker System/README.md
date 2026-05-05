# Amazon Locker System Design - Interview Guide

## 30-Second Explanation

"I designed an automated Amazon Locker system for secure package delivery and pickup. The system uses the **State Design Pattern** to manage the locker machine's interface (Idle, CarrierEntry, AgentDelivery, CustomerPickup) and the **Strategy Design Pattern** for flexible slot assignment and OTP generation. It handles concurrent slot reservations using thread-safe mechanisms like `AtomicBoolean` to prevent double-booking. The workflow includes customer reservation, agent delivery with barcode scanning, and secure customer pickup via OTP verification."

---

## Questions to Ask Interviewer

### Functional Requirements
1. How do customers find lockers? (Zip Code, current location?)
2. What package sizes are supported? (Small, Medium, Large, XL?)
3. How long can a package stay in the locker? (Retention period?)
4. What happens if a customer forgets their OTP?
5. How are agents assigned to lockers?
6. Should we support returns via lockers?

### Non-Functional Requirements
1. How many lockers and concurrent users should the system support?
2. Is thread safety required for slot reservation?
3. What is the expected availability of the system?
4. How should OTPs be sent (SMS, Email, App Notification)?

---

## Core Components

### Class Design

```text
LockerService (Singleton/Orchestrator)
  - lockers: List<Locker>
  - findEligibleLockers(zipCode, size)
  - reserveSlot(locker, size, packageId)

Locker
  - lockerId, zipCode, slots: List<Slot>

Slot
  - slotId, size, isAvailable: AtomicBoolean
  - currentPackage: Package
  - reserve(), release()

Package
  - packageId, customerId, size, status

LockerMachine (State Context)
  - currentState: LockerMachineState
  - carrierEntry(), customerPickup(), validateCode(), closeDoor()

OTPService
  - generateOTP(packageId), validateOTP(otp), invalidateOTP(otp)
```

### Enums
```java
LockerSize: SMALL, MEDIUM, LARGE, EXTRA_LARGE
PackageStatus: ORDERED, READY_FOR_DELIVERY, IN_LOCKER, PICKED_UP, RETURNED
```

---

## System Flow

### 1. Slot Reservation
```text
Customer places order -> LockerService searches lockers by Zip Code -> 
Checks availability for package size -> Atomically reserves Slot -> 
Marks package as READY_FOR_DELIVERY.
```

### 2. Agent Delivery
```text
Agent arrives -> Interacts with LockerMachine (CarrierEntry) -> 
Scans Package Barcode -> Machine opens pre-reserved Slot -> 
Agent places package -> Door closed -> Machine generates OTP via OTPService -> 
OTP sent to Customer.
```

### 3. Customer Pickup
```text
Customer arrives -> Interacts with LockerMachine (CustomerPickup) -> 
Enters OTP -> Machine validates OTP via OTPService -> 
Specific Slot opens -> Customer retrieves package -> 
Door closed -> Slot marked AVAILABLE -> OTP invalidated.
```

---

## Design Patterns Used

### 1. State Pattern
**LockerMachineState**: Manages the behavior of the physical machine.
- `IdleState`: Initial state, waits for user type identification.
- `CarrierEntryState`: Handles agent identification and barcode scanning.
- `AgentDeliveryState`: Manages the door-open state for the agent.
- `CustomerPickupState`: Manages OTP entry and door-open state for the customer.

### 2. Strategy Pattern
**SlotAssignmentStrategy**: Algorithms to find optimal slots.
- `FirstFitSlotStrategy`: Picks the first available slot that fits.
- `BestFitSlotStrategy`: Picks the smallest slot that fits to optimize space.

**OTPGenerationStrategy**: Algorithms for code security.
- `NumericOTPStrategy`: Generates 6-digit numeric codes.
- `AlphanumericOTPStrategy`: For higher security.

**AgentAssignmentStrategy**:
- `RandomAgentAssignmentStrategy`
- `ProximityAgentAssignmentStrategy`

---

## Expected Cross-Questions

### Q1: How do you handle concurrency (double-booking a slot)?

**Answer**:
- Used `AtomicBoolean` with `compareAndSet(true, false)` in the `Slot` class to ensure atomic reservation.
- In a distributed environment, use a distributed lock (e.g., Redis Redlock) or database-level optimistic locking.

### Q2: What if no slot of the exact size is available?

**Answer**:
- The system supports "Upsizing": A small package can be placed in a Medium or Large slot if no Small slots are available.
- `FirstFitSlotStrategy` implements this by checking `slot.getSize().ordinal() >= packageSize.ordinal()`.

### Q3: How do you handle OTP expiration?

**Answer**:
- OTPs can have a `createdAt` timestamp.
- The `OTPService` would check the duration during `validateCode()`. If expired, it triggers a "re-generate" or "notify admin" flow.

### Q4: What happens if the door is left open?

**Answer**:
- The physical locker has sensors. If the door isn't closed within X minutes, the system triggers an alert to the customer or a technician.
- The `LockerMachine` state remains in `AgentDelivery` or `CustomerPickup` until the `closeDoor()` signal is received.

### Q5: How to scale for millions of users?

**Answer**:
- **Sharding**: Shard locker data by Zip Code or Region.
- **Caching**: Cache locker availability in Redis for fast searches.
- **Microservices**: Separate `LockerSearchService`, `ReservationService`, and `OTPNotificationService`.

---

## SOLID Principles Applied

1.  **SRP**: `OTPService` only handles codes; `LockerService` handles physical storage; `LockerMachine` handles UI/State.
2.  **OCP**: New slot assignment strategies (e.g., `PowerSavingStrategy`) can be added without modifying `LockerService`.
3.  **LSP**: Any implementation of `LockerMachineState` can be used by the `LockerMachine` context.
4.  **ISP**: `LockerMachineState` defines only relevant actions for the physical interface.
5.  **DIP**: `LockerService` depends on the `SlotAssignmentStrategy` interface, not a concrete implementation.

---

## Summary - One Line Answer

"I designed an Amazon Locker system using the State pattern for UI flow and Strategy pattern for flexible slot allocation, ensuring thread-safe reservations and secure OTP-based retrieval."
