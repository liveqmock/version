package com.win.tools.easy.common;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 把ext、plugins、resources路径下文件加载到classpath中。
 * 
 * @author 袁晓冬
 * 
 */
public final class ExtClasspathLoader {
	private static final Logger LOGGER = Logger
			.getLogger(ExtClasspathLoader.class);
	private static final String BASE_PATH = initBasePath();
	/** URLClassLoader的addURL方法 */
	private static Method addURL = initAddMethod();

	private static URLClassLoader classloader = (URLClassLoader) ClassLoader
			.getSystemClassLoader();

	/**
	 * 初始化基类路径
	 * 
	 * @return
	 */
	private static String initBasePath() {
		String path = ExtClasspathLoader.class.getProtectionDomain()
				.getCodeSource().getLocation().getFile();
		try {
			path = URLDecoder.decode(path, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		LOGGER.debug("base path:" + path);
		return path;
	}

	/**
	 * 初始化addUrl 方法.
	 * 
	 * @return 可访问addUrl方法的Method对象
	 */
	private static Method initAddMethod() {
		Method add = null;
		try {
			add = URLClassLoader.class.getDeclaredMethod("addURL",
					new Class[] { URL.class });

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		add.setAccessible(true);
		return add;
	}

	/**
	 * 加载jar classpath。
	 */
	public static void loadClasspath() {
		List<String> files = getJarFiles();
		if (null != files) {
			for (String f : files) {
				loadClasspath(f);
			}
		}
		List<String> resFiles = getResFiles();
		if (null != resFiles) {
			for (String r : resFiles) {
				loadResourceDir(r);
			}
		}
	}

	private static void loadClasspath(String filepath) {
		File file = new File(filepath);
		loopFiles(file);
	}

	private static void loadResourceDir(String filepath) {
		File file = new File(filepath);
		loopDirs(file);
	}

	/**
	 * 循环遍历目录，找出所有的资源路径。
	 * 
	 * @param file
	 *            当前遍历文件
	 */
	private static void loopDirs(File file) {
		if (!file.exists()) {
			return;
		}
		// 资源文件只加载路径
		if (file.isDirectory()) {
			addURL(file);
			File[] tmps = file.listFiles();
			for (File tmp : tmps) {
				loopDirs(tmp);
			}
		}
	}

	/**
	 * 循环遍历目录，找出所有的jar包。
	 * 
	 * @param file
	 *            当前遍历文件
	 */
	private static void loopFiles(File file) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] tmps = file.listFiles();
			for (File tmp : tmps) {
				loopFiles(tmp);
			}
		} else {
			if (file.getAbsolutePath().endsWith(".jar")
					|| file.getAbsolutePath().endsWith(".zip")) {
				addURL(file);
			}
		}
	}

	/**
	 * 通过filepath加载文件到classpath。
	 * 
	 * @param filePath
	 *            文件路径
	 * @return URL
	 * @throws Exception
	 *             异常
	 */
	public static void addURL(File file) {
		if (!file.exists()) {
			return;
		}
		try {
			LOGGER.debug("add url to classPath:" + file.getAbsolutePath());
			addURL.invoke(classloader, new Object[] { file.toURI().toURL() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从配置文件中得到配置的需要加载到classpath里的路径集合。
	 * 
	 * @return
	 */
	private static List<String> getJarFiles() {
		File appFile = new File(BASE_PATH);
		List<String> pathList = new ArrayList<String>(2);
		pathList.add(appFile.getParent() + File.separator + "ext");
		pathList.add(appFile.getParent() + File.separator + "plugins");
		return pathList;
	}

	/**
	 * 从配置文件中得到配置的需要加载classpath里的资源路径集合<br>
	 * 添加/resources目录为资源文件目录
	 * 
	 * @return
	 */
	private static List<String> getResFiles() {
		File appFile = new File(BASE_PATH);
		List<String> pathList = new ArrayList<String>(2);
		pathList.add(appFile.getParentFile().getAbsolutePath() + File.separator
				+ "resources");
		return pathList;
	}

	// public static void main(String[] args) {
	// ExtClasspathLoader.loadClasspath();
	// }
}