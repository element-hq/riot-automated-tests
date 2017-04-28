package pom_ios.main_tabs;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import pom_ios.RiotNewChatPageObjects;
import pom_ios.RiotRoomPageObjects;

public class RiotHomePageTabObjects extends RiotTabPageObjects{

	public RiotHomePageTabObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		super(myDriver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"HomeVCView", true, 5),"HomePage tab isn't open.");
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

	/*
	 * FLOATING BUTTONS OR DIALOGS .
	 */

	/*
	 * 		Start/Create room sheet. Opened after click on plus button.
	 */
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionStart chat")
	public MobileElement startChatSheetButton;
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionCreate room")
	public MobileElement createRoomSheetButton;
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionCancel")
	public MobileElement cancelCreationSheetButton;

	/**
	 * Create a new room : click on plus button, then create room item. </br>
	 * Return a RiotRoomPageObjects object.
	 * @return
	 */
	public RiotRoomPageObjects createRoom(){
		createRoomButton.click();
		if(driver.findElementByClassName("XCUIElementTypeCollectionView")==null){
			createRoomButton.click();
		}
		createRoomSheetButton.click();
		return new RiotRoomPageObjects(driver);
	}

	/**
	 * Start a new chat with a user and returns the new created room. 
	 * @param displayNameOrMatrixId
	 * @return RiotRoomPageObjects
	 */
	public RiotRoomPageObjects startChat(String displayNameOrMatrixId){
		createRoomButton.click();
		startChatSheetButton.click();
		RiotNewChatPageObjects newChatA = new RiotNewChatPageObjects(appiumFactory.getiOsDriver1());
		newChatA.searchAndSelectMember(displayNameOrMatrixId);
		return new RiotRoomPageObjects(driver);
	}
}
