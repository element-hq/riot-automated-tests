package pom_android;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

/**
 * ANDROID LOGIN AND REGISTER PAGE
 * @author jeang
 *
 */
public class RiotLoginAndRegisterPageObjects extends TestUtilities{
	private AppiumDriver<MobileElement> driver;

	public RiotLoginAndRegisterPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		//ExplicitWaitToBeVisible(driver,this.inputsLoginLayout);
		try {
			if(driver instanceof AndroidDriver<?>){
				waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/login_inputs_layout", true, 10);
			}else if(driver instanceof IOSDriver<?>){
				waitUntilDisplayed((IOSDriver<MobileElement>) driver,"//UIAApplication[1]/UIAWindow[1]/UIAScrollView[2]/UIATextField[1]", true, 5);
			}		
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*
	 * Riot logo
	 */
	@AndroidFindBy(id="im.vector.alpha:id/login_large_logo")
	public MobileElement riotLogoImageView;
	/**
	 * MAIN LOGIN FORM
	 */
	/*
	 * 		login part
	 */
	@AndroidFindBy(id="im.vector.alpha:id/main_input_layout")
	public MobileElement inputsLoginLayout;

	@AndroidFindBy(id="im.vector.alpha:id/login_user_name")
	public MobileElement emailOrUserNameEditText;
	@AndroidFindBy(id="im.vector.alpha:id/login_phone_number_country")
	public MobileElement countryCodeSelectorEditText;
	@AndroidFindBy(id="im.vector.alpha:id/login_phone_number_value")
	public MobileElement phoneNumberEditText;
	@AndroidFindBy(id="im.vector.alpha:id/login_password")
	public MobileElement passwordEditText;

	/**
	 * Simple login without doing any verifications.
	 * @param usernameOrEmail
	 * @param password
	 */
	public void fillLoginForm(String usernameOrEmail, String phoneNumber,String password){
		if(null!=usernameOrEmail && usernameOrEmail.length()!=0)
			emailOrUserNameEditText.setValue(usernameOrEmail);
		if(null!=phoneNumber && phoneNumber.length()!=0)
			phoneNumberEditText.setValue(phoneNumber);
		if(null!=password && password.length()!=0)
			passwordEditText.setValue(password);
		loginButton.click();
	}

	/*
	 * 		register part
	 */
	@AndroidFindBy(id="im.vector.alpha:id/creation_inputs_layout")
	public MobileElement inputsRegisteringLayout;
	@AndroidFindBy(id="im.vector.alpha:id/creation_email_address")
	public MobileElement emailRegisterEditText;
	@AndroidFindBy(id="im.vector.alpha:id/creation_your_name")
	public MobileElement userNameRegisterEditText;
	@AndroidFindBy(id="im.vector.alpha:id/creation_password1")
	public MobileElement pwd1EditRegisterText;
	@AndroidFindBy(id="im.vector.alpha:id/creation_password2")
	public MobileElement pwd2EditRegisterText;

	/*
	 * 		forget password part
	 */
	@AndroidFindBy(id="im.vector.alpha:id/forget_password_inputs_layout")
	public MobileElement inputsForgetPasswordLayout;
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/forget_password_inputs_layout']/android.widget.TextView")
	public MobileElement resetPasswordTextView;
	@AndroidFindBy(id="im.vector.alpha:id/forget_email_address")
	public MobileElement mailResetPwdEditText;
	@AndroidFindBy(id="im.vector.alpha:id/forget_new_password")
	public MobileElement newPwdResetPwdEditText;
	@AndroidFindBy(id="im.vector.alpha:id/forget_confirm_new_password")
	public MobileElement confirmNewPwdResetPwdEditText;
	@AndroidFindBy(id="im.vector.alpha:id/button_reset_password")
	public MobileElement sendResetEmailButton;

	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/main_input_layout']//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/display_server_url_layout']/android.widget.TextView[@index='1']")
	public MobileElement customServerOptionsTextView;
	@AndroidFindBy(id="im.vector.alpha:id/display_server_url_expand_checkbox")
	public MobileElement customServerOptionsCheckBox;
	@AndroidFindBy(id="im.vector.alpha:id/login_forgot_password")
	public MobileElement forgotPwdButton;
	
	/*
	 * Verifying email page
	 */
	@AndroidFindBy(id="im.vector.alpha:id/flow_progress_message_textview")
	public MobileElement emailSentMessageTextView;
	@AndroidFindBy(id="im.vector.alpha:id/button_forgot_email_validate")
	public MobileElement iVerifiedMyMailButton;
	

	/*
	 * LOGIN MATRIX SERVER CUSTOM OPTIONS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/login_matrix_server_options_layout")
	public MobileElement serverCustomOptsLayout;
	//home server
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/login_matrix_server_options_layout']/android.widget.TextView[1]")
	public MobileElement homeServerTextView;
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/login_matrix_server_options_layout']/android.widget.EditText[1]")
	public MobileElement homeServerEditText;
	//identity server
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/login_matrix_server_options_layout']/android.widget.TextView[2]")
	public MobileElement identityServerTextView;
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='im.vector.alpha:id/login_matrix_server_options_layout']/android.widget.EditText[2]")
	public MobileElement identityServerEditText;

	/*
	 * BOTTOM BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/login_actions_bar")
	public MobileElement loginActionBar;

	@iOSFindBy(xpath="//UIAApplication[1]/UIAWindow[1]/UIAScrollView[2]/UIAButton[@label='Log in']")
	@AndroidFindBy(id="im.vector.alpha:id/button_login")
	public MobileElement loginButton;
	@AndroidFindBy(id="im.vector.alpha:id/button_register")
	public MobileElement registerButton;

	/*
	 * REGISTER CONFIRM MSGBOX
	 */
	@AndroidFindBy(id="android:id/parentPanel")
	public MobileElement msgboxConfirmationLayout;
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='android:id/contentPanel']//android.widget.TextView")
	public MobileElement msgboxConfirmationTextView;
	@AndroidFindBy(id="android:id/button2")
	public MobileElement msgboxConfirmationNoButton;
	@AndroidFindBy(id="android:id/button1")
	public MobileElement msgboxConfirmationYesButton;

	/**
	 * Start a registration to the captcha webview.
	 * @throws InterruptedException 
	 */
	public void fillRegisterForm(String mail, String username, String pwd1, String pwd2) throws InterruptedException{
		registerButton.click();
		emailRegisterEditText.setValue(mail);
		userNameRegisterEditText.setValue(username);
		pwd1EditRegisterText.setValue(pwd1);
		pwd2EditRegisterText.setValue(pwd2);
		registerButton.click();
		waitUntilDisplayed(driver,"android:id/parentPanel", true, 10);
		msgboxConfirmationYesButton.click();
	}
}
