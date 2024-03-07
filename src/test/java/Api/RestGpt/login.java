package Api.RestGpt;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import ResponseUtil.ResponseUtil;
import Utils.XmlHandler;
import constants.ResponsesCode;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class login {
	User user1 = new User();
//	 @Test
//	 public void testGetResource() throws FileNotFoundException {
//		 ContentType contentType = ContentType.JSON;
//		    //Map<String, String> updates = new HashMap<>();
//		    //updates.put("Accept", "application/xml");
//		   // updates.put("id", ApiClient.randomNumber());
//		  //  (ApiEndpoints.RESOURCE_ENDPOINT, fileName, updates , Utils.Helper.getJson());
//	     Response response = ApiClient.sendGet(ApiEndpoints.RESOURCE_ENDPOINT + "/findByStatus?status=available", null , contentType);
//	     ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//	   ApiResponseValidator.validateListContainsValue(response, "id", "9222968140497180179");
//	   ApiResponseValidator.validateResponseTime(response, 10000);
//	   response.time();
//	   ApiResponseValidator.validateResponseTime(response, 10000);
//	 System.out.println(response.time());
//	 System.out.println(response.getStatusCode());
//
//	     // Add more assertions as needed
//	 }
	
	 @BeforeTest
	    public void setUp() {
	        RestAssured.config = RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation());
	  
	  	
	  	user1.setProperty("userName", "es64725");
	  	user1.setProperty("password", "P@ssw0rd");

	  	User user2 = new User();
	  	user2.setProperty("userName", "ES0101");
	  	user2.setProperty("password", "P@ssw0rd");

	    }
	
	@Test (priority = 0)
  public void testPostResource() throws Exception {
      // Read the XML file and parse it into a Document

		ContentType contentType = ContentType.JSON;
      // Update values in the XML document
      //Element rootElement = xmlDocument.getDocumentElement();
        String userName = (String) user1.getProperty("userName");
        String password = (String) user1.getProperty("password");
Map<String, String> updates = new HashMap<>();
updates.put("userName", userName);
updates.put("password", password);

//     // XmlHandler.updateXmlValues(filePath.toString(), updates);
//
//      // Convert the updated XML document back to a string
//    //  String updatedXml = XmlHandler.convertDocumentToString(xmlDocument);
//
//      // Send the POST request with the updated XML payload
//      Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT2, "login.json",updates);
//
//      // Perform assertions on the response
//      //assertEquals(200, response.getStatusCode());
//      ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//      ApiResponseValidator.validateKeyIsNotNull(response, "id");
//      ApiResponseValidator.validateKeyIsNotNull(response, "statusDesc");
//      ApiResponseValidator.validateKeyIsNotNull(response, "statusCode");
//      ApiResponseValidator.validateBodyContains(response, "UserName");
//      ApiResponseValidator.validateBodyContains(response, "token");
//      
//      String token = ResponseUtil.extractValue(response.asString(), contentType, "MsgFooter.token");
//      System.out.println("token: " + token);
//      ApiClient.setBearerToken(token);
 }
	
	 @Test(priority = 1)
	 public void testGetResource() throws FileNotFoundException {
		 ContentType contentType = ContentType.JSON;
		    Map<String, String> updates = new HashMap<>();
		    updates.put("Accept", "application/xml");
		   // updates.put("id", ApiClient.randomNumber());
		  //  (ApiEndpoints.RESOURCE_ENDPOINT, fileName, updates , Utils.Helper.getJson());
	     Response response = ApiClient.sendGet(Config.ENDPOINT, updates , contentType);
	     ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
	     ApiResponseValidator.validateKeyIsNotNull(response, "email");
	 //    ApiResponseValidator.validateResponseField(response, "msgBody.email" , "HAsha@estarta.com");
	//   ApiResponseValidator.validateListContainsValue(response, "pets.Pet.id", "9222968140497180179");
	   ApiResponseValidator.validateResponseTime(response, 10000);
	   response.time();
	   ApiResponseValidator.validateResponseTime(response, 10000);
	 System.out.println(response.time());
	 System.out.println(response.getStatusCode());

	     // Add more assertions as needed
	 }
	 @Test(priority = 2)
	 public void testLogout() throws Exception {
		 ContentType contentType = ContentType.JSON;
		    Map<String, String> updates = new HashMap<>();
		    updates.put("Accept", "application/json");
		   // updates.put("id", ApiClient.randomNumber());
		  //  (ApiEndpoints.RESOURCE_ENDPOINT, fileName, updates , Utils.Helper.getJson());
	     Response response = ApiClient.sendPost(Config.ENDPOINT, updates , contentType);
	     ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
	     ApiResponseValidator.validateBodyContains(response, "Logout Successfully for user "+(String) user1.getProperty("userName"));
	     
	     

	   ApiResponseValidator.validateResponseTime(response, 500);


	     // Add more assertions as needed
	 }
	 
	 
//		@Test (priority = 4)
//		  public void testPostResourcetwice() throws Exception {
//		      // Read the XML file and parse it into a Document
//
//				ContentType contentType = ContentType.JSON;
//		      // Update values in the XML document
//		      //Element rootElement = xmlDocument.getDocumentElement();
//		        String userName = (String) user1.getProperty("userName");
//		        String password = (String) user1.getProperty("password");
//		Map<String, String> updates = new HashMap<>();
//		updates.put("userName", userName);
//		updates.put("password", password);
//
//		     // XmlHandler.updateXmlValues(filePath.toString(), updates);
//
//		      // Convert the updated XML document back to a string
//		    //  String updatedXml = XmlHandler.convertDocumentToString(xmlDocument);
//
//		      // Send the POST request with the updated XML payload
//		      Response response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT2, "login.json",updates);
//
//		      // Perform assertions on the response
//		      //assertEquals(200, response.getStatusCode());
//		      ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
//		      ApiResponseValidator.validateKeyIsNotNull(response, "id");
//		      ApiResponseValidator.validateKeyIsNotNull(response, "statusDesc");
//		      ApiResponseValidator.validateKeyIsNotNull(response, "statusCode");
//		      ApiResponseValidator.validateBodyContains(response, "UserName");
//		      ApiResponseValidator.validateBodyContains(response, "token");
//		      response = ApiClient.sendPost(ApiEndpoints.RESOURCE_ENDPOINT2, "login.json",updates);
//		  //    ApiResponseValidator.validateBodyContains(response, "This user already logged in.");
//		      
//		      String token = ResponseUtil.extractValue(response.asString(), contentType, "MsgFooter.token");
//		      System.out.println("token: " + token);
//		      ApiClient.setBearerToken(token);
//		  }
}
