package pom_android.main_tabs;

import java.io.IOException;

import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import utility.RiotParentTest;

public class RiotUxReworkTests extends RiotParentTest{
	@Test(groups={"1driver_android"})
	public void testAndroidNewUx() throws IOException, InterruptedException{
		RiotHomePageTabObjects hp = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		hp.hitSectionHeader("People");
//		hp.getRoomByName("Books").click();
//		RiotRoomPageObjects room=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
//		room.menuBackButton.click();
		RiotPeopleTabPageObjects peopleTab=hp.openPeopleTab();
		peopleTab.hitSectionHeader("Local address book");
		RiotRoomsTabPageObjects roomsList=hp.openRoomsTab();
		roomsList.hitSectionHeader("Room directory");
	}
	
	@Test(groups={"1driver_android"})
	public void testSwipe() throws IOException, InterruptedException{
		RiotHomePageTabObjects hp = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		hp.leftSwipeOnRoomSection(hp.directChatSectionLayout);
		hp.leftSwipeOnRoomSection(hp.directChatSectionLayout);
		hp.leftSwipeOnRoomSection(hp.roomsSectionLayout);
		hp.leftSwipeOnRoomSection(hp.roomsSectionLayout);
	}
	
	@Test(groups={"1driver_android"})
	public void swipeUntilRoomFound() throws IOException, InterruptedException{
		RiotHomePageTabObjects hp = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		
		MobileElement room=hp.swipeSectionUntilRoomDisplayed(hp.directChatSectionLayout,"yolo",10);
		if(null!=room)
			room.click();
		
		Thread.sleep(5000);
	}
	
}
