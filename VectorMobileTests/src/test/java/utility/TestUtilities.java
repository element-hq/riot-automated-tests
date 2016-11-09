package utility;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.Connection;

public class TestUtilities {
	
	public void ExplicitWait(AndroidDriver<MobileElement> driver, WebElement element){
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(element));
		System.out.println((Object) element.getTagName()+" clickable");
	}
	public void ExplicitWaitToBeVisible(AndroidDriver<MobileElement> driver,WebElement element){
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
	public Boolean waitUntilDisplayed(AndroidDriver<MobileElement> driver,String idOrXpath, Boolean displayed,int maxSecondsToWait) throws InterruptedException {
		Boolean isDisplayed = false;
		Boolean isXpath=false;
		if(idOrXpath.contains("//")){
			isXpath=true;
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
					driver.findElement(By.xpath(idOrXpath));
				}else{
					driver.findElement(By.id(idOrXpath));
				}
				isDisplayed=true;
			} catch (Exception e) {
				isDisplayed=false;
			}
		} while (displayed!=isDisplayed && secondsWaited<maxSecondsToWait);
		System.out.println("Seconds to wait "+idOrXpath+" to "+verb+": "+secondsWaited+". isXpath is "+isXpath.toString()+ " with device "+driver.getCapabilities().getCapability("deviceName"));
		return isDisplayed;
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
		File screen = ((TakesScreenshot) AppiumFactory.getAppiumDriver2())
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
}
