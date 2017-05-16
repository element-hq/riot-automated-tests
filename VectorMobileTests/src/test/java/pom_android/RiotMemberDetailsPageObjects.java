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

/**
 * Page opened after hitting a member item. Sections : ADMIN TOOLS, CALL, DEVICES, DIRECTS CHATS.
 * @author jeangb
 */
public class RiotMemberDetailsPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotMemberDetailsPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver=(AndroidDriver<MobileElement>) myDriver;
		Assert.assertTrue(waitUntilDisplayed(driver,"im.vector.alpha:id/member_details_activity_main_view", true, 5), "Contact details page isn't opened.");
	}
	
	@AndroidFindBy(id="im.vector.alpha:id/member_details_activity_main_view")
	public MobileElement mainViewLayout;
	
	/*
	 * TOP
	 */
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement menuBackButton;
	@AndroidFindBy(id="im.vector.alpha:id/avatar_img")
	public MobileElement avatarImageView;
	@AndroidFindBy(id="im.vector.alpha:id/member_details_name")
	public MobileElement memberNameTextView;
	@AndroidFindBy(id="im.vector.alpha:id/member_details_presence")
	public MobileElement memberPresenceStatusTextView;
	
	/*
	 * BODY
	 */
	@AndroidFindBy(id="im.vector.alpha:id/member_details_actions_list_view")
	public MobileElement actionsListView;
	
	/**
	 * List of sections (ADMIN TOOLS, DEVICES, DIRECT CHATS) as MobileElement.
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/member_details_actions_list_view']/android.widget.RelativeLayout")
	public List<MobileElement> sectionsList;
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/member_details_actions_list_view']/android.widget.LinearLayout")
	public List<MobileElement> actionsList;
	
	/**
	 * Return an action (Start Voice Call, Show Device List ...) by his name. The action must be visible.
	 * @param actionName
	 * @return
	 */
	public MobileElement getActionItemByName(String actionName){
		try {
			return driver.findElementByXPath("//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/member_details_actions_list_view']/android.widget.LinearLayout/android.widget.TextView[@resource-id='im.vector.alpha:id/adapter_member_details_action_text' and @text='"+actionName+"']");	
		} catch (Exception e) {
			System.out.println("No action found with name '"+actionName+"'");
			return null;
		}
	}
	
	/*
	 * DIALOG 
	 */
	@AndroidFindBy(id="android:id/parentPanel")
	public MobileElement dialogMainLinearLayout;
	@AndroidFindBy(id="android:id/message")
	public MobileElement dialogMainMessageTextView;
	@AndroidFindBy(id="android:id/button2")
	public MobileElement dialogNoButton;
	@AndroidFindBy(id="android:id/button1")
	public MobileElement dialogYesButton;
}
