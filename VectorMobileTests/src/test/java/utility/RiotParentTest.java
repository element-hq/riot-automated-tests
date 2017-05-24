package utility;

import java.net.URL;
import java.util.Map;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public abstract class RiotParentTest extends TestUtilities {
	public static AppiumFactory appiumFactory=AppiumFactory.getInstance();

	@BeforeGroups(groups="1driver_android")
	public void setUp1AndroidDriver() throws Exception{
		//Start appium server 1 if necessary.
		startAppiumServer1();

		//Create android driver 1 if necessary
		//if(appiumFactory.getAndroidDriver1()==null || appiumFactory.getAndroidDriver1()!=null && appiumFactory.getAndroidDriver1().getSessionId()==null){
		if(appiumFactory.getAndroidDriver1()==null || appiumFactory.getAndroidDriver1().getSessionId()==null){
			Map<String, String> androidDevice1=ReadConfigFile.getInstance().getDevicesMap().get("androiddevice1");
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
			//Set android driver 1
			appiumFactory.setAndroidDriver1(new URL(Constant.getServer1HttpAddress()), capabilities);
			System.out.println("Application "+Constant.PACKAGE_APP_NAME+" started on device "+capabilities.getCapability(MobileCapabilityType.DEVICE_NAME) +" with AppiumDriver 1.");	
		}
	}

	/**
	 * @throws Exception
	 */
	@BeforeGroups(groups="2drivers_android")
	public void setUp2AndroidDrivers() throws Exception{
		//Start the two appium's servers if necessary.
		start2AppiumServers();

		Map<String, String> androidDevice1=ReadConfigFile.getInstance().getDevicesMap().get("androiddevice1");
		Map<String, String> androidDevice2=ReadConfigFile.getInstance().getDevicesMap().get("androiddevice2");

		//Create android driver 1 if necessary
		if(appiumFactory.getAndroidDriver1()==null || appiumFactory.getAndroidDriver1().getSessionId()==null){
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

			appiumFactory.setAndroidDriver1(new URL(Constant.getServer1HttpAddress()), capabilities1);
			System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+capabilities1.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");	
		}

		if(appiumFactory.getAndroidDriver2()==null || appiumFactory.getAndroidDriver2().getSessionId()==null){
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
			appiumFactory.setAndroidDriver2(new URL(Constant.getServer2HttpAddress()), capabilities2);
			System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+capabilities2.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 2.");
		}
	}

	//	@AfterGroups(groups="1driver_android")
	//	public void tearDownAndroidDriver1() throws Exception{
	//		appiumFactory.getAndroidDriver1().quit();
	//		System.out.println("Android DRIVER 1 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("androiddevice1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
	//		System.out.println("session id driver 1 after quitting :"+appiumFactory.getAndroidDriver1().getSessionId());
	//		//Stop appium server1.
	//		stopAppiumServer1();
	//	}
	//
	//	@AfterGroups(groups={"2drivers_android"}, alwaysRun=true)
	//	public void tearDown2AndroidDrivers() throws Exception{
	//		appiumFactory.getAndroidDriver1().quit();
	//		System.out.println("Android DRIVER 1 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("androiddevice1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
	//		appiumFactory.getAndroidDriver2().quit();
	//		System.out.println("Android DRIVER 2 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("androiddevice2").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
	//		System.out.println("session id driver 1 after quitting :"+appiumFactory.getAndroidDriver1().getSessionId());
	//		System.out.println("session id driver 2 after quitting :"+appiumFactory.getAndroidDriver2().getSessionId());
	//		//Stop the two appium's servers.
	//		stop2AppiumServers();
	//	}

	@AfterGroups(groups={"2drivers_android","1driver_android","1driver_ios","2drivers_ios"})
	public void tearDownAllDrivers() throws Exception{
		if(null!=appiumFactory.getAndroidDriver1()){
			appiumFactory.getAndroidDriver1().quit();
			System.out.println("Android DRIVER 1 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("androiddevice1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");	
		}
		if(null!=appiumFactory.getAndroidDriver2()){
			appiumFactory.getAndroidDriver2().quit();
			System.out.println("Android DRIVER 2 quitted on ANDROID device "+ReadConfigFile.getInstance().getDevicesMap().get("androiddevice2").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");	
		}
		if(null!=appiumFactory.getiOsDriver1()){
			appiumFactory.getiOsDriver1().quit();
			System.out.println("Ios DRIVER 1 quitted on iOS device "+ReadConfigFile.getInstance().getDevicesMap().get("iosdevice1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");	
		}
		if(null!=appiumFactory.getiOsDriver2()){
			appiumFactory.getiOsDriver2().quit();
			System.out.println("Ios DRIVER 2 quitted on iOS device "+ReadConfigFile.getInstance().getDevicesMap().get("iosdevice2").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");	
		}
		//Stop the two appium's servers.
		stop2AppiumServers();
	}

	@BeforeGroups(groups="1driver_ios")
	public void setUp1IosDriver() throws Exception{
		//Start appium server 1 if necessary.
		startAppiumServer1();

		//Create ios driver 1 if necessary
		if(appiumFactory.getiOsDriver1()==null || appiumFactory.getiOsDriver1().getSessionId()==null){
			Map<String, String> iosDevice1=ReadConfigFile.getInstance().getDevicesMap().get("iosdevice1");
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setCapability(MobileCapabilityType.UDID, iosDevice1.get(MobileCapabilityType.UDID));
			capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice1.get(MobileCapabilityType.DEVICE_NAME));
			capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice1.get(MobileCapabilityType.PLATFORM_NAME));
			capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice1.get(MobileCapabilityType.PLATFORM_VERSION));
			capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "im.vector.app");
			capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,iosDevice1.get(MobileCapabilityType.AUTOMATION_NAME));
			capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
			capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
			capabilities.setCapability("xcodeOrgId", ReadConfigFile.getInstance().getConfMap().get("development_team"));
			capabilities.setCapability("xcodeSigningId", ReadConfigFile.getInstance().getConfMap().get("code_sign_identity"));
			//capabilities.setCapability("xcodeConfigFile", "/usr/local/lib/node_modules/appium/node_modules/appium-xcuitest-driver/WebDriverAgent/Configurations/ProjectSettings.xcconfig");
			//capabilities.setCapability("keychainPath","/Users/matrix/Library/Keychains/appiumKeychain.keychain");
			//capabilities.setCapability("keychainPassword","appium6754");
			capabilities.setCapability("autoDismissAlerts", false);
			capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);

			appiumFactory.setiOSDriver1(new URL(Constant.getServer1HttpAddress()), capabilities);
			System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");
		}
	}

	@BeforeGroups(groups="1driver_ios_install")
	public void setUp1IosDriverForInstallation() throws Exception{
		//Start appium server 1 if necessary.
		startAppiumServer1();
		Map<String, String> iosDevice1=ReadConfigFile.getInstance().getDevicesMap().get("iosdevice1");

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.UDID, iosDevice1.get(MobileCapabilityType.UDID));
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice1.get(MobileCapabilityType.DEVICE_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice1.get(MobileCapabilityType.PLATFORM_NAME));
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice1.get(MobileCapabilityType.PLATFORM_VERSION));
		capabilities.setCapability(MobileCapabilityType.APP, System.getProperty("user.dir")+Constant.PATH_TO_IOS_IPA);
		//capabilities.setCapability("bundleId", "im.vector.app");
		//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,iosDevice1.get(MobileCapabilityType.AUTOMATION_NAME));
		capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
		capabilities.setCapability("xcodeOrgId", ReadConfigFile.getInstance().getConfMap().get("development_team"));
		capabilities.setCapability("xcodeSigningId", ReadConfigFile.getInstance().getConfMap().get("code_sign_identity"));
		capabilities.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS, false);
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);
		//		capabilities.setCapability("autoWebview", true);

		AppiumFactory appiumFactory=AppiumFactory.getInstance();
		appiumFactory.setiOSDriver1(new URL(Constant.getServer1HttpAddress()), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");
	}

	@BeforeGroups(groups="2drivers_ios")
	public void setUp2IosDriver() throws Exception{
		//Start the two appium's servers if necessary.
		Boolean iosDriver1NeedToStart=false;
		start2AppiumServers();

		//Create ios driver 1 if necessary
		if(appiumFactory.getiOsDriver1()==null || appiumFactory.getiOsDriver1().getSessionId()==null){
			iosDriver1NeedToStart=true;
			Map<String, String> iosDevice1=ReadConfigFile.getInstance().getDevicesMap().get("iosdevice1");
			DesiredCapabilities capabilities1 = new DesiredCapabilities();
			capabilities1.setCapability(MobileCapabilityType.UDID, iosDevice1.get(MobileCapabilityType.UDID));
			capabilities1.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice1.get(MobileCapabilityType.DEVICE_NAME));
			capabilities1.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice1.get(MobileCapabilityType.PLATFORM_NAME));
			capabilities1.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice1.get(MobileCapabilityType.PLATFORM_VERSION));
			capabilities1.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "im.vector.app");
			//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
			//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
			capabilities1.setCapability(MobileCapabilityType.AUTOMATION_NAME,iosDevice1.get(MobileCapabilityType.AUTOMATION_NAME));
			capabilities1.setCapability(MobileCapabilityType.NO_RESET, true);
			capabilities1.setCapability(MobileCapabilityType.FULL_RESET, false);
			capabilities1.setCapability("xcodeOrgId", ReadConfigFile.getInstance().getConfMap().get("development_team"));
			capabilities1.setCapability("xcodeSigningId", ReadConfigFile.getInstance().getConfMap().get("code_sign_identity"));
			capabilities1.setCapability(IOSMobileCapabilityType.AUTO_DISMISS_ALERTS, false);
			capabilities1.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);

			appiumFactory.setiOSDriver1(new URL(Constant.getServer1HttpAddress()), capabilities1);
			System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities1.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 1.");
		}

		//Create ios driver 2 if necessary
		if(appiumFactory.getiOsDriver2()==null || appiumFactory.getiOsDriver2().getSessionId()==null){
			DesiredCapabilities capabilities2 = new DesiredCapabilities();
			Map<String, String> iosDevice2=ReadConfigFile.getInstance().getDevicesMap().get("iosdevice2");
			capabilities2.setCapability(MobileCapabilityType.UDID, iosDevice2.get(MobileCapabilityType.UDID));
			capabilities2.setCapability(MobileCapabilityType.DEVICE_NAME,iosDevice2.get(MobileCapabilityType.DEVICE_NAME));
			capabilities2.setCapability(MobileCapabilityType.PLATFORM_NAME,iosDevice2.get(MobileCapabilityType.PLATFORM_NAME));
			capabilities2.setCapability(MobileCapabilityType.PLATFORM_VERSION, iosDevice2.get(MobileCapabilityType.PLATFORM_VERSION));
			capabilities2.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "im.vector.app");//app
			//capabilities.setCapability(MobileCapabilityType.APP,"/Users/matrix/Documents/apps/ipa/Vector-d5ce6ff019a3e6b06a20bcc849ab57074e31e773-build1399.ipa");
			//XCUITest is used because Appium Ios driver doesn't support xcode version 8.0
			capabilities2.setCapability(MobileCapabilityType.AUTOMATION_NAME,iosDevice2.get(MobileCapabilityType.AUTOMATION_NAME));
			capabilities2.setCapability("realDeviceLogger", "/usr/local/lib/node_modules/deviceconsole/deviceconsole");
			capabilities2.setCapability(MobileCapabilityType.NO_RESET, true);
			//capabilities2.setCapability(MobileCapabilityType.FULL_RESET, false);
			capabilities2.setCapability("xcodeOrgId", ReadConfigFile.getInstance().getConfMap().get("development_team"));
			capabilities2.setCapability("xcodeSigningId", ReadConfigFile.getInstance().getConfMap().get("code_sign_identity"));
			capabilities2.setCapability("autoDismissAlerts", false);
			capabilities2.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);
			
			if(!iosDriver1NeedToStart){
				Thread.sleep(5000);
				int timeWaited=0;
				int maxToWait=6;
				while (!AppiumServerStartAndStopService.service2.isRunning()&&timeWaited<maxToWait) {
					timeWaited++;
					Thread.sleep(1000);
				}
				System.out.println(timeWaited);
			}
			appiumFactory.setiOSDriver2(new URL(Constant.getServer2HttpAddress()), capabilities2);
			System.out.println("Application "+Constant.APPLICATION_NAME+" started on IOS device "+capabilities2.getCapability(MobileCapabilityType.DEVICE_NAME) +" with DRIVER 2.");
		}		
	}

	//	@AfterGroups(groups="1driver_ios")
	//	public void tearDownIosDriver1() throws Exception{
	//		appiumFactory.getiOsDriver1().quit();
	//		System.out.println("Ios DRIVER 1 quitted on IOS device "+ReadConfigFile.getInstance().getDevicesMap().get("iosdevice1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
	//
	//		//Stop appium server1.
	//		stopAppiumServer1();
	//	}
	//
	//	@AfterGroups(groups={"2drivers_ios"}, alwaysRun=true)
	//	public void tearDown2IosDrivers() throws Exception{
	//		appiumFactory.getiOsDriver1().quit();
	//		System.out.println("Ios DRIVER 1 quitted on IOS device "+ReadConfigFile.getInstance().getDevicesMap().get("iosdevice1").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
	//		appiumFactory.getiOsDriver2().quit();
	//		System.out.println("Ios DRIVER 2 quitted on IOS device "+ReadConfigFile.getInstance().getDevicesMap().get("iosdevice2").get("deviceName") +", closing application "+Constant.APPLICATION_NAME+".");
	//
	//		//Stop the two appium's servers.
	//		stop2AppiumServers();
	//	}


	/**
	 * According to the chosen starting mode, start the first appium server if needed.
	 * @throws Exception 
	 */
	protected void startAppiumServer1() throws Exception{
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
	protected void start2AppiumServers() throws Exception{
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
	@SuppressWarnings("unused")
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
