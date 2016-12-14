package pom;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotSearchInvitePageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotSearchInvitePageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/decor_content_parent", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * TOP BAR : contains back button, search edittext and mic button
	 */
	@AndroidFindBy(id="im.vector.alpha:id/action_bar")
	public MobileElement actionBarView;
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement backButton;
	@AndroidFindBy(id="im.vector.alpha:id/ic_action_speak_to_search")
	public MobileElement micButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_action_bar_edit_text")
	public MobileElement searchMemberEditText;
	
	/*
	 * LIST OF FIND MEMBERS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_details_members_list")
	public List<MobileElement> detailsMemberListView;
	
	/**
	 * Enters a member adress, mail or else in the textbox, and click on the first item in the list. </br> If nothing is found, doesn't click.
	 * @param nameOrEmailMember
	 */
	public void searchAndSelectMember(String nameOrEmailMember){
		searchMemberEditText.setValue(nameOrEmailMember);
		try {
			//click on the status because clinking on the center of the item doesn't work
			detailsMemberListView.get(0).findElementById("im.vector.alpha:id/filtered_list_status").click();
		} catch (Exception e) {
			System.out.println("No members was found with '"+nameOrEmailMember+"'");
		}
		
	}
}
