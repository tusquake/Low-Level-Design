import java.util.*;

// ------------------- Robust URL Shortener Mini Project -------------------
public class URLShortenerDemo {

    public static void main(String[] args) {
        URLShortenerService shortener = new URLShortenerService();

        // Users
        User freeUser = new User("user1", UserType.FREE);
        User premiumUser = new User("user2", UserType.PREMIUM);

        String longURL1 = "https://www.amazon.in/Books-Programming-Software-Development/some-long-id-12345";
        String longURL2 = "https://www.google.com/search?q=system+design+interview";

        // Free user auto-generation
        String short1 = shortener.shortenURL(longURL1, freeUser, null);
        System.out.println("[FREE USER] Original: " + longURL1);
        System.out.println("[FREE USER] Shortened: " + short1);

        // Premium user with custom alias
        String short2 = shortener.shortenURL(longURL2, premiumUser, "MySearch");
        System.out.println("\n[PREMIUM USER] Original: " + longURL2);
        System.out.println("[PREMIUM USER] Shortened (custom alias): " + short2);

        // Expanding URLs
        System.out.println("\nExpanding " + short1 + " -> " + shortener.expandURL(short1));
        System.out.println("Expanding " + short2 + " -> " + shortener.expandURL(short2));

        // Premium user tries to take an existing alias
        String conflict = shortener.shortenURL(longURL1, premiumUser, "MySearch");
        System.out.println("\n[PREMIUM USER] Trying to use existing alias: " + conflict);

        // Auto-generated for same long URL again
        String shortAgain = shortener.shortenURL(longURL1, freeUser, null);
        System.out.println("\n[FREE USER] Shortening same URL again: " + shortAgain);
    }
}

// ------------------- User Type -------------------
enum UserType {
    FREE, PREMIUM
}

// ------------------- User Class -------------------
class User {
    String userId;
    UserType type;

    public User(String userId, UserType type) {
        this.userId = userId;
        this.type = type;
    }
}

// ------------------- URL Shortener Service -------------------
class URLShortenerService {
    private static final String BASE_HOST = "https://sho.rt/";
    private static final String CHAR_SET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = CHAR_SET.length();

    private Map<String, String> shortToLong = new HashMap<>();
    private Map<String, String> longToShort = new HashMap<>();
    private long counter = 1; // auto-increment ID

    // ------------------- Shorten URL -------------------
    public String shortenURL(String longURL, User user, String customAlias) {

        // If same long URL already shortened → return existing
        if (longToShort.containsKey(longURL)) {
            return longToShort.get(longURL);
        }

        // Premium user with custom alias
        if (user.type == UserType.PREMIUM && customAlias != null && !customAlias.isEmpty()) {
            String shortURL = BASE_HOST + customAlias;

            // Collision check
            if (shortToLong.containsKey(shortURL)) {
                return "Alias already taken!";
            }

            shortToLong.put(shortURL, longURL);
            longToShort.put(longURL, shortURL);
            return shortURL;
        }

        // Free user or premium without custom alias → auto-generate short code
        String shortCode = generateUniqueShortCode();
        String shortURL = BASE_HOST + shortCode;

        shortToLong.put(shortURL, longURL);
        longToShort.put(longURL, shortURL);

        return shortURL;
    }

    // ------------------- Expand URL -------------------
    public String expandURL(String shortURL) {
        return shortToLong.getOrDefault(shortURL, "URL not found");
    }

    // ------------------- Generate Unique Short Code -------------------
    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = encode(counter++);
        } while (shortToLong.containsKey(BASE_HOST + shortCode)); // ensure no collision
        return shortCode;
    }

    // ------------------- Base62 Encoding -------------------
    private String encode(long num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(CHAR_SET.charAt((int) (num % BASE)));
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
