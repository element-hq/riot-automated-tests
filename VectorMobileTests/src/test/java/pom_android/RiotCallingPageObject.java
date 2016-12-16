package pom_android;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotCallingPageObject extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotCallingPageObject(AppiumDriver<MobileElement> myDriver){
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver=(AndroidDriver<MobileElement>) myDriver;
		try {
			waitUntilDisplayed(driver,"im.vector.alpha:id/call_layout", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@AndroidFindBy(id="im.vector.alpha:id/call_layout")
	public MobileElement mainCallLayout;
	/*
	 * TOP BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/back_icon")
	public MobileElement menuBackButton;
	@AndroidFindBy(id="im.vector.alpha:id/incoming_call_title")
	public MobileElement incomingCallStatus;
	
	public void waitUntilCallTook() throws InterruptedException{
		waitUntilDisplayed(driver, "//*[@resource-id='im.vector.alpha:id/incoming_call_title' and text='Call connecting...']", false, 5);
	}
	/**
	 * Asserts true or false if the view is displayed.
	 * @param displayed
	 * @return 
	 * @throws InterruptedException
	 */
	public Boolean isDisplayed(Boolean displayed) throws InterruptedException{
		return waitUntilDisplayed(driver,"im.vector.alpha:id/call_layout", displayed, 5);
	}
	/*
	 * BOTTOM BAR : hang out button, microphone, etc
	 */
	@AndroidFindBy(id="im.vector.alpha:id/call_menu_buttons_layout_container")//im.vector.alpha:id/action_bar_header_room_members
	public MobileElement bottomBarLayout;
	@AndroidFindBy(id="im.vector.alpha:id/hang_up_button")
	public MobileElement hangUpButton;
	@AndroidFindBy(id="im.vector.alpha:id/mute_audio")
	public MobileElement muteAudioButton;
	@AndroidFindBy(id="im.vector.alpha:id/mute_local_camera")
	public MobileElement muteLocalCameraButton;
	@AndroidFindBy(id="im.vector.alpha:id/call_switch_camera_view")
	public MobileElement switchCameraButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_chat_link")
	public MobileElement chatLinkButton;
}
