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
				return new StringBuilder("@").append(displayName).append(":").append(ReadConfigFile.getInstance().getConfMap().get("homeserver")).toString();
			} catch (FileNotFoundException | YamlException e) {
				e.printStackTrace();
				return null;
			}
	}
	
	/**
	 * Return custom home server URL.</br>
	 * If fromLocal = true, return URL with 'localhost'.
	 * @param fromLocal
	 * @return
	 */
	public static String getCustomHomeServerURL(Boolean fromLocal){
		String address = null,port = null;
		try {
			address = ReadConfigFile.getInstance().getConfMap().get("homeserver_address");
			port=ReadConfigFile.getInstance().getConfMap().get("homeserver_port");
		} catch (FileNotFoundException | YamlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(fromLocal){
			return new StringBuilder("https://localhost:").append(port).toString();
		}else{
			return new StringBuilder("https://").append(address).append(":").append(port).toString();
		}		
	}
	
	/**
	 * According to homeserverlocal value in config.yaml, return default home server or custome one.
	 * @return
	 */
	public static String getHomeServerUrlForRequestToMatrix(){
		try {
			if("false".equals(ReadConfigFile.getInstance().getConfMap().get("homeserverlocal"))){
				return Constant.DEFAULT_MATRIX_SERVER_URL;
			}else{
				return getCustomHomeServerURL(true);
			}
		} catch (FileNotFoundException | YamlException e) {
			e.printStackTrace();
			return null;
		}
	}
}
