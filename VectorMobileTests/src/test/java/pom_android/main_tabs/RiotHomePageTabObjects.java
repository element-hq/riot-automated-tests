package pom_android.main_tabs;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import pom_android.RiotContactPickerPageObjects;
import pom_android.RiotNewChatPageObjects;
import pom_android.RiotRoomPageObjects;

public class RiotHomePageTabObjects extends RiotTabPageObjects{

	public RiotHomePageTabObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		super(myDriver);
		super.roomsList=this.roomsList;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/fragment_container", true, 10),"HomePage tab isn't open.");
	}

	/*
	 * INVITES
	 */
	
	/*
	 * ROOMS.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/circular_room_view")
	public List<WebElement> roomsList;
	/**
	 * Contains only the header of the section. His direct parent contains the section and the rooms.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/section_header")
	public List<WebElement> roomsHeader;
	
	/**
	 * Swipe on the left on a room section.
	 */
	public void leftSwipeOnRoomSection(MobileElement roomSection){
		int xStart, y;
		xStart=roomSection.getLocation().getX()+roomSection.getSize().width-10;
		y=roomSection.getLocation().getY()+roomSection.getSize().getHeight()/2;
		driver.swipe(xStart, y, 0, y, 300);
		//new TouchAction(driver).press(xStart, y).waitAction(500).moveTo(0, 0).perform();
	}
	
	/**
	 * Swipe on section until the room is found then returns it.
	 * @param roomSection
	 * @param roomName
	 * @param swipeNumberMax
	 * @return room
	 */
	public MobileElement swipeSectionUntilRoomDisplayed(MobileElement roomSection, String roomName, int swipeNumberMax){
		int nbSwipeDone=0;
		MobileElement room;
		while (null==(room=getRoomFromSectionByName(roomSection,roomName)) && nbSwipeDone<swipeNumberMax) {
			leftSwipeOnRoomSection(roomSection);
			nbSwipeDone++;
		}
		if(null==room)
			System.out.println("Room "+roomName+ " not found after "+nbSwipeDone+" swipes on his section.");
		return room;
	}
	
	/**
	 * Hit a section using his name. Can be use to focus on the related rooms or collapse a section.
	 * @param sectionName
	 */
	public void hitSectionHeader(String sectionName){
		try {
			driver.findElementByXPath("//android.widget.RelativeLayout/android.widget.TextView[contains(@text,'"+sectionName+"')]").click();
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
			return (MobileElement) driver.findElementByXPath("//android.support.v7.widget.RecyclerView//android.widget.TextView[@text='"+myRoomName+"']/..");	
			//return roomsListView.findElementByName(myRoomName);
		} catch (Exception e) {
			System.out.println("No room found with name "+myRoomName);
			return null;
		}
	}
	
	/**
	 * Search and returns a room from a section (Favourites, people, rooms, low priority).
	 * @param section
	 * @param myRoomName
	 * @return
	 */
	public MobileElement getRoomFromSectionByName(MobileElement section,String myRoomName){
		try {
			return section.findElementByXPath("//android.widget.RelativeLayout[@resource-id='im.vector.alpha:id/circular_room_view']/android.widget.TextView[@text='"+myRoomName+"']");
		} catch (Exception e) {
			System.out.println("No room found with name "+myRoomName+ " in this section.");
			return null;
		}
	}
	
	/**
	 * Only for HOME PAGE. </br>
	 * Check that room is in a room category (favorites, people, rooms, etc). </br>
	 * TODO maybe scroll to the end of the list to check the room ?
	 * @param roomNameTest
	 * @param category : can be : Favourites, People, Rooms, Low priority
	 */
	public Boolean checkRoomInCategory(String roomNameTest, MobileElement roomSection, int nbSwipeTrials) {
		System.out.println("Looking for room "+roomNameTest+" in the category "+roomSection);
		return null!=swipeSectionUntilRoomDisplayed(roomSection, roomNameTest,nbSwipeTrials);
	}
	
	/**
	 * Click on the context menu on a room, then choose one of the item : Notifications, Favourite, De-prioritize, Direct Chat, Leave Conversation
	 * @param roomName
	 * @param item
	 * @throws InterruptedException 
	 */
	public void clickOnContextMenuOnRoom(String roomName, String item) throws InterruptedException{
		//open contxt menu on a room item
		MobileElement roomItem=getRoomByName(roomName);
		if(null!=roomItem){
			TouchAction longPressAction = new TouchAction(driver);
			longPressAction.longPress(roomItem, 500).perform();
			//hit the item on the options
			driver.findElementByXPath("//android.widget.ListView//android.widget.TextView[@text='"+item+"']/../..").click();
			Assert.assertFalse(waitUntilDisplayed(driver,"//android.widget.ListView[count(android.widget.LinearLayout)=4]", false, 0), "Option windows isn't closed");	
		}else{
			System.out.println("No room found with name "+roomName+", impossible to click on item "+item+" on context menu.");
		}
	}
	
	/*
	 * 		Favourites Section.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/favourites_section")
	public MobileElement favouritesSectionLayout;
	
	/*
	 * 		People Section.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/direct_chats_section")
	public MobileElement directChatSectionLayout;
	
	/*
	 * 		Rooms Section.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/rooms_section")
	public MobileElement roomsSectionLayout;
	/*
	 * 		Low Priority Section.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/low_priority_section")
	public MobileElement lowPrioritySectionLayout;
	
	/*
	 * FLOATING BUTTONS OR DIALOGS .
	 */
	/*
	 * START CHAT / CREATE ROOM
	 */
	@AndroidFindBy(id="android:id/select_dialog_listview")
	public MobileElement selectRoomTypeCreationListView;
	@AndroidFindBy(xpath="//android.widget.CheckedTextView[@text='Start chat']")
	public MobileElement startChatCheckedTextView;
	@AndroidFindBy(xpath="//android.widget.CheckedTextView[@text='Create room']")
	public MobileElement createRoomCheckedTextView;
	@AndroidFindBy(xpath="//android.widget.Button[@text='Cancel']")
	public MobileElement cancelCreationListButton;
	@AndroidFindBy(xpath="//android.widget.Button[@text='OK']")
	public MobileElement okCreationListButton;

	/**
	 * Create a new room : click on plus button, then create room item. </br>
	 * Return a RiotRoomPageObjects object.
	 * @return
	 */
	public RiotRoomPageObjects createRoom(){
		createRoomFloatingButton.click();
		createRoomCheckedTextView.click();
		return new RiotRoomPageObjects(driver);
	}
	
	/**
	 * Start a new chat with a user and returns the new created room. 
	 * @param displayNameOrMatrixId
	 * @return RiotRoomPageObjects
	 * @throws InterruptedException 
	 */
	public RiotRoomPageObjects startChat(String displayNameOrMatrixId) throws InterruptedException{
		createRoomFloatingButton.click();
		startChatCheckedTextView.click();
		RiotContactPickerPageObjects inviteViewDevice1=new RiotContactPickerPageObjects(driver);
		inviteViewDevice1.searchAndSelectMember(getMatrixIdFromDisplayName(displayNameOrMatrixId));
		RiotNewChatPageObjects newChatViewDevice1= new RiotNewChatPageObjects(driver);
		newChatViewDevice1.confirmRoomCreationButton.click();
		return new RiotRoomPageObjects(driver);
	}
}
