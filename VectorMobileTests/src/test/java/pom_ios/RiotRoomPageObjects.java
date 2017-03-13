package pom_ios;

import java.util.List;

import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSFindBy;
import utility.TestUtilities;

public class RiotRoomPageObjects extends TestUtilities{
	private IOSDriver<MobileElement> driver;
	public RiotRoomPageObjects(AppiumDriver<MobileElement> myDriver) {
		driver= (IOSDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(driver), this);
		try {
			waitUntilDisplayed((IOSDriver<MobileElement>) driver,"RoomVCView", true, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
 /*
  * NAVIGATION BAR
  */
	@iOSFindBy(accessibility="Messages")
	public MobileElement navigationBar;
	@iOSFindBy(accessibility="Back")
	public MobileElement menuBackButton;
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeNavigationBar[@name=\"Messages\"]/XCUIElementTypeOther[1]//XCUIElementTypeStaticText")
	public MobileElement badgeNumberStaticText;
	@iOSFindBy(accessibility="DisplayNameTextField")
	public MobileElement roomNameStaticText;
	@iOSFindBy(accessibility="RoomDetailsIconImageView")
	public MobileElement openRoomDetails;
	@iOSFindBy(accessibility="search icon")
	public MobileElement searchInRoomButton;
	
	/*
	 * COLLAPSED NAVIGATION BAR
	 */
	@iOSFindBy(accessibility="RoomVCExpandedHeaderContainer")
	public MobileElement collapsedRoomDetailsContainer;
	@iOSFindBy(accessibility="RoomDetailsIconImageView")
	public MobileElement openRoomDetailsFromCollapsedBar;
	@iOSFindBy(accessibility="RoomMembers")
	public MobileElement inviteMembersLink;
	@iOSFindBy(accessibility="RoomTopic")
	public MobileElement roomTopicStaticText;
	@iOSFindBy(accessibility="RoomMembersDetailsIcon")
	public MobileElement roomMembersDetailIcon;
	
	
	/*
	 * PREVIEW LAYOUT
	 */
	 @iOSFindBy(accessibility="RoomVCPreviewHeaderContainer")
	 public MobileElement roomPreviewContainer;
	 @iOSFindBy(accessibility="MainHeaderBackground")
	 public MobileElement roomPreviewBackground;
	 @iOSFindBy(accessibility="PreviewLabel")
	 public MobileElement roomPreviewLabel;
	 @iOSFindBy(accessibility="RightButton")
	 public MobileElement joinRoomButton;
	 @iOSFindBy(accessibility="LeftButton")
	 public MobileElement declineRoomButton;
	 
	/**
	 * Check the room preview layout after accepting an invitation.
	 * @throws InterruptedException 
	 */
	public void checkPreviewRoomLayout(String roomName) throws InterruptedException{
		Assert.assertEquals(roomPreviewContainer.findElementByAccessibilityId("DisplayNameTextField").getText(), roomName);
		Assert.assertTrue(roomPreviewLabel.getText().contains("You have been invited to join this room by"));
		Assert.assertEquals(joinRoomButton.getText(), "Join");
		Assert.assertEquals(declineRoomButton.getText(), "Decline");
	}
	
	/*
	 * PENDING CALL VIEW
	 */
	public MobileElement getPendingCallView(String status) throws WebDriverException, InterruptedException{
		if(waitUntilDisplayed(driver, status, true, 5)) {
			return driver.findElementByAccessibilityId(status);
			}
		else{
			return null;
		}
	}
	
	public void checkPendingCallView(Boolean displayed,String status) throws InterruptedException {
		String messagePendingViewPresent;
		//Boolean statusFound=false;
		if(displayed){
			messagePendingViewPresent="Pending view isn't displayed";
		}else{
			messagePendingViewPresent="Pending view is displayed";
		}
//		try {
//			driver.findElementByAccessibilityId(status);
			
//			statusFound=true;
//		} catch (Exception e) {
//			// TODO: handle exception
//			statusFound=false;
//		}
		Assert.assertEquals(waitUntilDisplayed(driver, status, displayed, 5), displayed, messagePendingViewPresent);
		
	}
	/*
	 * MESSAGES
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeTable[1]")
	public MobileElement bubblesTable;
	@iOSFindBy(accessibility="RoomBubbleCell")
	public List<MobileElement> bubblesList;

	/**
	 * Return the last bubble as a mobileElement.
	 */
	public MobileElement getLastBubble(){
		if (bubblesList.size()>0){
			return bubblesList.get(bubblesList.size()-1);
		}
		else{
			return null;
		}
	}
	/**
	 * Return the last bubble as a mobileElement using xpath. May be faster than getLastBubble() if there is a lot of bubbles in the room.
	 */
	public MobileElement getLastBubble_bis(){
		return bubblesTable.findElementByXPath("//XCUIElementTypeCell[last()]");
	}
	
	/**
	 * Return a bubble by his index.
	 */
	public MobileElement getBubbleByIndex(int indexPost){
		return bubblesList.get(indexPost);
	}
	
	/**
	 * Return the author of a bubble as a MobileElement.
	 * @param bubble
	 * @return
	 */
	public MobileElement getAuthorFromBubble(MobileElement bubble){
		return bubble.findElementByAccessibilityId("UserNameLabel");
	}
	
	/**
	 * Return the content text of a bubble as a MobileElement.
	 */
	public MobileElement getTextViewFromBubble(MobileElement bubble){
		return bubble.findElementByAccessibilityId("MessageTextView");
	}
	/**
	 * Return the content text of a bubble as a MobileElement.
	 */
	public MobileElement getProgressStatsFromBubble(MobileElement bubble){
		try {
			return bubble.findElementByAccessibilityId("ProgressStats");
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/**
	 * Wait until progress bar in the post is still displayed.
	 * @param bubbleCell
	 * @param maxToWait
	 * @throws InterruptedException
	 */
	public Boolean waitAndCheckForMediaToBeUploaded(MobileElement bubbleCell, int maxToWait) throws InterruptedException {
		float secondsWaited=0;Boolean uploaded=false;
		while (getProgressStatsFromBubble(bubbleCell)!=null && secondsWaited<maxToWait) {
			Thread.sleep(500);
			secondsWaited=(float) (secondsWaited+0.5);
		}
		if(getProgressStatsFromBubble(bubbleCell)==null){
			uploaded=true;
			System.out.println("Media uploaded after "+secondsWaited+" s.");
		}
		return uploaded;
	}
	
	/**
	 * Wait for the bubbles lists to be not empty.
	 * @throws InterruptedException
	 */
	public void waitForBubblesToBeDisplayed() throws InterruptedException{
		int maxSecondsToWait=10;
		float secondsWaited=0;
		while (bubblesList.size()==0 && secondsWaited<maxSecondsToWait) {
			Thread.sleep(500);secondsWaited=(float) (secondsWaited+0.5);
		}
	}
	/**
	 * Wait for a new post to arrive in the room.
	 * @param maxSecondsToWait
	 * @throws InterruptedException 
	 */
	public void waitForReceivingNewMessage(int maxSecondsToWait) throws InterruptedException {
		int sizeAtBegining=bubblesList.size();
		float secondsWaited=0;
		while (bubblesList.size()==sizeAtBegining && secondsWaited<maxSecondsToWait) {
			Thread.sleep(500);secondsWaited=(float) (secondsWaited+0.5);
		}
		System.out.println("Time waited for new messages to arrive: "+secondsWaited+ " seconds.");
	}
	
	/*
	 * SELECT MEDIA SIZE MENU
	 */
	@iOSFindBy(accessibility="Do you want to send as:")
	public MobileElement selectSizeMenuSheet;
	/**
	 * 'Send' as item list. Contains small -> actual size items + cancel item.
	 */
	@iOSFindBy(xpath="//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeSheet//XCUIElementTypeCollectionView/XCUIElementTypeCell")
	public MobileElement sendAsItemList;
	
	/*
	 * NOTIFICATION AREA
	 */
	@iOSFindBy(accessibility="RoomVCActivitiesContainer")
	public MobileElement roomNotificationArea;
	@iOSFindBy(accessibility="RoomExtrasInfosView")
	public MobileElement roomNotificationArea2;
	
	/**
	 * Not sure it works. Better getNotificationMessage().
	 */
	@iOSFindBy(accessibility="RoomActivitiesViewMessageLabel")
	public MobileElement notificationMessage;
	
	public MobileElement getNotificationMessage(){
		return roomNotificationArea2.findElementByClassName("XCUIElementTypeTextView");
	}
	
	/**
	 * Click on "Resend All" link on the messages not sent due to unknown devices messages, on the notification area.
	 */
	public void clickOnResendAllLinkFromNotificationArea(){
		notificationMessage.getSize().getWidth();notificationMessage.getCenter();
		int xLink=notificationMessage.getCenter().getX();
		//int yLink=notificationMessage.getLocation().getY()+(notificationMessage.getSize().getHeight()/4)*3;
		int yLink=notificationMessage.getLocation().getY()+(notificationMessage.getSize().getHeight()/4)*2+5;
		driver.tap(1, xLink, yLink, 500);
	}
	
	/*
	 * BOTTOM
	 */
	@iOSFindBy(accessibility="AttachButton")
	public MobileElement uploadButton;
	@iOSFindBy(accessibility="SendButton")
	public MobileElement sendButton;
	@iOSFindBy(accessibility="VoiceCallButton")
	public MobileElement voiceCallButton;
	@iOSFindBy(accessibility="HangupCallButton")
	public MobileElement hangUpCallButton;
	@iOSFindBy(accessibility="EncryptedRoomIcon")
	public MobileElement e2eIconImage;
	@iOSFindBy(accessibility="GrowingTextView")
	public MobileElement sendKeyTextView;
	
	/*
	 * MENU VOICE and VIDEO.
	 */
	/**
	 * Menu opened after hitting the call button. Proposes voice or video call.
	 */
	@iOSFindBy(className="XCUIElementTypeCollectionView")
	public MobileElement menuCallChoiceCollectionView;
	@iOSFindBy(accessibility="Voice")
	public MobileElement voiceItemMenu;
	@iOSFindBy(accessibility="Video")
	public MobileElement videoItemMenu;
	
	/**
	 * Start a voice call from the room page.
	 * @throws InterruptedException 
	 */
	public void startVoiceCall() throws InterruptedException {
		voiceCallButton.click();
		voiceItemMenu.click();
	}
	/**
	 * Start a voice call from the room page.
	 * @throws InterruptedException 
	 */
	public void startVideoCall() throws InterruptedException {
		voiceCallButton.click();
		videoItemMenu.click();
	}

	/**
	 * Check that a photo is present in a post by checking his dimension.
	 * @param post
	 */
	public void checkThatPhotoIsPresentInPost(MobileElement post){
//		org.openqa.selenium.Dimension takenPhoto=this.getAttachedImageByPost(post).getSize();
//	    Assert.assertTrue(takenPhoto.height!=0 && takenPhoto.width!=0, "The unsent photo has null dimension");
	}
	/**
	 * Hit the upload button, take photo, then select a size.
	 * @param size
	 * @throws InterruptedException
	 */
	public void attachPhotoFromCamera(String size) throws InterruptedException {
		uploadButton.click();
		RiotCameraPageObjects cameraPage = new RiotCameraPageObjects(appiumFactory.getiOsDriver1());
		cameraPage.cameraCaptureButton.click();
		waitUntilDisplayed(driver, "OK", true, 10);
		cameraPage.okButton.click();
		getItemFromSendAsMenu(size).click();;
	}
	/**
	 * Return item from the "Send as menu"
	 * @param size : Original, Large, Medium, Small
	 * @return
	 */
	public MobileElement getItemFromSendAsMenu(String size){
		ExplicitWait(driver, selectSizeMenuSheet);
		return driver.findElementByXPath("//XCUIElementTypeApplication/XCUIElementTypeWindow//XCUIElementTypeSheet//XCUIElementTypeCollectionView/XCUIElementTypeCell/XCUIElementTypeButton[contains(@name,'"+size+"')]");
	}


	/**
	 * Type and send a message.
	 * @param msg
	 */
	public void sendAMessage(String msg) {
		sendKeyTextView.sendKeys(msg);
		sendButton.click();
	}
	
	/**
	 * From the room page, go into the details of the room and hit leave button.
	 */
	public void leaveRoom() {
		openDetailView();
		RiotRoomDetailsPageObjects details1= new RiotRoomDetailsPageObjects(driver);
		details1.settingsTab.click();
		details1.leaveButton.click();
		details1.confirmLeaveFromAlertButton.click();
		details1= new RiotRoomDetailsPageObjects(driver);
		details1.menuBackButton.click();
	}
	/**
	 * Open the detail view from the page.</br>
	 * Return a RiotRoomDetailsPageObjects.
	 */
	public RiotRoomDetailsPageObjects openDetailView(){
		if(isRoomHeaderExpanded()==true){
			roomMembersDetailIcon.click();
		}else{
			openRoomDetails.click();
		}
		return new RiotRoomDetailsPageObjects(driver);
	}
	/**
	 * Return true if the room details is expanded and false if not.
	 * @return
	 */
	public Boolean isRoomHeaderExpanded(){
		if(collapsedRoomDetailsContainer.findElementByAccessibilityId("DisplayNameTextField").getText().equals("Room Name")){
			return false;
		}else{
			return true;
		}
	}

	/*
	 * ENCRYPTION
	 */
	@iOSFindBy(accessibility="RoomVCEncryptionAlert")
	public MobileElement WarningOnBetaEncryptionAlert;
	@iOSFindBy(accessibility="RoomVCEncryptionAlertActionOK")
	public MobileElement WarningOnBetaEncryptionOkButton;
	
	/**
	 * Close the warning alert about the beta state of encryption when user enters the first time in a room.
	 * @throws InterruptedException
	 */
	public void closeWarningAlertAboutBetaStateOfE2e() throws InterruptedException{
		if(waitUntilDisplayed(driver, "RoomVCEncryptionAlert", true, 1)){
			WarningOnBetaEncryptionOkButton.click();
		}
		
	}
}
