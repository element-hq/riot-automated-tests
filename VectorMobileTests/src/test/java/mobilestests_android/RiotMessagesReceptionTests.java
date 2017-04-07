package mobilestests_android;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.MobileElement;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotMessagesReceptionTests extends RiotParentTest{
	private String msgFromUpUser="UP";
	private String roomId="!SBpfTGBlKgELgoLALQ%3Amatrix.org";
	private String pictureURL="mxc://matrix.org/gpQYPbjoqVeTWCGivjRshIni";
	private String roomTest="msg rcpt 4 automated tests";
	private String riotUserDisplayNameA="riotuser4";
	private String riotUserDisplayNameB="riotuser5";
	private String riotSenderUserDisplayName="riotuserup";
	private String riotSenderAccessToken;
	
	/**
	 * Receive a message in a room from an other user. </br>
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.</br>
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(groups={"messageReceivedInList","1checkuser","1driver_android"},priority=1)
	public void checkBadgeAndMessageOnRoomItem() throws InterruptedException, IOException{
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//get the current badge on the room.
		Integer currentBadge=riotRoomsList.getBadgeNumberByRoomName(roomTest);
		//send a message to the room with an other user using https request to matrix.
		HttpsRequestsToMatrix.sendMessageInRoom(riotSenderAccessToken, roomId, msgFromUpUser);
		if(currentBadge==null)currentBadge=0;
		//wait until message is received
		riotRoomsList.waitForRoomToReceiveNewMessage(roomTest, currentBadge);
		//Assertion on the badge
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
	 * Required : user must be logged in room </br>
	 * Receive a text message in a room from an other user. </br>
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.</br>
	 * Asserts that badge isn't displayed anymore on the room item when going back to rooms list.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(dependsOnGroups="messageReceivedInList",priority=2,groups={"roomOpenned","1checkuser","1driver_android"})
	public void checkTextMessageOnRoomPage() throws InterruptedException{
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//open room
		riotRoomsList.getRoomByName(roomTest).click();
		//check that lately sended message is the last displayed in the room
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		MobileElement lastPost= testRoom.getLastPost();
		Assert.assertEquals(testRoom.getTextViewFromPost(lastPost).getText(), msgFromUpUser);
	}
	
	/**
	 * Send message in a room.</br>
	 * Check that timestamp is only displayed on the last post.</br>
	 * Check that when a post is selected, timestamp is displayed.
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",groups={"1checkuser","1driver_android"},priority=3)
	public void checkTimeStampPositionOnRoomPage() throws InterruptedException{
		String message="test for timestamp display";
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		//send message
		testRoom.sendAMessage(message);Thread.sleep(500);
		//appiumFactory.getAppiumDriver().hideKeyboard();
		//check that timestamp is displayed on this message
		MobileElement lastPost= testRoom.getLastPost();
		Assert.assertNotNull(testRoom.getTimeStampByPost(lastPost), "Last message have no timestamp");
		Assert.assertTrue(testRoom.getTimeStampByPost(lastPost).getText().length()>=5, "Last message timestamp seems bad.");
		//check that before last message have not timestamp
		int beforeLastPostPosition=testRoom.postsListLayout.size()-2;
		MobileElement beforeLastPost=  testRoom.postsListLayout.get(beforeLastPostPosition);
		Assert.assertNull(testRoom.getTimeStampByPost(beforeLastPost), "Before last message have timestamp and should not.");
		//select before last message
		testRoom.getTextViewFromPost(beforeLastPost).click();
		//check that the timestamp is displayed
		//System.out.println(testRoom.getTextViewFromPost(beforeLastPost).getText());
		Assert.assertNotNull(testRoom.getTimeStampByPost(beforeLastPost), "Before last message have no timestamp");
		Assert.assertTrue(testRoom.getTimeStampByPost(beforeLastPost).getText().length()>=5, "Before last message timestamp seems bad.");
	}
	
	/**
	 * Receive a message by an other user in a room.</br>
	 * Send a first message. Check that avatar is displayed on the post.</br>
	 * Send a second message. Check that avatar is not displayed on the post.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",groups={"1checkuser","1driver_android"},priority=4)
	public void checkAvatarDisplayInRoomPage() throws IOException, InterruptedException{
		String messageTest="hello from sender";
		String messageTest2="this message have an avatar";
		String messageTest3="this message doesn't have an avatar";
		//send a message to the room with an other user using https request to matrix.
		HttpsRequestsToMatrix.sendMessageInRoom(riotSenderAccessToken, roomId, messageTest);
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		testRoom.sendAMessage(messageTest2);Thread.sleep(500);
		Assert.assertNotNull(testRoom.getUserAvatarByPost(testRoom.getLastPost()), "The last post doesn't have an avatar and should because it's the first post from the user");
		testRoom.sendAMessage(messageTest3);
		Assert.assertNull(testRoom.getUserAvatarByPost(testRoom.getLastPost()), "The last post have an avatar and shouldn't because it's the second post from the user");
	}
	
	/**
	 * 1. Receive a image sent by an other user.</br>
	 * Check that image is correctly uploaded.</br>
	 * Check that a timestamp is visible on the last post.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",groups={"1checkuser","1driver_android"},priority=5)
	public void checkImageMessageOnRoomPage() throws IOException, InterruptedException{
		//1. Receive a image sent by an other user.
		HttpsRequestsToMatrix.sendPicture(riotSenderAccessToken, roomId, pictureURL);
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		Thread.sleep(500);
		MobileElement lastPost=testRoom.getLastPost();
		MobileElement uploadPicture = testRoom.getAttachedImageByPost(lastPost);
		//get dimensions of the uploaded image
		org.openqa.selenium.Dimension riotLogoDim=uploadPicture.getSize();
	    Assert.assertTrue(riotLogoDim.height!=0 && riotLogoDim.width!=0, "Uploaded picture seems empty");
	    Assert.assertNotNull(testRoom.getTimeStampByPost(lastPost), "Last post doesn't have a timestamp");
	}

	/**
	 * Add a room in favorites from the rooms list. </br>
	 * Check that the room is added in the favorites category. </br>
	 * Remove the room from the favorites </br>
	 * Check that the room is no more in the favorites.
	 * @throws InterruptedException
	 */
	@Test(groups={"1checkuser","1driver_android"},priority=1)
	public void addRoomInFavorites() throws InterruptedException{
		RiotRoomsListPageObjects roomslist= new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//add room in favourites
		roomslist.clickOnContextMenuOnRoom(roomTest, "Favourite");
		Thread.sleep(2000);
		Assert.assertTrue(roomslist.checkRoomInCategory(roomTest, "FAVORITES"), "Room "+roomTest+" isn't added in the FAVORITES category");
		roomslist.clickOnContextMenuOnRoom(roomTest, "Favourite");
		Assert.assertFalse(roomslist.checkRoomInCategory(roomTest, "FAVORITES"), "Room "+roomTest+" is in the FAVORITES category and should not");
	}
	
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
	@Test(groups={"2drivers_android","2checkuser"},priority=0)
	public void typingIndicatorTest() throws InterruptedException{
		String notSentMsg="tmp";
		RiotRoomsListPageObjects roomsListA = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListB= new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());

		//1. Open roomtest with device A.
		roomsListA.getRoomByName(roomTest).click();
		RiotRoomPageObjects roomA=new  RiotRoomPageObjects(appiumFactory.getAndroidDriver1());

		//2. Open roomtest with device B.		
		roomsListB.getRoomByName(roomTest).click();
		RiotRoomPageObjects roomB=new  RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		
		//3. User A write something in the message bar but don't send it.
		roomA.messageZoneEditText.setValue(notSentMsg);
		//Test that the typing indicator indicates '[user1] is typing..." with device B.
		Assert.assertEquals(roomB.notificationMessage.getText(), riotUserDisplayNameA+" is typing…");
		Assert.assertTrue(roomB.notificationMessage.isDisplayed(),"Typing indicator isn't displayed on device B");
		
		//4. Type an other msg and clear it with user 4 in the message bar.
		roomA.messageZoneEditText.setValue(notSentMsg);
		roomA.messageZoneEditText.clear();
		Thread.sleep(1000);
		//Test that the typing indicator is empty on device B.
		Assert.assertFalse(isPresentTryAndCatch(roomB.notificationMessage),"Typing indicator is displayed on device B and shouldn't because device A isn't typing");
		//come back to rooms list
		roomA.menuBackButton.click();
		roomB.menuBackButton.click();
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
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayNameA, Constant.DEFAULT_USERPWD);
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
	private void checkIfUser2Logged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayNameA, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver2(), riotUserDisplayNameB, Constant.DEFAULT_USERPWD);
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
