package utility;

import java.io.FileNotFoundException;

import com.esotericsoftware.yamlbeans.YamlException;

/**
 * Functions only relatives to matrix.org
 * @author jeangb
 */
public class MatrixUtilities {

	public static String getMatrixIdFromDisplayName(String displayName){
		try {
			return new StringBuilder("@").append(displayName).append(":").append(ReadConfigFile.getInstance().getDevicesMap().get("androiddevice1")).toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (YamlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getCustomHomeServerURL(){
		String address = null,port = null;
		try {
			address = ReadConfigFile.getInstance().getConfMap().get("homeserver_address");
			port=ReadConfigFile.getInstance().getConfMap().get("homeserver_port");
		} catch (FileNotFoundException | YamlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new StringBuilder("https://").append(address).append(":").append(port).toString();
	}
}
