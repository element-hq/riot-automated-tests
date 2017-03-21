package mobilestests_ios;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import pom_ios.RiotContactPickerPageObjects;
import pom_ios.RiotRoomDetailsPageObjects;
import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
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
	private String invitedUser="@riotuser16:matrix.org";
	private String riotUserDisplayName="riotuser15";
	
	/**
	 * 1. Open room testRoom and open his details, then people tab.
	 * Check that text of the filter edittext is correct.
	 * Check that the filter button isn't present
	 * 2. Enter a filter in the "Filter room members" edittext.
	 * Check that the people are correctly filtered.
	 * Check that the filter button is present
	 * 3. Clear the filter
	 * Check that the people are no more filtered
	 * 4. Filter with a random string
	 * Check that the list of filtered members is empty and no results textview is displayed.
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_ios","1checkuser"})
	public void useFilterFieldOnPeopleTabTest() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomFilter=(new StringBuilder("filter_").append(randInt1)).toString();
		
		//1. Open room testRoom and open his details.
		RiotRoomsListPageObjects roomList1=new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		roomList1.getRoomByName(testRoom).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomDetailsPageObjects roomsDetails1=roomPage1.openDetailView();
		
		//Check that text of the filter edittext is correct.
		Assert.assertEquals(roomsDetails1.searchInviteBarView.findElementByClassName("XCUIElementTypeSearchField").getAttribute("label"),"Filter room members","Text on the Filter Room Members is not correct");
		//Check that the filter button isn't present
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getiOsDriver1(), "Cancel", false , 0));
		
		//2. Enter a filter in the "Filter room members" edittext.
		roomsDetails1.filterOnRoomMembersList(matchingWithKnownContactFilter1);
		appiumFactory.getiOsDriver1().hideKeyboard();
		//Check that the people are correctly filtered.
		for (MobileElement member : roomsDetails1.membersList) {
			Assert.assertTrue(roomsDetails1.getDisplayNameOfMemberFromPeopleTab(member).contains(matchingWithKnownContactFilter1), "A display name of a member doesn't have the filtered word in");	
		}
		int nbFilteredMembers=roomsDetails1.membersList.size();
		//Check that the filter button is present
		Assert.assertTrue(waitUntilDisplayed(appiumFactory.getiOsDriver1(), "Clear text",true , 0));
	
		//3. Clear the filter
		roomsDetails1.clearFilteredBarButton.click();
		//Check that the people are no more filtered
		int nbMembers=roomsDetails1.membersList.size();
		Assert.assertTrue(nbMembers>nbFilteredMembers, "Clear filter button doesn't seem to work");
		
		//4. Filter with a random string
		roomsDetails1.filterOnRoomMembersList(randomFilter);
		//Check that the list of filtered members is empty and no result textview is displayed
		Assert.assertEquals(roomsDetails1.membersList.size(), 0);
		
		//back to rooms list
		roomsDetails1.menuBackButton.click();
		roomPage1.menuBackButton.click();
	}
	
	/**
	 * 1. Open room testRoom and open his details, then people tab.
	 * 2. Hit the addMember button
	 * Check that the ContactPicker page is open and check the default layout
	 * 3. Enter a random string in the search bar
	 * Check that the text of the first item is equal to the random string
	 * Check that the item of the LOCAL CONTACTS categorie is (0)
	 * Check that the item of the KNOWN CONTACTS categorie is (0) https://github.com/vector-im/riot-ios/issues/1017
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_ios","1checkuser"})
	public void contactPickerWithRandomSearchTest() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomContactName=(new StringBuilder("contact_").append(randInt1)).toString();

		//1. Open room testRoom and open his details.
		RiotRoomsListPageObjects roomList1=new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		roomList1.getRoomByName(testRoom).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomDetailsPageObjects roomsDetails1=roomPage1.openDetailView();
		
		//2. Hit the addMember button
		roomsDetails1.addParticipantButton.click();
		//Check that the ContactPicker page is open
		RiotContactPickerPageObjects contactPicker1 = new RiotContactPickerPageObjects(appiumFactory.getiOsDriver1());
		contactPicker1.checkDefaultLayout();
		
		//3. Enter a random string in the search bar
		contactPicker1.searchMemberEditText.setValue(randomContactName);
		//Check that the text of the first item is equal to the random string
		Assert.assertEquals(contactPicker1.getDisplayNameOfMemberFromContactPickerList(contactPicker1.contactsOnlyList.get(0)), randomContactName);
		//Check that there is no KNOWN CONTACTS categorie
		Assert.assertEquals(contactPicker1.getCategoriesList().size(), 2, "There is more than 2 categories.");
		//Check that the item of the LOCAL CONTACTS categorie is (0)
		Assert.assertEquals(contactPicker1.getCategoriesList().get(0).findElementsByClassName("XCUIElementTypeStaticText").get(0).getText(), "LOCAL CONTACTS (0)");
		Assert.assertEquals(contactPicker1.getCategoriesList().get(1).findElementsByClassName("XCUIElementTypeStaticText").get(0).getText(), "KNOWN CONTACTS (0)");
		Assert.assertEquals(contactPicker1.contactsOnlyList.size(), 1, "There is too much members found with a random string.");
		
		//back to rooms list
		contactPicker1.cancelButton.click();
		roomsDetails1.menuBackButton.click();
		roomPage1.menuBackButton.click();
	}
	
	/**
	 * 1. Open room testRoom and open his details, then people tab.
	 * 2. Hit the addMember button
	 * 3. Enter in the search bar a word matching known contacts
	 * Check that KNOWN CONTACTS categorie is displayed
	 * Check that known contacts are as many as the number indicated in the category
	 * Check that there is at least 2 filtered people
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","1checkuser"})
	public void contactPickerWithMatchingSearchOnKnownContact() throws InterruptedException{
		//1. Open room testRoom and open his details.
		RiotRoomsListPageObjects roomList1=new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		roomList1.getRoomByName(testRoom).click();
		RiotRoomPageObjects roomPage1=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomDetailsPageObjects roomsDetails1=roomPage1.openDetailView();
		
		//2. Hit the addMember button
		roomsDetails1.addParticipantButton.click();
		//Check that the ContactPicker page is open
		RiotContactPickerPageObjects contactPicker1 = new RiotContactPickerPageObjects(appiumFactory.getiOsDriver1());
		contactPicker1.checkDefaultLayout();
		
		//3. Enter in the search bar a word matching known contacts
		contactPicker1.searchMemberEditText.setValue(matchingWithKnownContactFilter1);
		//Check that the text of the first item is equal to the random string
		Assert.assertEquals(contactPicker1.getDisplayNameOfMemberFromContactPickerList(contactPicker1.contactsOnlyList.get(0)), matchingWithKnownContactFilter1);
		//Check that KNOWN CONTACTS categorie is displayed
		Assert.assertEquals(contactPicker1.getCategoriesList().size(), 2, "There is more than 2 categorie.");
		Assert.assertTrue(contactPicker1.getCategoriesList().get(0).findElementsByClassName("XCUIElementTypeStaticText").get(0).getText().matches("^LOCAL CONTACTS \\([0-9]*\\)$"));
		String knownContacts=contactPicker1.getCategoriesList().get(1).findElementsByClassName("XCUIElementTypeStaticText").get(0).getText();
		Assert.assertTrue(knownContacts.matches("^KNOWN CONTACTS \\([^0][0-9]*\\)$"));
		//Check that there is at least 2 filtered people
		Assert.assertTrue(contactPicker1.contactsOnlyList.size()>=2, "There not enough members in the list after filtering with matching word.");
		
		//back to rooms list
		contactPicker1.cancelButton.click();
		roomsDetails1.menuBackButton.click();
		roomPage1.menuBackButton.click();
	}
	
	/**
	 * 1. Create a room.
	 * 2. Invite a participant
	 * 3. Remove this participant from the room details
	 * Check that there is no more INVITED category
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_ios","1checkuser"})
	public void inviteAndCancelInvitationTest() throws InterruptedException{
//		//1. Create a room.
		RiotRoomsListPageObjects roomList1=new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		RiotRoomPageObjects roomPage=roomList1.createRoom();
		
		//2. Invite a participant
		roomPage.inviteMembersLink.click();
		RiotRoomDetailsPageObjects roomDetails1 = new RiotRoomDetailsPageObjects(appiumFactory.getiOsDriver1());
		roomDetails1.addParticipant(invitedUser);
		roomDetails1.waitUntilInvitedCategorieIsDisplayed(true);
		
		//3. Remove this participant from the room details
		roomDetails1.removeMemberWithSwipeOnItem(roomDetails1.membersList.get(1));
		//roomDetails1.waitUntilDetailsDone();
		roomDetails1.waitUntilInvitedCategorieIsDisplayed(false);
		
		//4. Check that there is no more INVITED category
		Assert.assertEquals(roomDetails1.membersList.size(), 1, "Invited category is still here");
	}
	
	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException{
		switch (m.getName()) {
		case "inviteAndCancelInvitationTest":
			leaveRoomFromRoomDetailsPageAfterTest("Empty room");
			break;
		}
	}

	private void leaveRoomFromRoomDetailsPageAfterTest(String roomName){
		RiotRoomDetailsPageObjects roomDetails1=new RiotRoomDetailsPageObjects(appiumFactory.getiOsDriver1());
		roomDetails1.menuBackButton.click();
		RiotRoomPageObjects roomPage=new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		roomPage.leaveRoom();
		System.out.println("Leave room "+roomName+ " with device 1");
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUser1Logged() throws InterruptedException{
		checkIfUserLoggedIos(appiumFactory.getiOsDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
