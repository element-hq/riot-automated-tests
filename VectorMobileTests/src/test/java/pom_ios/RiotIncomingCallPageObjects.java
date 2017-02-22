package pom_ios;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotIncomingCallPageObjects extends TestUtilities {
	private IOSDriver<MobileElement> driver;
	public RiotIncomingCallPageObjects(AppiumDriver<MobileElement> myDriver){
		driver= (IOSDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			waitUntilDisplayed(driver,"AppDelegateIncomingCallAlert", true, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@iOSFindBy(xpath="AppDelegateIncomingCallAlert")
	public MobileElement incomingCallAlert;
	@iOSFindBy(accessibility="AppDelegateIncomingCallAlertActionDecline")
	public MobileElement declineCallButton;
	@iOSFindBy(accessibility="AppDelegateIncomingCallAlertActionAccept")
	public MobileElement acceptCallButton;

	
	public void checkIncomingCallView(Boolean isDisplayed, String callerName, String voiceOrVideo) throws InterruptedException{
//		String messagePendingViewPresent;
//		if(isDisplayed){
//			messagePendingViewPresent="Incoming call view isn't displayed";
//		}else{
//			messagePendingViewPresent="Incoming call view is displayed";
//		}
		//Assert.assertTrue(waitUntilDisplayed(driver, "//XCUIElementTypeAlert[contains(@name,'Incoming voice call')]", isDisplayed, 5)==isDisplayed, messagePendingViewPresent);
		if(isDisplayed){
			//Assert.assertTrue(waitUntilDisplayed(driver, "XCUIElementTypeAlert[@name='AppDelegateIncomingCallAlert']//XCUIElementTypeStaticText[@value='Incoming voice call from "+callerName+"']", true, 5), "Incoming call message is wrong.");
			Assert.assertTrue(waitUntilDisplayed(driver, "Incoming "+voiceOrVideo+" call from "+callerName, true, 5), "Incoming call message is wrong.");
		}
	}

}
