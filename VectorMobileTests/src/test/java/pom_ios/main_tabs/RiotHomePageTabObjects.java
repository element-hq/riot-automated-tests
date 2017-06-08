package pom_ios.main_tabs;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class RiotHomePageTabObjects extends RiotTabPageObjects{

	public RiotHomePageTabObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		super(myDriver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"HomeVCView", true, 10),"HomePage tab isn't open.");
		//Instanciate protected properties from mother class with own and uniques values of this tab.
		super.roomsTableView=hpTabRoomsTableView;
		super.tabMainView=homePageTabView;
	}

	@iOSFindBy(accessibility="HomeVCView")
	public MobileElement homePageTabView;

	/*
	 * ROOMS LIST.
	 */
	/**
	 * Table view of rooms items. </br> Contains a list of 'RecentTableViewCell' cells.
	 */
	@iOSFindBy(accessibility="HomeVCTableView")
	public MobileElement hpTabRoomsTableView;
	
	/**
	 * TEMP !!!!!!!!!!
	 * Return a room as a MobileElement using his title.</br>
	 * Return null if not found.
	 * @param myRoomName
	 * @return
	 */
	public MobileElement getRoomByName(String myRoomName){
		try {
			return roomsTableView.findElementByXPath("//XCUIElementTypeCell/XCUIElementTypeStaticText[@name='TitleLabel' and @value='"+myRoomName+"']");
		} catch (Exception e) {
			System.out.println("No room found with name "+myRoomName);
			return null;
		}
	}
}
