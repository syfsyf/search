package org.syfsyf.search;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.syfsyf.search.model.Config;

import com.thoughtworks.xstream.XStream;

/**
 * Read and write project file.
 * 
 * @author syfsyf@gmail.com
 * 
 */
public class ConfigManager {
	
	public static final Version LUCENE_VERSION=Version.LUCENE_36;
	
	private static final Logger LOGGER = Logger.getLogger(ConfigManager.class);

	private static ConfigDb configDb;

	/**
	 * Default application directory.
	 * 
	 * @return
	 */
	public static File defaultAppDir() {
		String path = System.getProperty("user.home") + "/.org.syfysyf.search";
		return new File(path);
	}

	/**
	 * Default application config file.
	 * 
	 * @return
	 */
	public static File defaultConfig() {
		File file = new File(defaultAppDir() + "/config.xml");
		return file;
	}

	public static Config loadDefaultConfig() throws IOException {
		File def = defaultConfig();
		if (!def.exists()) {
			LOGGER.debug("creating default config");
			return createDefaultConfig();
		} else {
			return readFromFile(def);
		}
	}

	private static Config createDefaultConfig() {
		Config config = new Config();
		config.setIndexDirectory(defaultAppDir() + "/indexes");
		return config;
	}

	public static void saveDefualtConfig(Config config)
			throws IOException {
		File defDir = defaultAppDir();
		if (!defDir.exists()) {
			defDir.mkdirs();
		}
		saveToFile(defaultConfig(), config);
	}

	public static Config readFromFile(File file) throws IOException {

		XStream stream = createXSTream();
		FileInputStream fis = new FileInputStream(file);
		try {
			return (Config) stream.fromXML(fis);
		} finally {
			fis.close();
		}
	}

	public static void saveToFile(File file, Config project) throws IOException {
		XStream stream = createXSTream();
		FileOutputStream fos = new FileOutputStream(file);
		try {
			stream.toXML(project, fos);
		} finally {
			fos.close();
		}
	}

	private static XStream createXSTream() {

		XStream stream = new XStream();
		return stream;
	}
	public static IndexWriter createConfigWriter() throws IOException
	{
		File path = new File(ConfigManager.defaultAppDir() + "/config");

		Directory directory = new SimpleFSDirectory(path);

		Analyzer analyzer = new StandardAnalyzer(LUCENE_VERSION);
		IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION,
				analyzer);
		
		IndexWriter w=new IndexWriter(directory, config);
		return w;
		
		
	}
	public static String databaseUrl()
	{
		return "jdbc:h2:~/.org.syfysyf.search/config/db";
	}
	public static ConfigDb getConfgDb() 
	{
		try {
		if(configDb==null){
			configDb=new ConfigDb(databaseUrl());
		}
		return configDb;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static File indexesDir()
	{
		File file = new File(defaultAppDir(),"indexes");
		return file;
	}
}
