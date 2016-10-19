//package utility;
//
//import java.io.IOException;
//import java.net.URL;
//import java.util.concurrent.TimeUnit;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.BeforeTest;
//import org.testng.annotations.Test;
//
//import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.android.AndroidDriver;
//import mobilestests.Riot;
//
//public class notused_AppiumStartAndStop {
//	Process p;
//	 // Set path of your node.exe file.
//	 // Progra~1 represents Program Files folder.
//	 String nodePath = "C:/Progra~1/Appium/node.exe";
//	 // Set path of your appium.js file.
//	 String appiumJSPath = "C:/Progra~1/Appium/node_modules/appium/bin/appium.js";
//	 String cmd = nodePath + " " + appiumJSPath;
//
//	 private static AppiumDriver<WebElement> driver;
//
//	 // This method Is responsible for starting appium server.
//	 public void appiumStart() throws IOException, InterruptedException {
//	  // Execute command string to start appium server.
//	  p = Runtime.getRuntime().exec(cmd);
//	  // Provide wait time of 10 mins to start appium server properly.
//	  // If face any error(Could not start a new session...) then Increase
//	  // this time to 15 or 20 mins.
//	  Thread.sleep(10000);
//	  if (p != null) {
//	   System.out.println("Appium server Is started now.");
//	  }
//	 }
//
//	 // This method Is responsible for stopping appium server.
//	 public void appiumStop() throws IOException {
//	  if (p != null) {
//	   p.destroy();
//	  }
//	  System.out.println("Appium server Is stopped now.");
//	 }
//
//	 @BeforeClass
//	 public void setUp() throws Exception {
//	  // Stop appium server If It Is already running.
//	  appiumStop();
//	  // Start appium server.
//	  appiumStart();
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability("deviceName","a71011c8");
//		capabilities.setCapability("platformName","Android");
//		capabilities.setCapability("platformVersion", "4.4.2");
//
//		capabilities.setCapability("appPackage", "im.vector.alpha");
//		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");
//		setDriver(new AndroidDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"),capabilities));
//		//Wait until app is really started (experimental)
//		System.out.println("setUp()");
//		//MobileElement expelement=(MobileElement) (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.FrameLayout")));
//		(new WebDriverWait(getDriver(), 15)).until(ExpectedConditions.presenceOfElementLocated(By.id("im.vector.alpha:id/action_bar_root")));
//		System.out.println("fin setUp()");
//	 }
//
////	 @Test
////	 public void ScrollToView() {
////	  // Scroll till element which contains "Views" text If It Is not visible on screen.
////	  driver.scrollTo("Views");
////	  // Click on Views.
////	  driver.findElement(By.name("Views")).click();
////	 }
//
//	 @AfterTest
//	 public void End() throws IOException {
//	  driver.quit();
//	  // Stop appium server when test Is ended.
//	  appiumStop();
//	 }
//		/**
//		 * @return the driver
//		 */
//		public static AppiumDriver<WebElement> getDriver() {
//			return driver;
//		}
//
//		/**
//		 * @param driver the driver to set
//		 */
//		public static void setDriver(AppiumDriver<WebElement> driver) {
//			Riot.driver = driver;
//		}
//}
