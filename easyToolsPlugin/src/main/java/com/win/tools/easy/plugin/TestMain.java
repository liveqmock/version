package com.win.tools.easy.plugin;

import java.util.ArrayList;
import java.util.List;

import com.win.tools.easy.platform.PluginInfo;
import com.win.tools.easy.platform.ui.Manager;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PluginInfo info = new PluginInfo();
		info.setAuthor("author1");
		info.setId("systemInit");
		info.setLoadIndex(-1);
		info.setName("name1");
		info.setStatus(1);
		List<Class<?>> classList = new ArrayList<Class<?>>();
		classList.add(PlatformInitPlugin.class);
		info.setPluginClassList(classList);
		Manager.getInstance().startToolsWithPlugin(info);
	}
}
