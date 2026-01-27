# Observer + Command Pattern: YouTube Subscription System

## Simple Analogy

**YouTube Channel:**
- Creator uploads video (Command)
- All subscribers get notified automatically (Observer)
- Creator can undo/delete video (Command undo)

One upload → Millions notified instantly

---

## Implementation

### 1. Command Interface

```java
public interface ChannelCommand {
    void execute();
    void undo();
}
```

### 2. Upload Video Command

```java
public class UploadVideoCommand implements ChannelCommand {
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
```

### 3. Subscriber Interface (Observer)

```java
public interface Subscriber {
    void notify(String channelName, String videoTitle);
}
```

### 4. Concrete Subscribers

```java
public class EmailSubscriber implements Subscriber {
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

public class MobileSubscriber implements Subscriber {
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
```

### 5. Channel (Subject)

```java
public class Channel {
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
```

### 6. Demo

```java
public class YouTubeDemo {
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
```

---

## Output

```
🎥 Tech Guru uploaded: Java Design Patterns Tutorial
Notifying 3 subscribers...
📧 Email to Alice: Tech Guru uploaded 'Java Design Patterns Tutorial'
📱 Push to Bob: Tech Guru uploaded 'Java Design Patterns Tutorial'
📱 Push to Charlie: Tech Guru uploaded 'Java Design Patterns Tutorial'

🎥 Tech Guru uploaded: System Design Interview Tips
Notifying 2 subscribers...
📧 Email to Alice: Tech Guru uploaded 'System Design Interview Tips'
📱 Push to Bob: Tech Guru uploaded 'System Design Interview Tips'

🗑️ Deleted: System Design Interview Tips
```

---

## Key Benefits

**Command Pattern:**
- Encapsulates "upload" action
- Can undo (delete video)
- Can queue uploads

**Observer Pattern:**
- Subscribers auto-notified
- Easy to add/remove subscribers
- Channel doesn't know subscriber details

**Together:**
- Upload once → All notified automatically
- Can undo → Clean up easily
- Decoupled and flexible

---

## Real-World Uses

- **Spotify**: Artist releases song → Followers notified
- **Netflix**: New episode → Watchlist subscribers notified
- **Instagram**: New post → Followers see in feed
- **Twitch**: Streamer goes live → Subscribers alerted

---

## Quick Summary

| Pattern | What It Does |
|---------|-------------|
| Command | Encapsulates upload/post actions with undo |
| Observer | Auto-notifies all subscribers |
| Combined | One action → Everyone updated automatically |