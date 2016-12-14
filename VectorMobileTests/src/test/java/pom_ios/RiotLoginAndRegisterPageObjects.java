package pom_ios;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotLoginAndRegisterPageObjects extends TestUtilities{
private AppiumDriver<MobileElement> driver;
	
	public RiotLoginAndRegisterPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"//UIAApplication[1]/UIAWindow[1]/UIAScrollView[2]/UIATextField[1]", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIANavigationBar[1]")
	public MobileElement navigationBar;
	@iOSFindBy(xpath="//UIANavigationBar[1]/UIAButton[@name='Register']")
	public MobileElement registerButton;
	
	/*
	 * Riot logo
	 */
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIAScrollView[2]/UIAImage[@name='logo']")
	public MobileElement riotLogoImage;
	
	
	/*
	 * MAIN LOGIN FORM
	 */
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIAScrollView")
	public MobileElement loginScrollView;
			/**
			 * 		login part
			 */
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIAScrollView[2]/UIATextField[1]")
	public MobileElement emailOrUserNameEditText;
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIAScrollView[2]/UIASecureTextField[1]")
	public MobileElement passwordEditText;
	
	
	
	/*
	 * BOTTOM: Forgot password, login
	 */
	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIAScrollView[2]/UIAButton[@label='Log in']")
	public MobileElement loginButton;

}
