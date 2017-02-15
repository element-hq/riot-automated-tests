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

public class RiotContactPickerPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotContactPickerPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
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
	/**
	 * List of categories displayed : LOCAL CONTACTS, KNOW CONTACTS. </br>
	 * When a text is entered is entered in the search bar, there is an empty category above the first item.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/people_header_layout")
	public List<MobileElement> categoryList;
	
	@AndroidFindBy(id="im.vector.alpha:id/filtered_list_cell")
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
			waitUntilDisplayed(driver, "im.vector.alpha:id/room_creation_spinner_views", false, 10);
		} catch (Exception e) {
			System.out.println("No members was found with '"+nameOrEmailMember+"'");
		}
	}
	
	/**
	 * Check the layout of the ContactPicker page when the search bar is empty.</br>
	 * Contact permission must be ON in the riot settings otherwise this function will fail.
	 */
	public void checkDefaultLayout(){
		Assert.assertTrue(micButton.isDisplayed(), "Mic button isn't displayed");
		Assert.assertEquals(searchMemberEditText.getText(), "User ID, Name or email");
		Assert.assertEquals(categoryList.size(), 1, "There is more than 1 categorie.");
		Assert.assertTrue(categoryList.get(0).findElementById("im.vector.alpha:id/people_header_text_view").getText().matches("^LOCAL CONTACTS \\([0-9]*\\)$"));
		Assert.assertTrue(categoryList.get(0).findElementsByClassName("android.widget.CheckBox").size()==1, "There is no checkbox in the LOCAL CONTACTS category item.");
		Assert.assertEquals(categoryList.get(0).findElementByXPath("//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/people_header_matrix_contacts_layout']/android.widget.TextView").getText(), "Matrix users only");
	}
	
	/**
	 * Return the displayname from a member list in the contact picker.
	 * @param member
	 * @return
	 */
	public String getDisplayNameOfMemberFromContactPickerList(MobileElement member){
		try {
			return member.findElementById("im.vector.alpha:id/filtered_list_name").getText();
		} catch (Exception e) {
			return null;
		}
	}
}
