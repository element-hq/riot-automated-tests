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

public class RiotContactPickerPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotContactPickerPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"ContactsTableVCTableView", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(accessibility="Messages")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="Search / invite by User ID, Name or email")
	public MobileElement searchMemberEditText;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelButton;
	@iOSFindBy(accessibility="Clear text")
	public MobileElement clearTextButton;
	
	/*
	 * CONTACTS LIST
	 */
	@iOSFindBy(accessibility="ContactsTableVCTableView")
	public MobileElement contactsAndCategoriesList;
	/**
	 * Return the list of contacts without the categories.
	 */
	@iOSFindBy(accessibility="ContactTableViewCell")
	public List<MobileElement> contactsOnlyList;
	/**
	 * Return a list of categories (section) using AccessibilityIds.</br>
	 * Ex: LOCAL CONTACTS, KNOWN CONTACTS
	 */
	@iOSFindBy(accessibility="??")
	public List<MobileElement> categoryOnlyList;
	/**
	 * Return a list of categories (section) throught the parent, using classname.</br>
	 * Ex: LOCAL CONTACTS, KNOWN CONTACTS
	 */
	public List<MobileElement> getCategoriesList(){
		return contactsAndCategoriesList.findElementsByClassName("XCUIElementTypeOther");
	}
	
	/**
	 * Enters a member adress, mail or else in the textbox, and click on the first item in the list.  </br>
	 *	If nothing is found, doesn't click.
	 */
	public void searchAndSelectMember(String inviteeAddress){
		searchMemberEditText.setValue(inviteeAddress+"\n");
		inviteConfirmationMsgBoxButton.click();
	}
	
	/**
	 * Check the layout of the ContactPicker page when the search bar is empty.
	 */
	public void checkDefaultLayout(){
		Assert.assertEquals(searchMemberEditText.getAttribute("label"), "Search / invite by User ID, Name or email");
		Assert.assertEquals(getCategoriesList().size(), 1, "There is more than 1 categorie.");
		Assert.assertTrue(getCategoriesList().get(0).findElementsByClassName("XCUIElementTypeStaticText").get(0).getText().matches("^LOCAL CONTACTS   [0-9]*$"));//^LOCAL CONTACTS \\([0-9]*\\)$
		Assert.assertTrue(getCategoriesList().get(0).findElementsByClassName("XCUIElementTypeStaticText").size()==2, "There is no checkbox in the LOCAL CONTACTS category item.");
		Assert.assertTrue(getCategoriesList().get(0).findElementsByAccessibilityId("Matrix users only").size()==1, "Name of the checkbox isn't 'Matrix Only'.");
		
	}
	/**
	 * Return the displayname from a member list in the contact picker.
	 * @param member
	 * @return
	 */
	public String getDisplayNameOfMemberFromContactPickerList(MobileElement member){
		try {
			return member.findElementByAccessibilityId("MemberDisplayName").getText();
		} catch (Exception e) {
			return null;
		}
	}
	
	/*
	 * INVITE MSGBOX CONFIRMATION: "Are you sure you want to invite [address] to this chat ?"
	 */
	@iOSFindBy(accessibility="RoomParticipantsVCInviteAlertActionInvite")
	public MobileElement inviteConfirmationMsgBoxButton;
	@iOSFindBy(accessibility="RoomParticipantsVCInviteAlertActionCancel")
	public MobileElement cancelConfirmationMsgBoxButton;
	
	
}
