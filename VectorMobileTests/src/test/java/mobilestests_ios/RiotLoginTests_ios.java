package mobilestests_ios;

import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
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
		loginPage.emailOrUserNameTextField.setValue(sUserName);
		loginPage.passwordTextField.setValue(sPassword);
		loginPage.loginButton.click();

		//Wait for the main page (rooms list) to be opened, and log out.
		Thread.sleep(5000);
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		mainPage.logOut();
		Assert.assertTrue(waitUntilDisplayed(AppiumFactory.getiOsDriver1(), "AuthenticationVCView", true, 15), "The login page isn't displayed after the log-out.");
		//Assert.assertTrue(loginPage.authenticationView.isEnabled(), "The login page isn't displayed after the log-out.");
	}
	
	@Test(groups={"2drivers_ios"})
	public void doubleLogin(){
String sUserName="riotuser1";
String sPassword ="riotuser";
		RiotLoginAndRegisterPageObjects loginPage1 = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		RiotLoginAndRegisterPageObjects loginPage2 = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver2());
		loginPage1.emailOrUserNameTextField.setValue(sUserName);
		loginPage1.passwordTextField.setValue(sPassword);
		loginPage1.loginButton.click();
		loginPage2.emailOrUserNameTextField.setValue(sUserName);
		loginPage2.passwordTextField.setValue(sPassword);
		loginPage2.loginButton.click();
	}
	
	@Test(groups={"1driver_ios"})
	public void simpleLogin(){
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		loginPage.emailOrUserNameTextField.setValue("riotuser2");
		loginPage.passwordTextField.setValue("riotuser");
		loginPage.loginButton.click();
		
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getiOsDriver1());
		mainPage.settingsButton.click();
		mainPage.signOutButton.click();
		//mainPage.navigationBar.click();
	}
	
	@Test(groups={"1driver_ios"})
	public void checkCancelButtonNotDisplayed(){
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getiOsDriver1());
		Assert.assertTrue(loginPage.registerButton.isDisplayed());
		//loginPage.cancelButton.click();
		//Assert.assertEquals(loginPage.cancelButton.getAttribute("Is Visible"),"false");
	//	IsElementPresent sd=new IsElementPresent(ByAccessibilityId);
		//IsElementPresent(ByAccessibilityId("ss"));
		//loginPage.cancelButton.
	
//		Assert.assertTrue(ExpectedConditions.elementToBeClickable(loginPage.registerButton) != null);
//		Assert.assertFalse(ExpectedConditions.elementToBeClickable(loginPage.cancelButton) != null);
		System.out.println(loginPage.cancelButton.getSize());
		System.out.println(loginPage.cancelButton.getAttribute("userInteractionEnabled"));
		System.out.println(ExpectedConditions.elementToBeClickable(loginPage.cancelButton));
		System.out.println(ExpectedConditions.invisibilityOfElementLocated(By.name("AuthenticationVCCancelAuthFallbackButton")).toString());
		System.out.println(ExpectedConditions.elementToBeClickable(loginPage.registerButton));
		
//		System.out.println(AppiumFactory.getiOsDriver1().findElementsByAccessibilityId("Register").isEmpty());
//		System.out.println(AppiumFactory.getiOsDriver1().findElementsByAccessibilityId("AuthenticationVCCancelAuthFallbackButton").isEmpty());
		
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
		Assert.assertEquals(loginPage.homeServerTextField.getText(), Constant.DEFAULT_MATRIX_SERVER);
		Assert.assertEquals(loginPage.identityServerTextField.getText(), Constant.DEFAULT_IDENTITY_SERVER);
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
		Assert.assertEquals(loginPage.forgetPasswordMessageLabel.getText(), expectedResetPwdMessage);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.mailResetPwdEditText), "The email address  edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.newPwdResetPwdEditText), "The new password edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.confirmNewPwdResetPwdEditText), "The confirm your new pwd edittext is not present");
		Assert.assertTrue(loginPage.sendResetEmailButton.isEnabled(), "The send reset email button is not enabled");
		//verifies that the login and register button are hidden
		Assert.assertEquals(loginPage.loginButton.getText(), "Send Reset Email");
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
		Assert.assertTrue(loginPage.authenticationInputContainer.getSize().width>0, "The Riot login page is not displayed.");
		//Assert.assertTrue(waitUntilDisplayed(AppiumFactory.getiOsDriver1(), "//XCUIElementTypeApplication/XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeScrollView/XCUIElementTypeOther", true, 2), "The Riot login page is not displayed");
	}
}
