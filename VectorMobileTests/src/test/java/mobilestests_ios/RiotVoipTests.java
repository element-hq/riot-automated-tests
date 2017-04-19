package mobilestests_ios;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.MobileElement;
import pom_ios.RiotCallingPageObjects;
import pom_ios.RiotIncomingCallPageObjects;
import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotVoipTests extends RiotParentTest{
	private String roomNameTest="1:1_voip_automated_tests";
	private String conferenceRoomTest="voip_conference_automated_tests";
	private String unwantedRoomTest="VoIP Conference";
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
	@Test(groups={"1driver_ios","1checkuser"}, description="test on call", priority=1)
	public void cancelAudioCallFromChatRoom() throws InterruptedException{
		restartApplication(appiumFactory.getiOsDriver1());
		
		//1. Launch an audio call from a room
		RiotRoomsListPageObjects roomsList= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		roomsList.getRoomByName(roomNameTest).click();;
		RiotRoomPageObjects room1=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		room1.startVoiceCall();
		RiotCallingPageObjects callingView1=new RiotCallingPageObjects(appiumFactory.getiOsDriver1());

		//2. From the call layout, hit the room link button
		callingView1.chatLinkButton.click();
		//Check the pending call view on the room view.
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.checkPendingCallView(true, "Active Call ("+riotuser2DisplayName+")");
		//assert on messages
		Assert.assertTrue(room1.getTextViewFromBubble(room1.getLastBubble()).getText().contains("placed a voice call"),"No '[user] placed a voice call' message in the room.");

		//3. Switch between call view and room view
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.getPendingCallView("Active Call ("+riotuser2DisplayName+")").click();
		Assert.assertTrue(callingView1.isDisplayed(true), "Calling view isn't displayed after touching pending call view from room view");
		callingView1.chatLinkButton.click();

		//4. End the call from the room view. 
		room1.hangUpCallButton.click();
		room1.checkPendingCallView(false, "Active Call ("+riotuser2DisplayName+")");
		//assert on messages
		Assert.assertTrue(room1.getTextViewFromBubble(room1.getLastBubble()).getText().contains("ended the call"),"No '[user] ended the call' message in the room.");
		//come back in rooms list
		room1.menuBackButton.click();
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
	@Test(groups={"1driver_ios","1checkuser"}, description="test on call",priority=2)
	public void cancelVideoCallFromChatRoom() throws InterruptedException{
		restartApplication(appiumFactory.getiOsDriver1());
		
		//1. Launch an video call from a room
		RiotRoomsListPageObjects roomsList= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		roomsList.getRoomByName(roomNameTest).click();;
		RiotRoomPageObjects room1=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		room1.startVideoCall();
		RiotCallingPageObjects callingView1=new RiotCallingPageObjects(appiumFactory.getiOsDriver1());

		//2. From the call layout, hit the room link button
		callingView1.chatLinkButton.click();
		//Check the pending call view on the room view.
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.checkPendingCallView(true, "Active Call ("+riotuser2DisplayName+")");
		//assert on messages
		Assert.assertTrue(room1.getTextViewFromBubble(room1.getLastBubble()).getText().contains("placed a video call"),"No '[user] placed a video call' message in the room.");

		//3. Switch between call view and room view
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.getPendingCallView("Active Call ("+riotuser2DisplayName+")").click();
		Assert.assertTrue(callingView1.isDisplayed(true), "Calling view isn't displayed after touching pending call view from room view");
		callingView1.chatLinkButton.click();

		//4. End the call from the room view. 
		room1.hangUpCallButton.click();
		room1.checkPendingCallView(false, "Active Call ("+riotuser2DisplayName+")");
		//assert on messages
		Assert.assertTrue(room1.getTextViewFromBubble(room1.getLastBubble()).getText().contains("ended the call"),"No '[user] ended the call' message in the room.");
		//come back in rooms list
		room1.menuBackButton.click();
	}
	
	/**
	 * Required : both devices have an user logged.
	 * 1. Open a room, launch a voice call.</br>
	 * With device 2, assert that the incomming call view is displayed and correc.</br>
	 * 2. Ignore the call from device 2, assert that the incomming call view is closed. </br>
	 * From device 1, check that the calling view is closed.</br>
	 * From device 2, check that last message is a 'ended call' event on the rooms list page</br>
	 * From device 1, check that last message is a 'ended call' event on the room page</br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_ios","2checkuser"}, description="call from device 1 answered by device 2",priority=3)
	public void cancelIncomingAudioCall() throws InterruptedException{
		restartApplication(appiumFactory.getiOsDriver1());
		restartApplication(appiumFactory.getiOsDriver2());
		
		//1. Open a room, launch a voice call.
		RiotRoomsListPageObjects roomsList1= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomsListPageObjects roomsList2= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver2());
		roomsList1.getRoomByName(roomNameTest).click();;
		RiotRoomPageObjects room1=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		room1.startVoiceCall();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingView1=new RiotCallingPageObjects(appiumFactory.getiOsDriver1());
		//With device 2, assert that the incomming call view is displayed and correc.
		RiotIncomingCallPageObjects incomingCall2 = new RiotIncomingCallPageObjects(appiumFactory.getiOsDriver2());
		incomingCall2.checkIncomingCallView(true, riotuser1DisplayName,"voice");
		//2. Ignore the call from device 2, assert that the incomming call view is closed
		incomingCall2.declineCallButton.click();
		//check that incoming call layout is closed on device 2
		incomingCall2.checkIncomingCallView(false, riotuser1DisplayName,"voice");
		//From device 1, check that the calling view is closed.
		callingView1.isDisplayed(false);
		//From device 2, check that last message is a 'ended call' event on the rooms list page
		Assert.assertEquals(roomsList2.getLastEventByRoomName(roomNameTest,false), riotuser2DisplayName+" ended the call");
		//From device 1, check that last message is a 'ended call' event on the room page
		Assert.assertTrue(room1.getTextViewFromBubble(room1.getLastBubble()).getText().contains(riotuser2DisplayName+" ended the call"));
		room1.menuBackButton.click();
	}

	/**
	 * Required : both devices have an user logged.
	 * 1. Open a room </br>
	 * 2. Launch a voice call.</br>
	 * With device 2, assert that the incomming call view is displayed and correct.</br>
	 * 3. Accept the call from device 2, assert that the incomming call view is opened. </br>
	 * From device 2, check that the calling view is opened.</br>
	 * 4. Hang up call from device 2</br>Check that both calling view are closed.</br>
	 * Check that "[user] ended the call" event is displayed on room view and room list view.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_ios","2checkuser"}, description="call from device 1 answered by device 2",priority=4)
	public void acceptIncomingAudioCall() throws InterruptedException{
		restartApplication(appiumFactory.getiOsDriver1());
		restartApplication(appiumFactory.getiOsDriver2());
		
		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomsListPageObjects roomsList2 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver2());
		//1. Open a room
		roomsList1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		
		//2. Launch a voice call.
		roomPage1.startVoiceCall();
		RiotCallingPageObjects callingPage1=new RiotCallingPageObjects(appiumFactory.getiOsDriver1());
		Assert.assertTrue(callingPage1.isDisplayed(true),"Calling view is not displayed on device 1");
		//With device 2, assert that the incomming call view is displayed and correct
		RiotIncomingCallPageObjects incomingPage2=new RiotIncomingCallPageObjects(appiumFactory.getiOsDriver2());
		incomingPage2.checkIncomingCallView(true,riotuser1DisplayName, "voice");
		
		//3. Accept the call from device 2, assert that the incomming call view is opened.
		incomingPage2.acceptCallButton.click();
		//From device 2, check that the calling view is opened.
		RiotCallingPageObjects callingPage2=new RiotCallingPageObjects(appiumFactory.getiOsDriver2());
		Assert.assertTrue(callingPage2.isDisplayed(true),"Calling view is not displayed on device 2");
		
		//4. Hang up call from device 2
		callingPage2.hangUpButton.click();
		//Check that both calling view are closed
		Assert.assertFalse(callingPage1.isDisplayed(false),"Calling view is still displayed on device 1 after call is ended");
		Assert.assertFalse(callingPage2.isDisplayed(false),"Calling view is still displayed on device 2 after call is ended");
		//Check that "[user] ended the call" event is displayed on room view and room list view.
		Assert.assertEquals(roomsList2.getLastEventByRoomName(roomNameTest,false), riotuser2DisplayName+" ended the call");
		//From device 1, check that last message is a 'ended call' event on the room page
		Assert.assertTrue(roomPage1.getTextViewFromBubble(roomPage1.getLastBubble()).getText().contains(riotuser2DisplayName+" ended the call"));
		roomPage1.menuBackButton.click();
	}
	
	/**
	 * Required : both devices have an user logged. </br>
	 * 1. Open a room </br>
	 * 2. Launch a video call.</br>
	 * With device 2, assert that the incomming call view is displayed and correct.</br>
	 * 3. Accept the call from device 2, assert that the incomming call view is opened. </br>
	 * From device 2, check that the calling view is opened.</br>
	 * 4. Hang up call from device 1</br>Check that both calling view are closed.</br>
	 * Check that "[user] ended the call" event is displayed on room view and room list view.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_ios","2checkuser"}, description="video call from device 1 answered by device 2",priority=5)
	public void acceptIncomingVideoCall() throws InterruptedException{
		restartApplication(appiumFactory.getiOsDriver1());
		restartApplication(appiumFactory.getiOsDriver2());
		
		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomsListPageObjects roomsList2 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver2());
		//1. Open a room
		roomsList1.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		
		//2. Launch a voice call.
		roomPage1.startVideoCall();
		RiotCallingPageObjects callingPage1=new RiotCallingPageObjects(appiumFactory.getiOsDriver1());
		Assert.assertTrue(callingPage1.isDisplayed(true),"Calling view is not displayed on device 1");
		//With device 2, assert that the incomming call view is displayed and correct
		RiotIncomingCallPageObjects incomingPage2=new RiotIncomingCallPageObjects(appiumFactory.getiOsDriver2());
		incomingPage2.checkIncomingCallView(true,riotuser1DisplayName, "video");
		
		//3. Accept the call from device 2, assert that the incomming call view is opened.
		incomingPage2.acceptCallButton.click();
		//From device 2, check that the calling view is opened.
		RiotCallingPageObjects callingPage2=new RiotCallingPageObjects(appiumFactory.getiOsDriver2());
		Assert.assertTrue(callingPage2.isDisplayed(true),"Calling view is not displayed on device 2");
		
		//4. Hang up call from device 1
		callingPage1.hangUpWithDoubleTap();
		
		//Check that both calling view are closed
		Assert.assertFalse(callingPage1.isDisplayed(false),"Calling view is still displayed on device 1 after call is ended");
		Assert.assertFalse(callingPage2.isDisplayed(false),"Calling view is still displayed on device 2 after call is ended");
		//Check that "[user] ended the call" event is displayed on room view and room list view.
		Assert.assertEquals(roomsList2.getLastEventByRoomName(roomNameTest,false), riotuser1DisplayName+" ended the call");
		//From device 1, check that last message is a 'ended call' event on the room page
		Assert.assertTrue(roomPage1.getTextViewFromBubble(roomPage1.getLastBubble()).getText().contains(riotuser1DisplayName+" ended the call"));
		roomPage1.menuBackButton.click();
	}
	
	/**
	 * Cover this issue https://github.com/vector-im/riot-ios/issues/953.</br>
	 * 0. Prerequisite: check that there is no VoIP Conference named room in the list. </br>
	 * 1. Open room conferenceRoomTest: this room have >2 users </br>
	 * 2. Start a voice call</br>
	 * 3. From calling layout, hang up the call</br>
	 * 4. Hit the back button to return in the rooms list.</br>
	 * Verify that no 'VoIP Conference' has been created.</br>
	 * @throws InterruptedException 
	 * 
	 */
	@Test(groups={"1driver_ios","1checkuser"}, description="test on conference",priority=6)
	public void hangUpAudioConference() throws InterruptedException{
		RiotRoomsListPageObjects riotRoomsList1= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		//0. Prerequisite: check that there is no VoIP Conference named room in the list.
		while (null!=riotRoomsList1.getRoomByName(unwantedRoomTest)) {
			riotRoomsList1.clickOnSwipedMenuOnRoom(unwantedRoomTest, "close");
		}
		//1. Open room conferenceRoomTest: this room have >2 users
		riotRoomsList1.getRoomByName(conferenceRoomTest).click();
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		
		//2. Start a voice call
		roomPage1.startVoiceCall();
		RiotCallingPageObjects callingPage1 = new RiotCallingPageObjects(appiumFactory.getiOsDriver1());
		
		//3. From calling layout, hang up the call
		callingPage1.hangUpButton.click();
		//4. Hit the back button to return in the rooms list.
		roomPage1.menuBackButton.click();
		
		//Verify that no 'VoIP Conference' has been created.
		MobileElement unWantedRoom=riotRoomsList1.getRoomByName(unwantedRoomTest);
		
		//tearDown
		if(null!=unWantedRoom){
			do {
				riotRoomsList1.clickOnSwipedMenuOnRoom(unwantedRoomTest, "close");
			} while (null!=riotRoomsList1.getRoomByName(unwantedRoomTest));
		}
		Assert.assertNull(unWantedRoom, "A room "+unwantedRoomTest+ " has been created after a Voip Conference (and deleted since).");
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
		checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotuser1DisplayName, Constant.DEFAULT_USERPWD);
	}
	
	@BeforeGroups("2checkuser")
	private void checkIfUser2Logged() throws InterruptedException, FileNotFoundException, YamlException{
		checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotuser1DisplayName, Constant.DEFAULT_USERPWD);
		checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver2(), riotuser2DisplayName, Constant.DEFAULT_USERPWD);
	}
}
