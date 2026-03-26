# Movie Ticket Booking System (BookMyShow Scenario)

## Simple Analogy

**Movie Booking:**
- **Cinema/Theater:** The physical building with multiple screens.
- **Show:** A specific movie playing at a specific time on a specific screen.
- **Seat:** Your reserved space (VIP, Premium, Normal).
- **Booking:** Your receipt/ticket showing your seat and movie details.

It's like a specialized inventory system where the "items" (seats) are time-sensitive and unique to each "event" (show).

---

## Technical Challenges (LLD)

1. **Concurrency**: Two people trying to book the same seat at the exact same time. (Solved using `synchronized` or `ConcurrentHashMap` in the demo).
2. **Seat Locking**: Holding a seat for 5-10 minutes while the user completes payment (Advanced).
3. **Capacity Management**: Ensuring no overbooking.

---

## Demo Summary

The [MovieBookingDemo.java](file:///c:/Users/tushar.seth/Desktop/LLD/Low-Level-Design/6.Practice%20Projects/Movie%20Ticket%20Booking/MovieBookingDemo.java) demonstrates:
- Adding movies and scheduling shows.
- Thread-safe seat booking using `synchronized` methods.
- Handling concurrent booking attempts for the same seat.

```java
// SUCCESS: Alice booked seat A1 for Inception
// ERROR: Seat A1 already booked for Inception
```

---

## Key OOPS Used

- **Encapsulation**: Protecting seat status within the `Show` class.
- **Composition**: A `BookingSystem` has a map of `Shows`.
- **Thread Safety**: Using `synchronized` to prevent race conditions during seat allocation.

---

## Real-World Uses

- **BookMyShow, PVR, AMC**.
- **Airline Seat Booking**.
- **Concert Ticket Platforms (Ticketmaster)**.
