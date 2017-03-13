package mobilestests_android;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import pom_android.RiotLoginAndRegisterPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.Constant;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotLoginTests extends RiotParentTest{
	
	@Test(groups={"1driver_android","loginpage"})
	public void simpleLogin() throws Exception {
		String sUserName="riotuser2", sPassword="riotuser";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.fillLoginForm(sUserName, sPassword);
	}

	/**
	 * Log and logout and iterate on several datas from excel file.
	 */
	@Test(groups={"1driver_android","loginpage"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void loginAndLogoutTest(String sUserName,String sPassword)  throws Exception {
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.fillLoginForm(sUserName, sPassword);
		//Wait for the main page (rooms list) to be opened, and log out.
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		Assert.assertTrue(mainPage.roomsExpandableListView.isDisplayed(), "Rooms list ins't displayed after login.");
		mainPage.logOut();
		Assert.assertTrue(loginPage.inputsLoginLayout.isDisplayed(), "The login page isn't displayed after the log-out.");
	}
	
	/**
	 * Check the custom server options and verify the form.
	 */
	@Test(groups={"1driver_android","loginpage"})
	public void customServerOptionsCheck(){
		String homeServerTextView="Home Server:";
		String identityServerTextView="Identity Server:";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		//hide the keyboard
		appiumFactory.getAndroidDriver1().hideKeyboard();
		loginPage.customServerOptionsCheckBox.click();
		Assert.assertEquals(loginPage.homeServerTextView.getText(), homeServerTextView);
		Assert.assertEquals(loginPage.identityServerTextView.getText(), identityServerTextView);
		Assert.assertEquals(loginPage.homeServerEditText.getText(), Constant.DEFAULT_MATRIX_SERVER);
		Assert.assertEquals(loginPage.identityServerEditText.getText(), Constant.DEFAULT_IDENTITY_SERVER);
	}
	
	/**
	 * Check the reset password form.
	 * Doesn't verifies the reset password function.
	 */
	@Test(groups={"1driver_android","loginpage"})
	public void forgotPasswordFormTest(){
		String expectedResetPwdMessage="To reset your password, enter the email address linked to your account:";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.forgotPwdButton.click();
		//hide the keyboard
		appiumFactory.getAndroidDriver1().hideKeyboard();
		//assertions on the form
		Assert.assertEquals(loginPage.resetPasswordTextView.getText(), expectedResetPwdMessage);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.mailResetPwdEditText), "The email address  edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.newPwdResetPwdEditText), "The new password edittext is not present");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.confirmNewPwdResetPwdEditText), "The confirm your new pwd edittext is not present");
		Assert.assertTrue(loginPage.sendResetEmailButton.isEnabled(), "The send reset email button is not enabled");
		//verifies that the login and register button are hidden
		Assert.assertFalse(isPresentTryAndCatch(loginPage.loginButton), "The login button is displayed");
		Assert.assertFalse(isPresentTryAndCatch(loginPage.registerButton), "The register button is displayed");
		//riot logo is still displayed
		Assert.assertTrue(isPresentTryAndCatch(loginPage.riotLogoImageView), "The riot logo isn't displayed");
		//custom server option checkbox is still displayed
		Assert.assertTrue(isPresentTryAndCatch(loginPage.customServerOptionsCheckBox), "The custom server option checkbox isn't displayed");
		//come back in login page
		appiumFactory.getAndroidDriver1().pressKeyCode(AndroidKeyCode.BACK);
	}
	
	/**
	 * Fill the form with forbidden characters. </br>
	 * Iterates on several datas. </br>
	 * Verifies that the form is not sent.
	 * @throws InterruptedException 
	 * @throws MalformedURLException 
	 */
	@Test(groups={"1driver_android","loginpage"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void fillForgotFormPasswordWithForbiddenCharacter(String mailTest, String newPwdTest, String confirmPwdTest) throws InterruptedException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();
		//wait in case that the reset pwd form is not displayed
		waitUntilDisplayed(appiumFactory.getAndroidDriver1(),"im.vector.alpha:id/forget_password_inputs_layout",false,1);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.inputsForgetPasswordLayout), "The forget pwd form is not displayed");
		//Since that is an iterative test, we need to setup the next iteration : form must be cleared, fort it app is reset.
		appiumFactory.getAndroidDriver1().resetApp();
	}
	
	/**
	 * Fill the forgot password form with corrects (but with fake mail) characters. </br>
	 * Check that the login form is displayed then.
	 */
	@Test(groups={"1driver_android","loginpage"})
	public void fillForgotPasswordWithAllowedCharacters(){
		String mailTest="riot@gmail.com";
		String newPwdTest="riotuser";
		String confirmPwdTest="riotuser";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();
		Assert.assertTrue(loginPage.inputsLoginLayout.isDisplayed(), "The Riot login page is not displayed.");
	}
	
	@Test(groups={"1driver_android","loginpage"})
	public void checkRiotLogoFromLoginPage() throws IOException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		boolean status = false;
	    //take screen shot
		String destDir = "screenshots\\comparison";
		// Capture screenshot.
		File scrLoginPageFile = ((TakesScreenshot) appiumFactory.getAndroidDriver1()).getScreenshotAs(OutputType.FILE);
		// Set date format to set It as screenshot file name.
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
		// Create folder under project with name "screenshots" provided to destDir.
		new File(destDir).mkdirs();
		// Set file name using current date time.
		String destFile = dateFormat.format(new Date()) + ".png";
		try {
			FileUtils.copyFile(scrLoginPageFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

	    MobileElement riotLogo = loginPage.riotLogoImageView;
	    //retrieval of logo dimensions
	    org.openqa.selenium.Dimension riotLogoDim=riotLogo.getSize();
	    Assert.assertTrue(riotLogoDim.height!=0 && riotLogoDim.width!=0, "Riot logo has null dimension");
	    Point point = riotLogo.getLocation();
	    System.out.println(point.toString());
	    //get element dimension
	    int width = riotLogo.getSize().getWidth();
	    int height = riotLogo.getSize().getHeight();

	    BufferedImage img = ImageIO.read(scrLoginPageFile);
	    BufferedImage dest = img.getSubimage(point.getX(), point.getY(), width,
	                                                                 height);
	    ImageIO.write(dest, "png", scrLoginPageFile);
	    File file = new File(destDir + "/"+"riotActualLogo.png");
	    FileUtils.copyFile(scrLoginPageFile, file);
	    
	    //get the expected image
	    String pathToExpectedRiotLogo = "src\\test\\resources\\expected_images\\logo_login.png";
	    File fileOutPut = new File(pathToExpectedRiotLogo);
//	    verifyImage(destDir + "/"+"riotActualLogo.png", pathToExpectedRiotLogo );
//	    Assert.assertTrue(status, "FAIL Event doesn't match");
	}
	private void verifyImage(String image1, String image2) throws IOException{
//	    File fileInput = new File(image1);
//	    File fileOutPut = new File(image2);
//
//	    BufferedImage bufileInput = ImageIO.read(fileInput);
//	    DataBuffer dafileInput = bufileInput.getData().getDataBuffer();
//	    int sizefileInput = dafileInput.getSize();                     
//	    BufferedImage bufileOutPut = ImageIO.read(fileOutPut);
//	    DataBuffer dafileOutPut = bufileOutPut.getData().getDataBuffer();
//	    int sizefileOutPut = dafileOutPut.getSize();
//	    Boolean matchFlag = true;
//	    if(sizefileInput == sizefileOutPut) {                         
//	       for(int j=0; j<sizefileInput; j++) {
//	             if(dafileInput.getElem(j) != dafileOutPut.getElem(j)) {
//	                   matchFlag = false;
//	                   break;
//	             }
//	        }
//	    }
//	    else                            
//	       matchFlag = false;
//	    Assert.assertTrue(matchFlag, "Images are not same");    
	    File fileInput = new File(image1);
	    File fileOutPut = new File(image2);

	    BufferedImage bufileInput = ImageIO.read(fileInput);
	    DataBuffer dafileInput = bufileInput.getData().getDataBuffer();
	    int sizefileInput = dafileInput.getSize();                     
	    BufferedImage bufileOutPut = ImageIO.read(fileOutPut);
	    //Finder finder = new Finder("path/to/image", new Region(0, 0, <imgwidth>, <imgheight>));
	 }
	
	private void clearFiled(AndroidDriver driver,MobileElement textFieldToClear){
		textFieldToClear.click();
		for (int i = 0;i<=textFieldToClear.getText().length();i++){
			driver.pressKeyCode(AndroidKeyCode.DEL );
		}

	}
	/**
	 * Log-out the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod(groups="loginpage")
	private void logOutIfNecessary() throws InterruptedException{
		if (appiumFactory.getAndroidDriver1() instanceof AndroidDriver){
			if(false==waitUntilDisplayed(appiumFactory.getAndroidDriver1(),"im.vector.alpha:id/main_input_layout", true, 5)){
				System.out.println("Can't access to the login page, a user must be logged. Forcing the log-out.");
				RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
				mainPage.logOut();
			}
		}
	}
}
