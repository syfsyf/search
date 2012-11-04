package org.syfsyf.search.model;

import java.util.List;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;


public class Index extends BaseModel{
	
	@ForeignCollectionField
	private ForeignCollection<Dir> dirs;
	
	@DatabaseField
	private String name;
	
	public ForeignCollection<Dir> getDirs() {
		return dirs;
	}
	public void setDirs(ForeignCollection<Dir> dirs) {
		this.dirs = dirs;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return getName()+"["+getId()+"]";
	}
}
