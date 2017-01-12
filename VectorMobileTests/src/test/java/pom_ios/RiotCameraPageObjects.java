package pom_ios;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotCameraPageObjects extends TestUtilities{
private AppiumDriver<MobileElement> driver;
	
	public RiotCameraPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
//		try {
//			//TODO waitUntilDisplayed((IOSDriver<MobileElement>) driver,"//", true, 5);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}		
	/*
	 * TOP
	 */
	@iOSFindBy(accessibility="camera_switch.png")
	public MobileElement cameraSwitchButton;
	
	/*
	 * CAMERA
	 */
	@iOSFindBy(accessibility="camera capture")
	public MobileElement cameraCaptureButton;
	
	/*
	 * CONFIRMATION LAYOUT
	 */
	@iOSFindBy(accessibility="OK")
	public MobileElement okButton;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelButton;
}
