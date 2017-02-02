package mobilestests_ios;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import pom_ios.RiotCallingPageObjects;
import pom_ios.RiotIncomingCallPageObjects;
import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotVoipTests extends RiotParentTest{
	private String user1="riotuser2";
	private String user2="riotuser3";
	private String roomNameTest="voip room test";
	private String pwd="riotuser";

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
	@Test(groups="1driver_ios", description="test on call")
	public void cancelAudioCallFromChatRoom() throws InterruptedException{
		//1. Launch an audio call from a room
		RiotRoomsListPageObjects roomsList= new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		roomsList.getRoomByName(roomNameTest).click();;
		RiotRoomPageObjects room1=new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		room1.startVoiceCall();
		RiotCallingPageObjects callingView1=new RiotCallingPageObjects(AppiumFactory.getiOsDriver1());

		//2. From the call layout, hit the room link button
		callingView1.chatLinkButton.click();
		//Check the pending call view on the room view.
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.checkPendingCallView(true, "Active Call ("+user2+")");
		//assert on messages
		Assert.assertTrue(room1.getTextViewFromBubble(room1.getLastBubble()).getText().contains("placed a voice call"),"No '[user] placed a voice call' message in the room.");

		//3. Switch between call view and room view
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.getPendingCallView("Active Call ("+user2+")").click();
		Assert.assertTrue(callingView1.isDisplayed(true), "Calling view isn't displayed after touching pending call view from room view");
		callingView1.chatLinkButton.click();

		//4. End the call from the room view. 
		room1.hangUpCallButton.click();
		room1.checkPendingCallView(false, "Active Call ("+user2+")");
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
	@Test(groups="1driver_ios", description="test on call")
	public void cancelVideoCallFromChatRoom() throws InterruptedException{
		//1. Launch an video call from a room
		RiotRoomsListPageObjects roomsList= new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		roomsList.getRoomByName(roomNameTest).click();;
		RiotRoomPageObjects room1=new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		room1.startVideoCall();
		RiotCallingPageObjects callingView1=new RiotCallingPageObjects(AppiumFactory.getiOsDriver1());

		//2. From the call layout, hit the room link button
		callingView1.chatLinkButton.click();
		//Check the pending call view on the room view.
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.checkPendingCallView(true, "Active Call ("+user2+")");
		//assert on messages
		Assert.assertTrue(room1.getTextViewFromBubble(room1.getLastBubble()).getText().contains("placed a video call"),"No '[user] placed a video call' message in the room.");

		//3. Switch between call view and room view
		//TODO in the future change to room1.checkPendingCallView(true, "Calling..."); because of https://github.com/vector-im/riot-ios/issues/977
		room1.getPendingCallView("Active Call ("+user2+")").click();
		Assert.assertTrue(callingView1.isDisplayed(true), "Calling view isn't displayed after touching pending call view from room view");
		callingView1.chatLinkButton.click();

		//4. End the call from the room view. 
		room1.hangUpCallButton.click();
		room1.checkPendingCallView(false, "Active Call ("+user2+")");
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
	 * From device 1, check that the calling view is closed.
	 * @throws InterruptedException 
	 */
	@Test(groups="2driver_ios", description="call from device 1 answered by device 2")
	public void cancelIncommingAudioCall() throws InterruptedException{
		//TODO maybe use a different option than checkIfUserLogged.
		//checkIfUserLogged(AppiumFactory.getiOsDriver1(), user1, pwd);
		//checkIfUserLogged(AppiumFactory.getiOsDriver2(), user2, pwd);
		
		//1. Open a room, launch a voice call.
		RiotRoomsListPageObjects roomsList1= new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		roomsList1.getRoomByName(roomNameTest).click();;
		RiotRoomPageObjects room1=new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		room1.startVoiceCall();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingView1=new RiotCallingPageObjects(AppiumFactory.getiOsDriver1());
		//With device 2, assert that the incomming call view is displayed and correc.
		RiotIncomingCallPageObjects incomingCall2 = new RiotIncomingCallPageObjects(AppiumFactory.getiOsDriver2());
		incomingCall2.checkIncomingCallView(true, user1);
		//2. Ignore the call from device 2, assert that the incomming call view is closed
		incomingCall2.declineCallButton.click();
		//check that incoming call layout is closed on device 2
		incomingCall2.checkIncomingCallView(false, user1);
		//From device 1, check that the calling view is closed.
		callingView1.isDisplayed(false);
		room1.menuBackButton.click();
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 */
	private void checkIfUserLogged(IOSDriver<MobileElement> myDriver, String username, String pwd) throws InterruptedException {
		//if login page is displayed, then logged with the wanted user
		System.out.println("Check if user "+username+" is logged in "+Constant.APPLICATION_NAME);
		if(waitUntilDisplayed(myDriver, "AuthenticationVCView", false, 5)){
			System.out.println("User "+username+" isn't logged, login forced.");
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(myDriver);
			loginPage.fillLoginForm(username, pwd);
		}else{
			//check if the wanted user is loged in
			RiotRoomsListPageObjects listRoom = new RiotRoomsListPageObjects(myDriver);
			listRoom.settingsButton.click();
			String actualLoggedUser=listRoom.displayNameTextField.getText();
			System.out.println("display :"+actualLoggedUser);
			if(!actualLoggedUser.equals(username)){
				System.out.println("User "+username+" isn't logged. An other user is logged ("+actualLoggedUser+"), login with "+username+".");
				listRoom.logOut();
				RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(myDriver);
				loginPage.fillLoginForm(username, pwd);
			}else{
				//close lateral menu
				System.out.println("User "+username+" is logged.");
				myDriver.findElementByAccessibilityId("Back").click();
			}
		}
		
	}
}
