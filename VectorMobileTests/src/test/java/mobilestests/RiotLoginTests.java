package mobilestests;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.android.Connection;
import jdk.nashorn.internal.ir.annotations.Ignore;
import pom.RiotLoginAndRegisterPageObjects;
import pom.RiotRoomsListPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.DataproviderClass;
import utility.ScreenshotUtility;
import utility.testUtilities;

@Listeners({ ScreenshotUtility.class })
public class RiotLoginTests  extends testUtilities{
	@BeforeClass
	public void setUp() throws MalformedURLException{
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("deviceName",Constant.DEVICE_NAME);
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");
		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");

		//Create RemoteWebDriver instance and connect to the Appium server
		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
		AppiumFactory appiumFactory=new AppiumFactory();
		appiumFactory.setDriver(new URL(Constant.SERVER_ADRESS), capabilities);
		System.out.println("setUp() done");
	}

	@AfterClass
	public void tearDown(){
		AppiumFactory.getAppiumDriver().quit();
	}
	
	@Test//(enabled=true)
	public void simpleLogin() throws Exception {
		String sUserName="riotuser2", sPassword="riotuser";
		MobileElement emailOrUserNameEditText= (MobileElement) AppiumFactory.getAppiumDriver().findElement(By.id("im.vector.alpha:id/login_user_name"));
		MobileElement passwordEditText= AppiumFactory.getAppiumDriver().findElement(By.id("im.vector.alpha:id/login_password"));
		MobileElement loginButton= AppiumFactory.getAppiumDriver().findElement(By.id("im.vector.alpha:id/button_login"));
		emailOrUserNameEditText.setValue(sUserName);
		passwordEditText.setValue(sPassword);
		loginButton.click();
		//		emailOrUserNameEditText.click();
//		AppiumFactory.getAppiumDriver().getKeyboard().sendKeys(sUserName);
//		passwordEditText.click();
//		AppiumFactory.getAppiumDriver().getKeyboard().sendKeys(sPassword);
		loginButton.click();
	}

