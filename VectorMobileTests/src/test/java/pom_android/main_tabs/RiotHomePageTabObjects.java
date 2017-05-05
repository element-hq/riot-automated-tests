package pom_android.main_tabs;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
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
		//new TouchAction(driver).press(room.getLocation().getX(), room.getLocation().getY()).waitAction(500).moveTo(20, room.getLocation().getY()).perform();
	}
	
	/**
	 * Swipe on section until the room is found.
	 * @param roomSection
	 * @param roomName
	 * @param swipeNumberMax
	 * @return room
	 */
	public MobileElement swipeSectionUntilRoomDisplayed(MobileElement roomSection, String roomName, int swipeNumberMax){
		int nbSwipeDone=0;
		MobileElement room;
		while (null==(room=getRoomByName(roomName)) && nbSwipeDone<swipeNumberMax) {
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
