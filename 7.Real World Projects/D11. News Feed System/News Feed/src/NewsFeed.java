import java.util.*;

public class NewsFeed {
    public static void main(String[] args) {
        // Create Feed Service (Singleton)
        FeedService feedService = FeedService.getInstance();

        // Create Users
        User alice = new User("U1", "Alice");
        User bob = new User("U2", "Bob");
        User charlie = new User("U3", "Charlie");

        // Register Users
        feedService.addUser(alice);
        feedService.addUser(bob);
        feedService.addUser(charlie);

        // Users follow each other
        alice.follow("U2"); // Alice follows Bob
        alice.follow("U3"); // Alice follows Charlie
        bob.follow("U3");   // Bob follows Charlie

        // Users create posts
        feedService.addPost(new Post("P1", "U2", "Bob's first post"));
        sleep(1000);
        feedService.addPost(new Post("P2", "U3", "Charlie's first post"));
        sleep(1000);
        feedService.addPost(new Post("P3", "U2", "Bob's second post"));
        sleep(1000);
        feedService.addPost(new Post("P4", "U3", "Charlie's second post"));

        // Fetch Alice's feed
        System.out.println("\nAlice's Feed:");
        List<Post> aliceFeed = feedService.getFeed("U1");
        for(Post post : aliceFeed){
            System.out.println(post);
        }

        // Fetch Bob's feed
        System.out.println("\nBob's Feed:");
        List<Post> bobFeed = feedService.getFeed("U2");
        for(Post post : bobFeed){
            System.out.println(post);
        }

        // Fetch Charlie's feed
        System.out.println("\nCharlie's Feed:");
        List<Post> charlieFeed = feedService.getFeed("U3");
        for(Post post : charlieFeed){
            System.out.println(post);
        }
    }

    // Helper to simulate time difference between posts
    private static void sleep(int millis){
        try { Thread.sleep(millis); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}

// ------------------------ User ------------------------
class User {
    private String userId;
    private String name;
    private Set<String> followees = new HashSet<>();

    public User(String userId, String name){
        this.userId = userId;
        this.name = name;
    }

    public void follow(String userId){ followees.add(userId); }
    public void unfollow(String userId){ followees.remove(userId); }
    public Set<String> getFollowees(){ return followees; }
    public String getUserId(){ return userId; }
    public String getName(){ return name; }
}

// ------------------------ Post ------------------------
class Post {
    private String postId;
    private String userId;
    private String content;
    private long timestamp;

    public Post(String postId, String userId, String content){
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserId(){ return userId; }
    public long getTimestamp(){ return timestamp; }

    @Override
    public String toString(){
        return "[" + userId + "] " + content + " (ts=" + timestamp + ")";
    }
}

// ------------------------ Feed Service (Singleton + Strategy) ------------------------
interface FeedStrategy {
    List<Post> generateFeed(User user, Map<String, List<Post>> userPosts);
}

class PullFeedStrategy implements FeedStrategy {
    @Override
    public List<Post> generateFeed(User user, Map<String, List<Post>> userPosts){
        List<Post> feed = new ArrayList<>();
        for(String followeeId : user.getFollowees()){
            List<Post> posts = userPosts.getOrDefault(followeeId, Collections.emptyList());
            feed.addAll(posts);
        }
        feed.sort((a,b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        return feed;
    }
}

class FeedService {
    private static FeedService instance;
    private Map<String, User> users = new HashMap<>();
    private Map<String, List<Post>> userPosts = new HashMap<>();
    private FeedStrategy feedStrategy = new PullFeedStrategy(); // Can switch strategy later

    private FeedService(){}

    public static FeedService getInstance(){
        if(instance == null) instance = new FeedService();
        return instance;
    }

    public void addUser(User user){ users.put(user.getUserId(), user); }

    public void addPost(Post post){
        userPosts.putIfAbsent(post.getUserId(), new ArrayList<>());
        userPosts.get(post.getUserId()).add(post);
        // Observer pattern can be applied here to notify followers (optional)
    }

    public List<Post> getFeed(String userId){
        User user = users.get(userId);
        if(user == null) return Collections.emptyList();
        return feedStrategy.generateFeed(user, userPosts);
    }
}
