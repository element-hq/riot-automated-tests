package utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

import io.appium.java_client.remote.MobileCapabilityType;

public class RiotParentTest extends TestUtilities{
	@BeforeGroups(groups="1driver")
	public void setUp1Driver() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.DEVICE1_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities.setCapability("noReset", true);
		//capabilities.setCapability("autoWebview", true);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setAndroidDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.PACKAGE_APP_NAME+" started on device "+Constant.DEVICE1_NAME +" with AppiumDriver 1.");
	}
	
	@BeforeGroups(groups="2drivers")
	public void setUp2Drivers() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.DEVICE1_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities.setCapability("noReset", true);
		
		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability("deviceName",Constant.DEVICE2_NAME);
		capabilities2.setCapability("platformName","Android");
		capabilities2.setCapability("platformVersion", "4.3");
		capabilities2.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities2.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities.setCapability("noReset", true);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setAndroidDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+Constant.DEVICE1_NAME +" with DRIVER 1.");
		
		appiumFactory.setAndroidDriver2(new URL(Constant.SERVER2_ADRESS), capabilities2);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+Constant.DEVICE2_NAME +" with DRIVER 2.");
	}
	
	@AfterGroups(groups="1driver")
	public void tearDown1Driver(){
		AppiumFactory.getAndroidDriver1().quit();
		System.out.println("DRIVER 1 quitted, closing application "+Constant.APPLICATION_NAME+".");
	}
	
	@AfterGroups(groups="2drivers")
	public void tearDown2Drivers(){
		AppiumFactory.getAndroidDriver1().quit();
		System.out.println("Android DRIVER 1 quitted on ANDROID device "+Constant.DEVICE1_NAME +", closing application "+Constant.APPLICATION_NAME+".");
		AppiumFactory.getAndroidDriver2().quit();
		System.out.println("Android DRIVER 2 quitted on ANDROID device "+Constant.DEVICE2_NAME +", closing application "+Constant.APPLICATION_NAME+".");
	}
	
	@BeforeGroups(groups="1driver_ios")
	public void setUp1IosDriver() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"STZ_DE_13243");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"iOS");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3.5");
		capabilities.setCapability(MobileCapabilityType.UDID, "e75c0085c74a872846772a6b2ee56a86849a4d92");
		capabilities.setCapability("bundleId", "im.vector.app");//app
		//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
		//TODO: XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		//capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"XCUITest");
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		//capabilities.setCapability("autoWebview", true);

		//AppiumFactory// appiumFactory=new AppiumFactory();
		//appiumFactory.setDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);      
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setiOSDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+Constant.DEVICE1_NAME +" with DRIVER 1.");
	}
}
