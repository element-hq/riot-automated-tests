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

public class RiotUnifiedSearchPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	private int search_timeout=30;

	public RiotUnifiedSearchPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"SegmentedVCSelectionContainer", true, 5),"Global search page isn't opened");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	/*
	 * NAVIGATION BAR.
	 */
	@iOSFindBy(accessibility="Search")
	public MobileElement searchSearchField;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelButton;

	/*
	 * TABS
	 */
	@iOSFindBy(accessibility="SegmentedVCSelectionContainer")
	public MobileElement tabsBarContainer;

	@iOSFindBy(accessibility="SegmentedVCSectionLabel0")
	public MobileElement roomsTab;
	@iOSFindBy(accessibility="SegmentedVCSectionLabel1")
	public MobileElement messagesTab;
	@iOSFindBy(accessibility="SegmentedVCSectionLabel2")
	public MobileElement peopleTab;
	@iOSFindBy(accessibility="SegmentedVCSectionLabel3")
	public MobileElement filesTab;

	/**
	 * Hit ROOMS tab, then launch a search with @param  searchedText.
	 * @param searchedText
	 * @throws InterruptedException 
	 */
	public void launchASearch(String searchedText, Boolean waitSearchFinished) throws InterruptedException{
		searchSearchField.clear();
		/*if(searchEditText.getText().length()>0)*/searchSearchField.setValue(searchedText+"\n");
		//driver.pressKeyCode(AndroidKeyCode.KEYCODE_ENTER);
		if(waitSearchFinished)	waitUntilSearchFinished();
		driver.hideKeyboard();
	}

	public void waitUntilSearchFinished() throws InterruptedException{
		int timeWaited=0;
		while(timeWaited<search_timeout && progressActivityIndicator.isDisplayed()){
			timeWaited++;
		}
	}

	@iOSFindBy(className="XCUIElementTypeActivityIndicator")
	public MobileElement progressActivityIndicator;

	/*
	 * RESULTS
	 */
	@iOSFindBy(accessibility="DirectoryRecentTableViewCell")
	public MobileElement browseDirectoryItemLayout;
	@iOSFindBy(accessibility="RecentTableViewCell")
	public List<MobileElement> roomsWithoutBrowseDirectoryLayouts;
	@iOSFindBy(accessibility="RecentsVCTableView")
	public MobileElement roomsWithBrowseDirectoryTableView;
	
	/*
	 * ROOMS RESULTS
	 */

	/*
	 * MESSAGES RESULTS
	 */

	/*
	 *	PEOPLE RESULTS 
	 */

}
