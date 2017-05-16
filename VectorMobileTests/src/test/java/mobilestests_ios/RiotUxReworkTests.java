package mobilestests_ios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.main_tabs.RiotHomePageTabObjects;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.MatrixUtilities;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on Ux Rework.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotUxReworkTests extends RiotParentTest{
	private String riotUserADisplayName="riotuser1";
	private String riotUserBDisplayName="riotuser2";
	private String riotUserAAccessToken;
	private String testRoomId;
	
	/**
	 * 1. Create room A from home page. </br>
	 * 2. Make it favourite. </br>
	 * Check that the room is in the FAVOURITES section on the home page. </br>
	 * 3. Hit FAVOURITES tab, and check that room A is present. </br>
	 * 4. Hit PEOPLE tab, and check that room A is not present. </br>
	 * 5. Hit ROOMS tab and check that room A is present.
	 * @throws IOException, InterruptedException 
	 */
	@Test(groups={"1driver_ios","1checkuser"}, priority=0)
	public void duplicatedFavouritedRoomAccrossTabsTest() throws IOException, InterruptedException{
		String roomName="favouriteRoomAutoTest";
		//1. Create room A from home page.
		createRoomWithByRequestsToMatrix(roomName);
		
		//2. Make it favourite. 
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		homePage.clickOnSwipedMenuOnRoom(roomName, "Favourite");
	}
	
	/**
	 * Create a room by using https requests to home server.
	 * @param roomName
	 * @throws IOException 
	 */
	private void createRoomWithByRequestsToMatrix(String roomName) throws IOException{
		//1. Create room R with user A.
		riotUserAAccessToken=HttpsRequestsToMatrix.login(riotUserADisplayName, Constant.DEFAULT_USERPWD);
		testRoomId=HttpsRequestsToMatrix.createRoom(riotUserAAccessToken, roomName);
		//2. Invite user B. 
		HttpsRequestsToMatrix.sendInvitationToUser(riotUserAAccessToken, testRoomId, MatrixUtilities.getMatrixIdFromDisplayName(riotUserBDisplayName));
	}
	
	/**
	 * Create a direct chatwith user B by using https requests to home server.
	 * @param roomName
	 * @throws IOException 
	 */
	private void createDirectChatWithByRequestsToMatrix(String invitedMatrixId) throws IOException{
		//1. Create room R with user A.
		riotUserAAccessToken=HttpsRequestsToMatrix.login(riotUserADisplayName, Constant.DEFAULT_USERPWD);
		testRoomId=HttpsRequestsToMatrix.createDirectChatRoom(riotUserAAccessToken, invitedMatrixId);
		//2. Invite user B. 
		HttpsRequestsToMatrix.sendInvitationToUser(riotUserAAccessToken, testRoomId, MatrixUtilities.getMatrixIdFromDisplayName(riotUserBDisplayName));
	}
	
	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException, IOException{
		switch (m.getName()) {
		case "duplicatedFavouritedRoomAccrossTabsTest":
			leaveAndForgetRoomUsers();
			break;
		case "duplicatedDirectChatAccrossTabsTest":
			leaveAndForgetRoomUsers();
			break;
		case "notDuplicatedLowPriorityRoomAccrossTabsTest":
			leaveAndForgetRoomUsers();
			break;
		case "notDuplicatedDirectChatAccrossTabsTest":
			leaveAndForgetRoomUsers();
			break;
		default:
			break;
		}
	}
	
	private void leaveAndForgetRoomUsers() throws IOException{
		//leave room user A
		HttpsRequestsToMatrix.leaveRoom(riotUserAAccessToken, testRoomId);
		//forget room user A
		HttpsRequestsToMatrix.forgetRoom(riotUserAAccessToken, testRoomId);
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
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotUserADisplayName, Constant.DEFAULT_USERPWD);
	}
}
