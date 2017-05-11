package pom_android.main_tabs;

import java.io.FileNotFoundException;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.pagefactory.AndroidFindBy;
import pom_android.RiotLoginAndRegisterPageObjects;
import pom_android.RiotSettingsPageObjects;
import utility.TestUtilities;

public abstract class RiotTabPageObjects extends TestUtilities{
	protected AndroidDriver<MobileElement> driver;
	public RiotTabPageObjects(AppiumDriver<MobileElement> myDriver){
		driver= (AndroidDriver<MobileElement>) myDriver;
	}
	
	/*
	 * TOOL BAR.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/home_toolbar")
	public MobileElement toolBarView;
	/** Button openning the lateral menu. */
	@AndroidFindBy(xpath="//android.view.View[@resource-id='im.vector.alpha:id/home_toolbar']/android.widget.ImageButton[@content-desc='Navigate up']")//Menu button (opens the lateral menu)
	public MobileElement contextMenuButton;
	/** Search button openning the unified search. */
	@Deprecated@AndroidFindBy(id="im.vector.alpha:id/ic_action_search_room")
	public MobileElement searchButton;
	public void openGlobalSearchLayout(){
		driver.pressKeyCode(AndroidKeyCode.MENU);
		globalSearchItemExpandedMenu.click();
	}
	/**
	 * Filter bar.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/search_src_text")
	public MobileElement filterBarEditText;
	
	/**
	 * Send text in the filter bar.
	 * @param filterText
	 */
	public void useFilterBar(String filterText){
		filterBarEditText.setValue(filterText);
	}
	
	/*
	 * LATERAL MENU
	 */
	@AndroidFindBy(id="im.vector.alpha:id/design_navigation_view")
	public MobileElement lateralMenuView;
	@AndroidFindBy(xpath="//android.widget.FrameLayout//android.widget.CheckedTextView[@text='Settings']")////android.widget.CheckedTextView[@text='Logout']
	public MobileElement settingsButton;
	@AndroidFindBy(xpath="//android.widget.FrameLayout//android.widget.CheckedTextView[@text='Sign out']")////android.widget.CheckedTextView[@text='Logout']
	public MobileElement signOutButton;
	@AndroidFindBy(id="im.vector.alpha:id/home_menu_main_displayname")
	public MobileElement userDisplayNameFromLateralMenu;
	@AndroidFindBy(id="im.vector.alpha:id/home_menu_main_matrix_id")
	public MobileElement userMatrixIdFromLateralMenu;
	
	@AndroidFindBy(xpath="//android.widget.FrameLayout//android.widget.CheckedTextView[@text='Copyright']")//copyright button
	public MobileElement openCopyrightButton;
	
	/**
	 * Get an item from the lateral menu.
	 * @param name
	 * @return
	 */
	public MobileElement getItemMenuByName(String name){
		return lateralMenuView.findElementByName(name);
	}
	
	public RiotSettingsPageObjects openRiotSettingsFromLateralMenu() throws InterruptedException{
		this.contextMenuButton.click();
		this.settingsButton.click();
		return new RiotSettingsPageObjects(driver);
	}
	
	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOut(){
		this.contextMenuButton.click();
		this.signOutButton.click();
		//verifies that sign out pop up is displayed and correct
		Assert.assertEquals(alertDialogMessage.getText(), "For security, logging out will delete any end-to-end encryption keys making previous encrypted chat history unreadable if you log back in.\nSelect export to backup them before signing out.");
		Assert.assertTrue(alertDialogButton1.isDisplayed(),"Cancel button isn't displayed");
		Assert.assertTrue(alertDialogButton2.isDisplayed(),"OK button isn't displayed");
		alertDialogButton2.click();
	}

	/**
	 * Log out from the rooms list, log in with the parameters.</br>
	 * Return a RiotHomePageTabObjects POM.</br> Can be used to renew the encryption keys.
	 * @param username
	 * @param pwd
	 * @return new RiotRoomsListPageObjects
	 */
	public RiotHomePageTabObjects logOutAndLogin(String username, String password, Boolean forceDefaultHs) throws InterruptedException{
		this.logOut();
		RiotLoginAndRegisterPageObjects loginPage= new RiotLoginAndRegisterPageObjects(driver);
		try {
			loginPage.logUser(username, null,password,forceDefaultHs);
		} catch (FileNotFoundException | YamlException e) {
			e.printStackTrace();
		}
		return new RiotHomePageTabObjects(driver);
	}
	
	/*
	 * MAIN PAGE: INVITES, ROOMS, CONTACTS, depending of the tab.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/fragment_container")
	public MobileElement tabMainView;
	
	/*
	 * INVITES
	 */
	/** Invites collapsing bar. */
	@AndroidFindBy(xpath="//android.widget.TextView[contains(@text,'INVITES') and @resource-id='im.vector.alpha:id/section_title']/../..")
	public MobileElement invitesHeadingLayout;
	
