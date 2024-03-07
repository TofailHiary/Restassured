package Api.RestGpt;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ResponseUtil.ResponseUtil;
import constants.ResponsesCode;

import io.restassured.response.Response;

public class Petclean extends Setup {
//	@Parameters("findByStatus")
//	@Test(description = "Test to verify status available")
//
//	public void testGetResource(String findByStatus) throws FileNotFoundException {
//
//		Response response = ApiClient.sendGet(Config.ENDPOINT + findByStatus, null, contentType);
//		ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//		ApiResponseValidator.validateListContainsValue(response, "id", "9222968140497180179");
//		ApiResponseValidator.validateResponseTime(response, 10000);
//		response.time();
//		ApiResponseValidator.validateResponseTime(response, 10000);
//		System.out.println(response.time());
//		System.out.println(response.getStatusCode());
//
//	}
//	@Test // json 
//	public void testPostResourceWithObj() throws Exception {
//		long petId = 9223372036854288555L; // Example ID, ensure it's unique or suitable for your test
//		int categoryId = 2; // Example category ID
//		int tagId = 0; // Example tag ID
//        JsonObject requestBody = new JsonObject();
//        requestBody.addProperty("id", petId);
//
//        JsonObject category = new JsonObject();
//        category.addProperty("id", categoryId);
//        category.addProperty("name", "newone");
//        requestBody.add("category", category);
//
//        requestBody.addProperty("name", "Testdoggie");
//
//        JsonArray photoUrls = new JsonArray();
//        photoUrls.add("string");
//        requestBody.add("photoUrls", photoUrls);
//
//        JsonObject tag = new JsonObject();
//        tag.addProperty("id", tagId);
//        tag.addProperty("name", "string");
//
//        JsonArray tags = new JsonArray();
//        tags.add(tag);
//        requestBody.add("tags", tags);
//
//        requestBody.addProperty("status", "sold");
//        
//       // String jsonRequestBody = SerializationUtil.serialize(requestBody, contentType);
//		Response response = ApiClient.sendPost(Config.ENDPOINT , requestBody ,contentType);
//
//		System.out.println(response.getStatusCode());
//
//		ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//
//		ApiResponseValidator.validateKeyIsNotNull(response, "id");
//
//		ApiResponseValidator.validateKeyIsNotNull(response, "name");
//
//		ApiResponseValidator.validateKeyIsNotNull(response, "status");
//		ApiResponseValidator.validateBodyContains(response, "Testdoggie");
//
//	    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	    String id = ResponseUtil.extractValue(response.asString(), contentType, "id");
//	    
//	    System.out.println("Extracted JSON ID: " + id);
//
//	}

//	@Test (priority = 0)
//	public void testPostResourceDynamicWithMap() throws Exception {
//	    // Prepare dynamic data
//	    Map<String, Object> dynamicData = new HashMap<>();
//	    dynamicData.put("name", "doggie");
//	    dynamicData.put("status", "available");
//
//	    Map<String, Object> category = new HashMap<>();
//	    category.put("id", 0);
//	    category.put("name", "string");
//	    dynamicData.put("Category", category);
//
//	    List<Map<String, Object>> tags = new ArrayList<>();
//	    Map<String, Object> tag = new HashMap<>();
//	    tag.put("id", 0);
//	    tag.put("name", "string");
//	    tags.add(tag);
//	    dynamicData.put("tags", tags);
//
//	    List<String> photoUrls = new ArrayList<>();
//	    photoUrls.add("string");
//	    dynamicData.put("photoUrls", photoUrls);
//
//
//	   // String requestBody = SerializationUtil.serialize(dynamicData, contentType);
//	    
//	    // Send request with the dynamically generated requestBody
//	    Response response = ApiClient.sendPost(Config.ENDPOINT, dynamicData, contentType);
//	    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	    String id = ResponseUtil.extractValue(response.asString(), contentType, "id");
//	    
//	    System.out.println("Extracted JSON ID: " + id);
//	    // Add more assertions as needed
//	}

//	 @Test  (priority = 1)
//	 public void testDeleteResource() throws Exception {
//		 //ContentType contentType = ContentType.XML;
//		  String id = ResponseUtil.extractValue(ApiClient.getLastResponse().asPrettyString(), contentType, "id");
//	      System.out.println("Extracted JSON ID: " + id);
//	     Response response = ApiClient.sendDelete(Config.ENDPOINT+"/"+id,contentType);
//	     ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	     response = ApiClient.sendDelete(Config.ENDPOINT+"/"+id,contentType);
//	    ApiResponseValidator.validateStatusCode(response, ResponsesCode.NOT_FOUND);
//	     // Add more assertions as needed
//	 }

//@Test 
//public void testPostResourceWithFile() throws Exception {
//
//
//  // Update values in the XML document
//  //Element rootElement = xmlDocument.getDocumentElement();
//Map<String, String> updates = new HashMap<>();
//updates.put("name", "newDoggie");
//
// // XmlHandler.updateXmlValues(filePath.toString(), updates);
//
//  // Convert the updated XML document back to a string
////  String updatedXml = XmlHandler.convertDocumentToString(xmlDocument);
//
//  // Send the POST request with the updated XML payload
//  Response response = ApiClient.sendPost(Config.ENDPOINT, "Create.xml",updates);
//
//  // Perform assertions on the response
//  //assertEquals(200, response.getStatusCode());
//  ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//  ApiResponseValidator.validateKeyIsNotNull(response, "id");
//  ApiResponseValidator.validateKeyIsNotNull(response, "name");
//  ApiResponseValidator.validateKeyIsNotNull(response, "status");
//  ApiResponseValidator.validateBodyContains(response, "newDoggie");
//}
	
//	@Test
//	public void testPutResourceWithFile() throws Exception {
//	    String fileName = "UpdatePet.json";   
//	   
//	    Map<String, Object> updates = new HashMap<>();
//	    updates.put("status", "sold");
//	    updates.put("id", ApiClient.randomNumber());
//
//	    Response response = ApiClient.sendPut(Config.ENDPOINT, fileName, updates);
//	    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	    ApiResponseValidator.validateResponseField(response, "id", String.valueOf(ApiClient.getLastRandomNumber()));
//	    ApiResponseValidator.validateBodyContains(response, "doggie");
//	    
//	    // Additional assertions as needed
//	}
//	@Test
//	public void testPutResourceWithoutFileMap() throws Exception {
//	    
//	    Map<String, Object> dynamicData = new HashMap<>();
//	    dynamicData.put("name", "putdogs");
//	    dynamicData.put("status", "pending");
//	    dynamicData.put("id", "9223372036854288555");
//
//	    Map<String, Object> category = new HashMap<>();
//	    category.put("id", 0);
//	    category.put("name", "string");
//	    dynamicData.put("Category", category);
//
//	    List<Map<String, Object>> tags = new ArrayList<>();
//	    Map<String, Object> tag = new HashMap<>();
//	    tag.put("id", 0);
//	    tag.put("name", "string");
//	    tags.add(tag);
//	    dynamicData.put("tags", tags);
//
//	    List<String> photoUrls = new ArrayList<>();
//	    photoUrls.add("string");
//	    dynamicData.put("photoUrls", photoUrls);
//
//
//	   // String requestBody = SerializationUtil.serialize(dynamicData, contentType);
//
//	    Response response = ApiClient.sendPut(Config.ENDPOINT, dynamicData, contentType);
//	    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	   ApiResponseValidator.validateResponseField(response, "/Pet/id","9223372036854288555");
//	   ApiResponseValidator.validateBodyContains(response, "9223372036854288555");
//	    ApiResponseValidator.validateBodyContains(response, "putdogs");
//	    
//	    // Additional assertions as needed
//	}
	
//	@Test // json 
//	public void testPostResourceWithObj() throws Exception {
//		long petId = 9223372036854288555L; // Example ID, ensure it's unique or suitable for your test
//		int categoryId = 2; // Example category ID
//		int tagId = 0; // Example tag ID
//        JsonObject requestBody = new JsonObject();
//        requestBody.addProperty("id", petId);
//
//        JsonObject category = new JsonObject();
//        category.addProperty("id", categoryId);
//        category.addProperty("name", "newone");
//        requestBody.add("category", category);
//
//        requestBody.addProperty("name", "Hi");
//
//        JsonArray photoUrls = new JsonArray();
//        photoUrls.add("string");
//        requestBody.add("photoUrls", photoUrls);
//
//        JsonObject tag = new JsonObject();
//        tag.addProperty("id", tagId);
//        tag.addProperty("name", "string");
//
//        JsonArray tags = new JsonArray();
//        tags.add(tag);
//        requestBody.add("tags", tags);
//
//        requestBody.addProperty("status", "sold");
//        
//       // String jsonRequestBody = SerializationUtil.serialize(requestBody, contentType);
//		Response response = ApiClient.sendPut(Config.ENDPOINT , requestBody ,contentType);
//
//		System.out.println(response.getStatusCode());
//
//		ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//
//		ApiResponseValidator.validateKeyIsNotNull(response, "id");
//
//		ApiResponseValidator.validateKeyIsNotNull(response, "name");
//
//		ApiResponseValidator.validateKeyIsNotNull(response, "status");
//		ApiResponseValidator.validateBodyContains(response, "Hi");
//
//	    ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	//    String id = ResponseUtil.extractValue(response.asString(), contentType, "id");
//	    
//	//    System.out.println("Extracted JSON ID: " + id);
//
//	}
}