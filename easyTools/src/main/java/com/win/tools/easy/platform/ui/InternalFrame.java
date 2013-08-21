package com.win.tools.easy.platform.ui;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.JInternalFrame;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/**
 * 内部窗口基类
 * 
 * @author 袁晓冬
 * 
 */
public class InternalFrame extends JInternalFrame {
	/** 内部窗口默认高度 */
	public final static int DEFAULT_HEIGHT = 500;
	/** 内部窗口默认宽度 */
	public final static int DEFAULT_WITH = 500;
	/**
	 * 窗口ID序列号
	 */
	private static long sequence = 0;

	/**
	 * 获取新窗口ID
	 * 
	 * @return
	 */
	public static long getNextSequence() {
		return sequence++;
	}

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3121589391237535758L;
	/**
	 * 窗口唯一标识
	 */
	private long id;

	/**
	 * 获取窗口唯一标识
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * 设置窗口唯一标识
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * 构造函数
	 * 
	 * @param title
	 */
	public InternalFrame(String title) {
		super(title);
		this.setVisible(false);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMaximizable(true);
		setId(getNextSequence());
		Font font = new Font("微软雅黑", Font.PLAIN, 12);
		initGlobalFontSetting(font);
	}

	public InternalFrame() {
		this("");
	}

	/**
	 * 字体设置
	 * 
	 * @param font
	 */
	public void initGlobalFontSetting(Font font) {
		FontUIResource fontUIResource = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys
				.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontUIResource);
			}
		}
	}

	/**
	 * 获取窗口数据
	 * 
	 * @return
	 */
	public Object getData() {
		throw new UnsupportedOperationException("未实现此方法！");
	}

	/**
	 * 设置窗口数据
	 * 
	 * @param data
	 * @return
	 */
	public boolean setData(Object data) {
		throw new UnsupportedOperationException("未实现此方法！");
	}

	@Override
	public void doDefaultCloseAction() {
		this.setVisible(false);
	}
}