	@AndroidFindBy(id="im.vector.alpha:id/invite_view")
	public List<WebElement> invitesList;
	
	public MobileElement getInvitationLayoutByName(String roomName) throws InterruptedException{
		if (waitUntilDisplayed(driver, "//android.widget.TextView[@resource-id='im.vector.alpha:id/room_name' and@text='"+roomName+"']/../../..", true, 20))
		{return driver.findElementByXPath( "//android.widget.TextView[@resource-id='im.vector.alpha:id/room_name' and@text='"+roomName+"']/../../..");}
		else{
			return null;
		}
	}
	
	/**
	 * Check some properties on a room invitation layout.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void checkInvitationLayout(String roomName) throws InterruptedException{
		MobileElement roomInvitationLayout=getInvitationLayoutByName(roomName);
		//room's avatar is not empty
		org.openqa.selenium.Dimension roomAvatar=roomInvitationLayout.findElementById("im.vector.alpha:id/room_avatar_image_view").getSize();
		Assert.assertTrue(roomAvatar.height!=0 && roomAvatar.width!=0, "Riot logo has null dimension");
		//System.out.println(roomInvitationLayout.findElementById("im.vector.alpha:id/roomSummaryAdapter_roomName").getText());
		//! warning is present
		Assert.assertEquals(roomInvitationLayout.findElementById("im.vector.alpha:id/roomSummaryAdapter_unread_count").getText(), "!", "Unread count on the invitation layout isn't present");
		//last message received is not empty
		Assert.assertFalse(roomInvitationLayout.findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage").getText().isEmpty(), "Last received message is empty");
		//the 2 buttons are enabled
		Assert.assertEquals(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_reject_button").getText(), "Reject");
		Assert.assertTrue(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_reject_button").isEnabled(), "Reject button isn't enabled");
		Assert.assertEquals(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_preview_button").getText(), "Preview");
		Assert.assertTrue(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_preview_button").isEnabled(), "Preview button isn't enabled");
	}
	
	/**
	 * Hit the "preview" button on an invitation.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void previewInvitation(String roomName) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/recents_groups_invitation_group", true, 30);
		try {
			MobileElement roomInvitationLayout=getInvitationLayoutByName(roomName);
			roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_preview_button").click();
		} catch (Exception e) {
			Assert.fail("No invitation found with room "+roomName);
		}
	}

	/**
	 * Hit the "reject" button on an invitation.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void rejectInvitation(String roomName) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/recents_groups_invitation_group", true, 30);
		try {
			MobileElement roomInvitationLayout=getInvitationLayoutByName(roomName);
			roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_reject_button").click();
		} catch (Exception e) {
			Assert.fail("No invitation found with room "+roomName);
		}
	}
	
	/*
	 * ROOMS.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/recyclerview")
	public MobileElement roomsListView;
	
	@AndroidFindBy(id="im.vector.alpha:id/room_view")
	public List<WebElement> roomsList;
	
	@AndroidFindBy(id="im.vector.alpha:id/section_layout")
	public List<WebElement> sectionsLayoutList;
	
	/**
	 * Contains rooms and categories (favorites, people, rooms ...)
	 */
	@AndroidFindBy(xpath="//android.support.v7.widget.RecyclerView[@resource-id='im.vector.alpha:id/recyclerview']/*")
	public List<MobileElement> roomsAndCategoriesList;
	
	/**
	 * Hit a section using his name. Can be use to focus on the related rooms or collapse a section. </br>
	 * Use capital letters for ROOMS tab.
	 * @param sectionName
	 */
	public void hitSectionHeader(String sectionName){
		try {
			driver.findElementByXPath("//android.widget.RelativeLayout[@resource-id='im.vector.alpha:id/section_layout']/android.widget.TextView[contains(@text,'"+sectionName+"')]/..").click();
		} catch (Exception e) {
			Assert.fail("No section found with name: "+sectionName);
		}
	}
	
	/**
	 * Return a room as a MobileElement. </br>
	 * Return null if not found.
	 * @param myRoomName
	 * @return
	 */
	public MobileElement getRoomByName(String myRoomName){
		try {
			return (MobileElement) driver.findElementByXPath("//android.support.v7.widget.RecyclerView//android.widget.TextView[@text='"+myRoomName+"']/../..");	
			//return roomsListView.findElementByName(myRoomName);
		} catch (Exception e) {
			System.out.println("No room found with name "+myRoomName);
			return null;
		}
	}
	
