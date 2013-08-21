package com.win.tools.easy.attence.ui.tabs;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.win.tools.easy.attence.common.AttenceConfig;
import com.win.tools.easy.attence.service.SettingService;
import com.win.tools.easy.common.PersitentManager;
import com.win.tools.easy.mail.MailSenderInfo;

/**
 * 设置面板
 * 
 * @author 袁晓冬
 * 
 */
public class SettingPanel extends BasePanel {
	/**
	 * serialVersion
	 */
	private static final long serialVersionUID = 4789254913687525489L;
	/** 设置服务类 */
	private SettingService service = null;
	/** 数据库连接 */
	private JTextField connField = null;
	/** 上班时间 */
	private JTextField startTime = null;
	/** 调休开始时间 */
	private JTextField leaveStartTime = null;
	/** 下班时间 */
	private JTextField endTime = null;
	/** 调休结束时间 */
	private JTextField leaveEndTime = null;
	/** 邮件服务器地址 */
	private JTextField hostField = null;
	/** 邮件服务器端口 */
	private JTextField portField = null;
	/** 用户名 */
	private JTextField userNameField = null;
	/** 密码 */
	private JPasswordField passwordField = null;
	/** 发件人 */
	private JTextField fromField = null;

	public SettingPanel() {
		service = new SettingService();
		initBorderLayout();
	}

	/**
	 * 工具栏
	 */
	@Override
	protected JComponent createToolbarComponent() {
		JPanel settingToolbar = new JPanel();
		settingToolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
		JButton saveSettingBtn = new JButton("保存");
		saveSettingBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PersitentManager manager = PersitentManager.load();
				AttenceConfig config = manager.get(AttenceConfig.class);
				config.setConnectionURL(connField.getText());
				config.setStartTime(startTime.getText());
				config.setEndTime(endTime.getText());
				config.setLeaveStartTime(leaveStartTime.getText());
				config.setLeaveEndTime(leaveEndTime.getText());
				MailSenderInfo mailSenderInfo = manager
						.get(MailSenderInfo.class);
				mailSenderInfo.setMailServerHost(hostField.getText());
				mailSenderInfo.setMailServerPort(portField.getText());
				mailSenderInfo.setUserName(userNameField.getText());
				mailSenderInfo.setPassword(new String(passwordField
						.getPassword()));
				mailSenderInfo.setFromAddress(fromField.getText());
				manager.save();
			}
		});
		settingToolbar.add(saveSettingBtn);

		JButton initDatabase = new JButton("初始化数据库");
		initDatabase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				service.initDatabase();
			}
		});
		settingToolbar.add(initDatabase);
		return settingToolbar;
	}

	/**
	 * 中间内容面板
	 */
	@Override
	protected JComponent createCenterComponent() {
		JPanel setting = new JPanel();
		setting.setLayout(new GridLayout(8, 1));
		setting.add(createDatabasePanel());
		setting.add(createAttenceSettingPanel());
		setting.add(createAttenceLayoutPanel());
		setting.add(createMailPanel());
		return setting;
	}

	/**
	 * 数据库配置
	 * 
	 * @return
	 */
	private JPanel createDatabasePanel() {
		JPanel dataBasePanel = new JPanel();
		dataBasePanel.setBorder(new TitledBorder("数据库配置"));
		dataBasePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		connField = new JTextField(20);
		connField.setText(PersitentManager.load().get(AttenceConfig.class)
				.getConnectionURL());
		dataBasePanel.add(new JLabel("连接:"));
		dataBasePanel.add(connField);
		return dataBasePanel;
	}

	/**
	 * 考勤显示配置
	 * 
	 * @return
	 */
	private JPanel createAttenceLayoutPanel() {
		AttenceConfig config = PersitentManager.load().get(AttenceConfig.class);
		JPanel attenceLayoutPanel = new JPanel();
		attenceLayoutPanel.setBorder(new TitledBorder("考勤显示配置"));
		attenceLayoutPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		attenceLayoutPanel.add(new JLabel("异常考勤底色:"));
		final JLabel colorLabel = new JLabel("    ");
		colorLabel.setBackground(config.getAttenceExColor());
		colorLabel.setOpaque(true);
		colorLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				PersitentManager manager = PersitentManager.load();
				AttenceConfig config = manager.get(AttenceConfig.class);
				Color newColor = JColorChooser.showDialog(colorLabel, "选择颜色",
						config.getAttenceExColor());
				if (null != newColor
						&& !newColor.equals(config.getAttenceExColor())) {
					config.setAttenceExColor(newColor);
					colorLabel.setBackground(newColor);
					manager.save();
				}
			}
		});
		attenceLayoutPanel.add(colorLabel);
		return attenceLayoutPanel;
	}

	/**
	 * 考勤配置
	 * 
	 * @return
	 */
	private JPanel createAttenceSettingPanel() {
		AttenceConfig config = PersitentManager.load().get(AttenceConfig.class);
		JPanel attenceSettingPanel = new JPanel();
		attenceSettingPanel.setBorder(new TitledBorder("考勤时间配置"));
		attenceSettingPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		attenceSettingPanel.add(new JLabel("上班时间:"));
		startTime = new JTextField(10);
		startTime.setText(config.getStartTime());
		attenceSettingPanel.add(startTime);
		attenceSettingPanel.add(new JLabel("下班时间:"));
		endTime = new JTextField(10);
		endTime.setText(config.getEndTime());
		attenceSettingPanel.add(endTime);

		// 调休时间设置
		attenceSettingPanel.add(new JLabel("调休开始时间:"));
		leaveStartTime = new JTextField(10);
		leaveStartTime.setText(config.getLeaveStartTime());
		attenceSettingPanel.add(leaveStartTime);
		attenceSettingPanel.add(new JLabel("调休结束时间:"));
		leaveEndTime = new JTextField(10);
		leaveEndTime.setText(config.getLeaveEndTime());
		attenceSettingPanel.add(leaveEndTime);
		return attenceSettingPanel;
	}

	/**
	 * 邮件配置
	 * 
	 * @return
	 */
	private JPanel createMailPanel() {
		MailSenderInfo mailSenderInfo = PersitentManager.load().get(
				MailSenderInfo.class);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder("邮件发送配置"));
		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel("Host"));
		hostField = new JTextField(10);
		hostField.setText(mailSenderInfo.getMailServerHost());
		panel.add(hostField);
		panel.add(new JLabel("Port:"));
		portField = new JTextField(2);
		portField.setText(mailSenderInfo.getMailServerPort());
		panel.add(portField);
		// 登录用户名密码
		panel.add(new JLabel("UserName:"));
		userNameField = new JTextField(6);
		userNameField.setText(mailSenderInfo.getUserName());
		panel.add(userNameField);
		panel.add(new JLabel("password:"));
		passwordField = new JPasswordField(6);
		passwordField.setText(mailSenderInfo.getPassword());
		panel.add(passwordField);
		// 发送邮箱
		panel.add(new JLabel("From:"));
		fromField = new JTextField(15);
		fromField.setText(mailSenderInfo.getFromAddress());
		panel.add(fromField);
		return panel;
	}
}
