import java.util.ArrayList;
import java.util.List;

interface ChannelCommand {
    void execute();
    void undo();
}

class UploadVideoCommand implements ChannelCommand {
    private Channel channel;
    private String videoTitle;

    public UploadVideoCommand(Channel channel, String videoTitle) {
        this.channel = channel;
        this.videoTitle = videoTitle;
    }

    @Override
    public void execute() {
        channel.uploadVideo(videoTitle);
    }

    @Override
    public void undo() {
        channel.deleteVideo(videoTitle);
    }
}

interface Subscriber {
    void notify(String channelName, String videoTitle);
}

class EmailSubscriber implements Subscriber {
    private String name;

    public EmailSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void notify(String channelName, String videoTitle) {
        System.out.println("📧 Email to " + name + ": " +
                channelName + " uploaded '" + videoTitle + "'");
    }
}

class MobileSubscriber implements Subscriber {
    private String name;

    public MobileSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void notify(String channelName, String videoTitle) {
        System.out.println("📱 Push to " + name + ": " +
                channelName + " uploaded '" + videoTitle + "'");
    }
}

class Channel {
    private String name;
    private List<Subscriber> subscribers = new ArrayList<>();

    public Channel(String name) {
        this.name = name;
    }

    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void uploadVideo(String videoTitle) {
        System.out.println("\n🎥 " + name + " uploaded: " + videoTitle);
        notifySubscribers(videoTitle);
    }

    private void notifySubscribers(String videoTitle) {
        System.out.println("Notifying " + subscribers.size() + " subscribers...");
        for (Subscriber subscriber : subscribers) {
            subscriber.notify(name, videoTitle);
        }
    }

    public void deleteVideo(String videoTitle) {
        System.out.println("\n🗑️ Deleted: " + videoTitle);
    }
}

public class SubscriptionSystem {
        public static void main(String[] args) {

            // Create channel
            Channel techChannel = new Channel("Tech Guru");

            // Create subscribers
            Subscriber alice = new EmailSubscriber("Alice");
            Subscriber bob = new MobileSubscriber("Bob");
            Subscriber charlie = new MobileSubscriber("Charlie");

            // Subscribe
            techChannel.subscribe(alice);
            techChannel.subscribe(bob);
            techChannel.subscribe(charlie);

            // Upload video
            ChannelCommand upload = new UploadVideoCommand(
                    techChannel,
                    "Java Design Patterns Tutorial"
            );
            upload.execute();

            // Someone unsubscribes
            techChannel.unsubscribe(charlie);

            // Upload another video
            ChannelCommand upload2 = new UploadVideoCommand(
                    techChannel,
                    "System Design Interview Tips"
            );
            upload2.execute();

            // Undo (delete video)
            upload2.undo();

    }
}