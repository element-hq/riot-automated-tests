package pom_ios;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotCallingPageObjects extends TestUtilities{
	private IOSDriver<MobileElement> driver;
	public RiotCallingPageObjects(AppiumDriver<MobileElement> myDriver){
		driver= (IOSDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			waitUntilDisplayed(driver,"CallVCView", true, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@iOSFindBy(accessibility="CallVCView")
	public MobileElement mainCallLayout;
	
	/*
	 * TOP
	 */
	@iOSFindBy(accessibility="CallVCBackToAppButton")
	public MobileElement menuBackButton;
	@iOSFindBy(accessibility="CallVCCallerNameLabel")
	public MobileElement callerName;
	@iOSFindBy(accessibility="CallVCCallStatusLabel")
	public MobileElement incomingCallStatus;
	@iOSFindBy(accessibility="CallVCCameraSwitchButton")
	public MobileElement switchCameraButton;

	/*
	 * MIDDLE
	 */
	@iOSFindBy(accessibility="CallVCCallerImageView")
	public MobileElement callerImage;
	
	/*
	 * BOTTOM BAR
	 */
	/**
	 * Contains the controls at the bottom of the view.
	 */
	@iOSFindBy(accessibility="CallVCCallControlContainerView")
	public MobileElement callControllContainer;
	@iOSFindBy(accessibility="CallVCAudioMuteButton")
	public MobileElement muteMicButton;
	@iOSFindBy(accessibility="CallVCSpeakerButton")
	public MobileElement muteAudioButton;
	@iOSFindBy(accessibility="CallVCEndCallButton")
	public MobileElement hangUpButton;
	@iOSFindBy(accessibility="CallVCVideoMuteButton")
	public MobileElement muteLocalCameraButton;
	@iOSFindBy(accessibility="CallVCChatButton")
	public MobileElement chatLinkButton;
	
	/**
	 * Asserts true or false if the view is displayed.
	 * @param displayed
	 * @return 
	 * @throws InterruptedException
	 */
	public Boolean isDisplayed(Boolean displayed) throws InterruptedException{
		return waitUntilDisplayed(driver,"CallVCOverlayContainerView", displayed, 5);
	}
}
