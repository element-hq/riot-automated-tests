package pom_ios;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotSettingsPageObjects extends TestUtilities {
	private AppiumDriver<MobileElement> driver;

	public RiotSettingsPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		//waitUntilDisplayed((IOSDriver<MobileElement>) driver,"RecentsVCTableView", true, 5);
	}
	/*
	 * NAV BAR
	 */
	@iOSFindBy(accessibility="SettingsVCNavBarSaveButton")
	public MobileElement saveNavBarButton;
	@iOSFindBy(accessibility="Back")
	public MobileElement backMenuButton;
	@iOSFindBy(accessibility="SettingsVCSignOutButton")
	public MobileElement signOutButton;
	
	/**
	 * Log-out from Riot from the settings view.
	 */
	public void logOutFromSettingsView(){
		signOutButton.click();
		signOutAlertDialogButtonConfirm.click();
	}
	/**
	 * Log out from the rooms list, log in with the parameters, and with custom homeserver.</br>
	 * Return a RiotRoomsListPageObjects POM.</br> Can be used to renew the encryption keys.
	 * @param username
	 * @param pwd
	 * @return new RiotRoomsListPageObjects
	 * @throws InterruptedException 
	 */
	public RiotRoomsListPageObjects logOutAndLoginFromSettingsView(String username, String pwd) throws InterruptedException {
		logOutFromSettingsView();
		RiotLoginAndRegisterPageObjects loginPage= new RiotLoginAndRegisterPageObjects(driver);
		loginPage.logUser(username, null, pwd);
		return new RiotRoomsListPageObjects(driver);
	}
	
	/*
	 * USER SETTINGS
	 */
	@iOSFindBy(accessibility="SettingsVCProfilPictureCell")
	public MobileElement profilePictureCell;
	@iOSFindBy(accessibility="SettingsVCDisplayNameTextField")
	public MobileElement displayNameTextField;
	@iOSFindBy(accessibility="SettingsVCChangePwdStaticText")
	public MobileElement changePasswordStaticText;
	@iOSFindBy(accessibility="SettingsVCSignoutAlertActionSign Out")
	public MobileElement signOutAlertDialogButtonConfirm;
	@iOSFindBy(accessibility="SettingsVCSignoutAlertActionCancel")
	public MobileElement signOutAlertDialogButtonCancel;
	
	/*
	 * ADVANCED
	 */
	@iOSFindBy(accessibility="SettingsVCConfigStaticText")
	public MobileElement configStaticText;
	
	
	/**
	 * From the settings view, erase the display name and set a new one.</br>
	 * It doesn't click on the save button.
	 * @param newDisplayName
	 */
	public void changeDisplayNameFromSettings(String newDisplayName){
		displayNameTextField.click();
		displayNameTextField.clear();
		displayNameTextField.setValue(newDisplayName);
	}
	
	/**
	 * From the settings view, hit the profile picture item, and change the avatar by taking a new picture.</br>
	 * It doesn't click on the save button.
	 * @throws InterruptedException 
	 */
	public void changeAvatarFromSettings() throws InterruptedException{
		profilePictureCell.click();
		RiotCameraPageObjects cameraPage = new RiotCameraPageObjects(appiumFactory.getiOsDriver1());
		cameraPage.cameraCaptureButton.click();
		waitUntilDisplayed(driver, "OK", true, 10);
		cameraPage.okButton.click();
	}
	
	/**
	 * From the settings view, hit the Change password item, change the password in the AlertDialog and click on save on this alert.
	 * </br> Then click on the confirmation alertbox 'Your pwd have been updated'.
	 * @param oldPwd
	 * @param newPwd
	 * @throws InterruptedException 
	 */
	public void changePasswordFromSettings(String oldPwd, String newPwd, Boolean expectedCorrectlyChange) throws InterruptedException{
		changePasswordStaticText.click();
		driver.getKeyboard().sendKeys(oldPwd+"\n");
		driver.getKeyboard().sendKeys(newPwd+"\n");
		driver.getKeyboard().sendKeys(newPwd);
		//driver.findElementByAccessibilityId("Save").click();
		driver.findElementsByClassName("XCUIElementTypeCollectionView").get(1).findElementsByClassName("XCUIElementTypeCell").get(1).click();
		if(expectedCorrectlyChange){
			Assert.assertTrue(waitUntilDisplayed(driver, "Your password has been updated", true, 10), "Password updated alert dialog isn't displayed after changing the password");
			driver.findElementByAccessibilityId("SettingsVCOnPasswordUpdatedAlertActionOK").click();
		}else{
			Assert.assertTrue(waitUntilDisplayed(driver, "Fail to update password", true, 10), "Password updated fail dialog isn't displayed after changing the password");
			driver.findElementByAccessibilityId("SettingsVCPasswordChangeFailedAlertActionOK").click();
		}

		
	}
}
