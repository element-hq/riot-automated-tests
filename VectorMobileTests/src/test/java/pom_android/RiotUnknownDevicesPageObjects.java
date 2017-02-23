package pom_android;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

/**
 * Modal opened when a user send a message in a e2e room where there is unknown devices.
 * @author jeangb
 */
public class RiotUnknownDevicesPageObjects extends TestUtilities {
	private AndroidDriver<MobileElement> driver;
	public RiotUnknownDevicesPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"android:id/parentPanel", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * ALERT TITLE
	 */
	@AndroidFindBy(id="android:id/alertTitle")
	public MobileElement alertTitleTextView;
	
	/*
	 * ALERT BODY
	 */
	@AndroidFindBy(id="im.vector.alpha:id/unknown_devices_list_view")
	public MobileElement alertBodyListView;
	/**
	 * Text view containing the main message.
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/unknown_devices_list_view']//android.widget.TextView")
	public MobileElement alertBodyMessageTextView;
	
	/**
	 * List of the collapsable items containing the unknown devices of a user.
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/unknown_devices_list_view']/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.TextView[@resource-id='im.vector.alpha:id/heading']/../")
	public List<MobileElement> userNameSectionCollapsableList;
	
	/**
	 * List of unknown devices items. An item contains the device name, the device id and the two buttons: verify/unverify, blacklist/unblacklist.
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/unknown_devices_list_view']//android.widget.TextView[@resource-id='im.vector.alpha:id/device_name']/..")
	public List<MobileElement> unknownDevicesItemsList;
	
	/*
	 * ALERT BOTTOM
	 */
	@AndroidFindBy(id="android:id/buttonPanel")
	public MobileElement buttonPanel;
	
	@AndroidFindBy(id="android:id/button2")
	public MobileElement cancelButton;
	@AndroidFindBy(id="android:id/button1")
	public MobileElement okButton;
	
	public void checkUnknownDevicesModal(){
		Assert.assertEquals(alertTitleTextView.getText(), "Room contains unknown devices", "Modal title is wrong.");
		Assert.assertEquals(alertBodyMessageTextView.getText(), "This room contains unknown devices which have not been verified.\nThis means there is no guarantee that the devices belong to the users they claim to.\nWe recommend you go through the verification process for each device before continuing, but you can resend the message without verifying if you prefer.\n\nUnknown devices:", "Modal main message is wrong.");
		Assert.assertTrue(unknownDevicesItemsList.size()>=1, "There is less than 1 device to verify");
		Assert.assertEquals(cancelButton.getText(), "Cancel");
		Assert.assertEquals(okButton.getText(), "OK");
	}
	
	/**
	 * Return the device name of an item at indexDevice.
	 * @param indexDevice
	 * @return
	 */
	public String getDeviceNameByIndex(int indexDevice){
		if(indexDevice<unknownDevicesItemsList.size()){
			return unknownDevicesItemsList.get(indexDevice).findElementById("im.vector.alpha:id/device_name").getText();
		}else{
			System.out.println("There is not device item at index "+indexDevice);
			return null;
		}
	}
	
	/**
	 * Return the device id of an item at indexDevice.
	 * @param indexDevice
	 * @return
	 */
	public String getDeviceIDByIndex(int indexDevice){
		if(indexDevice<unknownDevicesItemsList.size()){
			return unknownDevicesItemsList.get(indexDevice).findElementById("im.vector.alpha:id/device_id").getText();
		}else{
			System.out.println("There is not device item at index "+indexDevice);
			return null;
		}
	}
	
	
	/**
	 * Hit the verify button of a device by his index position.
	 * @param indexDevice
	 */
	public MobileElement getVerifyDeviceButton(int indexDevice){
		if(indexDevice<unknownDevicesItemsList.size()){
			return unknownDevicesItemsList.get(indexDevice).findElementById("im.vector.alpha:id/button_verify");
		}else{
			System.out.println("There is not device item at index "+indexDevice);
			return null;
		}
	}
}
