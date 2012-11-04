package org.syfsyf.search;

import java.io.File;

import org.apache.lucene.document.Document;
import org.syfsyf.search.model.Dir;

public class IndexerContext {

	private IndexTask indexTask;
	private Dir dir;
	private File file;
	private Document document;
	
	
	public IndexerContext(IndexTask indexTask, Dir dir, File file,Document document) {
		super();
		this.indexTask = indexTask;
		this.dir = dir;
		this.file = file;
		this.document=document;
		
	}
	
	public IndexTask getIndexTask() {
		return indexTask;
	}
	public Dir getDir() {
		return dir;
	}
	public File getFile() {
		return file;
	}
	public Document getDocument() {
		return document;
	}
	
}
