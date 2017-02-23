package pom_ios;

import java.util.List;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

/**
 * Page containing a list of devices needed to be verified or blacklist.</br>
 * Opened after hitting the 'Verify...' button on the 'Room contains unknown devices list from the room page.
 * @author jeangb
 *
 */
public class RiotUnknownDevicesListPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotUnknownDevicesListPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(IOSDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		try {
			waitUntilDisplayed(driver,"UsersDevicesVCTitleStaticText", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(accessibility="Unknown devices")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="UsersDevicesVCTitleStaticText")
	public MobileElement titleStaticText;
	@iOSFindBy(accessibility="UsersDevicesVCDoneButton")
	public MobileElement doneButton;
	
	
	/*
	 * BODY
	 */
	@iOSFindBy(accessibility="UsersDevicesVCDTableView")
	public MobileElement usersDeviceTableView;
	
	/**
	 * List of the items containing the unknown devices of a user.</br>
	 * Name of the user in the first and only XCUIElementTypeStaticText child of an item.
	 */
	public List<MobileElement> getUserNameSectionCollapsableList(){
		return usersDeviceTableView.findElementsByAccessibilityId("XCUIElementTypeOther");
	}
	
	/**
	 * List of unknown devices. An item contains the device name, the device id and the two buttons: verify/unverify, blacklist/unblacklist.
	 */
	@iOSFindBy(accessibility="DeviceTableViewCell")
	public List<MobileElement> devicesViewCellList;

	
	/**
	 * Return the device name + id of an item at indexDevice.
	 * @param indexDevice
	 * @return
	 */
	public String getDeviceNamePlusIdByIndex(int indexDevice){
		if(indexDevice<devicesViewCellList.size()){
			return devicesViewCellList.get(indexDevice).findElementByAccessibilityId("DeviceTableViewCellDeviceName").getText();
		}else{
			System.out.println("There is not device item at index "+indexDevice);
			return null;
		}
	}
	/**
	 * Return the device name of an item at indexDevice.
	 * @param indexDevice
	 * @return
	 */
	public String getDeviceNameByIndex(int indexDevice){
			String namePlusId= getDeviceNamePlusIdByIndex(indexDevice);
			String name=namePlusId.substring(0, namePlusId.indexOf("(")-1);
			return name;
	}
	
	/**
	 * Return the device id of an item at indexDevice.
	 * @param indexDevice
	 * @return
	 */
	public String getDeviceIDByIndex(int indexDevice){
		String namePlusId= getDeviceNamePlusIdByIndex(indexDevice);
		String id=namePlusId.substring(namePlusId.indexOf("(")+1,namePlusId.indexOf(")"));
		return id;
	}
	
	
	/**
	 * Hit the verify button of a device by his index position.
	 * @param indexDevice
	 */
	public MobileElement getVerifyDeviceButton(int indexDevice){
		if(indexDevice<devicesViewCellList.size()){
			return devicesViewCellList.get(indexDevice).findElementByAccessibilityId("DeviceTableViewCellVerifyButton");
		}else{
			System.out.println("There is not device item at index "+indexDevice);
			return null;
		}
	}
}
