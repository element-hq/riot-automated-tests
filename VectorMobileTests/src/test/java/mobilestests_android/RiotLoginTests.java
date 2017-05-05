package mobilestests_android;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileNotFoundException;
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

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import pom_android.RiotLoginAndRegisterPageObjects;
import pom_android.main_tabs.RiotHomePageTabObjects;
import utility.Constant;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotLoginTests extends RiotParentTest{
	private String mailTest="riotusertest1@gmail.com";
	
	@Test(groups={"1driver_android","loginpage"})
	public void simpleLogin() throws Exception {
		String sUserName="riotuser2", sPassword="riotuser";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.logUser(sUserName, null,sPassword);
	}

	/**
	 * Log and logout and iterate on several datas from excel file.
	 */
	@Test(groups={"1driver_android","loginpage"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void loginAndLogoutTest(String sUserName,String sPassword)  throws Exception {
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.logUser(sUserName, null,sPassword);
		//Wait for the main page (rooms list) to be opened, and log out.
		RiotHomePageTabObjects homePage=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		Assert.assertTrue(homePage.roomsListView.isDisplayed(), "Rooms list ins't displayed after login.");
		homePage.logOut();
		Assert.assertTrue(loginPage.inputsLoginLayout.isDisplayed(), "The login page isn't displayed after the log-out.");
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
	 * Check the custom server options and verify the form. </br>
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
		Assert.assertEquals(loginPage.homeServerEditText.getText(), Constant.DEFAULT_MATRIX_SERVER_URL);
		Assert.assertEquals(loginPage.identityServerEditText.getText(), Constant.DEFAULT_IDENTITY_SERVER_URL);
	}
	
	/**
	 * Check the reset password form. </br>
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
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		appiumFactory.getAndroidDriver1().hideKeyboard();
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(Constant.DEFAULT_USERPWD);
		loginPage.confirmNewPwdResetPwdEditText.setValue(Constant.DEFAULT_USERPWD);
		loginPage.sendResetEmailButton.click();
		Assert.assertTrue(loginPage.inputsLoginLayout.isDisplayed(), "The Riot login page is not displayed.");
	}
	
	/**
	 * Cover issue https://github.com/vector-im/riot-android/issues/1053 </br>
	 * 1. Hit 'Forgot password?' </br>
	 * 2. Fill the form with existing mail, and matching passwords </br>
	 * Check the controls of the following screen. </br>
	 * 3. Click on the 'I Have verified my mal' without actually verifying the mail </br>
	 * Check that the screen is the same
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","loginpage"})
	public void checkMailSentForForgotPasswordPageTest() throws InterruptedException{
		//1. Hit 'Forgot password?'
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		appiumFactory.getAndroidDriver1().hideKeyboard();
		loginPage.forgotPwdButton.click();
		
		//2. Fill the form with existing mail, and matching passwords
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(Constant.DEFAULT_USERPWD);
		loginPage.confirmNewPwdResetPwdEditText.setValue(Constant.DEFAULT_USERPWD);
		loginPage.sendResetEmailButton.click();
		//Check the controls of the following screen.
		waitUntilDisplayed(appiumFactory.getAndroidDriver1(), "im.vector.alpha:id/search_progress", false, 10);
		Assert.assertEquals(loginPage.emailSentMessageTextView.getText(), "An email has been sent to "+mailTest+". Once you've followed the link it contains, click below.");
		Assert.assertTrue(isPresentTryAndCatch(loginPage.iVerifiedMyMailButton),"'I have verified my email address' button is not here.");
		Assert.assertEquals(loginPage.iVerifiedMyMailButton.getText(), "I have verified my email address");
		
		//3. Click on the 'I Have verified my mal' without actually verifying the mail
		loginPage.iVerifiedMyMailButton.click();
		waitUntilDisplayed(appiumFactory.getAndroidDriver1(), "im.vector.alpha:id/search_progress", false, 10);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.emailSentMessageTextView),"'Mail sent to [address] isn't displayed");
		appiumFactory.getAndroidDriver1().pressKeyCode(AndroidKeyCode.BACK);
		appiumFactory.getAndroidDriver1().pressKeyCode(AndroidKeyCode.BACK);
	}
	
	@Test(groups={"1driver_android","loginpage"},dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void fillLoginFormWithUnvalidPhoneNumberTest(String emailOrUserName, String phoneNumber, String pwd) throws InterruptedException, FileNotFoundException, YamlException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
		loginPage.logUser(emailOrUserName, phoneNumber, pwd);
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getAndroidDriver1(),"im.vector.alpha:id/fragment_recents_list", false, 2), "Riot rooms list page is opened and shouldn't");
		appiumFactory.getAndroidDriver1().resetApp();
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
				RiotHomePageTabObjects homePage=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
				homePage.logOut();
			}
		}
	}
}
