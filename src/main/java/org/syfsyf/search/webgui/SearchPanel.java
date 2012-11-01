package org.syfsyf.search.webgui;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class SearchPanel extends VerticalLayout{
	
	public SearchPanel() {
		
		addComponent(new Label("query"));
		
		TextField query=new TextField();
		addComponent(query);
		
		
		addComponent(new Button("search"));
		
		
		
	}

}
