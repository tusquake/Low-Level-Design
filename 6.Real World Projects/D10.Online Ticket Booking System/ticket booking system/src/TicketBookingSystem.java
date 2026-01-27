import java.util.*;

// ------------------------ Main Class ------------------------
public class TicketBookingSystem {

    public static void main(String[] args) {
        // Create Users
        User user1 = new User("U1", "Alice", "alice@example.com");
        User user2 = new User("U2", "Bob", "bob@example.com");

        // Create Show
        Show show = new Show("S1", "Avengers: Endgame", 5);

        // Initialize Notification Service (Observer)
        NotificationService notificationService = NotificationService.getInstance();
        notificationService.addObserver(new EmailNotifier());
        notificationService.addObserver(new SMSNotifier());

        // Initialize PaymentService with Strategy
        PaymentService paymentService = new PaymentService(new CreditCardPayment());

        // Initialize Booking Service (Singleton)
        BookingService bookingService = BookingService.getInstance(paymentService, notificationService);

        // User1 books seats
        List<String> seats1 = Arrays.asList("1", "2");
        Booking booking1 = bookingService.createBooking(user1, show, seats1);
        System.out.println("Booking1 Status: " + booking1.getStatus());

        // User2 tries to book overlapping seat
        List<String> seats2 = Arrays.asList("2", "3");
        try {
            Booking booking2 = bookingService.createBooking(user2, show, seats2);
            System.out.println("Booking2 Status: " + booking2.getStatus());
        } catch (RuntimeException e) {
            System.out.println("Booking2 failed: " + e.getMessage());
        }

        // Change payment strategy to PayPal and book remaining seats
        paymentService.setPaymentStrategy(new PayPalPayment());
        List<String> seats3 = Arrays.asList("3", "4");
        Booking booking3 = bookingService.createBooking(user2, show, seats3);
        System.out.println("Booking3 Status: " + booking3.getStatus());
    }
}

// ------------------------ User ------------------------
class User {
    private String userId;
    private String name;
    private String email;

    public User(String userId, String name, String email){
        this.userId = userId;
        this.name = name;
        this.email = email;
    }
    public String getUserId(){ return userId; }
}

// ------------------------ Show & Seat ------------------------
class Show {
    private String showId;
    private String name;
    private Map<String, Seat> seats;

    public Show(String showId, String name, int totalSeats){
        this.showId = showId;
        this.name = name;
        seats = new HashMap<>();
        for(int i=1;i<=totalSeats;i++){
            seats.put(String.valueOf(i), new Seat(String.valueOf(i)));
        }
    }

    public Seat getSeat(String seatNumber){
        return seats.get(seatNumber);
    }
}

enum SeatStatus { AVAILABLE, HELD, BOOKED }

class Seat {
    private String seatNumber;
    private SeatStatus status;

    public Seat(String seatNumber){
        this.seatNumber = seatNumber;
        this.status = SeatStatus.AVAILABLE;
    }

    public synchronized boolean holdSeat(){
        if(status == SeatStatus.AVAILABLE){
            status = SeatStatus.HELD;
            return true;
        }
        return false;
    }

    public synchronized void bookSeat(){ status = SeatStatus.BOOKED; }

    public synchronized void releaseSeat(){ status = SeatStatus.AVAILABLE; }
}

// ------------------------ Booking ------------------------
enum BookingStatus { PENDING, CONFIRMED, CANCELLED }

class Booking {
    private String bookingId;
    private User user;
    private Show show;
    private List<Seat> seats;
    private BookingStatus status;

    public Booking(String bookingId, User user, Show show, List<Seat> seats){
        this.bookingId = bookingId;
        this.user = user;
        this.show = show;
        this.seats = seats;
        this.status = BookingStatus.PENDING;
    }

    public void confirmBooking(){
        for(Seat seat : seats) seat.bookSeat();
        status = BookingStatus.CONFIRMED;
    }

    public void cancelBooking(){
        for(Seat seat : seats) seat.releaseSeat();
        status = BookingStatus.CANCELLED;
    }

    public BookingStatus getStatus(){ return status; }
    public String getBookingId(){ return bookingId; }
}

// ------------------------ Booking Service (Singleton) ------------------------
class BookingService {
    private static BookingService instance;
    private Map<String, Booking> bookings = new HashMap<>();
    private PaymentService paymentService;
    private NotificationService notificationService;

    private BookingService(PaymentService paymentService, NotificationService notificationService){
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }

    public static BookingService getInstance(PaymentService paymentService, NotificationService notificationService){
        if(instance == null){
            instance = new BookingService(paymentService, notificationService);
        }
        return instance;
    }

    public Booking createBooking(User user, Show show, List<String> seatNumbers){
        List<Seat> seatsToHold = new ArrayList<>();
        for(String seatNum : seatNumbers){
            Seat seat = show.getSeat(seatNum);
            if(seat.holdSeat()){
                seatsToHold.add(seat);
            } else {
                for(Seat s : seatsToHold) s.releaseSeat();
                throw new RuntimeException("Seat " + seatNum + " not available");
            }
        }

        Booking booking = new Booking(UUID.randomUUID().toString(), user, show, seatsToHold);
        bookings.put(booking.getBookingId(), booking);

        // Process payment (Strategy)
        paymentService.processPayment(booking);

        // Notify user (Observer)
        notificationService.notify(booking);

        return booking;
    }
}

// ------------------------ Payment Service (Strategy + Factory) ------------------------
interface PaymentStrategy { boolean pay(Booking booking); }

class CreditCardPayment implements PaymentStrategy {
    public boolean pay(Booking booking){
        System.out.println("Paid using Credit Card for booking " + booking.getBookingId());
        return true;
    }
}

class PayPalPayment implements PaymentStrategy {
    public boolean pay(Booking booking){
        System.out.println("Paid using PayPal for booking " + booking.getBookingId());
        return true;
    }
}

class PaymentService {
    private PaymentStrategy paymentStrategy;

    public PaymentService(PaymentStrategy paymentStrategy){
        this.paymentStrategy = paymentStrategy;
    }

    public void processPayment(Booking booking){
        boolean success = paymentStrategy.pay(booking);
        if(success) booking.confirmBooking();
        else booking.cancelBooking();
    }

    public void setPaymentStrategy(PaymentStrategy strategy){ this.paymentStrategy = strategy; }
}

// ------------------------ Notification Service (Observer) ------------------------
interface Observer { void update(Booking booking); }

class EmailNotifier implements Observer {
    public void update(Booking booking){ System.out.println("Email sent for booking " + booking.getBookingId()); }
}

class SMSNotifier implements Observer {
    public void update(Booking booking){ System.out.println("SMS sent for booking " + booking.getBookingId()); }
}

class NotificationService {
    private static NotificationService instance;
    private List<Observer> observers = new ArrayList<>();

    private NotificationService() {}

    public static NotificationService getInstance(){
        if(instance == null) instance = new NotificationService();
        return instance;
    }

    public void addObserver(Observer observer){ observers.add(observer); }

    public void notify(Booking booking){
        for(Observer observer : observers) observer.update(booking);
    }
}
