package org.syfsyf.search.webgui;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.model.Config;
import org.syfsyf.search.model.Index;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.VerticalLayout;

public class IndexPanel extends VerticalLayout {

	private Index index;
	private Config config;
	private static final Logger LOGGER = Logger.getLogger(IndexPanel.class);

	@SuppressWarnings("serial")
	class FieldFactory extends DefaultFieldFactory {

		@Override
		public Field createField(Item item, Object propertyId,
				Component uiContext) {

			Field field = super.createField(item, propertyId, uiContext);

			return field;
		}

	}

	public IndexPanel(final Config config, Index index) {
		this.index = index;
		this.config = config;
		setMargin(true);
		setSpacing(true);

		final Form form = new Form();
		form.setCaption("Index");
		BeanItem<Index> indexItem = new BeanItem<Index>(index);
		form.setWriteThrough(false);
		form.setInvalidCommitted(false);
		form.setItemDataSource(indexItem);
		form.setFormFieldFactory(new FieldFactory());
		form.setVisibleItemProperties(Arrays.asList(new String[] { "name" }));

		addComponent(form);
		Button button = new Button("save");
		button.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				try {
					form.commit();
					ConfigManager.saveDefualtConfig(config);
					Application.trayMessage("config saved.");
				} catch (Exception e) {
					LOGGER.error(e);
					Application.showError(e.getMessage());
				}
			}
		});
		form.getFooter().addComponent(button);

		addComponent(form);

		Button button2 = new Button("test");
		addComponent(button2);
		button2.addListener(new Button.ClickListener() {

			public void buttonClick(ClickEvent event) {
				ConfirmDialog.show(Application.getMain(),
						new ConfirmDialog.Listener() {

							public void onClose(ConfirmDialog arg0) {
								// TODO Auto-generated method stub

							}
						});

			}

		});

	}
}
