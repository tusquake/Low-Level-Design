/**
 * Main Demo Class - Observer Pattern Testing
 * Yeh class sabhi components ko test karne ke liye hai
 */
public class ObserverPatternDemo {

    public static void main(String[] args) {
        System.out.println("🎬 ===== Observer Pattern Demo - YouTube Subscription System =====");
        System.out.println("🚀 Starting demo with separate Java classes...\n");

        try {
            // Demo scenarios
            basicSubscriptionDemo();
            advancedFeaturesDemo();
            realWorldScenarioDemo();

        } catch (Exception e) {
            System.err.println("❌ Demo mein error aaya: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n🎉 ===== Demo Complete! Observer Pattern Successfully Implemented =====");
    }

    /**
     * Basic subscription demo
     */
    private static void basicSubscriptionDemo() throws InterruptedException {
        System.out.println("📺 === BASIC SUBSCRIPTION DEMO ===");

        // YouTube channels banate hain
        Channel techChannel = new Channel("TechGuru", "Latest technology tutorials and reviews");
        Channel cookingChannel = new Channel("CookingMaster", "Delicious recipes from around the world");
        Channel musicChannel = new Channel("MusicHub", "Latest music and entertainment");

        // Subscribers banate hain
        Subscriber rahul = new Subscriber("Rahul", "rahul@email.com");
        Subscriber priya = new Subscriber("Priya", "priya@email.com");
        Subscriber amit = new Subscriber("Amit");

        System.out.println("\n--- Creating Subscriptions ---");

        // Subscriptions create karte hain
        rahul.subscribeToChannel(techChannel);
        rahul.subscribeToChannel(musicChannel);

        priya.subscribeToChannel(techChannel);
        priya.subscribeToChannel(cookingChannel);

        amit.subscribeToChannel(cookingChannel);

        System.out.println("\n--- Channel Statistics ---");
        techChannel.showChannelStats();
        cookingChannel.showChannelStats();

        System.out.println("\n--- Video Uploads ---");

        // Videos upload karte hain
        techChannel.uploadVideo("Java Design Patterns Complete Tutorial");
        Thread.sleep(1000);

        cookingChannel.uploadVideo("Perfect Biryani Recipe - Step by Step");
        Thread.sleep(1000);

        musicChannel.uploadVideo("Top 10 Songs of 2024");
        Thread.sleep(1000);

        System.out.println("\n📺 === BASIC DEMO COMPLETED ===\n");
    }

    /**
     * Advanced features demo
     */
    private static void advancedFeaturesDemo() {
        System.out.println("🔥 === ADVANCED FEATURES DEMO ===");

        // New channels and subscribers
        Channel fitnessChannel = new Channel("FitnessFreak", "Health and fitness motivation");
        Channel newsChannel = new Channel("NewsToday", "Latest news and updates");

        Subscriber sara = new Subscriber("Sara", "sara@email.com");
        Subscriber karan = new Subscriber("Karan", "karan@email.com");

        System.out.println("\n--- Advanced Subscriptions ---");
        sara.subscribeToChannel(fitnessChannel);
        sara.subscribeToChannel(newsChannel);

        karan.subscribeToChannel(fitnessChannel);

        // Show profiles
        sara.showProfile();
        karan.showProfile();

        System.out.println("\n--- Live Stream Demo ---");
        fitnessChannel.startLiveStream("LIVE: 30-Minute Morning Workout");

        System.out.println("\n--- Notification Management ---");

        // Upload some videos
        fitnessChannel.uploadVideo("5 Exercises for Perfect Abs");
        newsChannel.uploadVideo("Breaking: Major Tech Conference Announced");

        // Turn off notifications for Sara
        sara.setNotificationsEnabled(false);

        // Upload another video
        fitnessChannel.uploadVideo("Healthy Diet Plan for Beginners");

        // Turn notifications back on
        sara.setNotificationsEnabled(true);

        // Show notification history
        sara.showNotificationHistory();
        karan.showNotificationHistory();

        System.out.println("\n🔥 === ADVANCED DEMO COMPLETED ===\n");
    }

    /**
     * Real-world scenario demo
     */
    private static void realWorldScenarioDemo() {
        System.out.println("🌟 === REAL-WORLD SCENARIO DEMO ===");

        // Create popular channels
        Channel bollywoodChannel = new Channel("BollywoodMasala", "Latest Bollywood news and gossips");
        Channel travelChannel = new Channel("WanderlustVibes", "Travel vlogs and destination guides");

        // Create multiple subscribers
        Subscriber[] subscribers = {
                new Subscriber("Ankita", "ankita@email.com"),
                new Subscriber("Rohit", "rohit@email.com"),
                new Subscriber("Neha", "neha@email.com"),
                new Subscriber("Vikram", "vikram@email.com"),
                new Subscriber("Pooja", "pooja@email.com")
        };

        System.out.println("\n--- Mass Subscription Scenario ---");

        // Multiple subscribers subscribe to channels
        for (Subscriber subscriber : subscribers) {
            if (subscriber.getName().hashCode() % 2 == 0) {
                subscriber.subscribeToChannel(bollywoodChannel);
            }
            if (subscriber.getName().hashCode() % 3 == 0) {
                subscriber.subscribeToChannel(travelChannel);
            }
        }

        System.out.println("\n--- Viral Video Upload ---");
        bollywoodChannel.uploadVideo("SHOCKING: Celebrity Wedding Leaked Photos!");

        System.out.println("\n--- Travel Vlog Upload ---");
        travelChannel.uploadVideo("Solo Trip to Ladakh - Most Beautiful Places");

        System.out.println("\n--- Subscriber Management ---");

        // Some subscribers unsubscribe
        subscribers[0].showSubscribedChannels();
        subscribers[0].unsubscribeFromChannel(bollywoodChannel);
        subscribers[0].showSubscribedChannels();

        // Mass unsubscribe
        subscribers[1].unsubscribeFromAllChannels();

        System.out.println("\n--- Final Channel Statistics ---");
        bollywoodChannel.showChannelStats();
        travelChannel.showChannelStats();

        System.out.println("\n--- Subscriber Profiles ---");
        for (Subscriber subscriber : subscribers) {
            subscriber.showProfile();
        }

        System.out.println("\n🌟 === REAL-WORLD DEMO COMPLETED ===\n");
    }

    /**
     * Sleep utility method for smooth demo flow
     */
    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}