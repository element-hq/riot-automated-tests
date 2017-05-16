package pom_android.main_tabs;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import pom_android.RiotContactPickerPageObjects;
import pom_android.RiotNewChatPageObjects;
import pom_ios.RiotRoomPageObjects;

public class RiotPeopleTabPageObjects extends RiotTabPageObjects{

	public RiotPeopleTabPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException {
		super(myDriver);
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		//if 'Riot permissions .... Allow Riot to access your contacts' pops up, close it.
		if(waitUntilDisplayed(driver, "//android.widget.TextView[@resource-id='android:id/alertTitle' and @text='Information']", true, 1)){
			driver.findElementById("android:id/button1").click();
		}
		Assert.assertTrue(waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/fragment_container", true, 5),"PeoplePage tab isn't open.");
	}

	/*
	 * ROOMS AND PEOPLE LIST.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/contact_view")
	public List<WebElement> peopleList;
	
	/**
	 * Return a people item from the people list, as a MobileElement.
	 * @param peopleDisplayName
	 * @return
	 */
	public MobileElement getPeopleByName(String peopleDisplayName){
		try {
			return driver.findElementByXPath("//android.support.v7.widget.RecyclerView/android.widget.RelativeLayout//android.widget.TextView[@resourceid='im.vector.alpha:id/contact_name' and @text='"+peopleDisplayName+"']/../..");	
		} catch (Exception e) {
			System.out.println("No people found with display name: "+peopleDisplayName+ " in people tab.");
			return null;
		}
	}
	
	/*
	 * FLOATING BUTTONS OR DIALOGS .
	 */
	/**
	 * Start a new chat with a user and returns the new created room. 
	 * @param displayNameOrMatrixId
	 * @return RiotRoomPageObjects
	 * @throws InterruptedException 
	 */
	public RiotRoomPageObjects startChat(String displayNameOrMatrixId) throws InterruptedException{
		createRoomFloatingButton.click();
		RiotContactPickerPageObjects inviteViewDevice1=new RiotContactPickerPageObjects(driver);
		inviteViewDevice1.searchAndSelectMember(getMatrixIdFromDisplayName(displayNameOrMatrixId));
		RiotNewChatPageObjects newChatViewDevice1= new RiotNewChatPageObjects(driver);
		newChatViewDevice1.confirmRoomCreationButton.click();
		return new RiotRoomPageObjects(driver);
	}
}
