package org.syfsyf.search;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.syfsyf.search.model.Dir;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;

public class TestConfigDb extends BaseTest{

	@Test
	public void test() throws SQLException {
		
		ConfigDb cfg = ConfigManager.getConfgDb();
		Dao<Index, Integer> dao = cfg.dao(Index.class);
		Index index=new Index();
		index.setName("test");
		dao.createOrUpdate(index);
		assertNotNull(index.getId());
		
		Index test = dao.queryForId(index.getId());
		assertEquals("test", test.getName());
		
		
		Dao<Dir, Integer> dirDao = cfg.dao(Dir.class);
		dirDao.delete(dirDao.queryForAll());
		
		
		
		Dir dir=new Dir();
		dir.setParent(test);
		dir.setPath("hello");
		
		dirDao.createOrUpdate(dir);
		
		dao.refresh(test);
		assertEquals(1, test.getDirs().size());
		
		
		
		
	}

}
