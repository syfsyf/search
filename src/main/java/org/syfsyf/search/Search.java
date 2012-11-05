package org.syfsyf.search;

import java.io.File;
import java.io.IOException;
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
	
	private Index index;

	private SimpleFSDirectory directory;

	private StandardAnalyzer analyzer;

	private IndexReader reader;

	private IndexSearcher searcher;
	
	

	public Search(Index index) throws IOException {
		super();
		this.index = index;
		File indexDir = new File(ConfigManager.indexesDir(), "" + index.getId());
		this.directory = new SimpleFSDirectory(indexDir);
		this.analyzer = new StandardAnalyzer(
				ConfigManager.LUCENE_VERSION);
		this.reader = IndexReader.open(directory);
		this.searcher = new IndexSearcher(reader);
		
	}



	public List<Document> search(String query) throws Exception {
		List<Document> result = new ArrayList<Document>();
		
		Query q = new CustomQueryParser(ConfigManager.LUCENE_VERSION,
				FileIndexer.FILE_CONTENT, analyzer).parse(query);
		///q = new QueryParser(ConfigManager.LUCENE_VERSION, "", analyzer)
		//		.parse(query);
		
		TopScoreDocCollector collector = TopScoreDocCollector.create(100, true);

		searcher.search(q, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		for (int i = 0; i < hits.length; i++) {
			result.add(searcher.doc(hits[i].doc));
		}
		

		return result;
	}
}
