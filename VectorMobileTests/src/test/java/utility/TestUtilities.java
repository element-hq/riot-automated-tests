package utility;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.Connection;
import io.appium.java_client.ios.IOSDriver;
import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.RiotSettingsPageObjects;
import pom_ios.main_tabs.RiotHomePageTabObjects;

public class TestUtilities extends MatrixUtilities{
	public static AppiumFactory appiumFactory=AppiumFactory.getInstance();

	public void ExplicitWait(AppiumDriver<MobileElement> driver, WebElement element){
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(element));
		System.out.println((Object) element.getTagName()+" clickable");
	}
	public void ExplicitWaitToBeVisible(AppiumDriver<MobileElement> driver,WebElement element){
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOf(element));
		System.out.println((Object) element.getTagName()+" displayed");
	}

	/**
	 * return true if the parentPanel is present and false if it's not
	 * @return
	 */
	public Boolean isPresentTryAndCatch(MobileElement mobileElement){
		try {
			mobileElement.isEnabled();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void ExplicitWaitToBeVisible(AndroidDriver<MobileElement> driver, MobileElement element, Integer timeToWait){
		(new WebDriverWait(driver, timeToWait)).until(ExpectedConditions.visibilityOf(element));
		System.out.println((Object) element.getTagName()+" displayed");
	}
	
	/**
	 * Wait @param maxSecondsToWait for the @param id to appear or not, using try and catch.
	 * @param idOrXpath
	 * @param displayed : put false if you want the object to disapear or true if you want the object to appear
	 * @param maxSecondsToWait
	 * @throws InterruptedException 
	 */
	public Boolean waitUntilDisplayed(AppiumDriver<MobileElement> driver,String idOrXpath, Boolean displayed,int maxSecondsToWait) throws InterruptedException {
		long start = System.currentTimeMillis();
		Boolean isDisplayed = false;
		Boolean isXpath=false;
		String methodUsed="Method used : byId";
		if(driver.getCapabilities().getPlatform().name().equals("MAC")){
			methodUsed="Method used : by AccessibilityId";
		}
		if(idOrXpath.contains("//")){
			isXpath=true;
			methodUsed="Method used : byXpath";
		}
		String verb;
		if(displayed){
			verb="appear";
		}else{
			verb="disappear";
		}
		do {
			try {
				if(isXpath){
					driver.findElementByXPath(idOrXpath);
				}else{
					if(driver.getCapabilities().getPlatform().name().equals("MAC")){
						driver.findElementByAccessibilityId((idOrXpath));
					}else{
						driver.findElement(By.id(idOrXpath));
					}
				}
				isDisplayed=true;
			} catch (Exception e) {
				isDisplayed=false;
			}
		} while (displayed!=isDisplayed && (System.currentTimeMillis()-start)/1000F<maxSecondsToWait);
		System.out.println("Time to wait "+idOrXpath+" to "+verb+": "+(System.currentTimeMillis()-start)/1000F+" seconds. "+methodUsed+ ". Device "+driver.getCapabilities().getCapability("deviceName"));
		return isDisplayed;
	}

	/**
	 * Wait until @param propertyValue of @param propertyName is @param set.
	 * @param driver
	 * @param element
	 * @param propertyName
	 * @param propertyValue
	 * @param set
	 * @return 
	 * @throws InterruptedException 
	 */
	public boolean waitUntilPropertyIsSet(MobileElement element, String propertyName, String propertyValue, Boolean set,int maxSecondsToWait) throws InterruptedException{
		float secondsWaited=0;
		String msgFound="have";
		while ((element.getAttribute(propertyName).equals(propertyValue))!=set && secondsWaited<maxSecondsToWait) {
			if(maxSecondsToWait!=0){Thread.sleep(500);secondsWaited=(float) (secondsWaited+0.5);}
		}
		Boolean found=(element.getAttribute(propertyName).equals(propertyValue));
		if (!found)msgFound="don't have";
		System.out.println("Seconds to wait element property '"+propertyName+ "' to "+msgFound+" value "+propertyValue+": "+secondsWaited);
		return found;
	}

	/**
	 * Check if connection is NONE and swith to WIFI in that case.
	 */
	public void forceWifiOnIfNeeded(AndroidDriver<MobileElement> driver){
		if(driver.getConnection().equals(Connection.NONE)){
			System.out.println("Internet is NONE, switching to WIFI on device "+driver.getCapabilities().getCapability("deviceName"));
			driver.setConnection(Connection.WIFI);
		}
	}
	/**
	 * Check if connection is NONE and swith to WIFI in that case.
	 */
	public void forceWifiOfIfNeeded(AndroidDriver<MobileElement> driver){
		if(driver.getConnection().equals(Connection.WIFI)){
			System.out.println("Internet is WIFI, switching to NONE on device "+driver.getCapabilities().getCapability("deviceName"));
			driver.setConnection(Connection.NONE);
		}
	}

	public void captureImage(String imgUrl,MobileElement element) throws IOException{
		new File(imgUrl).delete();
		File screen = ((TakesScreenshot) appiumFactory.getAndroidDriver2())
				.getScreenshotAs(OutputType.FILE);
		Point point = element.getLocation();

		//get element dimension
		int width = element.getSize().getWidth();
		int height = element.getSize().getHeight();

		BufferedImage img = ImageIO.read(screen);
		BufferedImage dest = img.getSubimage(point.getX(), point.getY(), width,
				height);
		ImageIO.write(dest, "png", screen);
		File file = new File(imgUrl);
		FileUtils.copyFile(screen, file);
	}

	public Boolean compareImages(String image1, String image2) throws IOException{
		File fileInput = new File(image1);
		File fileOutPut = new File(image2);

		BufferedImage bufileInput = ImageIO.read(fileInput);
		DataBuffer dafileInput = bufileInput.getData().getDataBuffer();
		int sizefileInput = dafileInput.getSize();                     
		BufferedImage bufileOutPut = ImageIO.read(fileOutPut);
		DataBuffer dafileOutPut = bufileOutPut.getData().getDataBuffer();
		int sizefileOutPut = dafileOutPut.getSize();
		Boolean matchFlag = true;
		if(sizefileInput == sizefileOutPut) {                         
			for(int j=0; j<sizefileInput; j++) {
				if(dafileInput.getElem(j) != dafileOutPut.getElem(j)) {
					matchFlag = false;
					break;
				}
			}
		}
		else                            
			matchFlag = false;
		return matchFlag;
	}

	public void scrollToBottom(AppiumDriver<MobileElement> driver){
		Dimension dimensions = driver.manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.9;
		int scrollStart = screenHeightStart.intValue();
		//System.out.println("s="+scrollStart);
		Double screenHeightEnd = dimensions.getHeight() * 0.2;
		int scrollEnd = screenHeightEnd.intValue();
		//appiumFactory.getAndroidDriver1().swipe(0,scrollStart,0,scrollEnd,2000);
		driver.swipe(0,scrollStart,0,scrollEnd,2000);
	}

	public boolean doubleTapElement(MobileElement element, AppiumDriver<MobileElement> driver) {
		int x,y;
		try {
			x = element.getCenter().getX();
			y = element.getCenter().getY();
			driver.tap(1, x, y, 0);
			try{Thread.sleep(0);}catch (Exception e1) {}
			driver.tap(1, x, y, 0);
			try{Thread.sleep(0);}catch (Exception e1) {}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 */
	public void checkIfUserLoggedAndHomeServerSetUpAndroid(AndroidDriver<MobileElement> myDriver, String username, String pwd) throws InterruptedException, FileNotFoundException, YamlException {
		checkIfUserLoggedAndHomeServerSetUpAndroid(myDriver, username, pwd, false);
	}
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @param forceDefaultHs : true to force using default matrix homeserver.
	 */
	public void checkIfUserLoggedAndHomeServerSetUpAndroid(AndroidDriver<MobileElement> myDriver, String username, String pwd, Boolean forceDefaultHs) throws InterruptedException, FileNotFoundException, YamlException {
		String expectedHomeServer;
		if("true".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal")) && forceDefaultHs==false){
			expectedHomeServer=ReadConfigFile.getInstance().getConfMap().get("homeserver");
		}else{
			expectedHomeServer=Constant.DEFAULT_MATRIX_SERVER;
		}
		//If login page is displayed, then logged with the wanted user
		System.out.println("Check if user "+username+" is logged in "+Constant.APPLICATION_NAME);
		if(waitUntilDisplayed(myDriver, "im.vector.alpha:id/main_input_layout", false, 5)){
			System.out.println("User "+username+" isn't logged, login forced on home server: "+expectedHomeServer);
			pom_android.RiotLoginAndRegisterPageObjects loginView = new pom_android.RiotLoginAndRegisterPageObjects(myDriver);
			loginView.logUser(username,null, pwd, forceDefaultHs);
			//If riot home page isn't displayed, restart riot
		}else{			
			if(!waitUntilDisplayed(myDriver, "im.vector.alpha:id/home_toolbar", true, 0)){
				restartApplication(myDriver);
			}
			pom_android.main_tabs.RiotHomePageTabObjects homePage = new pom_android.main_tabs.RiotHomePageTabObjects(myDriver);
			homePage.contextMenuButton.click();
			String actualLoggedUserDisplayName=homePage.userDisplayNameFromLateralMenu.getText();
			String actualLoggedMatrixId=homePage.userMatrixIdFromLateralMenu.getText();
			String hs=actualLoggedMatrixId.substring(actualLoggedMatrixId.indexOf(":")+1, actualLoggedMatrixId.length());
			if(null==actualLoggedUserDisplayName){
				actualLoggedUserDisplayName="";
			}
			if(!actualLoggedUserDisplayName.equals(username) || !actualLoggedMatrixId.contains(expectedHomeServer)){
				if("true".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal"))){
					System.out.println("Wrong actual logged user: "+actualLoggedUserDisplayName+" or wrong homeserver: "+hs+". Let's log with user "+username+" on homeserver: "+expectedHomeServer);
				}else{
					System.out.println("Wrong actual logged user: "+actualLoggedUserDisplayName+". Let's log with user "+username+" on homeserver: "+expectedHomeServer);	
				}
				myDriver.navigate().back();
				homePage.logOutAndLogin(username, pwd, forceDefaultHs);
			}else{
				System.out.println("User "+username+" is logged with expected homeserver: " +expectedHomeServer+". No need to log out and log in.");
				myDriver.navigate().back();
			}
		}
	}
	public void checkIfUserLoggedAndHomeServerSetUpIos(IOSDriver<MobileElement> myDriver, String username, String pwd) throws InterruptedException, FileNotFoundException, YamlException {
		checkIfUserLoggedAndHomeServerSetUpIos(myDriver, username, pwd, false);
	}
	public void checkIfUserLoggedAndHomeServerSetUpIos(IOSDriver<MobileElement> myDriver, String username, String pwd, Boolean forceDefaultHs) throws InterruptedException, FileNotFoundException, YamlException {
		String expectedHomeServer=null;
		if("true".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal"))&& forceDefaultHs==false){
			expectedHomeServer=ReadConfigFile.getInstance().getConfMap().get("homeserver");
		}else{
			expectedHomeServer=Constant.DEFAULT_MATRIX_SERVER;
		}
		//if login page is displayed, then logged with the wanted user
		System.out.println("Check if user "+username+" is logged in "+Constant.APPLICATION_NAME);
		if(waitUntilDisplayed(myDriver, "AuthenticationVCScrollViewContentView", false, 3)){
			System.out.println("User "+username+" isn't logged, login forced on home server: "+expectedHomeServer);
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(myDriver);
			loginPage.logUser(username,null, pwd,forceDefaultHs);
			new RiotHomePageTabObjects(myDriver);
		}else{
			//if riot home page isn't displayed, restart riot
			if(!waitUntilDisplayed(myDriver, "HomeVCView", true, 0)){
				restartApplication(myDriver);
			}
			RiotHomePageTabObjects homePage = new RiotHomePageTabObjects(myDriver);
			RiotSettingsPageObjects settingsPage=homePage.openRiotSettings();
			//check if the wanted user is loged in
			String actualLoggedUserDisplayName=settingsPage.displayNameTextField.getText();
			String actualLoggedMatrixId=settingsPage.configStaticText.getText();
			String hs=actualLoggedMatrixId.substring(actualLoggedMatrixId.indexOf(":")+1, actualLoggedMatrixId.indexOf("Home server")-1);
			if(null==actualLoggedUserDisplayName){
				actualLoggedUserDisplayName="";
			}
			//new
			if(!actualLoggedUserDisplayName.equals(username) || !actualLoggedMatrixId.contains(expectedHomeServer)){
				if("true".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal"))){
					System.out.println("Wrong actual logged user: "+actualLoggedUserDisplayName+" or wrong homeserver: "+hs+". Let's log with user "+username+" on homeserver: "+expectedHomeServer);
				}else{
					System.out.println("Wrong actual logged user: "+actualLoggedUserDisplayName+". Let's log with user "+username+" on homeserver: "+expectedHomeServer);	
				}
				settingsPage.logOutAndLoginFromSettingsView(username, pwd,forceDefaultHs);
			}else{
				System.out.println("User "+username+" is logged with expected homeserver: " +expectedHomeServer+". No need to log out and log in.");
				settingsPage.backMenuButton.click();
			}
		}
	}
	/**
	 * Close then open again the application.
	 * @param myDriver
	 */
	public void restartApplication(AppiumDriver<MobileElement> myDriver) {
		System.out.println("Restart "+Constant.APPLICATION_NAME);
		myDriver.closeApp();
		myDriver.launchApp();
	}
}
