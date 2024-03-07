package Api.RestGpt;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
//ApiTests.java
import io.restassured.response.Response;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Element;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import ResponseUtil.ResponseUtil;
import Utils.ExtentReportListener;
import Utils.JsonHandler;
import Utils.SerializationUtil;
import Utils.XmlHandler;
import constants.ResponsesCode;
import groovy.xml.XmlUtil;

public class ApiTests2 extends Setup {
	 @Parameters("findByStatus")
 @Test (description = "Test to verify status available")

 public void testGetResource(String findByStatus) throws FileNotFoundException {
//	 ContentType contentType = ContentType.JSON;
//	    Map<String, String> updates = new HashMap<>();
//	    updates.put("Accept", "application/xml");
	   // updates.put("id", ApiClient.randomNumber());
	  //  (ApiEndpoints.RESOURCE_ENDPOINT, fileName, updates , Utils.Helper.getJson());
     Response response = ApiClient.sendGet(Config.ENDPOINT + findByStatus, null , contentType);
     ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
 ApiResponseValidator.validateListContainsValue(response, "id", "9222968140497180179");
   ApiResponseValidator.validateResponseTime(response, 10000);
   response.time();
   ApiResponseValidator.validateResponseTime(response, 10000);
 System.out.println(response.time());
 System.out.println(response.getStatusCode());

     // Add more assertions as needed
 }

	@Test // json 
	public void testPostResource() throws Exception {
		long petId = 9223372036854288555L; // Example ID, ensure it's unique or suitable for your test
		int categoryId = 2; // Example category ID
		int tagId = 0; // Example tag ID
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("id", petId);

        JsonObject category = new JsonObject();
        category.addProperty("id", categoryId);
        category.addProperty("name", "newone");
        requestBody.add("category", category);

        requestBody.addProperty("name", "Testdoggie");

        JsonArray photoUrls = new JsonArray();
        photoUrls.add("string");
        requestBody.add("photoUrls", photoUrls);

        JsonObject tag = new JsonObject();
        tag.addProperty("id", tagId);
        tag.addProperty("name", "string");

        JsonArray tags = new JsonArray();
        tags.add(tag);
        requestBody.add("tags", tags);

        requestBody.addProperty("status", "sold");
	//	String jsonFileName = "createPet.json";
   //  String prettyJsonString = new GsonBuilder().setPrettyPrinting().create().toJson(requestBody);
        System.out.println(requestBody);
	
		
		Response response = ApiClient.sendPost(Config.ENDPOINT , requestBody ,contentType);

		System.out.println(response.getStatusCode());

		ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);

		ApiResponseValidator.validateKeyIsNotNull(response, "id");

		ApiResponseValidator.validateKeyIsNotNull(response, "name");

		ApiResponseValidator.validateKeyIsNotNull(response, "status");
		ApiResponseValidator.validateBodyContains(response, "Testdoggie");
		// assertTrue(expectedId == 222);
		// assertEquals(expectedId, expectedId)

		// Add more assertions as needed
	}

	
//	@Test 
//    public void testPostResource() throws Exception {
//        // Read the XML file and parse it into a Document
//        Document xmlDocument = XmlHandler.getXmlDocument("Create.xml");
//
//        // Update values in the XML document
//        //Element rootElement = xmlDocument.getDocumentElement();
//Map<String, String> updates = new HashMap<>();
//updates.put("name", "newDoggie");
//
//       // XmlHandler.updateXmlValues(filePath.toString(), updates);
//
//        // Convert the updated XML document back to a string
//      //  String updatedXml = XmlHandler.convertDocumentToString(xmlDocument);
//
//        // Send the POST request with the updated XML payload
//        Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT, "Create.json",updates);
//
//        // Perform assertions on the response
//        //assertEquals(200, response.getStatusCode());
//        ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//        ApiResponseValidator.validateKeyIsNotNull(response, "id");
//        ApiResponseValidator.validateKeyIsNotNull(response, "name");
//        ApiResponseValidator.validateKeyIsNotNull(response, "status");
//        ApiResponseValidator.validateBodyContains(response, "newDoggie");
//    }


