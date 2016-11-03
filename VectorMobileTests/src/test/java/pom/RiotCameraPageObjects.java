package pom;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotCameraPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotCameraPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Thread.sleep(2000);
		//ExplicitWait(driver,this.roomsExpandableListView);
		try {
			waitUntilDisplayed(driver,"im.vector.alpha:id/medias_picker_preview_gallery_layout", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * PREVIEW MAIN LAYOUT
	 */
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_preview_gallery_layout")
	public MobileElement mainPreviewLayout;
	
	/**
	 * CAMERA PREVIEW LAYOUT
	 */
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_camera_preview_layout")
	public MobileElement cameraPreviewLayout;
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_exit")
	public MobileElement exitCameraButton;
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_switch_camera")
	public MobileElement switchCameraButton;
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_camera_button")
	public MobileElement triggerCameraButton;//take picture button
	
	/**
	 * TABLE GALLERY PHOTO
	 */
	@AndroidFindBy(id="im.vector.alpha:id/gallery_table_layout")
	public MobileElement galleryPhotoTableLAyout;
	
	/**
	 * MEDIA PICKER PREVIEW
	 */
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_preview")
	public MobileElement mainMediaPickerLayout;
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_preview_image_view")
	public MobileElement previewImage;
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_attach_take_picture_imageview")
	public MobileElement confirmPickingPictureButton;
	@AndroidFindBy(id="im.vector.alpha:id/medias_picker_cancel_take_picture_imageview")
	public MobileElement cancelPickingPictureButton;
	
	/**
	 * SELECT SIZE MENU
	 */
	@AndroidFindBy(id="android:id/parentPanel")
	public MobileElement sendAsMenuLayout;
	@AndroidFindBy(id="android:id/alertTitle") //titre du menu : "Send as"
	public MobileElement sendAsMenuTitleTextView;
	@AndroidFindBy(id="android:id/select_dialog_listview")
	public MobileElement selectSizeListView;
	
	/**
	 * Return item from the "Send as menu"
	 * @param size : Original, Large, Medium, Small
	 * @return
	 */
	public MobileElement getItemFromSendAsMenu(String size){
		return driver.findElementByXPath("//android.widget.ListView/android.widget.CheckedTextView[contains(@text,'"+size+"')]");
	}
	
	
}
