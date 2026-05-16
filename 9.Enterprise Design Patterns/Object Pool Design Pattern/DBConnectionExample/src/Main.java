public class Main {
    public static void main(String[] args) {
        // Initialize pool: 2 initial, 3 max
        DBConnectionPool pool = DBConnectionPool.getInstance(2, 3);

        // Task to simulate a database operation
        Runnable task = () -> {
            String threadName = Thread.currentThread().getName();
            DBConnection conn = pool.acquireConnection();
            
            if (conn != null) {
                conn.executeQuery("Query from " + threadName);
                try {
                    // Simulate work
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pool.releaseConnection(conn);
            }
        };

        // Create 5 threads trying to use 3 connections
        for (int i = 1; i <= 5; i++) {
            new Thread(task, "User-" + i).start();
        }
    }
}
