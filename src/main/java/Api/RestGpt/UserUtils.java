package Api.RestGpt;

import java.util.HashMap;
import java.util.Map;

public class UserUtils {
    private static Map<String, User> users = new HashMap<>();

    static {
        // Load users from a configuration file instead of hardcoding
        initializeUsers();
    }

    private static void initializeUsers() {
        // This is a placeholder. Implement loading logic here,
        // For example, load from a properties file, JSON file, etc.
        User user1 = new User();
        user1.setProperty("userName", "es64725");
        user1.setProperty("password", "P@ssw0rd");

        User user2 = new User();
        user2.setProperty("userName", "ES0101");
        user2.setProperty("password", "P@ssw0rd");

        users.put("user1", user1);
        users.put("user2", user2);
    }

    public static User getUser(String userId) {
        return users.get(userId);
    }
}
