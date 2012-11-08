package org.syfsyf.search.webgui;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;

public class SettingsPanel extends VerticalLayout{


	private TabSheet tabSheet;

	public SettingsPanel() throws Exception {
		
		this.tabSheet = new TabSheet();
		tabSheet.setWidth("100%");
		tabSheet.setHeight("650px");
		addComponent(tabSheet);
		
		tabSheet.addTab(new ConfigureIndexesPanel(),"Configure Indexes");
		tabSheet.addTab(new CreateIndexesPanel(),"Create Indexes");
		tabSheet.addListener(new TabSheet.SelectedTabChangeListener() {
			
			public void selectedTabChange(SelectedTabChangeEvent event) {
				System.out.println("event:"+event);
				Component sel = tabSheet.getSelectedTab();
				if(sel instanceof CreateIndexesPanel){
					CreateIndexesPanel createIndexesPanel=(CreateIndexesPanel) sel;
					createIndexesPanel.refresh();
				}
				System.out.println("tab changed");
				
			}
		});
		//tabSheet.addTab(new ConfigPanel(),"Configure Indexes");
		
	}
	
}
