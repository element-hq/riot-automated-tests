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

/**
 * Page opened after hitting a member item. Sections : ADMIN TOOLS, CALL, DEVICES, DIRECTS CHATS.
 * @author jeangb
 */
public class RiotMemberDetailsPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotMemberDetailsPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"RoomMemberDetailsVC", true, 5),"Contact details page isn't opened.");
	}
	
	@iOSFindBy(accessibility="RoomMemberDetailsVC")
	public MobileElement mainViewLayout;

	/*
	 * TOP
	 */
	@iOSFindBy(accessibility="Back")
	public MobileElement menuBackButton;
	@iOSFindBy(accessibility="RoomMemberDetailsVCAvatarMask")
	public MobileElement avatarLayout;
	@iOSFindBy(accessibility="RoomMemberDetailsVCNameLabel")
	public MobileElement memberNameStaticText;
	@iOSFindBy(accessibility="RoomMemberDetailsVCStatusLabel")
	public MobileElement memberPresenceStatusStaticText;
	
	
	
	/*
	 * BODY
	 */
	@iOSFindBy(accessibility="RoomMemberDetailsVCTableView")
	public MobileElement actionsTableView;
	
	@iOSFindBy(accessibility="TableViewCell")
	public List<MobileElement> actionsList;

	/**
	 * Return an action (Start Voice Call, Show Device List ...) by his name. The action must be visible.
	 * @param actionName
	 * @return
	 */
	public MobileElement getActionItemByName(String actionName){
		try {
			return actionsTableView.findElementByXPath("//XCUIElementTypeCell[@name='TableViewCell']/XCUIElementTypeButton[@label='"+actionName+"']/..");	
		} catch (Exception e) {
			return null;
		}
		
	}
}
