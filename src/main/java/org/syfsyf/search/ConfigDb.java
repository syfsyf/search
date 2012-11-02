package org.syfsyf.search;

import java.sql.SQLException;

import org.syfsyf.search.model.Dir;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class ConfigDb {

	private String databaseUrl;
	private ConnectionSource connectionSource;

	public ConfigDb(String databaseUrl) throws SQLException {
		super();
		this.databaseUrl = databaseUrl;
		//new JdbcConnectionSource()
		this.connectionSource = new JdbcConnectionSource(this.databaseUrl,"sa","sa");
		TableUtils.createTableIfNotExists(connectionSource, Index.class);
		TableUtils.createTableIfNotExists(connectionSource, Dir.class);
	}

	public <T> Dao<T, Integer> dao(Class<T> t) {
		try {
			return DaoManager.createDao(connectionSource, t);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
