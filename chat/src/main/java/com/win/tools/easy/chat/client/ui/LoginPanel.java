package com.win.tools.easy.chat.client.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import com.win.tools.easy.chat.client.ClientConfig;
import com.win.tools.easy.chat.client.ClientDaemon;
import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.client.service.UserService;
import com.win.tools.easy.chat.common.PersitentManager;

/**
 * 登录画面
 * 
 * @author 袁晓冬
 * 
 */
public class LoginPanel extends JPanel {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5676755570821117434L;
	private JTextField accountField = null;
	private JPasswordField pwdField = null;

	public LoginPanel() {
		// SpringLayout
		SpringLayout springLayout = new SpringLayout();
		this.setLayout(springLayout);
		// 账号标签
		JLabel accountLabel = new JLabel("账号：");
		this.add(accountLabel);
		springLayout.putConstraint(SpringLayout.NORTH, accountLabel, 150,
				SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, accountLabel, 10,
				SpringLayout.WEST, this);

		// 账号输入框
		accountField = new JTextField("test");
		this.add(accountField);
		springLayout.putConstraint(SpringLayout.NORTH, accountField, 5,
				SpringLayout.SOUTH, accountLabel);
		springLayout.putConstraint(SpringLayout.WEST, accountField, 0,
				SpringLayout.WEST, accountLabel);
		springLayout.putConstraint(SpringLayout.EAST, accountField, -15,
				SpringLayout.EAST, this);

		// 密码标签
		JLabel pwdLabel = new JLabel("密码：");
		this.add(pwdLabel);
		springLayout.putConstraint(SpringLayout.NORTH, pwdLabel, 5,
				SpringLayout.SOUTH, accountField);
		springLayout.putConstraint(SpringLayout.WEST, pwdLabel, 0,
				SpringLayout.WEST, accountField);

		// 密码输入框
		pwdField = new JPasswordField("test");
		this.add(pwdField);
		springLayout.putConstraint(SpringLayout.NORTH, pwdField, 5,
				SpringLayout.SOUTH, pwdLabel);
		springLayout.putConstraint(SpringLayout.WEST, pwdField, 0,
				SpringLayout.WEST, pwdLabel);
		springLayout.putConstraint(SpringLayout.EAST, pwdField, 0,
				SpringLayout.EAST, accountField);
		// 登录按钮面板
		JPanel loginBtnPane = new JPanel();
		loginBtnPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton login = new JButton("登录");
		login.addActionListener(getLoginBtnListener());
		loginBtnPane.add(login);
		this.add(loginBtnPane);

		springLayout.putConstraint(SpringLayout.NORTH, loginBtnPane, 10,
				SpringLayout.SOUTH, pwdField);

		springLayout.putConstraint(SpringLayout.WEST, loginBtnPane, 0,
				SpringLayout.WEST, accountField);
		springLayout.putConstraint(SpringLayout.EAST, loginBtnPane, 0,
				SpringLayout.EAST, accountField);

	}

	private ActionListener getLoginBtnListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Socket socket = ClientFactory.getSocket();
				if (null == socket) {
					PersitentManager manager = PersitentManager.load();
					ClientConfig config = manager.get(ClientConfig.class);
					socket = createSocket(config.getServerIp(),
							config.getPort());
					// config.setServerIp("172.16.124.176");
					// manager.save();
				}
				if (null == socket) {
					JOptionPane.showMessageDialog(null, "服务器无法链接");
					return;
				}
				// 初始化工厂socket
				ClientFactory.setSocket(socket);
				// 启动客户端监听
				startDaemon(socket);
				UserService userService = ClientFactory.getUserService();
				String uname = accountField.getText();
				String pwd = new String(pwdField.getPassword());
				// 用户登录
				userService.login(uname, pwd);
			}
		};
	}

	/**
	 * 客户端监听服务器响应线程
	 */
	private void startDaemon(Socket socket) {
		ClientDaemon clientDaemon = new ClientDaemon();
		clientDaemon.setSocket(socket);
		Thread daemon = new Thread(clientDaemon);
		daemon.setDaemon(true);
		daemon.start();
	}

	/**
	 * 创建服务器链接socket
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	private static Socket createSocket(String address, int port) {
		// 创建Socket
		try {
			return new Socket(address, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
