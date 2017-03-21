package utility;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
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

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.Connection;
import io.appium.java_client.ios.IOSDriver;
import pom_ios.RiotLoginAndRegisterPageObjects;
import pom_ios.RiotRoomsListPageObjects;

public class TestUtilities {
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
		float secondsWaited=0;
		do {
			if(maxSecondsToWait!=0){Thread.sleep(500);secondsWaited=(float) (secondsWaited+0.5);}
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
		} while (displayed!=isDisplayed && secondsWaited<maxSecondsToWait);
		System.out.println("Seconds to wait "+idOrXpath+" to "+verb+": "+secondsWaited+". "+methodUsed+ ". Device "+driver.getCapabilities().getCapability("deviceName"));
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
	 * @throws InterruptedException
	 */
	public void checkIfUserLoggedAndroid(AndroidDriver<MobileElement> myDriver, String username, String pwd) throws InterruptedException {
		//if login page is displayed, then logged with the wanted user
		System.out.println("Check if user "+username+" is logged in "+Constant.APPLICATION_NAME);
		if(waitUntilDisplayed(myDriver, "im.vector.alpha:id/main_input_layout", false, 5)){
			System.out.println("User "+username+" isn't logged, login forced.");
			pom_android.RiotLoginAndRegisterPageObjects loginView = new pom_android.RiotLoginAndRegisterPageObjects(myDriver);
			loginView.fillLoginForm(username,null, pwd);
		}else{
			//check if the wanted user is loged in
			pom_android.RiotRoomsListPageObjects listRoom = new pom_android.RiotRoomsListPageObjects(myDriver);
			listRoom.contextMenuButton.click();
			String actualLoggedUser=listRoom.displayedUserMain.getText();
			if(null==actualLoggedUser){
				actualLoggedUser="";
			}
			if(!actualLoggedUser.equals(username)){
				System.out.println("User "+username+" isn't logged. An other user is logged ("+actualLoggedUser+"), login with "+username+".");
				myDriver.navigate().back();
				listRoom.logOutAndLogin(username, pwd);
			}else{
				//close lateral menu
				System.out.println("User "+username+" is logged.");
				myDriver.navigate().back();
			}
		}
	}
	public void checkIfUserLoggedIos(IOSDriver<MobileElement> myDriver, String username, String pwd) throws InterruptedException {
		//if login page is displayed, then logged with the wanted user
		System.out.println("Check if user "+username+" is logged in "+Constant.APPLICATION_NAME);
		if(waitUntilDisplayed(myDriver, "AuthenticationVCScrollViewContentView", false, 5)){
			System.out.println("User "+username+" isn't logged, login forced.");
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(myDriver);
			loginPage.fillLoginForm(username,null, pwd);
			new RiotRoomsListPageObjects(myDriver);
		}else{
			//check if the wanted user is loged in
			RiotRoomsListPageObjects listRoom = new RiotRoomsListPageObjects(myDriver);
			listRoom.settingsButton.click();
			String actualLoggedUser=listRoom.displayNameTextField.getText();
			if(null==actualLoggedUser){
				actualLoggedUser="";
			}
			if(!actualLoggedUser.equals(username)){
				System.out.println("User "+username+" isn't logged. An other user is logged ("+actualLoggedUser+"), let's log in with "+username+".");
				listRoom.logOutAndLoginFromSettingsView(username, pwd);
			}else{
				//close lateral menu
				System.out.println("User "+username+" is logged.");
				listRoom.backMenuButton.click();
			}
		}
	}
}
