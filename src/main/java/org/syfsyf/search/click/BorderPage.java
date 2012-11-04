package org.syfsyf.search.click;

import java.util.ArrayList;
import java.util.List;

import org.apache.click.Page;
import org.apache.click.control.PageLink;
import org.syfsyf.search.ConfigManager;
import org.syfsyf.search.click.FlashItem.Type;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;

public class BorderPage extends Page {

	public String title = "Page";
	public List mainPageLinks=new ArrayList();
	private List<FlashItem> flashItems=new ArrayList<FlashItem>();
	private List<String> logMessages=new ArrayList<String>();
	
	
	public BorderPage() {
		
		mainPageLinks.add(new PageLink("home",HomePage.class));
		mainPageLinks.add(new PageLink("config",ConfigPage.class));
		addModel("logMessages", logMessages);
	}

	public String getTemplate() {
		return "/border-template.htm";
	}
	public void flashError(Object message){
		flashItems.add(new FlashItem(message,Type.error));
		getContext().setFlashAttribute("flash", flashItems);
	}
	public void flashNotice(Object message){
		flashItems.add(new FlashItem(message,Type.notice));
		getContext().setFlashAttribute("flash", flashItems);
	}
	public void flashSuccess(Object message){
		flashItems.add(new FlashItem(message,Type.success));
		getContext().setFlashAttribute("flash", flashItems);
	}
	public void log(String message){
		logMessages.add(message);
	}
	
	public <T> Dao<T, Integer> dao(Class<T> t) {
		
		return ConfigManager.getConfgDb().dao(t);
	}
}
