package ResponseUtil;

import io.restassured.http.ContentType;

public class ResponseUtil {

    public static String extractValue(String response, ContentType contentType, String key) throws Exception {
        ResponseHandler handler;
        if (contentType == ContentType.JSON) {
            handler = new JsonResponseHandler();
        } else if (contentType == ContentType.XML) {
            handler = new XmlResponseHandler();
        } else {
            throw new IllegalArgumentException("Unsupported content type");
        }

        return handler.getValueByKey(response, key);
    }
}
