package Api.RestGpt;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ResponseUtil.ResponseUtil;
import constants.ResponsesCode;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class Login2  extends Setup{
	User user1 = UserUtils.getUser("user1");
	String userName = (String) user1.getProperty("userName");
	String password = (String) user1.getProperty("password");
	// Continue with your test logic...


	@Test(priority = 0)

	public void testPostResource() throws Exception {

		Map<String, String> updates = new HashMap<>();
		updates.put("userName", userName);
		updates.put("password", password);

		Response response = ApiClient.sendPost(Config.ENDPOINT, "login.json", updates);

		ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
		ApiResponseValidator.validateKeyIsNotNull(response, "MsgHeader.statusDesc");
		ApiResponseValidator.validateKeyIsNotNull(response, "MsgHeader.statusCode");
		ApiResponseValidator.validateBodyContains(response, "UserName");
		ApiResponseValidator.validateBodyContains(response, "token");
		ApiResponseValidator.validateKeyIsNotNull(response, "MsgFooter.token");
		String token = ResponseUtil.extractValue(response.asString(), contentType, "MsgFooter.token");
		System.out.println("token: " + token);
		ApiClient.setBearerToken(token);
	}

	@Test(priority = 1)
	 @Parameters({"apiName"})
	public void testGetResource(String apiName) throws FileNotFoundException {
		//setupApiConfig(apiName);

		Response response = ApiClient.sendGet(Config.ENDPOINT, null, contentType);
		ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
		ApiResponseValidator.validateKeyIsNotNull(response, "msgBody.email");
		ApiResponseValidator.validateResponseField(response, "msgBody.email", "HAsha@estarta.com");

		ApiResponseValidator.validateResponseTime(response, 10000);
		response.time();
		ApiResponseValidator.validateResponseTime(response, 10000);
		System.out.println(response.time());
		System.out.println(response.getStatusCode());

	}

	@Test(priority = 2)
	@Parameters({"apiName"})
	
	public void testLogout(String apiName) throws Exception {

	//	setupApiConfig(apiName);
		Response response = ApiClient.sendPost(Config.ENDPOINT, null, contentType);
		ApiResponseValidator.validateStatusCode(response, ResponsesCode.OK);
		ApiResponseValidator.validateBodyContains(response,
				"Logout Successfully for user " + (String) user1.getProperty("userName"));

		ApiResponseValidator.validateResponseTime(response, 500);
		response = ApiClient.sendPost(Config.ENDPOINT, null, contentType);
		ApiResponseValidator.validateBodyContains (response,"This user already logged out."); 


		// Add more assertions as needed
	}
}
