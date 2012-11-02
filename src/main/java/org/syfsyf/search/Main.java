package org.syfsyf.search;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.syfsyf.search.model.Config;


/**
 * Main app class.
 * @author syfsyf@gmail.com
 *
 */
public class Main {
	
	private static final Logger LOGGER = Logger.getLogger(Main.class);
	private static Config config;
	
	public static Config getConfig() {
		if(config==null){
			try {
				config=ConfigManager.loadDefaultConfig();
			} catch (IOException e) {
				LOGGER.error(e);
				throw new RuntimeException(e);
			}
		}
		return config;
	}
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		BasicConfigurator.configure();
		config=ConfigManager.loadDefaultConfig();
		ConfigManager.saveDefualtConfig(config);
	}

}
