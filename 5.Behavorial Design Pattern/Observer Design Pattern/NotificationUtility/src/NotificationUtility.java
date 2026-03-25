import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Notification Utility Class
 * Yeh class notification related helper functions provide karti hai
 */
public class NotificationUtility {

    // Different notification types
    public enum NotificationType {
        VIDEO_UPLOAD("🎬"),
        LIVE_STREAM("🔴"),
        COMMUNITY_POST("📝"),
        SHORTS_UPLOAD("📱");

        private final String emoji;

        NotificationType(String emoji) {
            this.emoji = emoji;
        }

        public String getEmoji() {
            return emoji;
        }
    }

    /**
     * Format notification message
     * @param type - Notification type
     * @param channelName - Channel name
     * @param content - Content title/description
     * @param subscriberName - Subscriber name
     * @return formatted notification
     */
    public static String formatNotification(NotificationType type, String channelName,
                                            String content, String subscriberName) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        return String.format("%s [%s] %s: %s uploaded '%s'",
                type.getEmoji(), timestamp, subscriberName, channelName, content);
    }

    /**
     * Create fancy notification border
     * @param message - Message to wrap
     * @return bordered message
     */
    public static String createNotificationBorder(String message) {
        int length = message.length();
        String border = "═".repeat(length + 4);

        return "╔" + border + "╗\n" +
                "║  " + message + "  ║\n" +
                "╚" + border + "╝";
    }

    /**
     * Send bulk notifications to multiple subscribers
     * @param subscribers - List of subscribers
     * @param channelName - Channel name
     * @param content - Content title
     * @param type - Notification type
     */
    public static void sendBulkNotifications(List<ISubscriber> subscribers,
                                             String channelName, String content,
                                             NotificationType type) {
        if (subscribers == null || subscribers.isEmpty()) {
            System.out.println("⚠️ No subscribers to notify!");
            return;
        }

        System.out.println("\n" + createNotificationBorder("BULK NOTIFICATION SENDING"));
        System.out.println("📊 Total subscribers to notify: " + subscribers.size());
        System.out.println("📺 Channel: " + channelName);
        System.out.println("🎬 Content: " + content);
        System.out.println("📱 Type: " + type.name());

        for (int i = 0; i < subscribers.size(); i++) {
            ISubscriber subscriber = subscribers.get(i);
            try {
                String notification = formatNotification(type, channelName, content, subscriber.getName());
                System.out.println((i + 1) + ". " + notification);

                // Simulate network delay
                Thread.sleep(50);

            } catch (Exception e) {
                System.err.println("❌ Failed to notify " + subscriber.getName() + ": " + e.getMessage());
            }
        }

        System.out.println("✅ Bulk notification completed!\n");
    }

    /**
     * Generate notification summary
     * @param channelName - Channel name
     * @param subscriberCount - Number of subscribers
     * @param type - Notification type
     * @return summary string
     */
    public static String generateNotificationSummary(String channelName, int subscriberCount,
                                                     NotificationType type) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

        return String.format(
                "📊 NOTIFICATION SUMMARY\n" +
                        "═══════════════════════════════════\n" +
                        "📺 Channel: %s\n" +
                        "👥 Subscribers Notified: %d\n" +
                        "📱 Notification Type: %s %s\n" +
                        "⏰ Timestamp: %s\n" +
                        "✅ Status: Successfully Sent\n" +
                        "═══════════════════════════════════",
                channelName, subscriberCount, type.getEmoji(), type.name(), timestamp
        );
    }

    /**
     * Create animated notification (for fun!)
     * @param message - Message to animate
     */
    public static void showAnimatedNotification(String message) {
        String[] frames = {
                "🔔 " + message,
                "🔕 " + message,
                "🔔 " + message,
                "🔕 " + message,
                "🔔 " + message
        };

        try {
            for (String frame : frames) {
                System.out.print("\r" + frame);
                Thread.sleep(300);
            }
            System.out.println(); // New line after animation
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Validate notification content
     * @param content - Content to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidNotificationContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            return false;
        }

        // Check for inappropriate length
        if (content.length() > 100) {
            System.out.println("⚠️ Notification content too long! Max 100 characters allowed.");
            return false;
        }

        // Check for minimum length
        if (content.length() < 5) {
            System.out.println("⚠️ Notification content too short! Min 5 characters required.");
            return false;
        }

        return true;
    }

    /**
     * Get notification priority based on subscriber count
     * @param subscriberCount - Number of subscribers
     * @return priority level
     */
    public static String getNotificationPriority(int subscriberCount) {
        if (subscriberCount > 1000000) {
            return "🔥 VIRAL";
        } else if (subscriberCount > 100000) {
            return "⭐ HIGH";
        } else if (subscriberCount > 10000) {
            return "📈 MEDIUM";
        } else if (subscriberCount > 1000) {
            return "📊 NORMAL";
        } else {
            return "🌱 SMALL";
        }
    }

    /**
     * Format subscriber notification with custom styling
     * @param subscriber - Subscriber object
     * @param channelName - Channel name
     * @param content - Content title
     * @param type - Notification type
     * @return formatted notification
     */
    public static String formatCustomNotification(ISubscriber subscriber, String channelName,
                                                  String content, NotificationType type) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String priority = getNotificationPriority(1000); // Default priority

        return String.format(
                "%s [%s] %s\n" +
                        "👤 To: %s\n" +
                        "📺 Channel: %s\n" +
                        "🎬 Content: %s\n" +
                        "🏷️ Priority: %s\n" +
                        "─────────────────────────────────",
                type.getEmoji(), timestamp, type.name(),
                subscriber.getName(), channelName, content, priority
        );
    }

    /**
     * Show notification statistics
     * @param totalSent - Total notifications sent
     * @param successful - Successful notifications
     * @param failed - Failed notifications
     */
    public static void showNotificationStats(int totalSent, int successful, int failed) {
        double successRate = totalSent > 0 ? (successful * 100.0) / totalSent : 0;

        System.out.println("\n📊 NOTIFICATION STATISTICS");
        System.out.println("═════════════════════════════════════");
        System.out.println("📤 Total Sent: " + totalSent);
        System.out.println("✅ Successful: " + successful);
        System.out.println("❌ Failed: " + failed);
        System.out.println("📈 Success Rate: " + String.format("%.2f%%", successRate));

        if (successRate >= 95) {
            System.out.println("🎉 Excellent delivery rate!");
        } else if (successRate >= 80) {
            System.out.println("👍 Good delivery rate!");
        } else {
            System.out.println("⚠️ Delivery rate needs improvement!");
        }
        System.out.println("═════════════════════════════════════\n");
    }

    /**
     * Create notification with emoji reactions
     * @param message - Base message
     * @param reactions - Array of emoji reactions
     * @return message with reactions
     */
    public static String addReactionsToNotification(String message, String[] reactions) {
        if (reactions == null || reactions.length == 0) {
            return message;
        }

        StringBuilder reactionString = new StringBuilder();
        for (String reaction : reactions) {
            reactionString.append(reaction).append(" ");
        }

        return message + "\n💭 Reactions: " + reactionString.toString().trim();
    }
}