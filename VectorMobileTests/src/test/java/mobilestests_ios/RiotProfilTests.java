package mobilestests_ios;

import org.testng.annotations.Test;

import utility.RiotParentTest;

/**
 * TODO
 * Tests on User Settings section from Settings view. 
 * @author jeangb
 *
 */
public class RiotProfilTests extends RiotParentTest{
	/**
	 * 1. Open settings from rooms list.
	 * 2. Click on 'Change Password' from USER SETTINGS section.
	 * 3. Change the password
	 * Verify that the 'password have been updated' is displayed
	 * 4. Log out
	 * 5. Login with the new password
	 * Check that the log-in is correctly done
	 * 6. Open settings and change password by setting the first one.
	 */
	@Test(groups="1driver_ios")
	public void changePasswordFromSettingsTest(){
		
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
	@Test(groups="1driver_ios")
	public void changeDisplayNameFromSettingsTest(){
		
	}
}
