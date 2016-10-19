package utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AppiumFactory {
	//private static AppiumFactory instance = new AppiumFactory();
	private static AndroidDriver<MobileElement> driver;

//	private AppiumFactory() {
//	}

//	// Get the only object available
//	public static AppiumFactory getInstance() {
//		return instance;
//	}

	// Get the only object available
	public void setDriver(URL url,DesiredCapabilities capabilities) throws MalformedURLException {
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);          
	}

	public static AndroidDriver<MobileElement> getAppiumDriver() {
		return driver;
	}   
}
