package mobilestests_ios;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.MobileElement;
import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotMessagesReceptionTests extends RiotParentTest{
	private String msgFromUpUser="UP";
	private String roomId="!SBpfTGBlKgELgoLALQ%3Amatrix.org";
	private String roomTest="msg rcpt 4 automated tests";
	private String riotUserDisplayNameA="riotuser4";
	private String riotUserDisplayNameB="riotuser5";
	private String riotSenderUserDisplayName="riotuserup";
	private String riotSenderAccessToken;

	/**
	 * Validates issue https://github.com/vector-im/riot-ios/issues/809
	 * 1. Open roomtest with device A.
	 * 2. Open roomtest with device B.
	 * 3. User A write something in the message bar but don't send it.
	 * Test that the typing indicator indicates '[user1] is typing..." with device B.
	 * 4. Type an other msg and clear it with user 4 in the message bar.
	 * Test that the typing indicator is empty on device B.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_ios","2checkuser"})//@Test(groups="2drivers_ios")
	public void typingIndicatorTest() throws InterruptedException{
		String notSentMsg="tmp";
		RiotRoomsListPageObjects roomsListA = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomsListPageObjects roomsListB= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver2());

		//1. Open roomtest with device A.
		roomsListA.getRoomByName(roomTest).click();
		RiotRoomPageObjects roomA=new  RiotRoomPageObjects(appiumFactory.getiOsDriver1());

		//2. Open roomtest with device B.		
		roomsListB.getRoomByName(roomTest).click();
		RiotRoomPageObjects roomB=new  RiotRoomPageObjects(appiumFactory.getiOsDriver2());

		//3. User A write something in the message bar but don't send it.
		roomA.sendKeyTextView.setValue(notSentMsg);
		//Test that the typing indicator indicates '[user1] is typing..." with device B.
		Assert.assertEquals(roomB.notificationMessage.getText(), riotUserDisplayNameA+" is typing...");
		Assert.assertTrue(roomB.notificationMessage.isDisplayed(),"Typing indicator isn't displayed on device B");

		//4. Type an other msg and clear it with user 4 in the message bar.
		roomA.sendKeyTextView.setValue(notSentMsg);
		roomA.sendKeyTextView.findElementByClassName("XCUIElementTypeTextView").clear();
		//Test that the typing indicator is empty on device B.
		Assert.assertFalse(roomB.notificationMessage.isDisplayed(),"Typing indicator is displayed on device B and shouldn't because device A isn't typing");
		//come back to rooms list
		roomA.menuBackButton.click();
		roomB.menuBackButton.click();
	}

	/**
	 * 1. Stay in recents list and get the current badge on room roomTest </br>
	 * 2. Receive a message in a room from an other user. </br>
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.</br>
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(groups={"messageReceivedInList","1checkuser","1driver_ios"},priority=1)
	public void checkBadgeAndMessageOnRoomItem() throws InterruptedException, IOException{
		//1. Stay in recents list and get the current badge on room roomTest
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		Integer currentBadge=riotRoomsList.getBadgeNumberByRoomName(roomTest);

		//2. Receive a message in a room from an other user.
		HttpsRequestsToMatrix.sendMessageInRoom(riotSenderAccessToken, roomId, msgFromUpUser);
		if(currentBadge==null)currentBadge=0;
		//wait until message is received
		riotRoomsList.waitForRoomToReceiveNewMessage(roomTest, currentBadge);
		//Asserts that badge is set to 1 or incremented on the room's item in the rooms list
		Assert.assertNotNull(riotRoomsList.getBadgeNumberByRoomName(roomTest), "There is no badge on this room.");
		Assert.assertEquals((int)riotRoomsList.getBadgeNumberByRoomName(roomTest),currentBadge+1, "Badge number wasn't incremented after receiving the message");	
		//Assertion on the message.
		Assert.assertEquals(riotRoomsList.getLastEventByRoomName(roomTest,false), msgFromUpUser, "Received message on the room item isn't the same as sended by matrix.");
	}

	/**
	 * TODO : write this test
	 * Required : user must be logged in room </br>
	 * Set the notifications off on the room </br>
	 * Receive a text message in a room from an other user. </br>
	 * Asserts that no badge appears after receiving the message.</br>
	 */
	@Test(enabled=false)
	public void checkNoBadgeOnMessageReceptionWithNootificationsOff(){

	}
	/**
	 * Receive a text message in a room from an other user. </br>
	 * 1. Open room roomName
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.</br>
	 * Asserts that badge isn't displayed anymore on the room item when going back to rooms list.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(dependsOnGroups="messageReceivedInList",priority=2,groups={"roomOpenned","1checkuser","1driver_ios"})
	public void checkTextMessageOnRoomPage() throws InterruptedException{
		//1. Open room roomName
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		riotRoomsList.getRoomByName(roomTest).click();
		//check that lately sended message is the last displayed in the room
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		MobileElement lastPost= testRoom.getLastBubble();
		Assert.assertTrue(testRoom.getTextViewFromBubble(lastPost).getText().contains(msgFromUpUser), "Last bubble doesn't contains msg sent by riotuserup.");
	}

	/**
	 * 1. Send message in a room.</br>
	 * Check that timestamp is displayed on the last post.</br>
	 * Check that timestamp is not displayed on the before last post.</br>
	 * 2. Select an other post that the last </br>
	 * Check that when a post is selected, timestamp is displayed.
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",groups={"1checkuser","1driver_ios"},priority=3)
	public void checkTimeStampPositionOnRoomPage() throws InterruptedException{
		String message="test for timestamp display";
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		//1. Send message in a room.
		testRoom.sendAMessage(message);Thread.sleep(500);
		appiumFactory.getiOsDriver1().hideKeyboard();
		//Check that timestamp is displayed on the last post.
		MobileElement lastPost= testRoom.getLastBubble();
		Assert.assertNotNull(testRoom.getTimeStampByBubble(lastPost), "Last message have no timestamp");
		Assert.assertTrue(testRoom.getTimeStampByBubble(lastPost).getText().length()>=5, "Last message timestamp seems bad.");

		//Check that timestamp is not displayed on the before last post.
		int beforeLastPostPosition=testRoom.bubblesList.size()-2;
		MobileElement beforeLastPost=  testRoom.bubblesList.get(beforeLastPostPosition);
		Assert.assertNull(testRoom.getTimeStampByBubble(beforeLastPost), "Before last message have timestamp and should not.");

		//2. Select an other post that the last 
		testRoom.getTextViewFromBubble(beforeLastPost).click();
		//Check that when a post is selected, timestamp is displayed.
		Assert.assertNotNull(testRoom.getTimeStampByBubble(beforeLastPost), "Before last message have no timestamp");
		Assert.assertTrue(testRoom.getTimeStampByBubble(beforeLastPost).getText().length()>=5, "Before last message timestamp seems bad.");
	}

	/**
	 * 1. Add a room in favorites from the rooms list. </br>
	 * Check that the room is added in the favorites category. </br>
	 * 2. Remove the room from the favorites </br>
	 * Check that the room is no more in the favorites.
	 * @throws InterruptedException
	 */
	@Test(groups={"1checkuser","1driver_ios"},priority=1)
	public void addRoomInFavorites() throws InterruptedException{
		RiotRoomsListPageObjects roomslist= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		roomslist.clickOnSwipedMenuOnRoom(roomTest, "favourite");
		Thread.sleep(1000);
		Assert.assertTrue(roomslist.checkRoomInCategory(roomTest, "FAVOURITES"), "Room "+roomTest+" isn't added in the FAVORITES category");
		roomslist.clickOnSwipedMenuOnRoom(roomTest, "favourite");
		Thread.sleep(1000);
		Assert.assertFalse(roomslist.checkRoomInCategory(roomTest, "FAVOURITES"), "Room "+roomTest+" isn't added in the FAVORITES category");
	}
	/**
	 * TODO
	 * Check that a thumbnail is present when a photo is uploaded, instead of the full resolution photo.
	 */
	@Test(enabled=false)
	public void checkUploadedPhotoThumbnail(){

	}

	@BeforeGroups("1checkuser")
	private void checkIfUser1Logged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedIos(appiumFactory.getiOsDriver1(), riotUserDisplayNameA, Constant.DEFAULT_USERPWD);
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
	@BeforeGroups("2checkuser")
	private void checkIfUsersLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedIos(appiumFactory.getiOsDriver1(), riotUserDisplayNameA, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedIos(appiumFactory.getiOsDriver2(), riotUserDisplayNameB, Constant.DEFAULT_USERPWD);
	}

	/**
	 * Log riotuserup to get his access token. </br> Mandatory to send http request with it.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@BeforeGroups("1checkuser")
	private void renewRiotInviterAccessToken() throws IOException, InterruptedException{
		System.out.println("Log "+riotSenderUserDisplayName+" to get a new AccessToken.");
		riotSenderAccessToken=HttpsRequestsToMatrix.login(riotSenderUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
