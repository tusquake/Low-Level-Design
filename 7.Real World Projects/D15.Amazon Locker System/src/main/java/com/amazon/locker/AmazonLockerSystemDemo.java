package com.amazon.locker;

import com.amazon.locker.model.Locker;
import com.amazon.locker.model.Package;
import com.amazon.locker.model.Slot;
import com.amazon.locker.model.enums.LockerSize;
import com.amazon.locker.model.enums.PackageStatus;
import com.amazon.locker.service.LockerService;
import com.amazon.locker.service.OTPService;
import com.amazon.locker.state.LockerMachine;
import com.amazon.locker.strategy.FirstFitSlotStrategy;
import com.amazon.locker.strategy.NumericOTPStrategy;
import java.util.Arrays;
import java.util.List;

public class AmazonLockerSystemDemo {
    public static void main(String[] args) {
        // 1. Setup Services
        LockerService lockerService = new LockerService(new FirstFitSlotStrategy());
        OTPService otpService = new OTPService(new NumericOTPStrategy());

        // 2. Setup Data
        Slot s1 = new Slot("S1", LockerSize.SMALL);
        Slot s2 = new Slot("S2", LockerSize.MEDIUM);
        Slot s3 = new Slot("S3", LockerSize.LARGE);
        Locker locker1 = new Locker("L1", "110001", Arrays.asList(s1, s2, s3));
        lockerService.addLocker(locker1);

        LockerMachine machine1 = new LockerMachine("L1");

        System.out.println("--- Amazon Locker System Demo ---");

        // 3. Customer Scenario: Order placed
        String customerId = "CUST_001";
        String packageId = "PKG_999";
        LockerSize pkgSize = LockerSize.MEDIUM;
        String zipCode = "110001";

        System.out.println("Step 1: Customer searching for lockers in " + zipCode);
        List<Locker> eligibleLockers = lockerService.findEligibleLockers(zipCode, pkgSize);
        if (eligibleLockers.isEmpty()) {
            System.out.println("No lockers available.");
            return;
        }
        Locker selectedLocker = eligibleLockers.get(0);
        System.out.println("Selected Locker: " + selectedLocker.getLockerId());

        Slot reservedSlot = lockerService.reserveSlot(selectedLocker, pkgSize, packageId);
        if (reservedSlot != null) {
            System.out.println("Reserved Slot: " + reservedSlot.getSlotId() + " (Size: " + reservedSlot.getSize() + ")");
        }

        Package pkg = Package.builder()
                .packageId(packageId)
                .customerId(customerId)
                .size(pkgSize)
                .status(PackageStatus.READY_FOR_DELIVERY)
                .build();

        // 4. Agent Delivery Scenario
        System.out.println("\nStep 2: Agent arrives at locker " + selectedLocker.getLockerId());
        machine1.carrierEntry();
        machine1.validateCode(packageId); // Scan barcode (packageId)
        
        reservedSlot.setCurrentPackage(pkg);
        pkg.setStatus(PackageStatus.IN_LOCKER);
        
        System.out.println("Package " + packageId + " placed in slot " + reservedSlot.getSlotId());
        machine1.closeDoor();
        
        // Generate OTP upon door close
        String otp = otpService.generateOTP(packageId);
        System.out.println("NOTIF: OTP " + otp + " sent to customer " + customerId);

        // 5. Customer Pickup Scenario
        System.out.println("\nStep 3: Customer arrives for pickup");
        machine1.customerPickup();
        
        String enteredOtp = otp; // Simulated entry
        String pkgIdFromOtp = otpService.getPackageIdByOtp(enteredOtp);
        
        if (pkgIdFromOtp != null && pkgIdFromOtp.equals(packageId)) {
            machine1.validateCode(enteredOtp);
            Package retrievedPkg = reservedSlot.getCurrentPackage();
            retrievedPkg.setStatus(PackageStatus.PICKED_UP);
            System.out.println("Customer retrieved package: " + retrievedPkg.getPackageId());
            
            machine1.closeDoor();
            lockerService.releaseSlot(packageId);
            otpService.invalidateOTP(enteredOtp);
            
            System.out.println("Pickup completed. Slot " + reservedSlot.getSlotId() + " is now " + 
                               (reservedSlot.isAvailable() ? "AVAILABLE" : "OCCUPIED"));
        } else {
            System.out.println("Invalid OTP.");
        }
    }
}
