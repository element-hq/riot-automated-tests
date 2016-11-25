package utility;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;

public class RiotParentTest extends TestUtilities{
	@BeforeGroups(alwaysRun=false,groups="1driver")
	public void setUp1Driver() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.DEVICE1_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
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
		
		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability("deviceName",Constant.DEVICE2_NAME);
		capabilities2.setCapability("platformName","Android");
		capabilities2.setCapability("platformVersion", "4.3");
		capabilities2.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities2.setCapability("appActivity", Constant.APPLICATION_LOGIN_ACTIVITY);

		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+Constant.DEVICE1_NAME +" with DRIVER 1.");
		
		appiumFactory.setDriver2(new URL(Constant.SERVER2_ADRESS), capabilities2);
		System.out.println("Application "+Constant.APPLICATION_NAME+" started on ANDROID device "+Constant.DEVICE2_NAME +" with DRIVER 2.");
	}
	
	@AfterGroups(alwaysRun=false,groups="1driver")
	public void tearDown1Driver(){
		AppiumFactory.getAppiumDriver1().quit();
		System.out.println("DRIVER 1 quitted, closing application "+Constant.APPLICATION_NAME+".");
	}
	
	@AfterGroups(groups="2drivers")
	public void tearDown2Drivers(){
		AppiumFactory.getAppiumDriver1().quit();
		System.out.println("Android DRIVER 1 quitted on ANDROID device "+Constant.DEVICE1_NAME +", closing application "+Constant.APPLICATION_NAME+".");
		AppiumFactory.getAppiumDriver2().quit();
		System.out.println("Android DRIVER 2 quitted on ANDROID device "+Constant.DEVICE2_NAME +", closing application "+Constant.APPLICATION_NAME+".");
	}
}
