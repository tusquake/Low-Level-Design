package com.carrental.system.repository;

import com.carrental.system.model.Booking;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookingRepository {
    private final List<Booking> bookings = new ArrayList<>();

    public void save(Booking booking) {
        bookings.add(booking);
    }

    public Optional<Booking> findById(String id) {
        return bookings.stream().filter(b -> b.getId().equals(id)).findFirst();
    }
}