//	@Test
//	public void testPostResourceUsingJsonFile() {
//		String jsonFileName = "CreateAirline.json";
//		try {
//			Response response = ApiClient.sendPost("https://api.instantwebtools.net/v1/airlines/", jsonFileName);
//			JsonPath jsonPath = new JsonPath(response.asString());
//			String message = jsonPath.get("message");
//			
//			if (message == null) {
//			 ApiResponseValidator.validateStatusCode(response, ResponsesCodes.OK);
//			 }
//			else {
//				
//				ApiResponseValidator.validateStatusCode(response, ResponsesCodes.OK);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	

// @Test
// public void testPostResourceDynamic() throws Exception {
//     // Dynamically prepare request body data
//     Map<String, Object> dynamicData = new HashMap<>();
//     dynamicData.put("name", "ahmet");
//     dynamicData.put("gender", "male");
//     dynamicData.put("email", "test@test.com");
//     dynamicData.put("status", "Active");
//     // Add more fields as needed dynamically
//
//     Map<String, Object> requestBody = TestDataManager.preparePostRequestBody(dynamicData);
//     Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT, requestBody,ContentType.JSON);
//     ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//     // Add more assertions as needed
// }
 
 
//    @Test (priority = 0)
//    public void testPostResourceDynamic() throws Exception {
//        // Prepare dynamic data
//        Map<String, Object> dynamicData = new HashMap<>();
//        dynamicData.put("name", "doggie");
//        dynamicData.put("status", "available");
//
//        Map<String, Object> category = new HashMap<>();
//        category.put("id", 0);
//        category.put("name", "string");
//        dynamicData.put("Category", category);
//
//        List<Map<String, Object>> tags = new ArrayList<>();
//        Map<String, Object> tag = new HashMap<>();
//        tag.put("id", 0);
//        tag.put("name", "string");
//        tags.add(tag);
//        dynamicData.put("tags", tags);
//
//        List<String> photoUrls = new ArrayList<>();
//        photoUrls.add("string");
//        dynamicData.put("photoUrls", photoUrls);
//
//        // Choose ContentType based on what you need: ContentType.XML or ContentType.JSON
//        ContentType contentType = ContentType.XML; // or ContentType.XML for XML tests
//
//        String requestBody = SerializationUtil.serialize(dynamicData, contentType);
//        
//        // Send request with the dynamically generated requestBody
//        Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT, requestBody, contentType);
//        ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//        String id = ResponseUtil.extractValue(response.asString(), contentType, "id");
//        
//        System.out.println("Extracted JSON ID: " + id);
//        // Add more assertions as needed
//    }


//	@Test
//	public void testPutResource() throws Exception {
//	    String fileName = "UpdatePet.json";   
//	   
//	    Map<String, Object> updates = new HashMap<>();
//	    updates.put("status", "available");
//	    updates.put("id", ApiClient.randomNumber());
//
//	    Response response = ApiClient.sendPut(ApiEndpoints.RESOURCE_ENDPOINT, fileName, updates);
//	    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	    ApiResponseValidator.validateResponseField(response, "id", String.valueOf(ApiClient.getLastRandomNumber()));
//	    ApiResponseValidator.validateBodyContains(response, "doggie");
//	    
//	    // Additional assertions as needed
//	}

	

 @Test  (priority = 1)
 public void testDeleteResourceold() throws Exception {
	 ContentType contentType = ContentType.XML;
	  String id = ResponseUtil.extractValue(ApiClient.getLastResponse().asPrettyString(), contentType, "id");
      System.out.println("Extracted JSON ID: " + id);
     Response response = ApiClient.sendDelete(Config.ENDPOINT+"/"+id,contentType);
     ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
     response = ApiClient.sendDelete(Config.ENDPOINT+"/"+id,contentType);
    ApiResponseValidator.validateStatusCode(response, ResponsesCode.NOT_FOUND);
     // Add more assertions as needed
 }

 
//@Test  (priority = 2)
//public void testDeleteResourceagain() throws Exception {
//	 ContentType contentType = ContentType.XML;
//	  String id = ResponseUtil.extractValue(ApiClient.getLastResponse().asString(),contentType, "id");
//     System.out.println("Extracted JSON ID: " + id);
//    Response response = ApiClient.sendDelete(ApiEndpoints.RESOURCE_ENDPOINT+"/"+id,contentType);
//   // ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//    //response = ApiClient.sendDelete(ApiEndpoints.RESOURCE_ENDPOINT+"/"+id);
//    ApiResponseValidator.validateStatusCode(response, ResponsesCode.NOT_FOUND);
//    // Add more assertions as needed
//}
//}
 
