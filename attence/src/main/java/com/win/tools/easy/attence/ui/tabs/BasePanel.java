package com.win.tools.easy.attence.ui.tabs;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 面板基类
 * 
 * @author 袁晓冬
 * 
 */
public class BasePanel extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4137759063237545777L;

	/**
	 * 初始化BorderLayout样式面板
	 */
	protected void initBorderLayout() {
		setLayout(new BorderLayout());
		JComponent toolbar = createToolbarComponent();
		if (null != toolbar) {
			add(toolbar, BorderLayout.NORTH);
		}
		JComponent queryComp = createQueryComponent();
		if (null != queryComp) {
			add(queryComp, BorderLayout.SOUTH);
		}
		JComponent tableComp = createCenterComponent();
		if (null != tableComp) {
			add(tableComp, BorderLayout.CENTER);
		}
	}

	/**
	 * 工具栏面板-NORTH
	 * 
	 * @return JPanel
	 */
	protected JComponent createToolbarComponent() {
		return null;
	}

	/**
	 * 查询面板-SOUTH
	 * 
	 * @return JPanel
	 */
	protected JComponent createQueryComponent() {
		return null;
	}

	/**
	 * table列表面板-CENTER
	 * 
	 * @return JPanel
	 */
	protected JComponent createCenterComponent() {
		return null;
	}
}
