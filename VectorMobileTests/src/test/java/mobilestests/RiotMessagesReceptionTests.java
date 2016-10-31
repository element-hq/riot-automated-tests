package mobilestests;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Ignore;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
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
	 * Required : user must be logged in room </br>
	 * Receive a message in a room from an other user. </br>
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.</br>
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(groups="messageReceivedInList",priority=1)
	public void checkBadgeAndMessageOnRoomItem() throws InterruptedException, IOException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String roomName="room tests Jean";
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg2MTAyNjEKMDAyZnNpZ25hdHVyZSAMRHy3V2nt7jDJlDrhq1NkEBBiHH6umGQvaydgqLcYlQo";
		String messageTest="coucou";
		//TODO invite user in the room if room not present
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
	 * Required : user must be logged in room </br>
	 * Receive a text message in a room from an other user. </br>
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.</br>
	 * Asserts that badge isn't displayed anymore on the room item when going back to rooms list.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(dependsOnGroups="messageReceivedInList",priority=2,groups="roomOpenned")
	public void checkTextMessageOnRoomPage() throws InterruptedException{
		String roomName="room tests Jean";
		String messageTest="coucou";
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//open room
		riotRoomsList.getRoomByName(roomName).click();
		//check that lately sended message is the last displayed in the room
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		MobileElement lastPost= testRoom.getLastPost();
		Assert.assertEquals(testRoom.getTextViewFromPost(lastPost).getText(), messageTest);
	}
	
	/**
	 * Send message in a room.</br>
	 * Check that timestamp is only displayed on the last post.</br>
	 * Check that when a post is selected, timestamp is displayed.
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",priority=3)
	public void checkTimeStampPositionOnRoomPage() throws InterruptedException{
		String message="test for timestamp display";
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		//send message
		testRoom.sendAMessage(message);Thread.sleep(500);
		//AppiumFactory.getAppiumDriver().hideKeyboard();
		//check that timestamp is displayed on this message
		MobileElement lastPost= testRoom.getLastPost();
		Assert.assertNotNull(testRoom.getTimeStampByPost(lastPost), "Last message have no timestamp");
		Assert.assertTrue(testRoom.getTimeStampByPost(lastPost).getText().length()>=5, "Last message timestamp seems bad.");
		//check that before last message have not timestamp
		int beforeLastPostPosition=testRoom.postsListLayout.size()-2;
		MobileElement beforeLastPost=  testRoom.postsListLayout.get(beforeLastPostPosition);
		Assert.assertNull(testRoom.getTimeStampByPost(beforeLastPost), "Before last message have timestamp and should not.");
		//select before last message
		testRoom.getTextViewFromPost(beforeLastPost).click();
		//check that the timestamp is displayed
		//System.out.println(testRoom.getTextViewFromPost(beforeLastPost).getText());
		Assert.assertNotNull(testRoom.getTimeStampByPost(beforeLastPost), "Before last message have no timestamp");
		Assert.assertTrue(testRoom.getTimeStampByPost(beforeLastPost).getText().length()>=5, "Before last message timestamp seems bad.");
	}
	
	/**
	 * Receive a message by an other user in a room.</br>
	 * Send a first message. Check that avatar is displayed on the post.</br>
	 * Send a second message. Check that avatar is not displayed on the post.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",priority=4)
	public void checkAvatarDisplayInRoomPage() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg2MTAyNjEKMDAyZnNpZ25hdHVyZSAMRHy3V2nt7jDJlDrhq1NkEBBiHH6umGQvaydgqLcYlQo";
		String messageTest="hello from sender";
		String messageTest2="this message have an avatar";
		String messageTest3="this message doesn't have an avatar";
		//send a message to the room with an other user using https request to matrix.
		HttpsRequestsToMatrix.sendMessageInRoom(senderAccesToken, roomId, messageTest);
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		testRoom.sendAMessage(messageTest2);Thread.sleep(500);
		Assert.assertNotNull(testRoom.getUserAvatarByPost(testRoom.getLastPost()), "The last post doesn't have an avatar and should because it's the first post from the user");
		testRoom.sendAMessage(messageTest3);
		Assert.assertNull(testRoom.getUserAvatarByPost(testRoom.getLastPost()), "The last post have an avatar and shouldn't because it's the second post from the user");
	}
	
	/**
	 * Open a room, and receive a image sent by an other user.</br>
	 * Check that image is correctly uploaded.</br>
	 * Check that a timestamp is visible on the last post.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",priority=5)
	public void checkImageMessageOnRoomPage() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String pictureURL="mxc://matrix.org/gpQYPbjoqVeTWCGivjRshIni";
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg2MTAyNjEKMDAyZnNpZ25hdHVyZSAMRHy3V2nt7jDJlDrhq1NkEBBiHH6umGQvaydgqLcYlQo";
		//send picture of already uploaded picture
		HttpsRequestsToMatrix.sendPicture(senderAccesToken, roomId, pictureURL);
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		Thread.sleep(500);
		MobileElement lastPost=testRoom.getLastPost();
		MobileElement uploadPicture = testRoom.getAttachedImageByPost(lastPost);
		//get dimensions of the uploaded image
		org.openqa.selenium.Dimension riotLogoDim=uploadPicture.getSize();
	    Assert.assertTrue(riotLogoDim.height!=0 && riotLogoDim.width!=0, "Uploaded picture seems empty");
	    Assert.assertNotNull(testRoom.getTimeStampByPost(lastPost), "Last post doesn't have a timestamp");
	}

	
	/**
	 * Log-in the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod
	public void loginForSetup() throws InterruptedException{
		if(true==waitUntilDisplayed("im.vector.alpha:id/login_inputs_layout", false, 5)){
			System.out.println("Can't access to the rooms list page, none user must be logged. Forcing the log-in.");
			forceWifiOnIfNeeded();
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
			loginPage.emailOrUserNameEditText.setValue(Constant.DEFAULT_USERNAME);
			loginPage.passwordEditText.setValue(Constant.DEFAULT_USERPWD);
			//Forcing the login button to be enabled : this bug should be corrected.
			if(loginPage.loginButton.isEnabled()==false){
				loginPage.registerButton.click();
				loginPage.loginButton.click();
			}
			loginPage.loginButton.click();
		}
	}
	
	
}
