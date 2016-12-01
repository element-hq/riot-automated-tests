package mobilestests;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.android.Connection;
import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotNoConnectionTests extends RiotParentTest{
	
	/**
	 * Cut the wifi, launches Riot and asserts that the login button is disabled. </br>
	 * Bring back the wifi and verifies that the login button become enabled.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver"})
	public void logInWithoutInternetConnection() throws InterruptedException{
		logoutForSetup();
		AppiumFactory.getAppiumDriver1().setConnection(Connection.NONE);
		System.out.println("wifi off");
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver1());
		Assert.assertFalse(loginPage.loginButton.isEnabled(), "The loginButton should be disabled.");
		AppiumFactory.getAppiumDriver1().setConnection(Connection.WIFI);
		System.out.println("wifi on");
		Assert.assertTrue(loginPage.loginButton.isEnabled(), "The loginButton should be enabled.");
	}
	
	/**
	 * Test that rooms are still accessible when internet connection is off. </br>
	 * Verifies the connection lost notification in a room. </br>
	 * Bring back the internet connection and verifies that the notification isn't displayed anymore.
	 * @throws InterruptedException 
	 */
	@Test(groups={"nointernet","1driver"})
	public void roomsListWithoutConnectionTest() throws InterruptedException{
		String roomNameTest="temp room";
		String expectedNotificationMessage="Connectivity to the server has been lost.";
		
		loginForSetup();
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		//Verifies that messages are still here : there is more than one.
		Assert.assertFalse(riotRoomsList.roomsList.isEmpty(), "None room displayed in the rooms list.");
		//Open the first room.
		riotRoomsList.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		//Verifies that the warning message is displayed in the bottom of the messages.
		Assert.assertTrue(isPresentTryAndCatch(myRoom.notificationMessage),"The notification message is not displayed");
		org.openqa.selenium.Dimension riotLogoDim=myRoom.notificationIcon.getSize();
	    Assert.assertTrue(riotLogoDim.height!=0 && riotLogoDim.width!=0, "notification icon has null dimension");
	    Assert.assertEquals(myRoom.notificationMessage.getText(), expectedNotificationMessage);
	    //Bring back WIFI
	    System.out.println("Set up the internet connection to WIFI.");
	    AppiumFactory.getAppiumDriver1().setConnection(Connection.WIFI);
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
	@Test(groups={"1driver"})
	public void sendMessageWithoutConnectionTest() throws InterruptedException{
		String myMessage="message sent without internet connection";
		String expectedNotificationMessage="Messages not sent. Resend now?";
		String roomNameTest="temp room";
		
		loginForSetup();
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		//Open the first room.
		riotRoomsList.getRoomByName(roomNameTest).click();
		//cut internet
		forceWifiOfIfNeeded(AppiumFactory.getAppiumDriver1());
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		//send a message, then verifies that it's displayed in the message list
		myRoom.sendAMessage(myMessage);
		Assert.assertEquals(myRoom.getTextViewFromPost(myRoom.getLastPost()).getText(), myMessage,"The usent message isn't in the last message.");
		//Restart the application
		AppiumFactory.getAppiumDriver1().closeApp();
		AppiumFactory.getAppiumDriver1().launchApp();
		//Reopen the room
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		riotRoomsList.getRoomByName(roomNameTest).click();
		//bring back internet
		AppiumFactory.getAppiumDriver1().setConnection(Connection.WIFI);
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
	    Assert.assertEquals(myRoom.getTextViewFromPost(myRoom.getLastPost()).getText(), myMessage, "The unsent message doesn't isn't in the last message.");
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
	@Test(groups={"1driver"})
	public void sendPhotoWithoutConnectionTest() throws InterruptedException{
		String expectedNotificationMessage="Messages not sent. Resend now?";
		String roomNameTest="temp room";
		
		loginForSetup();
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		//Open the first room.
		riotRoomsList.getRoomByName(roomNameTest).click();
		//cut internet
		forceWifiOfIfNeeded(AppiumFactory.getAppiumDriver1());
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		//send a picture from the camera
		myRoom.attachPhotoFromCamera("Small");
		//verifies that it's displayed in the message list
		waitUntilDisplayed(AppiumFactory.getAppiumDriver1(),"im.vector.alpha:id/messagesAdapter_image", true, 5);
		org.openqa.selenium.Dimension takenPhoto=myRoom.getAttachedImageByPost(myRoom.getLastPost()).getSize();
	    Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
    
		//Restart the application
		AppiumFactory.getAppiumDriver1().closeApp();
		AppiumFactory.getAppiumDriver1().launchApp();
		//Reopen the room
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		riotRoomsList.getRoomByName(roomNameTest).click();
		//bring back internet
		AppiumFactory.getAppiumDriver1().setConnection(Connection.WIFI);
		//verifies that the upload failed icon is present in the last message
	    Assert.assertTrue(isPresentTryAndCatch(myRoom.getMediaUploadFailIconFromPost(myRoom.getLastPost())),"The 'media upload failed' icon isn't displayed on the unsent photo");
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
	    takenPhoto=myRoom.getAttachedImageByPost(myRoom.getLastPost()).getSize();
	    Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
		//verifies that the upload failed icon is present in the last message
	    Assert.assertFalse(isPresentTryAndCatch(myRoom.getMediaUploadFailIconFromPost(myRoom.getLastPost())),"The 'media upload failed' icon is displayed on the sent photo");
	    //teardown : going back to rooms list
	    myRoom.menuBackButton.click();
	}
	
	@BeforeGroups(groups="nointernet")
	private void setWifiOffForNoConnectionTests(){
		if(!AppiumFactory.getAppiumDriver1().getConnection().equals(Connection.NONE)){
			System.out.println("Setting up the connection to NONE for the tests without internet connection.");
			AppiumFactory.getAppiumDriver1().setConnection(Connection.NONE);
		}
	}
	
	/**
	 * Log-in the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	private void loginForSetup() throws InterruptedException{
		if(false==waitUntilDisplayed(AppiumFactory.getAppiumDriver1(),"im.vector.alpha:id/fragment_recents_list", true, 5)){
			System.out.println("Can't access to the rooms list page, none user must be logged. Forcing the log-in.");
			forceWifiOnIfNeeded(AppiumFactory.getAppiumDriver1());
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver1());
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
	
	private void logoutForSetup() throws InterruptedException{
		if (!waitUntilDisplayed(AppiumFactory.getAppiumDriver1(), "im.vector.alpha:id/login_inputs_layout", true, 5)){
			RiotRoomsListPageObjects mainPage= new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
			mainPage.logOut();
		}
	}
}
