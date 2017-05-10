package pom_ios.main_tabs;

import java.util.List;

import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.iOSFindBy;
import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.RiotSettingsPageObjects;
import utility.TestUtilities;

public abstract class RiotTabPageObjects extends TestUtilities {
	protected IOSDriver<MobileElement> driver;
	public RiotTabPageObjects(AppiumDriver<MobileElement> myDriver){
		driver= (IOSDriver<MobileElement>) myDriver;
	}
	
	/*
	 * NAVIGATION BAR.
	 */
	@iOSFindBy(className="XCUIElementTypeNavigationBar")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="settings icon")
	public MobileElement settingsButton;
	/** Search button openning the unified search. */
	@iOSFindBy(accessibility="search icon")
	public MobileElement searchButton;
	
	/**
	 * Return the static text containing the name of the tab.
	 * @return
	 */
	public MobileElement getNavigationBarStaticText(){
		return navigationBar.findElementByClassName("XCUIElementTypeStaticText");
	}
	
	/**
	 * Hit the settings button in the navigation bar and returns a RiotSettingsPageObjects.
	 * @return
	 */
	public RiotSettingsPageObjects openRiotSettings(){
		settingsButton.click();
		return new RiotSettingsPageObjects(driver);
	}
	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOutFromRoomsList(){
		RiotSettingsPageObjects settingsPage = openRiotSettings();
		settingsPage.logOutFromSettingsView();
	}
	
	/**
	 * Log out from the rooms list, log in with the parameters.</br>
	 * Return a RiotHomePageTabObjects POM.</br> Can be used to renew the encryption keys.
	 * @param username
	 * @param pwd
	 * @return new RiotRoomsListPageObjects
	 * @throws InterruptedException 
	 */
	public RiotHomePageTabObjects logOutAndLogin(String username, String pwd) throws InterruptedException {
		logOutFromRoomsList();
		RiotLoginAndRegisterPageObjects loginPage= new RiotLoginAndRegisterPageObjects(driver);
		loginPage.logUser(username, null, pwd);
		return new RiotHomePageTabObjects(driver);
	}
	
	/*
	 * MAIN PAGE: INVITES, ROOMS, CONTACTS, depending of the tab.
	 */
	/**
	 * Main view of the tab. His Accessibility id change according to the tab.
	 */
	protected MobileElement tabMainView;
	
	/*
	 * 		FILTER BAR.
	 */
	@iOSFindBy(className="XCUIElementTypeSearchField")
	public MobileElement filterBarSearchField;
	@iOSFindBy(accessibility="Clear text")
	public MobileElement clearTextFilterBarButton;
	@iOSFindBy(accessibility="Clear text")
	public MobileElement cancelFilterBarButton;
	
	/**
	 * Swipe down on the main tab view to make appear the filter bar.
	 */
	public void displayFilterBarBySwipingDown(){
		//tabMainView.
		int mainViewHeight=tabMainView.getSize().getHeight();
		int mainViewWidth=tabMainView.getSize().getWidth();
		//TouchAction swipeDown = new IOSTouchAction(driver);
		//swipeDown.longPress(tabMainView).moveTo(mainViewHeight, mainViewWidth);
		//swipeDown.press(roomsTableView).moveTo(mainViewHeight, mainViewWidth).release();
		tabMainView.swipe(SwipeElementDirection.DOWN,mainViewHeight/2,mainViewWidth/2, 0);
	}
	
	/**
	 * Send text in the filter bar.
	 * @param filterText
	 */
	public void useFilterBar(String filterText){
		filterBarSearchField.setValue(filterText);
	}
	
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
	 * ROOMS.
	 */
	@iOSFindBy(accessibility="RecentTableViewCell")
	public List<MobileElement>roomsCellsList;
	
	/**
	 * TableView containing the rooms cells. His Accessibility id change according to the tab.
	 */
	protected MobileElement roomsTableView;
	
	
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
			nbRooms=roomsCellsList.size();
			if(nbRooms==0)Thread.sleep(500);
			timewaited++;
		} while (nbRooms==0 && timewaited<=10);
	}
	
	/**
	 * Return a room as a MobileElement using his title.</br>
	 * Return null if not found.
	 * @param myRoomName
	 * @return
	 */
	public MobileElement getRoomByName(String myRoomName){
		try {
			return roomsTableView.findElementByXPath("//XCUIElementTypeCell[@name='RecentTableViewCell']/XCUIElementTypeStaticText[@name='TitleLabel' and @value='"+myRoomName+"']/..");
		} catch (Exception e) {
			System.out.println("No room found with name "+myRoomName);
			return null;
		}
	}
	
	/**
	 * Return a room as a MobileElement using his index.
	 * @param index
	 * @return
	 */
	public MobileElement getRoomByIndex(int index){
		try {
			return roomsCellsList.get(index);
		} catch (Exception e) {
			System.out.println("No room at index "+index);
			return null;
		}
	}
	
	/**
	 * Swipe on a room to display context actions items, then choose one of the item : dm, notifications, favourite, bottom, close
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
			case "dm":indexButton=0;break;
			case "notifications":indexButton=1;break;
			case "favourite":indexButton=2;break;
			case "bottom":indexButton=3;break;
			case "close":indexButton=4;break;
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
	 * @param myRoomName, withUser
	 * @return
	 * @throws InterruptedException 
	 */
	public String getLastEventByRoomName(String myRoomName, Boolean withUser) throws InterruptedException{
		String messageWithUsername= getRoomByName(myRoomName).findElementByAccessibilityId("LastEventDescription").getText();
		try {
			if(messageWithUsername.indexOf(":")!=-1&&!withUser){
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
	 * FLOATING BUTTONS OR DIALOGS .
	 */
	/** "+" floating button  */
	@iOSFindBy(accessibility="create_room")
	public MobileElement createRoomFloatingButton;
	
	/*
	 * BOTTOM: TABS.
	 */
	@iOSFindBy(className="XCUIElementTypeTabBar")
	public MobileElement tabBar;
	
	/**
	 * Return a list of the 4 tabs: Home, Favourite, People, Rooms as MobileElements.
	 * @return
	 */
	public List<MobileElement> listTabs(){
		return tabBar.findElementsByClassName("XCUIElementTypeButton");
	}
	public MobileElement getHomeTab(){
		return listTabs().get(0);
	}
	public MobileElement getFavouritesTab(){
		return listTabs().get(1);
	}
	public MobileElement getPeopleTab(){
		return listTabs().get(2);
	}
	public MobileElement getRoomsTab(){
		return listTabs().get(3);
	}
	
	/**
	 * Click on the HOME PAGE tab and return a new RiotHomePageObjects.
	 * @throws InterruptedException 
	 */
	public RiotHomePageTabObjects openHomePageTab() throws InterruptedException{
		System.out.println("Hit HOME PAGE tab.");
		getHomeTab().click();
		return new RiotHomePageTabObjects(driver);
	}
	
	/**
	 * Click on the FAVOURITES tab and return a new RiotFavouritesTabPageObjects.
	 * @throws InterruptedException 
	 */
	public RiotFavouritesTabPageObjects openFavouriteTab() throws InterruptedException{
		System.out.println("Hit FAVOURITES tab.");
		getFavouritesTab().click();
		return new RiotFavouritesTabPageObjects(driver);
	}
	/**
	 * Click on the PEOPLE tab and return a new RiotFavouritesTabPageObjects.
	 * @throws InterruptedException 
	 */
	public RiotPeopleTabPageObjects openPeopleTab() throws InterruptedException{
		System.out.println("Hit PEOPLE tab.");
		getPeopleTab().click();
		return new RiotPeopleTabPageObjects(driver);
	}
	/**
	 * Click on the ROOM tab and return a new RiotRoomsTabPageObjects.
	 * @throws InterruptedException 
	 */
	public RiotRoomsTabPageObjects openRoomsTab() throws InterruptedException{
		System.out.println("Hit ROOMS tab.");
		getRoomsTab().click();
		return new RiotRoomsTabPageObjects(driver);
	}
	
	/**
	 * Return the unread counter badge as a String from a tab button of the bottom bar.
	 * @param tabButtonLayout
	 * @return
	 */
	public String getUnreadCounterBadgeFromTab(MobileElement tabButtonLayout){
		String buttonTextValue=tabButtonLayout.getText();
		if (null==buttonTextValue)
			System.out.println("There is no unread counter badge on this tab button "+tabButtonLayout.getTagName());
		return tabButtonLayout.getText();
	}
}
