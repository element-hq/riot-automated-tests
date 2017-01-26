package pom_ios;

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
	 * MEMBERS
	 */
	@iOSFindBy(accessibility="Search / invite by name, email, id")
	public MobileElement searchInviteSearchField;
	///AppiumAUT/XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeTable
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeTable")
	public MobileElement membersList;
	@iOSFindBy(accessibility="add_participant")
	public MobileElement addParticipantButton;
	/**
	 * Add a participant from room details, Members tab.
	 * @param participant2Adress
	 */
	public void addParticipant(String participant2Adress) {
		addParticipantButton.click();
		RiotContactPickerPageObjects contactPicker = new RiotContactPickerPageObjects(driver);
		contactPicker.searchAndSelectMember(participant2Adress);
		
	}
	
	/*
	 * FILES
	 */
	
	/*
	 * SETTINGS
	 */
	@iOSFindBy(xpath="//XCUIElementTypeTable/XCUIElementTypeCell[2]/XCUIElementTypeTextField")
	public MobileElement roomNameTextField;
	
	@iOSFindBy(accessibility="Leave")
	public MobileElement leaveButton;
	@iOSFindBy(xpath="//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeButton[@name='Leave']")
	public MobileElement confirmLeaveFromAlertButton;
	
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
	@iOSFindBy(accessibility="Cancel")
	public MobileElement warningAlertCancelButton;
	@iOSFindBy(accessibility="OK")
	public MobileElement warningAlertOkButton;
	

}
