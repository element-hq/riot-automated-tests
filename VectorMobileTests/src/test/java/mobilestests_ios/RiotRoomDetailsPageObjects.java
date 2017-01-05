package mobilestests_ios;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidKeyCode;
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
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeOther[count(XCUIElementTypeStaticText)=3]", true, 5);
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
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar/XCUIElementTypeButton[@name='Done']")
	public MobileElement roomDetailsDoneButton;
	
	
	/*
	 * TABS: MEMBERS, FILES, SETTINGS
	 */
	/**
	 * Contains the 3 tabs : members, files, settings.
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeOther[count(XCUIElementTypeStaticText)=3]")
	public MobileElement tabsTypeOther;
	@iOSFindBy(accessibility="Members")
	public MobileElement membersTab;
	@iOSFindBy(accessibility="Files")
	public MobileElement filesTab;
	@iOSFindBy(accessibility="Settings")
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
		waitUntilDisplayed(driver, "//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeActivityIndicator[@name='In progress']", false, 10);
	}
	/*
	 * MEMBERS
	 */
	@iOSFindBy(accessibility="Search / invite by name, email, id")
	public MobileElement searchInviteSearchField;
	///AppiumAUT/XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther/XCUIElementTypeTable
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeTable")
	public MobileElement membersList;
	
	/*
	 * FILES
	 */
	
	/*
	 * SETTINGS
	 */
	@iOSFindBy(xpath="//XCUIElementTypeTable/XCUIElementTypeCell[2]/XCUIElementTypeTextField")
	public MobileElement roomNameTextField;
	
	public void changeRoomName(String roomName){
		roomNameTextField.clear();
		roomNameTextField.setValue(roomName);
		roomNameTextField.sendKeys(Keys.ENTER);
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
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeButton[@name='Cancel']")
	public MobileElement warningAlertCancelButton;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeButton[@name='OK']")
	public MobileElement warningAlertOkButton;
}
