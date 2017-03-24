package mobilestests_ios;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.Constant;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests about registration on riot-iOS.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotRegisterTests extends RiotParentTest{

	/**
	 * Cover issue https://github.com/vector-im/riot-ios/issues/1125
	 * 1. Hit the register button.
	 * 2. Fill the first form with valid displayName and matching passwords.
	 * 3. Fill the second form with an unvalid phone number.
	 * 4. Hit submit button
	 * Check that the form isn't sent.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void registerWithUnvalidPhoneNumberTest(String phoneNumber) throws InterruptedException{
		int userNamesuffix = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String displayNameTest=(new StringBuilder("riotuser").append(userNamesuffix)).toString();
		RiotLoginAndRegisterPageObjects loginRegisterView= new RiotLoginAndRegisterPageObjects(appiumFactory.getiOsDriver1());
		//1. Hit the register button.
		loginRegisterView.registerNavBarButton.click();
		//2. Fill the first form with valid displayName and matching passwords.
		loginRegisterView.emailTextField.setValue(displayNameTest);
		loginRegisterView.passwordTextField.setValue(Constant.DEFAULT_USERPWD);
		loginRegisterView.repeatPasswordTextField.setValue(Constant.DEFAULT_USERPWD+"\n");
		//3. Fill the second form with a phone number exceeding 17 characters && 4. Hit submit button
		loginRegisterView.phoneNumberTextField.setValue(phoneNumber+"\n");
		//Check that the form isn't sent.
		Assert.assertTrue(waitUntilDisplayed(appiumFactory.getiOsDriver1(), "Registration Failed", true, 2), "'Registration failed' popup isn't displayed.");
		Assert.assertTrue(waitUntilDisplayed(appiumFactory.getiOsDriver1(), "This doesn't look like a valid phone number", true, 2), "Msg about not valid phone number isn't displayed.");
		//back to login
		loginRegisterView.backButton.click();
		loginRegisterView.loginNavBarButton.click();
	}
	
	/**
	 * Log-out the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeGroups(groups="loginpage")
	private void logOutIfNecessary() throws InterruptedException{
		if(false==waitUntilDisplayed(appiumFactory.getiOsDriver1(),"AuthenticationVCView", true, 5)){
			System.out.println("Can't access to the login page, a user must be logged. Forcing the log-out.");
			RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
			mainPage.logOutFromRoomsList();
		}
	}
}
