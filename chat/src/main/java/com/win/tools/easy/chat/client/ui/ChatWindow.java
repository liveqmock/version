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
 * ���촰��
 * 
 * @author Ԭ����
 * 
 */
public class ChatWindow extends JFrame {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7572403760493778995L;

	/** ���촰��Ĭ�Ͽ�� */
	private static final int DEFAULT_WITDH = 600;
	/** ���촰��Ĭ�ϸ߶� */
	private static final int DEFAULT_HEIGHT = 400;
	/** ������� */
	private User user;
	/** ��ʾ�û����� */
	private JLabel userNameLabel;
	/** ����������ʾ��� */
	private JTextPane contentPanel;
	/** ����������� */
	private JTextPane inputPanel;
	/**
	 * ���촰�ڹ������
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
	 * ����Ի����
	 * 
	 * @return
	 */
	private JPanel createChatPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		// �����û�������ʾ���
		JPanel userStatusPanel = new JPanel();
		userStatusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		userNameLabel = new JLabel();
		userStatusPanel.add(userNameLabel);
		panel.add(userStatusPanel, BorderLayout.NORTH);
		// �������
		JSplitPane chatSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		chatSplitPane.setDividerSize(2);
		// �ϲ������������
		contentPanel = new JTextPane();
		contentPanel.setEditable(false);
		contentScrollPane = new JScrollPane(contentPanel);
		chatSplitPane.setTopComponent(contentScrollPane);
		// panel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
		// �²���������
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

		// �ϲ���ť���
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton sendBtn = new JButton("����");
		sendBtn.addActionListener(createSendListener());
		functionPanel.add(sendBtn);

		panel.add(functionPanel, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * �����͡��¼�����
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
	 * ������Ϣ
	 */
	private void sendMsg() {
		String message = inputPanel.getText();
		if (null == message || message.length() == 0) {
			return;
		}
		// ��������
		UserService userService = ClientFactory.getUserService();
		// �û���¼
		userService.sendMesage(message, user.getId());
		inputPanel.setText("");

	}

	/**
	 * �����û���Ϣ���
	 * 
	 * @return
	 */
	private JPanel createUserInfoPanel() {
		return new JPanel();
	}

	/**
	 * ��ȡ�����������
	 * 
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * ����������󣬲��������������Ϣ���ô�����ʾ
	 * 
	 * @param user
	 */
	public void setUser(User user) {
		userNameLabel.setText(user.getNickName());
		this.setTitle(user.getNickName());
		this.user = user;
	}

	/**
	 * ׷����Ϣ
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
		// ��ɫ
		if (bold == true) {
			StyleConstants.setBold(attrSet, true);
		}// ��������
		StyleConstants.setFontSize(attrSet, fontSize);
		// �����С
		// StyleConstants.setFontFamily(attrSet, "����");
		// ��������
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
