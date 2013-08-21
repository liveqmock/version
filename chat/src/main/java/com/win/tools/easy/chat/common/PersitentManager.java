package com.win.tools.easy.chat.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * 系统参数持久化，请确保持久化的参数类型在classPath中。如果移除则所有配置信息将丢失。
 * 
 * @author 袁晓冬
 * 
 */
public class PersitentManager extends HashMap<String, Object> implements
		Serializable {
	/** 日志记录 */
	private static final Logger LOGGER = Logger
			.getLogger(PersitentManager.class);
	private static transient PersitentManager cashe = null;
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 975552221535975650L;

	public Object put(Object value) {
		if (null == value) {
			return null;
		}
		return super.put(value.getClass().getName(), value);
	}

	/**
	 * 根据传入的类型名称作为主键查询数据
	 * 
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(Class<T> clazz) {
		T object = (T) super.get(clazz.getName());
		if (null == object) {
			try {
				object = clazz.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			this.put(clazz.getName(), object);
		}
		return object;
	}

	/**
	 * 保存配置
	 */
	public void save() {
		LOGGER.debug("config file saved");
		File objectFile = new File("chat.sv");
		if (!objectFile.exists()) {
			try {
				LOGGER.debug("创建文件");
				objectFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(objectFile));
			oos.writeObject(this);
			oos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	/**
	 * 加载配置
	 * 
	 * @return
	 */
	public static PersitentManager load() {
		if(cashe != null) {
			return cashe;
		}
		File objectFile = new File("chat.sv");
		PersitentManager persistence = null;
		if (!objectFile.exists()) {
			LOGGER.debug("配置文件不存在!");
			persistence = new PersitentManager();
			persistence.save();
			return persistence;
		}
		LOGGER.debug("配置文件路径：" + objectFile.getAbsolutePath());
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(objectFile));
			persistence = (PersitentManager) ois.readObject();
			cashe = persistence;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			LOGGER.debug("参数读取失败！" + e.toString());
			persistence = new PersitentManager();
		} finally {
			try {
				ois.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return persistence;
	}
}
