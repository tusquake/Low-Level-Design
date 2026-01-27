import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

enum LogLevel {
    INFO,
    ERROR,
    WARN
}

interface LoggerStrategy {
    void log(String message);
}

class InfoLogger implements LoggerStrategy {
    public void log(String message) {
        System.out.println("INFO: " + message);
    }
}

class ErrorLogger implements LoggerStrategy {
    public void log(String message) {
        System.err.println("ERROR: " + message);
    }
}

class WarnLogger implements LoggerStrategy {
    public void log(String message) {
        System.out.println("WARN: " + message);
    }
}

class LoggerFactory {

    public static LoggerStrategy getLogger(LogLevel level) {
        switch (level) {
            case INFO:
                return new InfoLogger();
            case ERROR:
                return new ErrorLogger();
            case WARN:
                return new WarnLogger();
            default:
                throw new IllegalArgumentException("Invalid log level");
        }
    }
}

class LoggerService {

    private static volatile LoggerService instance;
    private final ExecutorService executor;

    private LoggerService() {
        this.executor = Executors.newFixedThreadPool(2);
    }

    public static LoggerService getInstance() {
        if (instance == null) {
            synchronized (LoggerService.class) {
                if (instance == null) {
                    instance = new LoggerService();
                }
            }
        }
        return instance;
    }

    public void log(LogLevel level, String message) {
        LoggerStrategy logger = LoggerFactory.getLogger(level);

        executor.submit(() -> {
            logger.log(message);
        });
    }
}

public class SimpleLogger {
    public static void main(String[] args) {

        LoggerService logger = LoggerService.getInstance();

        logger.log(LogLevel.INFO, "Application started");
        logger.log(LogLevel.ERROR, "Null pointer exception");
        logger.log(LogLevel.WARN, "Memory usage high");
    }
}




