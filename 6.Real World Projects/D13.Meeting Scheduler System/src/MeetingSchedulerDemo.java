import java.util.*;
import java.util.concurrent.*;

class Meeting {
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

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getOrganizer() { return organizer; }
    public List<String> getParticipants() { return participants; }
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    public int getDuration() { return duration; }
    public boolean isRecurring() { return recurring; }
    public long getRecurringInterval() { return recurringInterval; }
}

class MeetingTask implements Comparable<MeetingTask> {
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

    public Meeting getMeeting() { return meeting; }
    public TaskType getType() { return type; }
    public long getExecutionTime() { return executionTime; }

    @Override
    public int compareTo(MeetingTask other) {
        return Long.compare(this.executionTime, other.executionTime);
    }
}

class MeetingScheduler {
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

        long reminderTime = meeting.getStartTime() - (15 * 60 * 1000);
        if (reminderTime > System.currentTimeMillis()) {
            addTask(new MeetingTask(meeting, MeetingTask.TaskType.SEND_REMINDER, reminderTime));
        }

        addTask(new MeetingTask(meeting, MeetingTask.TaskType.START_MEETING, meeting.getStartTime()));

        long endTime = meeting.getStartTime() + (meeting.getDuration() * 60 * 1000);
        addTask(new MeetingTask(meeting, MeetingTask.TaskType.END_MEETING, endTime));

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
        System.out.println("\n[NOTIFICATION] REMINDER: '" + meeting.getTitle() + "' starting soon!");
        System.out.println("  Participants: " + meeting.getParticipants());
    }

    private void startMeeting(Meeting meeting) {
        System.out.println("\n[EVENT] MEETING STARTED: " + meeting.getTitle());
        System.out.println("  Organizer: " + meeting.getOrganizer());
        System.out.println("  Join here: https://zoom.us/j/" + meeting.getId());

        if (meeting.isRecurring()) {
            long nextTime = meeting.getStartTime() + meeting.getRecurringInterval();
            meeting.setStartTime(nextTime);
            scheduleMeeting(meeting);
            System.out.println("  (Recurring meeting rescheduled for next interval)");
        }
    }

    private void endMeeting(Meeting meeting) {
        System.out.println("\n[EVENT] MEETING ENDED: " + meeting.getTitle());
        System.out.println("  Sending summary to " + meeting.getParticipants().size() + " participants.");
    }

    public boolean cancelMeeting(String meetingId) {
        Meeting meeting = meetings.remove(meetingId);
        if (meeting == null) return false;

        synchronized (taskQueue) {
            taskQueue.removeIf(task -> task.getMeeting().getId().equals(meetingId));
        }
        System.out.println("\n[ACTION] Meeting '" + meeting.getTitle() + "' cancelled.");
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

public class MeetingSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== MEETING SCHEDULER SYSTEM ===\n");
        
        MeetingScheduler scheduler = new MeetingScheduler(3);
        scheduler.start();

        long now = System.currentTimeMillis();

        Meeting quickMeeting = new Meeting(
            "Quick Sync",
            "alice@company.com",
            Arrays.asList("bob@company.com"),
            now + 2000,
            1 
        );
        scheduler.scheduleMeeting(quickMeeting);

        Meeting demo = new Meeting(
            "Client Demo",
            "john@company.com",
            Arrays.asList("client@example.com", "sarah@company.com"),
            now + 5000,
            30
        );
        scheduler.scheduleMeeting(demo);

        Meeting temp = new Meeting(
            "Temporary Sync",
            "dev@company.com",
            Arrays.asList("team@company.com"),
            now + 10000,
            15
        );
        String tempId = scheduler.scheduleMeeting(temp);
        Thread.sleep(1000);
        scheduler.cancelMeeting(tempId);

        System.out.println("\nWaiting for events to trigger...");
        Thread.sleep(10000);

        scheduler.shutdown();
    }
}
