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
import pom.RiotMainPageObjects;
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
		capabilities.setCapability("deviceName","a71011c8");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.package_name);
		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");

		//Create RemoteWebDriver instance and connect to the Appium server
		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver(new URL(Constant.Server_Adress), capabilities);
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
	@Test
	public void fillRegisterFormWithoutEmail(){
		String userNameTest="riotusername";
		String pwdTest="riotuser";
		String expectedConfirmationText="If you don't specify an email address, you won't be able to reset your password. Are you sure?";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.registerButton.click();
		registerPage.userNameRegisterEditText.sendKeys(userNameTest);
		registerPage.pwd1EditRegisterText.sendKeys(pwdTest);
		registerPage.pwd2EditRegisterText.sendKeys(pwdTest);
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
	 * Fill the register form with differents passwords. </br>
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
		registerPage.userNameRegisterEditText.sendKeys(userNameTest);
		registerPage.pwd1EditRegisterText.sendKeys(pwd1Test);
		registerPage.pwd2EditRegisterText.sendKeys(pwd2Test);
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
	@Test(dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void fillRegisterFormWithForbiddenCharacter(String mailTest,String userNameTest, String pwd1Test,String pwd2Test) throws MalformedURLException{
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.registerButton.click();
		registerPage.emailRegisterEditText.sendKeys(mailTest);
		registerPage.userNameRegisterEditText.sendKeys(userNameTest);
		registerPage.pwd1EditRegisterText.clear();
		registerPage.pwd1EditRegisterText.sendKeys(pwd1Test);
		registerPage.pwd2EditRegisterText.clear();
		registerPage.pwd2EditRegisterText.sendKeys(pwd2Test);
		registerPage.registerButton.click();
		//Validate the toasts : not possible with appium
		//Validate that we are still on the register form
		Assert.assertTrue(registerPage.isPresentTryAndCatch(registerPage.inputsRegisteringLayout), "The register form is not displayed");
	}
	
	/**
	 * Empty the home server custom URLs then validates that the register button is not enabled.
	 */
	@Test
	public void registerWithEmptyCustomServerUrls(){
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.registerButton.click();
		registerPage.customServerOptionsCheckBox.click();
		registerPage.homeServerEditText.clear();
		registerPage.identityServerEditText.sendKeys("");;
		//Assert that the register button is not clickable
		Assert.assertFalse(registerPage.registerButton.isEnabled(), "The register button is not disabled after clearing the custom server URLs");
	}
	
	/**
	 * Start a sign-in and enters a wrong captcha.</br>
	 * Validate that the register can't go any further.
	 * @throws InterruptedException 
	 */
	@Test(groups="captchaOn")
	public void registerWithFailingCaptchaCheckingTest() throws InterruptedException{
		//creation of a "unique" username by adding a randomize number to the username.
		int userNamesuffix = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String userNameTest=(new StringBuilder("riotuser").append(userNamesuffix)).toString();
		String pwdTest="riotuser";
		
		RiotLoginAndRegisterPageObjects registerPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		registerPage.fillRegisterForm(null, userNameTest,pwdTest, pwdTest);
		RiotCaptchaPageObject captchaPage = new RiotCaptchaPageObject(AppiumFactory.getAppiumDriver());
		captchaPage.notARobotCheckBox.click();
		captchaPage.selectAllImages();
		captchaPage.verifyCaptchaButton.click();
		ExplicitWait(captchaPage.tryAgainView);
		Assert.assertTrue(captchaPage.tryAgainView.isDisplayed(), "The 'Please try again' view is not displayed");
	}
	
	/**
	 * Log-out the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod
	public void logoutForSetup() throws InterruptedException{
		if(false==waitUntilDisplayed("im.vector.alpha:id/main_input_layout", true, 5)){
			System.out.println("Can't access to the login page, a user must be logged. Forcing the log-out.");
			RiotMainPageObjects mainPage = new RiotMainPageObjects(AppiumFactory.getAppiumDriver());
			mainPage.logOut();
		}
	}
}
