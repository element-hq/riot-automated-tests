package mobilestests_ios;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_ios.RiotRoomsListPageObjects;
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
	 * 1. Open settings from rooms list.
	 * 2. Click on 'Change Password' from USER SETTINGS section.
	 * 3. Change the password
	 * Verify that the 'password have been updated' is displayed
	 * 4. Log out
	 * 5. Login with the new password
	 * Check that the log-in is correctly done
	 * 6. Open settings and change password by setting the first one.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void changePasswordWithCorrectsInputsFromSettingsTest() throws InterruptedException{
		String newPwd="newPwd";
		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		//1. Open settings from rooms list.
		roomsList1.settingsButton.click();
		//2. Click on 'Change Password' from USER SETTINGS section & 3. Change the password
		roomsList1.changePasswordFromSettings(Constant.DEFAULT_USERPWD, newPwd,true);
		//4. Log out & 5. Login with the new password
		roomsList1.backMenuButton.click();
		roomsList1=roomsList1.logOutAndLogin(testUser, newPwd);
		//6. Open settings and change password by setting the first one.
		waitUntilDisplayed(appiumFactory.getiOsDriver1(), "settings icon", true, 5);
		roomsList1.settingsButton.click();
		roomsList1.changePasswordFromSettings(newPwd,Constant.DEFAULT_USERPWD,true);
		roomsList1.backMenuButton.click();
	}
	
	/**
	 * 1. Open settings from rooms list.
	 * 2. Click on 'Change Password' from USER SETTINGS section.
	 * 3. Change the password with wrong inputs
	 * Verify that the 'password change fail' is displayed
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void changePasswordWithWrongInputsFromSettingsTest() throws InterruptedException{
		String newPwd="newPwd", wrongPwd="fakePwd";
		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		//1. Open settings from rooms list.
		roomsList1.settingsButton.click();
		//2. Click on 'Change Password' from USER SETTINGS section & 3. Change the password
		roomsList1.changePasswordFromSettings(wrongPwd, newPwd,false);
		roomsList1.backMenuButton.click();
	}
	
	/**
	 * 1. Open settings from rooms list.
	 * 2. Click on 'Display Name' from USER SETTINGS section.
	 * 3. Set a new one, hit the Done button.
	 * 4. Hit the Save button on the navigation bar, then the back button.
	 * 5. Open settings again
	 * Check that the new display name is correctly set.
	 * 6. Set the old display name, hit Save Button, then back button.
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void changeDisplayNameFromSettingsTest(){
		String displayNameBeforeChange;
		String newDisplayName="newOne";
		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		//1. Open settings from rooms list.
		roomsList1.settingsButton.click();
		//2. Click on 'Display Name' from USER SETTINGS section.
		displayNameBeforeChange=roomsList1.displayNameTextField.getText();
		//3. Set a new one, hit the Done button.
		roomsList1.changeDisplayNameFromSettings(newDisplayName);
		//4. Hit the Save button on the navigation bar, then the back button.
		roomsList1.saveNavBarButton.click();
		roomsList1.backMenuButton.click();
		//5. Open settings again
		roomsList1.settingsButton.click();
		//Check that the new display name is correctly set.
		Assert.assertEquals(roomsList1.displayNameTextField.getText(), newDisplayName);
		//6. Set the old display name, hit Save Button, then back button.
		roomsList1.changeDisplayNameFromSettings(displayNameBeforeChange);
		roomsList1.saveNavBarButton.click();
		roomsList1.backMenuButton.click();
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups("checkuser")
	private void checkIfUserLogged() throws InterruptedException{
		super.checkIfUserLoggedIos(appiumFactory.getiOsDriver1(), testUser, Constant.DEFAULT_USERPWD);
	}
}
