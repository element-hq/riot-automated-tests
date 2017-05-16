package pom_ios.main_tabs;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class RiotFavouritesTabPageObjects extends RiotTabPageObjects{

	public RiotFavouritesTabPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		super(myDriver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"FavouritesVCView", true, 5),"Favourites tab isn't open.");
		//Instanciate protected properties from mother class with own and uniques values of this tab.
		super.roomsTableView=favouritesTabRoomsTableView;
		super.tabMainView=favouritesTabView;
	}

	@iOSFindBy(accessibility="FavouritesVCView")
	public MobileElement favouritesTabView;

	/*
	 * ROOMS LIST.
	 */
	/**
	 * Table view of rooms items. </br> Contains a list of 'RecentTableViewCell' cells.
	 */
	@iOSFindBy(accessibility="FavouritesVCTableView")
	public MobileElement favouritesTabRoomsTableView;

	/*
	 * FLOATING BUTTONS OR DIALOGS .
	 */

}
