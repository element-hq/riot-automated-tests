package utility;

public class Constant {
	public static final String FILE_TESTDATA = "testdata1.xlsx";
	public static final String SERVER1_ADRESS="http://0.0.0.0:4723/wd/hub";
	public static final String SERVER2_ADRESS="http://127.0.0.1:4723/wd/hub";
	public static final String ANDROID_DEVICE1_NAME = "a71011c8";
	public static final String ANDROID_DEVICE2_NAME = "4df1a0ee7bf25fa3";
	/*** iPhone 6+ STZ_DE_13243.*/
	public static final String IOS_DEVICE1_UDID = "e75c0085c74a872846772a6b2ee56a86849a4d92";
	/*** iPhone 5C STZ_DE_13208.*/
	public static final String IOS_DEVICE2_UDID = "2a418a9dbcd960d904a501bf558120625f96f409";//1c7e0b4559589b57396a57f8eaa382c9bc42d8d7 //2a418a9dbcd960d904a501bf558120625f96f409 //99129ae17ce75bdc31f66f507725b2b98907a10d
	public static final String PATH_TESTDATA = "src//test//resources//testDatas//";
	public static final String PACKAGE_APP_NAME = "im.vector.alpha";
	public static final String APPLICATION_LOGIN_ACTIVITY="im.vector.activity.LoginActivity";
	public static final String APPLICATION_NAME = "RIOT";
	public static final String DEFAULT_USERNAME="riotuser2";
	public static final String DEFAULT_USERPWD="riotuser";
	public static final String DEFAULT_USERADRESS="@riotuser2:matrix.org";
	
	public static final String DEFAULT_MATRIX_SERVER="https://matrix.org";
	public static final String DEFAULT_IDENTITY_SERVER="https://vector.im";
	
	/*
	 * E2E ENCRYPTION
	 */
	public static final String ENCRYPTION_TURNEDON_EVENT="turned on end-to-end encryption";
	public static final String ENCRYPTION_UNKNOWN_SESSION_ID_MSG="** Unable to decrypt: The sender's device has not sent us the keys for this message. **";
}
