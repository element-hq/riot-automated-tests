package pom_ios.main_tabs;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import pom_ios.RiotRoomPageObjects;

public class RiotRoomsTabPageObjects extends RiotTabPageObjects {

	public RiotRoomsTabPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		super(myDriver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"RoomsVCView", true, 5),"Rooms tab isn't open.");
		//Instanciate protected properties from mother class with own and uniques values of this tab.
		super.roomsTableView=roomsTabRoomsTableView;
		super.tabMainView=roomsTabView;
	}

	@iOSFindBy(accessibility="RoomsVCView")
	public MobileElement roomsTabView;

	/*
	 * ROOMS LIST.
	 */
	/**
	 * Table view of rooms items. </br> Contains a list of 'RecentTableViewCell' cells.
	 */
	@iOSFindBy(accessibility="RoomsVCTableView")
	public MobileElement roomsTabRoomsTableView;
	
	@iOSFindBy(accessibility="PublicRoomTableViewCell")
	public List<MobileElement> publicRoomsList;

	/*
	 * FLOATING BUTTONS OR DIALOGS .
	 */
	/**
	 * Create a new room : click on plus button. </br>
	 * Return a RiotRoomPageObjects object.
	 * @return
	 */
	public RiotRoomPageObjects createRoom(){
		createRoomFloatingButton.click();
		if(driver.findElementByClassName("XCUIElementTypeCollectionView")==null){
			createRoomFloatingButton.click();
		}
		return new RiotRoomPageObjects(driver);
	}
}
