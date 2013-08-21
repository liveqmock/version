package com.win.tools.easy.platform.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.win.tools.easy.common.ExtClasspathLoader;
import com.win.tools.easy.common.PersitentManager;
import com.win.tools.easy.platform.PlatFormConfig;
import com.win.tools.easy.platform.PlatformPlugin;
import com.win.tools.easy.platform.entity.PluginManagerEntity;

/**
 * 程序启动类<br>
 * 创建引用程序面板<br>
 * 加载插件
 * 
 * @author 袁晓冬
 * 
 */
public class FrameStart {
	/** 日志记录 */
	private final static Logger LOG = Logger.getLogger(FrameStart.class);
	/** 插件对象缓存 */
	private final static Map<String, PlatformPlugin> pluginMap = new HashMap<String, PlatformPlugin>();

	/**
	 * 停止插件，具体行为由具体插件实现
	 * 
	 * @param id
	 */
	public static void stopPlugin(String id) {
		PlatformPlugin plugin = pluginMap.get(id);
		plugin.stop();
	}

	/**
	 * 按加载顺序排序并启动插件
	 * 
	 * @param plugins
	 * @param platFormFrame
	 */
	private static void startPlugins(List<PluginManagerEntity> plugins) {
		if (null == plugins || plugins.size() == 0) {
			return;
		}
		Collections.sort(plugins, new Comparator<PluginManagerEntity>() {
			@Override
			public int compare(PluginManagerEntity o1, PluginManagerEntity o2) {
				return o1.getLoadIndex() - o2.getLoadIndex();
			}
		});
		// 加载插件
		PersitentManager manager = PersitentManager.load();
		PlatFormConfig config = manager.get(PlatFormConfig.class);
		List<String> disables = config.getDisablePlugins();
		for (PluginManagerEntity plugin : plugins) {
			if (null != disables && disables.size() > 0) {
				if (disables.contains(plugin.getId())) {
					plugin.setStatus(0);
					continue;
				}
			}
			LOG.debug("start plugin:"+plugin.getId());
			for (String pluginClass : plugin.getPluginClassList()) {
				Class<?> clazz = null;
				try {
					clazz = Class.forName(pluginClass);
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
				if (null == clazz) {
					continue;
				}
				if (PlatformPlugin.class.isAssignableFrom(clazz)) {
					try {
						PlatformPlugin pluginObj = (PlatformPlugin) clazz
								.newInstance();
						pluginObj.start();
						pluginMap.put(plugin.getId(), pluginObj);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

				}
			}
		}
	}

	/**
	 * 加载plugins
	 * 
	 * @return
	 */
	protected static List<PluginManagerEntity> readPlugins() {
		List<PluginManagerEntity> pluginManagerEntities = new ArrayList<PluginManagerEntity>();
		/* 读取类路径下的plugin.properties文件 */
		Enumeration<URL> urls = null;
		try {
			urls = ClassLoader.getSystemResources("plugin.properties");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (null == urls) {
			return pluginManagerEntities;
		}
		/* 循环处理每个plugin.properties文件转换为PluginManagerEntity对象 */
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			Properties properties = new Properties();
			InputStream is = null;
			try {
				is = url.openStream();
				properties.load(url.openStream());
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			String pluginClasses = properties.getProperty("pluginClasses");
			// 未指定pluginClasses属性视为无效文件，不进行处理
			if (null == pluginClasses || "".equals(pluginClasses)) {
				continue;
			}
			String[] pluginClass = pluginClasses.split(";");
			PluginManagerEntity entity = new PluginManagerEntity();
			entity.setPluginClassList(pluginClass);
			// 插件作者
			entity.setAuthor(properties.getProperty("author"));
			// 插件名称
			entity.setName(properties.getProperty("name"));
			entity.setId(properties.getProperty("id"));
			entity.setEmail(properties.getProperty("email"));
			entity.setRemark(properties.getProperty("remark"));
			String index = properties.getProperty("loadIndex");
			if (StringUtils.hasLength(index)) {
				try {
					entity.setLoadIndex(Integer.parseInt(index));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			pluginManagerEntities.add(entity);
		}
		LOG.debug("plugin count:" + pluginManagerEntities.size());
		return pluginManagerEntities;
	}

	public static void start(List<PluginManagerEntity> plugins) {
		ExtClasspathLoader.loadClasspath();
		List<PluginManagerEntity> allPlugins = readPlugins();
		if (null != plugins && plugins.size() > 0) {
			allPlugins.addAll(plugins);
		}
		PlatFormFrame frame = FrameFactory.getPlatFormFrame();
		LOG.debug("set main frame visible!");
		frame.setVisible(true);
		FrameFactory.setPlugins(allPlugins);
		LOG.debug("start plugins!");
		startPlugins(allPlugins);
	}

	public static void start(PluginManagerEntity plugin) {
		start(Collections.singletonList(plugin));
	}

	public static void start() {
		start((List<PluginManagerEntity>) null);
	}

	/**
	 * 启动
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		start();
	}
}
