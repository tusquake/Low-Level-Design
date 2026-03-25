# Meeting Scheduler System

## Problem Statement
Design a system to schedule meetings and send reminders at specific times.

**Requirements:**
- Schedule one-time meetings
- Send reminders (15 min before, 1 hour before)
- Handle recurring meetings (daily standups, weekly reviews)
- Cancel meetings
- Notify participants

## Architecture

```
User schedules meeting
        |
        v
MeetingScheduler creates tasks:
  - Reminder task (15 min before)
  - Start task (at meeting time)
  - End task (after duration)
        |
        v
Tasks added to PriorityQueue (ordered by time)
        |
        v
Scheduler thread polls queue
        |
        v
ExecutorService executes tasks
```

## Core Components

### 1. Meeting
```java
public class Meeting {
    private String id;
    private String title;
    private String organizer;
    private List<String> participants;
    private long startTime;
    private int duration; // minutes
    private boolean recurring;
    private long recurringInterval;
    
    public Meeting(String title, String organizer, List<String> participants, 
                   long startTime, int duration) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.organizer = organizer;
        this.participants = participants;
        this.startTime = startTime;
        this.duration = duration;
        this.recurring = false;
    }
    
    public void setRecurring(long intervalMs) {
        this.recurring = true;
        this.recurringInterval = intervalMs;
    }
}
```

### 2. MeetingTask
```java
public class MeetingTask implements Comparable<MeetingTask> {
    private Meeting meeting;
    private TaskType type;
    private long executionTime;
    
    public enum TaskType {
        SEND_REMINDER,
        START_MEETING,
        END_MEETING
    }
    
    public MeetingTask(Meeting meeting, TaskType type, long executionTime) {
        this.meeting = meeting;
        this.type = type;
        this.executionTime = executionTime;
    }
    
    @Override
    public int compareTo(MeetingTask other) {
        return Long.compare(this.executionTime, other.executionTime);
    }
}
```

### 3. MeetingScheduler
```java
public class MeetingScheduler {
    private PriorityQueue<MeetingTask> taskQueue;
    private ExecutorService executor;
    private Thread schedulerThread;
    private volatile boolean running;
    private Map<String, Meeting> meetings;
    
    public MeetingScheduler(int threadPoolSize) {
        this.taskQueue = new PriorityQueue<>();
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
        this.meetings = new ConcurrentHashMap<>();
    }
    
    public String scheduleMeeting(Meeting meeting) {
        meetings.put(meeting.getId(), meeting);
        
        // Schedule reminder 15 min before
        long reminderTime = meeting.getStartTime() - (15 * 60 * 1000);
        addTask(new MeetingTask(meeting, TaskType.SEND_REMINDER, reminderTime));
        
        // Schedule meeting start
        addTask(new MeetingTask(meeting, TaskType.START_MEETING, meeting.getStartTime()));
        
        // Schedule meeting end
        long endTime = meeting.getStartTime() + (meeting.getDuration() * 60 * 1000);
        addTask(new MeetingTask(meeting, TaskType.END_MEETING, endTime));
        
        return meeting.getId();
    }
    
    private void addTask(MeetingTask task) {
        synchronized (taskQueue) {
            taskQueue.offer(task);
            taskQueue.notify();
        }
    }
    
    public void start() {
        running = true;
        schedulerThread = new Thread(this::run);
        schedulerThread.start();
    }
    
    private void run() {
        while (running) {
            synchronized (taskQueue) {
                while (taskQueue.isEmpty() && running) {
                    try {
                        taskQueue.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                
                if (!running) break;
                
                MeetingTask task = taskQueue.peek();
                long currentTime = System.currentTimeMillis();
                
                if (task.getExecutionTime() <= currentTime) {
                    taskQueue.poll();
                    executor.submit(() -> executeTask(task));
                } else {
                    long waitTime = task.getExecutionTime() - currentTime;
                    try {
                        taskQueue.wait(waitTime);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }
    
    private void executeTask(MeetingTask task) {
        Meeting meeting = task.getMeeting();
        
        switch (task.getType()) {
            case SEND_REMINDER:
                sendReminder(meeting);
                break;
            case START_MEETING:
                startMeeting(meeting);
                break;
            case END_MEETING:
                endMeeting(meeting);
                break;
        }
    }
    
    private void sendReminder(Meeting meeting) {
        System.out.println("REMINDER: " + meeting.getTitle() + " starting in 15 minutes");
        for (String participant : meeting.getParticipants()) {
            System.out.println("  Notifying: " + participant);
        }
    }
    
    private void startMeeting(Meeting meeting) {
        System.out.println("MEETING STARTED: " + meeting.getTitle());
        System.out.println("  Organizer: " + meeting.getOrganizer());
        System.out.println("  Participants: " + meeting.getParticipants());
        System.out.println("  Zoom link: https://zoom.us/j/" + meeting.getId());
        
        // Reschedule if recurring
        if (meeting.isRecurring()) {
            long nextTime = meeting.getStartTime() + meeting.getRecurringInterval();
            meeting.setStartTime(nextTime);
            scheduleMeeting(meeting);
        }
    }
    
    private void endMeeting(Meeting meeting) {
        System.out.println("MEETING ENDED: " + meeting.getTitle());
        System.out.println("  Sending summary to participants");
    }
    
    public boolean cancelMeeting(String meetingId) {
        Meeting meeting = meetings.remove(meetingId);
        if (meeting == null) return false;
        
        synchronized (taskQueue) {
            taskQueue.removeIf(task -> task.getMeeting().getId().equals(meetingId));
        }
        return true;
    }
    
    public void shutdown() {
        running = false;
        synchronized (taskQueue) {
            taskQueue.notify();
        }
        executor.shutdown();
    }
}
```

