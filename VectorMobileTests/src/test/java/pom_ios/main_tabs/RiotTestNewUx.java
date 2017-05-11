package pom_ios.main_tabs;

import java.io.IOException;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_ios.RiotRoomPageObjects;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotTestNewUx extends RiotParentTest{
	
	@Test(groups={"1driver_ios"})
	public void testRoomsTab() throws IOException, InterruptedException{
		RiotHomePageTabObjects riotHomePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		RiotRoomsTabPageObjects riotRoomsTab =riotHomePage.openRoomsTab();
		riotRoomsTab.navigationBar.click();
		riotRoomsTab.getRoomByName("room tests Jean").click();
		RiotRoomPageObjects room=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		room.menuBackButton.click();
		riotRoomsTab.favouriteTabBottomBarButton.click();
	}
	
	@Test(groups={"1driver_ios"})
	public void testSwipeOnItem() throws IOException, InterruptedException{
		RiotHomePageTabObjects riotHomePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		riotHomePage.clickOnSwipedMenuOnRoom("Common riotusers auto tests", "favourite");
	}
	
	@Test(groups={"1driver_ios"})
	public void testNavBar() throws IOException, InterruptedException{
		RiotHomePageTabObjects riotHomePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		System.out.println(riotHomePage.navigationBar.findElementByClassName("XCUIElementTypeStaticText").getText());
	}
	
	@Test(groups={"1driver_ios"})
	public void testsDisplayAndUseFilter() throws IOException, InterruptedException{
		RiotHomePageTabObjects riotHomePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		riotHomePage.displayFilterBarBySwipingDown();
		riotHomePage.useFilterBar("jean");
	}
}
