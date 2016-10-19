package pom;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.testUtilities;

public class RiotSearchFromRoomPageObjects extends testUtilities{
	
	public RiotSearchFromRoomPageObjects(AppiumDriver<MobileElement> driver){
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		ExplicitWait(driver,this.mainView);
	}
	
	@AndroidFindBy(id="im.vector.alpha:id/decor_content_parent")//main view from the search page
	public MobileElement mainView;
	
	/**
	 * ACTION BAR.
	 */
	@iOSFindBy()
	@AndroidFindBy(id="im.vector.alpha:id/room_action_bar_edit_text")//search edit text in the top
	public MobileElement searchEditText;
	
	/**
	 * ROOMS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/fragment_recents_list")//expandable view containing all the rooms lists (favorites, rooms, low priority, etc).
	public MobileElement roomsExpandableListView;
	
	public String roomName = "";
//	@AndroidFindBy(uiAutomator="new UiSelector().resourceId(\"android:id/text1\")"+org.openqa.selenium.support.How.valueOf(roomName)+"']")
//	public MobileElement roomNameTextView;
	
	public MobileElement getRoomByName(String myRommName){
	//	roomName="qsdqs";
		//search edit text in the top
 //MobileElement room;
		return roomsExpandableListView.findElementByName(myRommName);
		//return roomsExpandableListView.findElement(By.xpath("/android.widget.LinearLayout//android.widget.TextView[@text='"+myRommName+"']"));
		//return roomsExpandableListView.findElementByXPath("/android.widget.LinearLayout//android.widget.TextView[@text='"+myRommName+"']");
		//return (MobileElement) org.openqa.selenium.support.How.valueOf(myRommName)  .driver.findElementByXPath("//android.widget.TextView[@text='"+myRommName+"']");
	}
}
