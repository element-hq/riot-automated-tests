package mobilestests_android;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.SwipeElementDirection;
import pom_android.main_tabs.RiotHomePageTabObjects;
import pom_android.main_tabs.RiotRoomsTabPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on public rooms, on the rooms tab.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotPublicRoomsTests extends RiotParentTest{
	private String riotUserADisplayName="riotuser1";
	
	/**
	 * Force using matrix.org and not custom one. </br>
	 * 1. Hit ROOMS tab. </br>
	 * 2. Hit ROOM DIRECTORY section to focus on the public rooms.  </br>
	 * Check that list of public rooms isn't empty. </br>
	 * 3. Scroll several times o the public rooms.  </br>
	 * After each scroll, check that the last room isn't the same that the previous.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=0)
	public void checkPublicRoomsList() throws InterruptedException{
		RiotHomePageTabObjects homePageTab = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		//1. Hit ROOMS tab.
		RiotRoomsTabPageObjects roomsTab=homePageTab.openRoomsTab();
		
		//2. Hit ROOM DIRECTORY section to focus on the public rooms. 
		Thread.sleep(1000);
		roomsTab.hitSectionHeader("ROOM DIRECTORY");
		//Check that list of public rooms isn't empty. 
		Assert.assertTrue(roomsTab.publicRoomsList.size()>0, "List of public rooms is empty.");
		
		//3. Scroll several times o the public rooms.		
		int maxScroll=5;
		for(int i=1;i<=maxScroll;i++){
			String lastRoomName=roomsTab.getRoomName(roomsTab.publicRoomsList.get(roomsTab.publicRoomsList.size()-2));
			roomsTab.roomsListView.swipe(SwipeElementDirection.UP,200, 50, 100);
			//After each scroll, check that the last room isn't the same that the previous.
			Assert.assertNotEquals(roomsTab.getRoomName(roomsTab.publicRoomsList.get(roomsTab.publicRoomsList.size()-2)), lastRoomName, "Scroll n°"+i+" on "+maxScroll+" didn't seem to work.");
		}
	}

	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserADisplayName, Constant.DEFAULT_USERPWD,true);
	}
}
