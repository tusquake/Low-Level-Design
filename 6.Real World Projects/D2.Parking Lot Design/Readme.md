# Parking Lot System Design - Interview Guide

## 30-Second Explanation

"A parking lot system manages vehicle parking across multiple floors with different spot types. I design it using Strategy Pattern for spot allocation and pricing, with classes for Vehicle, ParkingSpot, and Ticket. The system tracks availability, calculates fees based on duration and vehicle type, and handles concurrent access using thread-safe data structures."

---

## Questions to Ask Interviewer

### Functional Requirements
1. What types of vehicles? (Motorcycle, Car, Truck, Bus?)
2. How many floors and spots per floor?
3. What spot types? (Compact, Regular, Large, Electric?)
4. How is pricing calculated? (Hourly, vehicle-based, flat rate?)
5. Should we support reservations?
6. Entry/Exit gates - multiple or single?

### Non-Functional Requirements
1. How many parking requests per second?
2. Should it be thread-safe (concurrent access)?
3. Display board showing available spots?
4. Payment methods to support?
5. What happens when parking is full?

---

## Core Components

### Class Design

```
Vehicle (Abstract)
  ├── Motorcycle
  ├── Car
  ├── Truck
  └── Bus

ParkingSpot
  - spotId, type, status, floor
  - canFitVehicle()
  - parkVehicle()
  - removeVehicle()

ParkingTicket
  - ticketId, vehicle, spot
  - entryTime, exitTime
  - amount

ParkingLot
  - spots, tickets
  - parkVehicle()
  - exitVehicle()
  - getAvailableSpots()

Strategies:
  - ParkingStrategy (nearest, lowest floor)
  - PricingStrategy (hourly, vehicle-based)
```

### Enums
```java
VehicleType: MOTORCYCLE, CAR, TRUCK, BUS
SpotType: COMPACT, REGULAR, LARGE, ELECTRIC
SpotStatus: AVAILABLE, OCCUPIED, RESERVED, OUT_OF_SERVICE
```

---

## System Flow

```
1. Vehicle Entry
   ↓
2. Find Available Spot (using ParkingStrategy)
   ↓
3. Park Vehicle & Generate Ticket
   ↓
4. Vehicle Stays (duration tracked)
   ↓
5. Vehicle Exit (present ticket)
   ↓
6. Calculate Fee (using PricingStrategy)
   ↓
7. Free Spot & Collect Payment
```

---

## Design Patterns Used

### 1. Strategy Pattern
**ParkingStrategy**: Different ways to find spots
- NearestSpotStrategy
- LowestFloorStrategy
- RandomSpotStrategy

**PricingStrategy**: Different pricing models
- HourlyPricing
- VehicleTypePricing
- FlatRatePricing

### 2. Factory Pattern
Create different vehicle types
```java
VehicleFactory.createVehicle(type, licensePlate)
```

### 3. Singleton Pattern
Single ParkingLot instance
```java
ParkingLot.getInstance()
```

---

## Expected Cross-Questions

### Q1: How do you handle concurrency (multiple vehicles at same time)?

**Answer**:
- Use ConcurrentHashMap for storing spots and tickets
- Synchronize spot allocation (lock on spot object)
- AtomicInteger for available spot counter
- Database transactions for distributed systems

### Q2: What if parking is full?

**Answer**:
- Return null/error when no spot available
- Display "Parking Full" on entry board
- Optionally: Queue vehicles or suggest nearby parking
- Send notifications when spots become available

### Q3: How to handle different vehicle sizes in spots?

**Answer**:
- Motorcycle can park in any spot (compact, regular, large)
- Car can park in regular or large spots
- Truck/Bus only in large spots
- Use canFitVehicle() method for validation

### Q4: How to calculate parking fee?

**Answer**:
**Hourly Pricing**: Duration × Rate per hour (round up)
**Vehicle-based**: Different rates for different vehicles
**Peak Hours**: Higher rates during peak times
**Formula**:
```
hours = ceil(duration_in_minutes / 60)
fee = hours × rate
```

### Q5: How to track which spot is on which floor?

**Answer**:
- Each ParkingSpot has floor attribute
- SpotId format: "F1-A101" (Floor 1, Section A, Spot 101)
- Group spots by floor in data structure
- Display board shows available spots per floor

### Q6: What if a vehicle is lost/overstays?

**Answer**:
- Maximum parking duration limit (e.g., 24 hours)
- After limit: Daily penalty fee
- Lost ticket: Pay maximum fee for that period
- Contact admin for special cases

