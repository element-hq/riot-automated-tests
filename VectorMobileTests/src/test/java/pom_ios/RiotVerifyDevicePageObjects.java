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
 * 'Verify Device' modal opened to verify keys for one device. Opened after clicking the verify button from a list of devices to verify.
 * @author jeang
 */
public class RiotVerifyDevicePageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotVerifyDevicePageObjects(AppiumDriver<MobileElement> myDriver) {
		driver=(IOSDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed(driver,"//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ALERT BODY
	 */
	@iOSFindBy(className="XCUIElementTypeTextView")
	public MobileElement alertMainMessageTextView;
	
	/*
	 * ALERT BOTTOM
	 */
	@iOSFindBy(accessibility="Verify")
	public MobileElement alertVerifyButton;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement alertCancelButton;
	
	/**
	 * Verify the alert title, messages and the device name, id and key.</br>
	 * Put these parameters as null if you don't want to test it.
	 * @param deviceName
	 * @param deviceId
	 * @param deviceKey
	 */
	public void checkVerifyDeviceAlert(String deviceName, String deviceId, String deviceKey){
		String mainMessage=alertMainMessageTextView.getText();
		Assert.assertTrue(mainMessage.contains("Verify device"));
		Assert.assertTrue(mainMessage.contains("To verify that this device can be trusted, please contact its owner using some other means (e.g. in person or a phone call) and ask them whether the key they see in their User Settings for this device matches the key below:"));
		Assert.assertTrue(mainMessage.contains("If it matches, press the verify button below. If it doesnt, then someone else is intercepting this device and you probably want to press the blacklist button instead.\n\nIn future this verification process will be more sophisticated."));
		
		if(deviceName!=null){
			Assert.assertTrue(mainMessage.contains(deviceName), "Device name in the 'Verify device' alert isn't the one expected.");
		}else{
			String extractedDeviceName=mainMessage.substring(mainMessage.indexOf("Device name: ")+13, mainMessage.indexOf("Device ID: "));
			Assert.assertTrue(extractedDeviceName.length()>2);
		}
		if(deviceId!=null){
			Assert.assertTrue(mainMessage.contains(deviceId), "Device id in the 'Verify device' alert isn't the one expected.");
		}else{
			String extractedDeviceId=mainMessage.substring(mainMessage.indexOf("Device ID: ")+11, mainMessage.indexOf("Device key: "));
			Assert.assertTrue(extractedDeviceId.length()>2);
		}
		if(deviceKey!=null){
			Assert.assertTrue(mainMessage.contains(deviceKey), "Device key in the 'Verify device' alert isn't the one expected.");
		}else{
			String extractedDeviceKey=mainMessage.substring(mainMessage.indexOf("Device key: ")+12, mainMessage.indexOf("If it matches"));
			Assert.assertTrue(extractedDeviceKey.length()>2);
		}
	}
}
