package pom_ios;

import java.io.FileNotFoundException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.support.PageFactory;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.SwipeElementDirection;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.Constant;
import utility.MatrixUtilities;
import utility.ReadConfigFile;
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
	public MobileElement registerNavBarButton;
	@iOSFindBy(accessibility="AuthenticationVCCancelAuthFallbackButton")
	public MobileElement cancelButton;
	@iOSFindBy(accessibility="Back")
	public MobileElement backButton;
	@iOSFindBy(accessibility="Log in")
	public MobileElement loginNavBarButton;
	
	/*
	 * AUTHENTICATION VIEW : auth forms, forgot password forms...
	 */
	@iOSFindBy(accessibility="AuthenticationVCScrollView")
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
	//email or login
	@iOSFindBy(accessibility="AuthInputsViewUserLoginTextField")
	public MobileElement emailOrUserNameTextField;
	//phone
	@iOSFindBy(accessibility="AuthInputsViewPhoneTextField")
	public MobileElement phoneNumberTextField;
	@iOSFindBy(accessibility="AuthInputsViewPhoneCountryCodeButton")
	public MobileElement chooseCountryCodeButton;
	@iOSFindBy(accessibility="AuthInputsViewPhoneCallingCodeLabel")
	public MobileElement chooseCountryCodeLabel;
	//password
	@iOSFindBy(accessibility="AuthInputsViewPasswordTextField")
	public MobileElement passwordTextField;

	/**
	 * Simple login without doing any verifications.
	 * @param usernameOrEmail
	 * @param password
	 * TODO: works need to be done here because if the password is null, the login won't happen.
	 * @throws InterruptedException 
	 */
	public void logUser(String usernameOrEmail, String phoneNumber,String password, Boolean forceDefaultHs) throws InterruptedException{
		try {
			if("true".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal"))&& false==forceDefaultHs){
				logUserWithCustomHomeServer(usernameOrEmail, phoneNumber,password,MatrixUtilities.getCustomHomeServerURL(false),Constant.DEFAULT_IDENTITY_SERVER_URL);
			}else{
				logUserWithDefaultHomeServer(usernameOrEmail, phoneNumber,password);
			}
		} catch (FileNotFoundException | YamlException | InterruptedException e) {
			e.printStackTrace();
		}
		//wait until login page isn't displayed to see if 'Riot would like to send you notifications' alert is displayed.
		waitUntilDisplayed(driver, "AuthenticationVCView", false, 60);
		if(isPresentTryAndCatch(alertBox)){
			System.out.println("Hit OK button on alert permission about notification");
			dialogOkButton.click();
			waitUntilDisplayed(driver, "XCUIElementTypeActivityIndicator", false, 30);
			if(isPresentTryAndCatch(alertBox)){
				System.out.println("Hit Yes button on alert permission about sending crash informations");
				dialogYesButton.click();
			}
		}
	}
	
	/**
	 * Fill login form, and hit the login button.
	 */
	public void logUserWithDefaultHomeServer(String usernameOrEmail, String phoneNumber,String password){
		if(null!=usernameOrEmail && usernameOrEmail.length()!=0)
			emailOrUserNameTextField.setValue(usernameOrEmail);
		if(null!=phoneNumber && phoneNumber.length()!=0)
			phoneNumberTextField.setValue(phoneNumber);
		if(null!=password && password.length()!=0)
		{
			driver.getKeyboard().sendKeys(Keys.RETURN);
			passwordTextField.setValue(password+"\n");
		}
	}
	/**
	 * Fill login form, custom server options, and hit the login button.
	 * @param usernameOrEmail
	 * @param phoneNumber
	 * @param password
	 * @param hs
	 * @param is
	 */
	public void logUserWithCustomHomeServer(String usernameOrEmail, String phoneNumber,String password, String hs, String is) throws InterruptedException{
		if(null!=usernameOrEmail && usernameOrEmail.length()!=0)
			emailOrUserNameTextField.setValue(usernameOrEmail);
		if(null!=phoneNumber && phoneNumber.length()!=0)
			phoneNumberTextField.setValue(phoneNumber);
		if(null!=password && password.length()!=0)
		{
			driver.getKeyboard().sendKeys(Keys.RETURN);
			passwordTextField.setValue(password);
		}
		setUpHomeServerAndIdentityServer(hs, is);
		loginButton.click();
		if(isPresentTryAndCatch(warningTrustRemoteServerAlert)){
			trustButtonFromTrustRemoteServerAlert.click();
		}
	}
	
	/**
	 * Fill the login form, doesn't hit the login button.
	 */
	public void fillLoginForm(String usernameOrEmail, String phoneNumber,String password){
		
	}
	
	/**
	 * Hit use custom server options checkbox, then fill home server and identity server fields.</br>
	 * Doesn't hit login button.
	 * @throws InterruptedException 
	 */
	public void setUpHomeServerAndIdentityServer(String hsAddress, String isAddress){
		//driver.swipe
		passwordTextField.swipe(SwipeElementDirection.DOWN,100);
		customServerOptionsCheckBox.click();
		if(isPresentTryAndCatch(warningTrustRemoteServerAlert)){
			trustButtonFromTrustRemoteServerAlert.click();
		}
		if(null!=hsAddress&&!homeServerTextField.getText().equals(hsAddress)){
			homeServerTextField.clear();homeServerTextField.setValue(hsAddress);
			driver.getKeyboard().sendKeys(Keys.ENTER);
			if(isPresentTryAndCatch(warningTrustRemoteServerAlert)){
				trustButtonFromTrustRemoteServerAlert.click();
			}
		}
		if(null!=isAddress&&!identityServerTextField.getText().equals(isAddress)){
			identityServerStaticText.clear();identityServerStaticText.setValue(hsAddress);
		}
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
	@iOSFindBy(accessibility="AuthenticationVCCustomServersTickButton")
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
	//Could not verify identity of remote server warning layout
	@iOSFindBy(accessibility="Could not verify identity of remote server.")
	public MobileElement warningTrustRemoteServerAlert;
	@iOSFindBy(accessibility="Trust")
	public MobileElement trustButtonFromTrustRemoteServerAlert;

	/*
	 * DIALOG ALERT
	 */
	@iOSFindBy(xpath="//XCUIElementTypeAlert[@name='Error']")
	public MobileElement errorMsgBox;
	@iOSFindBy(className="XCUIElementTypeAlert")
	public MobileElement alertBox;
	@iOSFindBy(accessibility="Passwords don't match")
	public MobileElement pwdDontMatchStaticText;
	@iOSFindBy(accessibility="OK")
	public MobileElement dialogOkButton;
	@iOSFindBy(accessibility="HomeVCUseGoogleAnalyticsAlertActionYes")
	public MobileElement dialogYesButton;
	

	/*
	 * CAPTCHA
	 */
	@iOSFindBy(accessibility="AuthInputsVRecaptchaWebView")
	public MobileElement captchaWebView;

}
