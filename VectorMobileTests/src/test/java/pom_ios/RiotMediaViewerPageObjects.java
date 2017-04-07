package pom_ios;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotMediaViewerPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotMediaViewerPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			Assert.assertTrue(waitUntilDisplayed((IOSDriver<MobileElement>) driver,"AttachmentsVC", true, 5), "Media viewer page isn't displayed");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@iOSFindBy(accessibility="AttachmentsVC")
	public MobileElement mediaViewerBody;

	/**
	 * Close the media viewer page by swiping down.
	 */
	public void close(){
		mediaViewerBody.swipe(SwipeElementDirection.DOWN, 100,200,50);
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
