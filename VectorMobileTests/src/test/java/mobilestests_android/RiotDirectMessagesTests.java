package mobilestests_android;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_android.RiotContactPickerPageObjects;
import pom_android.RiotNewChatPageObjects;
import pom_android.RiotRoomDetailsPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotSettingsPageObjects;
import pom_android.main_tabs.RiotHomePageTabObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on Direct Message.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotDirectMessagesTests extends RiotParentTest{
	private String riotuser1DisplayName="riotuser6";
	private String riotuser2DisplayName="riotuser7";
	private String riotuser3DisplayName="riotuser8";
	private String tmpRoomName="tmp room DM";

	/**
	 * Start a chat </br>
	 * Invite just on other user </br>
	 * Check that the new room doesn't have a little green man on both devices.</br>
	 * Try to start a newt chat with this same member : check that the previous room is opened instead.
	 * @throws InterruptedException
	 * @throws YamlException 
	 * @throws FileNotFoundException 
	 */
	@Test(groups={"2drivers_android","2checkusers_android","check_contacts_permission"}, description="direct message test")
	public void startChatWithOneUserTwice() throws InterruptedException, FileNotFoundException, YamlException{
		int randInt = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomMsg=(new StringBuilder("direct chat test").append(randInt)).toString();

		/*
		 * FIRST PART
		 */
		RiotHomePageTabObjects homePage1=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotHomePageTabObjects homePage2=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver2());
		//create a new "start chat" room
		RiotRoomPageObjects newRoomDevice1= homePage1.startChat(riotuser2DisplayName);
		newRoomDevice1.menuBackButton.click();
		//assertion on the inviter device: the new room is seen as an direct message room.
		Assert.assertTrue(homePage1.isDirectMessageByRoomName(riotuser2DisplayName),"Room "+riotuser2DisplayName+" doesn't have a little green man on inviter device.");
		//assertion the invitee device (device 2)
		//TODO: check little green man on invitation layout
		homePage2.previewInvitation(riotuser1DisplayName);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.joinRoomButton.click();
		//send a msg from device 2 as a proof for second part
		newRoomDevice2.sendAMessage(randomMsg);
		ExplicitWait(appiumFactory.getAndroidDriver2(), newRoomDevice2.menuBackButton);
		//appiumFactory.getAndroidDriver2().pressKeyCode(AndroidKeyCode.BACK);
		newRoomDevice2.menuBackButton.click();
		//assertion on the invitee device: the new room is seen as an direct message room.
		Assert.assertTrue(homePage2.isDirectMessageByRoomName(riotuser1DisplayName),"Room "+riotuser1DisplayName+" doesn't have a little green man on invitee device.");
		/*
		 * SECOND PART
		 */
		//create a new "start" room with the same member a second time.
		homePage1.createRoomFloatingButton.click();
		homePage1.startChatCheckedTextView.click();
		//invite a member
		RiotContactPickerPageObjects inviteViewDevice1=new RiotContactPickerPageObjects(appiumFactory.getAndroidDriver1());
		inviteViewDevice1.searchAndSelectMember(getMatrixIdFromDisplayName(riotuser2DisplayName));
		RiotNewChatPageObjects newChatViewDevice1= new RiotNewChatPageObjects(appiumFactory.getAndroidDriver1());
		newChatViewDevice1.confirmRoomCreationButton.click();
		//check that the previously create room is opened instead of being created again.
		newRoomDevice1= new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		Assert.assertEquals(newRoomDevice1.roomNameTextView.getText(), riotuser2DisplayName,"The previously created room isn't opened");
		Assert.assertEquals(newRoomDevice1.getTextViewFromPost(newRoomDevice1.getLastPost()).getText(), randomMsg,"The previously created room isn't opened");	
		newRoomDevice1.menuBackButton.click();
	}

	/**
	 * Start a chat </br>
	 * Invite more than two users </br>
	 * Check that the new room doesn't have a little green man on both devices.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkusers_android","check_contacts_permission"}, description="direct message test")
	public void startChatWithMoreThanTwoUsers() throws InterruptedException{
		String roomNameFromDevice1=riotuser2DisplayName+" and "+riotuser3DisplayName;

		RiotHomePageTabObjects homePage1=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotHomePageTabObjects homePage2=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver2());
		//create a new "start chat" room
		homePage1.createRoomFloatingButton.click();
		homePage1.startChatCheckedTextView.click();
		//invite a first member
		RiotContactPickerPageObjects inviteViewDevice1=new RiotContactPickerPageObjects(appiumFactory.getAndroidDriver1());
		inviteViewDevice1.searchAndSelectMember(getMatrixIdFromDisplayName(riotuser2DisplayName));
		RiotNewChatPageObjects newChatViewDevice1= new RiotNewChatPageObjects(appiumFactory.getAndroidDriver1());
		newChatViewDevice1.addMemberItemListView.click();
		//invite a second member
		inviteViewDevice1.searchAndSelectMember(getMatrixIdFromDisplayName(riotuser3DisplayName));
		newChatViewDevice1.confirmRoomCreationButton.click();

		//Room is created, then go back in rooms list
		RiotRoomPageObjects newRoomDevice1= new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		newRoomDevice1.menuBackButton.click();
		//assertion on the inviter device: the new room is NOT seen as an direct message room.
		Assert.assertFalse(homePage1.isDirectMessageByRoomName(roomNameFromDevice1),"Room "+roomNameFromDevice1+" doesn't have a little green man on inviter device.");
		//assertion the invitee device (device 2)
		//TODO: check little green man on invitation layout
		homePage2.previewInvitation(riotuser1DisplayName);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.joinRoomButton.click();
		ExplicitWait(appiumFactory.getAndroidDriver2(), newRoomDevice2.menuBackButton);
		newRoomDevice2.menuBackButton.click();
		//assertion on the invitee device: the new room is NOT seen as an direct message room.
		Assert.assertFalse(homePage2.isDirectMessageByRoomName(riotuser1DisplayName),"Room "+riotuser1DisplayName+" doesn't have a little green man on invitee device.");
	}

	/**
	 * Create a room </br>
	 * Invite just one user</br>
	 * Check that the new room doesn't have a little green man.
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkusers_android","check_contacts_permission"}, description="direct message test")
	public void createRoomWithOneUser() throws InterruptedException{
		RiotHomePageTabObjects homePage1=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotHomePageTabObjects homePage2=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver2());
		//create a new  room
		RiotRoomPageObjects newRoomDevice1 = homePage1.createRoom();
		//invite 1 member
		newRoomDevice1.addParticipantFromCollapsedActionBar(getMatrixIdFromDisplayName(riotuser2DisplayName));
		newRoomDevice1.menuBackButton.click();
		//assertion on the inviter device: the new room is NOT seen as an direct message room.
		Assert.assertFalse(homePage1.isDirectMessageByRoomName(riotuser2DisplayName),"Room "+riotuser2DisplayName+" doesn't have a little green man on inviter device.");

		//assertion the invitee device (device 2)
		//TODO: check little green man on invitation layout
		homePage2.previewInvitation(riotuser1DisplayName);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.joinRoomButton.click();
		ExplicitWait(appiumFactory.getAndroidDriver2(), newRoomDevice2.menuBackButton);
		//appiumFactory.getAndroidDriver2().pressKeyCode(AndroidKeyCode.BACK);
		newRoomDevice2.menuBackButton.click();
		//assertion on the invitee device: the new room is NOT seen as an direct message room.
		Assert.assertFalse(homePage2.isDirectMessageByRoomName(riotuser1DisplayName),"Room "+riotuser1DisplayName+" doesn't have a little green man on invitee device.");
	}

	/**
	 * 1. Create a non direct message room.</br>
	 * 2. Tag it on DM on device 1.</br>
	 * Check that the DM tag is changed on device 1.</br>
	 * 3. Tag it On DM on device 2</br>
	 * Check that the DM tag is changed on device 2.</br>
	 * 4. UnTag it on device 1.</br>
	 * Check that the DM tag is changed on device 1.</br>
	 * 5. UnTag it on device 2.</br>
	 * Check that the DM tag is changed on device 1.</br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_android","2checkusers_android","check_contacts_permission"})
	public void tagAndUntagDirectMessageRoom() throws InterruptedException{
		RiotHomePageTabObjects homePage1=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotHomePageTabObjects homePage2=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver2());
		//1. Create a direct message room.
		RiotRoomPageObjects newRoomDevice1 = homePage1.createRoom();
		//put a room name
		newRoomDevice1.changeRoomName(tmpRoomName);
		//invite 1 member
		newRoomDevice1.openRoomMemberSettingsButton.click();
		RiotRoomDetailsPageObjects roomDetailsDevice1 = new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());
		//add a participant from the details
		roomDetailsDevice1.addParticipant(getMatrixIdFromDisplayName(riotuser2DisplayName));
		roomDetailsDevice1.menuBackButton.click();
		newRoomDevice1.menuBackButton.click();

		//accept invitation from device 2.
		homePage2.previewInvitation(tmpRoomName);
		RiotRoomPageObjects newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.joinRoomButton.click();
		//ExplicitWait(appiumFactory.getAndroidDriver2(), newRoomDevice2.menuBackButton);
		newRoomDevice2 = new RiotRoomPageObjects(appiumFactory.getAndroidDriver2());
		newRoomDevice2.menuBackButton.click();

		//2. Tag it on DM on device 1
		homePage1.clickOnContextMenuOnRoom(tmpRoomName, "Direct Chat");
		Assert.assertTrue(homePage1.isDirectMessageByRoomName(tmpRoomName),"Room "+tmpRoomName+" doesn't have a little green man on inviter device.");
		//3. Tag it on DM on device 2
		homePage2.clickOnContextMenuOnRoom(tmpRoomName, "Direct Chat");
		Assert.assertTrue(homePage2.isDirectMessageByRoomName(tmpRoomName),"Room "+tmpRoomName+" doesn't have a little green man on invitee device.");

		//4. Untag it on DM on device 1
		homePage1.clickOnContextMenuOnRoom(tmpRoomName, "Direct Chat");
		Assert.assertFalse(homePage1.isDirectMessageByRoomName(tmpRoomName),"Room "+tmpRoomName+" have a little green man on inviter device.");
		//5. Untag it on DM on device 2
		homePage2.clickOnContextMenuOnRoom(tmpRoomName, "Direct Chat");
		Assert.assertFalse(homePage2.isDirectMessageByRoomName(tmpRoomName),"Room "+tmpRoomName+" have a little green man on invitee device.");
	}



	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException{
		switch (m.getName()) {
		case "startChatWithOneUserTwice":
			leaveRoomFromRoomsListAfterTest(riotuser2DisplayName,"Empty room");
			break;
		case "startChatWithMoreThanTwoUsers":
			leaveRoomFromRoomsListAfterTest(riotuser3DisplayName+" and "+riotuser2DisplayName,riotuser3DisplayName);
			break;
		case "createRoomWithOneUser":
			leaveRoomFromRoomsListAfterTest(riotuser2DisplayName,"Empty room");
			break;
		case "tagAndUntagDirectMessageRoom":
			leaveRoomFromRoomsListAfterTest(tmpRoomName,tmpRoomName);
			break;
		default:
			break;
		}
	}
	
	@AfterMethod(alwaysRun=true,groups={"2drivers_android"})
	private void restart2ApplicationAfterTest(Method m) throws InterruptedException{
		restartApplication(appiumFactory.getAndroidDriver1());
		restartApplication(appiumFactory.getAndroidDriver2());
	}

	private void leaveRoomFromRoomsListAfterTest(String roomNameFromDevice1, String roomNameFromDevice2) throws InterruptedException{
		RiotHomePageTabObjects homePage1=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotHomePageTabObjects homePage2=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver2());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		homePage1.leaveRoom(roomNameFromDevice1);
		homePage1.waitUntilSpinnerDone(5);
		System.out.println("Leave room "+roomNameFromDevice2+ " with device 2");
		homePage2.leaveRoom(roomNameFromDevice2);
		homePage2.waitUntilSpinnerDone(5);
		Assert.assertNull(homePage1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
		Assert.assertNull(homePage2.getRoomByName(roomNameFromDevice2), "Room "+roomNameFromDevice2+" is still displayed in the list in device 2.");
	}

	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 * @throws YamlException 
	 * @throws FileNotFoundException 
	 */
	@BeforeGroups("2checkusers_android")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotuser1DisplayName, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver2(), riotuser2DisplayName, Constant.DEFAULT_USERPWD);
	}

	/**
	 * Open settings from recents list and check if contacts permission are checked on both devices.
	 * @throws InterruptedException
	 */
	@BeforeGroups("check_contacts_permission")
	private void checkContactPermissionChecked() throws InterruptedException{
		System.out.println("Check if contact permission is checked on device1.");
		RiotHomePageTabObjects homePage1=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotSettingsPageObjects settingsView1= homePage1.openRiotSettingsFromLateralMenu();
		settingsView1.checkContactsPermissionIfNecessary(true);
		settingsView1.actionBarBackButton.click();

		System.out.println("Check if contact permission is checked on device2.");
		RiotHomePageTabObjects homePage2=new RiotHomePageTabObjects(appiumFactory.getAndroidDriver1());
		RiotSettingsPageObjects settingsView2= homePage2.openRiotSettingsFromLateralMenu();
		settingsView2.checkContactsPermissionIfNecessary(true);
		settingsView2.actionBarBackButton.click();
	}
}
