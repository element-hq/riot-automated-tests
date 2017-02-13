package utility;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.ExecuteException;

import com.github.genium_framework.appium.support.server.AppiumServer;
import com.github.genium_framework.server.ServerArguments;

public class AppiumServerStartAndStop {
private static AppiumServer appiumServer1;
public static AppiumServer getAppiumServer1() {
	return appiumServer1;
}
public static AppiumServer getAppiumServer2() {
	return appiumServer2;
}
private static AppiumServer appiumServer2;
	public static  void startAppiumServer1() throws ExecuteException, IOException{

		ServerArguments serverArguments = new ServerArguments();
		serverArguments.setArgument("--address", Constant.SERVER1_ADDRESS);
		serverArguments.setArgument("-p", Constant.APPIUM_COMMON_PORT);
		appiumServer1 = new AppiumServer(new File(Constant.PATH_TO_NODE_DIRECTORY), new File(Constant.PATH_TO_APPIUM_JAVASCRIPT), serverArguments);
		appiumServer1.startServer();
	}
	public static  void startAppiumServer2() throws ExecuteException, IOException{
		ServerArguments serverArguments = new ServerArguments();
		serverArguments.setArgument("-a", Constant.SERVER2_ADDRESS);
		serverArguments.setArgument("--webdriveragent-port", Constant.WEBDRIVERAGENT_PORT);
		appiumServer2 = new AppiumServer(new File(Constant.PATH_TO_NODE_DIRECTORY), new File(Constant.PATH_TO_APPIUM_JAVASCRIPT), serverArguments);
		appiumServer2.startServer();
	}
	
	public static void startAppiumServer1IfNecessary() throws ExecuteException, IOException{
		if(null==appiumServer1){
			System.out.println("Appium Server 1 is not running, let's start it.");
			startAppiumServer1();
		}else if(!appiumServer1.isServerRunning()){
			System.out.println("Restarting Appium Server 1.");
			startAppiumServer1();
		}		
	}
	public static void startAppiumServer2IfNecessary() throws ExecuteException, IOException{
		if(null==appiumServer2){
			System.out.println("Appium Server 2 is not running, let's start it.");
			startAppiumServer2();
		}else if(!appiumServer2.isServerRunning()){
			System.out.println("Restarting Appium Server 2.");
			startAppiumServer2();
		}		
	}
	
	public static void stopAppiumServer1() throws ExecuteException, IOException{
		appiumServer1.stopServer();
	} 
	public static void stopAppiumServer2() throws ExecuteException, IOException{
		appiumServer2.stopServer();
	} 
}
