package org.syfsyf.search.webgui;

import org.syfsyf.search.Main;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Window;

public class Application extends com.vaadin.Application {

	private static ThreadLocal<Application> currentApplication = new ThreadLocal<Application>();
	private TabSheet tabSheet;

	@Override
	public void init() {
		try {
			setTheme("custom");
			currentApplication.set(this);
			Window mainWindow = new Window("Searcher");
			setMainWindow(mainWindow);

			this.tabSheet = new TabSheet();
			tabSheet.setWidth("100%");
			tabSheet.setHeight("650px");
			mainWindow.addComponent(tabSheet);

			Tab tabSettings = tabSheet.addTab(new SettingsPanel(), "Settings");
			//Tab tabConfig = tabSheet.addTab(new ConfigureIndexesPanel(), "Config");
			Tab tabSearch = tabSheet.addTab(new SearchPanel(), "Search");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
			// TODO: handle exception
		}

	}

	public static Application getCurrent() {
		return currentApplication.get();
	}

	public static Window getMain() {
		return getCurrent().getMainWindow();
	}

	public static void showError(String message) {
		getMain().showNotification(message,
				Window.Notification.TYPE_ERROR_MESSAGE);
	}

	public static void showWarning(String message) {
		getMain().showNotification(message,
				Window.Notification.TYPE_WARNING_MESSAGE);
	}

	public static void showMessage(String message) {
		getMain().showNotification(message,
				Window.Notification.TYPE_HUMANIZED_MESSAGE);
	}

	public static void trayMessage(String message) {
		getMain().showNotification(message,
				Window.Notification.TYPE_TRAY_NOTIFICATION);
	}
}
