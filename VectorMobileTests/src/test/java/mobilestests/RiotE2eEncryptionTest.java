package mobilestests;

import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom.RiotCallingPageObject;
import pom.RiotIncomingCallPageObjects;
import pom.RiotNewChatPageObjects;
import pom.RiotRoomDetailsPageObject;
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
import pom.RiotSearchInvitePageObjects;
import utility.AppiumFactory;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotE2eEncryptionTest extends RiotParentTest{
	private String roomWithEncryption="auto test encryption";
	private String encrypted_msg_1="msg sent in encrypted room";
	private String encrypted_msg_2="this msg will be decrypted";
	private String participant2Adress="@riotuser9:matrix.org";
	private String participant1DisplayName="riotuser6";
	private String participant2DisplayName="riotuser9";

	/**
	 * 1. Create room with Device 1 and enable encryption.
	 * Check that the last event in the room is about turning e2e encryption
	 * 2. Sent a message.
	 * 3. Send invitation to user on Device 2 and device 2 accepts.
	 * Check that user on Device 2 can't read the msg sent by Device 1 before he joined.
	 * 4. Send a message with device 2
	 * Check that user on device 1 can decrypt and read the message.
	 * @throws InterruptedException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(groups={"2drivers","roomcreated2"})
	public void tryReadEncryptedMessageSentAfterJoining() throws InterruptedException, InstantiationException, IllegalAccessException{
		//1. Create room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		roomsListDevice1.plusRoomButton.click();
		roomsListDevice1.createRoomCheckedTextView.click();
		RiotRoomPageObjects newRoomDevice1= new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		//Open room details
		newRoomDevice1.moreOptionsButton.click();
		newRoomDevice1.roomDetailsMenuItem.click();
		RiotRoomDetailsPageObject newRoomDetailsDevice1 = new RiotRoomDetailsPageObject(AppiumFactory.getAppiumDriver1());
		newRoomDetailsDevice1.settingsTab.click();
		//changing room name
		newRoomDetailsDevice1.changeRoomName(roomWithEncryption);
		scrollToBottom(AppiumFactory.getAppiumDriver1());
		//enables encryption
		newRoomDetailsDevice1.enableEncryption();
		//come back on the room page
		ExplicitWait(AppiumFactory.getAppiumDriver1(), newRoomDetailsDevice1.menuBackButton);
		newRoomDetailsDevice1.menuBackButton.click();
		//Check that the last event in the room is about turning e2e encryption
		System.out.println(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText());
		waitUntilDisplayed(AppiumFactory.getAppiumDriver1(), "//android.widget.TextView[contains(@text,'"+ utility.Constant.ENCRYPTION_TURNEDON_EVENT +"')]", true, 5);
		Assert.assertTrue(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText().contains(utility.Constant.ENCRYPTION_TURNEDON_EVENT));
		//in the meantime take care of device 2
		RiotRoomsListPageObjects roomsListDevice2 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver2());

		//2. Sent a message.
		newRoomDevice1.sendAMessage(encrypted_msg_1);
		Assert.assertEquals(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText(), encrypted_msg_1);

		//3. Send invitation to user on Device 2 and device 2 accepts.
		//Open room details
		newRoomDevice1.moreOptionsButton.click();
		newRoomDevice1.roomDetailsMenuItem.click();
		newRoomDetailsDevice1 = new RiotRoomDetailsPageObject(AppiumFactory.getAppiumDriver1());
		newRoomDetailsDevice1.addParticipant(participant2Adress);
		ExplicitWait(AppiumFactory.getAppiumDriver1(), newRoomDetailsDevice1.menuBackButton);
		newRoomDetailsDevice1.menuBackButton.click();
		//accept invitation with device 2
		roomsListDevice2.previewInvitation(roomWithEncryption);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver2());
		newRoomDevice2.joinRoomButton.click();
		newRoomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver2());

		//Check that user on Device 2 can't read the msg sent by Device 1 before he joined.
		//get the before before last post
		newRoomDevice2.waitForPostsToBeDisplayed();
		int index=newRoomDevice2.postsListLayout.size()-3;
		Assert.assertEquals(newRoomDevice2.getTextViewFromPost(newRoomDevice2.getPostByIndex(index)).getText(), utility.Constant.ENCRYPTION_UNKNOWN_SESSION_ID_MSG);

		//4. Send a message with device 2
		newRoomDevice2.sendAMessage(encrypted_msg_2);
		//Check that user on device 1 can decrypt and read the message.
		newRoomDevice1.waitForReceivingNewMessage(5);
		Assert.assertEquals(newRoomDevice2.getTextViewFromPost(newRoomDevice1.getLastPost()).getText(), encrypted_msg_2);
	}

	/**
	 * 1. Create a room and enable encryption.
	 * 2. Send a photo
	 * Check that the photo is correctly uploaded. 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver","roomcreated"}, description="upload a photo in encrypted room")
	public void sendPhotoInEncryptedRoom() throws InterruptedException{
		//1. Create room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		roomsListDevice1.plusRoomButton.click();
		roomsListDevice1.createRoomCheckedTextView.click();
		RiotRoomPageObjects newRoomDevice1= new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		//Open room details
		newRoomDevice1.moreOptionsButton.click();
		newRoomDevice1.roomDetailsMenuItem.click();
		RiotRoomDetailsPageObject newRoomDetailsDevice1 = new RiotRoomDetailsPageObject(AppiumFactory.getAppiumDriver1());
		newRoomDetailsDevice1.settingsTab.click();
		ExplicitWait(AppiumFactory.getAppiumDriver1(), newRoomDetailsDevice1.listItemSettings);
		scrollToBottom(AppiumFactory.getAppiumDriver1());
		//enables encryption
		newRoomDetailsDevice1.enableEncryption();
		//come back on the room page
		ExplicitWait(AppiumFactory.getAppiumDriver1(), newRoomDetailsDevice1.menuBackButton);
		newRoomDetailsDevice1.menuBackButton.click();
		// 2. Send a photo
		newRoomDevice1.attachPhotoFromCamera("Small");
		//verifies that it's displayed in the message list
		Assert.assertTrue(newRoomDevice1.waitAndCheckForMediaToBeUploaded(newRoomDevice1.getLastPost(), 10), "Media wasn't uploaded after "+10+"s in encrypted room.");
		org.openqa.selenium.Dimension takenPhoto=newRoomDevice1.getAttachedImageByPost(newRoomDevice1.getLastPost()).getSize();
		Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
	}

	/**
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
	@Test(groups={"2drivers","roomcreated2bis"}, description="start a voice call in encrypted room")
	public void startVoiceAndVideoCallInEncryptedRoom() throws InterruptedException{
		//1. Create DM room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		RiotRoomsListPageObjects roomsListDevice2 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver2());
		roomsListDevice1.plusRoomButton.click();
		roomsListDevice1.startChatCheckedTextView.click();
		RiotSearchInvitePageObjects inviteViewDevice1=new RiotSearchInvitePageObjects(AppiumFactory.getAppiumDriver1());
		inviteViewDevice1.searchAndSelectMember(participant2Adress);
		RiotNewChatPageObjects newChatViewDevice1= new RiotNewChatPageObjects(AppiumFactory.getAppiumDriver1());
		newChatViewDevice1.confirmRoomCreationButton.click();
		RiotRoomPageObjects newRoomDevice1= new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		//Open room details
		newRoomDevice1.moreOptionsButton.click();
		newRoomDevice1.roomDetailsMenuItem.click();
		RiotRoomDetailsPageObject newRoomDetailsDevice1 = new RiotRoomDetailsPageObject(AppiumFactory.getAppiumDriver1());

		//2. Enable encryption.
		newRoomDetailsDevice1.settingsTab.click();
		scrollToBottom(AppiumFactory.getAppiumDriver1());ExplicitWait(AppiumFactory.getAppiumDriver1(), newRoomDetailsDevice1.listItemSettings);
		newRoomDetailsDevice1.enableEncryption();
		//come back on the room page
		ExplicitWait(AppiumFactory.getAppiumDriver1(), newRoomDetailsDevice1.menuBackButton);newRoomDetailsDevice1.menuBackButton.click();
		//accepts invitation with device 2
		roomsListDevice2.previewInvitation(participant1DisplayName);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver2());
		newRoomDevice2.joinRoomButton.click();
		//newRoomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver2());

		//3. Start a voice call with Device 2.
		newRoomDevice2.startVoiceCall();
		RiotCallingPageObject callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAppiumDriver2());
		callingViewDevice2.isDisplayed(true);
		//Check that an incoming layout is displayed on device 1.
		RiotIncomingCallPageObjects incomingCallDevice1= new RiotIncomingCallPageObjects(AppiumFactory.getAppiumDriver1());
		incomingCallDevice1.checkIncomingCallView(true, participant2DisplayName, "Incoming Call");

		//4. Accept call with device 1.
		incomingCallDevice1.acceptCallButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObject callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingViewDevice1.isDisplayed(true);

		//5. Hang-out after a few seconds.
		callingViewDevice1.hangUpButton.click();
		//Check that calling layout is closed on both devices.
		callingViewDevice1.isDisplayed(false);
		callingViewDevice2.isDisplayed(false);
		//check end call events on messages
		Assert.assertEquals(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText(),participant1DisplayName+" ended the call.");
		Assert.assertEquals(newRoomDevice2.getTextViewFromPost(newRoomDevice2.getLastPost()).getText(),participant1DisplayName+" ended the call.");

		//6. Start a video call with Device 2.
		newRoomDevice2.startVideoCall();
		callingViewDevice2= new RiotCallingPageObject(AppiumFactory.getAppiumDriver2());
		callingViewDevice2.isDisplayed(true);
		//Check that an incoming layout is displayed on device 1.
		incomingCallDevice1= new RiotIncomingCallPageObjects(AppiumFactory.getAppiumDriver1());
		incomingCallDevice1.checkIncomingCallView(true, participant2DisplayName, "Incoming Call");

		//7. Accept call with device 1.
		incomingCallDevice1.acceptCallButton.click();
		callingViewDevice2.waitUntilCallTook();
		//check that call layout is diplayed on device 1
		callingViewDevice1= new RiotCallingPageObject(AppiumFactory.getAppiumDriver1());
		callingViewDevice1.isDisplayed(true);

		//8. Hang-out after a few seconds.
		callingViewDevice1.mainCallLayout.click();//display the controls if they had fade out.
		callingViewDevice1.hangUpButton.click();
		//Check that calling layout is closed on both devices.
		callingViewDevice1.isDisplayed(false);
		callingViewDevice2.isDisplayed(false);
		//check end call events on messages
		Assert.assertEquals(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText(),participant1DisplayName+" ended the call.");
		Assert.assertEquals(newRoomDevice2.getTextViewFromPost(newRoomDevice2.getLastPost()).getText(),participant1DisplayName+" ended the call.");
	}

	@AfterGroups(groups="roomcreated2")
	private void leaveRoomAfterTest1() throws InterruptedException{
		leaveRoomOn2DevicesFromRoomPageAfterTest(roomWithEncryption,roomWithEncryption);
	}

	@AfterGroups(groups="roomcreated2bis")
	private void leaveRoomAfterTest2() throws InterruptedException{
		leaveRoomOn2DevicesFromRoomPageAfterTest(null,null);
	}

	@AfterGroups(groups="roomcreated")
	private void leaveRoomAfterTest3() throws InterruptedException{
		leaveRoomOn1DeviceFromRoomPageAfterTest("Empty room");
	}

	private void leaveRoomOn1DeviceFromRoomPageAfterTest(String roomNameFromDevice1) throws InterruptedException{
		RiotRoomPageObjects newRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		newRoomDevice1.leaveRoom();
		RiotRoomsListPageObjects roomsListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		//asserts that the DM rooms are really left
		roomsListDevice1.waitUntilSpinnerDone(5);
		Assert.assertNull(roomsListDevice1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
	}
	private void leaveRoomOn2DevicesFromRoomPageAfterTest(String roomNameFromDevice1, String roomNameFromDevice2) throws InterruptedException{
		RiotRoomPageObjects newRoomDevice1 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(AppiumFactory.getAppiumDriver2());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		newRoomDevice1.leaveRoom();
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 2");
		newRoomDevice2.leaveRoom();

		RiotRoomsListPageObjects roomsListDevice1=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		RiotRoomsListPageObjects roomsListDevice2=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver2());
		//asserts that the DM rooms are really left
		if(roomNameFromDevice1!=null){
			roomsListDevice1.waitUntilSpinnerDone(5);
			Assert.assertNull(roomsListDevice1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
		}

		if(roomNameFromDevice1!=null){
			roomsListDevice2.waitUntilSpinnerDone(5);
			Assert.assertNull(roomsListDevice2.getRoomByName(roomNameFromDevice2), "Room "+roomNameFromDevice2+" is still displayed in the list in device 2.");
		}

	}
}
