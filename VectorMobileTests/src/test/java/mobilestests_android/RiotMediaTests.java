package mobilestests_android;

import java.io.FileNotFoundException;

import org.testng.Assert;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_android.RiotMediaViewerPageObjects;
import pom_android.RiotRoomDetailsPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.Constant;
import utility.RiotParentTest;

/**
 * Tests on medias: photo, video, gif.
 * @author jeangb
 */
public class RiotMediaTests extends RiotParentTest{
	private String riotUserDisplayName="riotuser17";
	private String roomWithPhoto="attached photos";

	/**
	 * 1. Open room roomtest. </br>
	 * 2. Open room details, then FILES tab. </br>
	 * 3. Hit a file item. </br>
	 * Check that the photo is opened in the image viewer. </br>
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android","1checkuser"})
	public void openPhotoFromFilesTabTest() throws InterruptedException{
		RiotRoomsListPageObjects roomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//1. Open room roomtest.
		roomsList.getRoomByName(roomWithPhoto).click();
		RiotRoomPageObjects roomPage = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		//2. Open room details, then FILES tab.
		roomPage.moreOptionsButton.click();
		roomPage.roomDetailsMenuItem.click();
		RiotRoomDetailsPageObjects detailsPage=new RiotRoomDetailsPageObjects(appiumFactory.getAndroidDriver1());
		detailsPage.filesTab.click();
		//3. Hit a file item.
		if(detailsPage.attachedFilesList.size()>0){
			detailsPage.attachedFilesList.get(0).click();
		}else{
			Assert.fail("No files attached in this room, at least 1 is exepected for the test");
		}
		//Check that the photo is opened in the image viewer.
		RiotMediaViewerPageObjects mediaViewer = new RiotMediaViewerPageObjects(appiumFactory.getAndroidDriver1());
		mediaViewer.close();
		//come back to rooms list
		roomPage.menuBackButton.click();
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 * @throws InterruptedException 
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpAndroid(appiumFactory.getAndroidDriver1(), riotUserDisplayName, Constant.DEFAULT_USERPWD);
	}
}
