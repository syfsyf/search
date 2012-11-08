package org.syfsyf.search.webgui;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Dir;
import org.syfsyf.search.model.Index;
import org.vaadin.dialogs.ConfirmDialog;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.Action;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ConfigureIndexesPanel extends VerticalLayout implements
		Property.ValueChangeListener, Action.Handler {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(ConfigureIndexesPanel.class);
	public static final String PROPERTY_NAME = "name";
	public static final String PROPERTY_ICON = "icon";

	private static final String indexesNode = "Indexes";
	private Panel propertyPanel = new Panel("Properties");
	private Tree configTree;

	// Actions for the context menu
	private static final Action ACTION_ADD_INDEX = new Action("Add index");
	private static final Action ACTION_DEL_INDEX = new Action("Delete index");
	private static final Action ACTION_ADD_DIR = new Action("Add dir");
	private static final Action ACTION_DEL_DIR = new Action("Delete dir");

	private static final Action[] INDEXES_ACTIONS = new Action[] { ACTION_ADD_INDEX };
	private static final Action[] INDEX_ACTIONS = new Action[] {
			ACTION_DEL_INDEX, ACTION_ADD_DIR };
	private static final Action[] DIR_ACTIONS = new Action[] { ACTION_DEL_DIR };

	class FieldFactory extends DefaultFieldFactory {

		@Override
		public Field createField(Item item, Object propertyId,
				Component uiContext) {

			Field field = super.createField(item, propertyId, uiContext);
			if ("indexDirectory".equals(propertyId)) {
				field.setWidth("500px");
			}
			return field;
		}

	}

	public ConfigureIndexesPanel() throws Exception {

		setSpacing(true);
		setMargin(true);

		HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
		splitPanel.setSplitPosition(20, Sizeable.UNITS_PERCENTAGE);
		splitPanel.setHeight("550px");
		addComponent(splitPanel);

		splitPanel.addComponent(createTree());
		splitPanel.addComponent(propertyPanel);

	}

	private Component createTree() throws SQLException {

		this.configTree = new Tree("Config");

		configTree.addContainerProperty(PROPERTY_ICON, Resource.class, null);
		configTree.addContainerProperty(PROPERTY_NAME, String.class, null);
		configTree.setItemIconPropertyId(PROPERTY_ICON);
		configTree.setItemCaptionPropertyId(PROPERTY_NAME);
		configTree
				.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);

		Item indexesItem = configTree.addItem(indexesNode);
		indexesItem.getItemProperty(PROPERTY_NAME).setValue("Indexes");
		indexesItem.getItemProperty(PROPERTY_ICON).setValue(
				Resources.CONFIGURE_16);

		configTree.setChildrenAllowed(indexesNode, true);

		Dao<Index, Integer> idxDao = ConfigManager.getConfgDb()
				.dao(Index.class);
		List<Index> list = idxDao.queryForAll();

		for (Index idx : list) {
			Item item = configTree.addItem(idx);
			configTree.setParent(idx, indexesNode);
			item.getItemProperty(PROPERTY_NAME).setValue(idx.toString());
			item.getItemProperty(PROPERTY_ICON).setValue(Resources.INDEX_16);

			// item.getItemProperty("name").setValue(idx.getName());
			ForeignCollection<Dir> dirs = idx.getDirs();
			configTree.setChildrenAllowed(idx, dirs.size() > 0);

			for (Dir dir : dirs) {
				Item itemDir = configTree.addItem(dir);
				itemDir.getItemProperty(PROPERTY_NAME).setValue(dir.toString());
				itemDir.getItemProperty(PROPERTY_ICON).setValue(
						Resources.FOLDER_16);
				configTree.setParent(dir, idx);
				configTree.setChildrenAllowed(dir, false);
			}
		}

		configTree.expandItem(indexesNode);
		configTree.addListener(this);
		configTree.setImmediate(true);
		configTree.setSelectable(true);
		configTree.addActionHandler(this);
		return configTree;
	}

	public void valueChange(ValueChangeEvent event) {

		Object val = event.getProperty().getValue();
		if (indexesNode.equals(val)) {
			onIndexesNodeSelected();
		} else if (val instanceof Index) {
			onIndexSelected((Index) val);
		} else if (val instanceof Dir) {
			onDirSelected((Dir) val);
		}

	}

	private void setPropertyPanel(Panel panel) {
		propertyPanel.removeAllComponents();
		propertyPanel.addComponent(panel);

	}

	private void onDirSelected(final Dir val) {
		Panel panel = new Panel("Dir");
		panel.setIcon(Resources.FOLDER_32);
		final Form form = new Form();
		panel.addComponent(form);

		BeanItem<Dir> beanItem = new BeanItem<Dir>(val);
		form.setItemDataSource(beanItem);
		form.setInvalidCommitted(false);
		form.setVisibleItemProperties(new String[] { "path" });
		// Field nameField = form.getField("name");

		Button saveButton = new Button("save");
		saveButton.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					if (form.isValid()) {
						form.commit();
						ConfigManager.getConfgDb().dao(Dir.class).update(val);
						configTree.requestRepaint();
						Application.trayMessage("updated");
					}
				} catch (Exception e) {
					Application.showError(e.getMessage());
				}

			}
		});
		form.getFooter().addComponent(saveButton);

		setPropertyPanel(panel);

	}

	private void onIndexSelected(final Index val) {
		Panel panel = new Panel("Index");
		panel.setIcon(Resources.INDEX_32);

		final Form form = new Form();
		panel.addComponent(form);

		BeanItem<Index> beanItem = new BeanItem<Index>(val);
		form.setItemDataSource(beanItem);
		form.setInvalidCommitted(false);
		form.setVisibleItemProperties(new String[] { "name" });
		// Field nameField = form.getField("name");

		// configTree.expandItem(val);

		Button saveButton = new Button("save");
		saveButton.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					if (form.isValid()) {
						form.commit();

						// configTree.ref
						ConfigManager.getConfgDb().dao(Index.class).update(val);
						// Item itm = configTree.getItem(val);
						// itm.getItemProperty("name").setValue(val);
						configTree.requestRepaint();
						Application.trayMessage("updated");
					}
				} catch (Exception e) {
					Application.showError(e.getMessage());
				}

			}
		});
		form.getFooter().addComponent(saveButton);

		setPropertyPanel(panel);
	}

	private void onIndexesNodeSelected() {
		Panel dirPanel = new Panel("Indexes");
		setPropertyPanel(dirPanel);
	}

	public Action[] getActions(Object target, Object sender) {

		configTree.select(target);

		if (target == indexesNode) {
			return INDEXES_ACTIONS;
		} else if (target instanceof Index) {
			return INDEX_ACTIONS;
		} else if (target instanceof Dir) {
			return DIR_ACTIONS;
		}
		return null;
	}

	public void handleAction(Action action, Object sender, Object target) {

		try {
			if (action == ACTION_ADD_INDEX) {
				addIndex();
			} else if (action == ACTION_ADD_DIR) {
				addDir((Index) target);
			} else if (action == ACTION_DEL_INDEX) {
				deleteIndex((Index) target);
			} else if (action == ACTION_DEL_DIR) {

				deleteDir((Dir) target);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Application.showError(e.getMessage());
		}
	}

	private void addDir(final Index parent) {

		Panel panel = new Panel("Create Dir");
		panel.setIcon(Resources.FOLDER_32);

		final Form form = new Form();
		panel.addComponent(form);
		final Dir val = new Dir();
		BeanItem<Dir> beanItem = new BeanItem<Dir>(val);
		form.setItemDataSource(beanItem);
		form.setInvalidCommitted(false);
		form.setVisibleItemProperties(new String[] { "path" });
		// Field nameField = form.getField("name");

		Button saveButton = new Button("create");
		saveButton.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					if (form.isValid()) {
						form.commit();
						val.setParent(parent);
						ConfigManager.getConfgDb().dao(Dir.class).create(val);

						Item itm = configTree.addItem(val);
						itm.getItemProperty(PROPERTY_NAME).setValue(
								val.getPath());
						itm.getItemProperty(PROPERTY_ICON).setValue(
								Resources.FOLDER_16);

						configTree.setParent(val, parent);
						configTree.setChildrenAllowed(val, false);
						Application.trayMessage("dir created");
						configTree.select(val);
					}
				} catch (Exception e) {
					Application.showError(e.getMessage());
				}

			}
		});
		form.getFooter().addComponent(saveButton);

		setPropertyPanel(panel);
	}

	private void addIndex() {
		Panel panel = new Panel("Create Index");
		panel.setIcon(Resources.INDEX_32);

		final Form form = new Form();
		panel.addComponent(form);
		final Index val = new Index();
		val.setName("New Index");
		BeanItem<Index> beanItem = new BeanItem<Index>(val);
		form.setItemDataSource(beanItem);
		form.setInvalidCommitted(false);
		form.setVisibleItemProperties(new String[] { "name" });

		Button saveButton = new Button("create");
		saveButton.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					if (form.isValid()) {
						form.commit();
						ConfigManager.getConfgDb().dao(Index.class).create(val);
						Item itm = configTree.addItem(val);
						itm.getItemProperty(PROPERTY_NAME).setValue(
								val.toString());
						itm.getItemProperty(PROPERTY_ICON).setValue(
								Resources.INDEX_16);
						configTree.setParent(val, indexesNode);
						Application.trayMessage("index created");
						configTree.select(val);
					}
				} catch (Exception e) {
					LOGGER.error(e);
					Application.showError(e.getMessage());
				}
			}
		});
		form.getFooter().addComponent(saveButton);

		setPropertyPanel(panel);

	}

	private void deleteIndex(final Index target) {
		ConfirmDialog.show(Application.getMain(), "Are you sure?",
				new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								ConfigManager.getConfgDb().dao(Index.class)
										.delete(target);
								Collection childs = configTree
										.getChildren(target);
								for (Object o : childs) {
									configTree.removeItem(o);
								}

								configTree.removeItem(target);
								configTree.requestRepaint();
								Application.trayMessage("dir deleted");
							} catch (Exception e) {
								Application.showError(e.getMessage());
							}
						}

					}
				});

	}

	private void deleteDir(final Dir target) throws SQLException {

		ConfirmDialog.show(Application.getMain(), "Are you sure?",
				new ConfirmDialog.Listener() {

					public void onClose(ConfirmDialog dialog) {
						if (dialog.isConfirmed()) {
							try {
								ConfigManager.getConfgDb().dao(Dir.class)
										.delete(target);
								configTree.removeItem(target);
								Application.trayMessage("dir deleted");
							} catch (Exception e) {
								Application.showError(e.getMessage());
							}
						}

					}
				});
	}

}
