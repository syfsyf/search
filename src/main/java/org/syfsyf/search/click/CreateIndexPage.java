package org.syfsyf.search.click;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.click.control.Column;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.PageLink;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.syfsyf.search.ConfigDb;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;

public class CreateIndexPage extends BorderPage {

	public Form form = new Form("form");

	// public TextField name=new TextField(name)

	public CreateIndexPage() throws SQLException {
		title = "Create Index";

		FieldSet fieldSet = new FieldSet("Create Index");
		form.add(fieldSet);

		fieldSet.add(new TextField("name", true));

		form.add(new Submit("save", this, "onSubmit"));

	}

	public boolean onSubmit() throws SQLException {
		if (form.isValid()) {
			Field f = form.getField("name");
			System.out.println("submitted:" + f.getValue());
			ConfigDb db = ConfigManager.getConfgDb();
			Dao<Index, Integer> indexDao = db.dao(Index.class);
			Index index=new Index();
			index.setName(f.getValue());
			indexDao.create(index);
			flashSuccess("utworzono!");
			setRedirect(ConfigPage.class);
			
			
		}
		else{
			
		}
		return true;
	}
}
