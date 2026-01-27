import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer - Individual Subscriber
 * Yeh class actual subscriber ko represent karti hai
 */
public class Subscriber implements ISubscriber {

    // Subscriber ki properties
    private String name;
    private String email;
    private List<Channel> subscribedChannels;
    private List<String> notificationHistory;
    private boolean notificationsEnabled;
    private LocalDateTime joinDate;

    /**
     * Subscriber constructor
     * @param name - Subscriber ka naam
     */
    public Subscriber(String name) {
        this.name = name;
        this.subscribedChannels = new ArrayList<>();
        this.notificationHistory = new ArrayList<>();
        this.notificationsEnabled = true;
        this.joinDate = LocalDateTime.now();
    }

    /**
     * Subscriber constructor with email
     * @param name - Subscriber ka naam
     * @param email - Subscriber ka email
     */
    public Subscriber(String name, String email) {
        this.name = name;
        this.email = email;
        this.subscribedChannels = new ArrayList<>();
        this.notificationHistory = new ArrayList<>();
        this.notificationsEnabled = true;
        this.joinDate = LocalDateTime.now();
    }

    /**
     * Update method - Channel se notification receive karne ke liye
     * @param channelName - Konsa channel hai
     * @param videoTitle - Nayi video ka title
     */
    @Override
    public void update(String channelName, String videoTitle) {
        if (!notificationsEnabled) {
            return; // Agar notifications off hain toh return kar do
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String notification = "🔔 [" + timestamp + "] " + name + " ko notification mili: "
                + channelName + " ne nayi video upload ki - '" + videoTitle + "'";

        System.out.println(notification);

        // Notification history mein save kar do
        notificationHistory.add(notification);

        // Simulate user reaction
        simulateUserReaction(channelName, videoTitle);
    }

    /**
     * Channel ko subscribe karne ke liye
     * @param channel - Jo channel subscribe karna hai
     */
    public void subscribeToChannel(Channel channel) {
        if (channel != null && !subscribedChannels.contains(channel)) {
            subscribedChannels.add(channel);
            channel.subscribe(this);
            System.out.println("🎉 " + name + " successfully subscribed to " + channel.getName() + "!");
        } else if (subscribedChannels.contains(channel)) {
            System.out.println("⚠️ " + name + " already subscribed hai " + channel.getName() + " ko!");
        }
    }

    /**
     * Specific channel ko unsubscribe karne ke liye
     * @param channel - Jo channel unsubscribe karna hai
     */
    public void unsubscribeFromChannel(Channel channel) {
        if (channel != null && subscribedChannels.contains(channel)) {
            subscribedChannels.remove(channel);
            channel.unsubscribe(this);
            System.out.println("👋 " + name + " successfully unsubscribed from " + channel.getName() + "!");
        } else {
            System.out.println("⚠️ " + name + " subscribed nahi hai " + channel.getName() + " ko!");
        }
    }

    /**
     * Sabhi channels ko unsubscribe karne ke liye
     */
    public void unsubscribeFromAllChannels() {
        System.out.println("\n🚪 " + name + " sabhi channels se unsubscribe kar raha hai...");

        // Copy banao kyunki original list modify hogi
        List<Channel> channelsToUnsubscribe = new ArrayList<>(subscribedChannels);

        for (Channel channel : channelsToUnsubscribe) {
            unsubscribeFromChannel(channel);
        }

        System.out.println("✅ " + name + " ne sabhi channels se unsubscribe kar diya!");
    }

    /**
     * User reaction simulate karne ke liye
     * @param channelName - Channel name
     * @param videoTitle - Video title
     */
    private void simulateUserReaction(String channelName, String videoTitle) {
        Random random = new Random();
        String[] reactions = {
                "👍 Liked the video!",
                "💬 Left a comment",
                "🔄 Shared with friends",
                "⏰ Added to Watch Later",
                "📱 Opened the video immediately"
        };

        if (random.nextBoolean()) { // 50% chance of reaction
            String reaction = reactions[random.nextInt(reactions.length)];
            System.out.println("   └─ " + name + ": " + reaction);
        }
    }

    /**
     * Notifications on/off karne ke liye
     * @param enabled - true ya false
     */
    public void setNotificationsEnabled(boolean enabled) {
        this.notificationsEnabled = enabled;
        String status = enabled ? "ON" : "OFF";
        System.out.println("🔔 " + name + " ke notifications " + status + " kar diye!");
    }

    /**
     * Notification history dikhane ke liye
     */
    public void showNotificationHistory() {
        System.out.println("\n📜 " + name + " की Notification History:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (notificationHistory.isEmpty()) {
            System.out.println("📭 Koi notifications nahi hain!");
        } else {
            for (int i = notificationHistory.size() - 1; i >= 0; i--) { // Latest first
                System.out.println((notificationHistory.size() - i) + ". " + notificationHistory.get(i));
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Subscribed channels ki list dikhane ke liye
     */
    public void showSubscribedChannels() {
        System.out.println("\n📺 " + name + " के Subscribed Channels:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        if (subscribedChannels.isEmpty()) {
            System.out.println("😔 Koi subscribed channels nahi hain!");
        } else {
            for (int i = 0; i < subscribedChannels.size(); i++) {
                Channel channel = subscribedChannels.get(i);
                System.out.println((i + 1) + ". " + channel.getName() +
                        " (" + channel.getSubscriberCount() + " subscribers)");
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * Subscriber profile dikhane ke liye
     */
    public void showProfile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        System.out.println("\n👤 " + name + " की Profile:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("📧 Email: " + (email != null ? email : "Not provided"));
        System.out.println("📅 Join Date: " + joinDate.format(formatter));
        System.out.println("📺 Subscribed Channels: " + subscribedChannels.size());
        System.out.println("🔔 Notifications: " + (notificationsEnabled ? "ON" : "OFF"));
        System.out.println("📜 Total Notifications: " + notificationHistory.size());
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    // Getters and Setters
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Channel> getSubscribedChannels() {
        return new ArrayList<>(subscribedChannels); // Return copy
    }

    public boolean isNotificationsEnabled() {
        return notificationsEnabled;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public int getTotalNotifications() {
        return notificationHistory.size();
    }

    /**
     * toString method for easy printing
     */
    @Override
    public String toString() {
        return "Subscriber{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", subscribedChannels=" + subscribedChannels.size() +
                ", notificationsEnabled=" + notificationsEnabled +
                '}';
    }

    /**
     * equals method for proper comparison
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Subscriber that = (Subscriber) obj;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email);
    }

    /**
     * hashCode method
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, email);
    }
}