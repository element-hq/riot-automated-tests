package mobilestests_android;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_android.RiotCallingPageObjects;
import pom_android.RiotIncomingCallPageObjects;
import pom_android.RiotRoomDetailsPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import pom_android.RiotUnknownDevicesPageObjects;
import pom_android.RiotVerifyDevicePageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotE2eEncryptionTests extends RiotParentTest{
	private String roomWithEncryption="auto test encryption";
	private String oneToOneRoomWithEncryption="1:1e2e_automated tests";
	private String encrypted_msg_1="msg sent in encrypted room";
	private String encrypted_msg_2="this msg will be decrypted";
	private String participant2Adress="@riotuser10:matrix.org";
	private String participant1DisplayName="riotuser9";
	private String participant2DisplayName="riotuser10";	

	/**
	 * 1. Open room roomWithEncryption.
	 * 2. Send a photo
	 * Check that the photo is correctly uploaded. 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"},  priority=4, description="upload a photo in encrypted room")
	public void sendPhotoInEncryptedRoom() throws InterruptedException{
		//1. Create room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		roomsListDevice1.getRoomByName(oneToOneRoomWithEncryption);
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage1.closeWarningAlertAboutBetaStateOfE2e();
		// 2. Send a photo
		roomPage1.attachPhotoFromCamera("Small");
		//verifies that it's displayed in the message list
		Assert.assertTrue(roomPage1.waitAndCheckForMediaToBeUploaded(roomPage1.getLastPost(), 10), "Media wasn't uploaded after "+10+"s in encrypted room.");
		org.openqa.selenium.Dimension takenPhoto=roomPage1.getAttachedImageByPost(roomPage1.getLastPost()).getSize();
		Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
		//come back in rooms list
		roomPage1.menuBackButton.click();
	}

	/**
	 * 1. Open room roomWithEncryption with Device 1.
	 * 2. Open room roomWithEncryption with Device 2.
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
	@Test(groups={"2drivers_android","2checkusers"}, priority=5,description="start a voice call in encrypted room")
	public void startVoiceAndVideoCallInEncryptedRoom() throws InterruptedException{
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListDevice2 = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());

		//1. Open room roomWithEncryption with Device 1.
		roomsListDevice1.getRoomByName(oneToOneRoomWithEncryption);
		RiotRoomPageObjects newRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		newRoomDevice1.closeWarningAlertAboutBetaStateOfE2e();
		
		//2. Open room roomWithEncryption with Device 2.
		roomsListDevice2.getRoomByName(oneToOneRoomWithEncryption);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.closeWarningAlertAboutBetaStateOfE2e();

		//3. Start a voice call with Device 2.
		newRoomDevice2.startVoiceCall();
		RiotCallingPageObjects callingViewDevice2= new RiotCallingPageObjects(appiumFactory.getAndroidDriver2());
		callingViewDevice2.isDisplayed(true);
		//Check that an incoming layout is displayed on device 1.
		RiotIncomingCallPageObjects incomingCallDevice1= new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver1());
		incomingCallDevice1.checkIncomingCallView(true, participant2DisplayName, "Incoming Call");

		//4. Accept call with device 1.
		incomingCallDevice1.acceptCallButton.click();
		//check that call layout is diplayed on device 1
		RiotCallingPageObjects callingViewDevice1= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
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
		callingViewDevice2= new RiotCallingPageObjects(appiumFactory.getAndroidDriver2());
		callingViewDevice2.isDisplayed(true);
		//Check that an incoming layout is displayed on device 1.
		incomingCallDevice1= new RiotIncomingCallPageObjects(appiumFactory.getAndroidDriver1());
		incomingCallDevice1.checkIncomingCallView(true, participant2DisplayName, "Incoming Call");

		//7. Accept call with device 1.
		incomingCallDevice1.acceptCallButton.click();
		callingViewDevice2.waitUntilCallTook();
		//check that call layout is diplayed on device 1
		callingViewDevice1= new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
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
		
		//come back in rooms list
		newRoomDevice1.menuBackButton.click();
		newRoomDevice2.menuBackButton.click();
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
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(groups={"2drivers_android","2checkusers"}, priority=6)
	public void tryReadEncryptedMessageSentAfterJoining() throws InterruptedException, InstantiationException, IllegalAccessException{
		//1. Create room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsListDevice1 = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		roomsListDevice1.plusRoomButton.click();
		roomsListDevice1.createRoomCheckedTextView.click();
		RiotRoomPageObjects newRoomDevice1= new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		//Open room details
		newRoomDevice1.moreOptionsButton.click();
		newRoomDevice1.roomDetailsMenuItem.click();
		RiotRoomDetailsPageObjects newRoomDetailsDevice1 = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());
		newRoomDetailsDevice1.settingsTab.click();
		//changing room name
		newRoomDetailsDevice1.changeRoomName(roomWithEncryption);
		scrollToBottom(appiumFactory.getAndroidDriver1());
		//enables encryption
		newRoomDetailsDevice1.enableEncryption();
		//come back on the room page
		ExplicitWait(appiumFactory.getAndroidDriver1(), newRoomDetailsDevice1.menuBackButton);
		newRoomDetailsDevice1.menuBackButton.click();
		//Check that the last event in the room is about turning e2e encryption
		System.out.println(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText());
		waitUntilDisplayed(appiumFactory.getAndroidDriver1(), "//android.widget.TextView[contains(@text,'"+ utility.Constant.ENCRYPTION_TURNEDON_EVENT +"')]", true, 5);
		Assert.assertTrue(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText().contains(utility.Constant.ENCRYPTION_TURNEDON_EVENT));
		//in the meantime take care of device 2
		RiotRoomsListPageObjects roomsListDevice2 = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());

		//2. Send a message.
		newRoomDevice1.sendAMessage(encrypted_msg_1);
		Assert.assertEquals(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText(), encrypted_msg_1);

		//3. Send invitation to user on Device 2 and device 2 accepts.
		//Open room details
		newRoomDevice1.moreOptionsButton.click();
		newRoomDevice1.roomDetailsMenuItem.click();
		newRoomDetailsDevice1 = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());
		newRoomDetailsDevice1.addParticipant(participant2Adress);
		ExplicitWait(appiumFactory.getAndroidDriver1(), newRoomDetailsDevice1.menuBackButton);
		newRoomDetailsDevice1.menuBackButton.click();
		//accept invitation with device 2
		roomsListDevice2.previewInvitation(roomWithEncryption);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.joinRoomButton.click();
		newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.closeWarningAlertAboutBetaStateOfE2e();

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
		
		//come back in rooms list
		newRoomDevice1.menuBackButton.click();
		newRoomDevice2.menuBackButton.click();
	}

	/**
	 * 1. Log out / login for renew the keys 
	 * 2. Device 1 open room oneToOneRoomWithEncryption
	 * 3. Take a picture and attach it to the room
	 * Check that the 'Room contains unknown devices' modal is opened
	 * 4. Click OK on the modal
	 * 5. Click "Resend all" on the notification area of the room page
	 * Check that the file is actually uploaded.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android"}, priority=1,description="Test the Warn Unknown Devices modal with a file upload")
	public void sendPhotoInE2eRoomWithUnknownDevicesTest() throws InterruptedException{
		RiotRoomsListPageObjects roomsList1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//1. Device 1 logout/log in to renew his keys 
		roomsList1=roomsList1.logOutAndLogin(participant1DisplayName, Constant.DEFAULT_USERPWD);

		//2. Device 1 open room oneToOneRoomWithEncryption
		roomsList1.getRoomByName(oneToOneRoomWithEncryption).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage1.closeWarningAlertAboutBetaStateOfE2e();
		
		//3. Take a picture and attach it to the room
		roomPage1.attachPhotoFromCamera("Small");
		//roomPage1.waitAndCheckForMediaToBeUploaded(roomPage1.getLastPost(), 20);
		//Check that the 'Room contains unknown devices' modal is opened
		RiotUnknownDevicesPageObjects unknownDevicesModal1 = new RiotUnknownDevicesPageObjects(appiumFactory.getAndroidDriver1());
		unknownDevicesModal1.checkUnknownDevicesModal();

		// 4. Click OK on the modal
		unknownDevicesModal1.okButton.click();
		//5. Click "Resend all" on the notification area of the room page
		roomPage1.clickOnResendAllLinkFromNotificationArea();
		//Check that the message in the room notification area is gone
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getAndroidDriver1(), "im.vector.alpha:id/room_notification_message", true, 0),"Room notification message about msg not sent due to unknown devices is still here after clicking on 'Resend all'");
		//Check that the file is actually uploaded.
		roomPage1.checkThatPhotoIsPresentInPost(roomPage1.getLastPost());

		//come back to recents list
		roomPage1.menuBackButton.click();
	}

	/**
	 * 1. Device 2 logout/log in to renew his keys 
	 * 2. Device 1 open room oneToOneRoomWithEncryption
	 * 3. Device 2 open room oneToOneRoomWithEncryption
	 * 4. Device 1 send a message
	 * Check that message is not sent with device 2
	 * Check that the 'Room contains unknown devices' modal is opened
	 * 5. Hit the verify button on the first item of the list.
	 * Check that 'Verify device' modal is opened.
	 * 6. Hit the 'I verify that the keys match' button
	 * Check that the first button of the first device item is 'unverify'
	 * 7. Click on the OK button from the 'Room contains unknown devices' modal
	 * Check the message in the notification area, on the room page. 
	 * 8. Click on Resend all
	 * Check that message is sent in the room with device 2
	 * @throws InterruptedException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	@Test(groups={"2drivers_android","1checkuser"},  priority=2,description="Test the Warn Unknown Devices modal when sending a message")
	public void sendMessageInE2eRoomWithUnknownDevicesTest() throws InterruptedException, IllegalAccessException, InstantiationException{
		int deviceIndex=0;

		RiotRoomsListPageObjects roomsList1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsList2=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		//1. Device 2 logout/log in to renew his keys 
		roomsList2=roomsList2.logOutAndLogin(participant2DisplayName, Constant.DEFAULT_USERPWD);

		//2. Device 1 open room oneToOneRoomWithEncryption
		roomsList1.getRoomByName(oneToOneRoomWithEncryption).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage1.closeWarningAlertAboutBetaStateOfE2e();

		//3. Device 2 open room oneToOneRoomWithEncryption
		roomsList2.getRoomByName(oneToOneRoomWithEncryption).click();
		RiotRoomPageObjects roomPage2=new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		roomPage2.closeWarningAlertAboutBetaStateOfE2e();

		//4. Device 1 send a message
		roomPage1.sendAMessage(encrypted_msg_1);
		//Check that message is not sent with device 2
		Assert.assertFalse(roomPage2.getTextViewFromPost(roomPage2.getLastPost()).getText().equals(encrypted_msg_1), "Msg sent by user1 is decrypted by user2 whereas user1 has new key");
		//Check that the 'Room contains unknown devices' modal is opened
		RiotUnknownDevicesPageObjects unknownDevicesModal1 = new RiotUnknownDevicesPageObjects(appiumFactory.getAndroidDriver1());
		unknownDevicesModal1.checkUnknownDevicesModal();

		//5. Hit the verify button on the first item of the list.
		String expectedDeviceName = unknownDevicesModal1.getDeviceNameByIndex(deviceIndex);
		String expectedDeviceId = unknownDevicesModal1.getDeviceIDByIndex(deviceIndex);
		unknownDevicesModal1.getVerifyDeviceButton(deviceIndex).click();
		RiotVerifyDevicePageObjects verifyAlert1=new RiotVerifyDevicePageObjects(appiumFactory.getAndroidDriver1());
		//Check that 'Verify device' modal is opened.
		verifyAlert1.checkVerifyDeviceAlert(expectedDeviceName, expectedDeviceId, null);

		//6. Hit the 'I verify that the keys match' button
		verifyAlert1.alertVerifyButton.click();
		//Check that the first button of the first device item is 'unverify'
		Assert.assertEquals(unknownDevicesModal1.getVerifyDeviceButton(deviceIndex).getText(), "Unverify");

		//7. Click on the OK button from the 'Room contains unknown devices' modal
		unknownDevicesModal1.okButton.click();
		//Check the message in the notification area, on the room page. 
		Assert.assertEquals(roomPage1.notificationMessage.getText(), "Messages not sent due to unknown devices being present. Resend all or cancel all now?");

		//8. Click on Resend all
		roomPage1.clickOnResendAllLinkFromNotificationArea();
		//Check that message is sent in the room with device 2
		roomPage2.waitForReceivingNewMessage(10);
		Assert.assertTrue(roomPage2.getTextViewFromPost(roomPage2.getLastPost()).getText().equals(encrypted_msg_1), "Msg sent by user1 is decrypted by user2 whereas user1 has new key");

		//come back to rooms list
		roomPage1.menuBackButton.click();
		roomPage2.menuBackButton.click();
	}

	/**
	 * 1. Log out / login for renew the keys 
	 * 2. Device 1 open room oneToOneRoomWithEncryption
	 * 3. Start a voice call
	 * Check that the call layout isn't displayed
	 * Check that the 'Room contains unknown devices' modal is opened
	 * 4. Click OK on the modal
	 * 5. Start a call
	 * Check that the call is made
	 * 6. Cancel it
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android"},  priority=3,description="Test the Warn Unknown Devices modal with a voice call")
	public void tryVoiceCallInE2eRoomWithUnknownDevicesTest() throws InterruptedException{
		RiotRoomsListPageObjects roomsList1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//1. Device 1 logout/log in to renew his keys 
		roomsList1=roomsList1.logOutAndLogin(participant1DisplayName, Constant.DEFAULT_USERPWD);

		//2. Device 1 open room oneToOneRoomWithEncryption
		roomsList1.getRoomByName(oneToOneRoomWithEncryption).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage1.closeWarningAlertAboutBetaStateOfE2e();
		
		//3. Start a voice call
		roomPage1.startVoiceCall();
		//Check that the call layout isn't displayed
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getAndroidDriver1(),"im.vector.alpha:id/call_layout", true, 2), "Call layout is displayed and shouldn't.");
		//Check that the 'Room contains unknown devices' modal is opened
		RiotUnknownDevicesPageObjects unknownDevicesModal1 = new RiotUnknownDevicesPageObjects(appiumFactory.getAndroidDriver1());
		unknownDevicesModal1.checkUnknownDevicesModal();

		// 4. Click OK on the modal
		unknownDevicesModal1.okButton.click();
		//5. Start a call
		roomPage1.startVoiceCall();
		RiotCallingPageObjects callLayout1 = new RiotCallingPageObjects(appiumFactory.getAndroidDriver1());
		callLayout1.isDisplayed(true);

		//6. Cancel it
		callLayout1.hangUpButton.click();
		//come back to recents list
		roomPage1.menuBackButton.click();
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

	private void leaveRoomOn1DeviceFromRoomPageAfterTest(String roomNameFromDevice1) throws InterruptedException{
		RiotRoomPageObjects newRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		newRoomDevice1.leaveRoom();
		RiotRoomsListPageObjects roomsListDevice1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//asserts that the DM rooms are really left
		roomsListDevice1.waitUntilSpinnerDone(5);
		Assert.assertNull(roomsListDevice1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
	}
	private void leaveRoomOn2DevicesFromRoomPageAfterTest(String roomNameFromDevice1, String roomNameFromDevice2) throws InterruptedException{
		RiotRoomPageObjects newRoomDevice1 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		newRoomDevice1.leaveRoom();
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 2");
		newRoomDevice2.leaveRoom();

		RiotRoomsListPageObjects roomsListDevice1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListDevice2=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
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

	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups({"1checkuser"})
	private void checkIfUser1Logged() throws InterruptedException{
		super.checkIfUserLoggedAndroid(appiumFactory.getAndroidDriver1(), participant1DisplayName, Constant.DEFAULT_USERPWD);
	}
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups({"2checkusers"})
	private void checkIfUser2Logged() throws InterruptedException{
		super.checkIfUserLoggedAndroid(appiumFactory.getAndroidDriver2(), participant2DisplayName, Constant.DEFAULT_USERPWD);
	}
}
