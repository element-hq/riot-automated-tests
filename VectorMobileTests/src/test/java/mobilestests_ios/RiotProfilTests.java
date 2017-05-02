package mobilestests_ios;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.RiotSettingsPageObjects;
import pom_ios.main_tabs.RiotHomePageTabObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * TODO
 * Tests on User Settings section from Settings view. 
 * @author jeangb
 *
 */
@Listeners({ ScreenshotUtility.class })
public class RiotProfilTests extends RiotParentTest{
	String testUser="riotuser11";
	
	/**
	 * 1. Open settings from rooms list. </br>
	 * 2. Click on 'Change Password' from USER SETTINGS section. </br>
	 * 3. Change the password </br>
	 * Verify that the 'password have been updated' is displayed </br>
	 * 4. Log out </br>
	 * 5. Login with the new password </br>
	 * Check that the log-in is correctly done </br>
	 * 6. Open settings and change password by setting the first one. </br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void changePasswordWithCorrectsInputsFromSettingsTest() throws InterruptedException{
		String newPwd="newPwd";
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		//1. Open settings from rooms list.
		RiotSettingsPageObjects settingsPage = homePage.openRiotSettings();
		//2. Click on 'Change Password' from USER SETTINGS section & 3. Change the password
		settingsPage.changePasswordFromSettings(Constant.DEFAULT_USERPWD, newPwd,true);
		//4. Log out & 5. Login with the new password
		settingsPage.backMenuButton.click();
		homePage=homePage.logOutAndLogin(testUser, newPwd);
		//6. Open settings and change password by setting the first one.
		waitUntilDisplayed(appiumFactory.getiOsDriver1(), "settings icon", true, 5);
		settingsPage = homePage.openRiotSettings();
		settingsPage.changePasswordFromSettings(newPwd,Constant.DEFAULT_USERPWD,true);
		settingsPage.backMenuButton.click();
	}
	
	/**
	 * 1. Open settings from rooms list. </br>
	 * 2. Click on 'Change Password' from USER SETTINGS section. </br>
	 * 3. Change the password with wrong inputs </br>
	 * Verify that the 'password change fail' is displayed </br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void changePasswordWithWrongInputsFromSettingsTest() throws InterruptedException{
		String newPwd="newPwd", wrongPwd="fakePwd";
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		//1. Open settings from rooms list.
		RiotSettingsPageObjects settingsPage = homePage.openRiotSettings();
		//2. Click on 'Change Password' from USER SETTINGS section & 3. Change the password
		settingsPage.changePasswordFromSettings(wrongPwd, newPwd,false);
		settingsPage.backMenuButton.click();
	}
	
	/**
	 * 1. Open settings from rooms list. </br>
	 * 2. Click on 'Display Name' from USER SETTINGS section. </br>
	 * 3. Set a new one, hit the Done button. </br>
	 * 4. Hit the Save button on the navigation bar, then the back button. </br>
	 * 5. Open settings again </br>
	 * Check that the new display name is correctly set. </br>
	 * 6. Set the old display name, hit Save Button, then back button.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void changeDisplayNameFromSettingsTest() throws InterruptedException{
		String displayNameBeforeChange;
		String newDisplayName="newOne";
		RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		//1. Open settings from rooms list.
		RiotSettingsPageObjects settingsPage = homePage.openRiotSettings();
		//2. Click on 'Display Name' from USER SETTINGS section.
		displayNameBeforeChange=settingsPage.displayNameTextField.getText();
		//3. Set a new one, hit the Done button.
		settingsPage.changeDisplayNameFromSettings(newDisplayName);
		//4. Hit the Save button on the navigation bar, then the back button.
		settingsPage.saveNavBarButton.click();
		settingsPage.backMenuButton.click();
		//5. Open settings again
		settingsPage = homePage.openRiotSettings();
		//Check that the new display name is correctly set.
		Assert.assertEquals(settingsPage.displayNameTextField.getText(), newDisplayName);
		//6. Set the old display name, hit Save Button, then back button.
		settingsPage.changeDisplayNameFromSettings(displayNameBeforeChange);
		settingsPage.saveNavBarButton.click();
		settingsPage.backMenuButton.click();
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 * @throws YamlException 
	 * @throws FileNotFoundException 
	 */
	@BeforeGroups("checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), testUser, Constant.DEFAULT_USERPWD);
	}
}
