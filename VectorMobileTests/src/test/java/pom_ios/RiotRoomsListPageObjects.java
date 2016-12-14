package pom_ios;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotRoomsListPageObjects extends TestUtilities{
private AppiumDriver<MobileElement> driver;
	
	public RiotRoomsListPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		//ExplicitWait(driver,this.roomsAndCategoriesList);
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"//UIAApplication[1]/UIAWindow[1]/UIATableView[contains(@value,'rows')]", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIANavigationBar[1]")
	public MobileElement navigationBar;
	//@iOSFindBy(xpath="//UIAButton[@name='settings icon']")
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIANavigationBar[1]/UIAButton[1]")
	public MobileElement settingsButton;
	
	/*
	 * ROOMS
	 */
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIATableView[contains(@value,'rows')]")
	public MobileElement roomsAndCategoriesList;
	

	/*
	 * SETTINGS
	 */
	//@iOSFindBy(xpath="//UIAButton[@name='Sign Out']")
	@iOSFindBy(xpath="//UIAWindow[1]/UIATableView[1]/UIATableCell[1]/UIAButton[1]")
	//@iOSFindBy()
	public MobileElement signOutButton;
	
	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOut(){
		this.settingsButton.click();
		this.signOutButton.click();
	}
}
