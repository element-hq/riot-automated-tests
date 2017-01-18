package pom_android;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotRoomDetailsPageObject extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotRoomDetailsPageObject(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver=(AndroidDriver<MobileElement>) myDriver;
		//ExplicitWait(driver,this.messagesListView);
		//if 'Riot permissions .... Allow Riot to access your contacts' pops up, close it.
		if(waitUntilDisplayed(driver, "//android.widget.TextView[@resource-id='android:id/alertTitle' and @text='Riot permissions']", true, 2)){
			driver.findElementById("android:id/button2").click();
		}
		
		try {
			waitUntilDisplayed(driver,"//android.widget.HorizontalScrollView", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * TOP : contains action bar and the 3 tabs (PEOPLE, FILES, SETTINGS)
	 */
	@AndroidFindBy(id="im.vector.alpha:id/action_bar")
	public MobileElement actionBarView;
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement menuBackButton;
	@AndroidFindBy(xpath="//android.view.View[@resource-id='im.vector.alpha:id/action_bar']/android.widget.TextView[@index='1']")
	public MobileElement roomDetailsTextView;
	
	@AndroidFindBy(xpath="//android.widget.TextView[@text='People']/../*")
	public MobileElement peopleTab;
	@AndroidFindBy(xpath="//android.widget.TextView[@text='Files']/../*")
	public MobileElement filesTab;
	@AndroidFindBy(xpath="//android.widget.TextView[@text='Settings']/../*")
	public MobileElement settingsTab;
	/*
	 * 				PEOPLE TAB
	 */
	/*
	 * SEARCH BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/search_value_edit_text")
	public MobileElement searchEditText;
	
	/*
	 * MEMBERS LIST
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_details_members_exp_list_view")
	public List<MobileElement> membersList;
	
	/**
	 * ADD BUTTON
	 */
	@AndroidFindBy(id="im.vector.alpha:id/add_participants_create_view")
	public MobileElement addParticipantButton;
	
	public void addParticipant(String inviteeAddress) throws InterruptedException {
		addParticipantButton.click();
		RiotSearchInvitePageObjects inviteMember = new RiotSearchInvitePageObjects(driver);
		inviteMember.searchAndSelectMember(inviteeAddress);
		checkInviteConfirmationMsgBox(inviteeAddress);
		inputDialogOkButton.click();
	}
	/**
	 * Check texts of the invite confirmation msgbox.
	 * @throws InterruptedException 
	 */
	public void checkInviteConfirmationMsgBox(String memberAddress) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/parentPanel", true, 5);
		Assert.assertEquals(inputDialogNameTextView.getText(), "Invite?");
		Assert.assertTrue(inputDialogTextView.getText().matches("^Are you sure you want to invite (\\S+) to this chat\\?$"));
	}
	
	/*
	 * INVITE MSGBOX CONFIRMATION.
	 * Contains "Are you sure you want to invite ... to this chat ?".
	 */
	
	/*
	 * 				FILES TAB
	 */
	
	/*
	 * 				SETTINGS TAB
	 */
	@AndroidFindBy(id="android:id/list")
	public MobileElement listItemSettings;
	
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView")
	public MobileElement roomNameListItem;
	
	public void changeRoomName(String roomName){
		listItemSettings.findElementByXPath("//*[@text='Room Name']").click();
		ExplicitWait(driver, inputDialogEditText);
		inputDialogEditText.sendKeys(roomName);
		inputDialogOkButton.click();
		ExplicitWait(driver, listItemSettings.findElementByXPath("//*[@text='Room Name']"));
	}
	/*
	 * ADVANCED SECTION
	 */
	@AndroidFindBy(xpath="//android.widget.TextView[contains(@text,'Enable encryption')]")
	public MobileElement enableEncryptionSwitch;
	/**
	 * Switch off encryption on a room. The switch need to be focused.
	 * </br> Assert that the dialog have a warning title.
	 * </br> Click on Yes Button.
	 */
	public void enableEncryption(){
		enableEncryptionSwitch.click();
		Assert.assertEquals(inputAndroidDialogNameTextView.getText(), "Warning!");
		inputDialogOkButton.click();
	}
	
	/*
	 * 		MISC
	 */
	/*
	 * INPUT DIALOG BOX
	 */
	@AndroidFindBy(id="im.vector.alpha:id/parentPanel")
	public MobileElement inputDialogMainLayout;
	@AndroidFindBy(id="im.vector.alpha:id/alertTitle")
	public MobileElement inputDialogNameTextView;
	@AndroidFindBy(id="android:id/alertTitle")
	public MobileElement inputAndroidDialogNameTextView;
	@AndroidFindBy(id="android:id/message")
	public MobileElement inputDialogTextView;
	@AndroidFindBy(id="android:id/edit")
	public MobileElement inputDialogEditText;
	@AndroidFindBy(id="android:id/button2")
	public MobileElement inputDialogCancelButton;
	@AndroidFindBy(id="android:id/button1")
	public MobileElement inputDialogOkButton;
}
