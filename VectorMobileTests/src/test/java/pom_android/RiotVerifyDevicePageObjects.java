package pom_android;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

/**
 * 'Verify Device' modal opened to verify keys for one device. Opened after clicking the verify button from a list of devices to verify.
 * @author jeang
 *
 */
public class RiotVerifyDevicePageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotVerifyDevicePageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/parentPanel", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ALERT TITLE
	 */
	@AndroidFindBy(id="im.vector.alpha:id/alertTitle")
	public MobileElement alertTitleTextView;

	/*
	 * ALERT BODY
	 */
	@AndroidFindBy(xpath="//android.widget.ScrollView/android.widget.LinearLayout/android.widget.TextView[1]")
	public MobileElement alertBodyMessageTextView1;

	@AndroidFindBy(id="im.vector.alpha:id/encrypted_device_info_device_name")
	public MobileElement alertDeviceNameTextView;
	@AndroidFindBy(id="im.vector.alpha:id/encrypted_device_info_device_id")
	public MobileElement alertDeviceIdTextView;
	@AndroidFindBy(id="im.vector.alpha:id/encrypted_device_info_device_key")
	public MobileElement alertDeviceKeyTextView;

	@AndroidFindBy(xpath="//android.widget.ScrollView/android.widget.LinearLayout/android.widget.TextView[8]")
	public MobileElement alertBodyMessageTextView2;

	/*
	 * ALERT BOTTOM
	 */
	@AndroidFindBy(id="android:id/button1")
	public MobileElement alertVerifyButton;
	@AndroidFindBy(id="android:id/button2")
	public MobileElement alertCancelButton;

	/**
	 * Verify the alert title, messages and the device name, id and key.</br>
	 * Put these parameters as null if you don't want to test it.
	 * @param deviceName
	 * @param deviceId
	 * @param deviceKey
	 */
	public void checkVerifyDeviceAlert(String deviceName, String deviceId, String deviceKey){
		Assert.assertEquals(alertTitleTextView.getText(), "Verify device");
		Assert.assertEquals(alertBodyMessageTextView1.getText(), "To verify that this device can be trusted, please contact its owner using some other means (e.g. in person or a phone call) and ask them whether the key they see in their User Settings for this device matches the key below:");
		Assert.assertEquals(alertBodyMessageTextView2.getText(), "If it matches, press the verify button below. If it doesnt, then someone else is intercepting this device and you probably want to press the block button instead.\nIn future this verification process will be more sophisticated.");
		Assert.assertEquals(alertVerifyButton.getText(), "I verify that the keys match");
		Assert.assertEquals(alertCancelButton.getText(), "Cancel");
		
		if(deviceName!=null){
			Assert.assertEquals(alertDeviceNameTextView.getText(), deviceName, "Device name in the 'Verify device' alert isn't the one expected.");
		}else{
			Assert.assertNotNull(alertDeviceNameTextView.getText());
		}
		if(deviceId!=null){
			Assert.assertEquals(alertDeviceIdTextView.getText(), deviceId, "Device id in the 'Verify device' alert isn't the one expected.");
		}else{
			Assert.assertNotNull(alertDeviceIdTextView.getText());
		}
		if(deviceKey!=null){
			Assert.assertEquals(alertDeviceKeyTextView.getText(), deviceKey, "Device key in the 'Verify device' alert isn't the one expected.");
		}else{
			Assert.assertNotNull(alertDeviceKeyTextView.getText());
		}
	}
}
