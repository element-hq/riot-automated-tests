package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

/**
 * "New Chat" page opened after starting a new chat from the rooms List.</br>
 * After the user picked a contact, he can hit the "Start" button, and a new room is created and the contact invited.
 * @author jeang
 *
 */
public class RiotNewChatPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotNewChatPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"StartChatVCTableView", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelButton;
	@iOSFindBy(accessibility="Start")
	public MobileElement startButton;
	
	/*
	 * SEARCH / INVITE CONTACT
	 */
	/**
	 * Use findelementbyclass XCUIElementTypeSearchField to access to the searchbar.
	 */
	@iOSFindBy(accessibility="StartChatVCSearchBar")
	public MobileElement searchBar;
	
	
	/*
	 * CONTACTS TABLE
	 */
	/**
	 * Contains Categories (LOCAL CONTACTS) and Contacts.
	 */
	@iOSFindBy(accessibility="StartChatVCTableView")
	public MobileElement contactsAndCategoriesTable;
	
	@iOSFindBy(accessibility="ContactTableViewCell")
	public List<MobileElement> contactsList;
	
	/**
	 * Search and select a participant, then hit on "Start" to create a new Room.
	 * @param participantAddress
	 */
	public void searchAndSelectMember(String participantAddress){
		searchBar.setValue(participantAddress+"\n");
		startButton.click();
	}
}
