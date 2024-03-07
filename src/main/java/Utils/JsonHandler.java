package Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonHandler {

	/**
	 * Recursively updates the value of a specified key within a JSON structure.
	 * This method traverses the JSON element (whether an object or an array) and
	 * updates the value of the key if it is found. If the key is present at
	 * multiple levels or within nested structures, all occurrences of the key will
	 * be updated to the new value. This method supports both JsonObject and
	 * JsonArray types, ensuring comprehensive search and update functionality
	 * across complex JSON data structures.
	 *
	 * @param jsonElement The JSON element (object or array) to be traversed for
	 *                    updating the key's value.
	 * @param keyToUpdate The key whose value needs to be updated. This key is
	 *                    searched within the entire JSON structure.
	 * @param newValue    The new value to be assigned to the key. This value
	 *                    replaces the old value of the key wherever it is found.
	 */
//	public static void updateJsonValue(JsonElement jsonElement, String keyToUpdate, JsonElement newValue) {
//		if (jsonElement.isJsonObject()) {
//			JsonObject jsonObj = jsonElement.getAsJsonObject();
//			for (Entry<String, JsonElement> entry : jsonObj.entrySet()) {
//				if (entry.getKey().equals(keyToUpdate)) {
//					jsonObj.add(entry.getKey(), newValue);
//				} else {
//					updateJsonValue(entry.getValue(), keyToUpdate, newValue);
//				}
//			}
//		} else if (jsonElement.isJsonArray()) {
//			for (JsonElement elem : jsonElement.getAsJsonArray()) {
//				updateJsonValue(elem, keyToUpdate, newValue);
//			}
//		}
//	}

	
    /**
     * Dynamically updates specified keys with new values in a JsonObject.
     * This method is useful for scenarios where specific keys need to be updated
     * or added before sending a request, like a PUT request.
     *
     * @param jsonObject The JsonObject to be updated.
     * @param updates A map of key-value pairs where each key is the one to update, and the value is the new value.
     */
    public static void updateJsonValues(JsonObject jsonObject, Map<String, Object> updates) {
        Gson gson = new Gson();
        updates.forEach((key, value) -> {
            JsonElement newValue = gson.toJsonTree(value);
            updateJsonValue(jsonObject, key, newValue);
        });
    }
    
    /**
     * Recursively updates or adds a key with a new value within a JsonElement.
     * If the key exists, its value is updated; if not, the key is added.
     *
     * @param jsonElement The JsonElement (JsonObject or JsonArray) to update.
     * @param keyToUpdate The key to update or add.
     * @param newValue The new value to assign to the key.
     */
    public static void updateJsonValue(JsonElement jsonElement, String keyToUpdate, JsonElement newValue) {
        if (jsonElement.isJsonObject()) {
            JsonObject jsonObj = jsonElement.getAsJsonObject();
            if (jsonObj.has(keyToUpdate)) {
                jsonObj.add(keyToUpdate, newValue);
            } else {
                jsonObj.entrySet().forEach(entry -> updateJsonValue(entry.getValue(), keyToUpdate, newValue));
            }
        } else if (jsonElement.isJsonArray()) {
            jsonElement.getAsJsonArray().forEach(elem -> updateJsonValue(elem, keyToUpdate, newValue));
        }
    }
	/**
	 * Recursively searches for and retrieves the value associated with a specified
	 * key in a JSON object. This method navigates through the JSON object,
	 * including any nested objects or arrays, to find the specified key. It's
	 * designed to work with complex JSON structures where the target key may not be
	 * directly at the root level. If the key is found, its value is returned as a
	 * JsonElement, allowing for further processing or inspection. If the key is not
	 * found after traversing the entire JSON object, including its nested elements,
	 * null is returned.
	 *
	 * @param jsonObject The JSON object to search for the key. This is the starting
	 *                   point of the search.
	 * @param keyToRead  The key whose value is to be retrieved. This method
	 *                   searches the entire JSON structure for this key.
	 * @return JsonElement representing the value associated with the specified key,
	 *         or null if the key is not found. The returned JsonElement can be of
	 *         any type (e.g., JsonObject, JsonArray, JsonPrimitive).
	 */
	public static JsonElement readValueForKey(JsonObject jsonObject, String keyToRead) {
		// Directly return the value if the key exists at the root level
		if (jsonObject.has(keyToRead)) {
			return jsonObject.get(keyToRead);
		}

		// If the key is not at the root level, recursively search for the key in nested
		// objects or arrays.
		for (String key : jsonObject.keySet()) {
			JsonElement element = jsonObject.get(key);
			if (element.isJsonObject()) {
				// If the current element is a JSON object, recursively search this object for
				// the key.

				JsonElement result = readValueForKey(element.getAsJsonObject(), keyToRead);
				if (result != null) {
					// If the key is found in the nested object, return its value.

					return result;
				}
			} else if (element.isJsonArray()) {
				// If the current element is a JSON array, iterate through its elements to
				// search for the key.

				for (JsonElement arrayElement : element.getAsJsonArray()) {
					if (arrayElement.isJsonObject()) {
						// If an element of the array is a JSON object, recursively search this object
						// for the key.

						JsonElement result = readValueForKey(arrayElement.getAsJsonObject(), keyToRead);
						if (result != null) {
							// If the key is found in the nested object within the array, return its value.

							return result;
						}
					}
				}
			}
		}

		// Return null if the key is not found
		return null;
	}

	/**
	 * Reads a JSON file from the resources folder and converts it into a
	 * JsonObject. This method is useful for loading JSON data from a file into a
	 * JsonObject, which can then be manipulated or used in testing scenarios. The
	 * method utilizes Gson for JSON parsing.
	 *
	 * @param filename The name of the JSON file to be read. This file should be
	 *                 located in the resources folder of the project. The file name
	 *                 should include any necessary relative path information (e.g.,
	 *                 "data/testdata.json") from the resources root.
	 * @return JsonObject representing the content of the JSON file.
	 * @throws URISyntaxException If the URL of the given file is not formatted
	 *                            correctly.
	 * @throws IOException        If an I/O error occurs reading from the file or a
	 *                            malformed or unmappable byte sequence is read from
	 *                            the file.
	 */
	public static JsonObject getJsonObject(String filename) throws URISyntaxException, IOException {
		Gson gson = new Gson();

	    // Locate the JSON file within the resources folder using its filename.


		Path filePath = Paths.get(ClassLoader.getSystemResource(filename).toURI());
	    // Output the file path for debugging purposes; might be removed or commented out in production code.

		System.out.println(filePath);
	    // Read the entire content of the JSON file as a String.

		String payload = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(filename).toURI())));

		// convert the payload string to a JsonObject
		JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);
		return jsonObject;
	}
	
	public static String prepareJsonPayload(String jsonFileName, Map<String, Object> updates) throws Exception {
	    Gson gson = new Gson();

	    // Read the JSON file and convert it to a JsonObject
	    String payload = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(jsonFileName).toURI())));
	    JsonObject jsonObject = gson.fromJson(payload, JsonObject.class);

	    // Update the JsonObject with the updates map
	    if (updates!=null) {
	    updates.forEach((key, value) -> {
	        if (value instanceof String) {
	            jsonObject.addProperty(key, (String) value);
	        } else if (value instanceof Number) {
	            jsonObject.addProperty(key, (Number) value);
	        } else if (value instanceof Boolean) {
	            jsonObject.addProperty(key, (Boolean) value);
	        } else if (value instanceof Character) {
	            jsonObject.addProperty(key, (Character) value);
	        } else {
	            // For other types, convert to String or handle differently
	            // Here we're assuming conversion to String for simplicity
	            jsonObject.addProperty(key, value.toString());
	        }
	    });
	    }

	    // Convert the updated JsonObject back to a string
	    return gson.toJson(jsonObject);
	}
	
	public static String objtoSrting(JsonObject object ) {
		return new Gson().toJson(object);
		
		
	}
}
