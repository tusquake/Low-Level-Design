package com.amazon.locker.service;

import com.amazon.locker.model.Locker;
import com.amazon.locker.model.Slot;
import com.amazon.locker.model.enums.LockerSize;
import com.amazon.locker.strategy.FirstFitSlotStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

public class LockerServiceTest {
    private LockerService lockerService;
    private Locker locker;

    @BeforeEach
    public void setup() {
        lockerService = new LockerService(new FirstFitSlotStrategy());
        Slot s1 = new Slot("S1", LockerSize.SMALL);
        locker = new Locker("L1", "110001", Arrays.asList(s1));
        lockerService.addLocker(locker);
    }

    @Test
    public void testConcurrentReservation() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            final String packageId = "PKG_" + i;
            executor.submit(() -> {
                try {
                    latch.await();
                    Slot slot = lockerService.reserveSlot(locker, LockerSize.SMALL, packageId);
                    if (slot != null) {
                        successCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        latch.countDown(); // Start all threads
        executor.shutdown();
        executor.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);

        assertEquals(1, successCount.get(), "Only one thread should successfully reserve the single slot.");
    }
}
