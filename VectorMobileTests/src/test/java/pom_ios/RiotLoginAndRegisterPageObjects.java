package pom_ios;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
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
			waitUntilDisplayed(driver,"AuthenticationVCScrollViewContentView", true, 10);
					} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//ExplicitWait(driver, loginScrollView);
	}
	/**
	 * Authentication view : contains auth nav bar, and auth forms.
	 */
	@iOSFindBy(accessibility="AuthenticationVCView")
	public MobileElement authenticationView;
	
	/*
	 * NAVIGATION BAR
	 */
	@iOSFindBy(accessibility="AuthenticationVCNavigationBar")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="Register")
	public MobileElement registerButton;
	@iOSFindBy(accessibility="AuthenticationVCCancelAuthFallbackButton")
	public MobileElement cancelButton;
	
	/*
	 * AUTHENTICATION VIEW : auth forms, forgot password forms...
	 */
	@iOSFindBy(accessibility="AuthenticationVCSrcollView")
	public MobileElement authenticationScrollView;
	@iOSFindBy(accessibility="AuthenticationVCWelcomeImageView")
	public MobileElement riotLogoImage;
	/*
	 *  Forms
	 */
	@iOSFindBy(accessibility="AuthenticationVCInputContainerView")
	public MobileElement authenticationInputContainer;
			/*
			 * 		login part
			 */
	@iOSFindBy(accessibility="AuthInputsViewUserLoginTextField")
	public MobileElement emailOrUserNameTextField;
	@iOSFindBy(accessibility="AuthInputsViewPasswordTextField")
	public MobileElement passwordTextField;
	
	/**
	 * Simple login without doing any verifications.
	 * @param usernameOrEmail
	 * @param password
	 */
	public void fillLoginForm(String usernameOrEmail, String password){
		emailOrUserNameTextField.setValue(usernameOrEmail);
		passwordTextField.setValue(password+"\n");
	}
		/*
		 * 		register part
		 */
	@iOSFindBy(accessibility="AuthInputsViewUserLoginTextField")
	public MobileElement emailTextField;
	@iOSFindBy(accessibility="AuthInputsVRepeatPasswordTextField")
	public MobileElement repeatPasswordTextField;
	
	
	@iOSFindBy(accessibility="In progress")
	public MobileElement loadingWheel;
	
	/*
	 * BOTTOM: Forgot password, login
	 */
	@iOSFindBy(accessibility="AuthenticationVCForgotPasswordButton")
	public MobileElement forgotPwdButton;
	@iOSFindBy(accessibility="AuthenticationVCLoginButton")
	public MobileElement loginButton;
	@iOSFindBy(accessibility="AuthenticationVCOptionTickButton")
	public MobileElement customServerOptionsCheckBox;
	
	/*
	 * FORGOT PASSWORD FORM
	 */
	@iOSFindBy(accessibility="ForgotPasswordInputView")
	public MobileElement inputsForgetPasswordLayout;
	@iOSFindBy(accessibility="ForgotPasswordInputViewMessageLabel")
	public MobileElement forgetPasswordMessageLabel;
	@iOSFindBy(accessibility="ForgotPasswordInputViewEmailTextField")
	public MobileElement mailResetPwdEditText;
	@iOSFindBy(accessibility="ForgotPasswordInputViewPasswordTextField")
	public MobileElement newPwdResetPwdEditText;
	@iOSFindBy(accessibility="ForgotPasswordInputViewTextField")
	public MobileElement confirmNewPwdResetPwdEditText;
	@iOSFindBy(accessibility="AuthenticationVCLoginButton")
	public MobileElement sendResetEmailButton;
	
	/*
	 * CUSTOM SERVER FORM
	 */
	@iOSFindBy(accessibility="AuthenticationVCServerOptionsContainer")
	public MobileElement authenticationOptionContainer;
	@iOSFindBy(accessibility="AuthenticationVCHSLabel")
	public MobileElement homeServerStaticText;
	@iOSFindBy(accessibility="AuthenticationVCHSTextField")
	public MobileElement homeServerTextField;
	//identity server
	@iOSFindBy(accessibility="AuthenticationVCISLabel")
	public MobileElement identityServerStaticText;
	@iOSFindBy(accessibility="AuthenticationVCISTextField")
	public MobileElement identityServerTextField;
	
	/*
	 * DIALOG ALERT
	 */
	@iOSFindBy(xpath="//XCUIElementTypeAlert[@name='Error']")
	public MobileElement errorMsgBox;
	@iOSFindBy(accessibility="Passwords don't match")
	public MobileElement pwdDontMatchStaticText;
	@iOSFindBy(accessibility="OK")
	public MobileElement dialogOkButton;
	
	/*
	 * CAPTCHA
	 */
	@iOSFindBy(accessibility="AuthInputsVRecaptchaWebView")
	public MobileElement captchaWebView;
	
}
