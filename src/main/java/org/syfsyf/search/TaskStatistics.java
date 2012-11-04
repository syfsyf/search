package org.syfsyf.search;

import org.apache.commons.lang.builder.ToStringBuilder;

public class TaskStatistics {

	private long indexedFiles=0;
	private long indexedDirs=0;
	
	public void incIndexedFiles()
	{
		indexedFiles++;
	}
	public void incIndexedDirs()
	{
		indexedDirs++;
	}
	
	
	public long getIndexedDirs() {
		return indexedDirs;
	}
	public long getIndexedFiles() {
		return indexedFiles;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
