package mobilestests_ios;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotMessagesReceptionTests extends RiotParentTest{
	private String roomTest="msg rcpt 4 automated tests";
	private String riotUserDisplayNameA="riotuser4";
	private String riotUserDisplayNameB="riotuser5";
	
	/**
	 * Validates issue https://github.com/vector-im/riot-ios/issues/809
	 * 1. Open roomtest with device A.
	 * 2. Open roomtest with device B.
	 * 3. User A write something in the message bar but don't send it.
	 * Test that the typing indicator indicates '[user1] is typing..." with device B.
	 * 4. Type an other msg and clear it with user 4 in the message bar.
	 * Test that the typing indicator is empty on device B.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_ios","checkuser"})//@Test(groups="2drivers_ios")
	public void typingIndicatorTest() throws InterruptedException{
		String notSentMsg="tmp";
		RiotRoomsListPageObjects roomsListA = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		RiotRoomsListPageObjects roomsListB= new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver2());
		
		//1. Open roomtest with device A.
		roomsListA.getRoomByName(roomTest).click();
		RiotRoomPageObjects roomA=new  RiotRoomPageObjects(AppiumFactory.getiOsDriver1());
		
		//2. Open roomtest with device B.		
		roomsListB.getRoomByName(roomTest).click();
		RiotRoomPageObjects roomB=new  RiotRoomPageObjects(AppiumFactory.getiOsDriver2());
		
		//3. User A write something in the message bar but don't send it.
		roomA.sendKeyTextView.setValue(notSentMsg);
		//Test that the typing indicator indicates '[user1] is typing..." with device B.
		Assert.assertEquals(roomB.notificationMessage.getText(), riotUserDisplayNameA+" is typing...");
		Assert.assertTrue(roomB.notificationMessage.isDisplayed(),"Typing indicator isn't displayed on device B");
		
		//4. Type an other msg and clear it with user 4 in the message bar.
		roomA.sendKeyTextView.setValue(notSentMsg);
		roomA.sendKeyTextView.findElementByClassName("XCUIElementTypeTextView").clear();
		//Test that the typing indicator is empty on device B.
		Assert.assertFalse(roomB.notificationMessage.isDisplayed(),"Typing indicator is displayed on device B and shouldn't because device A isn't typing");
		//come back to rooms list
		roomA.menuBackButton.click();
		roomB.menuBackButton.click();
	}

	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups("checkuser")
	private void checkIfUsersLogged() throws InterruptedException{
		super.checkIfUserLoggedIos(AppiumFactory.getiOsDriver1(), riotUserDisplayNameA, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedIos(AppiumFactory.getiOsDriver2(), riotUserDisplayNameB, Constant.DEFAULT_USERPWD);
	}
}
