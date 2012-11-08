package org.syfsyf.search;

import java.sql.SQLException;

import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;

public class IndexerManager {

	private IndexerManagerContext context;

	public IndexerManager(IndexerManagerContext context) {
		super();
		this.context = context;
	}

	public IndexerManagerContext getContext() {
		return context;
	}

	public void run() throws Exception {

		ConfigDb db = ConfigManager.getConfgDb();
		Dao<Index, Integer> idxDao = db.dao(Index.class);

		for (Integer id : getContext().getIndexsIds()) {

			Index index = idxDao.queryForId(id);
			IndexTask indexTask = new IndexTask(index);
			indexTask.run();

		}
	}
}
