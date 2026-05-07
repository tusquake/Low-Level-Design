package com.carrental.system;

import com.carrental.system.factory.VehicleFactory;
import com.carrental.system.model.*;
import com.carrental.system.model.enums.VehicleType;
import com.carrental.system.repository.BookingRepository;
import com.carrental.system.repository.BranchRepository;
import com.carrental.system.service.BookingService;
import com.carrental.system.strategy.CheapestFirstStrategy;
import com.carrental.system.strategy.CreditCardPayment;
import com.carrental.system.strategy.HourlyPricingStrategy;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CarRentalSystemDemo {
    public static void main(String[] args) throws InterruptedException {
        // 1. Setup Infrastructure
        BranchRepository branchRepo = new BranchRepository();
        BookingRepository bookingRepo = new BookingRepository();
        
        Branch b1 = new Branch("B1", "Main Branch", "New York");
        Branch b2 = new Branch("B2", "Airport Branch", "New York");
        branchRepo.addBranch(b1);
        branchRepo.addBranch(b2);

        // 2. Add Inventory via Factory
        Vehicle car1 = VehicleFactory.createVehicle("V1", "NY-101", 50.0, VehicleType.CAR);
        Vehicle suv1 = VehicleFactory.createVehicle("V2", "NY-202", 80.0, VehicleType.SUV);
        b1.addVehicle(car1);
        b1.addVehicle(suv1);

        // 3. Setup Service (Singleton)
        BookingService bookingService = BookingService.getInstance(branchRepo, bookingRepo);
        bookingService.setBookingStrategy(new CheapestFirstStrategy());
        bookingService.setPricingStrategy(new HourlyPricingStrategy());
        bookingService.setPaymentStrategy(new CreditCardPayment());

        System.out.println("--- Car Rental System Demo ---");

        // 4. Multi-threaded Simulation: Race condition for the same car
        User userA = new User("U1", "Alice", "alice@example.com");
        User userB = new User("U2", "Bob", "bob@example.com");

        System.out.println("\nSCENARIO: Alice and Bob try to book the ONLY car NY-101 at the same time.");
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(1);

        executor.submit(() -> {
            try {
                latch.await();
                bookingService.bookVehicle(userA, "B1", "B2", VehicleType.CAR, 
                        LocalDateTime.now(), LocalDateTime.now().plusMinutes(45));
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        executor.submit(() -> {
            try {
                latch.await();
                bookingService.bookVehicle(userB, "B1", "B2", VehicleType.CAR, 
                        LocalDateTime.now(), LocalDateTime.now().plusHours(2));
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });

        latch.countDown(); // Race starts!
        executor.shutdown();
        executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);

        // 5. Return Flow
        System.out.println("\nSCENARIO: Returning the vehicle to Airport Branch.");
        // We don't know who won the race easily in this simple demo, but one did.
        // In a real app we'd have the booking object.
        
        System.out.println("\nDemo completed. Check console output for race results.");
    }
}
