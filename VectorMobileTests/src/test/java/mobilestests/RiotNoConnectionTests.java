package mobilestests;

import java.net.MalformedURLException;
import java.net.URL;

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
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.ScreenshotUtility;
import utility.testUtilities;

@Listeners({ ScreenshotUtility.class })
public class RiotNoConnectionTests extends testUtilities{
	

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
	 * Test that rooms are still accessible when internet connection is off. </br>
	 * Verifies the connection lost notification in a room. </br>
	 * Bring back the internet connection and verifies that the notification isn't displayed anymore.
	 * @throws InterruptedException 
	 */
	@Test(groups="nointernet")
	public void roomsListWithoutConnectionTest() throws InterruptedException{
		String roomNameTest="temp room";
		String expectedNotificationMessage="Connectivity to the server has been lost.";
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//Verifies that messages are still here : there is more than one.
		Assert.assertFalse(riotRoomsList.roomsList.isEmpty(), "None room displayed in the rooms list.");
		//Open the first room.
		riotRoomsList.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		//Verifies that the warning message is displayed in the bottom of the messages.
		Assert.assertTrue(isPresentTryAndCatch(myRoom.notificationMessage),"The notification message is not displayed");
		org.openqa.selenium.Dimension riotLogoDim=myRoom.notificationIcon.getSize();
	    Assert.assertTrue(riotLogoDim.height!=0 && riotLogoDim.width!=0, "notification icon has null dimension");
	    Assert.assertEquals(myRoom.notificationMessage.getText(), expectedNotificationMessage);
	    //Bring back WIFI
	    System.out.println("Set up the internet connection to WIFI.");
	    AppiumFactory.getAppiumDriver().setConnection(Connection.WIFI);
	    //Verifies that the warning message is not displayed anymore in the bottom of the messages.
	    Assert.assertFalse(isPresentTryAndCatch(myRoom.notificationMessage),"The notification message is displayed");
	    //teardown : going back to rooms list
	    myRoom.menuBackButton.click();
	}
	
	/**
	 * Send a message in a room without Internet connection. </br>
	 * Tests that the message is still added to the messages list. </br>
	 * Restart the application </br>
	 * Bring back internet, verifies that Riot proposes to send the unsent message.
	 * @throws InterruptedException 
	 */
	@Test
	public void sendMessageWithoutConnectionTest() throws InterruptedException{
		String myMessage="message sent without internet connection";
		String expectedNotificationMessage="Messages not sent. Resend now?";
		String roomNameTest="temp room";
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//Open the first room.
		riotRoomsList.getRoomByName(roomNameTest).click();
		//cut internet
		forceWifiOfIfNeeded();
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		//send a message, then verifies that it's displayed in the message list
		myRoom.sendAMessage(myMessage);
		Assert.assertEquals(myRoom.getTextViewFromMessage(myRoom.lastMessage).getText(), myMessage,"The usent message isn't in the last message.");
		//Restart the application
		AppiumFactory.getAppiumDriver().closeApp();
		AppiumFactory.getAppiumDriver().launchApp();
		//Reopen the room
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		riotRoomsList.getRoomByName(roomNameTest).click();
		//bring back internet
		AppiumFactory.getAppiumDriver().setConnection(Connection.WIFI);
		//Asserts that riot proposes to send the unsent message
		Assert.assertTrue(isPresentTryAndCatch(myRoom.roomNotificationArea),"The notification area is not displayed");
		org.openqa.selenium.Dimension riotLogoDim=myRoom.notificationIcon.getSize();
	    Assert.assertTrue(riotLogoDim.height!=0 && riotLogoDim.width!=0, "notification icon has null dimension");
	    Assert.assertEquals(myRoom.notificationMessage.getText(), expectedNotificationMessage);
	    //Click on "Resend now"
	    myRoom.notificationMessage.click();
	    //Asserts that the room notification area isn't dispkayed anymore
	    Assert.assertFalse(isPresentTryAndCatch(myRoom.roomNotificationArea),"The notification area is displayed");
	    //The previously unsent message is still in the message list
	    Assert.assertEquals(myRoom.getTextViewFromMessage(myRoom.lastMessage).getText(), myMessage, "The unsent message doesn't isn't in the last message.");
	    //teardown : going back to rooms list
	    myRoom.menuBackButton.click();
	}
	
	/**
	 * Take a photo and send it in a room without Internet connection. </br>
	 * Tests that the photo is still added to the messages list. </br>
	 * Restart the application </br>
	 * Bring back internet, verifies that Riot proposes to send the unsent photo.
	 * @throws InterruptedException 
	 */
	@Test
	public void sendPhotoWithoutConnectionTest() throws InterruptedException{
		String expectedNotificationMessage="Messages not sent. Resend now?";
		String roomNameTest="temp room";
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//Open the first room.
		riotRoomsList.getRoomByName(roomNameTest).click();
		//cut internet
		forceWifiOfIfNeeded();
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		//send a picture from the camera
		myRoom.attachPhotoFromCamera("Small");
		//verifies that it's displayed in the message list
		waitUntilDisplayed("im.vector.alpha:id/messagesAdapter_image", true, 5);
		org.openqa.selenium.Dimension takenPhoto=myRoom.getImageFromMessage(myRoom.lastMessage).getSize();
	    Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
    
		//Restart the application
		AppiumFactory.getAppiumDriver().closeApp();
		AppiumFactory.getAppiumDriver().launchApp();
		//Reopen the room
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		riotRoomsList.getRoomByName(roomNameTest).click();
		//bring back internet
		AppiumFactory.getAppiumDriver().setConnection(Connection.WIFI);
		//verifies that the upload failed icon is present in the last message
	    Assert.assertTrue(isPresentTryAndCatch(myRoom.getMediaUploadFailIconFromMessage(myRoom.lastMessage)),"The 'media upload failed' icon isn't displayed on the unsent photo");
		//Asserts that riot proposes to send the unsent message
		Assert.assertTrue(isPresentTryAndCatch(myRoom.roomNotificationArea),"The notification area is not displayed");
		org.openqa.selenium.Dimension riotLogoDim=myRoom.notificationIcon.getSize();
	    Assert.assertTrue(riotLogoDim.height!=0 && riotLogoDim.width!=0, "notification icon has null dimension");
	    Assert.assertEquals(myRoom.notificationMessage.getText(), expectedNotificationMessage);
	    //Click on "Resend now"
	    myRoom.notificationMessage.click();
	    //Asserts that the room notification area isn't dispkayed anymore
	    Assert.assertFalse(isPresentTryAndCatch(myRoom.roomNotificationArea),"The notification area is displayed");
	    //The previously unsent message is still in the message list
	    takenPhoto=myRoom.getImageFromMessage(myRoom.lastMessage).getSize();
	    Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
		//verifies that the upload failed icon is present in the last message
	    Assert.assertFalse(isPresentTryAndCatch(myRoom.getMediaUploadFailIconFromMessage(myRoom.lastMessage)),"The 'media upload failed' icon is displayed on the sent photo");
	    //teardown : going back to rooms list
	    myRoom.menuBackButton.click();
	}
	@BeforeGroups(groups="nointernet")
	public void setWifiOffForNoConnectionTests(){
		if(!AppiumFactory.getAppiumDriver().getConnection().equals(Connection.NONE)){
			System.out.println("Setting up the connection to NONE for the tests without internet connection.");
			AppiumFactory.getAppiumDriver().setConnection(Connection.NONE);
		}
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
