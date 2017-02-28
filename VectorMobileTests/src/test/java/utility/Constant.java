package utility;

import java.io.FileNotFoundException;

import com.esotericsoftware.yamlbeans.YamlException;

public class Constant {
	public static final String FILE_TESTDATA = "testdata1.xlsx";
	/*
	 * APPIUM SERVERS
	 */
	public static final String SERVER1_ADDRESS="0.0.0.0";
	public static final String SERVER2_ADDRESS="127.0.0.1";
	public static final String APPIUM_COMMON_PORT="4723";
	public static final String WEBDRIVERAGENT_PORT="8080";
	public static final String SERVER1_HTTP_ADDRESS="http://"+SERVER1_ADDRESS+":"+APPIUM_COMMON_PORT+"/wd/hub";
	public static final String SERVER2_HTTP_ADDRESS="http://"+SERVER2_ADDRESS+":"+APPIUM_COMMON_PORT+"/wd/hub";
	
	public static final String getServer1HttpAddress() throws FileNotFoundException, YamlException{
		if("2"==ReadConfigFile.getInstance().getConfMap().get("starting_server_mode"))
			return AppiumServerStartAndStopService.service_url1;
		else
			return SERVER1_HTTP_ADDRESS;
	}
	
	public static final String getServer2HttpAddress() throws FileNotFoundException, YamlException{
		if("2"==ReadConfigFile.getInstance().getConfMap().get("starting_server_mode"))
			return AppiumServerStartAndStopService.service_url2;
		else
			return SERVER2_HTTP_ADDRESS;
	}
	
	/*
	 * CONF
	 */
	public static final String CONFIG_FILE = "src/test/java/config/config.yaml";
	public static final String DEVICES_CONFIG_FILE ="src/test/java/config/devices.yaml";
	
	/*
	 * RIOT
	 */
	public static final String PACKAGE_APP_NAME = "im.vector.alpha";
	public static final String APPLICATION_LOGIN_ACTIVITY="im.vector.activity.LoginActivity";
	public static final String APPLICATION_NAME = "RIOT";
	
	/*
	 * NODE JS AND APPIUM
	 */
	public static final String PATH_TO_NODE_DIRECTORY="/usr/local/bin/node";
	public static final String PATH_TO_APPIUM_JAVASCRIPT="/usr/local/lib/node_modules/appium/build/lib/main.js";
	/*
	 * MISC 
	 */
	public static final String PATH_TESTDATA = "src//test//resources//testDatas//";
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
