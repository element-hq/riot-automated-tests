package mobilestests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.ScreenshotUtility;
import utility.testUtilities;

@Listeners({ ScreenshotUtility.class })
public class RiotVoipTests extends testUtilities{
	@BeforeSuite
	public void setUp() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.DEVICE1_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");
		
		DesiredCapabilities capabilities2 = new DesiredCapabilities();
		capabilities2.setCapability("deviceName",Constant.DEVICE2_NAME);
		capabilities2.setCapability("platformName","Android");
		capabilities2.setCapability("platformVersion", "4.3");
		capabilities2.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities2.setCapability("appActivity", "im.vector.activity.LoginActivity");

		//Create RemoteWebDriver instance and connect to the Appium server
		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver1(new URL(Constant.SERVER1_ADRESS), capabilities);
		System.out.println("setUp() device 1 done");
		
		appiumFactory.setDriver2(new URL(Constant.SERVER2_ADRESS), capabilities2);
		System.out.println("setUp() device 2 done");
		


	}
	
	@Test
	public void twoDevices() throws InterruptedException{
		RiotRoomsListPageObjects mainPageDevice1 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		RiotLoginAndRegisterPageObjects loginViewDevice2 = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver2());
		mainPageDevice1.getRoomByName("temp room").click();//clic on a room with device 1
		loginViewDevice2.fillLoginForm("riotuser3","riotuser"); //login on device 2
		RiotRoomPageObjects roomDevice1=new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		roomDevice1.menuBackButton.click();//come back in rooms list with device 1
		RiotRoomsListPageObjects mainPageDevice2 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver2());
		mainPageDevice2.logOut();//logout with device2
	}

	@AfterClass
	public void tearDown(){
		//AppiumFactory.getAppiumDriver().quit();
	}
}
