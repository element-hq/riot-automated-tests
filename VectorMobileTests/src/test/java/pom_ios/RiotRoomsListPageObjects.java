package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

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
			//waitUntilDisplayed((IOSDriver<MobileElement>) driver,"RecentsVCTableView", true, 5);
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"Messages", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(accessibility="Messages")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="settings icon")
	public MobileElement settingsButton;
	@iOSFindBy(xpath="//XCUIElementTypeNavigationBar[@name='Messages']/XCUIElementTypeStaticText")
	public MobileElement messagesStaticText;
	@iOSFindBy(accessibility="search icon")
	public MobileElement searchButton;
	
	/*
	 * INVITES
	 */
	@iOSFindBy(accessibility="InviteRecentTableViewCell")
	public List<MobileElement> invitationCells;
	
	public MobileElement getInvitationCellByName(String roomName){
		for (MobileElement invitationCell : invitationCells) {
			if(invitationCell.findElementByAccessibilityId("TitleLabel").getText().equals(roomName)){
				return invitationCell;
			}
		}
		System.out.println("No invitation cell of room: "+roomName);
		return null;
	}
	/**
	 * Hit the "preview" button on an invitation.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void previewInvitation(String roomName) throws InterruptedException{
		waitUntilDisplayed(driver, "InviteRecentTableViewCell", true, 30);
		try {
			MobileElement roomInvitationCell=getInvitationCellByName(roomName);
			roomInvitationCell.findElementByAccessibilityId("RightButton").click();
		} catch (Exception e) {
			Assert.fail("No invitation found for room "+roomName);
		}
	}
	
	/**
	 * Hit the "reject" button on an invitation.
	 * @param roomName
	 */
	public void rejectInvitation(String roomName){
		MobileElement roomInvitationLayout=getInvitationCellByName(roomName);
		roomInvitationLayout.findElementByAccessibilityId("LeftButton").click();
	}
	
	/*
	 * ROOMS
	 */
	@iOSFindBy(accessibility="RecentsVCTableView")
	public MobileElement roomsAndCategoriesListTable;
	@iOSFindBy(xpath="//XCUIElementTypeTable[@name='RecentsVCTableView']/XCUIElementTypeCell")
	public List<MobileElement> roomsList;
	/**
	 * Faster than roomsList.
	 */
	@iOSFindBy(accessibility="RecentTableViewCell")
	public List<MobileElement> roomsList_bis;
	
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
			return roomsAndCategoriesListTable.findElementByXPath("//XCUIElementTypeCell/XCUIElementTypeStaticText[@value='"+myRommName+"']/..");
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
		 if(driver.findElementByClassName("XCUIElementTypeCollectionView")==null){
			 plusRoomButton.click();
		 }
		 createRoomButton.click();
		 return new RiotRoomPageObjects(driver);
	 }
	
	/*
	 * SETTINGS VIEW
	 */
	@iOSFindBy(accessibility="SettingsVCSignOutButton")
	public MobileElement signOutButton;
	
	@iOSFindBy(xpath="//XCUIElementTypeTable//XCUIElementTypeCell//XCUIElementTypeStaticText[@name='Display Name']/parent::*//*[1]")
	public MobileElement displayNameTextField;
	
	@iOSFindBy(accessibility="SettingsVCSignoutAlertActionSign Out")
	public MobileElement signOutAlertDialogButtonConfirm;
	@iOSFindBy(accessibility="SettingsVCSignoutAlertActionCancel")
	public MobileElement signOutAlertDialogButtonCancel;
	
	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOut(){
		this.settingsButton.click();
		this.signOutButton.click();
		signOutAlertDialogButtonConfirm.click();
	}
}