	/**
	 * Log and logout and iterate on several datas from excel file.
	 */
	@Test(dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void loginAndLogoutTest(String sUserName,String sPassword)  throws Exception {
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		loginPage.emailOrUserNameEditText.setValue(sUserName);
		loginPage.passwordEditText.setValue(sPassword);
		loginPage.loginButton.click();

		//Wait for the main page (rooms list) to be opened, and log out.
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		//Thread.sleep(5000);
		mainPage.contextMenuButton.click();
		mainPage.logOutButton.click();
		Assert.assertTrue(loginPage.inputsLoginLayout.isDisplayed(), "The login page isn't displayed after the log-out.");
	}
	
	/**
	 * Check the custom server options and verify the form.
	 */
	@Test
	public void customServerOptionsCheck(){
		String homeServerTextView="Home Server:";
		String identityServerTextView="Identity Server:";
		String defaultHomeServerTextEdit="https://matrix.org";
		String defaultIdentityServerTextEdit="https://vector.im";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		//hide the keyboard
		AppiumFactory.getAppiumDriver().hideKeyboard();
		loginPage.customServerOptionsCheckBox.click();
		Assert.assertEquals(loginPage.homeServerTextView.getText(), homeServerTextView);
		Assert.assertEquals(loginPage.identityServerTextView.getText(), identityServerTextView);
		Assert.assertEquals(loginPage.homeServerEditText.getText(), defaultHomeServerTextEdit);
		Assert.assertEquals(loginPage.identityServerEditText.getText(), defaultIdentityServerTextEdit);
	}
	
	/**
	 * Check the reset password form.
	 * Doesn't verifies the reset password function.
	 */
	@Test
	public void forgotPasswordFormTest(){
		String expectedResetPwdMessage="To reset your password, enter the email address linked to your account:";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		loginPage.forgotPwdButton.click();
		//hide the keyboard
		AppiumFactory.getAppiumDriver().hideKeyboard();
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
	}
	
	/**
	 * Fill the form with forbidden characters. </br>
	 * Iterates on several datas. </br>
	 * Verifies that the form is not sent.
	 * @throws InterruptedException 
	 * @throws MalformedURLException 
	 */
	@Test(dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
	public void fillForgotFormPasswordWithForbiddenCharacter(String mailTest, String newPwdTest, String confirmPwdTest) throws InterruptedException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();
		//wait in case that the reset pwd form is not displayed
		waitUntilDisplayed("im.vector.alpha:id/forget_password_inputs_layout",false,1);
		Assert.assertTrue(isPresentTryAndCatch(loginPage.inputsForgetPasswordLayout), "The forget pwd form is not displayed");
		//Since that is an iterative test, we need to setup the next iteration : going back to the login page.
		((AndroidDeviceActionShortcuts) AppiumFactory.getAppiumDriver()).pressKeyCode(AndroidKeyCode.BACK);
		((AndroidDeviceActionShortcuts) AppiumFactory.getAppiumDriver()).pressKeyCode(AndroidKeyCode.BACK);
	}
	
	/**
	 * Fill the forgot password form with corrects (but with fake mail) characters. </br>
	 * Check that the login form is displayed then.
	 */
	@Test
	public void fillForgotPasswordWithAllowedCharacters(){
		String mailTest="riot@gmail.com";
		String newPwdTest="riotuser";
		String confirmPwdTest="riotuser";
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		loginPage.forgotPwdButton.click();
		loginPage.mailResetPwdEditText.setValue(mailTest);
		loginPage.newPwdResetPwdEditText.setValue(newPwdTest);
		loginPage.confirmNewPwdResetPwdEditText.setValue(confirmPwdTest);
		loginPage.sendResetEmailButton.click();
		Assert.assertTrue(loginPage.inputsLoginLayout.isDisplayed(), "The Riot login page is not displayed.");
	}
	
	/**
	 * Cut the wifi, launches Riot and asserts that the login button is disabled. </br>
	 * Bring back the wifi and verifies that the login button become enabled.
	 * @throws InterruptedException 
	 */
	@Test
	public void logInWithoutInternetConnection() throws InterruptedException{
		AppiumFactory.getAppiumDriver().setConnection(Connection.NONE);
		System.out.println("wifi off");
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		Assert.assertFalse(loginPage.loginButton.isEnabled(), "The loginButton should be disabled.");
		AppiumFactory.getAppiumDriver().setConnection(Connection.WIFI);
		System.out.println("wifi on");
		Assert.assertTrue(loginPage.loginButton.isEnabled(), "The loginButton should be enabled.");
	}
	
	@Test
	public void checkRiotLogoFromLoginPage() throws IOException{
		RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(AppiumFactory.getAppiumDriver());
		boolean status = false;
	    //take screen shot
		String destDir = "screenshots\\comparison";
		// Capture screenshot.
		File scrLoginPageFile = ((TakesScreenshot) AppiumFactory.getAppiumDriver()).getScreenshotAs(OutputType.FILE);
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
	public void verifyImage(String image1, String image2) throws IOException{
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

	@Ignore
	@Test
	public void logoutTest() throws Exception {
		if(AppiumFactory.getAppiumDriver()!=null){
			System.out.println("driver does not == null");
			AppiumFactory.getAppiumDriver().findElement(By.xpath("//android.widget.ImageButton"));
		} else {
			System.out.println("driver == null");
		}
		MobileElement mainMenuBis=(MobileElement) AppiumFactory.getAppiumDriver().findElementByXPath("//android.widget.ImageButton");

		//WebElement mainMenuBis=driver.findElementByClassName("android.widget.ImageButton");
		mainMenuBis.click();

		//touch the logout button
		WebElement logOutButton= AppiumFactory.getAppiumDriver().findElement(By.name("Logout"));
		logOutButton.click();
		//validate that we are on the log-in page
		WebElement riotLoginLogo= AppiumFactory.getAppiumDriver().findElement(By.id("im.vector.alpha:id/login_large_logo"));
		Assert.assertTrue(riotLoginLogo.isDisplayed(), "Riot Logo isn't displayed");
	}
	
	/**
	 * Log-out the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod
	public void logoutForSetup() throws InterruptedException{
		if(false==waitUntilDisplayed("im.vector.alpha:id/main_input_layout", true, 5)){
			System.out.println("Can't access to the login page, a user must be logged. Forcing the log-out.");
			RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
			mainPage.logOut();
		}
	}
}
