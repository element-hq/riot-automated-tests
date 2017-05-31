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

import pom_android.RiotRoomPageObjects;
import pom_android.main_tabs.RiotFavouritesTabPageObjects;
import pom_android.main_tabs.RiotHomePageTabObjects;
import pom_android.main_tabs.RiotPeopleTabPageObjects;
import pom_android.main_tabs.RiotRoomsTabPageObjects;
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
	private String riotUserBAccessToken;
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
	@Test(groups={"1driver_android","1checkuser"}, priority=0)
	public void duplicatedFavouritedRoomAccrossTabsTest() throws IOException, InterruptedException{
		String roomName="favouriteRoomAutoTest";
		//1. Create room A from home page.
		createRoomWithByRequestsToMatrix(roomName);

		//2. Make it favourite. 
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		homePage.clickOnContextMenuOnRoom(roomName, "Favourite");
		//Check that the room is in the FAVOURITES section on the home page. 
		homePage.checkRoomInCategory(roomName, homePage.favouritesSectionLayout,0);

		//3. Hit FAVOURITES tab, and check that room A is present.
		RiotFavouritesTabPageObjects favouritesPage=homePage.openFavouriteTab();
		Assert.assertNotNull(favouritesPage.getRoomByName(roomName), "Room "+roomName+" not found in Favourites tab.");

		//4. Hit PEOPLE tab, and check that room A is not present.
		RiotPeopleTabPageObjects peopleTab=favouritesPage.openPeopleTab();
		Assert.assertNull(peopleTab.getRoomByName(roomName), "Room "+roomName+" found in People tab and it shouldn't be.");

		//5. Hit ROOMS tab and check that room A is present.
		RiotRoomsTabPageObjects roomsTab = peopleTab.openRoomsTab();
		Assert.assertNotNull(roomsTab.getRoomByName(roomName), "Room "+roomName+" not found in Rooms tab.");
	}

	/**
	 * 1. Create direct chat with user B. </br>
	 * 2. Make it favourite. </br>
	 * Check that the room is in the FAVOURITES section on the home page. </br>
	 * 3. Hit FAVOURITES tab, and check that direct chat with user B is present. </br>
	 * 4. Hit PEOPLE tab and check that direct chat with user B is present.
	 * 5. Hit ROOMS tab and check that room A is not present.
	 * @throws IOException, InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=1)
	public void duplicatedDirectChatAccrossTabsTest() throws IOException, InterruptedException{
		//1. Create direct chat with user B.
		createDirectChatWithByRequestsToMatrix(MatrixUtilities.getMatrixIdFromDisplayName(riotUserBDisplayName));

		//2. Make it favourite.
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		homePage.clickOnContextMenuOnRoom(riotUserBDisplayName, "Direct Chat");
		homePage.clickOnContextMenuOnRoom(riotUserBDisplayName, "Favourite");
		//Check that the room is in the FAVOURITES section on the home page.
		homePage.checkRoomInCategory(riotUserBDisplayName, homePage.favouritesSectionLayout,0);

		//3. Hit FAVOURITES tab, and check that direct chat with user B is present. 
		RiotFavouritesTabPageObjects favouritesPage=homePage.openFavouriteTab();
		Assert.assertNotNull(favouritesPage.getRoomByName(riotUserBDisplayName), "Direct chat "+riotUserBDisplayName+" not found in Favourites tab.");

		//4. Hit PEOPLE tab and check that direct chat with user B is present.
		RiotPeopleTabPageObjects peopleTab = favouritesPage.openPeopleTab();
		Assert.assertNotNull(peopleTab.getRoomByName(riotUserBDisplayName), "Direct chat "+riotUserBDisplayName+" not found in People tab.");

		//5. Hit ROOMS tab and check that direct chat with user B is not present.
		RiotRoomsTabPageObjects roomsTab = peopleTab.openRoomsTab();
		Assert.assertNull(roomsTab.getRoomByName(riotUserBDisplayName), "Direct chat "+riotUserBDisplayName+" found in Rooms tab and it shouldn't be.");
	}

	/**
	 * 1. Create room A from home page. </br>
	 * 2. Make it low priority. </br>
	 * Check that the room is in the LOW PRIORITY section on the home page. </br>
	 * 3. Hit ROOMS tab and check that room A is not present.
	 * @throws IOException, InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=2)
	public void notDuplicatedLowPriorityRoomAccrossTabsTest() throws IOException, InterruptedException{
		String roomName = "lowPriorityRoomTest";
		//1. Create room A from home page.
		createRoomWithByRequestsToMatrix(roomName);

		//2. Make it low priority. 
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		homePage.clickOnContextMenuOnRoom(roomName, "De-prioritize");
		//Check that the room is in the LOW PRIORITY section on the home page. 
		homePage.checkRoomInCategory(roomName, homePage.lowPrioritySectionLayout,0);

		//3. Hit ROOMS tab and check that room A is not present
		RiotRoomsTabPageObjects roomsTab=homePage.openRoomsTab();
		Assert.assertNull(roomsTab.getRoomByName(roomName), "Room "+roomName+" found in Rooms tab and it shouldn't be.");
	}

	/**
	 * 1. Create direct chat with user B. </br>
	 * 2. Make it low priority. </br>
	 * Check that the room is in the LOW PRIORITY section on the home page. </br>
	 * 3. Hit PEOPLE tab and check that direct chat with user B is not present.
	 * @throws IOException, InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=3)
	public void notDuplicatedDirectChatAccrossTabsTest() throws IOException, InterruptedException{
		//1. Create direct chat with user B.
		createDirectChatWithByRequestsToMatrix(MatrixUtilities.getMatrixIdFromDisplayName(riotUserBDisplayName));

		//2. Make it low priority.
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		homePage.clickOnContextMenuOnRoom(riotUserBDisplayName, "Direct Chat");
		homePage.clickOnContextMenuOnRoom(riotUserBDisplayName, "De-prioritize");
		//Check that the room is in the LOW PRIORITY section on the home page. 
		homePage.checkRoomInCategory(riotUserBDisplayName, homePage.lowPrioritySectionLayout,0);

		//3. Hit PEOPLE tab and check that direct chat with user B is not present.
		RiotPeopleTabPageObjects peopleTab = homePage.openPeopleTab();
		Assert.assertNull(peopleTab.getRoomByName(riotUserBDisplayName), "Direct chat "+riotUserBDisplayName+" found in People tab and it shouldn't be.");
	}

	/**
	 * Brows tabs and check the filter search field text.
	 * 1. Open home page tab, check that filter field text is 'Search for rooms'
	 * 2. Open favourites tab, check that filter field text is 'Search for favourites'
	 * 3. Open people tab, check that filter field text is 'Search for people'
	 * 4. Open rooms tab, check that filter field text is 'Search for rooms'
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=4)
	public void filterOptionSearchTest() throws InterruptedException{
		//1. Open home page tab, check that filter field text is 'Search for rooms'
		RiotHomePageTabObjects homePageTab = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		Assert.assertEquals(homePageTab.filterBarEditText.getText(), "Search for rooms");
		//2. Open favourites tab, check that filter field text is 'Search for favourites'
		RiotFavouritesTabPageObjects favouritesTab= homePageTab.openFavouriteTab();
		Assert.assertEquals(favouritesTab.filterBarEditText.getText(), "Search for favourites");
		//3. Open people tab, check that filter field text is 'Search for people'
		RiotPeopleTabPageObjects peopleTab = favouritesTab.openPeopleTab();
		Assert.assertEquals(peopleTab.filterBarEditText.getText(), "Search for people");
		//4. Open rooms tab, check that filter field text is 'Search for rooms'
		RiotRoomsTabPageObjects roomsTab=peopleTab.openRoomsTab();
		Assert.assertEquals(roomsTab.filterBarEditText.getText(), "Search for rooms");
	}

	/**
	 * 1. Open people tab </br>
	 * 2. Hit LOCAL ADDRESS BOOK sticky header </br>
	 * Local contact list is displayed </br>
	 * 3. Hit CONVERSATIONS sticky header </br>
	 * Conversations list is displayed </br>
	 * 4. Open rooms tab </br>
	 * 5. Hit ROOM DIRECTORY stikcy header </br>
	 * Check that room directory section is displayed </br>
	 * 6. Hit ROOMS stikcy header </br>
	 * Check that rooms displayed
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser_default_hs"}, priority=6)
	public void stickyHeaderTest() throws InterruptedException{
		//1. Open people tab
		RiotHomePageTabObjects homePageTab = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotPeopleTabPageObjects peopleTab = homePageTab.openPeopleTab();

		//2. Hit LOCAL ADDRESS BOOK sticky heade
		peopleTab.hitSectionHeader("LOCAL ADDRESS BOOK");
		//Local contact list is displayed 
		Assert.assertTrue(peopleTab.peopleList.size()>1, "Contacts list is empty");

		//3. Hit CONVERSATIONS sticky header
		peopleTab.hitSectionHeader("CONVERSATIONS");
		//Conversations list is displayed
		Assert.assertTrue(peopleTab.roomsList.size()>1,"Conversations list is empty");

		//4. Open rooms tab 
		RiotRoomsTabPageObjects roomsTab = peopleTab.openRoomsTab();
		//5. Hit ROOM DIRECTORY stikcy header
		waitUntilDisplayed(appiumFactory.getAndroidDriver1(), "im.vector.alpha:id/section_loading", false, 10);
		roomsTab.hitSectionHeader("ROOM DIRECTORY");
		//Check that room directory section is displayed
		Assert.assertTrue(roomsTab.publicRoomsList.size()>1, "Rooms directory list is empty");

		// 6. Hit ROOMS stikcy header 
		roomsTab.hitSectionHeader("ROOMS");
		//Check that room section is displayed
		Assert.assertTrue(roomsTab.roomsList.size()>1, "Rooms list is empty");
	}

	/**
	 * 1. Create a direct chat room with user B. </br>
	 * 2. Accept invitation with user B.
	 * 3. Send a message with user B.  </br>
	 * Check the presence of the unread badge on the room from the home page. </br>
	 * Check the presence of the direct message badge on the room from the home page. </br>
	 * 4. Make the room encrypted with user A. </br>
	 * Check the presence of the encrypted badge on the room from the home page. </br>
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=5)
	public void badgesOnHomePageTest() throws IOException, InterruptedException{
		String unreadTestMsg="badge";

		//1. Create a direct chat room with user B
		createDirectChatWithByRequestsToMatrix(MatrixUtilities.getMatrixIdFromDisplayName(riotUserBDisplayName));
		//2. Accept invitation with user B.
		riotUserBAccessToken=HttpsRequestsToMatrix.login(riotUserBDisplayName, Constant.DEFAULT_USERPWD);
		HttpsRequestsToMatrix.joinRoom(riotUserBAccessToken, testRoomId);

		RiotHomePageTabObjects homePageTabA = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		homePageTabA.clickOnContextMenuOnRoom(riotUserBDisplayName, "Direct Chat");
		homePageTabA.getRoomByName(riotUserBDisplayName).click();
		RiotRoomPageObjects roomPageA=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPageA.menuBackButton.click();

		//3. Send a message with user B
		riotUserAAccessToken=HttpsRequestsToMatrix.login(riotUserADisplayName, Constant.DEFAULT_USERPWD);
		HttpsRequestsToMatrix.sendMessageInRoom(riotUserBAccessToken, testRoomId, unreadTestMsg);
		//Check the presence of the unread badge on the room from the home page.
		Assert.assertEquals(String.valueOf(homePageTabA.getBadgeNumberByRoomName(riotUserBDisplayName)), "1");
		//Check the presence of the direct message badge on the room from the home page. 
		Assert.assertTrue(homePageTabA.isRoomTaggedDirectMessage(homePageTabA.getRoomByName(riotUserBDisplayName)),"Room isn't tagged Direct Chat in the HomePage.");

		//4. Make the room encrypted with user A.
		HttpsRequestsToMatrix.enableEncryptionInRoom(riotUserAAccessToken, testRoomId);
		//Check the presence of the encrypted badge on the room from the home page. 
		Assert.assertTrue(homePageTabA.isRoomTaggedEncrypted(homePageTabA.getRoomByName(riotUserBDisplayName)),"Room isn't tagged Direct Chat in the HomePage.");
	}

	/**
	 * Create a room by using https requests to home server.
	 * @param roomName
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
			leaveAndForgetRoom2User();
			break;
		case "duplicatedDirectChatAccrossTabsTest":
			leaveAndForgetRoom2User();
			break;
		case "notDuplicatedLowPriorityRoomAccrossTabsTest":
			leaveAndForgetRoom2User();
			break;
		case "notDuplicatedDirectChatAccrossTabsTest":
			leaveAndForgetRoom2User();
			break;
		case "badgesOnHomePageTest":
			leaveAndForgetRoom2User();
			break;
		default:
			break;
		}
	}

	@AfterMethod(alwaysRun=true,groups={"1driver_android"})
	private void restart1ApplicationAfterTest(Method m) throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
	}

	private void leaveAndForgetRoom2User() throws IOException{
		//leave room user A
		HttpsRequestsToMatrix.leaveRoom(riotUserAAccessToken, testRoomId);
		//forget room user A
		HttpsRequestsToMatrix.forgetRoom(riotUserAAccessToken, testRoomId);

		if(null==riotUserBAccessToken)
			riotUserBAccessToken = HttpsRequestsToMatrix.login(riotUserBDisplayName, Constant.DEFAULT_USERPWD);
		//leave room user B
		HttpsRequestsToMatrix.leaveRoom(riotUserBAccessToken, testRoomId);
		//forget room user B
		HttpsRequestsToMatrix.forgetRoom(riotUserBAccessToken, testRoomId);
	}

	//	private void leaveAndForgetRoom2Users() throws IOException{
	//		//leave room user A
	//		HttpsRequestsToMatrix.leaveRoom(riotUserAAccessToken, testRoomId);
	//		//forget room user A
	//		HttpsRequestsToMatrix.forgetRoom(riotUserAAccessToken, testRoomId);
	//		//leave room user B
	//		HttpsRequestsToMatrix.leaveRoom(riotUserBAccessToken, testRoomId);
	//		//forget room user B
	//		HttpsRequestsToMatrix.forgetRoom(riotUserBAccessToken, testRoomId);
	//	}

	/**
	 * Log the good user if not.</br> Secure the test.
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserADisplayName, Constant.DEFAULT_USERPWD);
	}

	/**
	 * Log the good user if not.</br> Secure the test.
	 */
	@BeforeGroups("1checkuser_default_hs")
	private void checkIfUserLoggedWithDefaultHs() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserADisplayName, Constant.DEFAULT_USERPWD,true);
	}
}
