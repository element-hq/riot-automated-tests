package pom;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.testUtilities;

public class RiotMainPageObjects extends testUtilities {
	
	public RiotMainPageObjects(AppiumDriver<MobileElement> driver) throws InterruptedException{
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		Thread.sleep(2000);
		ExplicitWait(driver,this.roomsExpandableListView);
	}
	
	/**
	 * ROOMS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/fragment_recents_list")//expandable view containing all the rooms lists (favorites, rooms, low priority, etc).
	public MobileElement roomsExpandableListView;
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/android.widget.LinearLayout")
	public List<WebElement> roomsList;
	
	public MobileElement getRoomByName(String myRommName){
		return roomsExpandableListView.findElementByName(myRommName);
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
