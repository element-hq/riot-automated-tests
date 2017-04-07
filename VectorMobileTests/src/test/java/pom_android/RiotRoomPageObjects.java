package pom_android;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotRoomPageObjects extends TestUtilities{
	private AndroidDriver<MobileElement> driver;
	public RiotRoomPageObjects(AppiumDriver<MobileElement> myDriver){
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		driver=(AndroidDriver<MobileElement>) myDriver;
		//ExplicitWait(driver,this.messagesListView);
		try {
			waitUntilDisplayed(driver,"im.vector.alpha:id/listView_messages", true, 5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ACTION BAR
	 */
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_header")//collapsed view. contains avatar, room name, topic and active members
	public MobileElement actionBarCollapsedLayout;
	@AndroidFindBy(id="im.vector.alpha:id/avatar_img")
	public MobileElement avatarImageView;
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_header_room_title")
	public MobileElement roomNameTextViewCollapsed;
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_header_room_members")
	public MobileElement activeMembersTextView;
	@AndroidFindBy(id="im.vector.alpha:id/action_bar_header_invite_members")
	public MobileElement inviteMembersButton;

	@AndroidFindBy(id="im.vector.alpha:id/room_toolbar")//action bar : contains back, room name, topic,search, collapse and more options buttons.
	public MobileElement actionBarView;
	@AndroidFindBy(xpath="//android.widget.ImageButton[@content-desc='Navigate up']")
	public MobileElement menuBackButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_action_bar_title")
	public MobileElement roomNameTextView;
	@AndroidFindBy(id="im.vector.alpha:id/room_action_bar_topic")
	public MobileElement roomTopicTextView;
	@AndroidFindBy(id="im.vector.alpha:id/open_chat_header_arrow")// ^ button
	public MobileElement collapseChatButton;
	@AndroidFindBy(id="im.vector.alpha:id/ic_action_search_in_room")
	public MobileElement searchInRoomButton;
	@AndroidFindBy(xpath="//android.view.View[@resource-id='im.vector.alpha:id/room_toolbar']//android.widget.ImageView[@content-desc='More options']")
	public MobileElement moreOptionsButton;

	/*
	 * ROOM NAME DIALOG : opened after hit on room name from collapsed action bar.
	 */
	@AndroidFindBy(id="android:id/parentPanel")
	public MobileElement roomNameDialogLayout;//contains cancel and ok buttons, and room name
	@AndroidFindBy(id="im.vector.alpha:id/dialog_title")
	public MobileElement roomNameFromChangeDialogTextView;
	@AndroidFindBy(id="im.vector.alpha:id/dialog_edit_text")
	public MobileElement roomNameFromChangeDialogEditText;
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='android:id/buttonPanel']//android.widget.Button[@text='OK']")
	public MobileElement okFromChangeRoomNameButton;
	@AndroidFindBy(xpath="//android.widget.LinearLayout[@resource-id='android:id/buttonPanel']//android.widget.Button[@text='Cancel']")
	public MobileElement cancelFromChangeRoomNameButton;

	/**
	 * Change the room name. Action bar must be collapsed first.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void changeRoomName(String roomName) throws InterruptedException {
		waitUntilDisplayed(driver, "im.vector.alpha:id/action_bar_header", true, 10);
		roomNameTextViewCollapsed.click();
		roomNameFromChangeDialogEditText.sendKeys(roomName);
		okFromChangeRoomNameButton.click();
		//ExplicitWait(driver, roomNameTextViewCollapsed);
		waitUntilDisplayed(driver, "im.vector.alpha:id/listView_spinner", false, 4);
		Assert.assertEquals(roomNameTextViewCollapsed.getText(), roomName, "Room name haven't be changed.");
	}

	/*
	 * ROOM MENU
	 */
	@AndroidFindBy(xpath="//android.widget.TextView[@resource-id='im.vector.alpha:id/title' and @text='Room Details']")
	public MobileElement roomDetailsMenuItem;
	@AndroidFindBy(xpath="//android.widget.TextView[@resource-id='im.vector.alpha:id/title' and @text='Leave']")
	public MobileElement leaveRoomMenuItem;

	public void leaveRoom(){
		moreOptionsButton.click();
		leaveRoomMenuItem.click();
		alertDialogButton2.click();
	}

	/*
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
		ExplicitWait(driver,previewRoomLayout);
		//check invitation messsage
		Assert.assertTrue(invitationMessageTextView.getText().contains("You have been invited to join this room by "));
		//check buttons
		Assert.assertEquals(joinRoomButton.getText(), "Join Room");
		Assert.assertEquals(cancelInvitationButton.getText(), "Cancel");
		//check more options button is not here
		Assert.assertFalse(waitUntilDisplayed(driver,"//android.widget.TextView[@resource-id='im.vector.alpha:id/ic_action_search_in_room']/../android.widget.ImageView", false, 0));
		//check search button is not here
		Assert.assertFalse(waitUntilDisplayed(driver,"im.vector.alpha:id/ic_action_search_in_room", false, 0));
		//check back button is displayed
		Assert.assertTrue(menuBackButton.isDisplayed(),"Back button isn't displayed");
		//check collapse button is displayed
		Assert.assertTrue(collapseChatButton.isDisplayed(),"Collapse button isn't displayed");
		//room name is displayed
		Assert.assertEquals(roomNameTextViewCollapsed.getText(), roomName);
		//avatar isn't empty
		org.openqa.selenium.Dimension roomAvatar=avatarImageView.getSize();
		Assert.assertTrue(roomAvatar.height!=0 && roomAvatar.width!=0, "Riot logo has null dimension");
		//check that bottom bar isn't here
		//send bar
		Assert.assertFalse(waitUntilDisplayed(driver,"//android.widget.RelativeLayout[@resource-id='im.vector.alpha:id/room_bottom_layout']//android.widget.EditText[@resource-id='im.vector.alpha:id/editText_messageBox']", false, 0));
		Assert.assertFalse(waitUntilDisplayed(driver,"im.vector.alpha:id/room_send_layout", false, 0));
		Assert.assertFalse(waitUntilDisplayed(driver,"im.vector.alpha:id/room_start_call_layout", false, 0));
		Assert.assertFalse(waitUntilDisplayed(driver,"im.vector.alpha:id/room_end_call_layout", false, 0));
	}

	/**
	 * Check the room page layout.
	 * TODO complete this function by inspirant of checkPreviewRoomLayout() 
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void checkRoomLayout(String roomName) throws InterruptedException {
		//check that preview layout isn't displayed
		Assert.assertFalse(waitUntilDisplayed(driver,"im.vector.alpha:id/room_preview_info_layout", false, 3));
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
	 * Return true or false if the room view is still displayed.</br>
	 * Can be useful to test that the room page is closed after quitting it.
	 * @param displayed
	 * @throws InterruptedException
	 */
	public void isDisplayed(Boolean displayed) throws InterruptedException{
		String messageFailed;
		if(displayed){
			messageFailed="Room page isn't displayed.";
		}else{
			messageFailed="Room page is displayed.";
		}
		Assert.assertEquals(waitUntilDisplayed(driver,"im.vector.alpha:id/listView_messages", displayed, 5),displayed, messageFailed);
	}

	/*
	 * PENDING CALL VIEW
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_pending_call_view")
	public MobileElement roomPendingCallLayout;
	@AndroidFindBy(id="im.vector.alpha:id/call_icon")
	public MobileElement callIconImageView;
	@AndroidFindBy(id="im.vector.alpha:id/pending_call_room_name_textview")
	public MobileElement calledRoomNameTextView;
	@AndroidFindBy(id="im.vector.alpha:id/pending_call_status_textview")
	public MobileElement callStatusTextView;

	public void checkPendingCallView(Boolean displayed,String status) throws InterruptedException{
		String messagePendingViewPresent;
		if(displayed){
			messagePendingViewPresent="Pending view isn't displayed";
		}else{
			messagePendingViewPresent="Pending view is displayed";
		}
		Assert.assertTrue(waitUntilDisplayed(driver, "im.vector.alpha:id/room_pending_call_view", displayed, 5)==displayed, messagePendingViewPresent);
		if(displayed){
			waitUntilDisplayed(driver, "im.vector.alpha:id/pending_call_status_textview", true, 5);
			Assert.assertEquals(callStatusTextView.getText(), status);
		}
	}

	/*
	 * MESSAGES
	 */
	/**
	 * Similar to messagesListView, but as a list and not just a MobileElement.
	 */
	@AndroidFindBy(xpath="//android.widget.ListView[@resource-id='im.vector.alpha:id/listView_messages']/android.widget.LinearLayout")//messages list. Contains messages, days separators
	public List<MobileElement> postsListLayout;

	public MobileElement getLastPost(){
		//return Iterables.getLast(this.postsListLayout);
		int sizeList=postsListLayout.size();
		if(sizeList>0){
			return postsListLayout.get(sizeList-1);
		}else{
			Assert.fail("Not a single post layout found in the room.");
			return null;
		}
		
	}

	/**
	 * Return a post by his index.
	 */
	public MobileElement getPostByIndex(int indexPost){
		return postsListLayout.get(indexPost);
	}

	/**
	 * TODO use xpath instead of id because of https://github.com/appium/appium/issues/6269 issue
	 * @param postLinearLayout
	 * @return
	 */
	public MobileElement getUserAvatarByPost(MobileElement postLinearLayout){
		try {
			return postLinearLayout.findElementById("im.vector.alpha:id/avatar_img");
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * TODO use xpath instead of id because of https://github.com/appium/appium/issues/6269 issue
	 * @param postLinearLayout
	 * @return
	 */
	public MobileElement getSenderNameByPost(MobileElement postLinearLayout){
		try {
			return postLinearLayout.findElementById("im.vector.alpha:id/messagesAdapter_sender");
		} catch (Exception e) {
			return null;
		}
	}
	/**TODO use xpath instead of id because of https://github.com/appium/appium/issues/6269 issue
	 * Get the imageview attached from a linearLayout object message (first children of the listView_messages). </br> Return null if not found.
	 * @param message
	 * @return
	 */
	public MobileElement getAttachedImageByPost(MobileElement postLinearLayout){
		try {
			return postLinearLayout.findElement(By.id("im.vector.alpha:id/messagesAdapter_image"));
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * TODO use xpath instead of id because of https://github.com/appium/appium/issues/6269 issue
	 * Return the timestamp, example : 15:12. </br> Return null if not found.
	 * @param postLinearLayout
	 * @return
	 */
	public MobileElement getTimeStampByPost(MobileElement postLinearLayout){	
		try {
			//			return postLinearLayout.findElement(By.id("im.vector.alpha:id/messagesAdapter_timestamp"));
			return postLinearLayout.findElement(By.xpath("//android.widget.TextView[@resource-id='im.vector.alpha:id/messagesAdapter_timestamp']"));
		} catch (Exception e) {
			return null;
		}
	}
	public MobileElement getContextMenuByPost(MobileElement postLinearLayout){
		try {
			return postLinearLayout.findElementById("im.vector.alpha:id/messagesAdapter_action_image");
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * TODO use xpath instead of id because of https://github.com/appium/appium/issues/6269 issue
	 * Get the textView from a post. </br> Return null if not found.
	 * @param message
	 * @return
	 */
	public MobileElement getTextViewFromPost(MobileElement postLinearLayout){
		try {
			return postLinearLayout.findElement(By.id("im.vector.alpha:id/messagesAdapter_body"));
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Get the media upload failed icon on an attached image from a post. </br> Return null if not found.
	 * @param message
	 * @return
	 */
	public MobileElement getMediaUploadFailIconFromPost(MobileElement postLinearLayout){
		try {
			return postLinearLayout.findElement(By.id("im.vector.alpha:id/media_upload_failed"));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the progress bar displayed when a photo or video is uploaded.</br> Return null if not found.
	 * @param message
	 * @return
	 */
	public MobileElement getProgressionBarFromPost(MobileElement postLinearLayout){
		try {
			//return postLinearLayout.findElement(By.xpath("//android.widget.ProgressBar[@resource-id='im.vector.alpha:id/media_progress_view']"));
			return postLinearLayout.findElement(By.id("im.vector.alpha:id/media_progress_view"));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the "X" imageview to stop the upload of a photo or video.</br> Return null if not found.
	 * @param postLinearLayout
	 * @return
	 */
	public MobileElement getCancelMediaProgressFromPost(MobileElement postLinearLayout){
		try {
			return postLinearLayout.findElement(By.xpath("//android.widget.ProgressBar[@resource-id='im.vector.alpha:id/media_progress_view']"));
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * NOTIFICATION AREA
	 */
	@AndroidFindBy(id="im.vector.alpha:id/room_notifications_area")
	public MobileElement roomNotificationArea;
	@AndroidFindBy(id="im.vector.alpha:id/room_notification_icon")
	public MobileElement notificationIcon;
	@AndroidFindBy(id="im.vector.alpha:id/room_notification_message")
	public MobileElement notificationMessage;

	/**
	 * Click on "Resend All" link on the messages not sent due to unknown devices messages, on the notification area.
	 */
	public void clickOnResendAllLinkFromNotificationArea(){
		notificationMessage.getSize().getWidth();notificationMessage.getCenter();
		int xLink=notificationMessage.getCenter().getX()+10;
		int yLink=notificationMessage.getLocation().getY()+(notificationMessage.getSize().getHeight()/4)*3;
		driver.tap(1, xLink, yLink, 10);
	}
	//MobileElement lastMessage=(MobileElement) getDriver().findElementByXPath("//android.widget.ListView[@resource-id='im.vector.alpha:id/listView_messages']/android.widget.LinearLayout[last()]");

	/*
	 * BOTTOM BAR : send edittext, attachment button, callbutton
	 */
	//@AndroidFindBy(xpath="//android.widget.RelativeLayout[@resource-id='im.vector.alpha:id/room_bottom_layout']//android.widget.EditText[@resource-id='im.vector.alpha:id/editText_messageBox']")
	@AndroidFindBy(id="im.vector.alpha:id/editText_messageBox")
	public MobileElement messageZoneEditText;
	@AndroidFindBy(id="im.vector.alpha:id/room_send_layout")
	public MobileElement sendMessageButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_start_call_layout")//start a call button
	public MobileElement startCallButton;
	@AndroidFindBy(id="im.vector.alpha:id/room_end_call_layout")//end a call button
	public MobileElement endCallButton;

	/*
	 * CONTEXT MENU ON MESSAGE
	 */
	@AndroidFindBy(className="android.widget.ListView")//list items menu
	public MobileElement menuListItems;
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Quote']/../..")//quote item
	public MobileElement quoteItemFromMenu;

	/*
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
	@AndroidFindBy(xpath="//android.widget.ListView//android.widget.TextView[@text='Take photo or video']/..")//take photo menu with send files and take photo buttons
	public MobileElement takePhotoFromMenuButton;

	/*
	 * ALERT DIALOG
	 */
	/**
	 * Cancel Button.
	 */
	@AndroidFindBy(id="android:id/button2")
	public MobileElement alertDialogButton1;
	/**
	 * Leave Button.
	 */
	@AndroidFindBy(id="android:id/button1")
	public MobileElement alertDialogButton2;




	/*
	 * Functions
	 */
	/**
	 * Type and send a message.
	 * @param message
	 */
	public void sendAMessage(String message){
		ExplicitWait(driver, messageZoneEditText);
		messageZoneEditText.sendKeys(message);//messageZoneEditText.setValue(message); //<-- doesn't work on this edittext
		sendMessageButton.click();
		System.out.println("Message "+message+" sent in the room.");
	}

	/**
	 * Wait for a new post to arrive in the room.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InterruptedException
	 */
	public void waitForReceivingNewMessage(int maxSecondsToWait) throws InstantiationException, IllegalAccessException, InterruptedException{
		int sizeAtBegining=postsListLayout.size();
		float secondsWaited=0;
		while (postsListLayout.size()==sizeAtBegining && secondsWaited<maxSecondsToWait) {
			Thread.sleep(500);secondsWaited=(float) (secondsWaited+0.5);
		}
	}

	/**
	 * Wait for the posts lists to be not empty.
	 * @throws InterruptedException
	 */
	public void waitForPostsToBeDisplayed() throws InterruptedException{
		int maxSecondsToWait=10;
		float secondsWaited=0;
		while (postsListLayout.size()==0 && secondsWaited<maxSecondsToWait) {
			Thread.sleep(500);secondsWaited=(float) (secondsWaited+0.5);
		}
	}

	/**
	 * Wait until progress bar in the post is still displayed.
	 * @param postLinearLayout
	 * @param maxToWait
	 * @throws InterruptedException
	 */
	public Boolean waitAndCheckForMediaToBeUploaded(MobileElement postLinearLayout, int maxToWait) throws InterruptedException {
		float secondsWaited=0;Boolean uploaded=false;
		while (getProgressionBarFromPost(postLinearLayout)!=null && secondsWaited<maxToWait) {
			Thread.sleep(500);secondsWaited=(float) (secondsWaited+0.5);
		}
		if(getProgressionBarFromPost(postLinearLayout)==null){
			uploaded=true;
			System.out.println("Media uploaded after "+secondsWaited+" s.");
		}
		return uploaded;
	}
	
	/**
	 * Check that a photo is present in a post by checking his dimension.
	 * @param post
	 */
	public void checkThatPhotoIsPresentInPost(MobileElement post){
		org.openqa.selenium.Dimension takenPhoto=this.getAttachedImageByPost(post).getSize();
	    Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
	}

	/**
	 * From a room, take a photo and attach it to the messages.
	 * @param photoSize : choose between Original, Large, Medium, Small.
	 * @throws InterruptedException 
	 */
	public void attachPhotoFromCamera(String photoSize) throws InterruptedException{
		sendMessageButton.click(); //(this button id is for both attachment files and send message buttons)
		takePhotoFromMenuButton.click();
		RiotCameraPageObjects cameraPreview = new RiotCameraPageObjects(driver);
		cameraPreview.triggerCameraButton.click();//take a photo
		waitUntilDisplayed(driver,"im.vector.alpha:id/medias_picker_preview_layout", true, 5);
		cameraPreview.confirmPickingPictureButton.click();
		ExplicitWaitToBeVisible(driver,cameraPreview.sendAsMenuLayout);
		cameraPreview.getItemFromSendAsMenu(photoSize).click();
		//wait until the progress layout on the room page is gone. Don't mix up with the progress bar in the post showing the upload of the media.
		waitUntilDisplayed(driver, "im.vector.alpha:id/medias_processing_progress", false, 10);
	}

	/**
	 * Start a voice call from the room page.
	 * @throws InterruptedException 
	 */
	public void startVoiceCall() throws InterruptedException {
		waitForPostsToBeDisplayed();
		startCallButton.click();
		voiceCallFromMenuButton.click();
	}

	public void startVideoCall() throws InterruptedException{
		waitForPostsToBeDisplayed();
		startCallButton.click();
		videoCallFromMenuButton.click();
	}
	
	/*
	 * Encryption
	 */
	/**
	 * Close the warning alert about the beta state of encryption when user enters the first time in a room.
	 * @throws InterruptedException
	 */
	public void closeWarningAlertAboutBetaStateOfE2e() throws InterruptedException{
		if(waitUntilDisplayed(driver, "im.vector.alpha:id/parentPanel", true, 1)){
			alertDialogButton2.click();
		}
	}
}
