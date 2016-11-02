package pom;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.testUtilities;

public class RiotLegalStuffView extends testUtilities{
	AppiumDriver<MobileElement> driver;
	public RiotLegalStuffView(AppiumDriver<MobileElement> driver2){
		PageFactory.initElements(new AppiumFieldDecorator(driver2), this);
		super.actualDriver=(AndroidDriver<MobileElement>) driver;
		ExplicitWait(this.parentPanel);
		driver=driver2;
	}
	
	@AndroidFindBy(id="android:id/parentPanel")//the main frame
	public MobileElement parentPanel;
	@AndroidFindBy(id="android:id/button1")//the main frame
	public MobileElement okButton;
	@AndroidFindBy(xpath="//android.view.View/android.view.View[@index='1']")//the main frame
	public MobileElement secondTitle;
	
	/**
	 * return true if the parentPanel is present and false if it's not
	 * @return
	 */
	public Boolean isPresent(){
		 Boolean notPresent = ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(By.id("android:id/parentPanel"))).apply(driver);
		 return notPresent;
	}
	
	/**
	 * return true if the parentPanel is present and false if it's not
	 * @return
	 */
	public Boolean isPresentTryAndCatch(){
		try {
			parentPanel.isEnabled();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
}
