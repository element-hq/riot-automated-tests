package mobilestests_android;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_android.RiotRoomPageObjects;
import pom_android.RiotSettingsPageObjects;
import pom_android.main_tabs.RiotHomePageTabObjects;
import pom_android.main_tabs.RiotRoomsTabPageObjects;
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
		 * 6.  Set the old display name </br>
		 * @throws InterruptedException 
		 */
		@Test(groups={"1driver_android","1checkuser"})
		public void roomItemExcludedLastEventsTest() throws InterruptedException{
			int randInt = 1 + (int)(Math.random() * ((10000 - 1) + 1));
			String randomMsg=(new StringBuilder("last event msg").append(randInt)).toString();
			String newDisplayName=riotUserDisplayName+"-";
			
			RiotHomePageTabObjects homePage=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
			//1. Open room roomNameTest
			homePage.getRoomByName(roomNameTest).click();
			RiotRoomPageObjects roomPage = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
			//2. Send a message
			roomPage.sendAMessage(randomMsg);
			//3. Change user avatar
			roomPage.menuBackButton.click();
			homePage.contextMenuButton.click();
			homePage.settingsButton.click();
			RiotSettingsPageObjects settingsPage= new RiotSettingsPageObjects(appiumFactory.getAndroidDriver1());
			settingsPage.changeAvatarFromSettings();
			//4. Change user display name
			settingsPage.changeDisplayNameFromSettings(newDisplayName);
			//5. Come back on the rooms list.
			settingsPage.actionBarBackButton.click();
			RiotRoomsTabPageObjects roomsTab = homePage.openRoomsTab();
			//Check that the last event is the msg sent in step 2
			Assert.assertEquals(roomsTab.getLastEventByRoomName(roomNameTest,true), newDisplayName+": "+randomMsg);
			//6.  Set the old display name
			roomsTab.contextMenuButton.click();
			roomsTab.settingsButton.click();
			settingsPage.changeDisplayNameFromSettings(riotUserDisplayName);
			settingsPage.actionBarBackButton.click();
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
			super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
		}
}
