package org.syfsyf.search;

import java.util.ArrayList;
import java.util.List;

public class QueryModel {

	private String contentQuery="";
	/**
	 * witch indexes shoud be not searched.
	 */
	private List<Integer> disabledIndexes=new ArrayList<Integer>();

	public String getContentQuery() {
		return contentQuery;
	}

	public void setContentQuery(String contentQuery) {
		this.contentQuery = contentQuery;
	}
	
	
	
	
	
	
	
	
}
