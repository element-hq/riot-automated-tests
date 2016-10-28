package utility;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Connection;

public class testUtilities {
	public static void ExplicitWait(WebElement element){
		(new WebDriverWait(AppiumFactory.getAppiumDriver(), 10)).until(ExpectedConditions.elementToBeClickable(element));
		System.out.println((Object) element.getTagName()+" clickable");
	}
	
	public static void ExplicitWaitToBeVisible(WebElement element){
		(new WebDriverWait(AppiumFactory.getAppiumDriver(), 10)).until(ExpectedConditions.visibilityOf(element));
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
	
	public static void ExplicitWaitToBeVisible(AppiumDriver<MobileElement> driver, MobileElement element, Integer timeToWait){
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
	public Boolean waitUntilDisplayed(String idOrXpath, Boolean displayed,int maxSecondsToWait) throws InterruptedException {
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
					AppiumFactory.getAppiumDriver().findElement(By.xpath(idOrXpath));
				}else{
					AppiumFactory.getAppiumDriver().findElement(By.id(idOrXpath));
				}
				isDisplayed=true;
			} catch (Exception e) {
				isDisplayed=false;
			}
		} while (displayed!=isDisplayed && secondsWaited<maxSecondsToWait);
		System.out.println("Seconds to wait "+idOrXpath+" to "+verb+": "+secondsWaited+". isXpath is "+isXpath.toString());
		return isDisplayed;
	}
	
	
	/**
	 * Check if connection is NONE and swith to WIFI in that case.
	 */
	public void forceWifiOnIfNeeded(){
		if(AppiumFactory.getAppiumDriver().getConnection().equals(Connection.NONE)){
			System.out.println("Internet is NONE, switching to WIFI.");
			AppiumFactory.getAppiumDriver().setConnection(Connection.WIFI);
		}
	}
	/**
	 * Check if connection is NONE and swith to WIFI in that case.
	 */
	public void forceWifiOfIfNeeded(){
		if(AppiumFactory.getAppiumDriver().getConnection().equals(Connection.WIFI)){
			System.out.println("Internet is WIFI, switching to NONE.");
			AppiumFactory.getAppiumDriver().setConnection(Connection.NONE);
		}
	}
}
