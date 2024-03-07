package Api.RestGpt;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

public class RequestHelper {
	/**
	 * Prepares a request specification with specified parameters, headers, and
	 * filters.
	 * 
	 * @param baseUri     The base URI for the API.
	 * @param basePath    The base path for the API.
	 * @param contentType The content type of the request (e.g., JSON or XML).
	 * @param headers     Additional headers to include in the request.
	 * @param queryParams Query parameters to append to the URI.
	 * @param filters     A list of filters for logging or modifying requests and
	 *                    responses.
	 * @return A RequestSpecification configured with the provided details.
	 */
	public static RequestSpecification prepareRequest(String baseUri, String basePath, ContentType contentType,
			Map<String, String> headers, Map<String, String> queryParams, List<Filter> filters) {
		RequestSpecBuilder builder = new RequestSpecBuilder();

		// Set the base URI and path, only if they are provided
		if (baseUri != null && !baseUri.isEmpty()) {
			builder.setBaseUri(baseUri);
		}
		if (basePath != null && !basePath.isEmpty()) {
			builder.setBasePath(basePath);
		}

		// Set content type, defaulting to JSON if none is provided
		builder.setContentType(contentType != null ? contentType : ContentType.JSON);

		// Add any additional headers provided
		if (headers != null) {

			headers.forEach(builder::addHeader);

		} else if (headers == null) {

			// Add the "Accept" header, defaulting to application/json if not specified in
			// headers map
			// builder.addHeader("Accept", "application/json");
		}
		String token = ApiClient.getBearerToken();
		if (token != null && !token.isEmpty()) {
			builder.addHeader("Authorization", "Bearer " + token);
		}
		// Add any provided filters
		if (filters != null) {
			filters.forEach(builder::addFilter);
		}
		if (queryParams != null)
			queryParams.forEach(builder::addQueryParam);

		RequestSpecification requestSpecification = builder.build();
		return RestAssured.given().spec(requestSpecification);
	}

	/**
	 * Adds basic authentication to a request specification.
	 * @param request The request specification to add authentication to.
	 * @param username The username for basic authentication.
	 * @param password The password for basic authentication.
	 * @return The modified RequestSpecification with basic authentication.
	 */
	public static RequestSpecification withBasicAuth(RequestSpecification request, String username, String password) {
		return request.auth().preemptive().basic(username, password);
	}

	/**
	 * Adds OAuth2 authentication to a request specification.
	 * @param request The request specification to add authentication to.
	 * @param token The token for OAuth2 authentication.
	 * @return The modified RequestSpecification with OAuth2 authentication.
	 */
	public static RequestSpecification withOAuth2(RequestSpecification request, String token) {
		return request.auth().preemptive().oauth2(token);
	}
	/**
	 * Adds headers to a request specification.
	 * @param request The request specification to add headers to.
	 * @param headers A map of headers to add to the request.
	 * @return The modified RequestSpecification with added headers.
	 */
	public static RequestSpecification withHeaders(RequestSpecification request, Map<String, String> headers) {
		headers.forEach(request::header);
		return request;
	}

	/**
	 * Adds query parameters to a request specification.
	 * @param request The request specification to add query parameters to.
	 * @param queryParams A map of query parameters to add to the request.
	 * @return The modified RequestSpecification with added query parameters.
	 */
	public static RequestSpecification withQueryParams(RequestSpecification request, Map<String, String> queryParams) {
		queryParams.forEach(request::queryParam);
		return request;
	}

	/**
	 * Formats an endpoint URL by replacing placeholders with values from a map.
	 * @param endpoint The endpoint URL with placeholders.
	 * @param pathParams A map of path parameters to replace placeholders in the endpoint URL.
	 * @return The formatted endpoint URL with placeholders replaced by actual values.
	 */
	public static String formatEndpoint(String endpoint, Map<String, String> pathParams) {
		for (Map.Entry<String, String> entry : pathParams.entrySet()) {
			endpoint = endpoint.replace("{" + entry.getKey() + "}", entry.getValue());
		}
		return endpoint;
	}

}
