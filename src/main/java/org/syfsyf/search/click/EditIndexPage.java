package org.syfsyf.search.click;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.click.control.Column;
import org.apache.click.control.Field;
import org.apache.click.control.FieldSet;
import org.apache.click.control.Form;
import org.apache.click.control.HiddenField;
import org.apache.click.control.PageLink;
import org.apache.click.control.Submit;
import org.apache.click.control.Table;
import org.apache.click.control.TextField;
import org.apache.click.dataprovider.DataProvider;
import org.apache.click.util.Bindable;
import org.syfsyf.search.ConfigDb;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Dir;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

public class EditIndexPage extends BorderPage {

	public Form form = new Form("form");
	protected TextField name = new TextField("name", true);
	protected HiddenField fid = new HiddenField("id", Integer.class);

	public Form newDirForm = new Form("newDirForm");
	public List<Form> dirForms = new ArrayList<Form>();
	public Form dirForm = new Form("dirForm");

	@Bindable
	protected Integer id;

	// public TextField name=new TextField(name)

	public EditIndexPage() throws SQLException {
		title = "Update Index";
		FieldSet fieldSet = new FieldSet("Update Index");
		form.add(fieldSet);
		fieldSet.add(name);
		fieldSet.add(fid);
		form.add(new Submit("save", this, "onSubmit"));

	}

	private FieldSet createDirFormFieldSet() {
		FieldSet fieldSet = new FieldSet("Dir Form");

		TextField path = new TextField("path");
		fieldSet.add(path);
		return fieldSet;
	}

	@Override
	public void onInit() {
		FieldSet dirFieldSet = createDirFormFieldSet();
		newDirForm.add(dirFieldSet);

		newDirForm.add(new Submit("new dir", this, "onNewDirSubmit"));
		HiddenField parentId = new HiddenField("parent", Integer.class);
		newDirForm.add(parentId);
		parentId.setValueObject(id);

		dirForm.add(createDirFormFieldSet());

		parentId = new HiddenField("parent", Integer.class);
		dirForm.add(parentId);
		dirForm.add(new HiddenField("id", Integer.class));
		parentId.setValueObject(id);

		Submit updateSubmit = new Submit("update", this, "onUpdateDirSubmit");

		dirForm.add(updateSubmit);

		try {

			Dao<Index, Integer> indexDao = ConfigManager.getConfgDb().dao(
					Index.class);
			Index idx = indexDao.queryForId(id);
			name.setValue(idx.getName());
			fid.setValueObject(id);

			ForeignCollection<Dir> dirs = dao(Index.class).queryForId(id)
					.getDirs();

			for (Dir dir : dirs) {
				Form form = new Form(dirForm.getName());
				String action = form.getActionURL();
				action += "?id=" + id;
				form.setActionURL(action);
				form.add(createDirFormFieldSet());
				form.add(new HiddenField("parent", id));
				form.add(new HiddenField("id", dir.getId()));
				form.add(new Submit("update", this, "onUpdateDirSubmit"));

				form.getField("path").setValue(dir.getPath());
				dirForms.add(form);

			}
		} catch (SQLException e) {
			e.printStackTrace();
			flashError(e);
		}

	}

	public boolean onSubmit() throws Exception {
		if (form.isValid()) {

			ConfigDb db = ConfigManager.getConfgDb();
			Dao<Index, Integer> indexDao = db.dao(Index.class);
			Index index = indexDao.queryForId(id);
			index.setName(name.getValue());
			indexDao.update(index);
			flashSuccess("updated");
			setRedirect(ConfigPage.class);
		}
		return true;
	}

	public boolean onNewDirSubmit() throws SQLException {
		if (newDirForm.isValid()) {
			Integer parent = (Integer) newDirForm.getField("parent")
					.getValueObject();

			Dao<Dir, Integer> dao = dao(Dir.class);
			Dao<Index, Integer> idxDao = dao(Index.class);

			Dir dir = new Dir();
			dir.setParent(idxDao.queryForId(parent));
			dir.setPath(newDirForm.getField("path").getValue());

			dao.create(dir);

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", parent);
			flashSuccess("directory created");
			setRedirect(EditIndexPage.class, params);
			return false;
		}

		return true;
	}

	public boolean onUpdateDirSubmit() throws SQLException {
		System.out.println("onUpdateDirSubmit");
		if (dirForm.isValid()) {
			Dao<Dir, Integer> dao = dao(Dir.class);
			Integer dirId= (Integer) dirForm.getField("id").getValueObject();
			
			Dir dir = dao.queryForId(dirId);
			dir.setPath(dirForm.getFieldValue("path"));
			dao.update(dir);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", dir.getParent().getId());
			flashSuccess("directory updated");
			setRedirect(EditIndexPage.class, params);
		}

		return true;
	}
}
