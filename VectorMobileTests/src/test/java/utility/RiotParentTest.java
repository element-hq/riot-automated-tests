package utility;

import java.net.URL;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

import io.appium.java_client.remote.MobileCapabilityType;

public abstract class RiotParentTest extends TestUtilities {
	@BeforeGroups(groups="1driver_android")
	public void setUp1AndroidDriver() throws Exception{
		//Start appium server 1 if necessary.
		startAppiumServer1();
		Map<String, String> androidDevice1=ReadConfigFile.getInstance().getDevicesMap().get("android device 1");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.UDID,androidDevice1.get(MobileCapabilityType.UDID));
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,androidDevice1.get(MobileCapabilityType.DEVICE_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,androidDevice1.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, androidDevice1.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setAndroidDriver1(new URL(Constant.getServer1HttpAddress()), capabilities);
		System.out.println("Application "+Constant.PACKAGE_APP_NAME+" started on device "+capabilities.getCapability(MobileCapabilityType.DEVICE_NAME) +" with AppiumDriver 1.");
	}

	@BeforeGroups(groups="2drivers_android")
	public void setUp2AndroidDrivers() throws Exception{
		//Start the two appium's servers if necessary.
		start2AppiumServers();
		Map<String, String> androidDevice1=ReadConfigFile.getInstance().getDevicesMap().get("android device 1");
		Map<String, String> androidDevice2=ReadConfigFile.getInstance().getDevicesMap().get("android device 2");
		DesiredCapabilities capabilities1 = new DesiredCapabilities();
		capabilities1.setCapability(MobileCapabilityType.UDID,androidDevice1.get(MobileCapabilityType.UDID));
		capabilities1.setCapability(MobileCapabilityType.DEVICE_NAME,androidDevice1.get(MobileCapabilityType.DEVICE_NAME));
		capabilities1.setCapability(MobileCapabilityType.PLATFORM_NAME,androidDevice1.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities1.setCapability(MobileCapabilityType.PLATFORM_VERSION, androidDevice1.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities1.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities1.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities1.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities1.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "1200");
		capabilities1.setCapability(MobileCapabilityType.FULL_RESET, false);

		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability(MobileCapabilityType.UDID,androidDevice2.get(MobileCapabilityType.UDID));
		capabilities2.setCapability(MobileCapabilityType.DEVICE_NAME,androidDevice2.get(MobileCapabilityType.DEVICE_NAME));
		capabilities2.setCapability(MobileCapabilityType.PLATFORM_NAME,androidDevice2.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities2.setCapability(MobileCapabilityType.PLATFORM_VERSION, androidDevice2.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities2.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities2.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);
		capabilities2.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities2.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "1200");
		capabilities2.setCapability(MobileCapabilityType.FULL_RESET, false);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setAndroidDriver1(new URL(Constant.getServer1HttpAddress()), capabilities1);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+capabilities1.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");

		appiumFactory.setAndroidDriver2(new URL(Constant.getServer2HttpAddress()), capabilities2);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+capabilities2.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 2.");
	}

	@AfterGroups(groups="1driver_android")
	public void tearDownAndroidDriver1() throws Exception{
		AppiumFactory.getAndroidDriver1().quit();
		System.out.println("Android DRIVER 1 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("android device 1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
		//Stop appium server1.
		stopAppiumServer1();
	}

	@AfterGroups(groups={"2drivers_android"}, alwaysRun=true)
	public void tearDown2AndroidDrivers() throws Exception{
		AppiumFactory.getAndroidDriver1().quit();
		System.out.println("Android DRIVER 1 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("android device 1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
		AppiumFactory.getAndroidDriver2().quit();
		System.out.println("Android DRIVER 2 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("android device 2").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
		//Stop the two appium's servers.
		stop2AppiumServers();
	}

	@BeforeGroups(groups="1driver_ios")
	public void setUp1IosDriver() throws Exception{
		//Start appium server 1 if necessary.
		startAppiumServer1();
		Map<String, String> iosDevice1=ReadConfigFile.getInstance().getDevicesMap().get("ios device 1");
		
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.UDID, iosDevice1.get(MobileCapabilityType.UDID));
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice1.get(MobileCapabilityType.DEVICE_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice1.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice1.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities.setCapability("bundleId", "im.vector.app");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,iosDevice1.get(MobileCapabilityType.AUTOMATION_NAME));
		capabilities.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
		capabilities.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
		capabilities.setCapability("keychainPassword","appium6754");
		capabilities.setCapability("autoDismissAlerts", false);
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);
		//		capabilities.setCapability("autoWebview", true);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setiOSDriver1(new URL(Constant.getServer1HttpAddress()), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");
	}
	@BeforeGroups(groups="1driver_ios_bis")
	public void setUp1IosDriverBis() throws Exception{
		//Start appium server 1 if necessary.
		startAppiumServer1();
		Map<String, String> iosDevice2=ReadConfigFile.getInstance().getDevicesMap().get("ios device 2");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.UDID, iosDevice2.get(MobileCapabilityType.UDID));
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice2.get(MobileCapabilityType.DEVICE_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice2.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice2.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities.setCapability("bundleId", "im.vector.app");//app
		//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"XCUITest");
		capabilities.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
		capabilities.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
		capabilities.setCapability("keychainPassword","appium6754");
		capabilities.setCapability("autoDismissAlerts", false);
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);
		//		capabilities.setCapability("autoWebview", true);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setiOSDriver1(new URL(Constant.getServer1HttpAddress()), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");
	}
	@BeforeGroups(groups="2drivers_ios")
	public void setUp2IosDriver() throws Exception{
		//Start the two appium's servers if necessary.
		start2AppiumServers();
		Map<String, String> iosDevice1=ReadConfigFile.getInstance().getDevicesMap().get("ios device 1");
		Map<String, String> iosDevice2=ReadConfigFile.getInstance().getDevicesMap().get("ios device 2");
		DesiredCapabilities capabilities1 = new DesiredCapabilities();
		capabilities1.setCapability(MobileCapabilityType.UDID, iosDevice1.get(MobileCapabilityType.UDID));
		capabilities1.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice1.get(MobileCapabilityType.DEVICE_NAME));
		capabilities1.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice1.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities1.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice1.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities1.setCapability("bundleId", "im.vector.app");//app
		//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities1.setCapability(MobileCapabilityType.AUTOMATION_NAME,iosDevice1.get(MobileCapabilityType.AUTOMATION_NAME));
		capabilities1.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
		capabilities1.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities1.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities1.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
		capabilities1.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
		capabilities1.setCapability("keychainPassword","appium6754");
		capabilities1.setCapability("autoDismissAlerts", false);
		capabilities1.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);

		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability(MobileCapabilityType.UDID, iosDevice2.get(MobileCapabilityType.UDID));
		capabilities2.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice2.get(MobileCapabilityType.DEVICE_NAME));
		capabilities2.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice2.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities2.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice2.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities2.setCapability("bundleId", "im.vector.app");//app
		//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities2.setCapability(MobileCapabilityType.AUTOMATION_NAME,iosDevice2.get(MobileCapabilityType.AUTOMATION_NAME));
		capabilities2.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
		capabilities2.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities2.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities2.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
		capabilities2.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
		capabilities2.setCapability("keychainPassword","appium6754");
		capabilities2.setCapability("autoDismissAlerts", false);
		capabilities2.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setiOSDriver1(new URL(Constant.getServer1HttpAddress()), capabilities1);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities1.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");

		appiumFactory.setiOSDriver2(new URL(Constant.getServer2HttpAddress()), capabilities2);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities2.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 2.");
	}

	@AfterGroups(groups="1driver_ios")
	public void tearDownIosDriver1() throws Exception{
		AppiumFactory.getiOsDriver1().quit();
		System.out.println("Ios DRIVER 1 quitted on IOS device "+ReadConfigFile.getInstance().getDevicesMap().get("ios device 1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");

		//Stop appium server1.
		stopAppiumServer1();
	}

	@AfterGroups(groups={"2drivers_ios"}, alwaysRun=true)
	public void tearDown2IosDrivers() throws Exception{
		AppiumFactory.getiOsDriver1().quit();
		System.out.println("Ios DRIVER 1 quitted on IOS device "+ReadConfigFile.getInstance().getDevicesMap().get("ios device 1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
		AppiumFactory.getiOsDriver2().quit();
		System.out.println("Ios DRIVER 2 quitted on IOS device "+ReadConfigFile.getInstance().getDevicesMap().get("ios device 2").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");

		//Stop the two appium's servers.
		stop2AppiumServers();
	}
	
	/**
	 * According to the chosen starting mode, start the first appium server if needed.
	 * @throws Exception 
	 */
	private void startAppiumServer1() throws Exception{
		switch (ReadConfigFile.getInstance().getConfMap().get("starting_server_mode")) {
		case "1":
			AppiumServerStartAndStopCmdLine.startAppiumServer1IfNecessary();
			break;
		case "2":
			AppiumServerStartAndStopService.startAppiumServer1IfNecessary();
			break;
		default:
			break;
		}
	}
	/**
	 * According to the chosen starting mode, start the two appiums servers if needed.
	 * @throws Exception 
	 */
	private void start2AppiumServers() throws Exception{
		switch (ReadConfigFile.getInstance().getConfMap().get("starting_server_mode")) {
		case "1":
			AppiumServerStartAndStopCmdLine.startAppiumServer1IfNecessary();
			AppiumServerStartAndStopCmdLine.startAppiumServer2IfNecessary();
			break;
		case "2":
			AppiumServerStartAndStopService.startAppiumServer1IfNecessary();
			AppiumServerStartAndStopService.startAppiumServer2IfNecessary();
			break;
		default:
			break;
		}
	}
	/**
	 * According to the chosen starting mode, stop the first appium.
	 * @throws Exception 
	 */
	private void stopAppiumServer1() throws Exception{
		switch (ReadConfigFile.getInstance().getConfMap().get("starting_server_mode")) {
		case "1":
			AppiumServerStartAndStopCmdLine.stopAppiumServer1();
			break;
		case "2":
			AppiumServerStartAndStopService.appiumServer1Stop();
			break;
		default:
			break;
		}
	}
	/**
	 * According to the chosen starting mode, stop the two appiums servers.
	 * @throws Exception 
	 */
	private void stop2AppiumServers() throws Exception{
		switch (ReadConfigFile.getInstance().getConfMap().get("starting_server_mode")) {
		case "1":
			AppiumServerStartAndStopCmdLine.stopAppiumServer1();
			AppiumServerStartAndStopCmdLine.stopAppiumServer2();
			break;
		case "2":
			AppiumServerStartAndStopService.appiumServer1Stop();
			AppiumServerStartAndStopService.appiumServer2Stop();
			break;
		default:
			break;
		}
	}

}
