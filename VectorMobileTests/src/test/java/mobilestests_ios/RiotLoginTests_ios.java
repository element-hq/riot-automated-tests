package mobilestests_ios;

import java.net.MalformedURLException;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on login on IOS platform.
 * @author matrix
 *
 */
@Listeners({ ScreenshotUtility.class })
public class RiotLoginTests_ios extends RiotParentTest{
	
	/**
	 * Log and logout and iterate on several datas from excel file.
	 */
	@Test(groups={"1driver_ios"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void loginAndLogoutiOsTest(String sUserName,String sPassword)  throws Exception {
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		loginPage.emailOrUserNameEditText.setValue(sUserName);
		loginPage.passwordEditText.setValue(sPassword);
		loginPage.loginButton.click();

		//Wait for the main page (rooms list) to be opened, and log out.
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		//Thread.sleep(5000);
		mainPage.logOut();
		waitUntilDisplayed(AppiumFactory.getiOsDriver1(), "//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther", true, 5);
		Assert.assertTrue(loginPage.loginScrollView.isEnabled(), "The login page isn't displayed after the log-out.");
	}
	
	
	@Test(groups={"1driver_ios"})
	public void simpleLogin(){
//		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
//		loginPage.emailOrUserNameEditText.setValue("riotuser2");
//		loginPage.passwordEditText.setValue("riotuser");
//		loginPage.loginButton.click();
		
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		mainPage.settingsButton.click();
		mainPage.signOutButton.click();
		//mainPage.navigationBar.click();
	}
	
	/**
	 * Check the custom server options and verify the form.
	 */
	@Test(groups={"1driver_ios"})
	public void customServerOptionsCheck(){
		String homeServerTextView="Home Server:";
		String identityServerTextView="Identity Server:";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		AppiumFactory.getiOsDriver1().hideKeyboard();
		loginPage.customServerOptionsCheckBox.click();
		Assert.assertEquals(loginPage.homeServerStaticText.getText(), homeServerTextView);
		Assert.assertEquals(loginPage.identityServerStaticText.getText(), identityServerTextView);
		Assert.assertEquals(loginPage.homeServerEditText.getText(), Constant.DEFAULT_MATRIX_SERVER);
		Assert.assertEquals(loginPage.identityServerEditText.getText(), Constant.DEFAULT_IDENTITY_SERVER);
	}

	/**
	 * Check the reset password form.
	 * Doesn't verifies the reset password function.
	 */
	@Test(groups={"1driver_ios"})
	public void forgotPasswordFormTest(){
		String expectedResetPwdMessage="To reset your password, enter the email address linked to your account:";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		loginPage.forgotPwdButton.click();
		Assert.assertEquals(loginPage.resetPasswordTextView.getText(), expectedResetPwdMessage);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.mailResetPwdEditText), "The email address  edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.newPwdResetPwdEditText), "The new password edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.confirmNewPwdResetPwdEditText), "The confirm your new pwd edittext is not present");
		Assert.assertTrue(loginPage.sendResetEmailButton.isEnabled(), "The send reset email button is not enabled");
		//verifies that the login and register button are hidden
		Assert.assertFalse(isPresentTryAndCatch(loginPage.loginButton), "The login button is displayed");
		Assert.assertFalse(isPresentTryAndCatch(loginPage.registerButton), "The register button is displayed");
		//riot logo is still displayed
		Assert.assertTrue(isPresentTryAndCatch(loginPage.riotLogoImage), "The riot logo isn't displayed");
		//custom server option checkbox is still displayed
		Assert.assertTrue(isPresentTryAndCatch(loginPage.customServerOptionsCheckBox), "The custom server option checkbox isn't displayed");
		loginPage.cancelButton.click();
	}
	
	/**
	 * Will fail because of https://github.com/vector-im/riot-ios/issues/898
	 * Fill the form with forbidden characters. </br>
	 * Iterates on several datas. </br>
	 * Verifies that the form is not sent.
	 * @throws InterruptedException 
	 * @throws MalformedURLException 
	 */
	@Test(groups={"1driver_ios"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void fillForgotFormPasswordWithForbiddenCharacter(String mailTest, String newPwdTest, String confirmPwdTest) throws InterruptedException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();
		
		//wait the dialog alert to be displayed
		Assert.assertTrue(waitUntilDisplayed(AppiumFactory.getiOsDriver1(), "//XCUIElementTypeAlert[@name='Error']", true, 3), "Dialog error alert isn't displayed");
		loginPage.dialogOkButton.click();
		//wait in case that the reset pwd form is not displayed
		waitUntilDisplayed(AppiumFactory.getiOsDriver1(),"//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeScrollView/XCUIElementTypeOther/XCUIElementTypeOther",false,1);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.inputsForgetPasswordLayout), "The forget pwd form is not displayed");
		//Since that is an iterative test, we need to setup the next iteration : form must be cleared, fort it app is reset.
		//AppiumFactory.getiOsDriver1().resetApp();
		loginPage.cancelButton.click();
	}
	
	/**
	 * Will fail because of https://github.com/vector-im/riot-ios/issues/898
	 * Fill the forgot password form with corrects (but with fake mail) characters. </br>
	 * Check that the login form is displayed then.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios"})
	public void fillForgotPasswordWithAllowedCharacters() throws InterruptedException{
		String mailTest="riot@gmail.com";
		String newPwdTest="riotuser";
		String confirmPwdTest="riotuser";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();
		//Assert.assertTrue(loginPage.loginScrollView.isDisplayed(), "The Riot login page is not displayed.");
		Assert.assertTrue(loginPage.loginScrollView.getSize().width>0, "The Riot login page is not displayed.");
		//Assert.assertTrue(waitUntilDisplayed(AppiumFactory.getiOsDriver1(), "//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther", true, 2), "The Riot login page is not displayed");
	}
}
