package mobilestests_android;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_android.RiotCallingPageObjects;
import pom_android.RiotIncomingCallPageObjects;
import pom_android.RiotRoomDetailsPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotVoipTests extends RiotParentTest{
	private String roomNameTest="1:1_voip_automated_tests";
	private String roomNameTest2="1:1 room auto tests 2";
	private String riotuser1DisplayName="riotuser12";
	private String riotuser2DisplayName="riotuser13";
	
	/**
	 * 1. Launch an audio call from a room </br>
	 * 2. From the call layout, hit the room link button </br>
	 * Check the pending call view on the room view. </br>
	 * 3. Switch between call view and room view </br>
	 *	4. End the call from the room view. </br> 
	 * Check that the call is ended.</br>
	 * Check that events are logged in the messages.
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_android","1checkuser"}, description="test on call")
	public void cancelAudioCallFromChatRoom() throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		
		//1. Launch an audio call from a room
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		voipRoom.startCallButton.click();
		voipRoom.voiceCallFromMenuButton.click();
		RiotCallingPageObjects callingView= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		
		//2. From the call layout, hit the room link button
		callingView.chatLinkButton.click();
		//Check the pending call view on the room view.
		voipRoom.checkPendingCallView(true, "Calling…");
		//assert on messages
		Assert.assertTrue(voipRoom.getTextViewFromPost(voipRoom.getLastPost()).getText().contains("placed a voice call."),"No '[user] placed a voice call' message in the room.");
		
		//3. Switch between call view and room view
		voipRoom.roomPendingCallLayout.click();
		Assert.assertTrue(callingView.isDisplayed(true), "Calling view isn't displayed after touching pending call view from room view");
		callingView.chatLinkButton.click();
		
		//4. End the call from the room view. 
		voipRoom.endCallButton.click();
		//asserts on pending view
		voipRoom.checkPendingCallView(false, null);
		Assert.assertTrue(voipRoom.getTextViewFromPost(voipRoom.getLastPost()).getText().contains("ended the call."),"No '[user] ended the call' message in the room.");
		//come back in rooms list
		voipRoom.menuBackButton.click();
	}

	/**
	 * 1. Launch an video call from a room </br>
	 * 2. From the call layout, hit the room link button </br>
	 * Check the pending call view on the room view. </br>
	 * 3. Switch between call view and room view </br>
	 * 4. End the call from the room view. </br> 
	 * Check that the call is ended.</br>
	 * Check that events are logged in the messages.
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_android","1checkuser",}, description="test on call")
	public void cancelVideoCallFromChatRoom() throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		
		RiotRoomsListPageObjects mainListRoom=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		mainListRoom.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		voipRoom.startCallButton.click();
		voipRoom.videoCallFromMenuButton.click();
		RiotCallingPageObjects callingView= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		callingView.chatLinkButton.click();
		//asserts on pending view
		voipRoom.checkPendingCallView(true, "Calling…");
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

	/**
	 * Required : both devices have an user logged.
	 * Open a room, launch a voice call.</br>
	 * With device 2, assert that the incomming call view is displayed and correc.</br>
	 * Ignore the call from device 2, assert that the incomming call view is closed. </br>
	 * From device 1, check that the calling view is closed.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkuser"}, description="call from device 1 answered by device 2")
	public void cancelIncomingAudioCall() throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		restartApplication(appiumFactory.getAndroidDriver2());
		
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingView= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		callingView.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver2());
		incomingCallDevice2.checkIncomingCallView(true, riotuser1DisplayName, "Incoming Call");
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
	 * From device 2, check that the calling view is opened.</br>
	 * Hang up call from device 2</br>Check that both calling view are closed.</br>
	 * Check that "[user] ended the call" event is displayed on room view and room list view.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkuser"}, description="call from device 1 answered by device 2")
	public void acceptIncomingAudioCall() throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		restartApplication(appiumFactory.getAndroidDriver2());
		
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingViewDevice1= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver2());
		incomingCallDevice2.checkIncomingCallView(true, riotuser1DisplayName, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		//check that call layout is diplayed on device 2
		RiotCallingPageObjects callingViewDevice2= new RiotCallingPageObjects(appiumFactory.getAndroidDriver2());
		callingViewDevice2.isDisplayed(true);
		//TODO check the calling layout
		//hangout from device 2
		callingViewDevice2.hangUpButton.click();
		//check that both calling view are closed
		Assert.assertFalse(callingViewDevice1.isDisplayed(false),"Calling view is still displayed on device 1 after call is ended");
		Assert.assertFalse(callingViewDevice2.isDisplayed(false),"Calling view is still displayed on device 2 after call is ended");
		//check end call events on messages
		Assert.assertEquals(voipRoomDevice1.getTextViewFromPost(voipRoomDevice1.getLastPost()).getText(),riotuser2DisplayName+" ended the call.");
		Assert.assertEquals(riotListDevice2.getLastEventByRoomName(roomNameTest,false),riotuser2DisplayName+" ended the call.");
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
	@Test(groups={"2drivers_android","2checkuser"}, description="call from device 1 answered by device 2")
	public void acceptIncomingVideoCall() throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		restartApplication(appiumFactory.getAndroidDriver2());
		
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.videoCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingViewDevice1= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver2());
		incomingCallDevice2.checkIncomingCallView(true, riotuser1DisplayName, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		callingViewDevice1.waitUntilCallTook();
		//check that call layout is diplayed on device 2
		RiotCallingPageObjects callingViewDevice2= new RiotCallingPageObjects(appiumFactory.getAndroidDriver2());
		callingViewDevice2.isDisplayed(true);
		//TODO check the calling layout
		//hangout from device 2
		//callingViewDevice2.mainCallLayout.click();//display the controls if they had fade out.
		Thread.sleep(5000);
		appiumFactory.getAndroidDriver2().tap(1, 200, 200, 50);
		callingViewDevice2.hangUpButton.click();
		//check that both calling view are closed
		Assert.assertFalse(callingViewDevice1.isDisplayed(false),"Calling view is still displayed on device 2 after call is ended");
		Assert.assertFalse(callingViewDevice2.isDisplayed(false),"Calling view is still displayed on device 2 after call is ended");
		//check end call events on messages
		Assert.assertEquals(voipRoomDevice1.getTextViewFromPost(voipRoomDevice1.getLastPost()).getText(),riotuser2DisplayName+" ended the call.");
		Assert.assertEquals(riotListDevice2.getLastEventByRoomName(roomNameTest,false),riotuser2DisplayName+" ended the call.");
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
	@Test(groups={"2drivers_android","2checkuser"}, description="during a call desactivate mic")
	public void disableMicrophoneDuringCall() throws InterruptedException, IOException{
		restartApplication(appiumFactory.getAndroidDriver1());
		restartApplication(appiumFactory.getAndroidDriver2());
		
		//call from device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingViewDevice1= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver2());
		incomingCallDevice2.checkIncomingCallView(true, riotuser1DisplayName, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		callingViewDevice1.waitUntilCallTook();
		//check that call layout is diplayed on device 2
		RiotCallingPageObjects callingViewDevice2= new RiotCallingPageObjects(appiumFactory.getAndroidDriver2());
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
		RiotRoomPageObjects voipRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
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
	 * Launch an audio call with device 1 in a 1to1 room. </br>
	 * Accept the call with device 2</br>
	 * During the call, go on the room page and leave room with device 2.</br>
	 * Check that call is ended on both devices.</br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkuser"},priority=15, description="leave room during a call")
	public void leaveRoomDuringCall() throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		restartApplication(appiumFactory.getAndroidDriver2());
		
		//go on 1to1 room with device 1
		RiotRoomsListPageObjects riotListDevice1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		riotListDevice1.getRoomByName(roomNameTest2).click();
		RiotRoomPageObjects voipRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects riotListDevice2=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		
		//invite user2
		voipRoomDevice1.moreOptionsButton.click();
		voipRoomDevice1.roomDetailsMenuItem.click();
		RiotRoomDetailsPageObjects newRoomDetailsDevice1 = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());
		newRoomDetailsDevice1.addParticipant(getMatrixIdFromDisplayName(riotuser2DisplayName));
		ExplicitWait(appiumFactory.getAndroidDriver1(), newRoomDetailsDevice1.menuBackButton);
		newRoomDetailsDevice1.menuBackButton.click();
		//accept invitation with device 2
		riotListDevice2.previewInvitation(roomNameTest2);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.joinRoomButton.click();
		newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		
		//call from device 1
		voipRoomDevice1.startCallButton.click();
		voipRoomDevice1.voiceCallFromMenuButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingViewDevice1= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		//callingViewDevice1.isDisplayed(true);
		//check call from device 2
		RiotIncomingCallPageObjects incomingCallDevice2= new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver2());
		//incomingCallDevice2.checkIncomingCallView(true, riotuser1DisplayName, "Incoming Call");
		incomingCallDevice2.acceptCallButton.click();
		callingViewDevice1.waitUntilCallTook();
		//check that call layout is diplayed on device 2
		RiotCallingPageObjects callingViewDevice2= new RiotCallingPageObjects(appiumFactory.getAndroidDriver2());
		//callingViewDevice2.isDisplayed(true);
		//go back in room page and leave the room
		callingViewDevice2.chatLinkButton.click();
		newRoomDevice2.leaveRoom();
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
	@Test(groups={"2drivers_android","2checkuser"})
	public void hangUpWhenCalleeInRoomView() throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		restartApplication(appiumFactory.getAndroidDriver2());
		
		//Go in room voip test with both devices
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListDevice2 = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		roomsListDevice1.getRoomByName(roomNameTest).click();
		roomsListDevice2.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects roomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		//1. Launch a voice call in 1:1 room.
		RiotRoomPageObjects roomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomDevice1.startVoiceCall();
		RiotCallingPageObjects callingViewDevice1 = new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		//2. Callee accepts call
		RiotIncomingCallPageObjects incomingCallDevice2 = new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver2());
		incomingCallDevice2.acceptCallButton.click();
		RiotCallingPageObjects callingViewDevice2 = new RiotCallingPageObjects(appiumFactory.getAndroidDriver2());
		//3. Callee goes in the room from call layout by hitting the chatlink button
		callingViewDevice2.chatLinkButton.click();
		//4. Caller hang out the call
		callingViewDevice1.hangUpButton.click();
		//check that call is really ended.
		waitUntilDisplayed(appiumFactory.getAndroidDriver2(), "im.vector.alpha:id/room_start_call_layout", true, 5);
		Assert.assertTrue(roomDevice2.startCallButton.isDisplayed(), "Start call button on the callee device isn't displayed after the end of the call");
		//check that callee can receive a new call
		roomDevice1.startVoiceCall();
		incomingCallDevice2.checkIncomingCallView(true, riotuser1DisplayName, "Incoming Call");
		incomingCallDevice2.ignoreCallButton.click();
		//go back to the room
		roomDevice1.menuBackButton.click();
		roomDevice2.menuBackButton.click();
	}

	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 * @throws YamlException 
	 * @throws FileNotFoundException 
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUser1Logged() throws InterruptedException, FileNotFoundException, YamlException{
		checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotuser1DisplayName, Constant.DEFAULT_USERPWD);
	}
	
	@BeforeGroups("2checkuser")
	private void checkIfUser2Logged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotuser1DisplayName, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver2(), riotuser2DisplayName, Constant.DEFAULT_USERPWD);
	}
}