### Q7: How to support electric vehicle charging?

**Answer**:
- Create ELECTRIC spot type
- Track charging time separately
- Add charging fee to parking fee
- Monitor charging status

### Q8: How would you scale this for multiple parking lots?

**Answer**:
- Centralized database for all lots
- Each lot has unique ID
- API to check availability across locations
- Mobile app shows nearest available parking

### Q9: How to implement reservation system?

**Answer**:
- Add SpotStatus.RESERVED
- Time-based reservation (hold spot for X minutes)
- Auto-release if vehicle doesn't arrive
- Separate queue for reserved vs walk-in

### Q10: How to prevent ticket fraud?

**Answer**:
- Generate unique ticket IDs (UUID)
- QR code on ticket with encrypted data
- Validate ticket on exit (check in database)
- Camera/ANPR for vehicle verification

---

## Database Schema

### Parking Spot Table
```sql
parking_spots
  - spot_id (PK)
  - spot_type (COMPACT/REGULAR/LARGE)
  - floor
  - status (AVAILABLE/OCCUPIED)
  - parking_lot_id (FK)
```

### Parking Ticket Table
```sql
parking_tickets
  - ticket_id (PK)
  - vehicle_license_plate
  - spot_id (FK)
  - entry_time
  - exit_time
  - amount
  - payment_status
```

### Vehicle Table
```sql
vehicles
  - license_plate (PK)
  - vehicle_type
  - owner_name
  - phone
```

---

## Pricing Examples

### Hourly Pricing
```
Duration: 2 hours 15 minutes
Rounded: 3 hours
Rate: Rs. 20/hour
Fee: 3 × 20 = Rs. 60
```

### Vehicle Type Pricing
```
Motorcycle: Rs. 10/hour
Car: Rs. 20/hour
Truck: Rs. 40/hour
Bus: Rs. 50/hour
```

### Peak Hour Pricing
```
Normal: Rs. 20/hour
Peak (9AM-6PM): Rs. 30/hour
Night (10PM-6AM): Rs. 15/hour
```

---

## SOLID Principles Applied

1. **SRP**: Each class has single responsibility
    - ParkingSpot manages spot state
    - ParkingTicket tracks parking session
    - PricingStrategy calculates fees

2. **OCP**: Open for extension, closed for modification
    - Add new strategies without changing existing code
    - Add new vehicle types by extending Vehicle class

3. **LSP**: Subclasses are substitutable
    - All Vehicle subclasses can be used interchangeably
    - All strategies implement same interface

4. **ISP**: Interface segregation
    - ParkingStrategy interface (one method)
    - PricingStrategy interface (one method)

5. **DIP**: Depend on abstractions
    - ParkingLot depends on ParkingStrategy interface
    - Not dependent on concrete implementations

---

## Key Features to Mention

1. **Thread Safety**: ConcurrentHashMap, synchronized blocks
2. **Flexibility**: Strategy pattern for extensibility
3. **Scalability**: Can add more floors, spots dynamically
4. **Real-time Updates**: Availability tracking
5. **Multiple Payment Options**: Cash, card, UPI
6. **Reports**: Daily revenue, occupancy rate
7. **Admin Dashboard**: Monitor all spots
8. **Mobile App Integration**: Find spot, pay online

---

## Performance Considerations

### Time Complexity
- Find spot: O(N) where N = number of spots
- Park vehicle: O(1) with HashMap
- Calculate fee: O(1)
- Get available spots: O(N)

### Space Complexity
- O(N) for N parking spots
- O(M) for M active tickets

### Optimization
- Index spots by floor and type
- Cache available spot count
- Use priority queue for spot allocation
- Database indexing on frequently queried fields

---

## Summary - One Line Answer

"I designed a parking lot system using Strategy Pattern for spot allocation and pricing, with Vehicle, ParkingSpot, and Ticket classes, ensuring thread safety using ConcurrentHashMap and synchronized blocks, supporting multiple floors, spot types, and flexible pricing models."

---

## Additional Features (If Time Permits)

1. **Valet Parking**: Premium service
2. **Monthly Pass**: Subscription model
3. **Loyalty Program**: Discounts for frequent users
4. **Real-time Monitoring**: Camera integration
5. **Mobile Notifications**: Spot availability alerts
6. **Analytics Dashboard**: Usage patterns, peak hours
7. **Integration**: Payment gateway, SMS alerts
8. **Accessibility**: Disabled parking spots
9. **Security**: CCTV, emergency buttons
10. **Weather Protection**: Covered vs open spots