package pom_ios;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

/**
 * Modal opened when a user send a message in a e2e room where there is unknown devices.
 * @author jeangb
 */
public class RiotUnknownDevicesModalPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotUnknownDevicesModalPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(IOSDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed(driver,"RoomVCUnknownDevicesAlert", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ALERT TITLE
	 */
	@iOSFindBy(accessibility="Room contains unknown devices")
	public MobileElement alertTitleTextView;
	
	/*
	 * ALERT BODY
	 */
	@iOSFindBy(accessibility="RoomVCUnknownDevicesAlert")
	public MobileElement alertBodyListView;
	
	/**
	 * Return the text view containing the main message.
	 */
	public MobileElement getAlertBodyMessageTextView(){
		return alertBodyListView.findElementsByClassName("XCUIElementTypeStaticText").get(2);
	}
	
	/*
	 * ALERT BOTTOM
	 */
	@Deprecated
	@iOSFindBy(accessibility="RoomVCUnknownDevicesAlertActionCancel")
	public MobileElement cancelButton;
	@iOSFindBy(accessibility="RoomVCUnknownDevicesAlertActionVerify...")
	public MobileElement verifyButton;
	@iOSFindBy(accessibility="RoomVCUnknownDevicesAlertActionSend Anyway")
	public MobileElement sendAnywayButton;
	
	public void checkUnknownDevicesModal(){
		Assert.assertEquals(alertTitleTextView.getText(), "Room contains unknown devices", "Modal title is wrong.");
		Assert.assertEquals(getAlertBodyMessageTextView().getText(), "This room contains unknown devices which have not been verified. This means there is no guarantee that the devices belong to the users they claim to. We recommend you go through the verification process for each device before continuing, but you can resend the message without verifying if you prefer.", "Modal main message is wrong.");
		//For whatever reason, the text of these 2 buttons isn't accessible.
//		Assert.assertEquals(cancelButton.getText(), "Cancel");
//		Assert.assertEquals(verifyButton.getText(), "Verify...");
	}
}
