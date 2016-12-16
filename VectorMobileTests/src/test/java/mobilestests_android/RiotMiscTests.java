package mobilestests_android;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import pom_android.RiotLegalStuffView;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import pom_android.RiotSearchFromRoomPageObjects;
import utility.AppiumFactory;
import utility.Constant;
import utility.DataproviderClass;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotMiscTests extends RiotParentTest{

	public static AppiumDriver<MobileElement> driver;

	Dimension size;
	String destDir;
	DateFormat dateFormat;

	public void restardDriver() throws MalformedURLException{
		System.out.println("teardown after test");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		AppiumFactory.getAndroidDriver1().quit();
		capabilities.setCapability("deviceName","a71011c8");
		capabilities.setCapability("platformName","Android");
		capabilities.setCapability("platformVersion", "4.4.2");

		capabilities.setCapability("appPackage", Constant.PACKAGE_APP_NAME);
		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");

		//Create RemoteWebDriver instance and connect to the Appium server
		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
		//driver = (AndroidDriver) new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities); 
	}

	@Test(groups="1driver")
	public void switchOrientationMode() throws InterruptedException{
		driver.rotate(ScreenOrientation.LANDSCAPE);//Thread.sleep(2000);
		(new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.FrameLayout")));
		scrollWindowDown();
		driver.rotate(ScreenOrientation.PORTRAIT);//Thread.sleep(2000);
		(new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.FrameLayout")));
		scrollWindowDown();
		driver.rotate(ScreenOrientation.LANDSCAPE);//Thread.sleep(2000);
		(new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.FrameLayout")));
		scrollWindowDown();
	}
	
	@Test(groups="1driver")
	public void scrollRoomsList() throws Exception{
		//RiotRoomsListPageObjects mainPage=new RiotRoomsListPageObjects(AppiumFactory.getAppiumDriver());
		Dimension dimensions = AppiumFactory.getAndroidDriver1().manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.9;
		int scrollStart = screenHeightStart.intValue();
		System.out.println("s="+scrollStart);
		Double screenHeightEnd = dimensions.getHeight() * 0.2;
		int scrollEnd = screenHeightEnd.intValue();
		AppiumFactory.getAndroidDriver1().swipe(0,scrollStart,0,scrollEnd,2000);
	}


	/**
	 * Pre-condition: being in the room list.\n
	 * Detail: Enters in a room and post a message then come back in the main view.
	 * @throws Exception 
	 */
	@Test(groups="1driver")
	public void chatInTempRoom() throws Exception{
		String testRoomName = "#dm16:matrix.org";
		String testMessage1 = "this is an automated test on 1 line";
		String testMessage2 = "this is an automated test on 2 lines: \n here's the second line";

		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(AppiumFactory.getAndroidDriver1());
		mainPage.getRoomByName(testRoomName).click();
		RiotRoomPageObjects roomPage = new RiotRoomPageObjects(AppiumFactory.getAndroidDriver1());
		roomPage.messageZoneEditText.sendKeys(testMessage1);
		roomPage.sendMessageButton.click();
		roomPage.messageZoneEditText.sendKeys(testMessage2);
		roomPage.sendMessageButton.click();
		roomPage.menuBackButton.click();
	}

	/**
	 * Open all rooms context menus.
	 * @throws Exception
	 */
	@Test(groups="1driver")
	public void openRoomsMenuContexts() throws Exception {
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(driver);

		System.out.println("nb rooms"+ mainPage.roomsList.size());
		for (WebElement room : mainPage.roomsList) {
			MobileElement roomSummary=(MobileElement) room.findElement(By.id("im.vector.alpha:id/roomSummaryAdapter_action_image"));
			roomSummary.click();
			((AndroidDeviceActionShortcuts) driver).pressKeyCode(AndroidKeyCode.BACK);
		}
	}


	/**
	 * Search for a room, open it then come back to the main view.
	 * @throws Exception
	 */
	@Test(groups="1driver")
	public void searchForRoom() throws Exception{
		String roomTestName="temp room";
		//NEW WAY
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(driver);
		mainPage.searchButton.click();
		RiotSearchFromRoomPageObjects searchPage= new RiotSearchFromRoomPageObjects(driver);
		searchPage.searchEditText.setValue(roomTestName);
		searchPage.getRoomByName(roomTestName).click();
		RiotRoomPageObjects myRoom=new RiotRoomPageObjects(driver);
		myRoom.menuBackButton.click();
	}
	
	/**
	 * Open a room, then quote the last message entered
	 * @throws Exception
	 */
	@Test(groups="1driver")
	public void quoteLastMessage() throws Exception{
		String testRoomName = "temp room";

		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(driver);
		mainPage.getRoomByName(testRoomName).click();
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(driver);
		MobileElement lastMessage = myRoom.getLastPost();
		lastMessage.click();
		myRoom.getContextMenuByPost(lastMessage).click();
		myRoom.quoteItemFromMenu.click();
	}

	/**
	 * Open a room, start a call, end it and come back to the main view.
	 * @throws InterruptedException
	 */
	@Test(groups="1driver")
	public void voiceCallFromRoomTest() throws InterruptedException{
		String testRoomName = "temp room";

		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(driver);
		mainPage.getRoomByName(testRoomName).click();
		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(driver);
		myRoom.startCallButton.click();
		myRoom.voiceCallFromMenuButton.click();
		Thread.sleep(3000);
		//back button
		((AndroidDeviceActionShortcuts) driver).pressKeyCode(AndroidKeyCode.BACK);
		myRoom.endCallButton.click();
		myRoom.menuBackButton.click();
	}


	@Test(groups="1driver")
	public void takeScreenShot() throws Exception{
		// Set folder name to store screenshots.
		destDir = "screenshots";
		// Capture screenshot.
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		// Set date format to set It as screenshot file name.
		dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
		// Create folder under project with name "screenshots" provided to destDir.
		new File(destDir).mkdirs();
		// Set file name using current date time.
		String destFile = dateFormat.format(new Date()) + ".png";

		try {
			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Test qui va planter intentionnelement.
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@Test(groups="1driver")
	public void checkUserDisplayName() throws IOException, InterruptedException{
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(driver);
		mainPage.contextMenuButton.click();
		Assert.assertEquals(mainPage.displayedUserMain.getText(), "Roger");
	}

	/**
	 * Open all the legal stuff webiews
	 * @throws InterruptedException 
	 */
	@Test(dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class,groups="1driver")
	public void openLegalStuffFromPortraitAndLandscapeMode(String items, String expectedTitle) throws InterruptedException{
		RiotRoomsListPageObjects mainPage = new RiotRoomsListPageObjects(driver);
		driver.rotate(ScreenOrientation.PORTRAIT);
		mainPage.contextMenuButton.click();
		mainPage.getItemMenuByName(items).click();
		RiotLegalStuffView copyrightPolicyView= new RiotLegalStuffView(driver);
		Assert.assertTrue(copyrightPolicyView.isPresentTryAndCatch(), "Copyright Policy view isn't open");
		Assert.assertEquals(copyrightPolicyView.secondTitle.getAttribute("name"), expectedTitle);
		copyrightPolicyView.okButton.click();

		//open it again and turn the device in landscape mode : the panel should be still displayed
		mainPage.contextMenuButton.click();Thread.sleep(1000);
		mainPage.openCopyrightButton.click();
		copyrightPolicyView= new RiotLegalStuffView(driver);
		driver.rotate(ScreenOrientation.LANDSCAPE);
		Thread.sleep(1500);
		Assert.assertTrue(copyrightPolicyView.isPresentTryAndCatch(), items+" view isn't open");
		//copyrightPolicyView.okButton.click();
		driver.rotate(ScreenOrientation.PORTRAIT);
	}

	private void scrollWindowDown(){
		Dimension dimensions = driver.manage().window().getSize();
		Double screenHeightStart = dimensions.getHeight() * 0.5;
		int scrollStart = screenHeightStart.intValue();
		System.out.println("s="+scrollStart);
		Double screenHeightEnd = dimensions.getHeight() * 0.2;
		int scrollEnd = screenHeightEnd.intValue();
		driver.swipe(0,scrollStart,0,scrollEnd,2000);
	}
}
