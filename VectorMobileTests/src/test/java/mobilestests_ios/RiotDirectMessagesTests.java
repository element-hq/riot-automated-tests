package mobilestests_ios;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on Direct Message for iOS.</br>
 * For now, it's impossible to check the DM picto so there will be fewer tests than on Android.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotDirectMessagesTests extends RiotParentTest{
	private String riotuser1DisplayName="riotuser6";
	private String riotuser2DisplayName="riotuser7";
//	private String riotuser3DisplayName="riotuser8";
//	private String riotuser3MatrixId="@riotuser8:matrix.org";
//	private String tmpRoomName="tmp room DM";
	
	/**
	 * Cover this issue https://github.com/vector-im/riot-ios/issues/836 </br>
	 * 1. Start a chat with someone on device/user A </br>
	 * 2. Accept the invitation with device/user B </br>
	 * Verify that call button in the right bottom is present </br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"2drivers_ios","22checkuser"})
	public void checkCallButtonPresenceAfterInvitedUserJoinTest() throws InterruptedException{
		RiotRoomsListPageObjects roomsListA = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomsListPageObjects roomsListB = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver2());
		//1. Start a chat with someone on device/user A
		RiotRoomPageObjects roomPageA=roomsListA.startChatWithUser(getMatrixIdFromDisplayName(riotuser2DisplayName));
		
		//2. Accept the invitation with device/user B
		roomsListB.previewInvitation(riotuser1DisplayName);
		RiotRoomPageObjects roomPageB = new RiotRoomPageObjects(appiumFactory.getiOsDriver2());
		roomPageB.joinRoomButton.click();
		
		//Verify that call button in the right bottom is present
		Assert.assertTrue(roomPageA.voiceCallButton.isDisplayed(), "Voice call button isn't here.");
	}
	
	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException{
		switch (m.getName()) {
		case "checkCallButtonPresenceAfterInvitedUserJoinTest":
			leaveRoomOn2DevicesFromRoomPageAfterTest(riotuser2DisplayName,riotuser1DisplayName);
			break;
		default:
			break;
		}
	}

//	private void leaveRoomOn1DeviceFromRoomPageAfterTest(String roomNameFromDevice1) throws InterruptedException{
//		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
//		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
//		roomPage1.leaveRoom();
//		RiotRoomsListPageObjects roomsList1= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
//		Assert.assertNull(roomsList1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
//	}

	private void leaveRoomOn2DevicesFromRoomPageAfterTest(String roomNameFromDevice1, String roomNameFromDevice2) throws InterruptedException{
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomPageObjects roomPage2 = new RiotRoomPageObjects(appiumFactory.getiOsDriver2());
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 1");
		roomPage1.leaveRoom();
		System.out.println("Leave room "+roomNameFromDevice1+ " with device 2");
		roomPage2.leaveRoom();

		//asserts that the DM rooms are really left
		if(roomNameFromDevice1!=null){
			RiotRoomsListPageObjects roomsList1= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
			Assert.assertNull(roomsList1.getRoomByName(roomNameFromDevice1), "Room "+roomNameFromDevice1+" is still displayed in the list in device 1.");
		}
		if(roomNameFromDevice1!=null){
			RiotRoomsListPageObjects roomsList2= new RiotRoomsListPageObjects(appiumFactory.getiOsDriver2());
			Assert.assertNull(roomsList2.getRoomByName(roomNameFromDevice2), "Room "+roomNameFromDevice2+" is still displayed in the list in device 1.");
		}
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
	@BeforeGroups("2checkuser")
	private void checkIfUsersLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotuser1DisplayName, Constant.DEFAULT_USERPWD);
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver2(), riotuser2DisplayName, Constant.DEFAULT_USERPWD);
	}
}