## Usage Example

```java
public class Demo {
    public static void main(String[] args) throws InterruptedException {
        MeetingScheduler scheduler = new MeetingScheduler(5);
        scheduler.start();
        
        long now = System.currentTimeMillis();
        
        // One-time meeting
        Meeting demo = new Meeting(
            "Client Demo",
            "john@company.com",
            Arrays.asList("client@example.com", "sarah@company.com"),
            now + 5000,  // 5 seconds from now
            30           // 30 minutes duration
        );
        scheduler.scheduleMeeting(demo);
        
        // Recurring daily standup
        Meeting standup = new Meeting(
            "Daily Standup",
            "manager@company.com",
            Arrays.asList("dev1@company.com", "dev2@company.com"),
            now + 10000,
            15
        );
        standup.setRecurring(24 * 60 * 60 * 1000); // Daily
        scheduler.scheduleMeeting(standup);
        
        Thread.sleep(60000); // Let it run
        scheduler.shutdown();
    }
}
```

## Design Patterns Used

### 1. Command Pattern
- **Where**: MeetingTask encapsulates actions
- **Why**: Decouple task creation from execution

### 2. Producer-Consumer Pattern
- **Where**: User adds tasks, scheduler executes them
- **Why**: Separate scheduling from execution

### 3. Priority Queue Pattern
- **Where**: Tasks ordered by execution time
- **Why**: Always get next task in O(log n)

### 4. Observer Pattern
- **Where**: wait()/notify() mechanism
- **Why**: Scheduler wakes up when new tasks arrive

### 5. Thread Pool Pattern
- **Where**: ExecutorService
- **Why**: Efficient thread reuse

## Key Data Structures

### PriorityQueue (Min-Heap)
```java
private PriorityQueue<MeetingTask> taskQueue = new PriorityQueue<>();
```
- Automatically orders tasks by execution time
- peek() gives earliest task in O(1)
- offer() and poll() in O(log n)

### ConcurrentHashMap
```java
private Map<String, Meeting> meetings = new ConcurrentHashMap<>();
```
- Thread-safe storage of active meetings
- Fast lookup for cancellation

## Extension Points

### Multiple Reminder Times
```java
public class Meeting {
    private List<Long> reminderOffsets; // 15min, 1hour, 1day
    
    public void addReminder(long offsetMs) {
        reminderOffsets.add(offsetMs);
    }
}
```

### Priority Meetings
```java
public class MeetingTask implements Comparable<MeetingTask> {
    private int priority;
    
    @Override
    public int compareTo(MeetingTask other) {
        if (this.priority != other.priority) {
            return Integer.compare(other.priority, this.priority);
        }
        return Long.compare(this.executionTime, other.executionTime);
    }
}
```

### Notification Channels
```java
public interface NotificationService {
    void sendEmail(Meeting meeting);
    void sendSMS(Meeting meeting);
    void sendSlack(Meeting meeting);
}
```

### Meeting Rooms
```java
public class MeetingRoom {
    private String id;
    private String name;
    private int capacity;
    private boolean available;
}

public class RoomManager {
    public MeetingRoom bookRoom(Meeting meeting) {
        // Find available room
        // Mark as booked
        // Add to meeting
    }
}
```

## Testing

```java
@Test
public void testMeetingScheduling() throws InterruptedException {
    MeetingScheduler scheduler = new MeetingScheduler(2);
    scheduler.start();
    
    long now = System.currentTimeMillis();
    Meeting meeting = new Meeting("Test", "org@test.com", 
        Arrays.asList("p1@test.com"), now + 100, 5);
    
    String meetingId = scheduler.scheduleMeeting(meeting);
    assertNotNull(meetingId);
    
    Thread.sleep(200); // Wait for reminder
    // Verify reminder was sent
    
    scheduler.shutdown();
}

@Test
public void testMeetingCancellation() {
    MeetingScheduler scheduler = new MeetingScheduler(2);
    
    Meeting meeting = new Meeting("Test", "org@test.com", 
        Arrays.asList("p1@test.com"), 
        System.currentTimeMillis() + 10000, 30);
    
    String id = scheduler.scheduleMeeting(meeting);
    boolean cancelled = scheduler.cancelMeeting(id);
    
    assertTrue(cancelled);
}
```

## Real-World Systems

- **Google Calendar**: Similar scheduling mechanism
- **Outlook**: Meeting reminders and recurring events
- **Calendly**: Automated meeting scheduling
- **Zoom**: Auto-start meetings at scheduled time

## Common Interview Questions

**Q: How do you handle timezone differences?**
A: Store all times in UTC, convert to user timezone when displaying

**Q: What if server restarts?**
A: Persist meetings to database, reload on startup

**Q: How to handle conflicting meetings?**
A: Check availability before scheduling, maintain calendar slots

**Q: How to optimize for millions of meetings?**
A: Partition by time ranges, use distributed queue (Kafka/RabbitMQ)