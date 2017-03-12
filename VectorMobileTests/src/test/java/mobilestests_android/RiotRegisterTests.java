package mobilestests_android;

import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_android.RiotCaptchaPageObject;
import pom_android.RiotLoginAndRegisterPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotRegisterTests extends RiotParentTest {

	/**
	 * Fill the register form without any email adress. </br>
	 * Verifies that a confirmation messagebox pops up. 
	 */
	@Test(groups={"logout","1driver_android"})
	public void fillRegisterFormWithoutEmail(){
		String userNameTest="riotusername";
		String pwdTest="riotuser";
		String expectedConfirmationText="If you don't specify an email address, you won't be able to reset your password. Are you sure?";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAndroidDriver1());
		registerPage.registerButton.click();
		registerPage.userNameRegisterEditText.setValue(userNameTest);
		registerPage.pwd1EditRegisterText.setValue(pwdTest);
		registerPage.pwd2EditRegisterText.setValue(pwdTest);
		registerPage.registerButton.click();
		//Validation on the messagebox
		Assert.assertTrue(registerPage.isPresentTryAndCatch(registerPage.msgboxConfirmationLayout), "The confirmation msgbox is not displayed when an email is not specified in the register form");
		Assert.assertEquals(registerPage.msgboxConfirmationTextView.getText(), expectedConfirmationText);
		//Cancels the registeration
		registerPage.msgboxConfirmationNoButton.click();
		//Validate that we are still on the register form
		Assert.assertTrue(registerPage.inputsRegisteringLayout.isDisplayed(), "The register form is not displayed");
		//Validate that the fields are still filled
		Assert.assertFalse(registerPage.userNameRegisterEditText.getText().isEmpty(), "The username field from the register form is empty");
		//(Impossible to test the password text lenght)
		restartRiot();
	}
	
	/**
	 * Fill the register form with different passwords. </br>
	 * Verifies that the form is not sent and a notification "Passwords don't mach" pops.</br>
	 * 
	 */
	@Test(groups={"1driver_android"})
	public void fillRegisterFormWithDifferentPwds(){
		String userNameTest="riotusername";
		String pwd1Test="riotuser";
		String pwd2Test="riotuserdifferent";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAndroidDriver1());
		registerPage.registerButton.click();
		registerPage.userNameRegisterEditText.setValue(userNameTest);
		registerPage.pwd1EditRegisterText.setValue(pwd1Test);
		registerPage.pwd2EditRegisterText.setValue(pwd2Test);
		registerPage.registerButton.click();
		//Validate the toast "Passwords don't match" : not possible with appium
		//Validate that we are still on the register form
		Assert.assertTrue(registerPage.inputsRegisteringLayout.isDisplayed(), "The register form is not displayed");
		restartRiot();
	}
	
	/**
	 * Fill the form with forbidden characters. </br>
	 * Iterates on several datas. </br>
	 * Verifies that the form is not sent.
	 * @throws MalformedURLException 
	 */
	@Test(dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class,groups={"1driver_android"})//groups={"restartneeded","logout","1driver_android"})
	public void fillRegisterFormWithForbiddenCharacter(String mailTest,String userNameTest, String pwd1Test,String pwd2Test) throws MalformedURLException{
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAndroidDriver1());
		registerPage.registerButton.click();
		//if(registerPage.emailRegisterEditText.getText().length()>0)registerPage.emailRegisterEditText.clear();
		registerPage.emailRegisterEditText.setValue(mailTest);
		/*if(registerPage.userNameRegisterEditText.getText().length()>0)*///registerPage.userNameRegisterEditText.clear();
		registerPage.userNameRegisterEditText.setValue(userNameTest);
		/*registerPage.pwd1EditRegisterText.clear();*/
		registerPage.pwd1EditRegisterText.setValue(pwd1Test);
		/*registerPage.pwd2EditRegisterText.clear();*/
		registerPage.pwd2EditRegisterText.setValue(pwd2Test);
		registerPage.registerButton.click();
		//Validate the toasts : not possible with appium
		//Validate that we are still on the register form
		Assert.assertTrue(registerPage.isPresentTryAndCatch(registerPage.inputsRegisteringLayout), "The register form is not displayed");
		restartRiot();
	}
	
	/**
	 * Empty the home server custom URLs then validates that the register button is not enabled.
	 */
	@Test(groups={"logout","1driver_android"})
	public void registerWithEmptyCustomServerUrls(){
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAndroidDriver1());
		registerPage.registerButton.click();
		registerPage.customServerOptionsCheckBox.click();
		registerPage.homeServerEditText.clear();
		AppiumFactory.getAndroidDriver1().hideKeyboard();
		registerPage.identityServerEditText.clear();
		//Assert that the register button is not clickable
		Assert.assertFalse(registerPage.registerButton.isEnabled(), "The register button is not disabled after clearing the custom server URLs");
	}
	
	/**
	 * UIAUTOMATOR VIEW don't see the mosaic anymore.
	 * Start a sign-in and enters a wrong captcha.</br>
	 * Validate that the register can't go any further.
	 * @throws InterruptedException 
	 */
	@Test(groups={"logout","1driver_android"}, enabled=false)
	public void registerWithFailingCaptchaCheckingTest() throws InterruptedException{
		//creation of a "unique" username by adding a randomize number to the username.
		int userNamesuffix = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String userNameTest=(new StringBuilder("riotuser").append(userNamesuffix)).toString();
		String pwdTest="riotuser";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAndroidDriver1());
		registerPage.fillRegisterForm("", userNameTest,pwdTest, pwdTest);
		RiotCaptchaPageObject captchaPage = new RiotCaptchaPageObject(AppiumFactory.getAndroidDriver1());
		captchaPage.notARobotCheckBox.click();
		captchaPage.selectAllImages();
		captchaPage.verifyCaptchaButton.click();
		ExplicitWait(AppiumFactory.getAndroidDriver1(),captchaPage.tryAgainView);
		Assert.assertTrue(captchaPage.tryAgainView.isDisplayed(), "The 'Please try again' view is not displayed");
	}
	
	@Test(groups={"1driver_android"}, enabled=false)
	public void registerTestWebView() throws InterruptedException{
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAndroidDriver1());
		registerPage.fillRegisterForm("", "riotuser16","riotuser", "riotuser");
		RiotCaptchaPageObject captchaPage = new RiotCaptchaPageObject(AppiumFactory.getAndroidDriver1());
		captchaPage.notARobotCheckBox.click();
		captchaPage.handleCaptchaWebView();
	}
	
	private void restartRiot(){
		//Restart the application
		System.out.println("Restart the app");
		AppiumFactory.getAndroidDriver1().closeApp();
		AppiumFactory.getAndroidDriver1().launchApp();
	}
	
	/**
	 * Log-out the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod(groups="logout")
	private void logoutForSetup() throws InterruptedException{
		System.out.println("Check if logout is needed for the test.");
		if(false==waitUntilDisplayed(AppiumFactory.getAndroidDriver1(),"im.vector.alpha:id/main_input_layout", true, 3)){
			System.out.println("Can't access to the login page, a user must be logged. Forcing the log-out.");
			RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
			mainPage.logOut();
		}
	}
}
