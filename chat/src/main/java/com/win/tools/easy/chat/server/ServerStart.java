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
 * 服务器启动画面，配置相关启动参数
 * 
 * @author 袁晓冬
 * 
 */
public class ServerStart extends JFrame {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -36209921965254287L;
	/** 日志记录 */
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
		// 数据库URL标签
		JLabel urlLabel = new JLabel("URL：");
		container.add(urlLabel);
		springLayout.putConstraint(SpringLayout.NORTH, urlLabel, 5,
				SpringLayout.NORTH, container);
		springLayout.putConstraint(SpringLayout.WEST, urlLabel, 5,
				SpringLayout.WEST, container);
		springLayout.putConstraint(SpringLayout.EAST, urlLabel, -5,
				SpringLayout.EAST, container);

		// URL输入框
		urlField = new JTextField(config.getConnectionURL());
		container.add(urlField);
		springLayout.putConstraint(SpringLayout.NORTH, urlField, 5,
				SpringLayout.SOUTH, urlLabel);
		springLayout.putConstraint(SpringLayout.WEST, urlField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, urlField, 0,
				SpringLayout.EAST, urlLabel);

		// 数据库userName标签
		JLabel unameLabel = new JLabel("用户名：");
		container.add(unameLabel);
		springLayout.putConstraint(SpringLayout.NORTH, unameLabel, 5,
				SpringLayout.SOUTH, urlField);
		springLayout.putConstraint(SpringLayout.WEST, unameLabel, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, unameLabel, 0,
				SpringLayout.EAST, urlLabel);

		// 数据库userName输入框
		userNameField = new JTextField(config.getConnectionUser());
		container.add(userNameField);
		springLayout.putConstraint(SpringLayout.NORTH, userNameField, 5,
				SpringLayout.SOUTH, unameLabel);
		springLayout.putConstraint(SpringLayout.WEST, userNameField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, userNameField, 0,
				SpringLayout.EAST, urlLabel);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 数据库password标签
		JLabel pwdLabel = new JLabel("密码：");
		container.add(pwdLabel);
		springLayout.putConstraint(SpringLayout.NORTH, pwdLabel, 5,
				SpringLayout.SOUTH, userNameField);
		springLayout.putConstraint(SpringLayout.WEST, pwdLabel, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, pwdLabel, 0,
				SpringLayout.EAST, urlLabel);

		// 数据库userName输入框
		pwdField = new JPasswordField(config.getConnectionPassword());
		container.add(pwdField);
		springLayout.putConstraint(SpringLayout.NORTH, pwdField, 5,
				SpringLayout.SOUTH, pwdLabel);
		springLayout.putConstraint(SpringLayout.WEST, pwdField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, pwdField, 0,
				SpringLayout.EAST, urlLabel);

		// 服务器端口号标签
		JLabel portLabel = new JLabel("端口：");
		container.add(portLabel);
		springLayout.putConstraint(SpringLayout.NORTH, portLabel, 5,
				SpringLayout.SOUTH, pwdField);
		springLayout.putConstraint(SpringLayout.WEST, portLabel, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, portLabel, 0,
				SpringLayout.EAST, urlLabel);
		// 服务器端口输入框
		portField = new JTextField(String.valueOf(config.getServerPort()));
		container.add(portField);
		springLayout.putConstraint(SpringLayout.NORTH, portField, 5,
				SpringLayout.SOUTH, portLabel);
		springLayout.putConstraint(SpringLayout.WEST, portField, 0,
				SpringLayout.WEST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, portField, 0,
				SpringLayout.EAST, urlLabel);
		// 启动按钮面板
		JPanel startBtnPane = new JPanel();
		startBtnPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		JButton login = new JButton("启动");
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
	 * 启动事件处理
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
			JOptionPane.showMessageDialog(null, "数据库验证失败！");
			return;
		}
		persitentManager.put(config);
		persitentManager.save();
		// 创建ServerSocket对象
		try {
			this.serverSocket = new ServerSocket(port);
			LOGGER.debug("start service at port " + port);
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		// 初始化过滤器
		initFilter();
		try {
			while (true) {
				// 监听Socket的连接
				Socket s = this.serverSocket.accept();
				LOGGER.debug("new user connected!");
				// 启动服务器线程
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
	 * 初始化过滤器链
	 */
	private void initFilter() {
		if (SimpleFilterChain.isEmperty()) {
			// 添加执行过滤器，没有此过滤器ServerAction不会被执行
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
