package mobilestests;

import org.testng.annotations.Test;

import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.RiotParentTest;

public class RiotExempleTests extends RiotParentTest{

	@Test(groups="2drivers", suiteName="")
	public void test() throws InterruptedException{
		System.out.println("running test 1");
	}
	
	@Test(groups="2drivers")
	public void test2(){
		System.out.println("running test 2");
	}
	
	@Test(groups="2drivers")
	public void twoDevices() throws InterruptedException{
		RiotRoomsListPageObjects mainPageDevice1 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver1());
		RiotLoginAndRegisterPageObjects loginViewDevice2 = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver2());
		mainPageDevice1.getRoomByName("temp room").click();//clic on a room with device 1
		loginViewDevice2.fillLoginForm("riotuser3","riotuser"); //login on device 2
		RiotRoomPageObjects roomDevice1=new RiotRoomPageObjects(AppiumFactory.getAppiumDriver1());
		roomDevice1.menuBackButton.click();//come back in rooms list with device 1
		RiotRoomsListPageObjects mainPageDevice2 = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver2());
		mainPageDevice2.logOut();//logout with device2
	}

}
