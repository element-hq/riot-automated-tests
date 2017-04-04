package mobilestests_android;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_android.RiotRoomsListPageObjects;
import pom_android.RiotSettingsPageObjects;
import pom_android.RiotRoomPageObjects;
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
		 * Tests that some events are not displayed in the last event label on the rooms list.
		 * 1. Open room roomNameTest
		 * 2. Send a message
		 * 3. Change user avatar
		 * 4. Change user display name
		 * 5. Come back on the rooms list.
		 * Check that the last event is the msg sent in step 2.
		 * 6.  Set the old display name
		 * @throws InterruptedException 
		 */
		@Test(groups={"1driver_android","1checkuser"})
		public void roomItemExcludedLastEventsTest() throws InterruptedException{
			int randInt = 1 + (int)(Math.random() * ((10000 - 1) + 1));
			String randomMsg=(new StringBuilder("last event msg").append(randInt)).toString();
			String newDisplayName=riotUserDisplayName+"-";
			
			RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
			//1. Open room roomNameTest
			roomsList.getRoomByName(roomNameTest).click();
			RiotRoomPageObjects roomPage = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
			//2. Send a message
			roomPage.sendAMessage(randomMsg);
			//3. Change user avatar
			roomPage.menuBackButton.click();
			roomsList.contextMenuButton.click();
			roomsList.settingsButton.click();
			RiotSettingsPageObjects settingsPage= new RiotSettingsPageObjects(appiumFactory.getAndroidDriver1());
			settingsPage.changeAvatarFromSettings();
			//4. Change user display name
			settingsPage.changeDisplayNameFromSettings(newDisplayName);
			//5. Come back on the rooms list.
			settingsPage.actionBarBackButton.click();
			//Check that the last event is the msg sent in step 2
			Assert.assertEquals(roomsList.getLastEventByRoomName(roomNameTest,true), newDisplayName+": "+randomMsg);
			//6.  Set the old display name
			roomsList.contextMenuButton.click();
			roomsList.settingsButton.click();
			settingsPage.changeDisplayNameFromSettings(riotUserDisplayName);
			settingsPage.actionBarBackButton.click();
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
			super.checkIfUserLoggedAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
		}
}
