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
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionJoin room")
	public MobileElement joinRoomSheetButton;
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionCancel")
	public MobileElement cancelCreationSheetButton;
	
	/*
	 * 		JOIN ROOM DIALOG
	 */
	@iOSFindBy(accessibility="RecentsVCJoinARoomAlert")
	public MobileElement joinRoomParentLayout;
	@iOSFindBy(accessibility="Join a room")
	public MobileElement joinRoomDescriptionEditText;
	@iOSFindBy(accessibility="RecentsVCJoinARoomAlertTextField0")
	public MobileElement joinRoomEditText;
	@iOSFindBy(accessibility="RecentsVCJoinARoomAlertActionJoin")
	public MobileElement joinRoomJoinButton;
	@iOSFindBy(accessibility="RecentsVCJoinARoomAlertActionCancel")
	public MobileElement joinRoomCancelButton;
	
	
	/**
	 * Create a new room : click on plus button, then create room item. </br>
	 * Return a RiotRoomPageObjects object.
	 * @return
	 */
	public RiotRoomPageObjects createRoom(){
		createRoomFloatingButton.click();
		if(driver.findElementByClassName("XCUIElementTypeCollectionView")==null){
			createRoomFloatingButton.click();
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
		createRoomFloatingButton.click();
		startChatSheetButton.click();
		RiotNewChatPageObjects newChatA = new RiotNewChatPageObjects(appiumFactory.getiOsDriver1());
		newChatA.searchAndSelectMember(displayNameOrMatrixId);
		return new RiotRoomPageObjects(driver);
	}
	
	/**
	 * Join a room: click on plus button, then join room item, and fill the join room dialog. </br>
	 * If roomPageExpected = true, returns a new RiotRoomPageObjects.
	 * @param roomIdOrAlias
	 * @throws InterruptedException 
	 */
	public RiotRoomPageObjects joinRoom(String roomIdOrAlias, Boolean roomPageExpected) throws InterruptedException{
		createRoomFloatingButton.click();
		joinRoomSheetButton.click();
		joinRoomCheck();
		joinRoomEditText.setValue(roomIdOrAlias);
		joinRoomJoinButton.click();
		if(roomPageExpected){
			return new RiotRoomPageObjects(driver);
		}else{
			return null;
		}
	}
	/**
	 * Check the join room dialog.
	 */
	public void joinRoomCheck(){
		Assert.assertEquals(joinRoomDescriptionEditText.getText(), "Join a room");
	}
}
