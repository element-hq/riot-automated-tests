package mobilestests_ios;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

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
		Assert.assertTrue(loginPage.loginScrollView.isDisplayed(), "The login page isn't displayed after the log-out.");
	}

}
