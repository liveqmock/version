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
 * ��¼����
 * 
 * @author Ԭ����
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
		// �˺ű�ǩ
		JLabel accountLabel = new JLabel("�˺ţ�");
		this.add(accountLabel);
		springLayout.putConstraint(SpringLayout.NORTH, accountLabel, 150,
				SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, accountLabel, 10,
				SpringLayout.WEST, this);

		// �˺������
		accountField = new JTextField("test");
		this.add(accountField);
		springLayout.putConstraint(SpringLayout.NORTH, accountField, 5,
				SpringLayout.SOUTH, accountLabel);
		springLayout.putConstraint(SpringLayout.WEST, accountField, 0,
				SpringLayout.WEST, accountLabel);
		springLayout.putConstraint(SpringLayout.EAST, accountField, -15,
				SpringLayout.EAST, this);

		// �����ǩ
		JLabel pwdLabel = new JLabel("���룺");
		this.add(pwdLabel);
		springLayout.putConstraint(SpringLayout.NORTH, pwdLabel, 5,
				SpringLayout.SOUTH, accountField);
		springLayout.putConstraint(SpringLayout.WEST, pwdLabel, 0,
				SpringLayout.WEST, accountField);

		// ���������
		pwdField = new JPasswordField("test");
		this.add(pwdField);
		springLayout.putConstraint(SpringLayout.NORTH, pwdField, 5,
				SpringLayout.SOUTH, pwdLabel);
		springLayout.putConstraint(SpringLayout.WEST, pwdField, 0,
				SpringLayout.WEST, pwdLabel);
		springLayout.putConstraint(SpringLayout.EAST, pwdField, 0,
				SpringLayout.EAST, accountField);
		// ��¼��ť���
		JPanel loginBtnPane = new JPanel();
		loginBtnPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton login = new JButton("��¼");
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
					JOptionPane.showMessageDialog(null, "�������޷�����");
					return;
				}
				// ��ʼ������socket
				ClientFactory.setSocket(socket);
				// �����ͻ��˼���
				startDaemon(socket);
				UserService userService = ClientFactory.getUserService();
				String uname = accountField.getText();
				String pwd = new String(pwdField.getPassword());
				// �û���¼
				userService.login(uname, pwd);
			}
		};
	}

	/**
	 * �ͻ��˼�����������Ӧ�߳�
	 */
	private void startDaemon(Socket socket) {
		ClientDaemon clientDaemon = new ClientDaemon();
		clientDaemon.setSocket(socket);
		Thread daemon = new Thread(clientDaemon);
		daemon.setDaemon(true);
		daemon.start();
	}

	/**
	 * ��������������socket
	 * 
	 * @param address
	 * @param port
	 * @return
	 */
	private static Socket createSocket(String address, int port) {
		// ����Socket
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
