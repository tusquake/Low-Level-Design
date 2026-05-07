package com.carrental.system.service;

import com.carrental.system.model.*;
import com.carrental.system.model.enums.BookingStatus;
import com.carrental.system.model.enums.VehicleType;
import com.carrental.system.repository.BookingRepository;
import com.carrental.system.repository.BranchRepository;
import com.carrental.system.strategy.BookingStrategy;
import com.carrental.system.strategy.PaymentStrategy;
import com.carrental.system.strategy.PricingStrategy;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookingService {
    private static BookingService instance;
    private final BranchRepository branchRepository;
    private final BookingRepository bookingRepository;

    @Setter private BookingStrategy bookingStrategy;
    @Setter private PricingStrategy pricingStrategy;
    @Setter private PaymentStrategy paymentStrategy;

    private BookingService(BranchRepository branchRepo, BookingRepository bookingRepo) {
        this.branchRepository = branchRepo;
        this.bookingRepository = bookingRepo;
    }

    public static synchronized BookingService getInstance(BranchRepository branchRepo, BookingRepository bookingRepo) {
        if (instance == null) {
            instance = new BookingService(branchRepo, bookingRepo);
        }
        return instance;
    }

    public Booking bookVehicle(User user, String pickupBranchId, String dropBranchId, 
                               VehicleType type, LocalDateTime start, LocalDateTime end) {
        Branch pickupBranch = branchRepository.findById(pickupBranchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid pickup branch"));
        Branch dropBranch = branchRepository.findById(dropBranchId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid drop branch"));

        List<Vehicle> availableVehicles = pickupBranch.getInventory().stream()
                .filter(v -> v.getVehicleType() == type && v.isAvailable())
                .collect(Collectors.toList());

        if (availableVehicles.isEmpty()) {
            System.out.println("No vehicles of type " + type + " available at branch " + pickupBranch.getName());
            return null;
        }

        // Apply Selection Strategy
        Vehicle selectedVehicle = bookingStrategy.selectVehicle(availableVehicles);
        if (selectedVehicle == null) return null;

        // ATOMIC RESERVATION - The core concurrency fix
        if (selectedVehicle.reserve()) {
            double amount = pricingStrategy.calculatePrice(selectedVehicle, start, end);
            
            if (paymentStrategy.processPayment(amount)) {
                Booking booking = Booking.builder()
                        .id(UUID.randomUUID().toString())
                        .user(user)
                        .vehicle(selectedVehicle)
                        .pickupBranch(pickupBranch)
                        .dropBranch(dropBranch)
                        .startTime(start)
                        .endTime(end)
                        .totalAmount(amount)
                        .status(BookingStatus.CONFIRMED)
                        .build();
                
                bookingRepository.save(booking);
                System.out.println("SUCCESS: Vehicle " + selectedVehicle.getLicensePlate() + 
                                   " booked for " + user.getName());
                return booking;
            } else {
                selectedVehicle.release(); // Rollback reservation if payment fails
                System.out.println("FAILED: Payment failed for " + user.getName());
            }
        } else {
            System.out.println("FAILED: Vehicle " + selectedVehicle.getLicensePlate() + 
                               " was just taken by another user!");
        }

        return null;
    }

    public void returnVehicle(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        Vehicle vehicle = booking.getVehicle();
        Branch pickupBranch = booking.getPickupBranch();
        Branch dropBranch = booking.getDropBranch();

        // Update Inventory
        pickupBranch.removeVehicle(vehicle);
        dropBranch.addVehicle(vehicle);
        
        // Update Status
        vehicle.release();
        booking.setStatus(BookingStatus.COMPLETED);
        
        System.out.println("SUCCESS: Vehicle " + vehicle.getLicensePlate() + 
                           " returned to branch " + dropBranch.getName());
    }
}
