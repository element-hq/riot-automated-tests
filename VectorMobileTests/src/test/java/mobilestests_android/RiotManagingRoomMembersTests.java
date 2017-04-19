package mobilestests_android;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import io.appium.java_client.MobileElement;
import pom_android.RiotContactPickerPageObjects;
import pom_android.RiotRoomDetailsPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on the members tab from the room details, and on the contact picker.
 * @author jeang
 *
 */
@Listeners({ ScreenshotUtility.class })
public class RiotManagingRoomMembersTests extends RiotParentTest{
	private String testRoom="Common riotusers auto tests";
	private String matchingWithKnownContactFilter1="riot";
	private String invitedUserDisplayName="riotuser16";
	private String riotUserDisplayName="riotuser15";

	/**
	 * 1. Open room testRoom and open his details, then people tab. </br>
	 * Check that text of the filter edittext is correct. </br>
	 * Check that the filter button isn't present </br>
	 * 2. Enter a filter in the "Filter room members" edittext. </br>
	 * Check that the people are correctly filtered. </br>
	 * Check that the filter button is present </br>
	 * 3. Clear the filter </br>
	 * Check that the people are no more filtered </br>
	 * 4. Filter with a random string </br>
	 * Check that the list of filtered members is empty and no results textview is displayed. </br>
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_android","1checkuser"})
	public void useFilterFieldOnPeopleTabTest() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomFilter=(new StringBuilder("filter_").append(randInt1)).toString();

		//1. Open room testRoom and open his details.
		RiotRoomsListPageObjects roomsList1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		roomsList1.getRoomByName(testRoom).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage1.collapseChatButton.click();
		roomPage1.activeMembersTextView.click();
		RiotRoomDetailsPageObjects roomDetails1=new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());

		//Check that text of the filter edittext is correct.
		Assert.assertEquals(roomDetails1.filterRoomMembersEditText.getText(), "Filter room members","Text on the Filter Room Members is not correct");
		//Check that the filter button isn't present
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getAndroidDriver1(), "im.vector.alpha:id/clear_search_icon_image_view",false , 0));

		//2. Enter a filter in the "Filter room members" edittext.
		roomDetails1.filterOnRoomMembersList(matchingWithKnownContactFilter1);
		appiumFactory.getAndroidDriver1().hideKeyboard();
		//Check that the people are correctly filtered.
		for (MobileElement member : roomDetails1.membersList) {
			String memberDisplayName=roomDetails1.getDisplayNameOfMemberFromPeopleTab(member);
			if(null!=memberDisplayName)Assert.assertTrue(memberDisplayName.contains(matchingWithKnownContactFilter1), "A display name of a member doesn't have the filtered word in");	
		}
		//Check that the filter button is present
		Assert.assertTrue(waitUntilDisplayed(appiumFactory.getAndroidDriver1(), "im.vector.alpha:id/clear_search_icon_image_view",true , 0));

		//3. Clear the filter
		roomDetails1.clearFilteredBarButton.click();
		//Check that the people are no more filtered
		//Assert.assertFalse(roomDetails1.getDisplayNameOfMemberFromPeopleTab(roomDetails1.membersList.get(0)).contains(matchingWithKnownContactFilter1),"After filter cleared, first result still matches the matching filter string");

		//4. Filter with a random string
		roomDetails1.filterOnRoomMembersList(randomFilter);
		//Check that the list of filtered members is empty and no result textview is displayed
		Assert.assertEquals(roomDetails1.membersList.size(), 0);
		Assert.assertTrue(roomDetails1.noResultTextView.isDisplayed(), "No result text view isn't displayed after filtering members list with random string.");
		Assert.assertEquals(roomDetails1.noResultTextView.getText(), "No results");

		//back to rooms list
		roomDetails1.menuBackButton.click();
		roomPage1.menuBackButton.click();
	}

	/**
	 * 1. Open room testRoom and open his details, then people tab. </br>
	 * 2. Hit the addMember button </br>
	 * Check that the ContactPicker page is open and check the default layout </br>
	 * 3. Enter a random string in the search bar </br>
	 * Check that the text of the first item is equal to the random string </br>
	 * Check that the item of the LOCAL CONTACTS categorie is (0) </br>
	 * Check that the item of the KNOWN CONTACTS categorie is (0) (https://github.com/vector-im/riot-android/issues/923)
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"})
	public void contactPickerWithRandomSearchTest() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomContactName=(new StringBuilder("contact_").append(randInt1)).toString();

		//1. Open room testRoom and open his details.
		RiotRoomsListPageObjects roomsList1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		roomsList1.getRoomByName(testRoom).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage1.collapseChatButton.click();
		roomPage1.activeMembersTextView.click();
		RiotRoomDetailsPageObjects roomDetails1=new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());

		//2. Hit the addMember button
		roomDetails1.addParticipantButton.click();
		//Check that the ContactPicker page is open
		RiotContactPickerPageObjects contactPicker1 = new RiotContactPickerPageObjects(appiumFactory.getAndroidDriver1());
		appiumFactory.getAndroidDriver1().hideKeyboard();
		contactPicker1.checkDefaultLayout();

		//3. Enter a random string in the search bar
		contactPicker1.searchMemberEditText.setValue(randomContactName);
		appiumFactory.getAndroidDriver1().hideKeyboard();
		//Check that the text of the first item is equal to the random string
		Assert.assertEquals(contactPicker1.getDisplayNameOfMemberFromContactPickerList(contactPicker1.detailsMemberListView.get(0)), randomContactName);
		//Check that there is no KNOWN CONTACTS categorie
		Assert.assertEquals(contactPicker1.categoryList.size(), 2, "There is more than 2 categorie.");
		//Check that the item of the LOCAL CONTACTS categorie is (0)
		Assert.assertEquals(contactPicker1.categoryList.get(0).findElementById("im.vector.alpha:id/people_header_text_view").getText(), "LOCAL CONTACTS (0)");
		Assert.assertEquals(contactPicker1.categoryList.get(1).findElementById("im.vector.alpha:id/people_header_text_view").getText(), "KNOWN CONTACTS (0)");
		Assert.assertEquals(contactPicker1.detailsMemberListView.size(), 1, "There is too much members found with a random string.");

		//back to rooms list
		contactPicker1.backButton.click();
		roomDetails1.menuBackButton.click();
		roomPage1.menuBackButton.click();
	}

	/**
	 * 1. Open room testRoom and open his details, then people tab. </br>
	 * 2. Hit the addMember button </br>
	 * 3. Enter in the search bar a word matching known contacts </br>
	 * Check that KNOWN CONTACTS categorie is displayed </br>
	 * Check that known contacts are as many as the number indicated in the category </br>
	 * Check that there is at least 2 filtered people
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"})
	public void contactPickerWithMatchingSearchOnKnownContact() throws InterruptedException{
		//1. Open room testRoom and open his details.
		RiotRoomsListPageObjects roomsList1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		roomsList1.getRoomByName(testRoom).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage1.collapseChatButton.click();
		roomPage1.activeMembersTextView.click();
		RiotRoomDetailsPageObjects roomDetails1=new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());

		//2. Hit the addMember button
		roomDetails1.addParticipantButton.click();
		//Check that the ContactPicker page is open
		RiotContactPickerPageObjects contactPicker1 = new RiotContactPickerPageObjects(appiumFactory.getAndroidDriver1());
		//contactPicker1.checkDefaultLayout();

		//3. Enter in the search bar a word matching known contacts
		contactPicker1.searchMemberEditText.setValue(matchingWithKnownContactFilter1);
		appiumFactory.getAndroidDriver1().hideKeyboard();

		//Check that the text of the first item is equal to the random string
		Assert.assertEquals(contactPicker1.getDisplayNameOfMemberFromContactPickerList(contactPicker1.detailsMemberListView.get(0)), matchingWithKnownContactFilter1);
		//Check that KNOWN CONTACTS categorie is displayed
		Assert.assertEquals(contactPicker1.categoryList.size(), 2, "There is more than 2 categorie.");
		Assert.assertTrue(contactPicker1.categoryList.get(0).findElementById("im.vector.alpha:id/people_header_text_view").getText().matches("^LOCAL CONTACTS \\([0-9]*\\)$"));
		String knownContacts=contactPicker1.categoryList.get(1).findElementById("im.vector.alpha:id/people_header_text_view").getText();
		Assert.assertTrue(knownContacts.matches("^KNOWN CONTACTS \\([^0][0-9]*\\)$"));
		//Check that there is at least 2 filtered people
		Assert.assertTrue(contactPicker1.detailsMemberListView.size()>=2, "There is not enough members in the list after filtering with matching word.");
		
		//back to rooms list
		contactPicker1.backButton.click();
		roomDetails1.menuBackButton.click();
		roomPage1.menuBackButton.click();
	}

	/**
	 * 1. Create a room. </br>
	 * 2. Invite a participant </br>
	 * 3. Remove this participant from the room details </br>
	 * Check that there is no more INVITED category </br>
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_android","1checkuser"})
	public void inviteAndCancelInvitationTest() throws InterruptedException{
		//1. Create a room.
		RiotRoomsListPageObjects mainPage1=new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomPageObjects roomPage=mainPage1.createRoom();

		//2. Invite a participant
		roomPage.inviteMembersButton.click();
		RiotRoomDetailsPageObjects roomDetails1=new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());
		roomDetails1.addParticipant(getMatrixIdFromDisplayName(invitedUserDisplayName));
		roomDetails1.waitUntilInvitedCategorieIsDisplayed(true);

		//3. Remove this participant from the room details
		roomDetails1.removeMemberWithSwipeOnItem(roomDetails1.membersList.get(1));
		roomDetails1.waitUntilInvitedCategorieIsDisplayed(false);

		//4. Check that there is no more INVITED category
		Assert.assertEquals(roomDetails1.categoryList.size(), 1, "Invited category is still here");
		//Go back to room list
		roomDetails1.menuBackButton.click();
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
	@BeforeGroups("1checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
	
	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException{
		switch (m.getName()) {
		case "inviteAndCancelInvitationTest":
			leaveRoomFromRoomPageAfterTest("Empty room");
			break;
		}
	}

	private void leaveRoomFromRoomPageAfterTest(String roomName){
		RiotRoomPageObjects roomPage=new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		roomPage.leaveRoom();
		System.out.println("Leave room "+roomName+ " with device 1");
	}

}
