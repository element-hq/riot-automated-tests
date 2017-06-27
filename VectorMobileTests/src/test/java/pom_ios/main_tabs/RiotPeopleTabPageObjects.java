package pom_ios.main_tabs;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;

public class RiotPeopleTabPageObjects extends RiotTabPageObjects{

	public RiotPeopleTabPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		super(myDriver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"PeopleVCView", true, 5),"People tab isn't open.");
		//Instanciate protected properties from mother class with own and uniques values of this tab.
		super.roomsTableView=peopleTabRoomsTableView;
		super.tabMainView=peopleTabView;
	}

	@iOSFindBy(accessibility="PeopleVCView")
	public MobileElement peopleTabView;

	/*
	 * ROOMS AND PEOPLE LIST.
	 */
	/**
	 * Table view of rooms items. </br> Contains a list of 'RecentTableViewCell' cells.
	 */
	@iOSFindBy(accessibility="PeopleVCTableView")
	public MobileElement peopleTabRoomsTableView;
	
	//TODO
	@iOSFindBy(accessibility="???")
	public MobileElement peopleList;

	/**
	 * TODO
	 * Return a people item from the people list, as a MobileElement.
	 * @param peopleDisplayName
	 * @return
	 */
	public MobileElement getPeopleByName(String peopleDisplayName){
		return null;
	}
}
