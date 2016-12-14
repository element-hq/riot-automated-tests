package pom;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotIncomingCallPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotIncomingCallPageObjects(AppiumDriver<MobileElement> myDriver){
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver=(AndroidDriver<MobileElement>) myDriver;
		try {
			waitUntilDisplayed(driver,"android:id/content", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * INCOMING CALL VIEW
	 */
	@AndroidFindBy(xpath="//android.widget.TextView[@resource-id='im.vector.alpha:id/room_name']/../android.widget.RelativeLayout")
	public MobileElement incomingCallContainer;
	@AndroidFindBy(id="im.vector.alpha:id/avatar_img")
	public MobileElement incomingCallAvatarImageView;
	@AndroidFindBy(id="im.vector.alpha:id/room_name")
	public MobileElement incomingCallRoomName;
	@AndroidFindBy(id="im.vector.alpha:id/incoming_call_title")
	public MobileElement incomingCallStatus;
	@AndroidFindBy(id="im.vector.alpha:id/button_incoming_call_accept")
	public MobileElement acceptCallButton;
	@AndroidFindBy(id="im.vector.alpha:id/button_incoming_call_ignore")
	public MobileElement ignoreCallButton;
	/**
	 * Check the incoming call view. Put isDisplayed at false to ckeck that is not displayed.</br>
	 * Check that the 2 buttons are displayed.
	 * @param isDisplayed
	 * @param roomName
	 * @param status
	 * @throws InterruptedException 
	 */
	public void checkIncomingCallView(Boolean isDisplayed, String roomName, String status) throws InterruptedException{
		String messagePendingViewPresent;
		if(isDisplayed){
			messagePendingViewPresent="Incoming call view isn't displayed";
		}else{
			messagePendingViewPresent="Incoming call view is displayed";
		}
		Assert.assertTrue(waitUntilDisplayed(driver, "//android.widget.TextView[@resource-id='im.vector.alpha:id/room_name']/../android.widget.RelativeLayout", isDisplayed, 5)==isDisplayed, messagePendingViewPresent);
		if(isDisplayed){
			Assert.assertEquals(incomingCallRoomName.getText(), roomName);
			Assert.assertEquals(incomingCallStatus.getText(), status);
			Assert.assertTrue(acceptCallButton.isEnabled(), "Accept button in incoming call container isn't enabled");
			Assert.assertTrue(ignoreCallButton.isEnabled(), "Ignore button in incoming call container isn't enabled");
			Assert.assertEquals(acceptCallButton.getText(), "ACCEPT");
			Assert.assertEquals(ignoreCallButton.getText(), "IGNORE");
			org.openqa.selenium.Dimension avatarIcon=incomingCallAvatarImageView.getSize();
		    Assert.assertTrue(avatarIcon.height!=0 && avatarIcon.width!=0, "avatar icon from incoming call view has null dimension");
		}
	}
}
