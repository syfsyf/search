package org.syfsyf.search.webgui;

import org.syfsyf.search.Main;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Window;

public class Application extends com.vaadin.Application {

	@Override
	public void init() {
		Window mainWindow = new Window("Searcher");
		setMainWindow(mainWindow);
		
		TabSheet tabSheet=new TabSheet();
		tabSheet.setWidth("100%");
		tabSheet.setHeight("100%");
		mainWindow.addComponent(tabSheet);
		
		tabSheet.addTab(new SearchPanel(),"Search");
		tabSheet.addTab(new ConfigPanel(Main.getConfig()),"Config");
		

	}

}
