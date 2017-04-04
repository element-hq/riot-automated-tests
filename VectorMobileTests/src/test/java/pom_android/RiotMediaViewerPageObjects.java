package pom_android;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotMediaViewerPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotMediaViewerPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			Assert.assertTrue(waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/view_pager", true, 5), "Media viewer page isn't displayed");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * NAVIGATION BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_container")
	public MobileElement navigationBar;
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement backButton;
	
	/*
	 * BODY
	 */
	
	@AndroidFindBy(id="im.vector.alpha:id/view_pager")
	public MobileElement mediaViewerBody;

	/**
	 * Close the media viewer page by hitting back button.
	 */
	public void close(){
		backButton.click();
	}

	/**
	 * Swipe to the right to display the previous media.
	 */
	public void swipeToPreviousMedia(){

	}

	/**
	 * Swipe to the left to display the next media.
	 */
	public void swipeToNextMedia(){

	}
}
