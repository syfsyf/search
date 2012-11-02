package org.syfsyf.search.click;

public class FlashItem {

	public enum Type {error,notice,success};
	private Object message;
	private Type type;
	
	
	
	public FlashItem(Object message, Type type) {
		super();
		this.message = message;
		this.type = type;
	}
	public Object getMessage() {
		return message;
	}
	public void setMessage(Object message) {
		this.message = message;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	
	
	
	
}
