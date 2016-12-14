package utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class AppiumFactory {
	private static AppiumDriver<MobileElement> androidDriver1;
	private static AppiumDriver<MobileElement> androidDriver2;
	private static AppiumDriver<MobileElement> iosDriver3;
	private static AppiumDriver<MobileElement> iosDriver4;
	private static AppiumDriver<MobileElement> currentDriver;
	// Set Android drivers or iOS drivers
	public void setAndroidDriver1(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		androidDriver1 = new AndroidDriver<MobileElement>(url, capabilities);          
	}
	public void setAndroidDriver2(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		androidDriver2 = new AndroidDriver<MobileElement>(url, capabilities);          
	}
	public void setiOSDriver1(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		iosDriver3 = new IOSDriver<MobileElement>(url, capabilities);          
	}
	public void setiOSDriver2(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		iosDriver4 = new IOSDriver<MobileElement>(url, capabilities);          
	}
	
	
	//Get driver 1 or 2
	public static AndroidDriver<MobileElement> getAndroidDriver1() {
		currentDriver= androidDriver1;
		return (AndroidDriver<MobileElement>) androidDriver1;
	}   
	public static AndroidDriver<MobileElement> getAndroidDriver2() {
		currentDriver= androidDriver2;
		return (AndroidDriver<MobileElement>) androidDriver2;
	}  
	public static IOSDriver<MobileElement> getiOsDriver1() {
		currentDriver= iosDriver3;
		return (IOSDriver<MobileElement>) iosDriver3;
	}   
	public static IOSDriver<MobileElement> getiOsDriver2() {
		currentDriver= iosDriver4;
		return (IOSDriver<MobileElement>) iosDriver4;
	}
	public static AppiumDriver<MobileElement> getCurrentDriver() {
		return currentDriver;
	}  
}
