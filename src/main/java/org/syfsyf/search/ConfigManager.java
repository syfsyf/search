package org.syfsyf.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.syfsyf.search.model.Config;

import com.thoughtworks.xstream.XStream;

/**
 * Read and write project file.
 * 
 * @author syfsyf@gmail.com
 * 
 */
public class ConfigManager {
	private static final Logger LOGGER=Logger.getLogger(ConfigManager.class);
	/**
	 * Default application directory.
	 * @return
	 */
	public static File defaultAppDir()
	{
		String path = System.getProperty("user.home")+"/.org.syfysyf.search";
		return new File(path);
	}
	/**
	 * Default application config file.
	 * @return
	 */
	public static File defaultConfig()
	{
		File file=new File(defaultAppDir()+"/config.xml");
		return file;
	}
	public static Config loadDefaultConfig() throws FileNotFoundException
	{
		File def = defaultConfig();
		if(!def.exists()){
			return new Config();
		}
		else{
			return readFromFile(def);
		}
	}
	public static void saveDefualtConfig(Config config) throws FileNotFoundException{
		File defDir = defaultAppDir();
		if(!defDir.exists()){
			defDir.mkdirs();
		}
		saveToFile(defaultConfig(), config);
	}
	
	
	public static Config readFromFile(File file) throws FileNotFoundException {

		XStream stream = createXSTream();
		return (Config) stream.fromXML(new FileInputStream(file));
	}

	public static void saveToFile(File file, Config project) throws FileNotFoundException {
		XStream stream = createXSTream();
		stream.toXML(project, new FileOutputStream(file));
	}

	private static XStream createXSTream() {

		XStream stream = new XStream();
		return stream;
	}
}
