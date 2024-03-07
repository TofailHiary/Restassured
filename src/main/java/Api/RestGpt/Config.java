package Api.RestGpt;

import java.util.Map;

import config.DynamicConfigLoader;

//Config.java
public class Config {
 public static String BASE_URI = "";
 public static String BASE_PATH = "";
 public static  String ENDPOINT = "";

 
 
 
 public static void initializeConfig(String apiName) {
	 DynamicConfigLoader configLoader = new DynamicConfigLoader("api-config.json");
     Map<String, String> apiConfig = configLoader.getApiConfig(apiName);
     if (apiConfig != null) {
    	 BASE_URI = apiConfig.get("baseUri");
    	 BASE_PATH = apiConfig.get("basePath");
    	 ENDPOINT =  apiConfig.get("endpoint");
     }
 }
}
