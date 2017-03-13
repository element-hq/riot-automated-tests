package utility;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.appium.java_client.android.AndroidDriver;

public class ScreenshotUtility implements ITestListener {
	public static AppiumFactory appiumFactory=AppiumFactory.getInstance();
	
	public void onTestStart(ITestResult result) {

	}

	public void onTestSuccess(ITestResult result) {
		if(appiumFactory.getCurrentDriver()!=null)captureScreenShot(result, "pass");
	}

	public void onTestFailure(ITestResult result) {
		if(appiumFactory.getCurrentDriver()!=null)captureScreenShot(result, "fail");
	}

	public void onTestSkipped(ITestResult result) {

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	public void onStart(ITestContext context) {

	}

	public void onFinish(ITestContext context) {

	}
	// Function to capture screenshot.
	public void captureScreenShot(ITestResult result, String status) {
		//choice of output screenshot according to the sut
		String os="";
		if(appiumFactory.getCurrentDriver() instanceof AndroidDriver<?>){
			os="android";
		}else{
			os="ios";
		}
		System.out.println("Screenshot taken on "+os);
		String destDir = "";
		String passfailMethod = result.getMethod().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
		// To capture screenshot.
		File scrFile = ((TakesScreenshot) appiumFactory.getCurrentDriver()).getScreenshotAs(OutputType.FILE);
		DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");

		// If status = fail then set folder name "screenshots/Failures"
		if (status.equalsIgnoreCase("fail")) {
			destDir = "screenshots_"+os+"/Failures";
		}
		// If status = pass then set folder name "screenshots/Success"
		else if (status.equalsIgnoreCase("pass")) {
			destDir = "screenshots_"+os+"/Success";
		}

		// To create folder to store screenshots
		new File(destDir).mkdirs();
		// Set file name with combination of test class name + date time.
		String destFile = passfailMethod + " - " + dateFormat.format(new Date()) + ".png";

		try {
			// Store file at destination folder location
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
