package pom_android;

import java.util.List;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

/**
 * Page opened after hitting the search button from the rooms list.
 */
public class RiotSearchFromRoomsListPageObjects extends TestUtilities{
private AndroidDriver<MobileElement> driver;
	
	public RiotSearchFromRoomsListPageObjects(AppiumDriver<MobileElement> myDriver){
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		ExplicitWait(driver,this.mainView);
	}
	
	@AndroidFindBy(id="im.vector.alpha:id/decor_content_parent")//main view from the search page
	public MobileElement mainView;
	
	/*
	 * ACTION BAR.
	 */
	/**
	 * Action bar containing back button, search edittext, and mic button.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/action_bar")//search edit text in the top
	public MobileElement actionBarView;
	@AndroidFindBy(xpath="//android.view.View[@resource-id='im.vector.alpha:id/action_bar']/android.widget.ImageButton[@content-desc='Navigate up']")//search edit text in the top
	public MobileElement menuBackButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_action_bar_edit_text")
	public MobileElement searchEditText;
	@AndroidFindBy(id="im.vector.alpha:id/ic_action_speak_to_search")
	public MobileElement microphoneTextView;
	
	/*
	 * TABS
	 */
	@AndroidFindBy(xpath="//android.widget.TextView[@text='ROOMS']/../*")
	public MobileElement roomsTab;
	@AndroidFindBy(xpath="//android.widget.TextView[@text='MESSAGES']/../*")
	public MobileElement messagesTab;
	@AndroidFindBy(xpath="//android.widget.TextView[@text='PEOPLE']/../*")
	public MobileElement peopleTab;
	@AndroidFindBy(xpath="//android.widget.TextView[@text='FILES']/../*")
	public MobileElement filesTab;
	
	/**
	 * Hit ROOMS tab, then launch a search with @param  searchedText.
	 * @param searchedText
	 * @throws InterruptedException 
	 */
	public void launchASearch(String searchedText, Boolean waitSearchFinished) throws InterruptedException{
		searchEditText.clear();
		searchEditText.setValue(searchedText);
		driver.pressKeyCode(AndroidKeyCode.KEYCODE_ENTER);
		if(waitSearchFinished)	waitUntilSearchFinished();
		driver.hideKeyboard();
	}
	
	public Boolean waitUntilSearchFinished() throws InterruptedException {
		return waitUntilDisplayed(driver, "im.vector.alpha:id/search_in_progress_view", false, 10);
	}

	/*
	 * RESULTS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/search_in_progress_view")
	public List<MobileElement> searchProgressView;
	@AndroidFindBy(id="im.vector.alpha:id/search_no_result_textview")
	public MobileElement noResultTextView;
	
	/**
	 * Gets all the children (relative layout and linearlayout of the expandablelistview). For the ROOMS results it's categories and rooms.
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/*")
	public List<MobileElement> allResultsLayouts;
	
	/*
	 * ROOMS RESULTS
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/android.widget.LinearLayout/android.widget.TextView[@text='Browse directory']/../")
	public MobileElement browseDirectoryItemLayout;
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/android.widget.LinearLayout")
	public List<MobileElement> roomswithBrowseDirectoryLayouts;
	
	/**
	 * Check a room item from the ROOMS results.</br>
	 * If roomName, or lastMsg are null, assertions won't be made.
	 * @param index
	 */
	public void checkRoomItemFromResult(int index, String roomName, String lastMsg) {
		if(roomName !=null){
			Assert.assertEquals(roomswithBrowseDirectoryLayouts.get(index).findElementById("im.vector.alpha:id/roomSummaryAdapter_roomName").getText(), roomName);	
		}
		if (lastMsg!=null){
			String userAndMsg=roomswithBrowseDirectoryLayouts.get(index).findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage").getText();
			String user=userAndMsg.substring(0, userAndMsg.indexOf(":"));
			String msg=userAndMsg.substring(userAndMsg.indexOf(":")+2,userAndMsg.length());
			Assert.assertTrue(user.length()>1, "No user name in the last message of the searched room at index "+index);
			Assert.assertEquals(msg, lastMsg);	
		}
	}
	
	public MobileElement getBrowseDirectory(){
		return roomswithBrowseDirectoryLayouts.get(0);
	}
	
	/**
	 * Return only the rooms item (don't return the browse directory item).
	 * @return
	 * @throws InterruptedException 
	 */
	public List<MobileElement> getRoomsLayout() throws InterruptedException{
		waitUntilDisplayed(driver, "//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/android.widget.RelativeLayout[@index=2]", true, 5);
		return roomswithBrowseDirectoryLayouts.subList(0, roomswithBrowseDirectoryLayouts.size()-1);
	}
	/*
	 * MESSAGES RESULTS
	 */
	@AndroidFindBy(xpath="//android.widget.ListView/android.widget.LinearLayout")
	public List<MobileElement> listMessagesLinearLayouts;
	
	/**
	 * Check a message item from the MESSAGES results.
	 */
	public void checkMessageItemFromResult(int index, String roomName, String userName, String message){
		MobileElement messageToCheck=listMessagesLinearLayouts.get(index);
		if(roomName !=null)Assert.assertEquals(messageToCheck.findElementById("im.vector.alpha:id/messagesAdapter_message_room_name_textview").getText(), roomName);
		String authorName=messageToCheck.findElementById("im.vector.alpha:id/messagesAdapter_sender").getText();
		if(userName !=null){
			Assert.assertEquals(authorName, userName);
		}else{
			Assert.assertTrue(authorName.length()>1,"Author of the searched messages at index "+index+" doesn't seem to be there");
		}
		if(message!=null)Assert.assertEquals(messageToCheck.findElementById("im.vector.alpha:id/messagesAdapter_body").getText(), message);
		Assert.assertFalse(messageToCheck.findElementById("im.vector.alpha:id/messagesAdapter_timestamp").getText().isEmpty(), "Timestamp at message at index "+index+" doesn't seem to be there");
		org.openqa.selenium.Dimension roomAvatar=messageToCheck.findElementById("im.vector.alpha:id/avatar_img").getSize();
		Assert.assertTrue(roomAvatar.height!=0 && roomAvatar.width!=0, "Avatar of the searched messages at index "+index+"  has null dimension");
	}
	
	



	
	
	
}
