package Api.RestGpt;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;

public class Setup {
	User user1 = new User();
	static ContentType contentType;

	@BeforeMethod
	@Parameters({ "contentType", "apiName" })
	public void setUp(String contentType, String apiName) {
		RestAssured.config = RestAssured.config().sslConfig(SSLConfig.sslConfig().relaxedHTTPSValidation());

		if (contentType.equalsIgnoreCase("xml")) {
			Setup.contentType = ContentType.XML;
		} else if (contentType.equalsIgnoreCase("json")) {

			Setup.contentType = ContentType.JSON;

		}
		Config.initializeConfig(apiName);
	}
@BeforeMethod
@Parameters({"apiName" })
	public void setupApiConfig(String apiName) {
	    Config.initializeConfig(apiName);
	    // Any other setup code specific to the test environment
	}
}