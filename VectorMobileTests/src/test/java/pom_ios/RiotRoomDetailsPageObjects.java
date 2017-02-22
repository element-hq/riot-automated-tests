package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotRoomDetailsPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotRoomDetailsPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"Room Details", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(accessibility="Room Details")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="Back")
	public MobileElement menuBackButton;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeNavigationBar/XCUIElementTypeStaticText")
	public MobileElement roomDetailsStaticText;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar/XCUIElementTypeButton[@name='Cancel']")
	public MobileElement roomDetailsCancelButton;
	@iOSFindBy(accessibility="Done")
	public MobileElement roomDetailsDoneButton;
	
	/*
	 * COMMON.
	 */
	@iOSFindBy(accessibility="SegmentedVCUITableViewContainer")
	public MobileElement segmentedTable;
	
	/*
	 * TABS: MEMBERS, FILES, SETTINGS
	 */
	/**
	 * Contains the 3 tabs : members, files, settings.
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeOther[count(XCUIElementTypeStaticText)=3]")
	public MobileElement tabsTypeOther;
	@iOSFindBy(accessibility="SegmentedVCSectionLabel0")
	public MobileElement membersTab;
	@iOSFindBy(accessibility="SegmentedVCSectionLabel1")
	public MobileElement filesTab;
	@iOSFindBy(accessibility="SegmentedVCSectionLabel2")
	public MobileElement settingsTab;

	
	/**
	 * Loading wheel.
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeActivityIndicator[@name='In progress']")
	public MobileElement loadingWheelActivityIndicator;
	/**
	 * Wait until the loading wheel isn't displayed anymore. </br>
	 * Can be used after hitting Done on the navigation bar.
	 * @throws InterruptedException 
	 */
	public void waitUntilDetailsDone() throws InterruptedException{
		waitUntilDisplayed(driver, "//*[@name='In progress']", false, 10);
	}
	/*
	 * MEMBERS TAB
	 */
	//FILTER BAR
	@iOSFindBy(accessibility="RoomParticipantsVCSearchBarView")
	public MobileElement searchInviteBarView;
	@iOSFindBy(accessibility="RoomParticipantsVCSearchBarView")
	public MobileElement searchInviteSearchField;
	@iOSFindBy(accessibility="Clear text")
	public MobileElement clearFilteredBarButton;
	
	//MEMBERS LIST
	///AppiumAUT/XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeTable
	@iOSFindBy(accessibility="RoomParticipantsVCTableView")
	public MobileElement membersTableView;
	@iOSFindBy(accessibility="ContactTableViewCell")
	public List<MobileElement> membersList;
	@iOSFindBy(accessibility="add_participant")
	public MobileElement addParticipantButton;
	public List<MobileElement> getCategories(){
		
		return membersTableView.findElementsByClassName("XCUIElementTypeOther");
	}
	
	/**
	 * Add a participant from room details, Members tab.
	 * @param participant2Adress
	 */
	public void addParticipant(String participant2Adress) {
		addParticipantButton.click();
		RiotContactPickerPageObjects contactPicker = new RiotContactPickerPageObjects(driver);
		contactPicker.searchAndSelectMember(participant2Adress);
	}
	
	/**
	 * From the room details people tab, enter a text on the filter room members edittext.
	 * @param filter
	 */
	public void filterOnRoomMembersList(String filter){
		searchInviteBarView.setValue(filter);
	}
	/**
	 * Return the displayname from a filtered member from the people tab.
	 * @param member
	 * @return
	 */
	public String getDisplayNameOfMemberFromPeopleTab(MobileElement member){
		try {
			return member.findElementByAccessibilityId("MemberDisplayName").getText();
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Swipe to the left on a member item, then hit the "eject" button.</br>
	 * Wait until the progress bar is gone.
	 * @param memberItem
	 * @throws InterruptedException 
	 */
	public void removeMemberWithSwipeOnItem(MobileElement memberItem) throws InterruptedException{
		int x = memberItem.getLocation().getX();
		int y= memberItem.getLocation().getY();
		int witdthX=memberItem.getSize().getWidth();
		int xFirst=x+witdthX-80;
		float xEndF=(float) (0.3*witdthX);
		int xEnd=(int)xEndF;
		//Swipe to the left on the item.
		driver.swipe(witdthX+5,y, xFirst-50,y,500);
		//Hit on eject button
		memberItem.findElementByClassName("XCUIElementTypeButton").click();
		//hit on remove confirmation button from dialog alert
		alertRemoveConfirmationRemoveButton.click();
		
//		waitUntilDisplayed(driver, "android:id/parentPanel", true, 10);
//		Assert.assertEquals(inputAndroidDialogNameTextView.getText(), "Remove?");
//		inputDialogOkButton.click();
//		waitUntilDisplayed(driver, "//android.widget.ProgressBar", false, 10);
	}
	
	//Remove alert dialog
	@iOSFindBy(accessibility="RoomParticipantsVCKickAlertActionRemove")
	public MobileElement alertRemoveConfirmationRemoveButton;
	@iOSFindBy(accessibility="RoomParticipantsVCKickAlertActionCancel")
	public MobileElement alertRemoveConfirmationCancelButton;
	
	/**
	 * Wait until INVITED categorie is displayed.</br>
	 * Can be used to wait until an invitation is done.
	 * @throws InterruptedException
	 */
	public void waitUntilInvitedCategorieIsDisplayed(Boolean invitedCategorieDisplayed) throws InterruptedException{
		int maxToWait=30;
		int timeWaited=0;
		int size;
		if(invitedCategorieDisplayed){
			size=1;
		}else{
			size=0;
		}
		waitUntilDisplayed(driver, "RoomParticipantsVCTableView", true, 5);
		while (getCategories().size()!=size && timeWaited <=maxToWait) {
			Thread.sleep(500);
			timeWaited++;
		}
		if(timeWaited>=maxToWait){
			Assert.fail();
			System.out.println("INVITED categorie isn't displayed after "+maxToWait+"s.");
		}
	}
	
	/*
	 * FILES TAB
	 */
	
	/*
	 * SETTINGS TAB
	 */
	@iOSFindBy(xpath="//XCUIElementTypeTable/XCUIElementTypeCell[2]/XCUIElementTypeTextField")
	public MobileElement roomNameTextField;
	
	@iOSFindBy(accessibility="Leave")
	public MobileElement leaveButton;
	@iOSFindBy(accessibility="RoomSettingsVCLeaveAlertActionLeave")
	public MobileElement confirmLeaveFromAlertButton;
	@iOSFindBy(accessibility="RoomSettingsVCLeaveAlertActionCancel")
	public MobileElement confirmCancelFromAlertButton;
	
	public void changeRoomName(String roomName){
		roomNameTextField.clear();
		roomNameTextField.setValue(roomName+"\n");
		//roomNameTextField.sendKeys(Keys.ENTER);
	}
	//ADVANCED PART
	@iOSFindBy(xpath="//XCUIElementTypeTable/XCUIElementTypeCell/XCUIElementTypeSwitch[@name='Enable encryption (warning: cannot be disabled again!)']")
	public MobileElement enableEncryptionSwitch;
	/**
	 * Switch off encryption on a room. The switch need to be focused.
	 * </br> Click on OK Button.
	 */
	public void enableEncryption(){
		enableEncryptionSwitch.click();
		Assert.assertTrue(warningAlert.isDisplayed());
		warningAlertOkButton.click();
	}
		//Warning alert
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeAlert[@name='Warning!']")
	public MobileElement warningAlert;
	@iOSFindBy(accessibility="RoomSettingsVCEnableEncryptionAlertActionCancel")
	public MobileElement warningAlertCancelButton;
	@iOSFindBy(accessibility="RoomSettingsVCEnableEncryptionAlertActionOK")
	public MobileElement warningAlertOkButton;
	

}
