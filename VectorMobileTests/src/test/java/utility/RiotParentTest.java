package utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

import io.appium.java_client.remote.MobileCapabilityType;

public abstract class RiotParentTest extends TestUtilities {
	@BeforeGroups(groups="1driver")
	public void setUp1Driver() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.ANDROID_DEVICE1_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities.setCapability("noReset", true);
		//capabilities.setCapability("autoWebview", true);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setAndroidDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.PACKAGE_APP_NAME+" started on device "+Constant.ANDROID_DEVICE1_NAME +" with AppiumDriver 1.");
	}
	
	@BeforeGroups(groups="2drivers")
	public void setUp2Drivers() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.ANDROID_DEVICE1_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities.setCapability("noReset", true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		
		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability("deviceName",Constant.ANDROID_DEVICE2_NAME);
		capabilities2.setCapability("platformName","Android");
		capabilities2.setCapability("platformVersion", "4.3");
		capabilities2.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities2.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities2.setCapability("noReset", true);
		capabilities2.setCapability(MobileCapabilityType.FULL_RESET, false);
		
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setAndroidDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+Constant.ANDROID_DEVICE1_NAME +" with DRIVER 1.");
		
		appiumFactory.setAndroidDriver2(new URL(Constant.SERVER2_ADRESS), capabilities2);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+Constant.ANDROID_DEVICE2_NAME +" with DRIVER 2.");
	}
	
	@AfterGroups(groups="1driver")
	public void tearDownAndroidDriver1(){
		AppiumFactory.getAndroidDriver1().quit();
		System.out.println("Android DRIVER 1 quitted on ANDROID device "+Constant.ANDROID_DEVICE1_NAME +", closing application "+Constant.APPLICATION_NAME+".");
	}
	
	@AfterGroups(groups={"2drivers"}, alwaysRun=true)
	public void tearDown2AndroidDrivers() throws InterruptedException{
		AppiumFactory.getAndroidDriver1().quit();
		System.out.println("Android DRIVER 1 quitted on ANDROID device "+Constant.ANDROID_DEVICE1_NAME +", closing application "+Constant.APPLICATION_NAME+".");
		AppiumFactory.getAndroidDriver2().quit();
		System.out.println("Android DRIVER 2 quitted on ANDROID device "+Constant.ANDROID_DEVICE2_NAME +", closing application "+Constant.APPLICATION_NAME+".");
	}
	
	
	@BeforeGroups(groups="1driver_ios")
	public void setUp1IosDriver() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"iPhone");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"iOS");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3.5");
		capabilities.setCapability(MobileCapabilityType.UDID, Constant.IOS_DEVICE1_UDID);//"2a418a9dbcd960d904a501bf558120625f96f409");//e75c0085c74a872846772a6b2ee56a86849a4d92	//1c7e0b4559589b57396a57f8eaa382c9bc42d8d7
		capabilities.setCapability("bundleId", "im.vector.app");//app
		//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"XCUITest");
		//capabilities.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
		capabilities.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
		capabilities.setCapability("keychainPassword","appium6754");
		capabilities.setCapability("autoDismissAlerts", false);
//		capabilities.setCapability("autoWebview", true);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setiOSDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+Constant.IOS_DEVICE1_UDID +" with DRIVER 1.");
	}
	
	@BeforeGroups(groups="2driver_ios")
	public void setUp2IosDriver() throws MalformedURLException{
		DesiredCapabilities capabilities1 = new DesiredCapabilities();
		capabilities1.setCapability(MobileCapabilityType.DEVICE_NAME,"iPhone");
		capabilities1.setCapability(MobileCapabilityType.PLATFORM_NAME,"iOS");
		capabilities1.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3.5");
		capabilities1.setCapability(MobileCapabilityType.UDID, Constant.IOS_DEVICE1_UDID);//"2a418a9dbcd960d904a501bf558120625f96f409");//e75c0085c74a872846772a6b2ee56a86849a4d92	//1c7e0b4559589b57396a57f8eaa382c9bc42d8d7
		capabilities1.setCapability("bundleId", "im.vector.app");//app
		//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities1.setCapability(MobileCapabilityType.AUTOMATION_NAME,"XCUITest");
		//capabilities.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
		capabilities1.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities1.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities1.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
		capabilities1.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
		capabilities1.setCapability("keychainPassword","appium6754");
		capabilities1.setCapability("autoDismissAlerts", false);
		
		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability(MobileCapabilityType.DEVICE_NAME,"iPhone");
		capabilities2.setCapability(MobileCapabilityType.PLATFORM_NAME,"iOS");
		capabilities2.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3.5");
		capabilities2.setCapability(MobileCapabilityType.UDID, Constant.IOS_DEVICE2_UDID);//"2a418a9dbcd960d904a501bf558120625f96f409");//e75c0085c74a872846772a6b2ee56a86849a4d92	//1c7e0b4559589b57396a57f8eaa382c9bc42d8d7
		capabilities2.setCapability("bundleId", "im.vector.app");//app
		//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities2.setCapability(MobileCapabilityType.AUTOMATION_NAME,"XCUITest");
		//capabilities.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
		capabilities2.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities2.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities2.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
		capabilities2.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
		capabilities2.setCapability("keychainPassword","appium6754");
		capabilities2.setCapability("autoDismissAlerts", false);
		
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setiOSDriver1(new URL(Constant.SERVER1_ADRESS), capabilities1);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+Constant.IOS_DEVICE1_UDID +" with DRIVER 1.");
		
		appiumFactory.setiOSDriver2(new URL(Constant.SERVER2_ADRESS), capabilities2);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+Constant.IOS_DEVICE2_UDID +" with DRIVER 1.");
	}
	
	@AfterGroups(groups="1driver_ios")
	public void tearDownIosDriver1(){
		AppiumFactory.getiOsDriver1().quit();
		System.out.println("Ios DRIVER 1 quitted on IOS device "+Constant.IOS_DEVICE1_UDID +", closing application "+Constant.APPLICATION_NAME+".");
	}
	
	@AfterGroups(groups={"2driver_ios"}, alwaysRun=true)
	public void tearDown2IosDrivers() throws InterruptedException{
		AppiumFactory.getiOsDriver1().quit();
		System.out.println("Ios DRIVER 1 quitted on IOS device "+Constant.IOS_DEVICE1_UDID +", closing application "+Constant.APPLICATION_NAME+".");
		AppiumFactory.getiOsDriver2().quit();
		System.out.println("Ios DRIVER 2 quitted on IOS device "+Constant.IOS_DEVICE2_UDID +", closing application "+Constant.APPLICATION_NAME+".");
	}
	
	@BeforeGroups(groups="1driver_ios_sim")
	public void setUp1IosSimDriver() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,"STZ_DE_13243");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,"iOS");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 6s");
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "9.3");
		//capabilities.setCapability(MobileCapabilityType.UDID, "e75c0085c74a872846772a6b2ee56a86849a4d92");
		capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Downloads/Vector.ipa");
		capabilities.setCapability("bundleId", "im.vector.app");//app
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"XCUITest");
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);   
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setiOSDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+Constant.ANDROID_DEVICE1_NAME +" with DRIVER 1.");
	}
}
