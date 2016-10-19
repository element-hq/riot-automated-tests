package utility;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.Connection;

public class testUtilities {
	public static void ExplicitWait(AppiumDriver<MobileElement> driver, WebElement element){
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.elementToBeClickable(element));
		System.out.println((Object) element.getTagName()+" clickable");
	}
	
	public static void ExplicitWaitToBeVisible(AppiumDriver<MobileElement> driver, WebElement element){
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
	
	public static void ExplicitWaitToBeVisible(AppiumDriver<MobileElement> driver, MobileElement element, Integer timeToWait){
		(new WebDriverWait(driver, timeToWait)).until(ExpectedConditions.visibilityOf(element));
		System.out.println((Object) element.getTagName()+" displayed");
	}
	
	/**
	 * Wait @param maxSecondsToWait for the @param id to appear or not, using try and catch.
	 * @param id
	 * @param displayed : put false if you want the object to disapear or true if you want the object to appear
	 * @param maxSecondsToWait
	 * @throws InterruptedException 
	 */
	public Boolean waitUntilDisplayed(String id, Boolean displayed,int maxSecondsToWait) throws InterruptedException {
		Boolean isDisplayed = false;
		float secondsWaited=0;
		do {
			try {
				AppiumFactory.getAppiumDriver().findElement(By.id(id));
				isDisplayed=true;
			} catch (Exception e) {
				isDisplayed=false;
			}
			Thread.sleep(500);
			secondsWaited=(float) (secondsWaited+0.5);
		} while (displayed!=isDisplayed && secondsWaited<maxSecondsToWait);
		System.out.println("Seconds to wait "+id+" to disapear: "+secondsWaited);
		return isDisplayed;
	}
	
	/**
	 * Check if connection is NONE and swith to WIFI in that case.
	 */
	public void forceWifiIsNeeded(){
		if(AppiumFactory.getAppiumDriver().getConnection().equals(Connection.NONE)){
			System.out.println("Internet is NONE, switching to WIFI.");
			AppiumFactory.getAppiumDriver().setConnection(Connection.WIFI);
		}
	}
	
//	/**
//	 * Wait @param maxSecondsToWait for the @param id to appear, using try and catch.
//	 * @param id
//	 * @param maxSecondsToWait
//	 * @throws InterruptedException 
//	 */
//	public void waitUntilDisplayed(String id, int maxSecondsToWait) throws InterruptedException{
//		Boolean isDisplayed = false;
//		float secondsWaited=0;
//		do {
//			try {
//				MobileElement mobileElement= AppiumFactory.getAppiumDriver().findElement(By.id(id));
//				//ExplicitWaitToBeVisible(AppiumFactory.getAppiumDriver(), AppiumFactory.getAppiumDriver().findElement(By.id("im.vector.alpha:id/main_input_layout")));
//				isDisplayed=true;
//			} catch (Exception e) {
//				// TODO: handle exception
//				isDisplayed=false;
//			}
//			Thread.sleep(500);
//			secondsWaited=(float) (secondsWaited+0.5);
//		} while (true==isDisplayed && secondsWaited<maxSecondsToWait);
//		System.out.println("Seconds to wait "+id+" to disapear: "+secondsWaited);
//	}
}
