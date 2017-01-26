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
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"accessibilityIdentifierCaptureView", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}		
	/*
	 * TOP
	 */
	@iOSFindBy(accessibility="MediaPickerVCCameraSwitchButton")
	public MobileElement cameraSwitchButton;
	@iOSFindBy(accessibility="MediaPickerVCCloseButton")
	public MobileElement cameraCloseButton;
	
	/*
	 * CAMERA
	 */
	@iOSFindBy(accessibility="MediaPickerVCPreviewContainerView")
	public MobileElement previewCameraContainerView;
	@iOSFindBy(accessibility="MediaPickerVCCaptureButton")
	public MobileElement cameraCaptureButton;
	
	/*
	 * RECENT MEDIA. Below the camera, the recent taken pictures and movies.
	 */
	@iOSFindBy(accessibility="MediaPickerVCRecentCapturesCollectionView")
	public MobileElement recentMediasCollection;
	
	/*
	 * ALBUMS.
	 */
	@iOSFindBy(accessibility="MediaPickerVCAlbumsTableView")
	public MobileElement albumsTable;
	
	/*
	 * CONFIRMATION LAYOUT
	 */
	@iOSFindBy(accessibility="OK")
	public MobileElement okButton;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelButton;
}
