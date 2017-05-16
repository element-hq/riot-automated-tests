package mobilestests_ios;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotSettingsPageObjects;
import pom_ios.main_tabs.RiotHomePageTabObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests about Recents lists: badge, room name, room avatar, last event on room items.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotRoomsListItemsTests extends RiotParentTest{
private String roomNameTest="auto test list items";
private String riotUserDisplayName="riotuser11";
	/**
	 * Tests that some events are not displayed in the last event label on the rooms list. </br>
	 * 1. Open room roomNameTest </br>
	 * 2. Send a message </br>
	 * 3. Change user avatar </br>
	 * 4. Change user display name </br>
	 * 5. Come back on the rooms list. </br>
	 * Check that the last event is the msg sent in step 2. </br>
	 * 6.  Set the old display name
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void roomItemExcludedLastEventsTest() throws InterruptedException{
		int randInt = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomMsg=(new StringBuilder("last event msg").append(randInt)).toString();
		String newDisplayName=riotUserDisplayName+"-";
		
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		//1. Open room roomNameTest
		homePage.getRoomByName(roomNameTest).click();
		RiotRoomPageObjects roomPage = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		//2. Send a message
		roomPage.sendAMessage(randomMsg);
		//3. Change user avatar
		roomPage.menuBackButton.click();
		RiotSettingsPageObjects settingsPage = homePage.openRiotSettings();
		settingsPage.changeAvatarFromSettings();
		//4. Change user display name
		settingsPage.changeDisplayNameFromSettings(newDisplayName);
		settingsPage.saveNavBarButton.click();
		//5. Come back on the rooms list.
		settingsPage.backMenuButton.click();
		//Check that the last event is the msg sent in step 2
		Assert.assertEquals(homePage.getLastEventByRoomName(roomNameTest,true), newDisplayName+": "+randomMsg);
		//6.  Set the old display name
		settingsPage = homePage.openRiotSettings();
		settingsPage.changeDisplayNameFromSettings(riotUserDisplayName);
		settingsPage.saveNavBarButton.click();
		settingsPage.backMenuButton.click();
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
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
