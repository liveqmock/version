package com.win.tools.easy.chat.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * 客户端主页
 * 
 * @author 袁晓冬
 * 
 */
public class MainPanel extends JPanel {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -415910454098428393L;

	public MainPanel() {
		this.setLayout(new BorderLayout());
		JPanel userInfoPane = createUserInfoPane();
		this.add(userInfoPane, BorderLayout.NORTH);
		JTabbedPane content = createContentPane();
		this.add(content, BorderLayout.CENTER);
	}

	/**
	 * 北部用户信息面板（显示用户头像、用户名称、状态、消息等）
	 * 
	 * @return
	 */
	private JPanel createUserInfoPane() {
		JPanel userInfoPane = new JPanel();
		JLabel todo = new JLabel("TODO");
		userInfoPane.add(todo);
		return userInfoPane;
	}

	private JTabbedPane createContentPane() {
		// 添加选项卡控件
		JTabbedPane content = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		content.add("通讯录面板", new AddressPane());
		content.add("组织架构", new JPanel());
		return content;
	}
}
