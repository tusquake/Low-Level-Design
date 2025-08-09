import java.util.*;

/**
 * Concrete Subject - YouTube Channel
 * Yeh class actual channel ko represent karti hai
 */
public class Channel implements IChannel {

    // Channel ki properties
    private String name;
    private String latestVideo;
    private List<ISubscriber> subscribers;
    private int totalVideos;
    private String description;

    /**
     * Channel constructor
     * @param name - Channel ka naam
     */
    public Channel(String name) {
        this.name = name;
        this.subscribers = new ArrayList<>();
        this.totalVideos = 0;
        this.description = "";
    }

    /**
     * Channel constructor with description
     * @param name - Channel ka naam
     * @param description - Channel ka description
     */
    public Channel(String name, String description) {
        this.name = name;
        this.description = description;
        this.subscribers = new ArrayList<>();
        this.totalVideos = 0;
    }

    /**
     * Subscriber add karne ke liye
     * @param subscriber - Jo subscriber add karna hai
     */
    @Override
    public void subscribe(ISubscriber subscriber) {
        if (subscriber != null && !subscribers.contains(subscriber)) {
            subscribers.add(subscriber);
            System.out.println("✅ " + subscriber.getName() + " ne " + name + " channel ko subscribe kiya!");
            System.out.println("📊 Total Subscribers: " + subscribers.size());
        } else if (subscribers.contains(subscriber)) {
            System.out.println("⚠️ " + subscriber.getName() + " already subscribed hai " + name + " channel ko!");
        }
    }

    /**
     * Subscriber remove karne ke liye
     * @param subscriber - Jo subscriber remove karna hai
     */
    @Override
    public void unsubscribe(ISubscriber subscriber) {
        if (subscriber != null && subscribers.contains(subscriber)) {
            subscribers.remove(subscriber);
            System.out.println("❌ " + subscriber.getName() + " ne " + name + " channel ko unsubscribe kiya!");
            System.out.println("📊 Total Subscribers: " + subscribers.size());
        } else {
            System.out.println("⚠️ " + subscriber.getName() + " pehle se hi unsubscribed hai!");
        }
    }

    /**
     * Sabhi subscribers ko notify karne ke liye
     */
    @Override
    public void notifySubscribers() {
        if (subscribers.isEmpty()) {
            System.out.println("😔 " + name + " channel ka koi subscriber nahi hai!");
            return;
        }

        System.out.println("\n🔔 " + name + " channel ke " + subscribers.size() + " subscribers ko notify kar rahe hain...");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        for (ISubscriber subscriber : subscribers) {
            try {
                subscriber.update(name, latestVideo);
                Thread.sleep(100); // Simulate notification delay
            } catch (Exception e) {
                System.err.println("❌ Error notifying " + subscriber.getName() + ": " + e.getMessage());
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    /**
     * New video upload karne ke liye
     * @param videoTitle - Nayi video ka title
     */
    public void uploadVideo(String videoTitle) {
        if (videoTitle == null || videoTitle.trim().isEmpty()) {
            System.out.println("❌ Video title empty nahi ho sakta!");
            return;
        }

        this.latestVideo = videoTitle;
        this.totalVideos++;

        System.out.println("\n🎬 " + name + " ne nayi video upload ki!");
        System.out.println("📹 Video Title: " + videoTitle);
        System.out.println("📊 Total Videos: " + totalVideos);

        // Sabko notification bhej do
        notifySubscribers();
    }

    /**
     * Live stream start karne ke liye
     * @param streamTitle - Live stream ka title
     */
    public void startLiveStream(String streamTitle) {
        System.out.println("\n🔴 LIVE: " + name + " ka live stream shuru hua!");
        System.out.println("📺 Stream Title: " + streamTitle);

        // Live stream ke liye special notification
        if (!subscribers.isEmpty()) {
            System.out.println("\n🔔 Live stream notification bhej rahe hain...");
            for (ISubscriber subscriber : subscribers) {
                System.out.println("🔴 " + subscriber.getName() + " ko live notification mili: "
                        + name + " is now LIVE - '" + streamTitle + "'");
            }
        }
    }

    /**
     * Channel ki statistics dikhane ke liye
     */
    public void showChannelStats() {
        System.out.println("\n📊 " + name + " Channel Statistics:");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("👥 Total Subscribers: " + subscribers.size());
        System.out.println("🎬 Total Videos: " + totalVideos);
        System.out.println("📹 Latest Video: " + (latestVideo != null ? latestVideo : "No videos yet"));
        System.out.println("📝 Description: " + (description.isEmpty() ? "No description" : description));

        if (!subscribers.isEmpty()) {
            System.out.println("👥 Subscriber List:");
            for (int i = 0; i < subscribers.size(); i++) {
                System.out.println("   " + (i + 1) + ". " + subscribers.get(i).getName());
            }
        }
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }

    // Getters and Setters
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLatestVideo() {
        return latestVideo;
    }

    public int getTotalVideos() {
        return totalVideos;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSubscriberCount() {
        return subscribers.size();
    }

    public List<ISubscriber> getSubscribers() {
        return new ArrayList<>(subscribers); // Return copy to prevent direct modification
    }

    /**
     * toString method for easy printing
     */
    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", subscribers=" + subscribers.size() +
                ", totalVideos=" + totalVideos +
                ", latestVideo='" + latestVideo + '\'' +
                '}';
    }

    /**
     * equals method for proper comparison
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Channel channel = (Channel) obj;
        return Objects.equals(name, channel.name);
    }

    /**
     * hashCode method
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}