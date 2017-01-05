package pom_ios;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

/**
 * iOS LOGIN AND REGISTER PAGE
 * @author jeang
 *
 */
public class RiotLoginAndRegisterPageObjects extends TestUtilities{
private IOSDriver<MobileElement> driver;
	
	public RiotLoginAndRegisterPageObjects(AppiumDriver<MobileElement> myDriver) {
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver= (IOSDriver<MobileElement>) myDriver;
		try {
			waitUntilDisplayed(driver,"//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView[1]", true, 5);
					} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//ExplicitWait(driver, loginScrollView);
	}
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeNavigationBar")
	public MobileElement navigationBar;
	//@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeNavigationBar/XCUIElementTypeButton[2]")
	@iOSFindBy(accessibility="Register")
	public MobileElement registerButton;
	@iOSFindBy(accessibility="Cancel")
	public MobileElement cancelButton;
	
	/*
	 * Riot logo
	 */
	//@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeImage")
	@iOSFindBy(accessibility="logo")
	public MobileElement riotLogoImage;
	
	
	/*
	 * MAIN LOGIN FORM
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther")
	public MobileElement loginScrollView;
			/**
			 * 		login part
			 */
	//
	@iOSFindBy(xpath="//XCUIElementTypeTextField[1]")
	public MobileElement emailOrUserNameEditText;
	//
	@iOSFindBy(xpath="//XCUIElementTypeSecureTextField[1]")
	public MobileElement passwordEditText;
	
	@iOSFindBy(accessibility="In progress")
	public MobileElement loadingWheel;
	
	/*
	 * BOTTOM: Forgot password, login
	 */
	@iOSFindBy(accessibility="Forgot password?")
	public MobileElement forgotPwdButton;
	@iOSFindBy(accessibility="Log in")
	public MobileElement loginButton;
	@iOSFindBy(accessibility="Use custom server options (advanced)")
	public MobileElement customServerOptionsCheckBox;
	
	/*
	 * FORGOT PASSWORD FORM
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther")
	public MobileElement inputsForgetPasswordLayout;
	@iOSFindBy(accessibility="To reset your password, enter the email address linked to your account:")
	public MobileElement resetPasswordTextView;
	@iOSFindBy(xpath="//XCUIElementTypeStaticText[@name='To reset your password, enter the email address linked to your account:']/../XCUIElementTypeOther[1]/XCUIElementTypeTextField[1]")
	public MobileElement mailResetPwdEditText;
	@iOSFindBy(xpath="//XCUIElementTypeStaticText[@name='To reset your password, enter the email address linked to your account:']/../XCUIElementTypeOther[2]/XCUIElementTypeSecureTextField[1]")
	public MobileElement newPwdResetPwdEditText;
	@iOSFindBy(xpath="//XCUIElementTypeStaticText[@name='To reset your password, enter the email address linked to your account:']/../XCUIElementTypeOther[3]/XCUIElementTypeSecureTextField[1]")
	public MobileElement confirmNewPwdResetPwdEditText;
	@iOSFindBy(accessibility="Send Reset Email")
	public MobileElement sendResetEmailButton;
	
	/*
	 * CUSTOM SERVER FORM
	 */
	@iOSFindBy(accessibility="Home Server:")
	public MobileElement homeServerStaticText;
	@iOSFindBy(xpath="//XCUIElementTypeStaticText[@name='Home Server:']/../XCUIElementTypeTextField[1]")
	public MobileElement homeServerEditText;
	//identity server
	@iOSFindBy(accessibility="Identity Server:")
	public MobileElement identityServerStaticText;
	@iOSFindBy(xpath="//XCUIElementTypeStaticText[@name='Identity Server:']/../XCUIElementTypeTextField[1]")
	public MobileElement identityServerEditText;
	
	/*
	 * DIALOG ALERT
	 */
	@iOSFindBy(xpath="//XCUIElementTypeAlert[@name='Error']")
	public MobileElement errorMsgBox;
	@iOSFindBy(accessibility="Passwords don't match")
	public MobileElement pwdDontMatchStaticText;
	@iOSFindBy(accessibility="OK")
	public MobileElement dialogOkButton;
	
	
}
