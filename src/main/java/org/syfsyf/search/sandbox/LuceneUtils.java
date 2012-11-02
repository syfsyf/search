package org.syfsyf.search.sandbox;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

public class LuceneUtils {

	
	
	public static void dumpIndex(IndexSearcher indexSearcher) throws CorruptIndexException, IOException{
		
		IndexReader reader = indexSearcher.getIndexReader();
		System.out.println("documents:"+indexSearcher.maxDoc());
		for(int i=0;i<indexSearcher.maxDoc();i++)
		{
			if(reader.isDeleted(i)){
				System.out.println("doc id:"+i+" is deleted");
				continue;
			}
			
			Document doc = indexSearcher.doc(i);
			
			dumpDoc(doc);
			
			
		}
		
		
	}

	private static void dumpDoc(Document doc) {
		
		List<Fieldable> fields = doc.getFields();
		System.out.println("fields:"+fields.size());
		for(Fieldable f:fields){
			System.out.println("\t"+f.name()+":"+f.stringValue());
		}
		
		
		
	}
	
	
}
