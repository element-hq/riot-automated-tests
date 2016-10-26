package mobilestests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.android.Connection;
import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomsListPageObjects;
import pom.RiotRoomPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.ScreenshotUtility;
import utility.testUtilities;

@Listeners({ ScreenshotUtility.class })
public class RiotMessagesReceptionTests extends testUtilities{
	@BeforeSuite
	public void setUp() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.DEVICE_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");

		//Create RemoteWebDriver instance and connect to the Appium server
		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver(new URL(Constant.SERVER_ADRESS), capabilities);
		System.out.println("setUp() done");
	}

	@AfterClass
	public void tearDown(){
		AppiumFactory.getAppiumDriver().quit();
	}
	
	
	/**
	 * Required : user must be logged in room roomName </br>
	 * Receive a message in a room from an other user. </br>
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test
	public void checkBadgeAndMessageOnRoomItem() throws InterruptedException, IOException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String roomName="room tests Jean";
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0NzU1OTQxNjYwNTAKMDAyZnNpZ25hdHVyZSDofV-Ok8f6xSEPDNnKuZ9tM8YO_TXiwoKcfuvQrDLilwo";
		String messageTest="coucou";

		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//get the current badge on the room.
		Integer currentBadge=riotRoomsList.getBadgeNumberByRoomName(roomName);
		//send a message to the room with an other user using https request to matrix.
		HttpsRequestsToMatrix.sendMessageInRoom(senderAccesToken, roomId, messageTest);
		if(currentBadge==null)currentBadge=0;
		//wait until message is received
		riotRoomsList.waitForRoomToReceiveNewMessage(roomName, currentBadge);
		//Assertion on the badge
		Assert.assertEquals((int) riotRoomsList.getBadgeNumberByRoomName(roomName),currentBadge+1, "Badge number wasn't incremented after receiving the message");	
		//Assertion on the message.
		Assert.assertEquals(riotRoomsList.getReceivedMessageByRoomName(roomName), messageTest, "Received message on the room item isn't the same as sended by matrix.");
	}

	
	/**
	 * Log-in the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod
	public void loginForSetup() throws InterruptedException{
		if(false==waitUntilDisplayed("im.vector.alpha:id/fragment_recents_list", true, 5)){
			System.out.println("Can't access to the rooms list page, none user must be logged. Forcing the log-in.");
			forceWifiOnIfNeeded();
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
			loginPage.emailOrUserNameEditText.sendKeys(Constant.DEFAULT_USERNAME);
			loginPage.passwordEditText.sendKeys(Constant.DEFAULT_USERPWD);
			//Forcing the login button to be enabled : this bug should be corrected.
			if(loginPage.loginButton.isEnabled()==false){
				loginPage.registerButton.click();
				loginPage.loginButton.click();
			}
			loginPage.loginButton.click();
		}
	}
}
