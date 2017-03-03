package utility;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AppiumServerStartAndStopService {
    static String Appium_Node_Path=Constant.PATH_TO_NODE_DIRECTORY;
    static String Appium_JS_Path=Constant.PATH_TO_APPIUM_JAVASCRIPT;
    static AppiumDriverLocalService service1;
    static AppiumDriverLocalService service2;
    static String service_url1;
    static String service_url2;
    static Field streamField1;
    static Field streamFields1;
    static Field streamField2;
    static Field streamFields2;

    public static void appiumServer1Start() throws Exception{
        service1 = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().
                usingPort(Integer.parseInt(Constant.APPIUM_COMMON_PORT)).usingDriverExecutable(new File(Appium_Node_Path)).
                withAppiumJS(new File(Appium_JS_Path)));
        service1.start();
        service_url1 = service1.getUrl().toString();
        System.out.println("URL:" +service_url1);
        
        //workaround for server logs displayed in the console
        try {
            streamField1 = AppiumDriverLocalService.class.getDeclaredField("stream");
            streamField1.setAccessible(true);
            streamFields1 = Class.forName("io.appium.java_client.service.local.ListOutputStream")
                    .getDeclaredField("streams");
            streamFields1.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            ((ArrayList<OutputStream>) streamFields1.get(streamField1.get(service1))).clear(); // remove System.out logging
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static void appiumServer2Start() throws Exception{
        service2 = AppiumDriverLocalService.buildService(new AppiumServiceBuilder().
        		withArgument(() -> "--webdriveragent-port", Constant.WEBDRIVERAGENT_PORT).
        		withIPAddress(Constant.SERVER2_ADDRESS).
                usingDriverExecutable(new File(Appium_Node_Path)).
                withAppiumJS(new File(Appium_JS_Path)));
        service2.start();
        service_url2 = service2.getUrl().toString();
        System.out.println("URL:" +service_url2);
        
        //workaround for server logs displayed in the console
        try {
            streamField2 = AppiumDriverLocalService.class.getDeclaredField("stream");
            streamField2.setAccessible(true);
            streamFields2 = Class.forName("io.appium.java_client.service.local.ListOutputStream")
                    .getDeclaredField("streams");
            streamFields2.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            ((ArrayList<OutputStream>) streamFields2.get(streamField2.get(service2))).clear(); // remove System.out logging
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
	public static void startAppiumServer1IfNecessary() throws Exception{
		if(null==service1){
			System.out.println("Appium Server 1 is not running, let's start it.");
			appiumServer1Start();
		}else if(!service1.isRunning()){
			System.out.println("Restarting Appium Server 1.");
			appiumServer1Start();
		}		
	}
	
	public static void startAppiumServer2IfNecessary() throws Exception{
		if(null==service2){
			System.out.println("Appium Server 2 is not running, let's start it.");
			appiumServer2Start();
		}else if(!service2.isRunning()){
			System.out.println("Restarting Appium Server 2.");
			appiumServer2Start();
		}		
	}

    public static void appiumServer1Stop() throws Exception{
        service1.stop();
    }
    public static void appiumServer2Stop() throws Exception{
        service2.stop();
    }
}
