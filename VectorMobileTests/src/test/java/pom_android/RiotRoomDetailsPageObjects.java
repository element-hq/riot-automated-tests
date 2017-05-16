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

public class RiotRoomDetailsPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotRoomDetailsPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver=(AndroidDriver<MobileElement>) myDriver;
		//ExplicitWait(driver,this.messagesListView);
		//if 'Riot permissions .... Allow Riot to access your contacts' pops up, close it.
		if(waitUntilDisplayed(driver, "//android.widget.TextView[@resource-id='android:id/alertTitle' and @text='Information']", true, 1)){
			driver.findElementById("android:id/button1").click();
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
	 * FILTER BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/search_value_edit_text")
	public MobileElement filterRoomMembersEditText;
	@AndroidFindBy(id="im.vector.alpha:id/clear_search_icon_image_view")
	public MobileElement clearFilteredBarButton;
	/*
	 * MEMBERS LIST
	 */
	
	//@AndroidFindBy(id="im.vector.alpha:id/filtered_list_cell")
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/room_details_members_exp_list_view']/android.widget.RelativeLayout/android.widget.RelativeLayout[@resource-id='im.vector.alpha:id/filtered_list_cell']/..")
	public List<MobileElement> membersList;
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/room_details_members_exp_list_view']/android.widget.RelativeLayout[@enabled='true']")
	public List<MobileElement> categoryList;
	@AndroidFindBy(id="im.vector.alpha:id/search_no_results_text_view")
	public MobileElement noResultTextView;
	
	/**
	 * ADD BUTTON
	 */
	@AndroidFindBy(id="im.vector.alpha:id/add_participants_create_view")
	public MobileElement addParticipantButton;
	
	public void addParticipant(String inviteeAddress) throws InterruptedException {
		addParticipantButton.click();
		RiotContactPickerPageObjects inviteMember = new RiotContactPickerPageObjects(driver);
		inviteMember.searchAndSelectMember(inviteeAddress);
		checkInviteConfirmationMsgBox(inviteeAddress);
		inputDialogOkButton.click();
		waitUntilDisplayed(driver, "//android.widget.ProgressBar", false, 10);
	}
	
	/**
	 * Return a member relativelayout using name of the member.
	 * @param memberName
	 * @return
	 */
	public MobileElement getMemberByName(String memberName){
		try {
			return driver.findElementByXPath("//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/room_details_members_exp_list_view']//android.widget.TextView[@resource-id='im.vector.alpha:id/filtered_list_name' and @text='"+memberName+"']/..");	
		} catch (Exception e) {
			System.out.println("No member found with name '"+memberName+"'");
			return null;
		}
		
	}
	
	/**
	 * Check texts of the invite confirmation msgbox.
	 * @throws InterruptedException 
	 */
	public void checkInviteConfirmationMsgBox(String memberAddress) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/parentPanel", true, 5);
		Assert.assertEquals(inputDialogNameTextView.getText(), "Confirmation");
		Assert.assertTrue(inputDialogTextView.getText().matches("^Are you sure you want to invite (\\S+) to this chat\\?$"));
	}
	
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
			size=2;
		}else{
			size=1;
		}
		while (categoryList.size()!=size && timeWaited <=maxToWait) {
			Thread.sleep(500);
			timeWaited++;
		}
		if(timeWaited>=maxToWait){
			Assert.fail();
			System.out.println("INVITED categorie isn't displayed after "+maxToWait+"s.");
		}
	}
	
	/**
	 * From the room details people tab, enter a text on the filter room members edittext.
	 * @param filter
	 */
	public void filterOnRoomMembersList(String filter){
		filterRoomMembersEditText.setValue(filter);
	}
	
	/**
	 * Return the displayname from a filtered member from the people tab.
	 * @param member
	 * @return
	 */
	public String getDisplayNameOfMemberFromPeopleTab(MobileElement member){
		try {
			return member.findElementById("im.vector.alpha:id/filtered_list_name").getText();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Swipe to the left on a member item, then hit the "eject" button.</br>
	 * Wait until the progress bar is gone.
	 * @param memberItem
	 * @return 
	 * @throws InterruptedException 
	 */
	public void removeMemberWithSwipeOnItem(MobileElement memberItem) throws InterruptedException{
		int x = memberItem.getLocation().getX();
		int y= memberItem.getLocation().getY();
		int witdthX=memberItem.getSize().getWidth();
		int xFirst=x+witdthX-80;
		float xEndF=(float) (0.6*witdthX);
		int xEnd=(int)xEndF;
		//Swipe to the left on the item.
		driver.swipe(xFirst,y, xEnd,y,500);Thread.sleep(500);
		//Hit on eject button
		memberItem.findElementById("im.vector.alpha:id/filtered_list_delete_action").click();
		//hit on remove confirmation button from dialog alert
		waitUntilDisplayed(driver, "android:id/parentPanel", true, 10);
		Assert.assertEquals(inputAndroidDialogNameTextView.getText(), "Confirmation");
		inputDialogOkButton.click();
		waitUntilDisplayed(driver, "//android.widget.ProgressBar", false, 10);
	}
	
		
	/*
	 * 				FILES TAB
	 */
	@AndroidFindBy(xpath="//android.widget.ListView[@resource-id='im.vector.alpha:id/listView_messages']/android.widget.RelativeLayout")
	public List<MobileElement> attachedFilesList;
	
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
