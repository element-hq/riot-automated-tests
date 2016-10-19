package utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class ScreenshotUtility implements ITestListener {

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub
		captureScreenShot(result, "pass");
	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		captureScreenShot(result, "fail");
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub

	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub

	}
	// Function to capture screenshot.
	 public void captureScreenShot(ITestResult result, String status) {
		 AppiumFactory appiumFactory=new AppiumFactory();
		 System.out.println("Screenshot taken");
	  // AndroidDriver driver=ScreenshotOnPassFail.getDriver();
	  String destDir = "";
	  String passfailMethod = result.getMethod().getRealClass().getSimpleName() + "." + result.getMethod().getMethodName();
	  // To capture screenshot.
	  File scrFile = ((TakesScreenshot) appiumFactory.getAppiumDriver()).getScreenshotAs(OutputType.FILE);
	  DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
	  // If status = fail then set folder name "screenshots/Failures"
	  if (status.equalsIgnoreCase("fail")) {
	   destDir = "screenshots/Failures";
	  }
	  // If status = pass then set folder name "screenshots/Success"
	  else if (status.equalsIgnoreCase("pass")) {
	   destDir = "screenshots/Success";
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