	/**
	 * Check that room is in a room category (favorites, people, rooms, etc). </br>
	 * TODO maybe scroll to the end of the list to check the room ?
	 * @param roomNameTest
	 * @param category
	 */
	public Boolean checkRoomInCategory(String roomNameTest, String category) {
		Boolean categoryFound=false, roomFound=false, otherCategoryFound=false;int indexCategory=0;
		System.out.println("Looking for room "+roomNameTest+" in the category "+category);
		//looking for the category
		int index=0;
		do {
			//is the mobilelement a category ?
			if(roomsAndCategoriesList.get(index).getTagName().equals("android.widget.LinearLayout")){
				//is this the searched category ?
				if(roomsAndCategoriesList.get(index).findElementById("im.vector.alpha:id/section_title").getText().contains(category)){
					indexCategory=index;categoryFound=true;
				}
			}
			index++;
		} while (index<=roomsAndCategoriesList.size()-1 && categoryFound==false);
		if(categoryFound==false)return false;
		//looking for room name
		index=indexCategory+1;
		do {
			//is the mobilelement a room ?
			if(roomsAndCategoriesList.get(index).getTagName().equals("android.widget.RelativeLayout")){
				if(roomsAndCategoriesList.get(index).findElementById("im.vector.alpha:id/room_name").getText().contains(roomNameTest)){
					roomFound=true;
				}
			}else if(roomsAndCategoriesList.get(index).getTagName().equals("android.widget.LinearLayout")){//is this a category ?
				otherCategoryFound=true;
			}
			index++;
		} while (index<=roomsAndCategoriesList.size()-1 && roomFound==false && otherCategoryFound==false);
		return roomFound;
	}
	
	/**
	 * Click on the context menu on a room, then choose one of the item : Notifications, Favourite, De-prioritize, Leave Conversation
	 * @param roomName
	 * @param item
	 * @throws InterruptedException 
	 */
	public void clickOnContextMenuOnRoom(String roomName, String item) throws InterruptedException{
		//open contxt menu on a room item
		MobileElement roomItem=getRoomByName(roomName);
		if(null!=roomItem){
			roomItem.findElementById("im.vector.alpha:id/roomSummaryAdapter_action_click_area").click();
			//hit the item on the options
			driver.findElementByXPath("//android.widget.ListView//android.widget.TextView[@text='"+item+"']/../..").click();
			Assert.assertFalse(waitUntilDisplayed(driver,"//android.widget.ListView[count(android.widget.LinearLayout)=4]", false, 0), "Option windows isn't closed");	
		}else{
			System.out.println("No room found with name "+roomName+", impossible to click on item "+item+" on context menu.");
		}
	}
	
	/**
	 * Leave a room from the rooms list.
	 * @param roomName
	 * @throws InterruptedException
	 */
	public void leaveRoom(String roomName) throws InterruptedException{
		clickOnContextMenuOnRoom(roomName, "Leave Conversation");
		alertDialogButton2.click();
	}

