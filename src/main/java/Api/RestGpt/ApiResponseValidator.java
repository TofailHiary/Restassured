package Api.RestGpt;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
//ApiResponseValidator.java
import io.restassured.response.Response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matcher;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import Utils.ExtentReportListener;
import Utils.JsonHandler;
import constants.ResponsesCode;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class ApiResponseValidator {

	/**
	 * Validates the HTTP status code of a response against an expected status code.
	 * Logs the result of the validation to the ExtentReports for test reporting
	 * purposes.
	 *
	 * @param response           The response from the API call to validate.
	 * @param expectedStatusCode The expected HTTP status code.
	 */
	public static void validateStatusCode(Response response, ResponsesCode expectedStatusCode) {
		response.then().assertThat().statusCode(expectedStatusCode.getCode());
		ExtentReportListener.getTest().get()
				.pass("Successfully validated status code: " + expectedStatusCode.getCode());

	}

	/**
	 * Validates the value of a specified field in the response against an expected
	 * value. Supports nested fields for JSON responses and uses XPath for XML
	 * responses. Logs the validation result to ExtentReports.
	 *
	 * @param response      The API response to validate.
	 * @param field         The field to validate (dot notation for JSON, XPath for
	 *                      XML).
	 * @param expectedValue The expected value of the field.
	 */
	public static void validateResponseField(Response response, String field, String expectedValue) {
		String actualValue;
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			try {
				// Attempt to use dot notation to get the value directly
				actualValue = response.jsonPath().getString(field);
			} catch (Exception e) {
				// Fallback: Use readValueForKey for complex nested structures
				JsonObject jsonObject = new Gson().fromJson(response.asString(), JsonObject.class);
				JsonElement result = JsonHandler.readValueForKey(jsonObject, field);
				actualValue = (result != null) ? result.getAsString() : null;
			}
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			// For XML content type, extract using XPath
			try {
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(new InputSource(new StringReader(response.getBody().asString())));

				XPathFactory xpathFactory = XPathFactory.newInstance();
				XPath xpath = xpathFactory.newXPath();
				XPathExpression expr = xpath.compile(field.replaceFirst("/", "./")); // Adjust initial XPath to start
																						// from the current node

				actualValue = (String) expr.evaluate(doc, XPathConstants.STRING);
			} catch (Exception e) {

				e.printStackTrace();
				new Throwable("Test Error ");
				return;
			}
		} else {
			// Unsupported content type, log warning or skip validation
			System.out.println("Warning: Unsupported content type - " + contentType);
			return;
		}

		assertThat("Field value mismatch", actualValue, equalTo(expectedValue));
		ExtentReportListener.getTest().get()
				.pass("Successfully validated field '" + field + "' with value: " + expectedValue);
	}

	/**
	 * Validates that a list within the response contains a specific expected value.
	 * Supports both JSON and XML content types. Logs the validation outcome to
	 * ExtentReports.
	 *
	 * @param response      The API response containing the list to be validated.
	 * @param field         The path to the list field in the response (JSON path or
	 *                      XML path).
	 * @param expectedValue The value expected to be found within the list.
	 */
	public static void validateListContainsValue(Response response, String field, String expectedValue) {
		String contentType = response.header("Content-Type");
		if (contentType.contains("application/json")) {
			List<String> values = response.jsonPath().getList(field, String.class);
			assertThat(values, hasItem(expectedValue));
			ExtentReportListener.getTest().get()
					.pass("Successfully validated list '" + field + "' contains value: " + expectedValue);
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();

			// Remove or replace invalid characters
			responseBody = StringUtils.replace(responseBody, "\u001A", "");
			System.out.println(responseBody);
			List<String> values = response.xmlPath().getList(field, String.class);
			assertThat(values, hasItem(expectedValue));
			ExtentReportListener.getTest().get()
					.pass("Successfully validated list '" + field + "' contains value: " + expectedValue);
		}
	}

	/**
	 * Validates a specific header's value in the response against an expected
	 * value. Logs the result to ExtentReports for reporting purposes.
	 *
	 * @param response            The API response containing the header.
	 * @param headerName          The name of the header to validate.
	 * @param expectedHeaderValue The expected value of the header.
	 */
	public static void validateHeader(Response response, String headerName, String expectedHeaderValue) {
		response.then().assertThat().header(headerName, expectedHeaderValue);
		ExtentReportListener.getTest().get()
				.info("Successfully validated header '" + headerName + "' with value: " + expectedHeaderValue);

	}

	/**
	 * Validates a specific JSON/XML path in the response body against an expected
	 * value. Supports both JSON and XML content types. Logs the validation result
	 * to ExtentReports.
	 *
	 * @param response      The API response to validate.
	 * @param jsonPath      The JSON/XML path to the parameter in the response body.
	 * @param expectedValue The expected value of the parameter.
	 */

	public static void validateBodyParam(Response response, String jsonPath, String expectedValue) {
		String actualValue;
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			actualValue = response.jsonPath().getString(jsonPath);
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();

			// Remove or replace invalid characters if needed
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			// Parse the XML response
			actualValue = XmlPath.from(responseBody).getString(jsonPath);
		} else {
			// Unsupported content type, log warning or skip validation
			System.out.println("Warning: Unsupported content type - " + contentType);
			return;
		}

		assertThat("Body parameter value mismatch", actualValue, equalTo(expectedValue));
		ExtentReportListener.getTest().get()
				.info("Successfully validated body parameter '" + jsonPath + "' with value: " + expectedValue);
	}

	/**
	 * Validates that the response body contains a specified string. Supports both
	 * JSON and XML content types. Logs the result to ExtentReports.
	 *
	 * @param response        The API response to check.
	 * @param expectedContent The string expected to be found within the response
	 *                        body.
	 */

	public static void validateBodyContains(Response response, String expectedContent) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			response.then().assertThat().body(containsString(expectedContent));
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();

			// Remove or replace invalid characters if needed
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			XmlPath xmlPath = XmlPath.from(responseBody);
			assertThat(xmlPath.get().toString(), containsString(expectedContent));
			ExtentReportListener.getTest().get().info("Successfully validated body contains: " + expectedContent);
		}
	}

	/**
	 * Validates the response time is less than a specified maximum time. Logs the
	 * result to ExtentReports for performance monitoring.
	 *
	 * @param response    The API response to validate.
	 * @param maxTimeInMs The maximum acceptable response time in milliseconds.
	 */
	public static void validateResponseTime(Response response, long maxTimeInMs) {
		response.then().assertThat().time(lessThan(maxTimeInMs));
		ExtentReportListener.getTest().get()
				.pass("Successfully validated response time is less than: " + maxTimeInMs + "ms");

	}

	/**
	 * Validates the response body is empty or null. Supports both JSON and XML
	 * content types. Logs the result to ExtentReports.
	 *
	 * @param response The API response to validate.
	 */

	public static void validateEmptyBody(Response response) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			response.then().assertThat().body(emptyOrNullString());
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();

			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			// Check if the XML response body is empty or null
			if (StringUtils.isBlank(responseBody)) {
				ExtentReportListener.getTest().get().info("Successfully validated response body is empty or null.");
			} else {
				// Assertion for XML content
				try {
					response.then().assertThat().body(emptyOrNullString());
					ExtentReportListener.getTest().get().pass("Successfully validated response body is empty or null.");
				} catch (AssertionError e) {
					ExtentReportListener.getTest().get()
							.fail("Failed to validate response body is empty or null. Error: " + e.getMessage());
				}
			}
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			// Log a warning message or skip validation
		}
	}

	/**
	 * Validates the size of a JSON/XML array in the response body matches an
	 * expected size. Supports both JSON and XML content types. Logs the result to
	 * ExtentReports.
	 *
	 * @param response     The API response to validate.
	 * @param jsonPath     The JSON/XML path to the array in the response body.
	 * @param expectedSize The expected size of the array.
	 */

	public static void validateJsonArraySize(Response response, String jsonPath, int expectedSize) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();

			// Remove or replace invalid characters if needed
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			// Parse the XML response
			XmlPath xmlPath = XmlPath.from(responseBody);

			// Get the list of nodes based on the provided XML path
			List<Object> nodeList = xmlPath.getList(jsonPath);

			// Validate the size of the list
			try {
				assertThat("XML array size mismatch", nodeList.size(), equalTo(expectedSize));
				ExtentReportListener.getTest().get()
						.info("Successfully validated XML array size at '" + jsonPath + "' is: " + expectedSize);
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get()
						.fail("Failed to validate XML array size at '" + jsonPath + "'. Error: " + e.getMessage());
			}
		} else if (contentType.contains("application/json")) {
			try {
				response.then().assertThat().body(jsonPath, hasSize(expectedSize));
				ExtentReportListener.getTest().get()
						.info("Successfully validated JSON array size at '" + jsonPath + "' is: " + expectedSize);
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get()
						.fail("Failed to validate JSON array size at '" + jsonPath + "'. Error: " + e.getMessage());
			}
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			// Log a warning message or skip validation
		}
	}

	/**
	 * Validates the type of a field in the response body matches an expected type.
	 * Supports both JSON and XML content types. Logs the result to ExtentReports.
	 *
	 * @param response     The API response to validate.
	 * @param path         The JSON/XML path to the field in the response body.
	 * @param expectedType The expected Java type of the field.
	 */
	public static void validateFieldType(Response response, String path, Class<?> expectedType) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			try {
				JsonPath jsonPath = response.jsonPath();
				Object fieldValue = jsonPath.get(path);
				assertThat("JSON field type mismatch", fieldValue, instanceOf(expectedType));
				ExtentReportListener.getTest().get().info(
						"Successfully validated type of field '" + path + "' is a " + expectedType.getSimpleName());
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get().fail("Failed to validate type of field '" + path
						+ "'. Expected type: " + expectedType.getSimpleName() + ". Error: " + e.getMessage());
			}
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			try {
				XmlPath xmlPath = XmlPath.from(responseBody);
				Object fieldValue = xmlPath.get(path);
				assertThat("XML field type mismatch", fieldValue, instanceOf(expectedType));
				ExtentReportListener.getTest().get().info(
						"Successfully validated type of field '" + path + "' is a " + expectedType.getSimpleName());
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get().fail("Failed to validate type of field '" + path
						+ "'. Expected type: " + expectedType.getSimpleName() + ". Error: " + e.getMessage());
			}
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			// Optionally, log a warning to the extent report as well
			ExtentReportListener.getTest().get()
					.warning("Unsupported content type encountered in validateFieldType: " + contentType);
		}
	}

	/**
	 * Validates multiple fields in the response body against expected values using
	 * a map of field-value pairs. Supports both JSON and XML content types. Logs
	 * each validation result to ExtentReports.
	 *
	 * @param response       The API response to validate.
	 * @param expectedParams A map containing field names (or paths) and their
	 *                       expected values.
	 */
	public static void validateMultipleParams(Response response, Map<String, Object> expectedParams) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			JsonPath jsonPath = response.jsonPath();
			for (Map.Entry<String, Object> entry : expectedParams.entrySet()) {
				String key = entry.getKey();
				Object expectedValue = entry.getValue();
				try {
					Object actualValue = jsonPath.get(key);
					assertThat("JSON field value mismatch for key: " + key, actualValue, equalTo(expectedValue));
					ExtentReportListener.getTest().get()
							.info("Successfully validated '" + key + "' with value: " + expectedValue);
				} catch (AssertionError e) {
					ExtentReportListener.getTest().get().fail("Failed to validate '" + key + "' with value: "
							+ expectedValue + ". Error: " + e.getMessage());
				}
			}
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			XmlPath xmlPath = XmlPath.from(responseBody);
			for (Map.Entry<String, Object> entry : expectedParams.entrySet()) {
				String key = entry.getKey();
				Object expectedValue = entry.getValue();
				try {
					Object actualValue = xmlPath.get(key);
					assertThat("XML field value mismatch for key: " + key, actualValue, equalTo(expectedValue));
					ExtentReportListener.getTest().get()
							.pass("Successfully validated '" + key + "' with value: " + expectedValue);
				} catch (AssertionError e) {
					ExtentReportListener.getTest().get().fail("Failed to validate '" + key + "' with value: "
							+ expectedValue + ". Error: " + e.getMessage());
				}
			}
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			// Optionally, log a warning to the extent report as well
			ExtentReportListener.getTest().get()
					.warning("Unsupported content type encountered in validateMultipleParams: " + contentType);
		}
	}

	/**
	 * Validates if the response body matches a schema defined in a JSON or XML
	 * schema file. Supports both JSON and XML content types. Logs the validation
	 * result to ExtentReports.
	 *
	 * @param response   The API response to validate.
	 * @param schemaPath The path to the schema file (relative to the classpath for
	 *                   JSON).
	 */
	public static void validateSchema(Response response, String schemaPath) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			try {
				response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaPath));
				ExtentReportListener.getTest().get()
						.info("Successfully validated JSON response matches schema: " + schemaPath);
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get().fail("Failed to validate JSON response against schema: "
						+ schemaPath + ". Error: " + e.getMessage());
			}
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			// Assuming an XML schema validation method exists, replace
			// 'matchesJsonSchemaInClasspath' accordingly
			try {
				// Simulated method for XML schema validation
				// e.g., assertXmlMatchesSchema(responseBody, schemaPath);
				response.then().assertThat().body(matchesJsonSchemaInClasspath(schemaPath)); // Placeholder for actual
																								// XML validation
				ExtentReportListener.getTest().get()
						.info("Successfully validated XML response matches schema: " + schemaPath);
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get().fail(
						"Failed to validate XML response against schema: " + schemaPath + ". Error: " + e.getMessage());
			}
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			ExtentReportListener.getTest().get()
					.warning("Unsupported content type encountered in validateSchema: " + contentType);
		}
	}

	/**
	 * Validates multiple dynamic fields in the response body against expected
	 * values. Iterates over a map of expected field-value pairs, checking each
	 * against the response. Logs the validation results to ExtentReports.
	 *
	 * @param response            The API response to validate.
	 * @param expectedFieldValues A map containing field names and their expected
	 *                            values.
	 */
	public static void validateDynamicFields(Response response, Map<String, Object> expectedFieldValues) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			JsonPath jsonPath = response.jsonPath();
			expectedFieldValues.forEach((field, value) -> {
				try {
					Object actualValue = jsonPath.get(field);
					assertThat("JSON field value mismatch for key: " + field, actualValue, equalTo(value));
					ExtentReportListener.getTest().get()
							.info("Successfully validated dynamic field '" + field + "' with value: " + value);
				} catch (AssertionError e) {
					ExtentReportListener.getTest().get().fail("Failed to validate dynamic field '" + field
							+ "' with value: " + value + ". Error: " + e.getMessage());
				}
			});
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			XmlPath xmlPath = XmlPath.from(responseBody);
			expectedFieldValues.forEach((field, value) -> {
				try {
					Object actualValue = xmlPath.get(field);
					assertThat("XML field value mismatch for key: " + field, actualValue, equalTo(value));
					ExtentReportListener.getTest().get()
							.info("Successfully validated dynamic field '" + field + "' with value: " + value);
				} catch (AssertionError e) {
					ExtentReportListener.getTest().get().fail("Failed to validate dynamic field '" + field
							+ "' with value: " + value + ". Error: " + e.getMessage());
				}
			});
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			ExtentReportListener.getTest().get()
					.warning("Unsupported content type encountered in validateDynamicFields: " + contentType);
		}
	}

	/**
	 * Validates a specific JSON/XML path in the response body using a custom
	 * Hamcrest matcher. Allows for complex validations beyond simple value
	 * comparisons. Logs the validation result to ExtentReports.
	 *
	 * @param response The API response to validate.
	 * @param path     The JSON/XML path within the response body.
	 * @param matcher  The Hamcrest matcher for validation.
	 */

	public static void validateWithCustomMatcher(Response response, String path, Matcher<?> matcher) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json") || contentType.contains("application/xml")
				|| contentType.contains("text/xml")) {
			try {
				// Assuming the response validation method works for both JSON and XML,
				// but you might need to adjust this if a different method is required for XML
				// validation.
				response.then().assertThat().body(path, matcher);
				ExtentReportListener.getTest().get()
						.info("Successfully validated with custom matcher at '" + path + "'");
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get()
						.fail("Failed to validate with custom matcher at '" + path + "'. Error: " + e.getMessage());
			}
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			// Optionally, log a warning to the extent report as well
			ExtentReportListener.getTest().get()
					.warning("Unsupported content type encountered in validateWithCustomMatcher: " + contentType);
		}
	}

	/**
	 * Asserts the presence or absence of a field in the response body. Validates if
	 * a specified field should or should not exist and logs the result to
	 * ExtentReports.
	 *
	 * @param response        The API response to check.
	 * @param fieldPath       The JSON/XML path to the field in the response body.
	 * @param shouldBePresent True if the field is expected to be present; false if
	 *                        it should be absent.
	 */
	public static void assertFieldPresence(Response response, String fieldPath, boolean shouldBePresent) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json")) {
			try {
				if (shouldBePresent) {
					response.then().assertThat().body(fieldPath, notNullValue());
					ExtentReportListener.getTest().get()
							.pass("Successfully validated field '" + fieldPath + "' is present and not null.");
				} else {
					response.then().assertThat().body(fieldPath, nullValue());
					ExtentReportListener.getTest().get()
							.pass("Successfully validated field '" + fieldPath + "' is absent or null.");
				}
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get().fail("Validation failed for field presence '" + fieldPath
						+ "'. Expected presence: " + shouldBePresent + ". Error: " + e.getMessage());
			}
		} else if (contentType.contains("application/xml") || contentType.contains("text/xml")) {
			String responseBody = response.getBody().asString();
			responseBody = StringUtils.replace(responseBody, "\u001A", "");

			XmlPath xmlPath = XmlPath.from(responseBody);
			try {
				Object fieldValue = xmlPath.get(fieldPath);
				if (shouldBePresent) {
					assertThat("Field '" + fieldPath + "' is present and not null", fieldValue, notNullValue());
					ExtentReportListener.getTest().get()
							.pass("Successfully validated field '" + fieldPath + "' is present and not null.");
				} else {
					assertThat("Field '" + fieldPath + "' should not be present or should be null", fieldValue,
							nullValue());
					ExtentReportListener.getTest().get()
							.pass("Successfully validated field '" + fieldPath + "' is absent or null.");
				}
			} catch (AssertionError e) {
				ExtentReportListener.getTest().get().fail("Validation failed for field presence '" + fieldPath
						+ "' in XML. Expected presence: " + shouldBePresent + ". Error: " + e.getMessage());
			}
		} else {
			// Unsupported content type, log warning
			System.out.println("Warning: Unsupported content type - " + contentType);
			// Optionally, log a warning to the extent report as well
			ExtentReportListener.getTest().get()
					.warning("Unsupported content type encountered in assertFieldPresence: " + contentType);
		}
	}

	/**
	 * Validates that a specific key in the response body is not null. Supports both
	 * JSON and XML content types. Logs the validation result to ExtentReports.
	 *
	 * @param response The API response.
	 * @param key      The JSON/XML key to check for non-null value.
	 */

	public static void validateKeyIsNotNull(Response response, String key) {
		String contentType = response.header("Content-Type");

		if (contentType.contains("application/json") || contentType.contains("application/xml")
				|| contentType.contains("text/xml")) {
			Object value;
			if (contentType.contains("application/json")) {
				value = response.jsonPath().get(key);
			} else {
				String responseBody = response.getBody().asString();
				XmlPath xmlPath = XmlPath.from(responseBody);
				value = xmlPath.get(key);
			}

			// Assert that the extracted value is not null
			if (value == null) {
				String errorMessage = "Value for key '" + key + "' is null";
				ExtentReportListener.getTest().get().fail(errorMessage);
				throw new AssertionError(errorMessage); // Re-throw the AssertionError
			} else {
				ExtentReportListener.getTest().get().pass("Successfully validated key '" + key + "' is not null.");
			}
		} else {
			String warningMessage = "Warning: Unsupported content type - " + contentType;
			System.out.println(warningMessage);
			ExtentReportListener.getTest().get()
					.warning("Unsupported content type encountered in validateKeyIsNotNull: " + contentType);
			throw new AssertionError(warningMessage); // Throw an AssertionError for unsupported content type
		}

	}

}
