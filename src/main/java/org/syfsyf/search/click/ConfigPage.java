package org.syfsyf.search.click;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.click.control.Column;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.DataProvider;
import org.syfsyf.search.ConfigDb;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;

public class ConfigPage extends BorderPage{

	public Table table=new Table("table");
	
	public ConfigPage() throws SQLException {
		title="Config";
		ConfigDb db = ConfigManager.getConfgDb();
		Dao<Index, Integer> indexDao = db.dao(Index.class);
		List<Index> list = indexDao.queryForAll();
		table.setClass(Table.CLASS_SIMPLE);
		table.addColumn(new Column("name", "Index name"));
		
		table.setDataProvider(new DataProvider<Index>() {

			public Iterable<Index> getData() {
				try {
					return ConfigManager.getConfgDb().dao(Index.class).queryForAll();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException(e);
				}
				
			}
		});
		
		
		addModel("date", new Date());
		addModel("list", list);
	}	
}
