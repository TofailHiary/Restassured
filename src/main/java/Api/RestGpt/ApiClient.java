package Api.RestGpt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.StringWriter;

import java.util.Arrays;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Utils.JsonHandler;
import Utils.SerializationUtil;
import Utils.XmlHandler;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ApiClient {
	private static int lastRandomNumber = -1;
	private static Response lastResponse = null;

	static File file;;
	private static String bearerToken = null;

	/**
	 * Sends a GET request to the specified endpoint.
	 * 
	 * @param endpoint    The API endpoint to send the request to.
	 * @param updates     A map of query parameters or updates to append to the
	 *                    request.
	 * @param contentType The content type of the request (JSON or XML).
	 * @return The response from the server.
	 * @throws FileNotFoundException If the content type is unsupported.
	 */

	public static Response sendGet(String endpoint, Map<String, String> updates, ContentType contentType)
			throws FileNotFoundException {
//		lastResponse = prepareRequest().get(endpoint).andReturn();
//		return lastResponse;
		if (contentType.equals(ContentType.JSON)) {

			// Set headers for JSON
			Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.get(endpoint);
		} else if (contentType.equals(ContentType.XML)) {

			// Set headers for XML
			Map<String, String> headers = Map.of("Content-Type", "application/xml", "Accept", "application/xml");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.get(endpoint);
		} else {
			throw new IllegalArgumentException("Unsupported file type");
		}
		return lastResponse;
	}

	/**
	 * Sends a POST request with a body to the specified endpoint.
	 * 
	 * @param endpoint    The API endpoint to send the request to.
	 * @param body        The request body, either as an object or a file name.
	 * @param contentType The content type of the request (JSON or XML).
	 * @return The response from the server.
	 * @throws Exception For serialization issues or unsupported file types.
	 */
	public static Response sendPost(String endpoint, Object body, ContentType contentType) throws Exception {
		String jsonRequestBody = SerializationUtil.serialize(body, contentType);
		if (contentType.equals(ContentType.JSON)) {

			// Set headers for JSON
			Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.body(jsonRequestBody).post(endpoint).andReturn();
		} else if (contentType.equals(ContentType.XML)) {

			// Set headers for XML
			Map<String, String> headers = Map.of("Content-Type", "application/xml", "Accept", "application/xml");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.body(jsonRequestBody).post(endpoint).andReturn();
		} else {
			throw new IllegalArgumentException("Unsupported file type");
		}
		return lastResponse;
	}

	/**
	 * Sends a POST request using a file's content as the body to the specified
	 * endpoint.
	 * 
	 * @param endpoint The API endpoint to send the request to.
	 * @param fileName The name of the file to use as the request body.
	 * @param updates  Modifications to apply to the file content before sending.
	 * @return The response from the server.
	 * @throws Exception For unsupported file types.
	 */
	@SuppressWarnings("unchecked")
	public static Response sendPost(String endpoint, String fileName, Map<String, ?> updates) throws Exception {
		String payload;
		if (fileName.endsWith(".json")) {
			payload = prepareJsonPayload(fileName, (Map<String, Object>) updates);
			// Set headers for JSON
			Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, ContentType.JSON, headers, null, logs())
					.body(payload).post(endpoint).andReturn();
		} else if (fileName.endsWith(".xml")) {
			payload = XmlHandler.prepareXmlPayload(fileName, (Map<String, String>) updates);
			// Set headers for XML
			Map<String, String> headers = Map.of("Content-Type", "application/xml", "Accept", "application/xml");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, ContentType.XML, headers, null, logs())
					.body(payload).post(endpoint).andReturn();
		} else {
			throw new IllegalArgumentException("Unsupported file type");
		}
		return lastResponse;
	}

	/**
	 * Sends a PUT request with a body to the specified endpoint.
	 * 
	 * @param endpoint    The API endpoint to send the request to.
	 * @param body        The request body, either as an object or a file name.
	 * @param contentType The content type of the request (JSON or XML).
	 * @return The response from the server.
	 * @throws Exception For serialization issues or unsupported file types.
	 */
	public static Response sendPut(String endpoint, Object body, ContentType contentType) throws Exception {
//		lastResponse = prepareRequest().body(body).put(endpoint).andReturn();
//		return lastResponse;

		String jsonRequestBody = SerializationUtil.serialize(body, contentType);
		if (contentType.equals(ContentType.JSON)) {

			// Set headers for JSON
			Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.body(jsonRequestBody).put(endpoint).andReturn();
		} else if (contentType.equals(ContentType.XML)) {

			// Set headers for XML
			Map<String, String> headers = Map.of("Content-Type", "application/xml", "Accept", "application/xml");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.body(jsonRequestBody).put(endpoint).andReturn();
		} else {
			throw new IllegalArgumentException("Unsupported file type");
		}
		return lastResponse;

	}

	/**
	 * Sends a DELETE request to the specified endpoint.
	 * 
	 * @param endpoint    The API endpoint to send the request to.
	 * @param contentType The content type of the request (JSON or XML).
	 * @return The response from the server.
	 * @throws FileNotFoundException If the content type is unsupported.
	 */
	@SuppressWarnings("unchecked")
	public static Response sendPut(String endpoint, String fileName, Map<String, ?> updates) throws Exception {
		String payload;

		if (fileName.endsWith(".json")) {
			payload = prepareJsonPayload(fileName, (Map<String, Object>) updates);
			// Set headers for JSON
			Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, ContentType.JSON, headers, null, logs())
					.body(payload).put(endpoint).andReturn();
		} else if (fileName.endsWith(".xml")) {
			payload = XmlHandler.prepareXmlPayload(fileName, (Map<String, String>) updates);
			// Set headers for XML
			Map<String, String> headers = Map.of("Content-Type", "application/xml", "Accept", "application/xml");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, ContentType.XML, headers, null, logs())
					.body(payload).put(endpoint).andReturn();
		} else {
			throw new IllegalArgumentException("Unsupported file type");
		}
		return lastResponse;
	}

	/**
	 * Sends a DELETE request to the specified endpoint.
	 * 
	 * @param endpoint    The API endpoint to send the request to.
	 * @param contentType The content type of the request (JSON or XML).
	 * @return The response from the server.
	 * @throws FileNotFoundException If the content type is unsupported.
	 */
	public static Response sendDelete(String endpoint, ContentType contentType) throws FileNotFoundException {
//		lastResponse = prepareRequest().delete(endpoint).andReturn();
//		return lastResponse;
		if (contentType.equals(ContentType.JSON)) {

			// Set headers for JSON
			Map<String, String> headers = Map.of("Content-Type", "application/json", "Accept", "application/json");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.delete(endpoint).andReturn();
		} else if (contentType.equals(ContentType.XML)) {

			// Set headers for XML
			Map<String, String> headers = Map.of("Content-Type", "application/xml", "Accept", "application/xml");
			// Send request
			lastResponse = RequestHelper
					.prepareRequest(Config.BASE_URI, Config.BASE_PATH, contentType, headers, null, logs())
					.delete(endpoint).andReturn();
		} else {
			throw new IllegalArgumentException("Unsupported file type");
		}
		return lastResponse;
	}

	/**
	 * Uploads a file to the specified endpoint with additional form fields.
	 * 
	 * @param endpoint   The API endpoint for file upload.
	 * @param filePath   The path to the file to upload.
	 * @param formFields Additional form fields to include in the upload request.
	 * @return The response from the server.
	 */
	public static Response sendFileUpload(String endpoint, String filePath, Map<String, Object> formFields) {
		// Prepare the request for file upload
		RequestSpecification request = prepareRequestForFileUpload();

		// Add any additional form fields if present
		if (formFields != null && !formFields.isEmpty()) {
			for (Map.Entry<String, Object> field : formFields.entrySet()) {
				request.multiPart(field.getKey(), field.getValue());
			}
		}

		// Add the file to the request
		File file = new File(filePath);
		request.multiPart("file", file); // Assuming "file" is the name of the form field for the file upload

		// Send the POST request to upload the file
		lastResponse = request.post(endpoint).andReturn();
		return lastResponse;
	}

	/**
	 * Prepares a request specification with common settings for API requests.
	 * 
	 * @return A RequestSpecification instance with configured base URI, path, and
	 *         content type.
	 */
	public static RequestSpecification prepareRequest() {
		RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri(Config.BASE_URI)
				.setBasePath(Config.BASE_PATH).setContentType(ContentType.JSON).addHeader("Accept", "application/json")
				.addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter()).build();

		return RestAssured.given().spec(requestSpecification);
	}

	private static String prepareJsonPayload(String jsonFileName, Map<String, Object> updates) throws Exception {
		// This method should implement logic similar to JsonHandler.prepareJsonPayload
		// Please refer to JsonHandler.prepareJsonPayload for the implementation details
		return JsonHandler.prepareJsonPayload(jsonFileName, updates);
	}

	/**
	 * Serializes an object to a JSON string.
	 * 
	 * @param obj The object to serialize.
	 * @return The JSON string representation of the object.
	 */
	public static String objectToJson(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}

	/**
	 * Serializes an object to an XML string.
	 * 
	 * @param obj The object to serialize.
	 * @return The XML string representation of the object.
	 * @throws Exception For JAXB serialization errors.
	 */
	public static String objectToXml(Object obj) throws Exception {
		JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		StringWriter writer = new StringWriter();
		marshaller.marshal(obj, writer);
		return writer.toString();
	}

	/**
	 * Prepares a request specification for file uploads.
	 * 
	 * @return A RequestSpecification instance configured for multipart/form-data
	 *         content type.
	 */
	private static RequestSpecification prepareRequestForFileUpload() {
		RestAssured.baseURI = Config.BASE_URI;
		RestAssured.basePath = Config.BASE_PATH;

		return RestAssured.given().contentType("multipart/form-data").log().all();
	}
	// This will give a number between 100000 (inclusive) and 1000000

	public static int randomNumber() {
		Random rnd = new Random();
		int number = rnd.nextInt(900000) + 100000;
		// (exclusive)
		System.out.println(number);
		lastRandomNumber = number;
		return number;
	}

	// Getter for the lastRandomNumber
	public static int getLastRandomNumber() {

		return lastRandomNumber;

	}

	// Getter for the LastResponse
	public static Response getLastResponse() {
		return lastResponse;
	}

	/**
	 * Configures logging for requests and responses.
	 * 
	 * @return A list of filters for logging requests and responses.
	 * @throws FileNotFoundException If the log file cannot be created.
	 */
	public static List<Filter> logs() throws FileNotFoundException {
		PrintStream logStream = new PrintStream(new File("api_logs.txt" + System.currentTimeMillis()));
		List<Filter> filters = Arrays.asList(new RequestLoggingFilter(logStream), new ResponseLoggingFilter(logStream));
		return filters;

	}

	public static void setBearerToken(String token) {
		bearerToken = token;
	}

	public static String getBearerToken() {
		return bearerToken;
	}
}
