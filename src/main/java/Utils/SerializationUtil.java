package Utils;

import io.restassured.http.ContentType;

import java.util.Map;

import com.google.gson.Gson;
// Other necessary imports
import com.google.gson.JsonObject;

public class SerializationUtil {

	@SuppressWarnings("unchecked")
	public static String serialize(Object data, ContentType contentType) throws Exception {
if (data != null ) {
	    if (contentType == ContentType.JSON) {
	        if (data instanceof Map) {
	            // Handle serialization of Map
	            return new Gson().toJson(data);
	        } else if (data instanceof JsonObject) {
	            // Handle serialization of JsonObject
	            return new Gson().toJson(data);
	        } else {
	            throw new IllegalArgumentException("Unsupported data type for JSON serialization: " + data.getClass().getSimpleName());
	        }
	    } else if (contentType == ContentType.XML) {
	        // Handle XML serialization
	        // Assuming XmlHandler.mapToXmlString exists and handles XML conversion
	        if (data instanceof Map) {
	            return XmlHandler.mapToXmlString((Map<String, Object>) data);
	        } else {
	            throw new IllegalArgumentException("Unsupported data type for XML serialization: " + data.getClass().getSimpleName());
	        }
	    } else {
	        throw new IllegalArgumentException("Unsupported content type: " + contentType);
	    }
	}
return "";
	}

}
