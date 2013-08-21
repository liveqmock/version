package com.win.tools.easy.db;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.border.TitledBorder;

import com.win.tools.easy.platform.ui.InternalFrame;
import com.win.tools.easy.platform.ui.PlatformInterface;

/**
 * 控件工厂<br>
 * 单例模式
 * 
 * @author 袁晓冬
 * 
 */
public class ComponentFactory {
	/** 控件高度 */
	private final static int HEIGHT = 40;
	/** 单例对象 */
	private static final ComponentFactory instance = createInstance();
	/** 数据库连接信息对象 */
	private ConnectInfo connectInfo;
	/** 工具主面板 */
	private DatabaseFrame databaseFrame;

	private static ComponentFactory createInstance() {
		return new ComponentFactory();
	}

	/**
	 * 获取控件工厂对象
	 * 
	 * @return
	 */
	public static ComponentFactory getInstance() {
		return instance;
	}

	/**
	 * 创建打开链接窗口
	 * 
	 * @return
	 */
	public JDialog createOpenConnectionDialog(
			final PlatformInterface platformInterface) {
		JFrame frame = platformInterface.getApplicationFrame();
		final JDialog dialog = new JDialog(frame);
		dialog.setTitle("连接");
		dialog.setModal(true);
		dialog.setBounds(frame.getX() + frame.getWidth() / 2, frame.getY()
				+ frame.getHeight() / 2, 400, 300);
		SpringLayout springLayout = new SpringLayout();
		dialog.setLayout(springLayout);

		// JDBC Driver
		JLabel jdbcLabel = new JLabel("JDBC Driver");
		jdbcLabel.setSize(jdbcLabel.getWidth(), HEIGHT);
		dialog.getContentPane().add(jdbcLabel);
		springLayout.putConstraint(SpringLayout.NORTH, jdbcLabel, 5,
				SpringLayout.NORTH, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, jdbcLabel, 5,
				SpringLayout.WEST, dialog.getContentPane());
		final JTextField jdbcTextField = new JTextField("org.h2.Driver");
		jdbcTextField.setSize(jdbcTextField.getWidth(), HEIGHT);
		springLayout.putConstraint(SpringLayout.NORTH, jdbcTextField, 5,
				SpringLayout.NORTH, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, jdbcTextField, 5,
				SpringLayout.EAST, jdbcLabel);
		springLayout.putConstraint(SpringLayout.EAST, jdbcTextField, -5,
				SpringLayout.EAST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, jdbcLabel, 0,
				SpringLayout.NORTH, jdbcTextField);
		dialog.add(jdbcTextField);
		// URL
		JLabel urlLabel = new JLabel("URL");
		urlLabel.setSize(urlLabel.getWidth(), HEIGHT);
		dialog.getContentPane().add(urlLabel);
		springLayout.putConstraint(SpringLayout.NORTH, urlLabel, 5,
				SpringLayout.SOUTH, jdbcLabel);
		springLayout.putConstraint(SpringLayout.WEST, urlLabel, 5,
				SpringLayout.WEST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, urlLabel, 0,
				SpringLayout.EAST, jdbcLabel);
		final JTextField urlTextField = new JTextField(
				"jdbc:h2:tcp://localhost/~/test");
		urlTextField.setSize(urlTextField.getWidth(), HEIGHT);
		springLayout.putConstraint(SpringLayout.NORTH, urlTextField, 5,
				SpringLayout.SOUTH, jdbcTextField);
		springLayout.putConstraint(SpringLayout.WEST, urlTextField, 5,
				SpringLayout.EAST, urlLabel);
		springLayout.putConstraint(SpringLayout.EAST, urlTextField, -5,
				SpringLayout.EAST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, urlLabel, 0,
				SpringLayout.NORTH, urlTextField);
		dialog.add(urlTextField);

		// User
		JLabel userLabel = new JLabel("User");
		userLabel.setSize(userLabel.getWidth(), HEIGHT);
		dialog.getContentPane().add(userLabel);
		springLayout.putConstraint(SpringLayout.NORTH, userLabel, 5,
				SpringLayout.SOUTH, urlLabel);
		springLayout.putConstraint(SpringLayout.WEST, userLabel, 5,
				SpringLayout.WEST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, userLabel, 0,
				SpringLayout.EAST, urlLabel);
		final JTextField userTextField = new JTextField("sa");
		userTextField.setSize(userTextField.getWidth(), HEIGHT);
		springLayout.putConstraint(SpringLayout.NORTH, userTextField, 5,
				SpringLayout.SOUTH, urlTextField);
		springLayout.putConstraint(SpringLayout.WEST, userTextField, 5,
				SpringLayout.EAST, userLabel);
		springLayout.putConstraint(SpringLayout.EAST, userTextField, -5,
				SpringLayout.EAST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, userLabel, 0,
				SpringLayout.NORTH, userTextField);
		dialog.add(userTextField);
		// Password
		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setSize(passwordLabel.getWidth(), HEIGHT);
		dialog.getContentPane().add(passwordLabel);
		springLayout.putConstraint(SpringLayout.NORTH, passwordLabel, 5,
				SpringLayout.SOUTH, userLabel);
		springLayout.putConstraint(SpringLayout.WEST, passwordLabel, 5,
				SpringLayout.WEST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, passwordLabel, 0,
				SpringLayout.EAST, userLabel);
		final JTextField passwordTextField = new JPasswordField();
		passwordTextField.setSize(passwordTextField.getWidth(), HEIGHT);
		springLayout.putConstraint(SpringLayout.NORTH, passwordTextField, 5,
				SpringLayout.SOUTH, userTextField);
		springLayout.putConstraint(SpringLayout.WEST, passwordTextField, 5,
				SpringLayout.EAST, passwordLabel);
		springLayout.putConstraint(SpringLayout.EAST, passwordTextField, -5,
				SpringLayout.EAST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, passwordLabel, 0,
				SpringLayout.NORTH, passwordTextField);
		dialog.add(passwordTextField);
		// 确认按钮
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 创建窗口对象
				InternalFrame databaseInternalFrame = platformInterface
						.createInternalFrame();
				databaseInternalFrame.setTitle("EasyDB");
				// 获取数据库连接信息
				connectInfo = new ConnectInfo();
				connectInfo.setDriver(jdbcTextField.getText());
				connectInfo.setUrl(urlTextField.getText());
				connectInfo.setUser(userTextField.getText());
				connectInfo.setPassword(passwordTextField.getText());
				// 关闭连接对话框
				dialog.dispose();
				// 创建数据库主面板
				if (null == databaseFrame) {
					databaseFrame = new DatabaseFrame(databaseInternalFrame,
							connectInfo);
				} else {
					databaseFrame.updateConnectionInfo(connectInfo);
				}
				// 连接数据库
				boolean conn = false;
				try {
					conn = databaseFrame.tryConnect();
				} catch (Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(null, ex.getMessage());
				}
				if (conn) {
					// 连接成功显示主面板
					databaseFrame.show();
				} else {
					try {
						databaseInternalFrame.setClosed(true);
					} catch (PropertyVetoException e1) {
					}
				}
			}
		});
		okButton.setSize(okButton.getWidth(), HEIGHT);
		dialog.getContentPane().add(okButton);
		springLayout.putConstraint(SpringLayout.NORTH, okButton, 5,
				SpringLayout.SOUTH, passwordLabel);
		springLayout.putConstraint(SpringLayout.WEST, okButton, 5,
				SpringLayout.WEST, dialog.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, okButton, 0,
				SpringLayout.EAST, passwordLabel);
		return dialog;
	}

	/**
	 * 创建选项窗口
	 * 
	 * @return
	 */
	public JDialog createOptionDialog(final PlatformInterface platformInterface) {
		JFrame frame = platformInterface.getApplicationFrame();
		JDialog dialog = new JDialog(frame);
		dialog.setLayout(new BorderLayout());
		dialog.setTitle("选项");
		dialog.setModal(true);
		dialog.setBounds(frame.getX() + frame.getWidth() / 2, frame.getY()
				+ frame.getHeight() / 2, 600, 400);
		// 添加选项卡控件
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		dialog.add(tabbedPane, BorderLayout.CENTER);
		/** 数据库选项卡面板 */
		JPanel dataBasePanel = new JPanel();
		dataBasePanel.setLayout(new GridLayout(3, 1));
		// data 面板
		JPanel dataJPanel = new JPanel();
		dataJPanel.setBorder(new TitledBorder("数据"));
		dataJPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel dataLabel = new JLabel("缓存大小:");
		dataJPanel.add(dataLabel);
		JTextField dataField = new JTextField(5);
		dataJPanel.add(dataField);
		JLabel area = new JLabel("200~500");
		dataJPanel.add(area);
		dataBasePanel.add(dataJPanel);
		tabbedPane.addTab("数据库", dataBasePanel);
		/** JDBC驱动 */
		JPanel jdbcPackages = new JPanel();
		jdbcPackages.setLayout(new BorderLayout());
		tabbedPane.addTab("JDBC驱动", jdbcPackages);
		// jdbc驱动列表
		DefaultListModel jdbcListModel = new DefaultListModel();
		JList jdbclist = new JList(jdbcListModel);
		jdbcPackages.add(new JScrollPane(jdbclist), BorderLayout.CENTER);
		JPanel jdbcBtnPanel = new JPanel();
		jdbcBtnPanel.setLayout(new GridLayout(10, 1));
		jdbcBtnPanel.setSize(20, 100);
		JButton addButton = new JButton("添加");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		JButton removeButton = new JButton("删除");
		jdbcBtnPanel.add(addButton);
		jdbcBtnPanel.add(removeButton);
		jdbcPackages.add(jdbcBtnPanel, BorderLayout.EAST);

		JPanel optionBtnPanel = new JPanel();
		JButton todoButton = new JButton("todo");
		optionBtnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		optionBtnPanel.add(todoButton);
		dialog.add(optionBtnPanel, BorderLayout.SOUTH);
		return dialog;
	}

	/**
	 * 配置选项
	 * 
	 * @author 袁晓冬
	 * 
	 */
	public static class Option implements Serializable {

		/**
		 * 序列号
		 */
		private static final long serialVersionUID = -2979687997909430262L;

		private int fetchBufferSize = 200;

		private List<String> jdbcPackages = new ArrayList<String>();

		public int getFetchBufferSize() {
			return fetchBufferSize;
		}

		public void setFetchBufferSize(int fetchBufferSize) {
			this.fetchBufferSize = fetchBufferSize;
		}

		public List<String> getJdbcPackages() {
			return jdbcPackages;
		}

		public void setJdbcPackages(List<String> jdbcPackages) {
			this.jdbcPackages = jdbcPackages;
		}

		@Override
		public String toString() {
			String string = jdbcPackages.toString() + fetchBufferSize;
			return string;
		}

	}
}
