import java.util.*;
import java.util.stream.Collectors;

/**
 * Instagram Design Demo
 * Core Concepts: User, Post, Feed, Observer Pattern, Strategy Pattern.
 */

// 1. Observer Pattern and Strategy Interfaces
interface PostObserver {
    void update(Post post);
}

interface FeedRankingStrategy {
    List<Post> rank(List<Post> posts);
}

// 2. Core Entities
abstract class Post {
    private String postId;
    private String userId;
    private String content;
    private long timestamp;
    private List<User> likes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();

    public Post(String postId, String userId, String content) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public abstract void display();

    // Getters and helper methods
    public String getPostId() { return postId; }
    public String getUserId() { return userId; }
    public String getContent() { return content; }
    public long getTimestamp() { return timestamp; }
    public void addLike(User user) { likes.add(user); }
    public void addComment(Comment comment) { comments.add(comment); }
    public int getLikeCount() { return likes.size(); }
}

class ImagePost extends Post {
    private String imageUrl;
    public ImagePost(String postId, String userId, String content, String imageUrl) {
        super(postId, userId, content);
        this.imageUrl = imageUrl;
    }
    @Override
    public void display() {
        System.out.println("[IMAGE POST] " + getContent() + " | URL: " + imageUrl);
    }
}

class VideoPost extends Post {
    private String videoUrl;
    public VideoPost(String postId, String userId, String content, String videoUrl) {
        super(postId, userId, content);
        this.videoUrl = videoUrl;
    }
    @Override
    public void display() {
        System.out.println("[VIDEO POST] " + getContent() + " | URL: " + videoUrl);
    }
}

class Comment {
    private String commentId;
    private String userId;
    private String text;
    public Comment(String commentId, String userId, String text) {
        this.commentId = commentId;
        this.userId = userId;
        this.text = text;
    }
}

// 3. User Entity (acts as Subject in Observer Pattern)
class User implements PostObserver {
    private String userId;
    private String username;
    private Set<User> followers = new HashSet<>();
    private Set<User> following = new HashSet<>();
    private List<Post> myPosts = new ArrayList<>();

    public User(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public void follow(User user) {
        following.add(user);
        user.addFollower(this);
    }

    private void addFollower(User user) {
        followers.add(user);
    }

    public void createPost(Post post) {
        myPosts.add(post);
        System.out.println(username + " created a post: " + post.getContent());
        notifyFollowers(post);
    }

    private void notifyFollowers(Post post) {
        for (User follower : followers) {
            follower.update(post);
        }
    }

    @Override
    public void update(Post post) {
        System.out.println("Notification for " + username + ": Your friend created a post -> " + post.getContent());
    }

    public String getUsername() { return username; }
    public Set<User> getFollowing() { return following; }
    public List<Post> getMyPosts() { return myPosts; }
}

// 4. Strategy Implementation
class ChronologicalRanking implements FeedRankingStrategy {
    @Override
    public List<Post> rank(List<Post> posts) {
        return posts.stream()
                .sorted((p1, p2) -> Long.compare(p2.getTimestamp(), p1.getTimestamp()))
                .collect(Collectors.toList());
    }
}

// 5. Feed Service
class FeedService {
    private FeedRankingStrategy rankingStrategy;

    public FeedService(FeedRankingStrategy strategy) {
        this.rankingStrategy = strategy;
    }

    public void generateFeed(User user) {
        System.out.println("\n--- Generating Feed for " + user.getUsername() + " ---");
        List<Post> allPosts = new ArrayList<>();
        
        // Pull posts from everyone the user follows (Pull Model)
        for (User followedUser : user.getFollowing()) {
            allPosts.addAll(followedUser.getMyPosts());
        }

        List<Post> rankedPosts = rankingStrategy.rank(allPosts);
        for (Post post : rankedPosts) {
            post.display();
        }
    }
}

// 6. Main Demo
public class InstagramDesignDemo {
    public static void main(String[] args) {
        System.out.println("=== INSTAGRAM LLD DEMO ===\n");

        // 1. Setup Users
        User alice = new User("1", "Alice");
        User bob = new User("2", "Bob");
        User charlie = new User("3", "Charlie");

        // 2. Establish Relationships
        alice.follow(bob);
        alice.follow(charlie);
        bob.follow(charlie);

        // 3. Create Posts
        Post post1 = new ImagePost("p1", "2", "My new coffee!", "coffee.jpg");
        bob.createPost(post1);

        Post post2 = new VideoPost("p2", "3", "Coding in Java", "java.mp4");
        charlie.createPost(post2);

        // 4. Interaction
        post1.addLike(alice);

        // 5. Generate Feed
        FeedService feedService = new FeedService(new ChronologicalRanking());
        feedService.generateFeed(alice);

        System.out.println("\nInstagram Demo Completed.");
    }
}
