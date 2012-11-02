package org.syfsyf.search.sandbox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;
import org.syfsyf.search.model.BaseModel;

public class LuceneDao {

	private static final Logger LOGGER = Logger.getLogger(LuceneDao.class);
	// private IndexWriter indexWriter;
	private File indexPath;

	private SimpleFSDirectory directory;
	private StandardAnalyzer analyzer;
	private IndexWriter writer;
	private IndexSearcher searcher;

	public static final Version LUCENE_VERSION = Version.LUCENE_36;
	public static final String BASE_CLASS_NAME = BaseModel.class.getName().replaceAll("\\.", "");

	public enum FieldNames {
		nextId
	}

	public LuceneDao(File indexPath) throws IOException {
		super();
		this.indexPath = indexPath;
		this.directory = new SimpleFSDirectory(indexPath);
		
		this.analyzer = new StandardAnalyzer(LUCENE_VERSION);

		writer = __createWriter();
		searcher = __createReader();
		
		
		LuceneUtils.dumpIndex(searcher);
		

	}

	public void save(BaseModel object) throws Exception {

		Integer id = object.getId();
		if (id != null) {

		} else {
			object.setId(nextId());
			System.out.println("id:" + object.getId());
			Document document = new Document();
		}

		java.lang.reflect.Field[] fields = object.getClass()
				.getDeclaredFields();

		for (java.lang.reflect.Field f : fields) {
			System.out.println("f:" + f);
		}

	}

	private Integer nextId() throws Exception {

		Integer id = null;

		Document doc;
		Term term = new Term("class", BASE_CLASS_NAME);

		doc = queryFirst(term);

		String nextIdName = FieldNames.nextId.toString();
		if (doc == null) {
			LOGGER.debug("not found nextId doc");
			Document document = new Document();
			id = 1;
			Fieldable fieldClass = new Field("class", BASE_CLASS_NAME,
					Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
			Fieldable fieldNextId = new Field(nextIdName, "" + id,
					Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS);
			document.add(fieldClass);
			document.add(fieldNextId);

			add(document);
		} else {
			LOGGER.debug("found nextId doc");
			id = Integer.valueOf("" + doc.get(nextIdName));
			id++;
			Field f = doc.getField(nextIdName);
			f.setValue("" + id);
			//doc.removeField(nextIdName);
			//doc.add(f);
			updateDocument(term, doc);
			// Field f = doc.getField(FieldNames.nextId.toString());
		}
		return id;
	}

	private Document queryFirst(Term term) throws IOException {

		TermQuery query = new TermQuery(term);
		TopDocs hits = searcher.search(query, 1);
		if (hits.scoreDocs.length == 0) {
			return null;
		}
		return searcher.doc(hits.scoreDocs[0].doc);
	}

	private void add(Document document) throws IOException {

		writer.addDocument(document);
		writer.commit();
		
	}

	private void updateDocument(Term term, Document doc) throws IOException {

		writer.updateDocument(term, doc);
		writer.commit();
	}

	private Document queryFirst(String querystr) throws Exception {

		List<Document> docs = queryDocs(querystr, 1);
		LOGGER.debug("query first size:" + docs.size());
		if (docs.size() == 1) {
			return docs.get(0);
		}
		return null;
	}

	private List<Document> queryDocs(String query, int limit) throws Exception {

		Query q = new QueryParser(LUCENE_VERSION, "", analyzer).parse(query);
		TopScoreDocCollector collector = TopScoreDocCollector.create(1, true);

		try {
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
			List<Document> docs = new ArrayList<Document>();
			for (int i = 0; i < hits.length; i++) {
				docs.add(searcher.doc(hits[i].doc));
			}
			return docs;
		} finally {

		}

	}

	public BaseModel byId(Class class1, Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	public List list(Class class1) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(BaseModel index) {
		// TODO Auto-generated method stub

	}

	protected IndexWriter __createWriter() throws IOException {

		IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION,
				analyzer);

		config.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter w = new IndexWriter(directory, config);
		w.commit();
		return w;
	}

	protected IndexSearcher __createReader() throws IOException {

		IndexReader reader = IndexReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		return searcher;
	}

}
