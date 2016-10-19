package pom;

import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.testUtilities;

public class RiotRoomPageObjects extends testUtilities{
	public RiotRoomPageObjects(AppiumDriver<MobileElement> driver){
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		ExplicitWait(driver,this.messagesListView);
	}
	
	/**
	 * ACTION BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_toolbar : view")//action bar in the top
	public MobileElement actionBarView;
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")//action bar in the top
	//@AndroidFindBy(xpath="//android.view.View[@resource-id='im.vector.alpha:id/action_bar']//android.widget.ImageButton[@index='0']")
	public MobileElement menuBackButton;
	
	
	/**
	 * MESSAGES
	 */
	@AndroidFindBy(id="im.vector.alpha:id/listView_messages")//messages list. Contains messages, days separators
	public MobileElement messagesListView;
	
	@AndroidFindBy(xpath="//android.widget.ListView[@resource-id='im.vector.alpha:id/listView_messages']/android.widget.LinearLayout[last()]")//the last message
	public MobileElement lastMessage;
	
	public MobileElement getContextMenuByMessage(MobileElement messageLinearLayout){
		return messageLinearLayout.findElementById("im.vector.alpha:id/messagesAdapter_action_image");
	}
		//MobileElement lastMessage=(MobileElement) getDriver().findElementByXPath("//android.widget.ListView[@resource-id='im.vector.alpha:id/listView_messages']/android.widget.LinearLayout[last()]");
	
	/**
	 * BOTTOM BAR : send edittext, attachment button, callbutton
	 */
	@AndroidFindBy(xpath="//android.widget.RelativeLayout[@resource-id='im.vector.alpha:id/room_bottom_layout']//android.widget.EditText[@resource-id='im.vector.alpha:id/editText_messageBox']")
	public MobileElement messageZoneEditText;
	@AndroidFindBy(id="im.vector.alpha:id/room_send_layout")
	public MobileElement sendMessageButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_start_call_layout")//start a call button
	public MobileElement startCallButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_end_call_layout")//end a call button
	public MobileElement endCallButton;
	
	/**
	 * CONTEXT MENU ON MESSAGE
	 */
	@AndroidFindBy(className="android.widget.ListView")//list items menu
	public MobileElement menuListItems;
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Quote']/../..")//quote item
	public MobileElement quoteItemFromMenu;
	
	/**
	 * CALL MENU
	 */
	@AndroidFindBy(id="im.vector.alpha:id/listView_icon_and_text")//call menu with voice and videos buttons
	public MobileElement callMenuList;
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Voice Call']/..")//call menu with voice and videos buttons
	public MobileElement voiceCallFromMenuButton;
	

	
}
