package Api.RestGpt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

public class TestDataManager {
	/**
	 * Creates a request body for a POST request from a map of input data. This
	 * method copies all key-value pairs from the provided map into a new map, which
	 * can be used as the body of a POST request.
	 *
	 * @param inputData Map containing keys and values for the POST request body.
	 * @return A new map with all entries from inputData, ready for use as a POST
	 *         request body.
	 */
	public static Map<String, Object> preparePostRequestBody(Map<String, Object> inputData) {
		Map<String, Object> body = new HashMap<>();
		// Dynamically populate body with fields and values from inputData
		body.putAll(inputData);
		return body;
	}

	/**
	 * Merges base data with nested data into a single request body map. This method
	 * combines a map of base data and a map of nested data by adding all nested
	 * data into the base data map, allowing for the creation of a complex request
	 * body structure with nested objects.
	 *
	 * @param baseData   The base data for the request body as a map of key-value
	 *                   pairs.
	 * @param nestedData A map where each value is another map, representing nested
	 *                   data structures.
	 * @return The modified baseData map containing both the original base data and
	 *         the nested data.
	 */
	public static Map<String, Object> prepareNestedRequestBody(Map<String, Object> baseData,
			Map<String, Map<String, Object>> nestedData) {
		baseData.putAll(nestedData);
		return baseData;
	}

	/**
	 * Prepares a request body map with a key associated with an array of data. This
	 * method creates a map for a request body that includes a single key linked to
	 * a list of maps, facilitating the submission of array-like structures in a
	 * POST request.
	 *
	 * @param key       The key under which the array data will be stored in the
	 *                  request body.
	 * @param arrayData The list of maps representing the array data to be included
	 *                  in the request body.
	 * @return A map representing the request body, containing the provided array
	 *         data under the specified key.
	 */
	public static Map<String, Object> prepareArrayRequestBody(String key, List<Map<String, Object>> arrayData) {
		Map<String, Object> body = new HashMap<>();
		body.put(key, arrayData);
		return body;
	}

	/**
	 * Converts a POJO (Plain Old Java Object) to a JSON string. Utilizes Gson for
	 * serialization, enabling easy preparation of request bodies from object
	 * instances for API calls.
	 *
	 * @param pojo The object to be serialized to JSON.
	 * @return A JSON string representation of the provided POJO.
	 */

	public static String prepareRequestBodyFromPojo(Object pojo) {
		Gson gson = new Gson();
		return gson.toJson(pojo);
	}

	/**
	 * Generates a request body with randomized data. This method creates a map with
	 * random values for 'name' and 'email', useful for tests requiring unique data
	 * on each execution.
	 *
	 * @return A map with randomized 'name' and 'email' entries.
	 */
	public static Map<String, Object> prepareRandomizedRequestBody() {
		Map<String, Object> body = new HashMap<>();
		// Example: Generate random data
		body.put("name", "User" + new Random().nextInt(1000));
		body.put("email", "user" + new Random().nextInt(1000) + "@test.com");
		return body;
	}

}
