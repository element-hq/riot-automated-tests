package mobilestests_android;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotMessagesReceptionTests extends RiotParentTest{
	private String senderAccesToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI5Y2lkIHVzZXJfaWQgPSBAcmlvdHVzZXJ1cDptYXRyaXgub3JnCjAwMTZjaWQgdHlwZSA9IGFjY2VzcwowMDIxY2lkIG5vbmNlID0gLDV4QF5rQ0UuZERNNV9HeQowMDJmc2lnbmF0dXJlIM2bpjf8d6LoAkV4CZqmCjZjDWTVVyefC16_ts_SbSvGCg";  
	private String msgFromUpUser="UP";
	private String roomId="!SBpfTGBlKgELgoLALQ%3Amatrix.org";
	private String roomTest="msg rcpt 4 automated tests";
	private String riotUserDisplayNameA="riotuser4";
	private String riotUserDisplayNameB="riotuser5";
	
	/**
	 * Required : user must be logged in room and notifications are On on this room </br>
	 * Receive a message in a room from an other user. </br>
	 * Asserts that badge is set to 1 or incremented on the room's item in the rooms list.</br>
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@Test(groups={"messageReceivedInList","1checkuser","1driver_android"},priority=1)
	public void checkBadgeAndMessageOnRoomItem() throws InterruptedException, IOException{
		//TODO invite user in the room if room not present
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//get the current badge on the room.
		Integer currentBadge=riotRoomsList.getBadgeNumberByRoomName(roomTest);
		//send a message to the room with an other user using https request to matrix.
		HttpsRequestsToMatrix.sendMessageInRoom(senderAccesToken, roomId, msgFromUpUser);
		if(currentBadge==null)currentBadge=0;
		//wait until message is received
		riotRoomsList.waitForRoomToReceiveNewMessage(roomTest, currentBadge);
		//Assertion on the badge
		Assert.assertNotNull(riotRoomsList.getBadgeNumberByRoomName(roomTest), "There is no badge on this room.");
		Assert.assertEquals((int)riotRoomsList.getBadgeNumberByRoomName(roomTest),currentBadge+1, "Badge number wasn't incremented after receiving the message");	
		//Assertion on the message.
		Assert.assertEquals(riotRoomsList.getReceivedMessageByRoomName(roomTest), msgFromUpUser, "Received message on the room item isn't the same as sended by matrix.");
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
		HttpsRequestsToMatrix.sendMessageInRoom(senderAccesToken, roomId, messageTest);
		RiotRoomPageObjects testRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		testRoom.sendAMessage(messageTest2);Thread.sleep(500);
		Assert.assertNotNull(testRoom.getUserAvatarByPost(testRoom.getLastPost()), "The last post doesn't have an avatar and should because it's the first post from the user");
		testRoom.sendAMessage(messageTest3);
		Assert.assertNull(testRoom.getUserAvatarByPost(testRoom.getLastPost()), "The last post have an avatar and shouldn't because it's the second post from the user");
	}
	
	/**
	 * Open a room, and receive a image sent by an other user.</br>
	 * Check that image is correctly uploaded.</br>
	 * Check that a timestamp is visible on the last post.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(dependsOnGroups="roomOpenned",groups={"1checkuser","1driver_android"},priority=5)
	public void checkImageMessageOnRoomPage() throws IOException, InterruptedException{
		String pictureURL="mxc://matrix.org/gpQYPbjoqVeTWCGivjRshIni";
		//send picture of already uploaded picture
		HttpsRequestsToMatrix.sendPicture(senderAccesToken, roomId, pictureURL);
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
	@Test(groups={"1checkuser","1driver_android"})
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
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUserLogged() throws InterruptedException{
		super.checkIfUserLoggedAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayNameA, Constant.DEFAULT_USERPWD);
	}
	
}
