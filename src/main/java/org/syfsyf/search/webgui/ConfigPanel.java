package org.syfsyf.search.webgui;

import org.syfsyf.search.Main;
import org.syfsyf.search.model.Config;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class ConfigPanel extends VerticalLayout{

	private Config config;
	public ConfigPanel(Config config) {
		
		this.config=config;
		addComponent(new Label(""+config));
	}
	
}
