package org.syfsyf.search.model;

import com.j256.ormlite.field.DatabaseField;

public class Dir extends BaseModel{
	
	@DatabaseField(foreign=true)
	private Index parent;
	
	@DatabaseField(unique=true)
	private String path;
	
	public Index getParent() {
		return parent;
	}
	public void setParent(Index parent) {
		this.parent = parent;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
}
