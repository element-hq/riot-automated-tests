package utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AppiumFactory {
	private static AppiumDriver<MobileElement> driver1;
	private static AppiumDriver<MobileElement> driver2;
	// Get the only object available
	public void setDriver1(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		driver1 = new AndroidDriver<MobileElement>(url, capabilities);          
	}
	public void setDriver2(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		driver2 = new AndroidDriver<MobileElement>(url, capabilities);          
	}
	public static AndroidDriver<MobileElement> getAppiumDriver1() {
		return (AndroidDriver<MobileElement>) driver1;
	}   
	public static AndroidDriver<MobileElement> getAppiumDriver2() {
		return (AndroidDriver<MobileElement>) driver2;
	}  
}
