package mobilestests_ios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSTouchAction;
import pom_ios.RiotMemberDetailsPageObjects;
import pom_ios.RiotRoomDetailsPageObjects;
import pom_ios.RiotRoomPageObjects;
import pom_ios.main_tabs.RiotHomePageTabObjects;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.MatrixUtilities;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests about administration and moderation features in Riot.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotAdminAndModerationTests extends RiotParentTest{
	private String riotUserADisplayName="riotuser14";
	private String riotUserBDisplayName="riotuser15";
	private String riotUserCDisplayName="riotuser16";
	private String riotUserAAccessToken, riotUserBAccessToken,riotUserCAccessToken;
	private String testRoomId;
	/**
	 * TODO
	 * Cover issue https://github.com/vector-im/riot-ios/issues/1178.</br>
	 * 1. Create room R with user A.</br>
	 * 2. Invite user B. </br>
	 * 3. Accept invitation with user B. </br>
	 * 4. Open user B details page and ban him.  </br>
	 * Check that user B is banned. </br>
	 * 5. Unban user B </br>
	 * Check that user B is unbanned. </br>
	 * 6. Invite user B </br>
	 * 7. Accept invitation with user B. </br>
	 * Check on user A device that user B has actually joined the room.
	 */
	@Test(groups={"1driver_ios","22checkuser_ios"})
	public void banAndUnbanUserTest() throws IOException, InterruptedException{
		String tmpRoomName="ban temp room";
		//1. 2. 3
		//createRoomWith2MembersByRequestsToMatrix(tmpRoomName);

		//4. Open user B details page and ban him.
		//Opening the created room with Riot 
		RiotHomePageTabObjects hpA = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		//RiotRoomsListPageObjects roomsListB=new RiotRoomsListPageObjects(appiumFactory.getiOsDriver2());
		hpA.getRoomByName(tmpRoomName).click();
		//roomsListB.getRoomByName(tmpRoomName).click();
		RiotRoomPageObjects roomPageA = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		//		roomPageA.openDetailView();
		//		RiotRoomDetailsPageObjects roomDetailsA = new RiotRoomDetailsPageObjects(appiumFactory.getiOsDriver1());
		//		roomDetailsA.getMemberByName(riotUserBDisplayName).click();
		//		RiotMemberDetailsPageObjects memberPageUserB = new RiotMemberDetailsPageObjects(appiumFactory.getiOsDriver1());
		//		//Ban user B
		//		MobileElement banActionItem=memberPageUserB.getActionItemByName("Ban from this room");
		//		Assert.assertNotNull(banActionItem, "There is no Ban action visible on user B's member details page.");
		//		banActionItem.click();
		//		
		//		//Check that user B is banned.
		//		memberPageUserB.menuBackButton.click();
		//		//		in the room details page
		//		Assert.assertEquals(roomDetailsA.membersList.size(),1, "There should be 1 member in the list because 1 of 2 was banned.");
		//		roomDetailsA.menuBackButton.click();
		//		//		in the room page
		//		Assert.assertTrue(roomPageA.getTextViewFromBubble(roomPageA.getLastBubble()).getText().contains(riotUserADisplayName+" banned "+riotUserBDisplayName));
		//		
		//		//5. Unban user B
		//roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2).click();
		MobileElement beforeLastBubble= roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2);
		int x=beforeLastBubble.getLocation().getX();
		int y= beforeLastBubble.getLocation().getY();

		System.out.println(x+" , "+y);
		appiumFactory.getiOsDriver1().performTouchAction(new IOSTouchAction(appiumFactory.getiOsDriver1()).tap(x, y));
		new IOSTouchAction(appiumFactory.getiOsDriver1()).tap(beforeLastBubble);
		new IOSTouchAction(appiumFactory.getiOsDriver1()).tap(beforeLastBubble,100,10);
		new IOSTouchAction(appiumFactory.getiOsDriver1()).tap(beforeLastBubble,100,10);
		new IOSTouchAction(appiumFactory.getiOsDriver1()).tap(40, 260);
		new IOSTouchAction(appiumFactory.getiOsDriver1()).tap(200, 275);

		new TouchAction(appiumFactory.getiOsDriver1()).tap(beforeLastBubble,100,10);
		new TouchAction(appiumFactory.getiOsDriver1()).tap(40, 260);
		new TouchAction(appiumFactory.getiOsDriver1()).tap(200, 275);

		//		beforeLastBubble.click();
		//		appiumFactory.getiOsDriver1().tap(1,x,y,100);
		//		new IOSTouchAction(appiumFactory.getiOsDriver1()).tap(beforeLastBubble);
		//		new IOSTouchAction(appiumFactory.getiOsDriver1()).press(appiumFactory.getiOsDriver1().findElementByAccessibilityId("search icon"));
		//appiumFactory.getiOsDriver1().tap(1, roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2).getLocation().x, roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2).getLocation().y, 100);
		//		new TouchAction(appiumFactory.getiOsDriver1()).tap(roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2));
		//		new TouchAction(appiumFactory.getiOsDriver1()).tap(roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2),0,0);
		//		new TouchAction(appiumFactory.getiOsDriver1()).press(roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2),10,10);
		//		new TouchAction(appiumFactory.getiOsDriver1()).longPress(roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2),10,10);
		//roomPageA.bubblesList.get(roomPageA.bubblesList.size()-2).get

	}

	/**
	 * TODO
	 * 1. Create room R with user A.</br>
	 * 2. Invite user B. </br>
	 * 3. Accept invitation with user B. </br>
	 * 4. Open user B details page and kick him.  </br>
	 * Check that user B is kicked. </br>
	 * 5. Invite user B </br>
	 * Check that user B is invited. </br>
	 * 6. Accept invitation with user B. </br>
	 * Check on user A device that user B has actually joined the room.
	 */
	@Test(groups={"2drivers_ios","2checkuser_ios"})
	public void kickAndInviteUserTest() throws IOException, InterruptedException{
		String tmpRoomName="kick temp room";
		//1. 2. 3
		createRoomWith2MembersByRequestsToMatrix(tmpRoomName);
		//Opening the created room with Riot
		RiotHomePageTabObjects hpA = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		RiotHomePageTabObjects hpB = new RiotHomePageTabObjects(appiumFactory.getiOsDriver2());
		hpA.getRoomByName(tmpRoomName).click();
		hpB.getRoomByName(tmpRoomName).click();
		RiotRoomPageObjects roomPageA=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		roomPageA.openDetailView();
		RiotRoomDetailsPageObjects roomDetailsA = new RiotRoomDetailsPageObjects(appiumFactory.getiOsDriver1());

		//4. Open user B details page and ban him.
		roomDetailsA.getMemberByName(riotUserBDisplayName).click();
		RiotMemberDetailsPageObjects memberPageUserB=new RiotMemberDetailsPageObjects(appiumFactory.getiOsDriver1());
		//Ban user B
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Remove from this room"), "There is no Kick action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Remove from this room").click();

		//Check that user B is kicked.
		memberPageUserB.menuBackButton.click();
		//	in the room details page
		Assert.assertEquals(roomDetailsA.membersList.size(),1, "There should be 1 member in the list because 1 of 2 was banned.");
		roomDetailsA.menuBackButton.click();
		//		in the room page
		Assert.assertTrue(roomPageA.getTextViewFromBubble(roomPageA.getLastBubble()).getText().contains(riotUserADisplayName+" kicked "+riotUserBDisplayName));

		//5. Invite user B
		//roomPageA.getUserAvatarByPost(roomPageA.getBubbleByIndex(roomPageA.bubblesList.size()-2)).click();
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Invite"), "There is no Invite action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Invite").click();
		//Check that user B is invited.
		memberPageUserB.menuBackButton.click();
		Assert.assertTrue(roomPageA.getTextViewFromBubble(roomPageA.getLastBubble()).getText().contains(riotUserADisplayName+" invited "+riotUserBDisplayName));

		//6. Accept invitation with user B. 
		hpB.previewInvitation(tmpRoomName);
		RiotRoomPageObjects roomPageB = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		roomPageB.joinRoomButton.click();
		roomPageB.waitForBubblesToBeDisplayed();

		//Check on user A device that user B has actually joined the room
		memberPageUserB.menuBackButton.click();
		Assert.assertTrue(roomPageA.getTextViewFromBubble(roomPageA.getLastBubble()).getText().contains(riotUserBDisplayName+" joined"));
	}

	/**
	 * TODO
	 * 1. Create room R with user A.</br>
	 * 2. Invite user B. </br>
	 * 3. Invite user C. </br>
	 * 4. Accept invitation with user B. </br>
	 * 5. Accept invitation with user C. </br>
	 * 6. Make  user B moderator </br>
	 * 7. Open user C details page with user B </br>
	 * With user B, check that "remove from this room" and "ban" options are present </br>
	 * 8. Kick user C with user B </br>
	 * Check that user C is kicked with user A. </br>
	 * 9. Open user A details page with user B. </br>
	 * Check that user B doesn't have any moderator or admin privileges.
	 */
	@Test(groups={"2drivers_ios","2checkuser_ios"})
	public void makeMemberModeratorTest() throws IOException, InterruptedException{

	}

	/**
	 * TODO
	 * 1. Create room R with user A.</br>
	 * 2. Invite user B. </br>
	 * 3. Accept invitation with user B. </br>
	 * 4. Open user B details page with user A and make him admin. </br>
	 * Check that a confirmation dialog is opened. </br>
	 * 5. Hit YES button.  </br>
	 * Check that user A lost admin privileges on user B page (no more 'Make moderator' and 'Make admin' items). </br>
	 * 6. Open user A details page with user B </br>
	 * Check that user B don't have any admin privileges on user A page (no 'Make moderator' and 'Make admin' items). </br>
	 * 7. Open user B details page with user B </br>
	 * 8. Hit 'Reset to normal user' item action </br>
	 * With user A check on user B's details page that he retrieves his privileges on user B (items 'Make moderator' and 'Make admin' items). </br>
	 */
	@Test(groups={"2drivers_ios","2checkuser_ios"})
	public void makeMemberAdminTest() throws IOException, InterruptedException{

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
	@BeforeGroups("2checkuser_ios")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotUserADisplayName, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver2(), riotUserBDisplayName, Constant.DEFAULT_USERPWD);
	}

	/**
	 * Create a room and invite user B by using https requests to home server.
	 * @param roomName
	 * @throws IOException 
	 */
	private void createRoomWith2MembersByRequestsToMatrix(String roomName) throws IOException{
		//1. Create room R with user A.
		riotUserAAccessToken=HttpsRequestsToMatrix.login(riotUserADisplayName, Constant.DEFAULT_USERPWD);
		testRoomId=HttpsRequestsToMatrix.createRoom(riotUserAAccessToken, roomName);
		//2. Invite user B. 
		HttpsRequestsToMatrix.sendInvitationToUser(riotUserAAccessToken, testRoomId, MatrixUtilities.getMatrixIdFromDisplayName(riotUserBDisplayName));
		//3. Accept invitation with user B.
		riotUserBAccessToken=HttpsRequestsToMatrix.login(riotUserBDisplayName, Constant.DEFAULT_USERPWD);
		HttpsRequestsToMatrix.joinRoom(riotUserBAccessToken, testRoomId);
	}

	/**
	 * Create a room and invite user B by using https requests to home server.
	 * @param roomName
	 * @throws IOException 
	 */
	private void createRoomWith3MembersByRequestsToMatrix(String roomName) throws IOException{
		//1. Create room R with user A.
		riotUserAAccessToken=HttpsRequestsToMatrix.login(riotUserADisplayName, Constant.DEFAULT_USERPWD);
		testRoomId=HttpsRequestsToMatrix.createRoom(riotUserAAccessToken, roomName);
		System.out.println(testRoomId);
		//2. Invite user B. 
		HttpsRequestsToMatrix.sendInvitationToUser(riotUserAAccessToken, testRoomId, MatrixUtilities.getMatrixIdFromDisplayName(riotUserBDisplayName));
		//3. Invite user C.
		HttpsRequestsToMatrix.sendInvitationToUser(riotUserAAccessToken, testRoomId, MatrixUtilities.getMatrixIdFromDisplayName(riotUserCDisplayName));
		//4. Accept invitation with user B.
		riotUserBAccessToken=HttpsRequestsToMatrix.login(riotUserBDisplayName, Constant.DEFAULT_USERPWD);
		HttpsRequestsToMatrix.joinRoom(riotUserBAccessToken, testRoomId);
		//4. Accept invitation with user C.
		riotUserCAccessToken=HttpsRequestsToMatrix.login(riotUserCDisplayName, Constant.DEFAULT_USERPWD);
		HttpsRequestsToMatrix.joinRoom(riotUserCAccessToken, testRoomId);
	}

	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException, IOException{
		switch (m.getName()) {
		case "banAndUnbanUserTest":
			leaveAndForgetRoomWith2Users();
			break;
		case "kickAndInviteUserTest":
			leaveAndForgetRoomWith2Users();
			break;
		case "makeMemberModeratorTest":
			leaveAndForgetRoomWith3Users();
			break;
		case "makeMemberAdminTest":
			leaveAndForgetRoomWith2Users();
			break;
		default:
			break;
		}
	}

	private void leaveAndForgetRoomWith2Users() throws IOException{
		//leave room user A
		HttpsRequestsToMatrix.leaveRoom(riotUserAAccessToken, testRoomId);
		//leave room user B
		HttpsRequestsToMatrix.leaveRoom(riotUserBAccessToken, testRoomId);
		//forget room user A
		HttpsRequestsToMatrix.forgetRoom(riotUserAAccessToken, testRoomId);
		//forget room user B
		HttpsRequestsToMatrix.forgetRoom(riotUserBAccessToken, testRoomId);
	}

	private void leaveAndForgetRoomWith3Users() throws IOException{
		//leave room user A
		HttpsRequestsToMatrix.leaveRoom(riotUserAAccessToken, testRoomId);
		//leave room user B
		HttpsRequestsToMatrix.leaveRoom(riotUserBAccessToken, testRoomId);
		//leave room user C
		//HttpsRequestsToMatrix.leaveRoom(riotUserCAccessToken, testRoomId);
		//forget room user A
		HttpsRequestsToMatrix.forgetRoom(riotUserAAccessToken, testRoomId);
		//forget room user B
		HttpsRequestsToMatrix.forgetRoom(riotUserBAccessToken, testRoomId);
		//forget room user C
		HttpsRequestsToMatrix.forgetRoom(riotUserCAccessToken, testRoomId);
	}
}
