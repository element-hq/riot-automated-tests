package pom;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotRoomDetailsPageObject extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotRoomDetailsPageObject(AppiumDriver<MobileElement> myDriver){
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver=(AndroidDriver<MobileElement>) myDriver;
		//ExplicitWait(driver,this.messagesListView);
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
	
	@AndroidFindBy(xpath="//android.widget.TextView[@text='People']/../")
	public MobileElement peopleTab;
	@AndroidFindBy(xpath="//android.widget.TextView[@text='Files']/../")
	public MobileElement filesTab;
	@AndroidFindBy(xpath="//android.widget.TextView[@text='Settings']/../")
	public MobileElement settingsTab;
	
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
	
	/*
	 * ADD BUTTON
	 */
	@AndroidFindBy(id="im.vector.alpha:id/add_participants_create_view")
	public MobileElement addParticipantButton;
	
	public void addParticipant(String inviteeAddress) throws InterruptedException {
		addParticipantButton.click();
		RiotSearchInvitePageObjects inviteMember = new RiotSearchInvitePageObjects(driver);
		inviteMember.searchAndSelectMember(inviteeAddress);
		
	}
	
}
