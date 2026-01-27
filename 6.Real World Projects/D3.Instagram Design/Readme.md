# Instagram/Facebook System Design - Interview Guide

## 30-Second Explanation (Opening Statement)

"Instagram/Facebook is a social media platform where users can post content, follow others, like and comment on posts, and view a personalized feed. For the low-level design, I'll focus on core features like User, Post, Comment, Like, and NewsFeed using appropriate design patterns. For scalability, I'll discuss database sharding, caching, CDN for media, and async processing for feed generation."

---

## Questions YOU Should Ask Interviewer

### Functional Requirements
1. Which features to focus on?
    - Post creation (text, image, video)?
    - News feed generation?
    - Follow/Unfollow users?
    - Likes and Comments?
    - Direct messaging?
    - Stories (24-hour content)?

2. What's the scale?
    - How many users? (100K, 1M, 1B?)
    - How many posts per day?
    - Average feed size?

3. What's the priority?
    - Read-heavy or write-heavy?
    - Real-time updates needed?
    - Consistency vs Availability?

### Non-Functional Requirements
1. Should the feed be real-time or eventual consistency is okay?
2. How to handle media files (images/videos)?
3. What about privacy settings (public, private, friends-only)?
4. Should we support hashtags and mentions?

---

## Core Components to Design

### 1. Low-Level Design (LLD) - Classes and Relationships

```
User
  - userId, username, email
  - followers, following
  - posts

Post
  - postId, userId, content, mediaUrl
  - timestamp, likes, comments
  - privacy settings

Comment
  - commentId, postId, userId
  - content, timestamp
  - replies (nested comments)

Like
  - likeId, postId, userId
  - timestamp

NewsFeed
  - generates feed for user
  - ranks posts by relevance
```

### 2. High-Level Design (HLD) - System Architecture

```
Client (Mobile/Web)
      ↓
Load Balancer
      ↓
API Gateway
      ↓
Microservices:
  - User Service
  - Post Service
  - Feed Service
  - Notification Service
  - Media Service
      ↓
Databases:
  - User DB (SQL - PostgreSQL)
  - Post DB (NoSQL - Cassandra)
  - Feed DB (Redis Cache)
  - Media Storage (S3 + CDN)
      ↓
Message Queue (Kafka)
  - Async feed generation
  - Notifications
```

---

## Key Design Decisions

### 1. Database Choice

**User Data**: SQL (PostgreSQL)
- Structured data
- ACID properties needed
- Relational (followers/following)

**Posts/Comments**: NoSQL (Cassandra/MongoDB)
- High write throughput
- Horizontal scaling
- Time-series data

**Feed Cache**: Redis
- Fast reads
- Pre-computed feeds
- TTL support

**Media Storage**: S3 + CloudFront CDN
- Scalable storage
- Fast global delivery
- Cost-effective

### 2. Feed Generation Strategy

**Push Model (Write-Heavy)**
- Pre-compute feed when post is created
- Write to all followers' feeds
- Fast reads, slow writes
- Good for: Users with few followers

**Pull Model (Read-Heavy)**
- Compute feed when user opens app
- Fetch posts from followed users
- Slow reads, fast writes
- Good for: Celebrity accounts

**Hybrid Model (Instagram/Facebook)**
- Push for regular users
- Pull for celebrities
- Cache popular posts
- Lazy loading

### 3. Ranking Algorithm

**Simple Time-Based**
- Show latest posts first
- Easy to implement

**Engagement-Based**
- Rank by likes, comments, shares
- Show popular content first

**Personalized (ML-Based)**
- User interests
- Past engagement
- Time spent on similar posts
- Friend interactions

---

## Design Patterns Used

### 1. Observer Pattern
- Notify followers when user posts
- Real-time notifications

### 2. Strategy Pattern
- Different feed generation strategies
- Different ranking algorithms

### 3. Factory Pattern
- Create different post types (text, image, video)
- Create different notification types

### 4. Composite Pattern
- Nested comments (replies to replies)
- Comment tree structure

### 5. Singleton Pattern
- Database connection pool
- Cache manager

---

## Scalability Considerations

### 1. Horizontal Scaling
- Add more servers behind load balancer
- Stateless services
- Session management in Redis

### 2. Database Sharding
**User Sharding**: By userId % number_of_shards
**Post Sharding**: By postId or timestamp
**Feed Sharding**: By userId

### 3. Caching Strategy
- Cache hot posts (viral content)
- Cache user feeds (Redis)
- CDN for media files
- Application-level cache (in-memory)

### 4. Async Processing
- Use message queues (Kafka/RabbitMQ)
- Feed generation in background
- Notification delivery
- Analytics processing

### 5. Content Delivery
- CDN for images/videos (CloudFront, Cloudflare)
- Multiple data centers (geo-distribution)
- Image compression and optimization
- Lazy loading

---

## Expected Cross-Questions

### Question 1: How do you handle celebrity accounts with millions of followers?

**Answer**:
- Use Pull model for celebrities (fanout-on-load)
- Cache their posts separately
- Don't push to all followers
- Lazy load when followers open app
- Use pagination (show 20 posts, load more on scroll)

### Question 2: How to make the feed generation faster?

