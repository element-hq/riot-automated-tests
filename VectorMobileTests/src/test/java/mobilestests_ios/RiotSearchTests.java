package mobilestests_ios;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.RiotUnifiedSearchPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on unified search, and search in rooms.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotSearchTests extends RiotParentTest{
	private String riotUserDisplayName="riotuser15";
	
	/**
	 * Cover issue https://github.com/vector-im/riot-ios/issues/1152. (WIP)</br>
	 * 1. Hit search button from rooms list. </br>
	 * 2. In Rooms tab, launch a search with a random string.</br>
	 * Check that the only item of the results list is DIRECTORY and have "0 results found for [random_search]".</br>
	 * 3. Hit cancel button on the search bar.</br>
	 * Check that the global search page is closed: no more search tabs.</br>
	 * Check that numbers of room in recents is the same that before step 1.</br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void cancelUnsuccessfulGlobalSearchInRooms() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomRoomName=(new StringBuilder("room_search").append(randInt1)).toString();
		int nbRooms=0;
		
		//1. Hit search button from rooms list.
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		nbRooms=roomsList.roomsList.size();
		roomsList.searchButton.click();
		RiotUnifiedSearchPageObjects unifiedSearch = new RiotUnifiedSearchPageObjects(appiumFactory.getiOsDriver1());
		
		//2. In Rooms tab, launch a search with a random string.
		unifiedSearch.launchASearch(randomRoomName, true);
		//Check that the only item of the results list is DIRECTORY and have "0 results found for [random_search]".
		Assert.assertTrue(unifiedSearch.roomsWithBrowseDirectoryTableView.findElementsByClassName("XCUIElementTypeCell").size()==1, "More than 1 result after the search.");
		Assert.assertEquals(unifiedSearch.browseDirectoryItemLayout.findElementByAccessibilityId("TitleLabel").getText(), "Browse directory results");
		Assert.assertEquals(unifiedSearch.browseDirectoryItemLayout.findElementByAccessibilityId("DescriptionLabel").getText(), "0 results found for "+randomRoomName);
		
		//3. Hit cancel button on the search bar.
		unifiedSearch.cancelButton.click();
		//Check that the global search page is closed: no more search tabs.
		//Assert.assertFalse(isPresentTryAndCatch(unifiedSearch.tabsBarContainer), "Unified search is still opened after hitting Cancel button");
		//Check that numbers of room in recents is the same that before step 1.
		Assert.assertEquals(roomsList.roomsList.size(), nbRooms);
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
	@BeforeGroups("checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedIos(appiumFactory.getiOsDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
