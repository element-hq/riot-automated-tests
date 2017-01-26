package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

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
	public MobileElement inviteSearchField;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelButton;
	
	/*
	 * CONTACTS LIST
	 */
	@iOSFindBy(accessibility="ContactsTableVCTableView")
	public MobileElement contactsAndCategoriesList;
	/**
	 * Return the list of contacts without the categories.
	 */
	@iOSFindBy(accessibility="ContactTableViewCel")
	public List<MobileElement> contactsOnlyList;
	
	/**
	 * Enters a member adress, mail or else in the textbox, and click on the first item in the list. 
	 *	If nothing is found, doesn't click.
	 */
	public void searchAndSelectMember(String inviteeAddress){
		inviteSearchField.setValue(inviteeAddress+"\n");
		inviteConfirmationMsgBoxButton.click();
	}
	
	/*
	 * INVITE MSGBOX CONFIRMATION: "Are you sure you want to invite [address] to this chat ?"
	 */
	@iOSFindBy(accessibility="Invite")
	public MobileElement inviteConfirmationMsgBoxButton;
	
	
}