**Answer**:
- Pre-compute feeds for active users
- Use Redis cache for feeds
- Rank only top 1000 posts, not all
- Async background jobs for feed refresh
- Pagination (don't load entire feed at once)

### Question 3: How to handle millions of writes per second?

**Answer**:
- Use write-optimized database (Cassandra)
- Batch writes using message queue
- Database sharding
- Master-slave replication (write to master, read from slaves)
- Eventually consistent model

### Question 4: How to store and serve images/videos efficiently?

**Answer**:
- Store in S3 or blob storage
- Use CDN for global distribution
- Compress images on upload
- Store multiple sizes (thumbnail, medium, full)
- Lazy loading and progressive image loading
- Video: Adaptive bitrate streaming (HLS)

### Question 5: How to handle likes and comments at scale?

**Answer**:
- Store counts in separate table/cache
- Increment counters asynchronously
- Use Redis for real-time counts
- Eventual consistency (count may be slightly off)
- Batch updates to database

### Question 6: How do you prevent duplicate posts or spam?

**Answer**:
- Rate limiting per user
- Content moderation (ML models)
- Hash-based duplicate detection
- User reputation system
- Report and block features

### Question 7: How to implement real-time notifications?

**Answer**:
- WebSockets for real-time push
- Firebase Cloud Messaging (FCM) for mobile
- Notification service using Kafka
- Store notifications in database
- Mark as read/unread

### Question 8: How would you design the messaging feature?

**Answer**:
- Separate service (Message Service)
- WebSocket connections for real-time chat
- Store messages in Cassandra (time-series)
- Redis for online/offline status
- End-to-end encryption

### Question 9: How to handle privacy settings (public, friends-only, private)?

**Answer**:
- Store privacy level with each post
- Filter feed based on relationship and privacy
- Check permissions before showing post
- Cache permission checks

### Question 10: What if database goes down?

**Answer**:
- Master-slave replication (automatic failover)
- Multiple database replicas
- Read from slaves if master down
- Queue writes until master recovers
- Regular backups

---

## Data Models

### User Table (SQL)
```sql
users
  - user_id (PK)
  - username (unique)
  - email (unique)
  - password_hash
  - bio
  - profile_pic_url
  - created_at
  - updated_at
```

### Followers Table (SQL)
```sql
followers
  - follower_id (PK)
  - user_id (FK) - who is being followed
  - follower_user_id (FK) - who is following
  - created_at
```

### Posts Table (NoSQL - Cassandra)
```
posts
  - post_id (PK)
  - user_id
  - content
  - media_urls[]
  - post_type (text/image/video)
  - privacy (public/private/friends)
  - created_at
  - updated_at
```

### Likes Table (NoSQL)
```
likes
  - post_id (PK)
  - user_id (PK)
  - created_at
```

### Comments Table (NoSQL)
```
comments
  - comment_id (PK)
  - post_id
  - user_id
  - parent_comment_id (for nested replies)
  - content
  - created_at
```

### Feed Cache (Redis)
```
Key: user_id:feed
Value: [post_id1, post_id2, post_id3...]
TTL: 15 minutes
```

---

## API Endpoints (REST)

### User APIs
```
POST   /api/users/register
POST   /api/users/login
GET    /api/users/{userId}
PUT    /api/users/{userId}
POST   /api/users/{userId}/follow
DELETE /api/users/{userId}/unfollow
GET    /api/users/{userId}/followers
GET    /api/users/{userId}/following
```

### Post APIs
```
POST   /api/posts
GET    /api/posts/{postId}
PUT    /api/posts/{postId}
DELETE /api/posts/{postId}
GET    /api/posts?userId={userId}
```

### Feed APIs
```
GET    /api/feed?userId={userId}&page={page}
```

### Engagement APIs
```
POST   /api/posts/{postId}/like
DELETE /api/posts/{postId}/unlike
POST   /api/posts/{postId}/comments
GET    /api/posts/{postId}/comments
DELETE /api/comments/{commentId}
```

---

## Performance Metrics

### Target Metrics
- Feed load time: < 500ms
- Post creation: < 200ms
- Like/Comment: < 100ms
- Image upload: < 2s
- 99th percentile latency: < 1s

### Monitoring
- Request rate per second
- Error rates
- Database query time
- Cache hit ratio
- CDN performance

---

## Trade-offs and Considerations

### Consistency vs Availability
- Feed: Eventual consistency (okay if slightly delayed)
- Likes/Comments: Eventual consistency
- User data: Strong consistency
- CAP Theorem: Choose AP (Availability + Partition tolerance)

### Storage Costs
- Media files are expensive
- Compress images/videos
- Delete old/unused content
- Use cheaper storage tiers for old posts

### Network Bandwidth
- CDN reduces bandwidth costs
- Image compression
- Video streaming (not download)
- Lazy loading

---

## Summary - One-Line Answer

"I designed Instagram using microservices architecture with User, Post, Feed, and Notification services. Used SQL for user data, NoSQL for posts, Redis for feed cache, S3+CDN for media, and implemented a hybrid push-pull feed generation strategy with ranking algorithms for personalization."

---

## SOLID Principles Applied

1. **SRP**: Each class has single responsibility (User, Post, Comment)
2. **OCP**: Open for extension (new post types, new algorithms)
3. **LSP**: Post subtypes (TextPost, ImagePost, VideoPost)
4. **ISP**: Separate interfaces (Likeable, Commentable)
5. **DIP**: Depend on abstractions (FeedStrategy, RankingAlgorithm)

---

## Additional Features to Discuss (If Time Permits)

1. Stories (24-hour content)
2. Live streaming
3. Direct messaging
4. Search functionality
5. Hashtags and trending topics
6. User verification (blue tick)
7. Ad serving system
8. Analytics and insights
9. Content moderation
10. Recommendation engine