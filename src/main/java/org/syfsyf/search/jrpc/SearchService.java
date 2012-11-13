package org.syfsyf.search.jrpc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.syfsyf.search.ConfigDb;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Dir;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;

public class SearchService {

	
	public String hello()
	{
		return "hello";
	}
	public String errorTest()
	{
		throw new RuntimeException("bum!");
	}
	
	public List<Index> indexList() throws SQLException {
		
		ConfigDb db = ConfigManager.getConfgDb();
		Dao<Index, Integer> indexDao = db.dao(Index.class);
		Dao<Dir, Integer> dirDao = db.dao(Dir.class);
		
		Map<String, Object> result=new HashMap<String, Object>();
		
		List<Index> list = ConfigManager.getConfgDb().dao(Index.class)
				.queryForAll();
		
		return list;

	}
	public Index updateIndex(Index data) throws SQLException{
		
		ConfigDb db = ConfigManager.getConfgDb();
		Dao<Index, Integer> indexDao = db.dao(Index.class);
		Index idx = indexDao.queryForId(data.getId());
		
		idx.setName(data.getName());
		indexDao.update(idx);
		return idx;
		
		
		
	}
}
