package ResponseUtil;

import io.restassured.path.json.JsonPath;

public class JsonResponseHandler implements ResponseHandler {
    @Override
    public String getValueByKey(String response, String key) {
        JsonPath jsonPath = JsonPath.from(response);
        return jsonPath.getString(key);
    }
}
