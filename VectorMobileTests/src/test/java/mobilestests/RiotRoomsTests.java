package mobilestests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom.RiotLoginAndRegisterPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.ScreenshotUtility;
import utility.testUtilities;

@Listeners({ ScreenshotUtility.class })
public class RiotRoomsTests extends testUtilities{
	@BeforeClass
	public void setUp() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName","a71011c8");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.package_name);
		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");

		//Create RemoteWebDriver instance and connect to the Appium server
		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver(new URL(Constant.Server_Adress), capabilities);
		System.out.println("setUp() done");
	}

	@AfterClass
	public void tearDown(){
		AppiumFactory.getAppiumDriver().quit();
	}
	
	/**
	 * Test that rooms are still accessible when internet connection is off. </br>
	 * Verifies the connection lost warning in a room.
	 */
	@Test
	public void roomsListWithoutConnection(){
		
	}
	
	/**
	 * Log-in the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod
	public void loginForSetup() throws InterruptedException{
		if(false==waitUntilDisplayed("im.vector.alpha:id/fragment_recents_list", true, 5)){
			System.out.println("Can't access to the rooms list page, none user must be logged. Forcing the log-in.");
			forceWifiIsNeeded();
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
			loginPage.emailOrUserNameEditText.sendKeys(Constant.defaultUserName);
			loginPage.passwordEditText.sendKeys(Constant.defaultUserPwd);
			//Forcing the login button to be enabled : this bug should be corrected.
			if(loginPage.loginButton.isEnabled()==false){
				loginPage.registerButton.click();
				loginPage.loginButton.click();
			}
			loginPage.loginButton.click();
		}
	}
}
