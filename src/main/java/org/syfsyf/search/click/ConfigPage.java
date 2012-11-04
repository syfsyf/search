package org.syfsyf.search.click;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.click.ActionListener;
import org.apache.click.Context;
import org.apache.click.Control;
import org.apache.click.control.ActionLink;
import org.apache.click.control.Column;
import org.apache.click.control.Decorator;
import org.apache.click.control.PageLink;
import org.apache.click.control.Table;
import org.apache.click.dataprovider.DataProvider;
import org.syfsyf.search.ConfigDb;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Index;

import com.j256.ormlite.dao.Dao;

public class ConfigPage extends BorderPage {

	public Table table = new Table("table");
	public PageLink createIndexLink = new PageLink("createIndexLink",
			"create index", CreateIndexPage.class);

	private ActionLink viewLink = new ActionLink("view", this, "onViewClick");
	private PageLink editLink = new PageLink("edit", EditIndexPage.class);
	private ActionLink deleteLink = new ActionLink("delete", this,
			"onDeleteClick");

	public ConfigPage() throws SQLException {

		title="Config";
		addControl(deleteLink);

		table.setClass(Table.CLASS_SIMPLE);
		table.addColumn(new Column("name", "Index name"));
		deleteLink
				.setAttribute("onclick",
						"return window.confirm('Are you sure you want to delete this record?');");

		Column actions = new Column("actions");
		actions.setDecorator(new Decorator() {

			public String render(Object object, Context context) {
				Index index = (Index) object;
				String id = "" + index.getId();
				viewLink.setValue(id);
				editLink.setParameter("id", id);
				deleteLink.setValue(id);

				return viewLink.toString() + " | " + editLink.toString()
						+ " | " + deleteLink.toString();

			}
		});
		table.addColumn(actions);
		table.setDataProvider(new DataProvider<Index>() {

			public Iterable<Index> getData() {
				try {
					return ConfigManager.getConfgDb().dao(Index.class)
							.queryForAll();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					throw new RuntimeException(e);
				}

			}
		});
	}

	public boolean onDeleteClick() throws SQLException {
		Integer id = deleteLink.getValueInteger();
		System.out.println("id:" + id);

		Dao<Index, Integer> dao = dao(Index.class);
		dao.deleteById(id);
		flashSuccess("record deleted");

		setRedirect(ConfigPage.class);

		return true;
	}
}
