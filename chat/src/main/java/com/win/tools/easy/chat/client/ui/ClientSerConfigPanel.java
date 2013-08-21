package com.win.tools.easy.chat.client.ui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * 服务器配置画面-客户端
 * 
 * @author
 * 
 */
public class ClientSerConfigPanel extends JPanel {


	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1205130178000839782L;

	public ClientSerConfigPanel() {
		SpringLayout springLayout = new SpringLayout();
		this.setLayout(springLayout);
		// 账号标签
		JLabel accountLabel = new JLabel("服务器地址：");
		this.add(accountLabel);
		springLayout.putConstraint(SpringLayout.NORTH, accountLabel, 5,
				SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, accountLabel, 5,
				SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.EAST, accountLabel, -5,
				SpringLayout.EAST, this);
		// 账号输入框
		JTextField serverIpField = new JTextField("127.0.0.1");
		serverIpField.setPreferredSize(new Dimension(200, 25));
		this.add(serverIpField);
		springLayout.putConstraint(SpringLayout.NORTH, serverIpField, 5,
				SpringLayout.SOUTH, accountLabel);
		springLayout.putConstraint(SpringLayout.WEST, serverIpField, 0,
				SpringLayout.WEST, accountLabel);
		springLayout.putConstraint(SpringLayout.EAST, serverIpField, 0,
				SpringLayout.EAST, accountLabel);

		// 密码标签
		JLabel pwdLabel = new JLabel("端口号：");
		this.add(pwdLabel);
		springLayout.putConstraint(SpringLayout.NORTH, pwdLabel, 5,
				SpringLayout.SOUTH, serverIpField);
		springLayout.putConstraint(SpringLayout.WEST, pwdLabel, 0,
				SpringLayout.WEST, accountLabel);
		springLayout.putConstraint(SpringLayout.EAST, pwdLabel, 0,
				SpringLayout.EAST, accountLabel);

		// 账号输入框
		JTextField portField = new JTextField("13071");
		portField.setPreferredSize(new Dimension(200, 25));
		this.add(portField);
		springLayout.putConstraint(SpringLayout.NORTH, portField, 5,
				SpringLayout.SOUTH, pwdLabel);
		springLayout.putConstraint(SpringLayout.WEST, portField, 0,
				SpringLayout.WEST, accountLabel);
		springLayout.putConstraint(SpringLayout.EAST, portField, 0,
				SpringLayout.EAST, accountLabel);

	}

}
