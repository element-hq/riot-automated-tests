package mobilestests;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import pom.RiotCallingPageObject;
import pom.RiotIncomingCallPageObjects;
import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotVoipTests extends RiotParentTest{
	@Test(groups="2drivers", description="Plays with 2 devices",enabled=false)
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
	
	/**
	 * Launch an audio call from a room </br>
	 * From the call layout, hit the room link button </br>
	 * Check the pending call view on the room view. </br>
	 * Switch between call view and room view </br>
	 * End the call from the room view. </br> 
	 * Check that the call is ended.</br>
	 * Check that events are logged in the messages.
	 * @throws InterruptedException
	 */
	@Test(groups="1driver", description="test on call")
	public void cancelAudioCallFromChatRoom() throws InterruptedException{
		String roomNameTest="voip room test";
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		voipRoom.startCallButton.click();
		voipRoom.voiceCallFromMenuButton.click();
		RiotCallingPageObject callingView= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingView.chatLinkButton.click();
		//asserts on pending view
		voipRoom.checkPendingCallView(true, "Calling...");
		//assert on messages
		Assert.assertTrue(voipRoom.getTextViewFromPost(voipRoom.getLastPost()).getText().contains("placed a voice call."),"No '[user] placed a voice call' message in the room.");
		//come back in call view by touching pending call view
		voipRoom.roomPendingCallLayout.click();
		Assert.assertTrue(callingView.isDisplayed(true), "Calling view isn't displayed after touching pending call view from room view");
		callingView.chatLinkButton.click();
		voipRoom.endCallButton.click();
		//asserts on pending view
		voipRoom.checkPendingCallView(false, null);
		Assert.assertTrue(voipRoom.getTextViewFromPost(voipRoom.getLastPost()).getText().contains("ended the call."),"No '[user] ended the call' message in the room.");
		//come back in rooms list
		voipRoom.menuBackButton.click();
	}
	
	/**
	 * Launch an video call from a room </br>
	 * From the call layout, hit the room link button </br>
	 * Check the pending call view on the room view. </br>
	 * Switch between call view and room view </br>
	 * End the call from the room view. </br> 
	 * Check that the call is ended.</br>
	 * Check that events are logged in the messages.
	 * @throws InterruptedException
	 */
	@Test(groups="1driver", description="test on call")
	public void cancelVideoCallFromChatRoom() throws InterruptedException{
		String roomNameTest="voip room test";
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		voipRoom.startCallButton.click();
		voipRoom.videoCallFromMenuButton.click();
		RiotCallingPageObject callingView= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingView.chatLinkButton.click();
		//asserts on pending view
		voipRoom.checkPendingCallView(true, "Calling...");
		//assert on messages
		Assert.assertTrue(voipRoom.getTextViewFromPost(voipRoom.getLastPost()).getText().contains("placed a video call."),"No '[user] placed a voice call' message in the room.");
		//come back in call view by touching pending call view
		voipRoom.roomPendingCallLayout.click();
		Assert.assertTrue(callingView.isDisplayed(true), "Calling view isn't displayed after touching pending call view from room view");
		callingView.chatLinkButton.click();
		voipRoom.endCallButton.click();
		//asserts on pending view
		voipRoom.checkPendingCallView(false, null);
		Assert.assertTrue(voipRoom.getTextViewFromPost(voipRoom.getLastPost()).getText().contains("ended the call."),"No '[user] ended the call' message in the room.");
		//come back in rooms list
		voipRoom.menuBackButton.click();
	}
	
	@Test(groups="1driver", description="restart Riot during call", enabled=false)
	public void restartAppDuringCall() throws InterruptedException{
		String roomNameTest="voip room test";
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		voipRoom.startCallButton.click();
		voipRoom.voiceCallFromMenuButton.click();
		Thread.sleep(2000);
		AppiumFactory.getAppiumDriver1().pressKeyCode(AndroidKeyCode.HOME);
		AppiumFactory.getAppiumDriver1().startActivity(Constant.PACKAGE_APP_NAME, "im.vector.activity.LoginActivity");
		//AppiumFactory.getAppiumDriver1().runAppInBackground(5);
//		AppiumFactory.getAppiumDriver1().closeApp();
//		AppiumFactory.getAppiumDriver1().launchApp();
		Thread.sleep(10000);
	}
	
	/**
	 * Required : both devices have an user logged.
	 * Open a room, launch a voice call.</br>
	 * With device 2, assert that the incomming call view is displayed and correc.</br>
	 * Ignore the call from device 2, assert that the incomming call view is closed. </br>
	 * From device 1, check that the calling view is closed.
	 * @throws InterruptedException 
	 */
	@Test(groups="2drivers", description="call form device 1 answered by device 2")
	public void cancelIncommingAudioCall() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";
		
		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAppiumDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAppiumDriver2(), calledUser, pwd);
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingView= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingView.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAppiumDriver2());
		incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.ignoreCallButton.click();
		//check that incoming call layout is closed on device 2
		incomingCallDevice2.checkIncomingCallView(false, "", "");
		//check that calling view is closed on device 1
		callingView.isDisplayed(false);
		voipRoomDevice1.menuBackButton.click();
	}

	/**
	 * Required : both devices have an user logged.
	 * Open a room, launch a voice call.</br>
	 * With device 2, assert that the incomming call view is displayed and correc.</br>
	 * Accept the call from device 2, assert that the incomming call view is opened. </br>
	 * From device 1, check that the calling view is opened.</br>
	 * Hang up call from device 2</br>Check that both calling view are closed.</br>
	 * Check that "[user] ended the call" event is displayed on room view and room list view.
	 * @throws InterruptedException 
	 */
	@Test(groups="2drivers", description="call form device 1 answered by device 2")
	public void acceptIncommingAudioCall() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";
		
		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAppiumDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAppiumDriver2(), calledUser, pwd);
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver2());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAppiumDriver2());
		incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		//check that call layout is diplayed on device 2
		RiotCallingPageObject callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingViewDevice2.isDisplayed(true);
		//TODO check the calling layout
		//hangout from device 2
		callingViewDevice2.hangUpButton.click();
		//check that both calling view are closed
		callingViewDevice1.isDisplayed(false);
		callingViewDevice2.isDisplayed(false);
		//check end call events on messages
		Assert.assertEquals(voipRoomDevice1.getTextViewFromPost(voipRoomDevice1.getLastPost()).getText(),callingUser+" ended the call.");
		Assert.assertEquals(riotListDevice2.getReceivedMessageByRoomName(roomNameTest),callingUser+" ended the call.");
		//come back in rooms list on device 1
		voipRoomDevice1.menuBackButton.click();
	}
	/**
	 * Required : both devices have an user logged.
	 * Open a room, launch a video call.</br>
	 * With device 2, assert that the incomming call view is displayed and correc.</br>
	 * Accept the call from device 2, assert that the incomming call view is opened. </br>
	 * From device 1, check that the calling view is opened.</br>
	 * Hang up call from device 2</br>Check that both calling view are closed.</br>
	 * Check that "[user] ended the call" event is displayed on room view and room list view.
	 * @throws InterruptedException 
	 */
	@Test(groups="2drivers", description="call form device 1 answered by device 2")
	public void acceptIncommingVideoCall() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";
		
		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAppiumDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAppiumDriver2(), calledUser, pwd);
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver2());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.videoCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAppiumDriver2());
		incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		callingViewDevice1.waitUntilCallTook();
		//check that call layout is diplayed on device 2
		RiotCallingPageObject callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingViewDevice2.isDisplayed(true);
		//TODO check the calling layout
		//hangout from device 2
		callingViewDevice2.mainCallLayout.click();//display the controls if they had fade out.
		callingViewDevice2.hangUpButton.click();
		//check that both calling view are closed
		callingViewDevice1.isDisplayed(false);
		callingViewDevice2.isDisplayed(false);
		//check end call events on messages
		Assert.assertEquals(voipRoomDevice1.getTextViewFromPost(voipRoomDevice1.getLastPost()).getText(),callingUser+" ended the call.");
		Assert.assertEquals(riotListDevice2.getReceivedMessageByRoomName(roomNameTest),callingUser+" ended the call.");
		//come back in rooms list on device 1
		voipRoomDevice1.menuBackButton.click();
	}
	/**
	 * Log the good user if not.</br> Securised the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException
	 */
	private void checkIfUserLogged(AndroidDriver<MobileElement> myDriver, String username, String pwd) throws InterruptedException {
		//if login page is displayed, then logged with the wanted user
		System.out.println("Check if user "+username+" is logged in "+Constant.APPLICATION_NAME);
		if(waitUntilDisplayed(myDriver, "im.vector.alpha:id/main_input_layout", false, 5)){
			System.out.println("User "+username+" isn't logged, login forced.");
			RiotLoginAndRegisterPageObjects loginView = new RiotLoginAndRegisterPageObjects(myDriver);
			loginView.fillLoginForm(username, pwd);
		}else{
			//check if the wanted user is loged in
			RiotRoomsListPageObjects listRoom = new RiotRoomsListPageObjects(myDriver);
			listRoom.contextMenuButton.click();
			String actualLoggedUser=listRoom.displayedUserMain.getText();
			if(!actualLoggedUser.equals(username)){
				System.out.println("User "+username+" isn't logged. An other user is logged ("+actualLoggedUser+"), login with "+username+".");
				listRoom.logOutButton.click();
				RiotLoginAndRegisterPageObjects loginView = new RiotLoginAndRegisterPageObjects(myDriver);
				loginView.fillLoginForm(username, pwd);
			}else{
				//close lateral menu
				System.out.println("User "+username+" is logged.");
			myDriver.navigate().back();
			}
		}
	}

}
