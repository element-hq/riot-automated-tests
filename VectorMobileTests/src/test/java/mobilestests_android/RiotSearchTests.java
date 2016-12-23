package mobilestests_android;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import pom_android.RiotSearchFromRoomsListPageObjects;
import utility.AppiumFactory;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotSearchTests extends RiotParentTest{
	
	/**
	 * 1. Create a room with a random name.
	 * 2. Post a random msg within
	 * 3. From the rooms list hit the search button
	 * 4. Search in ROOMS tab the random name given in step1
	 * Check that the room previously created shown up
	 * Check that the room previously created doesn't appears in browse directory 
	 * 5. Search in MESSAGES tab the random msg given in step2
	 * Check that the random msg is shown up.
	 * @throws InterruptedException 
	 */
	@Test(groups="1driver", description="test on the search from the rooms list")
	public void searchRoomsAndMessages() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		int randInt2 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomRoomName=(new StringBuilder("room_search").append(randInt1)).toString();
		String randomMsg=(new StringBuilder("msg_search").append(randInt2)).toString();
		
		//1. Create a room with a random name.
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		RiotRoomPageObjects newRoom= roomsList.createRoom();
		newRoom.changeRoomName(randomRoomName);
		
		//2. Post a random msg within
		newRoom.sendAMessage(randomMsg);
		
		//3. From the rooms list hit the search button
		newRoom.menuBackButton.click();
		roomsList.searchButton.click();
		
		//4. Search in ROOMS tab the random name given in step1
		RiotSearchFromRoomsListPageObjects searchInRoomsList = new RiotSearchFromRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		searchInRoomsList.roomsTab.click();
		searchInRoomsList.launchASearch(randomRoomName,true);
		
		//Check that only 1 result shown up and Check that the room previously created shown up
		Assert.assertEquals(searchInRoomsList.getRoomsLayout().size(), 1);
		searchInRoomsList.checkRoomItemFromResult(1,randomRoomName,randomMsg);
		//Check that the room previously created doesn't appears in browse directory
		waitUntilPropertyIsSet(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage"), "text", "Searching directory..", false, 20);
		Assert.assertEquals(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage").getText(), "0 room found for "+randomRoomName);
		
		//5. Search in MESSAGES tab the random msg given in step2
		searchInRoomsList.messagesTab.click();
		searchInRoomsList.launchASearch(randomMsg,true);
		//Check that only 1 result shown up and Check that the random msg is shown up.
		Assert.assertEquals(searchInRoomsList.listMessagesLinearLayouts.size(), 1);
		searchInRoomsList.checkMessageItemFromResult(0, randomRoomName,null,randomMsg);
		
		//teardown : leave room
		searchInRoomsList.menuBackButton.click();
		roomsList.clickOnContextMenuOnRoom(randomRoomName, "Leave Conversation");
	}
	
	/**
	 * 1. From the rooms list hit the search button
	 * 2. Search in ROOMS tab a random name
	 * Check that none room is found and the search doesn't appears in browse directory
	 * 3. Search in MESSAGES tab a random name
	 * Check that "No results" is displayed after search finished.
	 * 4. Search in PEOPLE tab a random name
	 * Check that "No results" is displayed after search finished.
	 * 5. Search in FILES tab a random name
	 * Check that "No results" is displayed after search finished.
	 * @throws InterruptedException 
	 */
	@Test(groups="1driver", description="test on the search from the rooms list with non existent searches")
	public void searchForNonExistentMsgAndRoom() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomName=(new StringBuilder("randomsearch_").append(randInt1)).toString();
		
		//1. From the rooms list hit the search button
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		roomsList.searchButton.click();
		RiotSearchFromRoomsListPageObjects searchInRoomsList = new RiotSearchFromRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		
		//2. Search in ROOMS tab a random name
		searchInRoomsList.roomsTab.click();
		searchInRoomsList.launchASearch(randomName,true);
		//Check that none room is found and the search doesn't appears in browse directory
		waitUntilPropertyIsSet(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage"), "text", "Searching directory..", false, 20);
		Assert.assertEquals(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage").getText(), "0 room found for "+randomName);
		Assert.assertEquals(searchInRoomsList.getRoomsLayout().size(), 0,"Room(s) in results and it shouldn't be with search "+randomName);
		
		//3. Search in MESSAGES tab a random name
		searchInRoomsList.messagesTab.click();
		searchInRoomsList.waitUntilSearchFinished();
		//Check that "No results" is displayed after search finished.
		Assert.assertEquals(searchInRoomsList.noResultTextView.getText(), "No Results");
		
		//4. Search in PEOPLE tab a random name
		searchInRoomsList.peopleTab.click();
		searchInRoomsList.waitUntilSearchFinished();
		//Check that "No results" is displayed after search finished.
		Assert.assertEquals(searchInRoomsList.noResultTextView.getText(), "No Results");
		
		//5. Search in FILES tab a random name
		searchInRoomsList.filesTab.click();
		searchInRoomsList.waitUntilSearchFinished();
		//Check that "No results" is displayed after search finished.
		Assert.assertEquals(searchInRoomsList.noResultTextView.getText(), "No Results");
		//coming back in the rooms list
		searchInRoomsList.menuBackButton.click();
	}
}
