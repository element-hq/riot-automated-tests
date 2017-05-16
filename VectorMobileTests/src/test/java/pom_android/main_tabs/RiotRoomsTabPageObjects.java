package pom_android.main_tabs;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class RiotRoomsTabPageObjects extends RiotTabPageObjects{

	public RiotRoomsTabPageObjects(AndroidDriver<MobileElement> driver) throws InterruptedException {
		super(driver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Assert.assertTrue(waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/fragment_container", true, 5),"PeoplePage tab isn't open.");
	}
	
	/*
	 * ROOMS and ROOM DIRECTORY
	 */
	@AndroidFindBy(id="im.vector.alpha:id/floating_action_button")
	public MobileElement publicRoomsSelectorSpinner;

	@AndroidFindBy(id="im.vector.alpha:id/public_room_view")
	public List<MobileElement> publicRoomsList;
}
