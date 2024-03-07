package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;
import java.io.InputStream;

public class DynamicConfigLoader {
	private static Map<String, Map<String, Map<String, String>>> apiConfigs; // Correctly mapping the JSON structure

	public DynamicConfigLoader(String configFilePath) {
		try {
			InputStream inputStream = DynamicConfigLoader.class.getResourceAsStream(configFilePath);
			if (inputStream == null) {
				throw new IllegalArgumentException("Resource not found: " + configFilePath);
			}
			ObjectMapper mapper = new ObjectMapper();
			// Correctly reading from InputStream
			apiConfigs = mapper.readValue(inputStream,
					new TypeReference<Map<String, Map<String, Map<String, String>>>>() {
					});

		} catch (Exception e) {
			e.printStackTrace();
			// Handle exception: file not found, parsing error, etc.
		}

	}

	public  Map<String, String> getApiConfig(String apiName) {
		// Assuming the JSON structure and making sure to access the nested Map
		// correctly
		Map<String, Map<String, String>> apis = apiConfigs.get("apis");
		if (apis != null) {
			return apis.get(apiName);
		} else {
			return null; // Or handle this case as you see fit
		}
	}

}
