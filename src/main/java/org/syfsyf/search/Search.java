package org.syfsyf.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.SimpleFSDirectory;
import org.syfsyf.search.model.Index;

public class Search {
	private static final Logger LOGGER = Logger.getLogger(Search.class);
	public List<Document> search(Index index, String query) throws Exception{
		List<Document> result=new ArrayList<Document>();
	
		File indexDir = new File(ConfigManager.indexesDir(),""+index.getId());
		
		SimpleFSDirectory directory = new SimpleFSDirectory(indexDir);
		StandardAnalyzer analyzer = new StandardAnalyzer(ConfigManager.LUCENE_VERSION);
		IndexReader reader = IndexReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		
		Query q = new QueryParser(ConfigManager.LUCENE_VERSION, FileIndexer.FILE_NAME, analyzer).parse(query);
		 q = new QueryParser(ConfigManager.LUCENE_VERSION, "", analyzer).parse(query);
		TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);
		
		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;
		
		for (int i = 0; i < hits.length; i++) {
			result.add(searcher.doc(hits[i].doc));
		}
		searcher.close();
		reader.close();
		analyzer.close();
		directory.close();
		
		return result;
	}
}
