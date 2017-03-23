package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotRoomsListPageObjects extends TestUtilities{
private AppiumDriver<MobileElement> driver;
	
	public RiotRoomsListPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		//ExplicitWait(driver,this.roomsAndCategoriesList);
		try {
			//waitUntilDisplayed((IOSDriver<MobileElement>) driver,"RecentsVCTableView", true, 5);
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"RecentsVCTableView", true, 5);
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
	 * Check that room is in a room category (favorites, people, rooms, etc). </br>
	 * TODO maybe scroll to the end of the list to check the room ?
	 * @param roomNameTest
	 * @param category
	 */
	public Boolean checkRoomInCategory(String roomNameTest, String category) {
		System.out.println("Looking for room "+roomNameTest+" in the category "+category);
		List<MobileElement>roomsAndSection=driver.findElementsByXPath("//XCUIElementTypeTable[@name='RecentsVCTableView']/*");
		
		int indexLastElement=roomsAndSection.size()-1;
		Boolean categoryFound=false, otherCategoryFound=false, roomFound=false;
		int actualIndex=0;
		do {
			if(roomsAndSection.get(actualIndex).getTagName().equals("XCUIElementTypeOther")){
				if(roomsAndSection.get(actualIndex).findElementByClassName("XCUIElementTypeStaticText").getText().equals(category)){
					categoryFound=true;
				}
			}
			actualIndex++;
		} while (!categoryFound && actualIndex<indexLastElement);
		if(!categoryFound){
			System.out.println("Categorie "+category+ " not found.");
		}else{
			do {
				if(roomsAndSection.get(actualIndex).getTagName().equals("XCUIElementTypeOther")){
					if(roomsAndSection.get(actualIndex).findElementByClassName("XCUIElementTypeStaticText").getText().equals(category)){
						otherCategoryFound=true;
					}
				}else{
					if(roomsAndSection.get(actualIndex).findElementByAccessibilityId("TitleLabel").getText().equals(roomNameTest)){
						roomFound=true;
					}
				}
				actualIndex++;
			} while (!otherCategoryFound && !roomFound && actualIndex<indexLastElement);
		}
		return roomFound;
		
	}
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
	 * @param myRoomName
	 * @return 
	 * @throws InterruptedException 
	 */
	public MobileElement getRoomByName(String myRoomName) throws InterruptedException{
		waitUntilDisplayed(driver, "ROOMS",true, 10);
		try {
			return roomsAndCategoriesListTable.findElementByXPath("//XCUIElementTypeCell[@name='RecentTableViewCell']/XCUIElementTypeStaticText[@name='TitleLabel' and @value='"+myRoomName+"']/..");
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Click on the context menu on a room, then choose one of the item : dm, notifications, favourite, bottom, close
	 * @param roomName
	 * @param item: dm, notifications, favourite, bottom, close
	 * @throws InterruptedException 
	 */
	public void clickOnSwipedMenuOnRoom(String roomName, String item) throws InterruptedException {
		MobileElement roomItem=getRoomByName(roomName);
		if(null==roomItem){
			System.out.println( "Room "+roomName+" not found, impossible to swipe on it.");
		}else{
			//swipe on the room item
			roomItem.swipe(SwipeElementDirection.LEFT, 5, 200, 10);
			//click on the button revealed by the swipe
			int indexButton=4;
			switch (item) {
			case "dm":
				indexButton=0;
				break;
			case "notifications":
				indexButton=1;
				break;
			case "favourite":
				indexButton=2;
				break;
			case "bottom":
				indexButton=3;
				break;
			case "close":
				indexButton=4;
				break;
			default:
				Assert.fail("Wrong parameter item: "+item);
				break;
			}
			roomItem.findElementsByClassName("XCUIElementTypeButton").get(indexButton).click();
		}	
	}
	
	/**
	 * Return the last received message of a room by his name.</br>
	 * Message can be text or event. </br>
	 * Return null if no message found.
	 * @param myRoomName
	 * @return
	 * @throws InterruptedException 
	 */
	public String getReceivedMessageByRoomName(String myRoomName) throws InterruptedException{
		String messageWithUsername= getRoomByName(myRoomName).findElementByAccessibilityId("LastEventDescription").getText();
		try {
			if(messageWithUsername.indexOf(":")!=-1){
				return messageWithUsername.substring(messageWithUsername.indexOf(":")+2, messageWithUsername.length());
			}else{
				return messageWithUsername;
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Send back the badge number of a room by his name.</br>
	 * Return null if no badge.
	 * @param myRoomName
	 * @return
	 * @throws InterruptedException 
	 */
	public Integer getBadgeNumberByRoomName(String myRoomName) throws InterruptedException{
		MobileElement badge;
		try {
			badge=getRoomByName(myRoomName).findElementByAccessibilityId("MissedNotifAndUnreadBadge");
			return Integer.parseInt(badge.getText());
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Wait until badge of the room is incremented.
	 * @param myRoomName
	 * @param currentBadge
	 * @throws InterruptedException
	 */
	public void waitForRoomToReceiveNewMessage(String myRoomName, int currentBadge) throws InterruptedException{
		if(0==currentBadge){
			ExplicitWait(driver, getRoomByName(myRoomName).findElementByAccessibilityId("MissedNotifAndUnreadBadge"));
		}else{
			waitUntilPropertyIsSet(getRoomByName(myRoomName).findElementByAccessibilityId("MissedNotifAndUnreadBadge"), "value", String.valueOf(currentBadge+1), true, 5);	
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
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionStart chat")
	public MobileElement startChatButton;
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionCreate room")
	public MobileElement createRoomButton;
	@iOSFindBy(accessibility="HomeVCCreateRoomAlertActionCancel")
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
	/*
	 * NAV BAR
	 */
	@iOSFindBy(accessibility="SettingsVCNavBarSaveButton")
	public MobileElement saveNavBarButton;
	@iOSFindBy(accessibility="Back")
	public MobileElement backMenuButton;
	@iOSFindBy(accessibility="SettingsVCSignOutButton")
	public MobileElement signOutButton;
	
	/*
	 * USER SETTINGS
	 */
	@iOSFindBy(accessibility="SettingsVCDisplayNameTextField")
	public MobileElement displayNameTextField;
	@iOSFindBy(accessibility="SettingsVCChangePwdStaticText")
	public MobileElement changePasswordStaticText;
	@iOSFindBy(accessibility="SettingsVCSignoutAlertActionSign Out")
	public MobileElement signOutAlertDialogButtonConfirm;
	@iOSFindBy(accessibility="SettingsVCSignoutAlertActionCancel")
	public MobileElement signOutAlertDialogButtonCancel;
	
	
	/**
	 * From the settings view, erase the display name and set a new one.</br>
	 * It doesn't click on the save button.
	 * @param newDisplayName
	 */
	public void changeDisplayNameFromSettings(String newDisplayName){
		displayNameTextField.click();
		displayNameTextField.clear();
		displayNameTextField.setValue(newDisplayName);
	}
	
	/**
	 * From the settings view, hit the Change password item, change the password in the AlertDialog and click on save on this alert.
	 * </br> Then click on the confirmation alertbox 'Your pwd have been updated'.
	 * @param oldPwd
	 * @param newPwd
	 * @throws InterruptedException 
	 */
	public void changePasswordFromSettings(String oldPwd, String newPwd, Boolean expectedCorrectlyChange) throws InterruptedException{
		changePasswordStaticText.click();
		driver.getKeyboard().sendKeys(oldPwd+"\n");
		driver.getKeyboard().sendKeys(newPwd+"\n");
		driver.getKeyboard().sendKeys(newPwd);
		//driver.findElementByAccessibilityId("Save").click();
		driver.findElementsByClassName("XCUIElementTypeCollectionView").get(1).findElementsByClassName("XCUIElementTypeCell").get(1).click();
		if(expectedCorrectlyChange){
			Assert.assertTrue(waitUntilDisplayed(driver, "Your password has been updated", true, 10), "Password updated alert dialog isn't displayed after changing the password");
			driver.findElementByAccessibilityId("SettingsVCOnPasswordUpdatedAlertActionOK").click();
		}else{
			Assert.assertTrue(waitUntilDisplayed(driver, "Fail to update password", true, 10), "Password updated fail dialog isn't displayed after changing the password");
			driver.findElementByAccessibilityId("SettingsVCPasswordChangeFailedAlertActionOK").click();
		}

		
	}
	
	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOutFromRoomsList(){
		this.settingsButton.click();
		logOutFromSettingsView();
	}
	/**
	 * Log-out from Riot from the settings view.
	 */
	public void logOutFromSettingsView(){
		this.signOutButton.click();
		signOutAlertDialogButtonConfirm.click();
	}
	/**
	 * Log out from the rooms list, log in with the parameters.</br>
	 * Return a RiotRoomsListPageObjects POM.</br> Can be used to renew the encryption keys.
	 * @param username
	 * @param pwd
	 * @return new RiotRoomsListPageObjects
	 */
	public RiotRoomsListPageObjects logOutAndLogin(String username, String pwd) {
		this.logOutFromRoomsList();
		RiotLoginAndRegisterPageObjects loginPage= new RiotLoginAndRegisterPageObjects(driver);
		loginPage.fillLoginForm(username,null, pwd);
		return new RiotRoomsListPageObjects(driver);
	}
	/**
	 * Log out from the rooms list, log in with the parameters.</br>
	 * Return a RiotRoomsListPageObjects POM.</br> Can be used to renew the encryption keys.
	 * @param username
	 * @param pwd
	 * @return new RiotRoomsListPageObjects
	 */
	public RiotRoomsListPageObjects logOutAndLoginFromSettingsView(String username, String pwd) {
		this.logOutFromSettingsView();
		RiotLoginAndRegisterPageObjects loginPage= new RiotLoginAndRegisterPageObjects(driver);
		loginPage.fillLoginForm(username,null, pwd);
		return new RiotRoomsListPageObjects(driver);
	}
}
