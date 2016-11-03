package utility;

import org.openqa.selenium.By;
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
}
