package mobilestests_ios;

import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.main_tabs.RiotHomePageTabObjects;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on login on IOS platform.
 * @author matrix
 *
 */
@Listeners({ ScreenshotUtility.class })
public class RiotLoginTests extends RiotParentTest{

	/**
	 * Log and logout and iterate on several datas from excel file.
	 */
	@Test(groups={"1driver_ios","loginpage"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void loginAndLogoutiOsTest(String sUserName,String sPassword)  throws Exception {
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getiOsDriver1());
		loginPage.logUser(sUserName,null, sPassword,false);

		//Wait for the main page (rooms list) to be opened, and log out.
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		homePage.logOutFromTabs();
		Assert.assertTrue(waitUntilDisplayed(appiumFactory.getiOsDriver1(), "AuthenticationVCView", true, 15), "The login page isn't displayed after the log-out.");
		//Assert.assertTrue(loginPage.authenticationView.isEnabled(), "The login page isn't displayed after the log-out.");
	}

	@Test(groups={"1driver_ios","loginpage"})
	public void simpleLogin() throws InterruptedException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getiOsDriver1());
		loginPage.logUser("riotuser2", "","riotuser", true);
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		homePage.logOutFromTabs();
	}

	/**
	 * TODO
	 * Log with a matrix id.
	 */
	@Test(enabled=false)
	public void loginWithMatrixId(){

	}

	/**
	 * TODO
	 * Log with a phone number
	 */
	@Test(enabled=false)
	public void loginWithPhoneNumber(){

	}

	/**
	 * Check the reset password form.
	 * Doesn't verifies the reset password function.
	 */
	@Test(groups={"1driver_ios","loginpage"})
	public void forgotPasswordFormTest(){
		String expectedResetPwdMessage="To reset your password, enter the email address linked to your account:";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getiOsDriver1());
		loginPage.forgotPwdButton.click();
		Assert.assertEquals(loginPage.forgetPasswordMessageLabel.getText(), expectedResetPwdMessage);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.mailResetPwdEditText), "The email address  edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.newPwdResetPwdEditText), "The new password edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.confirmNewPwdResetPwdEditText), "The confirm your new pwd edittext is not present");
		Assert.assertTrue(loginPage.sendResetEmailButton.isEnabled(), "The send reset email button is not enabled");
		//verifies that the login and register button are hidden
		Assert.assertEquals(loginPage.loginButton.getText(), "Send Reset Email");
		Assert.assertFalse(isPresentTryAndCatch(loginPage.registerNavBarButton), "The register button is displayed");
		//riot logo is still displayed
		Assert.assertTrue(isPresentTryAndCatch(loginPage.riotLogoImage), "The riot logo isn't displayed");
		//custom server option checkbox is still displayed
		Assert.assertTrue(isPresentTryAndCatch(loginPage.customServerOptionsCheckBox), "The custom server option checkbox isn't displayed");
		loginPage.cancelButton.click();
	}

	/**
	 * Will fail because of https://github.com/vector-im/riot-ios/issues/898 </br>
	 * Fill the form with forbidden characters. </br>
	 * Iterates on several datas. </br>
	 * Verifies that the form is not sent.
	 * @throws InterruptedException 
	 * @throws MalformedURLException 
	 */
	@Test(groups={"1driver_ios","loginpage"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void fillForgotFormPasswordWithForbiddenCharacter(String mailTest, String newPwdTest, String confirmPwdTest) throws InterruptedException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getiOsDriver1());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();

		//wait the dialog alert to be displayed
		Assert.assertTrue(waitUntilDisplayed(appiumFactory.getiOsDriver1(), "//XCUIElementTypeAlert[@name='Error']", true, 3), "Dialog error alert isn't displayed");
		loginPage.dialogOkButton.click();
		//wait in case that the reset pwd form is not displayed
		waitUntilDisplayed(appiumFactory.getiOsDriver1(),"//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther",false,1);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.inputsForgetPasswordLayout), "The forget pwd form is not displayed");
		//Since that is an iterative test, we need to setup the next iteration : form must be cleared, fort it app is reset.
		//appiumFactory.getiOsDriver1().resetApp();
		loginPage.cancelButton.click();
	}

	/**
	 * Fill the forgot password form with corrects (but with fake mail) characters. </br>
	 * Check that the login form is displayed then.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","loginpage"})
	public void fillForgotPasswordWithAllowedCharacters() throws InterruptedException{
		String mailTest="riot@gmail.com";
		String newPwdTest="riotuser";
		String confirmPwdTest="riotuser";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getiOsDriver1());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();
		loginPage.dialogOkButton.click();
		Assert.assertTrue(loginPage.authenticationInputContainer.getSize().width>0, "The Riot login page is not displayed.");
	}

	@Test(groups={"1driver_ios","loginpage"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void fillLoginFormWithUnvalidPhoneNumberTest(String emailOrUserName, String phoneNumber, String pwd) throws InterruptedException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getiOsDriver1());
		loginPage.logUser(emailOrUserName,phoneNumber, pwd,false);
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getiOsDriver1(),"RecentsVCTableView", false, 2), "Riot rooms list page is opened and shouldn't");
		appiumFactory.getiOsDriver1().resetApp();
	}

	/**
	 * Log-out the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeGroups(groups="loginpage")
	private void logOutIfNecessary() throws InterruptedException{
		if(false==waitUntilDisplayed(appiumFactory.getiOsDriver1(),"AuthenticationVCView", true, 5)){
			System.out.println("Can't access to the login page, a user must be logged. Forcing the log-out.");
			RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
			homePage.logOutFromTabs();
		}
	}
	
	@AfterMethod(alwaysRun=true)
	private void restart1ApplicationAfterTest(Method m) throws InterruptedException{
		appiumFactory.getiOsDriver1().resetApp();
	}
}
