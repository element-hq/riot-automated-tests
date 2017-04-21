package mobilestests_android;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_android.RiotMemberDetailsPageObjects;
import pom_android.RiotRoomDetailsPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
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
	 * Cover issue https://github.com/vector-im/riot-android/issues/938.</br>
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
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkusers_android"})
	public void banAndUnbanUserTest() throws IOException, InterruptedException{
		String tmpRoomName="ban temp room";
		//1. 2. 3
		createRoomWith2MembersByRequestsToMatrix(tmpRoomName);

		//4. Open user B details page and ban him.
		//Opening the created room with Riot 
		RiotRoomsListPageObjects roomsListA=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListB=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		roomsListA.getRoomByName(tmpRoomName).click();
		roomsListB.getRoomByName(tmpRoomName).click();
		RiotRoomPageObjects roomPageA=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPageA.roomNameTextView.click();
		roomPageA.activeMembersTextView.click();
		RiotRoomDetailsPageObjects roomDetailsA = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());
		roomDetailsA.getMemberByName(riotUserBDisplayName).click();
		RiotMemberDetailsPageObjects memberPageUserB=new RiotMemberDetailsPageObjects(appiumFactory.getAndroidDriver1());
		//Ban user B
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Ban"), "There is no Ban action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Ban").click();

		//Check that user B is banned.
		memberPageUserB.menuBackButton.click();
		//	in the room details page
		Assert.assertEquals(roomDetailsA.membersList.size(),1, "There should be 1 member in the list because 1 of 2 was banned.");
		roomDetailsA.menuBackButton.click();
		//		in the room page
		Assert.assertEquals(roomPageA.getTextViewFromPost(roomPageA.getLastPost()).getText(), riotUserADisplayName+" banned "+riotUserBDisplayName);

		//5. Unban user B
		roomPageA.getUserAvatarByPost(roomPageA.getPostByIndex(roomPageA.postsListLayout.size()-2)).click();
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Unban"), "There is no Unban action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Unban").click();
		//Check that user B is unbanned.
		memberPageUserB.menuBackButton.click();
		Assert.assertEquals(roomPageA.getTextViewFromPost(roomPageA.getLastPost()).getText(), riotUserADisplayName+" unbanned "+riotUserBDisplayName);

		//6. Invite user B
		roomPageA.getUserAvatarByPost(roomPageA.getPostByIndex(roomPageA.postsListLayout.size()-3)).click();
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Invite"), "There is no Invite action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Invite").click();

		//7. Accept invitation with user B. 
		roomsListB.previewInvitation(tmpRoomName);
		RiotRoomPageObjects roomPageB = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		roomPageB.joinRoomButton.click();
		roomPageB.waitForPostsToBeDisplayed();

		//Check on user A device that user B has actually joined the room.
		memberPageUserB.menuBackButton.click();
		Assert.assertEquals(roomPageA.getTextViewFromPost(roomPageA.getLastPost()).getText(), riotUserBDisplayName+" joined");
	}

	/**
	 * 1. Create room R with user A.</br>
	 * 2. Invite user B. </br>
	 * 3. Accept invitation with user B. </br>
	 * 4. Open user B details page and kick him.  </br>
	 * Check that user B is kicked. </br>
	 * 5. Invite user B </br>
	 * Check that user B is invited. </br>
	 * 6. Accept invitation with user B. </br>
	 * Check on user A device that user B has actually joined the room.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkusers_android"})
	public void kickAndInviteUserTest() throws IOException, InterruptedException{
		String tmpRoomName="kick temp room";
		//1. 2. 3
		createRoomWith2MembersByRequestsToMatrix(tmpRoomName);
		//Opening the created room with Riot 
		RiotRoomsListPageObjects roomsListA=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListB=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		roomsListA.getRoomByName(tmpRoomName).click();
		roomsListB.getRoomByName(tmpRoomName).click();
		RiotRoomPageObjects roomPageA=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPageA.roomNameTextView.click();
		roomPageA.activeMembersTextView.click();
		RiotRoomDetailsPageObjects roomDetailsA = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());

		//4. Open user B details page and ban him.
		roomDetailsA.getMemberByName(riotUserBDisplayName).click();
		RiotMemberDetailsPageObjects memberPageUserB=new RiotMemberDetailsPageObjects(appiumFactory.getAndroidDriver1());
		//Ban user B
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Remove from this room"), "There is no Kick action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Remove from this room").click();

		//Check that user B is kicked.
		memberPageUserB.menuBackButton.click();
		//	in the room details page
		Assert.assertEquals(roomDetailsA.membersList.size(),1, "There should be 1 member in the list because 1 of 2 was banned.");
		roomDetailsA.menuBackButton.click();
		//		in the room page
		Assert.assertEquals(roomPageA.getTextViewFromPost(roomPageA.getLastPost()).getText(), riotUserADisplayName+" kicked "+riotUserBDisplayName);

		//5. Invite user B
		roomPageA.getUserAvatarByPost(roomPageA.getPostByIndex(roomPageA.postsListLayout.size()-2)).click();
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Invite"), "There is no Invite action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Invite").click();
		//Check that user B is invited.
		memberPageUserB.menuBackButton.click();
		Assert.assertEquals(roomPageA.getTextViewFromPost(roomPageA.getLastPost()).getText(), riotUserADisplayName+" invited "+riotUserBDisplayName);

		//6. Accept invitation with user B. 
		roomsListB.previewInvitation(tmpRoomName);
		RiotRoomPageObjects roomPageB = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		roomPageB.joinRoomButton.click();
		roomPageB.waitForPostsToBeDisplayed();

		//Check on user A device that user B has actually joined the room
		memberPageUserB.menuBackButton.click();
		Assert.assertEquals(roomPageA.getTextViewFromPost(roomPageA.getLastPost()).getText(), riotUserBDisplayName+" joined");
	}

	/**
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
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","22checkusers_android"})
	public void makeMemberModeratorTest() throws IOException, InterruptedException{
		String tmpRoomName="moderator temp room";
		//1. 2. 3. 4. 5.
		createRoomWith3MembersByRequestsToMatrix(tmpRoomName);
		//Opening the created room with Riot 
		RiotRoomsListPageObjects roomsListA=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomsListPageObjects roomsListB=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver2());
		roomsListA.getRoomByName(tmpRoomName).click();
		roomsListB.getRoomByName(tmpRoomName).click();
		RiotRoomPageObjects roomPageA=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPageA.roomNameTextView.click();
		roomPageA.activeMembersTextView.click();
		RiotRoomDetailsPageObjects roomDetailsA = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());

		//6. Make  user B moderator
		roomDetailsA.getMemberByName(riotUserBDisplayName).click();
		RiotMemberDetailsPageObjects memberPageUserB=new RiotMemberDetailsPageObjects(appiumFactory.getAndroidDriver1());
		//Make user B moderator
		Assert.assertNotNull(memberPageUserB.getActionItemByName("Make moderator"), "There is no make moderator action visible on user B's member details page.");
		memberPageUserB.getActionItemByName("Make moderator").click();
		
		//7. Open user C details page with user B
		RiotRoomPageObjects roomPageB=new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		roomPageB.roomNameTextView.click();
		roomPageB.activeMembersTextView.click();
		RiotRoomDetailsPageObjects roomDetailsB = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver2());
		roomDetailsB.getMemberByName(riotUserCDisplayName).click();
		RiotMemberDetailsPageObjects memberPageUserC=new RiotMemberDetailsPageObjects(appiumFactory.getAndroidDriver2());
		
		//With user B, check that "remove from this room" and "ban" options are present
		Assert.assertNotNull(memberPageUserC.getActionItemByName("Remove from this room"), "There is no Kick action visible on user B's member details page.");
		Assert.assertNotNull(memberPageUserC.getActionItemByName("Ban"), "Ban");
		
		//8. Kick user C with user B
		memberPageUserC.getActionItemByName("Remove from this room").click();
		//Check that user C is kicked with user A
		memberPageUserB.menuBackButton.click();
		roomDetailsA.menuBackButton.click();
		Assert.assertEquals(roomPageA.getTextViewFromPost(roomPageA.getLastPost()).getText(), riotUserBDisplayName+" kicked "+riotUserCDisplayName);
		
		//9. Open user A details page with user B.
		memberPageUserC.menuBackButton.click();
		roomDetailsB.getMemberByName(riotUserADisplayName).click();
		RiotMemberDetailsPageObjects memberPageUserA=new RiotMemberDetailsPageObjects(appiumFactory.getAndroidDriver2());
		//Check that user B doesn't have any moderator or admin privileges.
		Assert.assertNull(memberPageUserA.getActionItemByName("Remove from this room"), "There is a Kick action visible on user A's member details page and it shoudn't be.");
		Assert.assertNull(memberPageUserA.getActionItemByName("Ban"), "There is a Ban action visible on user A's member details page and it shoudn't be.");
		Assert.assertNull(memberPageUserA.getActionItemByName("Make moderator"), "There is a Make moderator action visible on user A's member details page and it shoudn't be.");
		Assert.assertNull(memberPageUserA.getActionItemByName("Ban"), "There is a Ban action visible on user A's member details page and it shoudn't be.");
	}

	public void makeMemberAdminTest(){

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
	@BeforeGroups("2checkusers_android")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserADisplayName, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver2(), riotUserBDisplayName, Constant.DEFAULT_USERPWD);
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
		System.out.println(testRoomId);
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
