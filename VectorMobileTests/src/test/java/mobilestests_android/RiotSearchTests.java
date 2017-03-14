package mobilestests_android;

import org.openqa.selenium.ScreenOrientation;
import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import pom_android.RiotSearchFromRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotSearchTests extends RiotParentTest{
	private String riotUserDisplayName="riotuser15";
	
	/**
	 * Search in ROOMS and MESSAGES tab in search from recent.</br> 
	 * Validate the issue https://github.com/vector-im/riot-android/issues/934 to. </br>
	 * 1. Create a room with a random name.</br>
	 * 2. Post a random msg within</br>
	 * 3. From the rooms list hit the search button</br>
	 * 4. Search in ROOMS tab the random name given in step1</br>
	 * Check that the room previously created shown up</br>
	 * Check that the room previously created doesn't appears in browse directory </br>
	 * 5. Search in MESSAGES tab the random msg given in step2</br>
	 * Check that the random msg is shown up.</br>
	 * 6. Turn the device in landscape mode, then portrait</br>
	 * Check that the search result is still displayed https://github.com/vector-im/riot-android/issues/934</br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=0,description="test on the search from the rooms list")
	public void searchRoomsAndMessages() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		int randInt2 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomRoomName=(new StringBuilder("room_search").append(randInt1)).toString();
		String randomMsg=(new StringBuilder("msg_search").append(randInt2)).toString();
		
		//1. Create a room with a random name.
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		RiotRoomPageObjects newRoom= roomsList.createRoom();
		newRoom.changeRoomName(randomRoomName);
		
		//2. Post a random msg within
		newRoom.sendAMessage(randomMsg);
		
		//3. From the rooms list hit the search button
		newRoom.menuBackButton.click();
		roomsList.searchButton.click();
		
		//4. Search in ROOMS tab the random name given in step1
		RiotSearchFromRoomsListPageObjects searchInRoomsList = new RiotSearchFromRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		searchInRoomsList.roomsTab.click();
		searchInRoomsList.launchASearch(randomRoomName,true);
		
		//Check that only 1 result shown up and Check that the room previously created shown up
		Assert.assertEquals(searchInRoomsList.getRoomsLayout(true).size(), 1);
		searchInRoomsList.checkRoomItemFromResult(1,randomRoomName,randomMsg);
		//Check that the room previously created doesn't appears in browse directory
		waitUntilPropertyIsSet(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage"), "text", "Searching directory..", false, 20);
		Assert.assertEquals(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage").getText(), "0 room found for "+randomRoomName);
		searchInRoomsList.clearSearchButton.click();
		
		//5. Search in MESSAGES tab the random msg given in step2
		searchInRoomsList.messagesTab.click();
		searchInRoomsList.launchASearch(randomMsg,true);
		//Check that only 1 result shown up and Check that the random msg is shown up.
		Assert.assertEquals(searchInRoomsList.listMessagesLinearLayouts.size(), 1);
		searchInRoomsList.checkMessageItemFromResult(0, randomRoomName,null,randomMsg);
		
		//6. Turn the device in landscape mode, then portrait
		searchInRoomsList.searchEditText.click();
		appiumFactory.getAndroidDriver1().rotate(ScreenOrientation.LANDSCAPE);
		Thread.sleep(1500);
		appiumFactory.getAndroidDriver1().rotate(ScreenOrientation.PORTRAIT);
		//Check that the search result is still displayed https://github.com/vector-im/riot-android/issues/934
		Assert.assertEquals(searchInRoomsList.listMessagesLinearLayouts.size(), 1);
		searchInRoomsList.checkMessageItemFromResult(0, randomRoomName,null,randomMsg);
		
		//teardown : leave room
		searchInRoomsList.menuBackButton.click();
		roomsList.leaveRoom(randomRoomName);
		Thread.sleep(500);
	}
	
	/**
	 * 1. From the rooms list hit the search button</br>
	 * 2. Search in ROOMS tab a random name</br>
	 * Check that none room is found and the search doesn't appears in browse directory</br>
	 * 3. Search in MESSAGES tab a random name</br>
	 * Check that "No results" is displayed after search finished.</br>
	 * 4. Search in PEOPLE tab a random name</br>
	 * Check that "No results" is displayed after search finished.</br>
	 * 5. Search in FILES tab a random name</br>
	 * Check that "No results" is displayed after search finished.</br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"}, priority=1,description="test on the search from the rooms list with non existent searches")
	public void searchForNonExistentMsgAndRoom() throws InterruptedException{
		int randInt1 = 1 + (int)(Math.random() * ((10000 - 1) + 1));
		String randomName=(new StringBuilder("randomsearch_").append(randInt1)).toString();
		
		//1. From the rooms list hit the search button
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		roomsList.searchButton.click();
		RiotSearchFromRoomsListPageObjects searchInRoomsList = new RiotSearchFromRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		
		//2. Search in ROOMS tab a random name
		searchInRoomsList.roomsTab.click();
		searchInRoomsList.launchASearch(randomName,true);
		//Check that none room is found and the search doesn't appears in browse directory
		waitUntilPropertyIsSet(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage"), "text", "Searching directory..", false, 20);
		Assert.assertEquals(searchInRoomsList.getBrowseDirectory().findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage").getText(), "0 room found for "+randomName);
		Assert.assertEquals(searchInRoomsList.getRoomsLayout(false).size(), 0,"Room(s) in results and it shouldn't be with search "+randomName);
		
		//3. Search in MESSAGES tab a random name
		searchInRoomsList.messagesTab.click();
		searchInRoomsList.waitUntilSearchFinished();
		//Check that "No results" is displayed after search finished.
		Assert.assertEquals(searchInRoomsList.noResultTextView.getText(), "No results");
		
		//4. Search in PEOPLE tab a random name
		searchInRoomsList.peopleTab.click();
		searchInRoomsList.waitUntilSearchFinished();
		//Check that the searched people is in the first and unique item of the results list
		Assert.assertEquals(searchInRoomsList.listPeopleResultWithCategorie.size(),1, "There is more than 1 item in the people result.");
		searchInRoomsList.checkPeopleItemFromResult(0, randomName,"");
		
		//5. Search in FILES tab a random name
		searchInRoomsList.filesTab.click();
		searchInRoomsList.waitUntilSearchFinished();
		//Check that "No results" is displayed after search finished.
		Assert.assertEquals(searchInRoomsList.noResultTextView.getText(), "No results");
		//coming back in the rooms list
		searchInRoomsList.menuBackButton.click();
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUserLogged() throws InterruptedException{
		super.checkIfUserLoggedAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
