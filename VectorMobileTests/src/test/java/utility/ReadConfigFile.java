package utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;

/**
 * Read conf datas and store it in java objects. </br> As it's a singleton class, conf files are parsed just once.
 * @author jeangb
 *
 */
public class ReadConfigFile {
	private static ReadConfigFile INSTANCE=null;
	private Map<String, Map<String, String>> devicesMap;
	private Map<String, String> confMap;

	public Map<String, String> getConfMap() {
		return confMap;
	}
	public Map<String, Map<String, String>> getDevicesMap() {
		return devicesMap;
	}
	
	/**
	 * Constructor
	 * @throws FileNotFoundException 
	 * @throws YamlException 
	 */
	private ReadConfigFile() throws FileNotFoundException, YamlException{
		readMainConfFile();
		readDecicesConfFile();
	}
	
	public static synchronized ReadConfigFile getInstance() throws FileNotFoundException, YamlException
	{			
		if (INSTANCE == null)
		{ 	INSTANCE = new ReadConfigFile();	
		}
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	private void readMainConfFile() throws YamlException, FileNotFoundException{
		System.out.println("Loading main config file.");
		YamlReader reader = new YamlReader(new FileReader(Constant.CONFIG_FILE));
		confMap = (Map<String, String>) reader.read();
	}

	@SuppressWarnings("unchecked")
	private void readDecicesConfFile() throws FileNotFoundException, YamlException{
		System.out.println("Loading devices config file.");
		YamlReader reader = new YamlReader(new FileReader(Constant.DEVICES_CONFIG_FILE));
		devicesMap = (Map<String, Map<String, String>>) reader.read();
	}
}
