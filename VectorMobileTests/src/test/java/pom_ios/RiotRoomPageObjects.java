package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotRoomPageObjects extends TestUtilities{
	private IOSDriver<MobileElement> driver;
	public RiotRoomPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= (IOSDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
//		try {
//			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar[@name=\"Messages\"]/XCUIElementTypeOther[2]//XCUIElementTypeTextField", true, 5);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}
 /*
  * NAVIGATION BAR
  */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="Back")
	public MobileElement menuBackButton;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar[@name=\"Messages\"]/XCUIElementTypeOther[1]//XCUIElementTypeStaticText")
	public MobileElement badgeNumberStaticText;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar[@name='Messages']/XCUIElementTypeOther[2]//XCUIElementTypeTextField")
	public MobileElement roomNameStaticText;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar[@name='Messages']/XCUIElementTypeOther[2]//XCUIElementTypeImage")
	public MobileElement openRoomDetails;
	@iOSFindBy(accessibility="search icon")
	public MobileElement searchInRoomButton;
	
	/*
	 * MESSAGES
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeTable[1]")
	public MobileElement bubblesTable;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeTable[1]/XCUIElementTypeCell")
	public List<MobileElement> bubblesList;

	/**
	 * Return the last bubble as a mobileElement.
	 */
	public MobileElement getLastBubble(){
		return bubblesList.get(bubblesList.size()-1);
	}
	public MobileElement getTextViewFromBubble(MobileElement bubble){
		return bubble.findElementByXPath("//XCUIElementTypeTextView");
	}
	
	/*
	 * BOTTOM
	 */
	@iOSFindBy(accessibility="upload icon")
	public MobileElement uploadButton;
	@iOSFindBy(accessibility="Send")
	public MobileElement sendButton;
	@iOSFindBy(accessibility="voice call icon")
	public MobileElement voiceCallButton;
	@iOSFindBy(accessibility="call hangup icon")
	public MobileElement hangUpCallButton;
	@iOSFindBy(accessibility="e2e_verified")
	public MobileElement e2eIconImage;
	@iOSFindBy(xpath="//XCUIElementTypeApplication//XCUIElementTypeButton[@name='Send']/../XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTextView")
	public MobileElement sendKeyTextView;

	
	
	public void attachPhotoFromCamera(String string) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Type and send a message.
	 * @param encrypted_msg_1
	 */
	public void sendAMessage(String encrypted_msg_1) {
		sendKeyTextView.sendKeys(encrypted_msg_1);
		sendButton.click();
	}
	
}
