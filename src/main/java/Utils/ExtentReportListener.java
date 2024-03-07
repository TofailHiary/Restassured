package Utils;




import java.io.PrintWriter;
import java.io.StringWriter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;


import Api.RestGpt.ApiClient;
import io.restassured.response.Response;

public class ExtentReportListener implements ITestListener {
	private static ExtentReports extent = ExtentManager.getReporter();
	 private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	@Override
	public void onStart(ITestContext context) {
		System.out.println("Test Suite started!");
	}

	@Override
	public void onFinish(ITestContext context) {
		System.out.println("Test Suite is ending!");
		extent.flush(); // Write test information to report
	}

	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getDescription(); // Get the description
		System.out.println("Description: " + testName); // Debug statement

		if (testName == null || testName.isEmpty()) {
			testName = result.getMethod().getMethodName();
		}

		System.out.println("Test being started: " + testName); // Debug statement
		getTest().set(extent.createTest(testName));
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		System.out.println(result.getMethod().getMethodName() + " passed!");
		getTest().get().pass("Test passed with all validations successful.");
	}

	// In ExtentReportListener class:

	// In ExtentReportListener class:

	@Override
	public void onTestFailure(ITestResult result) {
		System.out.println(result.getMethod().getMethodName() + " failed!");

		// Get the response from the ApiClient class or from the test itself
		Response response = ApiClient.getLastResponse(); // You need to implement this method in ApiClient

		// Log the response details to the ExtentReport
		if (response != null) {
		//	getTest().get().fail("Status Code: " + response.getStatusCode())
			getTest().get().fail("Response: " + response.getBody().asString())
					.fail("Headers: " + response.getHeaders().toString());
			// Add more details as required
		} else {
			getTest().get().fail("Test failed without a response");
		}
		  // Capture and log exception details
	    Throwable throwable = result.getThrowable();
	    if (throwable != null) {
	        // Convert the stack trace to a string
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        throwable.printStackTrace(pw);
	        String stackTrace = sw.toString();

	        // Log the error message and stack trace
	        getTest().get().fail("Error Message: " + throwable.getMessage())
	                .fail("Stack Trace: " + stackTrace);
	    }
	    getTest().get().fail("Test failed with some or all validations");
	}

	@Override
	public void onTestSkipped(ITestResult result) {
		System.out.println(result.getMethod().getMethodName() + " skipped!");
		getTest().get().skip(result.getThrowable());
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("onTestFailedButWithinSuccessPercentage for " + result.getMethod().getMethodName());
	}

	public static ThreadLocal<ExtentTest> getTest() {
		return test;
	}

	public static void setTest(ThreadLocal<ExtentTest> test) {
		ExtentReportListener.test = test;
	}
	
	
}