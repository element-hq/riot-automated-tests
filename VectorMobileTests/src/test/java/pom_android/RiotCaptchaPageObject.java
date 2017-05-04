package pom_android;

import java.util.Set;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotCaptchaPageObject extends TestUtilities{
	private AppiumDriver<MobileElement> driver;
	public RiotCaptchaPageObject(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Thread.sleep(2000);
		try {
			waitUntilDisplayed(driver,"im.vector.alpha:id/account_creation_message", true, 5);
		} catch (InterruptedException e) {
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
	@AndroidFindBy(xpath="//android.widget.CheckBox[@content-desc=\"I'm not a robot\"]")//
	public MobileElement notARobotCheckBox;

	/**
	 * CAPTCHA SELECT IMAGE VIEW
	 */
	//@AndroidFindBy(xpath="//android.webkit.WebView")
	@AndroidFindBy(xpath="//android.view.View")
	public WebElement newCaptchaWebView;
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
		//ExplicitWait(appiumFactory.getAppiumDriver(), selectImageForCaptchaView);
		if(waitUntilDisplayed(driver,"//android.view.View[count(android.view.View)>5]", true, 5)){
			for (MobileElement image : driver.findElementsByXPath("//android.view.View[count(android.view.View)>5]/android.view.View[@content-desc='']")) {
				image.click();
			}
		}

	}
	
	public void handleCaptchaWebView() throws InterruptedException{
		Thread.sleep(15000);
		//Set<String> contextNames = appiumFactory.getAndroidDriver1().getContextHandles();
		Set<String> contextNames = ( appiumFactory.getAndroidDriver1()).getContextHandles();
		for (String contextName : contextNames) {

			      System.out.println(contextName);
			  }
		System.out.println(appiumFactory.getAndroidDriver1().getCurrentUrl());
		appiumFactory.getAndroidDriver1().context("WEBVIEW");
		//captchaPage.newCaptchaWebView.
		newCaptchaWebView.submit();
		newCaptchaWebView.click();
	}
}