// @Test
// public void testNestedJsonRequest() {
//     Map<String, Object> baseData = new HashMap<>();
//     baseData.put("name", "John Doe");
//     baseData.put("email", "john@example.com");
//
//     Map<String, Map<String, Object>> nestedData = new HashMap<>();
//     Map<String, Object> address = new HashMap<>();
//     address.put("street", "123 Main St");
//     address.put("city", "Anytown");
//     nestedData.put("address", address);
//
//     Map<String, Object> requestBody = TestDataManager.prepareNestedRequestBody(baseData, nestedData);
//
//     Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT, requestBody);
//     ApiResponseValidator.validateStatusCode(response, ResponsesCodes.OK);
// }
 
 
// @Test
// public void testArrayRequestBody() {
//     List<Map<String, Object>> arrayData = new ArrayList<>();
//     Map<String, Object> item1 = new HashMap<>();
//     item1.put("id", 1);
//     item1.put("value", "A");
//     arrayData.add(item1);
//
//     Map<String, Object> requestBody = TestDataManager.prepareArrayRequestBody("items", arrayData);
//
//     Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT, requestBody);
//     ApiResponseValidator.validateStatusCode(response, ResponsesCodes.OK);
// }
 


// @Test
// public void testPojoRequestBody() throws Exception {
//     User user = new User("John Doe", "john@example.com", "Active");
//
//     String jsonRequestBody = TestDataManager.prepareRequestBodyFromPojo(user);
//
//     Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT, jsonRequestBody);
//     ApiResponseValidator.validateStatusCode(response, ResponsesCodes.OK);
// }

// @Test
// public void testRandomizedDataRequest() {
//     Map<String, Object> requestBody = TestDataManager.prepareRandomizedRequestBody();
//
//     Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT, requestBody);
//     ApiResponseValidator.validateStatusCode(response, ResponsesCodes.OK);
// }
@Test (priority = 0)
public void testPostResourceDynamic() throws Exception {
    // Prepare dynamic data
    Map<String, Object> dynamicData = new HashMap<>();
    dynamicData.put("name", "doggie");
    dynamicData.put("status", "available");

    Map<String, Object> category = new HashMap<>();
    category.put("id", 0);
    category.put("name", "string");
    dynamicData.put("Category", category);

    List<Map<String, Object>> tags = new ArrayList<>();
    Map<String, Object> tag = new HashMap<>();
    tag.put("id", 0);
    tag.put("name", "string");
    tags.add(tag);
    dynamicData.put("tags", tags);

    List<String> photoUrls = new ArrayList<>();
    photoUrls.add("string");
    dynamicData.put("photoUrls", photoUrls);

    // Choose ContentType based on what you need: ContentType.XML or ContentType.JSON
    ContentType contentType = ContentType.XML; // or ContentType.XML for XML tests

    String requestBody = SerializationUtil.serialize(dynamicData, contentType);
    
    // Send request with the dynamically generated requestBody
    Response response = ApiClient.sendPost(Config.ENDPOINT, requestBody, contentType);
    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
    String id = ResponseUtil.extractValue(response.asString(), contentType, "id");
    
    System.out.println("Extracted JSON ID: " + id);
    // Add more assertions as needed
}
@Test  (priority = 1)
public void testDeleteResource() throws Exception {
	 ContentType contentType = ContentType.XML;
	  String id = ResponseUtil.extractValue(ApiClient.getLastResponse().asPrettyString(), contentType, "id");
     System.out.println("Extracted JSON ID: " + id);
    Response response = ApiClient.sendDelete(Config.ENDPOINT+"/"+id,contentType);
    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
    response = ApiClient.sendDelete(Config.ENDPOINT+"/"+id,contentType);
   ApiResponseValidator.validateStatusCode(response, ResponsesCode.NOT_FOUND);
    // Add more assertions as needed
}
}

