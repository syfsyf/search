package org.syfsyf.search.sandbox;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Index;

public class LuceneDaoTest {

	@Before
	public void setup()
	{
		BasicConfigurator.configure();
	}
	
	@Test
	public void test() throws Exception {
		
		File indexPath = new File(ConfigManager.defaultAppDir()+"/test");
		LuceneDao dao=new LuceneDao(indexPath);
		
		Index index=new Index();
		index.setName("index 1");
		
		dao.save(index);
		assertNotNull(index.getId());
		
		Index i=(Index) dao.byId(Index.class,index.getId());
		assertEquals(index.getName(), i.getName());
		
		Index index2=new Index();
		index2.setName("index 2");
		dao.save(index2);
		
		List list= dao.list(Index.class);
		
		assertEquals(2, list.size());
		//assertEquals("", actual)
		
		dao.delete(index);
		
		list= dao.list(Index.class);
		assertEquals(1, list.size());
		
		i=(Index) list.get(0);
		assertEquals("index 2", i.getName());
		
	}

}
