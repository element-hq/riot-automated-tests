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

import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.ScreenshotUtility;
import utility.testUtilities;

@Listeners({ ScreenshotUtility.class })
public class RiotRoomInvitationTests extends testUtilities{
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
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Check that the invitation is closed when accepted.</br>
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void rejectInvitationToARoom() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg2MTAyNjEKMDAyZnNpZ25hdHVyZSAMRHy3V2nt7jDJlDrhq1NkEBBiHH6umGQvaydgqLcYlQo";
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		String leavingUserAccessToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI4Y2lkIHVzZXJfaWQgPSBAcmlvdHVzZXIyOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg3NDI5OTgKMDAyZnNpZ25hdHVyZSBapU0beWNgBCwjIb0CT16LUNT0F2jr0pm6qPAm7t0CEAo";
		
		HttpsRequestsToMatrix.leaveRoom(leavingUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(senderAccesToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		ExplicitWait(riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//reject invitation
		riotRoomsList.rejectInvitation(roomName);
		//check that the invite bar is closed
		Assert.assertFalse(waitUntilDisplayed("//android.widget.TextView[@resource-id='im.vector.alpha:id/heading' and @text='INVITES']/../..", false, 5),"The INVITES bar isn't closed after rejecting the invitation");
	}
	
	/**
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Click preview to preview the room.</br>
	 * Check the preview layout.</br>
	 * Cancel the invitation.</br>
	 * Check that the invitation is closed.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void cancelInvitationToARoom() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg2MTAyNjEKMDAyZnNpZ25hdHVyZSAMRHy3V2nt7jDJlDrhq1NkEBBiHH6umGQvaydgqLcYlQo";
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		String leavingUserAccessToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI4Y2lkIHVzZXJfaWQgPSBAcmlvdHVzZXIyOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg3NDI5OTgKMDAyZnNpZ25hdHVyZSBapU0beWNgBCwjIb0CT16LUNT0F2jr0pm6qPAm7t0CEAo";
		
		HttpsRequestsToMatrix.leaveRoom(leavingUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(senderAccesToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		ExplicitWait(riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//preview invitation
		riotRoomsList.previewInvitation(roomName);
		//check the preview layout
		RiotRoomPageObjects newRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		newRoom.checkPreviewRoomLayout(roomName);
		//cancel invitation
		newRoom.cancelInvitationButton.click();
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//check that the invite bar is closed
		Assert.assertFalse(waitUntilDisplayed("//android.widget.TextView[@resource-id='im.vector.alpha:id/heading' and @text='INVITES']/../..", false, 5),"The INVITES bar isn't closed after rejecting the invitation");
	}

	/**
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Click preview to preview the room.</br>
	 * Check the preview layout.</br>
	 * Join the room</br>
	 * Check that the room is opened. </br>
	 * Came back in the list and leave room.</br>
	 * Check that room page is closed and not present in the rooms list.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void acceptInvitationToARoom() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg2MTAyNjEKMDAyZnNpZ25hdHVyZSAMRHy3V2nt7jDJlDrhq1NkEBBiHH6umGQvaydgqLcYlQo";
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		String leavingUserAccessToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI4Y2lkIHVzZXJfaWQgPSBAcmlvdHVzZXIyOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg3NDI5OTgKMDAyZnNpZ25hdHVyZSBapU0beWNgBCwjIb0CT16LUNT0F2jr0pm6qPAm7t0CEAo";
		
		HttpsRequestsToMatrix.leaveRoom(leavingUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(senderAccesToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		ExplicitWait(riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//preview invitation
		riotRoomsList.previewInvitation(roomName);
		//check the preview layout
		RiotRoomPageObjects newRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		newRoom.checkPreviewRoomLayout(roomName);
		newRoom.joinRoomButton.click();
		newRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		newRoom.checkRoomLayout(roomName);
		//come back in roomslist
		newRoom.menuBackButton.click();
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//leave the room
		riotRoomsList.clickOnContextMenuOnRoom(roomName, "Leave Conversation");
		//check that room is closed and isn't in the rooms list page
		newRoom.isDisplayed(false);
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		Assert.assertNull(riotRoomsList.getRoomByName(roomName), "Room "+roomName+" is still in the rooms list after leaving it.");
	}
	
	/**
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Click preview to preview the room.</br>
	 * Check the preview layout.</br>
	 * Join the room</br>
	 * Check that the room is opened. </br>
	 * Came back in the list and leave room from the room menu.</br>
	 * Check that room page is closed and not present in the rooms list.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test
	public void acceptInvitationAndLeaveFromMenu() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg2MTAyNjEKMDAyZnNpZ25hdHVyZSAMRHy3V2nt7jDJlDrhq1NkEBBiHH6umGQvaydgqLcYlQo";
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		String leavingUserAccessToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI4Y2lkIHVzZXJfaWQgPSBAcmlvdHVzZXIyOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzc2NTg3NDI5OTgKMDAyZnNpZ25hdHVyZSBapU0beWNgBCwjIb0CT16LUNT0F2jr0pm6qPAm7t0CEAo";
		
		HttpsRequestsToMatrix.leaveRoom(leavingUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(senderAccesToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		ExplicitWait(riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//preview invitation
		riotRoomsList.previewInvitation(roomName);
		//check the preview layout
		RiotRoomPageObjects newRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		newRoom.checkPreviewRoomLayout(roomName);
		newRoom.joinRoomButton.click();
		newRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver());
		newRoom.checkRoomLayout(roomName);
		//leave room from room menu
		newRoom.moreOptionsButton.click();
		newRoom.leaveRoomMenuItem.click();
		//check that room is closed and isn't in the rooms list page
		newRoom.isDisplayed(false);
		riotRoomsList = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		Assert.assertNull(riotRoomsList.getRoomByName(roomName), "Room "+roomName+" is still in the rooms list after leaving it.");
	}
	
	/**
	 * Stress test on invitations.
	 * Receive multiple (10) invitations from an other user. </br>
	 * TODO write this test
	 */
	@Ignore
	public void receiveMultipleInvitations(){
		
	}
	/**
	 * TODO write this test
	 */
	@Ignore
	public void sendInvitationOnRestrictedRoom(){
		
	}
	
	/**
	 * TODO write this test
	 */
	@Ignore
	public void leaveRoom(){
		
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
