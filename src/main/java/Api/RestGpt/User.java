package Api.RestGpt;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a user with dynamic properties.
 */
public class User {
    private Map<String, Object> properties;

    /**
     * Constructs a new User with an empty properties map.
     */
    public User() {
        this.properties = new HashMap<>();
    }

    /**
     * Adds or updates a property for the user.
     * 
     * @param key The property name.
     * @param value The property value.
     */
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    /**
     * Retrieves a property value by its key.
     * 
     * @param key The property name.
     * @return The property value. Returns null if the property does not exist.
     */
    public Object getProperty(String key) {
        return properties.get(key);
    }

    /**
     * Removes a property from the user.
     * 
     * @param key The property name to remove.
     */
    public void removeProperty(String key) {
        properties.remove(key);
    }

    /**
     * Gets all user properties.
     * 
     * @return A map of all user properties.
     */
    public Map<String, Object> getProperties() {
        return properties;
    }
}
