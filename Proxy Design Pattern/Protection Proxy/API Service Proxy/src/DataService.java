import java.util.Map;

public interface DataService {
    Map<String, Object> getUserData(String userId) throws Exception;
    boolean updateUserData(String userId, Map<String, Object> data) throws Exception;
}
