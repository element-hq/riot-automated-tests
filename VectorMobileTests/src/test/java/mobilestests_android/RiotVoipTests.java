package mobilestests_android;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import pom_android.RiotCallingPageObject;
import pom_android.RiotIncomingCallPageObjects;
import pom_android.RiotLoginAndRegisterPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotVoipTests extends RiotParentTest{
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
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		voipRoom.startCallButton.click();
		voipRoom.voiceCallFromMenuButton.click();
		RiotCallingPageObject callingView= new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
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
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		voipRoom.startCallButton.click();
		voipRoom.videoCallFromMenuButton.click();
		RiotCallingPageObject callingView= new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
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
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		voipRoom.startCallButton.click();
		voipRoom.voiceCallFromMenuButton.click();
		Thread.sleep(2000);
		AppiumFactory.getAndroidDriver1().pressKeyCode(AndroidKeyCode.HOME);
		AppiumFactory.getAndroidDriver1().startActivity(Constant.PACKAGE_APP_NAME, "im.vector.activity.LoginActivity");
		//AppiumFactory.getAndroidDriver1().runAppInBackground(5);
		//		AppiumFactory.getAndroidDriver1().closeApp();
		//		AppiumFactory.getAndroidDriver1().launchApp();
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
	@Test(groups="2drivers", description="call from device 1 answered by device 2")
	public void cancelIncommingAudioCall() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";

		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAndroidDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAndroidDriver2(), calledUser, pwd);
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingView= new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
		callingView.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAndroidDriver2());
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
	@Test(groups="2drivers", description="call from device 1 answered by device 2")
	public void acceptIncommingAudioCall() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";

		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAndroidDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAndroidDriver2(), calledUser, pwd);
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver2());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAndroidDriver2());
		incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		//check that call layout is diplayed on device 2
		RiotCallingPageObject callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAndroidDriver2());
		callingViewDevice2.isDisplayed(true);
		//TODO check the calling layout
		//hangout from device 2
		callingViewDevice2.hangUpButton.click();
		//check that both calling view are closed
		callingViewDevice1.isDisplayed(false);
		callingViewDevice2.isDisplayed(false);
		//check end call events on messages
		Assert.assertEquals(voipRoomDevice1.getTextViewFromPost(voipRoomDevice1.getLastPost()).getText(),calledUser+" ended the call.");
		Assert.assertEquals(riotListDevice2.getReceivedMessageByRoomName(roomNameTest),calledUser+" ended the call.");
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
	@Test(groups="2drivers", description="call from device 1 answered by device 2")
	public void acceptIncommingVideoCall() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";

		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAndroidDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAndroidDriver2(), calledUser, pwd);
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver2());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.videoCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAndroidDriver2());
		incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		callingViewDevice1.waitUntilCallTook();
		//check that call layout is diplayed on device 2
		RiotCallingPageObject callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAndroidDriver2());
		callingViewDevice2.isDisplayed(true);
		//TODO check the calling layout
		//hangout from device 2
		callingViewDevice2.mainCallLayout.click();//display the controls if they had fade out.
		callingViewDevice2.hangUpButton.click();
		//check that both calling view are closed
		callingViewDevice1.isDisplayed(false);
		callingViewDevice2.isDisplayed(false);
		//check end call events on messages
		Assert.assertEquals(voipRoomDevice1.getTextViewFromPost(voipRoomDevice1.getLastPost()).getText(),calledUser+" ended the call.");
		Assert.assertEquals(riotListDevice2.getReceivedMessageByRoomName(roomNameTest),calledUser+" ended the call.");
		//come back in rooms list on device 1
		voipRoomDevice1.menuBackButton.click();
	}

	/**
	 * Cover this issue https://github.com/vector-im/vector-android/issues/684 </br>
	 * Required : both devices have an user logged.</br>
	 * Launch an audio call in a 1to1 room. </br>
	 * On the called device, hit on mute button on the call layout. </br>
	 * Come back on the room layout, then on the call layout again.</br>
	 * Check that mute button is still active.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(groups="2drivers", description="during a call desactivate mic")
	public void disableMicrophoneDuringCall() throws InterruptedException, IOException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";

		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAndroidDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAndroidDriver2(), calledUser, pwd);
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAndroidDriver2());
		incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		callingViewDevice1.waitUntilCallTook();
		//check that call layout is diplayed on device 2
		RiotCallingPageObject callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAndroidDriver2());
		callingViewDevice2.isDisplayed(true);
		//get image before button touched
		captureImage("screenshots\\comparison\\muteAudioButtonNonPressed.png", callingViewDevice2.muteAudioButton);
		//hit on mute button
		callingViewDevice2.muteAudioButton.click();
		//verify that mute audio is touched
		captureImage("screenshots\\comparison\\muteAudioButtonPressed.png", callingViewDevice2.muteAudioButton);
		Assert.assertFalse(compareImages("screenshots\\comparison\\muteAudioButtonNonPressed.png", "screenshots\\comparison\\muteAudioButtonPressed.png"), "Mute button not pressed");
		//going back on room page then calling layout again
		callingViewDevice2.chatLinkButton.click();
		RiotRoomPageObjects voipRoomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver2());
		voipRoomDevice2.roomPendingCallLayout.click();
		//verify that mute audio is still touched
		captureImage("screenshots\\comparison\\muteAudioButtonPressed.png", callingViewDevice2.muteAudioButton);
		Assert.assertFalse(compareImages("screenshots\\comparison\\muteAudioButtonNonPressed.png", "screenshots\\comparison\\muteAudioButtonPressed.png"), "Mute button not pressed");
		//hangout
		callingViewDevice2.hangUpButton.click();
		voipRoomDevice1.menuBackButton.click();
		voipRoomDevice2.menuBackButton.click();
		
	}

	/**
	 * Cover this issue https://github.com/vector-im/vector-android/issues/685 </br>
	 * Required : both devices have an user logged.</br>
	 * Launch an audio call with device 1 in a 1to1 room. </br>
	 * Accept the call with device 2</br>
	 * During the call, go on the room page and leave room with device 2.</br>
	 * Check that call is ended on both devices.</br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers","invitationneeded"}, description="leave room during a call")
	public void leaveRoomDuringCall() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";
		
		//TODO maybe use a different option than checkIfUserLogged.
		checkIfUserLogged(AppiumFactory.getAndroidDriver1(), callingUser, pwd);
		checkIfUserLogged(AppiumFactory.getAndroidDriver2(), calledUser, pwd);
		//go on 1to1 room with device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		//go on 1to1 room with device 2
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver2());
		riotListDevice2.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver2());
		
		//call from device 1
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
		//callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(AppiumFactory.getAndroidDriver2());
		//incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		callingViewDevice1.waitUntilCallTook();
		//check that call layout is diplayed on device 2
		RiotCallingPageObject callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAndroidDriver2());
		//callingViewDevice2.isDisplayed(true);
		//go back in room page and leave the room
		callingViewDevice2.chatLinkButton.click();
		voipRoomDevice2.moreOptionsButton.click();
		voipRoomDevice2.leaveRoomMenuItem.click();
		//check that call is ended on device 2
		Assert.assertFalse(callingViewDevice2.isDisplayed(false), "Call isn't ended on device 2 after leaving the room.");
		//check that call is ended on device 1
		Assert.assertFalse(callingViewDevice1.isDisplayed(false), "Call isn't ended on device 1 after leaving the room.");
	}

	/**
	 * Cover this issue https://github.com/vector-im/riot-android/issues/784 </br>
	 * Required : both devices have an user logged.</br>
	 * 1. Launch a voice call in 1:1 room.
	 * 2. Callee accepts call
	 * 3. Callee goes in the room from call layout by hitting the chatlink button
	 * 4. Caller hang out the call
	 * Check that call is really hung up.
	 * @throws InterruptedException 
	 * 
	 */
	@Test(groups={"2drivers"})
	public void hangUpWhenCalleeInRoomView() throws InterruptedException{
		String callingUser="riotuser2";
		String calledUser="riotuser3";
		String pwd="riotuser";
		String roomNameTest="voip room test";
		
		//TODO maybe use a different option than checkIfUserLogged.
//		checkIfUserLogged(AppiumFactory.getAndroidDriver1(), callingUser, pwd);
//		checkIfUserLogged(AppiumFactory.getAndroidDriver2(), calledUser, pwd);
		
		//Go in room voip test with both devices
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListDevice2 = new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver2());
		roomsListDevice1.getRoomByName(roomNameTest).click();
		roomsListDevice2.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects roomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver2());
		//1. Launch a voice call in 1:1 room.
		RiotRoomPageObjects roomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		roomDevice1.startVoiceCall();
		RiotCallingPageObject callingViewDevice1 = new RiotCallingPageObject(AppiumFactory.getAndroidDriver1());
		//2. Callee accepts call
		RiotIncomingCallPageObjects incomingCallDevice2 = new RiotIncomingCallPageObjects(AppiumFactory.getAndroidDriver2());
		incomingCallDevice2.acceptCallButton.click();
		RiotCallingPageObject callingViewDevice2 = new RiotCallingPageObject(AppiumFactory.getAndroidDriver2());
		//3. Callee goes in the room from call layout by hitting the chatlink button
		callingViewDevice2.chatLinkButton.click();
		//4. Caller hang out the call
		callingViewDevice1.hangUpButton.click();
		//check that call is really ended.
		waitUntilDisplayed(AppiumFactory.getAndroidDriver2(), "im.vector.alpha:id/room_start_call_layout", true, 5);
		Assert.assertTrue(roomDevice2.startCallButton.isDisplayed(), "Start call button on the callee device isn't displayed after the end of the call");
		//check that callee can receive a new call
		roomDevice1.startVoiceCall();
		incomingCallDevice2.checkIncomingCallView(true, callingUser, "Incoming Call");
		incomingCallDevice2.ignoreCallButton.click();
		//go back to the room
		roomDevice1.menuBackButton.click();
		roomDevice2.menuBackButton.click();
	}
	
	/**
	 * Invite riotuser3 by riotuser2 on room voiptest.
	 * Accept invitation with riotuser2.
	 * @throws IOException 
	 */
	@AfterGroups("invitationneeded")
	private void inviteUser2ToRoom() throws IOException{
		String accessTokenDevice2="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI4Y2lkIHVzZXJfaWQgPSBAcmlvdHVzZXIzOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMWRjaWQgdGltZSA8IDE0Nzg3NzkwMjM2ODcKMDAyZnNpZ25hdHVyZSC8Xnf2FrXx45wz9isY2objlIVMQ0BR0mwDwP0ucxBb6wo";
		String roomId="!AcqGzXlGcDgpUAUTvS:matrix.org";
		HttpsRequestsToMatrix.joinRoom(accessTokenDevice2, roomId);
	}

	/**
	 * Log the good user if not.</br> Secure the test.
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
				listRoom.signOutButton.click();
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
