package com.win.tools.easy.other.ui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;

import javax.swing.JTabbedPane;

import com.win.tools.easy.other.ui.tabs.DDLFormatPanel;
import com.win.tools.easy.other.ui.tabs.RegFormatPanel;
import com.win.tools.easy.other.ui.tabs.SqlFormatPanel;
import com.win.tools.easy.platform.ui.InternalFrame;

/**
 * 格式化工具
 * 
 * @author 袁晓冬
 * 
 */
public class FormatFrame {
	/** 窗口对象 */
	private InternalFrame internalFrame;
	/** 选项卡 */
	private JTabbedPane tabbedPane = null;

	/**
	 * 构造函数
	 * 
	 * @param internalFrame
	 */
	public FormatFrame(InternalFrame internalFrame) {
		internalFrame.setLayout(new BorderLayout());
		this.internalFrame = internalFrame;
	}

	/**
	 * 显示画面
	 */
	public void show() {
		try {
			internalFrame.setMaximum(true);
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		setContentPanel();
		internalFrame.setVisible(true);
	}

	/**
	 * 设置内容面板
	 */
	public void setContentPanel() {
		// 添加选项卡控件
		tabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addTab("SQL格式化", new SqlFormatPanel());
		tabbedPane.addTab("DLL格式化", new DDLFormatPanel());
		tabbedPane.addTab("正则表达式导出", new RegFormatPanel());
		internalFrame.add(tabbedPane, BorderLayout.CENTER);
	}
}
