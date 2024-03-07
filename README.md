# Restassured
# API Testing Framework with RestAssured and TestNG

This API Testing Framework is designed to simplify and streamline the process of writing and executing API tests. It leverages the powerful RestAssured library for API interactions and TestNG for test management and execution. The framework includes a set of utility classes and listeners to enhance testing capabilities, support dynamic data management, and produce detailed test reports.

## Key Components

### 1. ApiClient
A central class for sending HTTP requests (GET, POST, PUT, DELETE) and handling responses. It supports setting up requests with various content types (JSON, XML) and managing authentication.

### 2. ApiResponseValidator
Provides methods to validate response status codes, content, headers, and other aspects of the response against expected values. It simplifies assertion logic in tests.

### 3. Config and DynamicConfigLoader
`Config` manages API configurations such as base URI and paths. `DynamicConfigLoader` reads configuration settings from an external JSON file, enabling dynamic configuration based on the environment or test requirements.

### 4. RequestHelper
Facilitates the creation of `RequestSpecification` objects with customized settings for headers, query parameters, and body content. It abstracts RestAssured request configuration to minimize redundancy.

### 5. Setup
Utilizes TestNG annotations to prepare the test environment before test execution. It ensures that each test runs with the correct configuration settings.

### 6. User and UserUtils
`User` represents a user with dynamic properties, useful for tests involving user data. `UserUtils` manages a collection of `User` objects for easy access during tests.

### 7. ExtentReportListener
Implements `ITestListener` to integrate ExtentReports for detailed reporting on test execution. It automatically logs test outcomes, response details on failures, and supports parallel execution logging.

### 8. SerializationUtil and XmlHandler
`SerializationUtil` handles serialization of objects to JSON or XML strings based on the content type. `XmlHandler` offers utilities for parsing, updating, and formatting XML content.

### 9. Helper and JsonHandler
`Helper` provides simple utility methods, e.g., for getting MIME types. `JsonHandler` includes methods for JSON data manipulation, such as updating values and reading JSON files.

## Setup and Configuration

1. **Dependencies**: Ensure RestAssured and TestNG libraries are included in your project's dependencies.
2. **Configuration Files**: Place API configuration settings in a JSON file as per `DynamicConfigLoader` expectations. Adjust the `Config` class if necessary.
3. **TestNG Suite**: Define your tests in TestNG XML files, specifying test classes, methods, and any required parameters.

## Writing Tests

1. **Define Test Methods**: Use TestNG annotations (`@Test`) to define your test methods.
2. **Prepare Request Data**: Utilize `JsonHandler` or `XmlHandler` for handling request payloads. `SerializationUtil` can serialize objects based on the content type.
3. **Send Requests**: Use `ApiClient` to send requests to the API, specifying the endpoint, method, and request data.
4. **Validate Responses**: Apply methods from `ApiResponseValidator` to assert expected response statuses, headers, and content.
5. **Logging and Reporting**: `ExtentReportListener` automatically captures test outcomes and details for reporting.

## Running Tests

Execute your tests through TestNG, either via your IDE, command line, or build tools like Maven or Gradle. TestNG will automatically invoke the `ExtentReportListener`, generating a report at the end of the test execution.

## Extending the Framework

- **Add Custom Utility Methods**: Extend utility classes with methods specific to your testing needs.
- **Integrate Additional APIs**: Use `DynamicConfigLoader` to manage configurations for new APIs.
- **Enhance Reporting**: Customize `ExtentReportListener` to include additional information in test reports.

This framework offers a structured and extensible approach to API testing, focusing on ease of use, flexibility, and comprehensive reporting. By leveraging the provided utilities and following the setup instructions, you can efficiently write, execute, and report on your API tests.