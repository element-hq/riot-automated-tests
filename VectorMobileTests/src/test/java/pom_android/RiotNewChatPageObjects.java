package pom;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotNewChatPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotNewChatPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/room_creation_members_list_view", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * TOP BAR : contains back button, and confirm button
	 */
	@AndroidFindBy(id="im.vector.alpha:id/action_bar")
	public MobileElement actionBarView;
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement backButton;
	@AndroidFindBy(id="im.vector.alpha:id/action_create_room")
	public MobileElement confirmRoomCreationButton;
	
	/*
	 * LIST MEMBERS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_creation_members_list_view")
	public List<MobileElement> detailsMemberListView;
	@AndroidFindBy(id="im.vector.alpha:id/room_creation_add_member_texview")
	public MobileElement addMemberItemListView;
	
	/**
	 * Hit on remove button on a member item from the list.
	 * @param displayedMemberName
	 */
	public void removeMemberFromList(String displayedMemberName){
		driver.findElementByXPath("//android.widget.RelativeLayout/android.widget.TextView[@text='"+displayedMemberName+"']/..//android.widget.ImageView[@resource-id='im.vector.alpha:id/filtered_list_remove_button']").click();
	}
	/**
	 * Hit on remove button on a member item from the list.
	 * @param displayedMemberName
	 */
	public void removeMemberFromList(int index){
		detailsMemberListView.get(index).findElementById("im.vector.alpha:id/filtered_list_remove_button").click();
	}

}
