package org.syfsyf.search;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.SimpleFSDirectory;
import org.syfsyf.search.model.Dir;
import org.syfsyf.search.model.Index;
import org.syfsyf.search.utils.FileWaker;
import org.syfsyf.search.utils.FileWaker.Visitor;

import com.j256.ormlite.dao.ForeignCollection;

public class IndexTask {

	private static final Logger LOGGER = Logger.getLogger(IndexTask.class);
	private Index index;
	
	
	public static final String SEARCHER_DIR_ID="srch.dir.l";
	
	private SimpleFSDirectory directory;
	private StandardAnalyzer analyzer;
	private IndexWriter writer;
	private List<Indexer> indexers=new ArrayList<Indexer>();
	private TaskStatistics statistics=new TaskStatistics();
	
	public IndexTask(Index index) {
		super();
		this.index = index;
		indexers.add(new FileIndexer());
	}

	public void run() throws IOException {
		
		// creating index dir
		File indexDir = new File(ConfigManager.indexesDir(),""+index.getId());
		
		LOGGER.info("creating index dir:"+indexDir.getAbsolutePath());
		indexDir.mkdirs();
		
		this.directory = new SimpleFSDirectory(indexDir);
		this.analyzer = new StandardAnalyzer(ConfigManager.LUCENE_VERSION);
		IndexWriterConfig config = new IndexWriterConfig(ConfigManager.LUCENE_VERSION,
				analyzer);
		config.setOpenMode(OpenMode.CREATE);
		this.writer = new IndexWriter(directory, config);
	
		
		ForeignCollection<Dir> dirs = index.getDirs();
		LOGGER.info("dirs in index:"+dirs.size());
		for(Dir dir:dirs){
			LOGGER.info("indexing dir:"+dir.getPath());
			indexDir(dir);
		}
		writer.commit();
		writer.close();
		directory.close();
	}

	private void indexDir(final Dir dir) {
		
		
		File path = new File(dir.getPath());
		if(!path.exists()){
			LOGGER.warn("path not exists:"+dir);
			return;
		}
		if(!path.isDirectory()){
			LOGGER.warn("path is not directory:"+dir);
			return;
		}
		
		//WildcardFileFilter fileFilter2=new WildcardFileFilter(wildcards)
		//TODO - to define how shoud work
		
		//FileUtils.i
		
		//Iterator<File> iter = FileUtils.iterateFiles(path, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
		
		//Iterator<File> iter=FileUtils.iterateFiles(path, null, true);
		
		
		Visitor visitor=new Visitor() {
			public boolean file(File f) {
				try {
					indexFile(dir, f);
				}
				catch (Exception e) {
					LOGGER.error(e);
				}
				return true;
			}
		};
		FileWaker.walk(path, visitor);
		
		
		
		LOGGER.info("indexing dir done:"+dir);
	}

	private void indexFile(Dir dir, File file) throws CorruptIndexException, IOException {
		LOGGER.debug("indexing file:"+file.getAbsolutePath());
		if(file.isDirectory()){
			statistics.incIndexedDirs();
		}
		if(file.isFile()){
			statistics.incIndexedFiles();
		}
		Document document=new Document();
		document.add(new NumericField(SEARCHER_DIR_ID,Field.Store.YES,true).setIntValue(dir.getId()));
		
		IndexerContext context=new IndexerContext(this, dir, file,document);
		for(Indexer indexer:this.indexers){
			try {
				indexer.index(context);
			} catch (Exception e) {
				LOGGER.error(e);
			}	
		}
		getWriter().addDocument(document);
		
		
		LOGGER.debug("indexing file done:"+file.getAbsolutePath());	
	}
	public IndexWriter getWriter() {
		return writer;
	}
	public TaskStatistics getStatistics() {
		return statistics;
	}
}
