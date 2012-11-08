package org.syfsyf.search.webgui;

import java.sql.SQLException;
import java.util.List;

import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class CreateIndexesPanel extends VerticalLayout{

	private OptionGroup group;

	public CreateIndexesPanel() throws Exception {
		
		
		
		this.group=new OptionGroup("Select Indexes");
		
		group.setMultiSelect(true);
		
		loadData();
		addComponent(group);
		
		
	}

	public void refresh()
	{
		try {
			loadData();
		} catch (SQLException e) {
			e.printStackTrace();
			Application.showError(e.getMessage());
			// TODO Auto-generated catch block
			
		}
		
		
	}
	
	private void loadData() throws SQLException {
		Dao<Index, Integer> dao = ConfigManager.getConfgDb().dao(Index.class);
		List<Index> list = dao.queryForAll();
		group.removeAllItems();
		for(Index index:list){
			group.addItem(index);
		}
	}
	
}
