package mobilestests_ios;

import org.testng.Assert;
import org.testng.annotations.Test;

import pom_ios.RiotCameraPageObjects;
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
		//RiotRoomPageObjects newRoom= roomsList.createRoom();
		roomsList.getRoomByName("a encryption").click();//TEMP
		RiotRoomPageObjects roomPage = new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		roomPage.attachPhotoFromCamera("Actual Size");//Small
		roomPage.menuBackButton.click();
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
	@Test(groups={"2driver_ios"}, description="upload a photo in encrypted room")
	public void tryReadEncryptedMessageSentAfterJoining() throws InterruptedException{
		//1. Create room with Device 1 and enable encryption.
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		//RiotRoomPageObjects newRoom= roomsList.createRoom();
		RiotRoomsListPageObjects roomsList2 = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver2());//TMP
		roomsList.getRoomByName("a encryption").click();//TEMP
		RiotRoomPageObjects roomPage = new RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		
//		//Open room details
//		waitUntilDisplayed(AppiumFactory.getiOsDriver1(), "//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar[@name='Messages']/XCUIElementTypeOther[2]//XCUIElementTypeImage", true, 2);
//		roomPage.openRoomDetails.click();
//		RiotRoomDetailsPageObjects roomDetails = new RiotRoomDetailsPageObjects(AppiumFactory.getiOsDriver1());
//		roomDetails.settingsTab.click();
//		//changing room name
//		roomDetails.changeRoomName(roomWithEncryption);
//		scrollToBottom(AppiumFactory.getiOsDriver1());
//		roomDetails.enableEncryption();
//		roomDetails.roomDetailsDoneButton.click();
//		roomDetails.waitUntilDetailsDone();
		
		////Check that the last event in the room is about turning e2e encryption
//		roomPage.menuBackButton.click();
		//utility.Constant.ENCRYPTION_TURNEDON_EVENT
		//TODO Assert.assertTrue(roomPage.getTextViewFromBubble(roomPage.getLastBubble()).getText().contains("Hi"), "The last message isn't about encryption turned on.");
		
		//2. Sent a message.
		//TODO roomPage.sendAMessage(encrypted_msg_1);
		
		//tmp: with device2, open the room
		
		roomsList2.getRoomByName("a encryption").click();
		RiotRoomPageObjects roomPage2 = new RiotRoomPageObjects(AppiumFactory.getiOsDriver2());
		roomPage2.sendAMessage("coucou");

	}
}
