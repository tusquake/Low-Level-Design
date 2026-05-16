public class DBConnection {
    private String id;

    public DBConnection(String id) {
        this.id = id;
        // Simulate expensive connection creation
        System.out.println(">>> [System] Establishing expensive database connection: " + id);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void executeQuery(String sql) {
        System.out.println(">>> [Connection " + id + "] Executing: " + sql);
    }

    public String getId() {
        return id;
    }
}
