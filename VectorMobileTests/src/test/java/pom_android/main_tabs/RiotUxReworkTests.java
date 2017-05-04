package pom_android.main_tabs;

import java.io.IOException;

import org.testng.annotations.Test;

import pom_android.RiotRoomPageObjects;
import utility.RiotParentTest;

public class RiotUxReworkTests extends RiotParentTest{
	@Test(groups={"1driver_android"})
	public void testAndroidNewUx() throws IOException, InterruptedException{
		RiotHomePageTabObjects hp = new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		hp.hitSection("People");
//		hp.getRoomByName("Books").click();
//		RiotRoomPageObjects room=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
//		room.menuBackButton.click();
		RiotPeopleTabPageObjects peopleTab=hp.openPeopleTab();
		peopleTab.hitSection("Local address book");
		RiotRoomsTabPageObjects roomsList=hp.openRoomsTab();
		roomsList.hitSection("Room directory");
	}
}