	/**
	 * Send back the badge number of a room by his name.</br>
	 * Return null if no badge.
	 * @param myRoomName
	 * @return
	 */
	public Integer getBadgeNumberByRoomName(String myRoomName){
		MobileElement roomItem = getRoomByName(myRoomName);
		try {
			String badgeNumber= roomItem.findElementById("im.vector.alpha:id/room_unread_count").getText();
			return Integer.parseInt(badgeNumber);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Returns the name of a room.
	 * @param room
	 * @return
	 */
	public String getRoomName(MobileElement room){
		if (null!=room) {
			return room.findElementById("im.vector.alpha:id/public_room_name").getText();
		}else{
			return "";
		}
	}
	/**
	 * TODO: this function isn't valid because the unread indicator is always present even if there is not unread message on the room. </br>
	 * Return true if a room has an unread indicator.
	 * @param myRoomName
	 * @return
	 */
	@Deprecated
	public Boolean doesRoomHaveUnreadIndicator(String myRoomName){
		MobileElement roomItem = getRoomByName(myRoomName);
		if(null==roomItem){
			return false;
		}else{
			return !roomItem.findElementsById("im.vector.alpha:id/indicator_unread_message").isEmpty();
		}
	}
	
	/**
	 * Return the last received message of a room by his name.</br>
	 * Message can be text or event. </br>
	 * Return null if no message found.
	 * @param myRoomName
	 * @return
	 */
	public String getLastEventByRoomName(String myRoomName,Boolean withUser){
		try {
			MobileElement roomItem = getRoomByName(myRoomName);
			String messageWithUsername =roomItem.findElementById("im.vector.alpha:id/room_message").getText();
			if(messageWithUsername.indexOf(":")!=-1&&!withUser){
				return messageWithUsername.substring(messageWithUsername.indexOf(":")+2, messageWithUsername.length());
			}else{
				return messageWithUsername;
			}

		} catch (Exception e) {
			return null;
		}
	}
	
	public Boolean isDirectMessageByRoomName(String myRoomName){
		try {
			if(getRoomByName(myRoomName).findElementById("im.vector.alpha:id/direct_chat_indicator")!=null)return true;
		} catch (Exception e) {
			return false;
		}
		return false;
	}
	
	/**
	 * Wait until badge of the room is incremented.
	 * @param myRoomName
	 * @param currentBadge
	 * @throws InterruptedException
	 */
	public void waitForRoomToReceiveNewMessage(String myRoomName, int currentBadge) throws InterruptedException{
		waitUntilDisplayed(driver,"//android.support.v7.widget.RecyclerView//android.widget.TextView[@text='"+myRoomName+"']/../android.widget.TextView[@resource-id='im.vector.alpha:id/room_unread_count' and @text='"+Integer.sum(currentBadge, 1)+"']", true, 5);
	}
	
	/*
	 * FLOATING BUTTONS OR DIALOGS .
	 */
	/** "+" floating button  */
	@AndroidFindBy(id="im.vector.alpha:id/floating_action_button")
	public MobileElement createRoomFloatingButton;
	
	/**
	 * Wait until the spinner isn't displayed anymore.
	 * @param secondsToWait
	 * @throws InterruptedException 
	 */
	public void waitUntilSpinnerDone(int secondsToWait) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/listView_spinner", false, secondsToWait);
	}
	
	/*
	 * 		Expanded menu opened after hitting menu android touch.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/expanded_menu")
	public MobileElement expandedMenu;
	@AndroidFindBy(xpath="//android.widget.TextView[@resource-id='im.vector.alpha:id/title' and @text='Global search']/..")
	public MobileElement globalSearchItemExpandedMenu;
	@AndroidFindBy(xpath="//android.widget.TextView[@resource-id='im.vector.alpha:id/title' and @text='Mark all as read']/..")
	public MobileElement markAllAsReadSearchItemExpandedMenu;
	
	/*
	 * ALERT DIALOG
	 */
	@AndroidFindBy(id="android:id/parentPanel")
	public MobileElement alertDialogParentPanel;
	@AndroidFindBy(id="android:id/message")
	public MobileElement alertDialogMessage;
	@AndroidFindBy(id="android:id/button1")
	public MobileElement alertDialogButton2;
	@AndroidFindBy(id="android:id/button2")
	public MobileElement alertDialogButton1;
	
	/*
	 * BOTTOM BAR.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/bottom_navigation")
	public MobileElement bottomBar;
	@AndroidFindBy(id="im.vector.alpha:id/bottom_action_home")
	public MobileElement homeTabBottomBarButton;
	@AndroidFindBy(id="im.vector.alpha:id/bottom_action_favourites")
	public MobileElement favouriteTabBottomBarButton;
	@AndroidFindBy(id="im.vector.alpha:id/bottom_action_people")
	public MobileElement peopleTabBottomBarButton;
	@AndroidFindBy(id="im.vector.alpha:id/bottom_action_rooms")
	public MobileElement roomsTabBottomBarButton;
	
	/**
	 * Click on the HOME PAGE tab and return a new RiotHomePageObjects.
	 * @throws InterruptedException 
	 */
	public RiotHomePageTabObjects openHomePageTab() throws InterruptedException{
		System.out.println("Hit HOME PAGE tab.");
		homeTabBottomBarButton.click();
		return new RiotHomePageTabObjects(driver);
	}
	
	/**
	 * Click on the FAVOURITES tab and return a new RiotFavouritesTabPageObjects.
	 * @throws InterruptedException 
	 */
	public RiotFavouritesTabPageObjects openFavouriteTab() throws InterruptedException{
		System.out.println("Hit FAVOURITES tab.");
		favouriteTabBottomBarButton.click();
		return new RiotFavouritesTabPageObjects(driver);
	}
	/**
	 * Click on the PEOPLE tab and return a new RiotFavouritesTabPageObjects.
	 * @throws InterruptedException 
	 */
	public RiotPeopleTabPageObjects openPeopleTab() throws InterruptedException{
		System.out.println("Hit PEOPLE tab.");
		peopleTabBottomBarButton.click();
		return new RiotPeopleTabPageObjects(driver);
	}
	/**
	 * Click on the ROOM tab and return a new RiotRoomsTabPageObjects.
	 * @throws InterruptedException 
	 */
	public RiotRoomsTabPageObjects openRoomsTab() throws InterruptedException{
		System.out.println("Hit ROOMS tab.");
		roomsTabBottomBarButton.click();
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
