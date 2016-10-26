package mobilestests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom.RiotCaptchaPageObject;
import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.DataproviderClass;
import utility.ScreenshotUtility;
import utility.testUtilities;

@Listeners({ ScreenshotUtility.class })
public class RiotRegisterTests extends testUtilities {
	@BeforeClass
	public void setUp() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.DEVICE_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");

		//Create RemoteWebDriver instance and connect to the Appium server
		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver(new URL(Constant.SERVER_ADRESS), capabilities);
		System.out.println("setUp() done");
	}

	@AfterClass
	public void tearDown(){
		AppiumFactory.getAppiumDriver().quit();
	}
	
	/**
	 * Fill the register form without any email adress. </br>
	 * Verifies that a confirmation messagebox pops up. 
	 */
	@Test(groups={"restartneeded","logout"})
	public void fillRegisterFormWithoutEmail(){
		String userNameTest="riotusername";
		String pwdTest="riotuser";
		String expectedConfirmationText="If you don't specify an email address, you won't be able to reset your password. Are you sure?";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
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
	}
	
	/**
	 * Fill the register form with different passwords. </br>
	 * Verifies that the form is not sent and a notification "Passwords don't mach" pops.</br>
	 * 
	 */
	@Test
	public void fillRegisterFormWithDifferentPwds(){
		String userNameTest="riotusername";
		String pwd1Test="riotuser";
		String pwd2Test="riotuserdifferent";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.registerButton.click();
		registerPage.userNameRegisterEditText.setValue(userNameTest);
		registerPage.pwd1EditRegisterText.setValue(pwd1Test);
		registerPage.pwd2EditRegisterText.setValue(pwd2Test);
		registerPage.registerButton.click();
		//Validate the toast "Passwords don't match" : not possible with appium
		//Validate that we are still on the register form
		Assert.assertTrue(registerPage.inputsRegisteringLayout.isDisplayed(), "The register form is not displayed");
	}
	
	/**
	 * Fill the form with forbidden characters. </br>
	 * Iterates on several datas. </br>
	 * Verifies that the form is not sent.
	 * @throws MalformedURLException 
	 */
	@Test(dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class,groups={"restartneeded","logout"})
	public void fillRegisterFormWithForbiddenCharacter(String mailTest,String userNameTest, String pwd1Test,String pwd2Test) throws MalformedURLException{
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.registerButton.click();
		if(registerPage.emailRegisterEditText.getText().length()>0)registerPage.emailRegisterEditText.clear();
		registerPage.emailRegisterEditText.setValue(mailTest);
		/*if(registerPage.userNameRegisterEditText.getText().length()>0)*/registerPage.userNameRegisterEditText.clear();
		registerPage.userNameRegisterEditText.setValue(userNameTest);
		/*registerPage.pwd1EditRegisterText.clear();*/
		registerPage.pwd1EditRegisterText.setValue(pwd1Test);
		/*registerPage.pwd2EditRegisterText.clear();*/
		registerPage.pwd2EditRegisterText.setValue(pwd2Test);
		registerPage.registerButton.click();
		//Validate the toasts : not possible with appium
		//Validate that we are still on the register form
		Assert.assertTrue(registerPage.isPresentTryAndCatch(registerPage.inputsRegisteringLayout), "The register form is not displayed");
	}
	
	/**
	 * Empty the home server custom URLs then validates that the register button is not enabled.
	 */
	@Test(groups={"restartneeded","logout"})
	public void registerWithEmptyCustomServerUrls(){
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.registerButton.click();
		registerPage.customServerOptionsCheckBox.click();
		registerPage.homeServerEditText.clear();
		registerPage.identityServerEditText.setValue("");;
		//Assert that the register button is not clickable
		Assert.assertFalse(registerPage.registerButton.isEnabled(), "The register button is not disabled after clearing the custom server URLs");
	}
	
	/**
	 * Start a sign-in and enters a wrong captcha.</br>
	 * Validate that the register can't go any further.
	 * @throws InterruptedException 
	 */
	@Test(groups={"restartneeded","logout"})
	public void registerWithFailingCaptchaCheckingTest() throws InterruptedException{
		//creation of a "unique" username by adding a randomize number to the username.
		int userNamesuffix = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String userNameTest=(new StringBuilder("riotuser").append(userNamesuffix)).toString();
		String pwdTest="riotuser";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.fillRegisterForm("", userNameTest,pwdTest, pwdTest);
		RiotCaptchaPageObject captchaPage = new RiotCaptchaPageObject(AppiumFactory.getAppiumDriver());
		captchaPage.notARobotCheckBox.click();
		captchaPage.selectAllImages();
		captchaPage.verifyCaptchaButton.click();
		ExplicitWait(captchaPage.tryAgainView);
		Assert.assertTrue(captchaPage.tryAgainView.isDisplayed(), "The 'Please try again' view is not displayed");
	}
	
	@BeforeMethod(groups="restartneeded")
	public void restartRiot(){
		//Restart the application
		System.out.println("Restart the app");
		AppiumFactory.getAppiumDriver().closeApp();
		AppiumFactory.getAppiumDriver().launchApp();
	}
	
	/**
	 * Log-out the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod(groups="logout")
	public void logoutForSetup() throws InterruptedException{
		System.out.println("Check if logout is needed for the test.");
		if(false==waitUntilDisplayed("im.vector.alpha:id/main_input_layout", true, 5)){
			System.out.println("Can't access to the login page, a user must be logged. Forcing the log-out.");
			RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
			mainPage.logOut();
		}
	}
}
