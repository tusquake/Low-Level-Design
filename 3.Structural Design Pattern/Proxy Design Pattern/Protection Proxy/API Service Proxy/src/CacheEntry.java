import java.util.HashMap;
import java.util.Map;

public class CacheEntry {
    private Map<String, Object> data;
    private long timestamp;

    public CacheEntry(Map<String, Object> data) {
        this.data = new HashMap<>(data);
        this.timestamp = System.currentTimeMillis();
    }

    public Map<String, Object> getData() {
        return new HashMap<>(data);
    }

    public boolean isValid(long timeoutMs) {
        return (System.currentTimeMillis() - timestamp) < timeoutMs;
    }

    public void updateData(Map<String, Object> newData) {
        this.data.putAll(newData);
        this.timestamp = System.currentTimeMillis();
    }
}
