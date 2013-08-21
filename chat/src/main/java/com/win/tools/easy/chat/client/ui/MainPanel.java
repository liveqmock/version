package com.win.tools.easy.chat.client.ui;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * �ͻ�����ҳ
 * 
 * @author Ԭ����
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
	 * �����û���Ϣ��壨��ʾ�û�ͷ���û����ơ�״̬����Ϣ�ȣ�
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
		// ���ѡ��ؼ�
		JTabbedPane content = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		content.add("ͨѶ¼���", new AddressPane());
		content.add("��֯�ܹ�", new JPanel());
		return content;
	}
}
