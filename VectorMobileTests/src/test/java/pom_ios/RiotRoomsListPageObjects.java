package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import pom_ios.RiotRoomPageObjects;
import utility.TestUtilities;

public class RiotRoomsListPageObjects extends TestUtilities{
private AppiumDriver<MobileElement> driver;
	
	public RiotRoomsListPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		//ExplicitWait(driver,this.roomsAndCategoriesList);
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeOther[2]//XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeTable", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="settings icon")
	public MobileElement settingsButton;
	@iOSFindBy(accessibility="Messages")
	public MobileElement messagesStaticText;
	@iOSFindBy(accessibility="search icon")
	public MobileElement searchButton;
	
	
	/*
	 * ROOMS
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeOther[2]//XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeTable")
	public MobileElement roomsAndCategoriesListTable;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeOther[2]//XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeTable/XCUIElementTypeCell")
	public List<MobileElement> roomsList;
	
	/**
	 * Wait until there is at least 1 room in the rooms list.
	 * @throws InterruptedException
	 */
	public void waitUntilRoomsLoaded() throws InterruptedException{
		int timewaited=0;
		int nbRooms;
		do {
			nbRooms=roomsList.size();
			if(nbRooms==0)Thread.sleep(500);
			timewaited++;
		} while (nbRooms==0 && timewaited<=10);
	}
	/**
	 * Return a room as a MobileElement.</br>
	 * Return null if not found.
	 * @param myRommName
	 * @return 
	 */
	public MobileElement getRoomByName(String myRommName){
		try {
			return roomsAndCategoriesListTable.findElementByXPath("//XCUIElementTypeCell/XCUIElementTypeStaticText[@value='"+myRommName+"']");
		} catch (Exception e) {
			return null;
		}
	}
	
	/*
	 * BOTTOM
	 */
	@iOSFindBy(accessibility="create_room")
	public MobileElement plusRoomButton;
	
	/*
	 * START / CREATE ROOM SHEET. Opened after click on plus button.
	 */
	@iOSFindBy(accessibility="Start chat")
	public MobileElement startChatButton;
	@iOSFindBy(accessibility="Create room")
	public MobileElement createRoomButton;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelCreationButton;
	
	/**
	 * Create a new room : click on plus button, then create room item. </br>
	 * Return a RiotRoomPageObjects object.
	 * @return
	 */
	public RiotRoomPageObjects createRoom(){
		 plusRoomButton.click();
		 createRoomButton.click();
		 return new RiotRoomPageObjects(driver);
	 }
	
	/*
	 * SETTINGS
	 */
	//@iOSFindBy(xpath="//UIAButton[@name='Sign Out']")
	//@iOSFindBy(xpath="///XCUIElementTypeWindow[1]//XCUIElementTypeTableView[1]//XCUIElementTypeTableCell[1]//XCUIElementTypeButton[@value='Sign Out']")
	//@iOSFindBy()
	//@iOSFindBy(xpath="//XCUIElementTypeButton[@name='Sign Out']")
	@iOSFindBy(accessibility="Sign Out")
	public MobileElement signOutButton;
	
	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOut(){
		this.settingsButton.click();
		this.signOutButton.click();
	}
}
