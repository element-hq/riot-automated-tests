package pom;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.AppiumFactory;
import utility.testUtilities;

public class RiotCaptchaPageObject extends testUtilities{
	public RiotCaptchaPageObject(AppiumDriver<MobileElement> driver) throws InterruptedException{
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Thread.sleep(2000);
		//ExplicitWait(driver,this.roomsExpandableListView);
		try {
			waitUntilDisplayed("im.vector.alpha:id/account_creation_message", true, 5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * LOGO LAYOUT
	 */
	@AndroidFindBy(id="im.vector.alpha:id/captcha_logo_layout")
	public MobileElement logoLayout;
	@AndroidFindBy(id="im.vector.alpha:id/login_large_logo")
	public MobileElement logoImageView;

	/**
	 * CAPTCHA 'WELCOME' MESSAGE
	 */
	@AndroidFindBy(id="im.vector.alpha:id/account_creation_message")
	public MobileElement captchaWelcomeMessageTextView;

	/**
	 * CAPTCHA CHECKING VIEW
	 */
	@AndroidFindBy(xpath="//android.view.View[@content-desc=\"I'm not a robot\"]")//"I'm not a robot" text
	public MobileElement notARobotView;
	@AndroidFindBy(xpath="//android.view.View[@content-desc=\"I'm not a robot\"]/../android.view.View/android.widget.CheckBox")//
	public MobileElement notARobotCheckBox;

	/**
	 * CAPTCHA SELECT IMAGE VIEW
	 */
	@AndroidFindBy(xpath="//android.view.View[count(android.view.View)>5]")//main view containing the images to select in order to validate the captcha
	public MobileElement selectImageForCaptchaView;
	@AndroidFindBy(xpath="//android.widget.Button[@content-desc='VERIFY']")
	public MobileElement verifyCaptchaButton;
	@AndroidFindBy(xpath="//android.widget.Button[@content-desc='Help']")
	public MobileElement helpCaptchaButton;
	@AndroidFindBy(xpath="//android.widget.Button[@content-desc='Get an audio challenge']")
	public MobileElement getAudioCaptchaButton;
	@AndroidFindBy(xpath="//android.widget.Button[@content-desc='Get a new challenge']")
	public MobileElement reloadCaptchaCaptchaButton;
	@AndroidFindBy(xpath="//android.view.View[@content-desc='Please try again.']")
	public MobileElement tryAgainView;

	/**
	 * Touch each one of the images.
	 * @throws InterruptedException 
	 */
	public void selectAllImages() throws InterruptedException{
		//ExplicitWait(AppiumFactory.getAppiumDriver(), selectImageForCaptchaView);
		if(waitUntilDisplayed("//android.view.View[count(android.view.View)>5]", true, 5)){
			for (MobileElement image : AppiumFactory.getAppiumDriver().findElementsByXPath("//android.view.View[count(android.view.View)>5]/android.view.View[@content-desc='']")) {
				image.click();
			}
		}

	}
}

