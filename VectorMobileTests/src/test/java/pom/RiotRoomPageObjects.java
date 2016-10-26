package pom;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.AppiumFactory;
import utility.testUtilities;

public class RiotRoomPageObjects extends testUtilities{
	public RiotRoomPageObjects(AppiumDriver<MobileElement> driver){
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		//ExplicitWait(driver,this.messagesListView);
		try {
			waitUntilDisplayed("im.vector.alpha:id/listView_messages", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ACTION BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_header")//collapsed view. contains avatar, room name, and active members
	public MobileElement actionBarCollapsedLayout;
	@AndroidFindBy(id="im.vector.alpha:id/avatar_img")
	public MobileElement avatarImageView;
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_header_room_title")
	public MobileElement roomNameTextView;
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_header_room_members")
	public MobileElement activeMembersTextView;
	
	@AndroidFindBy(id="im.vector.alpha:id/room_toolbar")//action bar : contains back, search, collapse and more options buttons.
	public MobileElement actionBarView;
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement menuBackButton;
	@AndroidFindBy(id="im.vector.alpha:id/open_chat_header_arrow")// ^ button
	public MobileElement collapseChatButton;
	@AndroidFindBy(id="im.vector.alpha:id/ic_action_search_in_room")
	public MobileElement searchInRoomButton;
	@AndroidFindBy(xpath="//android.widget.TextView[@resource-id='im.vector.alpha:id/ic_action_search_in_room']/../android.widget.ImageView")
	public MobileElement moreOptionsButton;
	/**
	 * PREVIEW LAYOUT
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_preview_info_layout")//preview info layout : contains join room and cancel buttons
	public MobileElement previewRoomLayout;
	@AndroidFindBy(id="im.vector.alpha:id/room_preview_invitation_textview")//"You have been invited to join this room by USER"
	public MobileElement invitationMessageTextView;
	@AndroidFindBy(id="im.vector.alpha:id/button_join_room")
	public MobileElement joinRoomButton;
	@AndroidFindBy(id="im.vector.alpha:id/button_decline")
	public MobileElement cancelInvitationButton;
	
	/**
	 * Check the room preview layout after accepting an invitation.
	 * @throws InterruptedException 
	 */
	public void checkPreviewRoomLayout(String roomName) throws InterruptedException{
		ExplicitWait(previewRoomLayout);
		//check invitation messsage
		Assert.assertTrue(invitationMessageTextView.getText().contains("You have been invited to join this room by "));
		//check buttons
		Assert.assertEquals(joinRoomButton.getText(), "Join Room");
		Assert.assertEquals(cancelInvitationButton.getText(), "Cancel");
		//check more options button is not here
		Assert.assertFalse(waitUntilDisplayed("//android.widget.TextView[@resource-id='im.vector.alpha:id/ic_action_search_in_room']/../android.widget.ImageView", false, 0));
		//check search button is not here
		Assert.assertFalse(waitUntilDisplayed("im.vector.alpha:id/ic_action_search_in_room", false, 0));
		//check back button is displayed
		Assert.assertTrue(menuBackButton.isDisplayed(),"Back button isn't displayed");
		//check collapse button is displayed
		Assert.assertTrue(collapseChatButton.isDisplayed(),"Collapse button isn't displayed");
		//room name is displayed
		Assert.assertEquals(roomNameTextView.getText(), roomName);
		//avatar isn't empty
		org.openqa.selenium.Dimension roomAvatar=avatarImageView.getSize();
		Assert.assertTrue(roomAvatar.height!=0 && roomAvatar.width!=0, "Riot logo has null dimension");
		//check that bottom bar isn't here
		//send bar
		Assert.assertFalse(waitUntilDisplayed("//android.widget.RelativeLayout[@resource-id='im.vector.alpha:id/room_bottom_layout']//android.widget.EditText[@resource-id='im.vector.alpha:id/editText_messageBox']", false, 0));
		Assert.assertFalse(waitUntilDisplayed("im.vector.alpha:id/room_send_layout", false, 0));
		Assert.assertFalse(waitUntilDisplayed("im.vector.alpha:id/room_start_call_layout", false, 0));
		Assert.assertFalse(waitUntilDisplayed("im.vector.alpha:id/room_end_call_layout", false, 0));
	}
	
	/**
	 * Check the room page layout.
	 * TODO complete this function by inspirant of checkPreviewRoomLayout() 
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void checkRoomLayout(String roomName) throws InterruptedException {
		//check that preview layout isn't displayed
		Assert.assertFalse(waitUntilDisplayed("im.vector.alpha:id/room_preview_info_layout", false, 0));
		//check action bar
		Assert.assertTrue(actionBarView.isDisplayed(), "Action bar isn't displayed");
		Assert.assertTrue(menuBackButton.isDisplayed(), "Menu back button isn't displayed");
		Assert.assertTrue(collapseChatButton.isDisplayed(), "Collapse chat button isn't displayed");
		Assert.assertTrue(searchInRoomButton.isDisplayed(), "Search in room button isn't displayed");
		Assert.assertTrue(moreOptionsButton.isDisplayed(), "More option button isn't displayed");
		//check that bottom bar is displayed
		Assert.assertTrue(messageZoneEditText.isDisplayed(), "Message zone edit text isn't displayed");
		Assert.assertTrue(sendMessageButton.isDisplayed(), "Send message button isn't displayed");
		Assert.assertTrue(startCallButton.isDisplayed(), "Start call button isn't displayed");
	}
	
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
	
	/**
	 * Get the textView from a linearLayout object message (first children of the listView_messages).
	 * @param message
	 * @return
	 */
	public MobileElement getTextViewFromMessage(MobileElement message){
		return message.findElement(By.id("im.vector.alpha:id/messagesAdapter_body"));
	}
	
	/**
	 * Get the imageview attached from a linearLayout object message (first children of the listView_messages).
	 * @param message
	 * @return
	 */
	public MobileElement getImageFromMessage(MobileElement message){
		return message.findElement(By.id("im.vector.alpha:id/messagesAdapter_image"));
	}
	
	/**
	 * Get the media upload failed icon on an attached image from a linearLayout object message (first children of the listView_messages).
	 * @param message
	 * @return
	 */
	public MobileElement getMediaUploadFailIconFromMessage(MobileElement message){
		try {
			return message.findElement(By.id("im.vector.alpha:id/media_upload_failed"));
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	/**
	 * NOTIFICATION AREA
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_notifications_area")
	public MobileElement roomNotificationArea;
	@AndroidFindBy(id="im.vector.alpha:id/room_notification_icon")
	public MobileElement notificationIcon;
	@AndroidFindBy(id="im.vector.alpha:id/room_notification_message")
	public MobileElement notificationMessage;
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
	 * POPING MENU: CALL MENU (voice call, video call), or ATTACHMENT MENU (send files, take photo)
	 */
	@AndroidFindBy(id="im.vector.alpha:id/listView_icon_and_text")//call menu with voice and videos buttons
	public MobileElement callMenuList;
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Voice Call']/..")//call menu with voice and videos buttons
	public MobileElement voiceCallFromMenuButton;
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Video Call']/..")//video menu with voice and videos buttons
	public MobileElement videoCallFromMenuButton;
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Send files']/..")//send files menu with send files and take photo buttons
	public MobileElement sendFilesFromMenuButton;
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Take photo']/..")//take photo menu with send files and take photo buttons
	public MobileElement takePhotoFromMenuButton;
	/**
	 * Functions
	 */
	
	/**
	 * Type and send a message.
	 * @param message
	 */
	public void sendAMessage(String message){
		messageZoneEditText.setValue(message);
		sendMessageButton.click();
		System.out.println("Message "+message+" sent in the room.");
	}
	/**
	 * From a room, take a photo and attach it to the messages.
	 * @param photoSize : choose between Original, Large, Medium, Small.
	 * @throws InterruptedException 
	 */
	public void attachPhotoFromCamera(String photoSize) throws InterruptedException{
		sendMessageButton.click(); //(this button id is for both attachment files and send message buttons)
		takePhotoFromMenuButton.click();
		RiotCameraPageObjects cameraPreview = new RiotCameraPageObjects(AppiumFactory.getAppiumDriver());
		cameraPreview.triggerCameraButton.click();//take a photo
		waitUntilDisplayed("im.vector.alpha:id/medias_picker_preview", true, 5);
		cameraPreview.confirmPickingPictureButton.click();
		ExplicitWaitToBeVisible(cameraPreview.sendAsMenuLayout);
		cameraPreview.getItemFromSendAsMenu(photoSize).click();
	}
}
