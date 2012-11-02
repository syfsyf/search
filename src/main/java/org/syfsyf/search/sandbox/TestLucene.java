package org.syfsyf.search.sandbox;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Index;

public class TestLucene {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		IndexWriter w=ConfigManager.createConfigWriter();
		Document document=new Document();
		
		
		document.add(new Field("class", Index.class.getName(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		
		
		
		
		
		
		
		
		w.addDocument(document);
		w.close();
		
		
		
		
	}

}
