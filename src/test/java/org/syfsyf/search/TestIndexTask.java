package org.syfsyf.search;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.junit.Test;
import org.syfsyf.search.model.Dir;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;

public class TestIndexTask extends BaseTest{

	public static final File SAMPLEDATA_FOLDER=new File("src/test/resources/sampledata");
	
	@Test
	public void test() throws Exception {
		
		// delete all
		FileUtils.deleteDirectory(ConfigManager.defaultAppDir());
		
		ConfigDb cfg = ConfigManager.getConfgDb();
		Dao<Index, Integer> idxDao = cfg.dao(Index.class);
		Dao<Dir, Integer> dirDao = cfg.dao(Dir.class);
	
		Index index=new Index();
		index.setName("t1");
		idxDao.create(index);
		
		Dir dir=new Dir();
		dir.setPath(new File(SAMPLEDATA_FOLDER,"dir1").getAbsolutePath());
		dir.setParent(index);
		dirDao.create(dir);
		
		
		// foreign collection need that.
		idxDao.refresh(index);
		
		IndexTask indexTask=new IndexTask(index);
		indexTask.run();
		
		System.out.println(indexTask.getStatistics());
		
		Search search=new Search();
		List<Document> result;
		result = search.search(index, "file.name:readme*txt");
		System.out.println("result:"+result.size());
		
		
		result = search.search(index, "file.type:f");
		System.out.println("result:"+result.size());
		
		System.out.println(search.search(index, "file.size:13").size());
		
	}

}
