package com.win.tools.easy.chat.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.client.service.UserService;
import com.win.tools.easy.chat.entity.User;

/**
 * 聊天窗口
 * 
 * @author 袁晓冬
 * 
 */
public class ChatWindow extends JFrame {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7572403760493778995L;

	/** 聊天窗口默认宽度 */
	private static final int DEFAULT_WITDH = 600;
	/** 聊天窗口默认高度 */
	private static final int DEFAULT_HEIGHT = 400;
	/** 聊天对象 */
	private User user;
	/** 显示用户名称 */
	private JLabel userNameLabel;
	/** 聊天内容显示面板 */
	private JTextPane contentPanel;
	/** 聊天输入面板 */
	private JTextPane inputPanel;
	/**
	 * 聊天窗口滚动面板
	 */
	JScrollPane contentScrollPane = null;

	public ChatWindow() {
		this.setSize(DEFAULT_WITDH, DEFAULT_HEIGHT);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				ClientFactory.removeChatWindow(user);
			}
		});
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		JPanel chatPanel = createChatPanel();
		container.add(chatPanel, BorderLayout.CENTER);
		JPanel userInfoPanel = createUserInfoPanel();
		container.add(userInfoPanel, BorderLayout.EAST);
	}

	/**
	 * 聊天对话面板
	 * 
	 * @return
	 */
	private JPanel createChatPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		// 北部用户名称显示面板
		JPanel userStatusPanel = new JPanel();
		userStatusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userNameLabel = new JLabel();
		userStatusPanel.add(userNameLabel);
		panel.add(userStatusPanel, BorderLayout.NORTH);
		// 聊天面板
		JSplitPane chatSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		chatSplitPane.setDividerSize(2);
		// 上部聊天内容面板
		contentPanel = new JTextPane();
		contentPanel.setEditable(false);
		contentScrollPane = new JScrollPane(contentPanel);
		chatSplitPane.setTopComponent(contentScrollPane);
		// panel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
		// 下部输入框面板
		inputPanel = new JTextPane();
		inputPanel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (KeyEvent.VK_ENTER == e.getKeyCode()) {
					sendMsg();
				}
			}
		});
		chatSplitPane.setBottomComponent(new JScrollPane(inputPanel));
		// panel.add(new JScrollPane(inputPanel), BorderLayout.SOUTH);
		chatSplitPane.setDividerLocation(220);
		panel.add(chatSplitPane, BorderLayout.CENTER);

		// 南部按钮面板
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton sendBtn = new JButton("发送");
		sendBtn.addActionListener(createSendListener());
		functionPanel.add(sendBtn);

		panel.add(functionPanel, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * 【发送】事件处理
	 * 
	 * @return
	 */
	private ActionListener createSendListener() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		};
	}

	/**
	 * 发送消息
	 */
	private void sendMsg() {
		String message = inputPanel.getText();
		if (null == message || message.length() == 0) {
			return;
		}
		// 发送请求
		UserService userService = ClientFactory.getUserService();
		// 用户登录
		userService.sendMesage(message, user.getId());
		inputPanel.setText("");

	}

	/**
	 * 聊天用户信息面板
	 * 
	 * @return
	 */
	private JPanel createUserInfoPanel() {
		return new JPanel();
	}

	/**
	 * 获取窗口聊天对象
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * 设置聊天对象，并根据聊天对象信息设置窗口显示
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		userNameLabel.setText(user.getNickName());
		this.setTitle(user.getNickName());
		this.user = user;
	}

	/**
	 * 追加消息
	 * 
	 * @param str
	 * @param col
	 * @param bold
	 * @param fontSize
	 */
	public void appendMessage(final String str, int color, boolean bold,
			int fontSize) {
		final SimpleAttributeSet attrSet = new SimpleAttributeSet();
		StyleConstants.setForeground(attrSet, new Color(color));
		// 颜色
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		}// 字体类型
		StyleConstants.setFontSize(attrSet, fontSize);
		// 字体大小
		// StyleConstants.setFontFamily(attrSet, "黑体");
		// 设置字体
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				insert(str, attrSet);
				JScrollBar jsb = contentScrollPane.getVerticalScrollBar();
				jsb.setValue(jsb.getMaximum());
			}
		});

	}

	public void insert(String str, AttributeSet attrSet) {
		Document doc = contentPanel.getDocument();
		str = "\n" + str;
		try {
			doc.insertString(doc.getLength(), str, attrSet);
		} catch (BadLocationException e) {
			System.out.println("BadLocationException: " + e);
		}
	}

}
