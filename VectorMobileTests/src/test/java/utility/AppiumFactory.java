package utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class AppiumFactory {
	private static AppiumFactory instance = new AppiumFactory();
	
	private static AppiumDriver<MobileElement> androidDriver1;
	private static AppiumDriver<MobileElement> androidDriver2;
	private static AppiumDriver<MobileElement> iosDriver1;
	private static AppiumDriver<MobileElement> iosDriver2;
	private static AppiumDriver<MobileElement> currentDriver;
	
	
	public static AppiumFactory getInstance() {
        return instance;
    }
	/*
	 *  Set Android drivers or iOS drivers
	 */
	public void setAndroidDriver1(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		androidDriver1 = new AndroidDriver<MobileElement>(url, capabilities);
	}
	public void setAndroidDriver2(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		androidDriver2 = new AndroidDriver<MobileElement>(url, capabilities);
	}
	public void setiOSDriver1(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		iosDriver1 = new IOSDriver<MobileElement>(url, capabilities);
	}
	public void setiOSDriver2(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		iosDriver2 = new IOSDriver<MobileElement>(url, capabilities);
	}
	
	/*
	 * Get driver 1 or 2
	 */
	public  AndroidDriver<MobileElement> getAndroidDriver1() {
		currentDriver= androidDriver1;
		return (AndroidDriver<MobileElement>) androidDriver1;
	}   
	public  AndroidDriver<MobileElement> getAndroidDriver2() {
		currentDriver= androidDriver2;
		return (AndroidDriver<MobileElement>) androidDriver2;
	}  
	public  IOSDriver<MobileElement> getiOsDriver1() {
		currentDriver= iosDriver1;
		return (IOSDriver<MobileElement>) iosDriver1;
	}   
	public  IOSDriver<MobileElement> getiOsDriver2() {
		currentDriver= iosDriver2;
		return (IOSDriver<MobileElement>) iosDriver2;
	}
	public  AppiumDriver<MobileElement> getCurrentDriver() {
		return currentDriver;
	}  
}
