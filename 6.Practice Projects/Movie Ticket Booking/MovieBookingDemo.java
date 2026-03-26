import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class Movie {
    private String title;
    private String genre;
    private int duration;

    public Movie(String title, String genre, int duration) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDuration() { return duration; }
}

class Show {
    private String showId;
    private Movie movie;
    private String startTime;
    private Map<String, Boolean> seats; // SeatID -> isBooked

    public Show(String showId, Movie movie, String startTime, List<String> seatIds) {
        this.showId = showId;
        this.movie = movie;
        this.startTime = startTime;
        this.seats = new ConcurrentHashMap<>();
        for (String id : seatIds) {
            seats.put(id, false);
        }
    }

    public synchronized boolean bookSeat(String seatId) {
        if (!seats.containsKey(seatId)) {
            System.out.println("Error: Seat " + seatId + " doesn't exist!");
            return false;
        }

        if (seats.get(seatId)) {
            System.out.println("Error: Seat " + seatId + " already booked for " + movie.getTitle());
            return false;
        }

        seats.put(seatId, true);
        return true;
    }

    public String getShowId() { return showId; }
    public Movie getMovie() { return movie; }
    public String getStartTime() { return startTime; }
}

class BookingSystem {
    private List<Movie> movies = new ArrayList<>();
    private Map<String, Show> shows = new HashMap<>();

    public void addMovie(Movie movie) {
        movies.add(movie);
    }

    public void addShow(Show show) {
        shows.put(show.getShowId(), show);
        System.out.println("Show created: " + show.getMovie().getTitle() + " at " + show.getStartTime());
    }

    public void bookTicket(String showId, String seatId, String userName) {
        Show show = shows.get(showId);
        if (show == null) {
            System.out.println("Error: Show not found!");
            return;
        }

        if (show.bookSeat(seatId)) {
            System.out.println("SUCCESS: " + userName + " booked seat " + seatId + " for " + show.getMovie().getTitle());
        } else {
            System.out.println("FAILURE: " + userName + " could not book seat " + seatId);
        }
    }
}

public class MovieBookingDemo {
    public static void main(String[] args) {
        System.out.println("=== MOVIE TICKET BOOKING SYSTEM ===\n");

        BookingSystem system = new BookingSystem();

        // Setup
        Movie movie1 = new Movie("Inception", "Sci-Fi", 148);
        system.addMovie(movie1);

        List<String> seatsArr = Arrays.asList("A1", "A2", "A3", "B1", "B2", "B3");
        Show eveningShow = new Show("S001", movie1, "18:30", seatsArr);
        system.addShow(eveningShow);

        // Simulate bookings
        System.out.println("\n--- Booking Attempts ---");
        system.bookTicket("S001", "A1", "Alice");
        system.bookTicket("S001", "A2", "Bob");
        
        // Concurrent attempt for same seat
        system.bookTicket("S001", "A1", "Charlie");

        // Invalid seat
        system.bookTicket("S001", "C99", "David");

        System.out.println("\nFinalizing system...");
    }
}
