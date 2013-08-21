package com.win.tools.easy.attence.ui;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;

import javax.swing.JTabbedPane;

import com.win.tools.easy.attence.ui.tabs.AttencePanel;
import com.win.tools.easy.attence.ui.tabs.LeavePanel;
import com.win.tools.easy.attence.ui.tabs.SettingPanel;
import com.win.tools.easy.attence.ui.tabs.UserPanel;
import com.win.tools.easy.attence.ui.tabs.WorkdayPanel;
import com.win.tools.easy.platform.ui.InternalFrame;

/**
 * 考勤管理画面
 * 
 * @author 袁晓冬
 * 
 */
public class AttenceFrame {
	/** 窗口对象 */
	private InternalFrame internalFrame;
	/** 选项卡 */
	private JTabbedPane tabbedPane = null;

	/**
	 * 构造函数
	 * 
	 * @param internalFrame
	 */
	public AttenceFrame(InternalFrame internalFrame) {
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
		tabbedPane.addTab("日常考勤", new AttencePanel());
		tabbedPane.addTab("请假调休", new LeavePanel());
		tabbedPane.addTab("工作日设置", new WorkdayPanel());
		tabbedPane.addTab("人员维护", new UserPanel());
		tabbedPane.addTab("配置", new SettingPanel());
		internalFrame.add(tabbedPane, BorderLayout.CENTER);
	}
}
