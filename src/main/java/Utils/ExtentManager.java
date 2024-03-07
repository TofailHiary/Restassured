package Utils;



import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentManager {
	private static ExtentReports extent;

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {
			// Initialize ExtentReports and attach the SparkReporter
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
			String dateTime = formatter.format(new Date());
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter("extent_" + dateTime + ".html");
			extent = new ExtentReports();
			extent.attachReporter(sparkReporter);
		}
		return extent;
	}
}