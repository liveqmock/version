package com.win.tools.easy.chat.server;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.log4j.Logger;

import com.win.tools.easy.chat.common.PersitentManager;
import com.win.tools.easy.chat.common.ServerActionExecuteFilter;
import com.win.tools.easy.chat.common.SimpleFilterChain;
import com.win.tools.easy.chat.server.dao.DBManager;

/**
 * �������������棬���������������
 * 
 * @author Ԭ����
 * 
 */
public class ServerStart extends JFrame {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -36209921965254287L;
	/** ��־��¼ */
	static final Logger LOGGER = Logger.getLogger(ServerStart.class);
	private JTextField urlField = null;
	private JTextField userNameField = null;
	private JPasswordField pwdField = null;
	private JTextField portField = null;

	private ServerSocket serverSocket;

	public ServerStart() {
		this.setTitle("chat service");
		ServiceConfig config = PersitentManager.load().get(ServiceConfig.class);
		LOGGER.debug("service config:" + config);
		this.setSize(400, 300);
		Container container = this.getContentPane();
		SpringLayout springLayout = new SpringLayout();
		container.setLayout(springLayout);
		// ���ݿ�URL��ǩ
		JLabel urlLabel = new JLabel("URL��");
		container.add(urlLabel);
		springLayout.putConstraint(SpringLayout.NORTH, urlLabel, 5,
				SpringLayout.NORTH, container);
		springLayout.putConstraint(SpringLayout.WEST, urlLabel, 5,
				SpringLayout.WEST, container);
		springLayout.putConstraint(SpringLayout.EAST, urlLabel, -5,
				SpringLayout.EAST, container);

		// URL�����
		urlField = new JTextField(config.getConnectionURL());
		container.add(urlField);
		springLayout.putConstraint(SpringLayout.NORTH, urlField, 5,
				SpringLayout.SOUTH, urlLabel);
		springLayout.putConstraint(SpringLayout.WEST, urlField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, urlField, 0,
				SpringLayout.EAST, urlLabel);

		// ���ݿ�userName��ǩ
		JLabel unameLabel = new JLabel("�û�����");
		container.add(unameLabel);
		springLayout.putConstraint(SpringLayout.NORTH, unameLabel, 5,
				SpringLayout.SOUTH, urlField);
		springLayout.putConstraint(SpringLayout.WEST, unameLabel, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, unameLabel, 0,
				SpringLayout.EAST, urlLabel);

		// ���ݿ�userName�����
		userNameField = new JTextField(config.getConnectionUser());
		container.add(userNameField);
		springLayout.putConstraint(SpringLayout.NORTH, userNameField, 5,
				SpringLayout.SOUTH, unameLabel);
		springLayout.putConstraint(SpringLayout.WEST, userNameField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, userNameField, 0,
				SpringLayout.EAST, urlLabel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// ���ݿ�password��ǩ
		JLabel pwdLabel = new JLabel("���룺");
		container.add(pwdLabel);
		springLayout.putConstraint(SpringLayout.NORTH, pwdLabel, 5,
				SpringLayout.SOUTH, userNameField);
		springLayout.putConstraint(SpringLayout.WEST, pwdLabel, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, pwdLabel, 0,
				SpringLayout.EAST, urlLabel);

		// ���ݿ�userName�����
		pwdField = new JPasswordField(config.getConnectionPassword());
		container.add(pwdField);
		springLayout.putConstraint(SpringLayout.NORTH, pwdField, 5,
				SpringLayout.SOUTH, pwdLabel);
		springLayout.putConstraint(SpringLayout.WEST, pwdField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, pwdField, 0,
				SpringLayout.EAST, urlLabel);

		// �������˿ںű�ǩ
		JLabel portLabel = new JLabel("�˿ڣ�");
		container.add(portLabel);
		springLayout.putConstraint(SpringLayout.NORTH, portLabel, 5,
				SpringLayout.SOUTH, pwdField);
		springLayout.putConstraint(SpringLayout.WEST, portLabel, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, portLabel, 0,
				SpringLayout.EAST, urlLabel);
		// �������˿������
		portField = new JTextField(String.valueOf(config.getServerPort()));
		container.add(portField);
		springLayout.putConstraint(SpringLayout.NORTH, portField, 5,
				SpringLayout.SOUTH, portLabel);
		springLayout.putConstraint(SpringLayout.WEST, portField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, portField, 0,
				SpringLayout.EAST, urlLabel);
		// ������ť���
		JPanel startBtnPane = new JPanel();
		startBtnPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton login = new JButton("����");
		login.addActionListener(getStartBtnListener());
		startBtnPane.add(login);
		container.add(startBtnPane);
		springLayout.putConstraint(SpringLayout.NORTH, startBtnPane, 5,
				SpringLayout.SOUTH, portField);

		springLayout.putConstraint(SpringLayout.WEST, startBtnPane, 0,
				SpringLayout.WEST, portField);
		springLayout.putConstraint(SpringLayout.EAST, startBtnPane, 0,
				SpringLayout.EAST, portField);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * �����¼�����
	 * 
	 * @return
	 */
	private ActionListener getStartBtnListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ServerStart object = ServerStart.this;
				object.setVisible(false);
				start();
			}
		};
	}

	public static void main(String[] args) {
		new ServerStart();
	}

	public void start() {
		PersitentManager persitentManager = PersitentManager.load();
		ServiceConfig config = persitentManager.get(ServiceConfig.class);
		String portText = portField.getText();
		int port = config.getServerPort();
		try {
			port = Integer.parseInt(portText);
		} catch (NumberFormatException e) {
			port = config.getServerPort();
		}
		config.setServerPort(port);
		config.setConnectionURL(this.urlField.getText());
		config.setConnectionUser(this.userNameField.getText());
		config.setConnectionPassword(new String(this.pwdField.getPassword()));
		try {
			checkDB(config);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "���ݿ���֤ʧ�ܣ�");
			return;
		}
		persitentManager.put(config);
		persitentManager.save();
		// ����ServerSocket����
		try {
			this.serverSocket = new ServerSocket(port);
			LOGGER.debug("start service at port " + port);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		// ��ʼ��������
		initFilter();
		try {
			while (true) {
				// ����Socket������
				Socket s = this.serverSocket.accept();
				LOGGER.debug("new user connected!");
				// �����������߳�
				new Thread(new ServerSocketExecuter(s)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (null != this.serverSocket) {
				try {
					this.serverSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ��ʼ����������
	 */
	private void initFilter() {
		if (SimpleFilterChain.isEmperty()) {
			// ���ִ�й�������û�д˹�����ServerAction���ᱻִ��
			SimpleFilterChain.addFilter(new ServerActionExecuteFilter());
		}
	}

	private void checkDB(ServiceConfig config) throws SQLException {
		String sql = "SELECT 1 FROM T_CHAT_USER ";
		Connection connection = null;
		ResultSet rs = null;
		try {
			connection = DBManager.getConnection(config);
			PreparedStatement stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
