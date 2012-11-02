package org.syfsyf.search.webgui;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Config;
import org.syfsyf.search.model.Index;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class ConfigPanel extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER=Logger.getLogger(ConfigPanel.class);
	
	public static final String INDEX_PROPERTY_NAME="name";
	
	private Config config;
	private Label indexDirectoryLabel;
	private IndexedContainer indexesDataSource;

	@SuppressWarnings("serial")
	class FieldFactory extends DefaultFieldFactory {

		@Override
		public Field createField(Item item, Object propertyId,
				Component uiContext) {

			Field field = super.createField(item, propertyId, uiContext);
			if("indexDirectory".equals(propertyId)){
				field.setWidth("500px");
			}
			return field;
		}

	}

	public ConfigPanel(final Config config) {

		setSpacing(true);
		setMargin(true);
		this.config = config;
		// addComponent(new Label(""+config));

		BeanItem<Config> configItem = new BeanItem<Config>(config);
		final Form form = new Form();
		
		form.setCaption("Config");
		addComponent(form);
		form.setWriteThrough(false);
		form.setInvalidCommitted(false);
		form.setItemDataSource(configItem);
		form.setFormFieldFactory(new FieldFactory());
		form.setVisibleItemProperties(Arrays.asList(new String[] {
                "indexDirectory" }));
		
		Button button=new Button("save");
		button.addListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				try {
					form.commit();
					ConfigManager.saveDefualtConfig(config);
					Application.trayMessage("config saved.");
				}
				catch (Exception e) {
					LOGGER.error(e);
					Application.showError(e.getMessage());
				}
			}
		});
		form.getFooter().addComponent(button);
		
		
		Table indexesTable=new Table("Indexes");
		addComponent(indexesTable);
		indexesDataSource=new IndexedContainer();
		
		indexesDataSource.addContainerProperty(INDEX_PROPERTY_NAME, String.class, null);
		
		
		
		indexesTable.setContainerDataSource(indexesDataSource);
		ColumnGenerator generatedColumn=new ColumnGenerator() {
			
			public Object generateCell(Table source, Object itemId, Object columnId) {
				
				final Index index=(Index) itemId;
				HorizontalLayout horizontalLayout=new HorizontalLayout();
				Button removeButton=new Button("delete");
				Button configureButton=new Button("configure");
				horizontalLayout.addComponent(removeButton);
				horizontalLayout.addComponent(configureButton);
				
				removeButton.addListener(new Button.ClickListener() {
					
					public void buttonClick(ClickEvent event) {
						ConfirmDialog.show(Application.getMain(), "Are you sure?",new ConfirmDialog.Listener() {
							
							public void onClose(ConfirmDialog dialog) {
								if(dialog.isConfirmed()){
									config.getIndexs().remove(index);
									Application.trayMessage("index deleted");
									fillIndexesDataSource();	
								}	
							}
						});
					}
				});
				configureButton.addListener(new Button.ClickListener() {
					public void buttonClick(ClickEvent event) {
						
						Window window=new Window("Index");
						window.addComponent(new IndexPanel(config, index));
						Application.getMain().addWindow(window);
						
						
					}
				});
				return horizontalLayout;
			}
		};
		indexesTable.addGeneratedColumn("actions", generatedColumn);
		indexesTable.setHeight("300px");
		
		
		fillIndexesDataSource();
		
		HorizontalLayout horizontalLayout=new HorizontalLayout();
		horizontalLayout.setSpacing(true);
		
		Button createIndexButton=new Button("create index");
		createIndexButton.addListener(new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				createIndex();
				
			}
		});
		horizontalLayout.addComponent(createIndexButton);
		
		
		addComponent(horizontalLayout);
		
		
		
	}

	protected void createIndex() {
		
		Index index=new Index();
		index.setName("new index");
		config.getIndexs().add(index);
		
		fillIndexesDataSource();
		Application.trayMessage("new index created");
		
		
	}

	private void fillIndexesDataSource() {
		indexesDataSource.removeAllItems();
		
		
		for(Index index:config.getIndexs()){
			
			Item item = indexesDataSource.addItem(index);
			item.getItemProperty(INDEX_PROPERTY_NAME).setValue(index.getName());
		}
		
	}

	

}
