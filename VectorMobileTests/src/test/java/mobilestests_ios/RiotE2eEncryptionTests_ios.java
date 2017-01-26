package mobilestests_ios;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import pom_ios.RiotCallingPageObjects;
import pom_ios.RiotIncomingCallPageObjects;
import pom_ios.RiotRoomDetailsPageObjects;
import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.RiotParentTest;

/**
 * Tests on e2e encryption on IOS platform.
 * @author matrix
 *
 */
public class RiotE2eEncryptionTests_ios extends RiotParentTest{
	private String roomWithEncryption="auto test encryption";
	private String encrypted_msg_1="msg sent in encrypted room";
	private String encrypted_msg_2="this msg will be decrypted";
	private String participant2Adress="@riotuser9:matrix.org";
	private String participant1DisplayName="riotuser6";
	private String participant2DisplayName="riotuser9";


	/**
	 * 1. Create a room and enable encryption.
	 * 2. Send a photo
	 * Check that the photo is correctly uploaded. 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios"}, description="upload a photo in encrypted room")
	public void sendPhotoInEncryptedRoom() throws InterruptedException{
		//1. Create room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		RiotRoomPageObjects roomPage= roomsList.createRoom();
		RiotRoomDetailsPageObjects roomDetails =roomPage.openDetailView();
		roomDetails.settingsTab.click();
		scrollToBottom(AppiumFactory.getiOsDriver1());
		roomDetails.enableEncryption();
		roomDetails.roomDetailsDoneButton.click();
		roomDetails.waitUntilDetailsDone();

		//2. Send a photo
		roomPage.attachPhotoFromCamera("Small");
		//verifies that it's displayed in the message list
		roomPage.waitAndCheckForMediaToBeUploaded(roomPage.getLastBubble(), 15);
		//TODO check size of the uploaded photo
		//org.openqa.selenium.Dimension takenPhoto=newRoomDevice1.getAttachedImageByPost(newRoomDevice1.getLastPost()).getSize();
		//Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
		//roomPage.menuBackButton.click();
	}
	/**
	 * 1. Create room with Device 1 and enable encryption.
	 * Check that the last event in the room is about turning e2e encryption
	 * 2. Sent a message.
	 * 3. Send invitation to user on Device 2 and device 2 accepts.
	 * Check that user on Device 2 can't read the msg sent by Device 1 before he joined.
	 * 4. Send a message with device 2
	 * Check that user on device 1 can decrypt and read the message.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2driver_ios"})
	public void tryReadEncryptedMessageSentAfterJoining() throws InterruptedException{
		//1. Create room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		RiotRoomPageObjects roomPage1= roomsList.createRoom();
		roomPage1.roomMembersDetailIcon.click();
		RiotRoomDetailsPageObjects roomDetails = new RiotRoomDetailsPageObjects(AppiumFactory.getiOsDriver1());
		roomDetails.settingsTab.click();
		roomDetails.changeRoomName(roomWithEncryption);
		scrollToBottom(AppiumFactory.getiOsDriver1());
		roomDetails.enableEncryption();
		roomDetails.roomDetailsDoneButton.click();
		roomDetails.waitUntilDetailsDone();
		RiotRoomsListPageObjects roomsList2 = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver2());//TMP

		//2. Send a message.
		roomPage1.sendAMessage(encrypted_msg_1);
		Assert.assertTrue(roomPage1.getTextViewFromBubble(roomPage1.getLastBubble()).getText().contains(encrypted_msg_1), "Msg sent in e2e room isn't received in sender's room");

		//3. Send invitation to user on Device 2 and device 2 accepts.
		roomPage1.openRoomDetails.click();
		RiotRoomDetailsPageObjects roomDetails1=new RiotRoomDetailsPageObjects(AppiumFactory.getiOsDriver1());
		roomDetails1.addParticipant(participant2Adress);
		roomDetails1.menuBackButton.click();
		//accept invitation with device 2
		roomsList2.previewInvitation(roomWithEncryption);
		RiotRoomPageObjects roomPage2 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver2());
		roomPage2.joinRoomButton.click();

		//Check that user on Device 2 can't read the msg sent by Device 1 before he joined.
		roomPage2.waitForBubblesToBeDisplayed();
		int index=roomPage2.bubblesList.size()-2;
		Assert.assertTrue(roomPage2.getTextViewFromBubble(roomPage2.getBubbleByIndex(index)).getText().contains(utility.Constant.ENCRYPTION_UNKNOWN_SESSION_ID_MSG), "Msg sent by device 1 isn't encrypted, or encrypted unknown msg has changed");

		//4. Send a message with device 2
		roomPage2.sendAMessage(encrypted_msg_2);
		//Check that user on device 1 can decrypt and read the message.
		roomPage1.waitForReceivingNewMessage(2);
		Assert.assertTrue(roomPage1.getTextViewFromBubble(roomPage1.getLastBubble()).getText().contains(encrypted_msg_2), "Msg sent by device 2 isn't decrypted or received on device 1.");
	}

	/**
	 * TODO test this test when https://github.com/vector-im/riot-ios/issues/972 is resolved
	 * 1. Create DM room with Device 1 and invite device 2.
	 * 2. Enable encryption.
	 * 3. Start a VOICE call with Device 2.
	 * Check that an incoming layout is displayed on device 1.
	 * 4. Accept call with device 1.
	 * 5. Hang-out after a few seconds.
	 * Check that calling layout is closed on both devices.
	 * 6. Start a VIDEO call with Device 2.
	 * Check that an incoming layout is displayed on device 1.
	 * 7. Accept call with device 1.
	 * 8. Hang-out after a few seconds.
	 * Check that calling layout is closed on both devices.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2driver_ios"}, description="start a voice call in encrypted room")
	public void startVoiceAndVideoCallInEncryptedRoom() throws InterruptedException{
		//1. Create DM room with Device 1 and invite device 2.
		//		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		//		RiotRoomsListPageObjects roomsList2 = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver2());
		//		roomsList1.plusRoomButton.click();
		//		roomsList1.startChatButton.click();
		//		RiotNewChatPageObjects inviteNewChatPage = new RiotNewChatPageObjects(AppiumFactory.getiOsDriver1());
		//		inviteNewChatPage.searchAndSelectMember(participant2Adress);
		//		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		//		
		//		//2. Enable encryption.
		//		roomPage1.roomNameStaticText.click();
		//		roomPage1.roomMembersDetailIcon.click();
		//		RiotRoomDetailsPageObjects roomDetails = new RiotRoomDetailsPageObjects(AppiumFactory.getiOsDriver1());
		//		roomDetails.settingsTab.click();
		//		scrollToBottom(AppiumFactory.getiOsDriver1());
		//		roomDetails.enableEncryption();
		//		roomDetails.roomDetailsDoneButton.click();
		//		roomDetails.waitUntilDetailsDone();
		//		//accepts invitation with device 2
		//		roomsList2.previewInvitation(participant1DisplayName);
		//		RiotRoomPageObjects roomPage2 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver2());
		//		roomPage2.joinRoomButton.click();
		//		roomPage2.waitForBubblesToBeDisplayed();


		//TMP
		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		RiotRoomsListPageObjects roomsList2 = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver2());
		roomsList1.getRoomByName(participant2DisplayName).click();
		roomsList2.getRoomByName(participant1DisplayName).click();
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		RiotRoomPageObjects roomPage2 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver2());
		//FIN TMP


		//3. Start a VOICE call with Device 2.
		roomPage2.startVoiceCall();
		RiotCallingPageObjects callLayout2= new RiotCallingPageObjects(AppiumFactory.getiOsDriver2());
		//		callLayout2.isDisplayed(true);
		//Check that an incoming layout is displayed on device 1.
		RiotIncomingCallPageObjects incomingCall1= new RiotIncomingCallPageObjects(AppiumFactory.getiOsDriver1());
		//incomingCall1.checkIncomingCallView(true, participant2DisplayName);

		//4. Accept call with device 1.
		incomingCall1.acceptCallButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callLayout1= new RiotCallingPageObjects(AppiumFactory.getiOsDriver1());
		callLayout1.isDisplayed(true);

		//5. Hang-out after a few seconds.
		callLayout1.hangUpButton.click();
		//Check that calling layout is closed on both devices.
		callLayout1.isDisplayed(false);
		callLayout2.isDisplayed(false);
		//check end call events on messages
		Assert.assertTrue(roomPage1.getTextViewFromBubble(roomPage1.getLastBubble()).getText().contains(participant1DisplayName+" ended the call."));
		Assert.assertTrue(roomPage2.getTextViewFromBubble(roomPage2.getLastBubble()).getText().contains(participant1DisplayName+" ended the call."));

		//6. Start a video call with Device 2.
		roomPage2.startVideoCall();
		callLayout2= new RiotCallingPageObjects(AppiumFactory.getiOsDriver2());

		//7. Accept call with device 1.
		incomingCall1.acceptCallButton.click();
		//check that call layout is diplayed on device 1
		callLayout1= new RiotCallingPageObjects(AppiumFactory.getiOsDriver1());
		callLayout1.isDisplayed(true);

		//8. Hang-out after a few seconds.
		callLayout1.callerImage.click();
		callLayout1.hangUpButton.click();
		callLayout1.isDisplayed(false);
		callLayout2.isDisplayed(false);
		//check end call events on messages
		Assert.assertTrue(roomPage1.getTextViewFromBubble(roomPage1.getLastBubble()).getText().contains(participant1DisplayName+" ended the call."));
		Assert.assertTrue(roomPage2.getTextViewFromBubble(roomPage2.getLastBubble()).getText().contains(participant1DisplayName+" ended the call."));
	}

	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException{
		switch (m.getName()) {
		case "tryReadEncryptedMessageSentAfterJoining":
			leaveRoomOn2DevicesFromRoomPageAfterTest(roomWithEncryption,roomWithEncryption);
			break;
		case "startVoiceAndVideoCallInEncryptedRoom":
			leaveRoomOn2DevicesFromRoomPageAfterTest(null,null);
			break;
		case "sendPhotoInEncryptedRoom":
			leaveRoomOn1DeviceFromRoomPageAfterTest("Empty room");
			break;
		default:
			break;
		}
	}

	@Test
	public void testTearDown() throws InterruptedException{
		leaveRoomOn1DeviceFromRoomPageAfterTest("Empty room");
	}

	private void leaveRoomOn1DeviceFromRoomPageAfterTest(String roomNameFromDevice1) throws InterruptedException{
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		roomPage1.leaveRoom();
		RiotRoomsListPageObjects roomsList1= new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		Assert.assertNull(roomsList1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
	}
	
	private void leaveRoomOn2DevicesFromRoomPageAfterTest(String roomNameFromDevice1, String roomNameFromDevice2) throws InterruptedException{
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		RiotRoomPageObjects roomPage2 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver2());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		roomPage1.leaveRoom();
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 2");
		roomPage2.leaveRoom();

		//asserts that the DM rooms are really left
		if(roomNameFromDevice1!=null){
			RiotRoomsListPageObjects roomsList1= new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
			Assert.assertNull(roomsList1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
		}
		if(roomNameFromDevice1!=null){
			RiotRoomsListPageObjects roomsList2= new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver2());
			Assert.assertNull(roomsList2.getRoomByName(roomNameFromDevice2), "Room "+roomNameFromDevice2+" is still displayed in the list in device 1.");
		}
	}
}
