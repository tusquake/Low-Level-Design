import java.util.ArrayList;
import java.util.List;

/**
 * A thread-safe Object Pool for DBConnections.
 * This implementation includes blocking behavior: if the pool is full, 
 * the thread will wait until a connection is released.
 */
public class DBConnectionPool {
    private final List<DBConnection> freeConnections = new ArrayList<>();
    private final List<DBConnection> usedConnections = new ArrayList<>();
    private final int maxPoolSize;
    private static DBConnectionPool instance;

    private DBConnectionPool(int initialSize, int maxSize) {
        this.maxPoolSize = maxSize;
        for (int i = 0; i < initialSize; i++) {
            freeConnections.add(new DBConnection("Conn-" + (i + 1)));
        }
    }

    public static synchronized DBConnectionPool getInstance(int initialSize, int maxSize) {
        if (instance == null) {
            instance = new DBConnectionPool(initialSize, maxSize);
        }
        return instance;
    }

    /**
     * Acquires a connection. Blocks if max size is reached and no connections are free.
     */
    public synchronized DBConnection acquireConnection() {
        while (freeConnections.isEmpty() && usedConnections.size() >= maxPoolSize) {
            try {
                System.out.println("--- [Pool] Pool full. Thread " + Thread.currentThread().getName() + " is waiting...");
                wait(); // Wait until someone releases a connection
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        DBConnection conn;
        if (!freeConnections.isEmpty()) {
            conn = freeConnections.remove(freeConnections.size() - 1);
        } else {
            // Pool not full yet, create a new one lazily
            int nextId = usedConnections.size() + freeConnections.size() + 1;
            conn = new DBConnection("Conn-" + nextId);
        }

        usedConnections.add(conn);
        System.out.println("+++ [Pool] Acquired " + conn.getId() + ". (Free: " + freeConnections.size() + ", Used: " + usedConnections.size() + ")");
        return conn;
    }

    public synchronized void releaseConnection(DBConnection conn) {
        if (conn != null) {
            usedConnections.remove(conn);
            freeConnections.add(conn);
            System.out.println("--- [Pool] Released " + conn.getId() + ". (Free: " + freeConnections.size() + ", Used: " + usedConnections.size() + ")");
            notifyAll(); // Wake up threads waiting for a connection
        }
    }
}
