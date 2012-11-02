package org.syfsyf.search.model;

import com.j256.ormlite.field.DatabaseField;

public abstract class BaseModel {

	@DatabaseField(generatedId=true)
	private Integer id;
	public  Integer getId(){
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
}

