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
			//waitUntilDisplayed(driver,"//XCUIElementTypeAlert[contains(@name,'Incoming voice call')]", true, 10);
			waitUntilDisplayed(driver,"//XCUIElementTypeAlert", true, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@iOSFindBy(xpath="//XCUIElementTypeAlert[contains(@name,'Incoming voice call')]")
	public MobileElement incomingCallAlert;
	@iOSFindBy(accessibility="Decline")
	public MobileElement acceptCallButton;
	@iOSFindBy(accessibility="Accept")
	public MobileElement declineCallButton;

	
	public void checkIncomingCallView(Boolean isDisplayed, String callerName) throws InterruptedException{
//		String messagePendingViewPresent;
//		if(isDisplayed){
//			messagePendingViewPresent="Incoming call view isn't displayed";
//		}else{
//			messagePendingViewPresent="Incoming call view is displayed";
//		}
		//Assert.assertTrue(waitUntilDisplayed(driver, "//XCUIElementTypeAlert[contains(@name,'Incoming voice call')]", isDisplayed, 5)==isDisplayed, messagePendingViewPresent);
		if(isDisplayed){
			Assert.assertTrue(waitUntilDisplayed(driver, "//XCUIElementTypeStaticText[@value='Incoming voice call from "+callerName+"']", true, 5), "Inconming call message is wrong.");
		}
	}

}
