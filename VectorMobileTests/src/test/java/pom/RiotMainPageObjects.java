package pom;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.AppiumFactory;
import utility.testUtilities;

public class RiotMainPageObjects extends testUtilities {
	
	public RiotMainPageObjects(AppiumDriver<MobileElement> driver) throws InterruptedException{
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Thread.sleep(2000);
		//ExplicitWait(driver,this.roomsExpandableListView);
		try {
			waitUntilDisplayed("im.vector.alpha:id/fragment_recents_list", true, 5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * ROOMS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/fragment_recents_list")//expandable view containing all the rooms lists (favorites, rooms, low priority, etc).
	public MobileElement roomsExpandableListView;
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/android.widget.LinearLayout")
	public List<WebElement> roomsList;
	
	public MobileElement getRoomByName(String myRommName){
		//return roomsExpandableListView.findElementByName(myRommName);
		return AppiumFactory.getAppiumDriver().findElementByXPath("//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRommName+"']/../../../..");
	}
	
	/**
	 * Send back the badge number of a room by his name.</br>
	 * Return null if no badge.
	 * @param myRommName
	 * @return
	 */
	public Integer getBadgeNumberByRoomName(String myRommName){
		try {
			//MobileElement roomItem=getRoomByName(myRommName);
			//String badgeNumber= roomItem.findElementById("im.vector.alpha:id/roomSummaryAdapter_unread_count").getText();
			String badgeNumber= AppiumFactory.getAppiumDriver().findElement(By.xpath("//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRommName+"']/../android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_unread_count']")).getText();
			return Integer.parseInt(badgeNumber);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Send back the last received message of a room by his name.</br>
	 * Message can be text or event. </br>
	 * Return null if no message found.
	 * @param myRommName
	 * @return
	 */
	public String getReceivedMessageByRoomName(String myRommName){
		try {
			String messageWithUsername =AppiumFactory.getAppiumDriver().findElement(By.xpath("//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRommName+"']/../..//android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_roomMessage']")).getText();
			return messageWithUsername.substring(messageWithUsername.indexOf(":")+2, messageWithUsername.length());
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Wait until badge of the room is incremented.
	 * @param myRommName
	 * @param currentBadge
	 * @throws InterruptedException
	 */
	public void waitForRoomToReceiveNewMessage(String myRommName, int currentBadge) throws InterruptedException{
		waitUntilDisplayed("//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRommName+"']/../android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_unread_count' and @text='"+Integer.sum(currentBadge, 1)+"']", true, 5);
	}
	
	/**
	 * TOP MENU
	 */
	@AndroidFindBy(xpath="//android.widget.ImageButton")//Menu button (opens the lateral menu)
	public MobileElement contextMenuButton;
	
	@AndroidFindBy(id="im.vector.alpha:id/ic_action_search_room") //Open the search for rooms page
	public MobileElement searchButton;
	
	/**
	 * LATERAL MENU
	 */
	//Logout button
	
	@AndroidFindBy(id="im.vector.alpha:id/navigation_view")
	public MobileElement lateralMenuLayout;
	@AndroidFindBy(xpath="//android.widget.CheckedTextView[@text='Logout']")
	public MobileElement logOutButton;
	@AndroidFindBy(id="im.vector.alpha:id/home_menu_main_displayname")
	public MobileElement displayedUserMain;
	
	@AndroidFindBy(xpath="//android.widget.FrameLayout//android.widget.CheckedTextView[@text='Copyright']")//copyright button
	public MobileElement openCopyrightButton;
	
	/**
	 * Get an item from the lateral menu.
	 * @param name
	 * @return
	 */
	public MobileElement getItemMenuByName(String name){
		return lateralMenuLayout.findElementByName(name);
	}
	
	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOut(){
		this.contextMenuButton.click();
		this.logOutButton.click();
	}
	

	

}
