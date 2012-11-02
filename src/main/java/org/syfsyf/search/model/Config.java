package org.syfsyf.search.model;

import java.util.ArrayList;
import java.util.List;

public class Config {
	
	private List<Index> indexs=new ArrayList<Index>();
	/**
	 * full path to directory with index files
	 */
	private String indexDirectory;
	
	public List<Index> getIndexs() {
		return indexs;
	}
	public void setIndexs(List<Index> indexs) {
		this.indexs = indexs;
	}
	
	public String getIndexDirectory() {
		return indexDirectory;
	}
	public void setIndexDirectory(String indexDirectory) {
		this.indexDirectory = indexDirectory;
	}
}
