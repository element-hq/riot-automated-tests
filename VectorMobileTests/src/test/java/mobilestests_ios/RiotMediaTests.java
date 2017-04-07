package mobilestests_ios;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.RiotMediaViewerPageObjects;
import pom_ios.RiotRoomDetailsPageObjects;
import pom_ios.RiotRoomPageObjects;
import pom_ios.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

/**
 * Tests on medias: photo, video, gif.
 * @author jeangb
 */
@Listeners({ ScreenshotUtility.class })
public class RiotMediaTests extends RiotParentTest{
private String riotUserDisplayName="riotuser17";
private String roomWithPhoto="attached photos";

	/**
	 * 1. Open room roomtest.
	 * 2. Open room details, then FILES tab.
	 * 3. Hit a file item.
	 * Check that the photo is opened in the image viewer.
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_ios","checkuser"})
	public void openPhotoFromFilesTabTest() throws InterruptedException{
		RiotRoomsListPageObjects roomsList1 = new RiotRoomsListPageObjects(appiumFactory.getiOsDriver1());
		//1. Open room roomtest.
		roomsList1.getRoomByName(roomWithPhoto).click();
		RiotRoomPageObjects roomPage1 = new RiotRoomPageObjects(appiumFactory.getiOsDriver1());
		//2. Open room details, then FILES tab.
		roomPage1.openDetailView();
		RiotRoomDetailsPageObjects roomDetailsPage1 = new RiotRoomDetailsPageObjects(appiumFactory.getiOsDriver1());
		roomDetailsPage1.filesTab.click();
		//3. Hit a file item.
		if(roomDetailsPage1.attachedFilesList.size()>0){
			roomDetailsPage1.attachedFilesList.get(0).click();
		}else{
			Assert.fail("No files attached in this room, at least 1 is exepected for the test");
		}
		//Check that the photo is opened in the image viewer.
		RiotMediaViewerPageObjects mediaViewer = new RiotMediaViewerPageObjects(appiumFactory.getiOsDriver1());
		Assert.assertTrue(mediaViewer.mediaViewerBody.isDisplayed(), "The media viewer isn't displayed");
		//appiumFactory.getiOsDriver1().
		mediaViewer.close();
		//come back to rooms list
		roomDetailsPage1.menuBackButton.click();
		roomPage1.menuBackButton.click();
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
	@BeforeGroups("checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
