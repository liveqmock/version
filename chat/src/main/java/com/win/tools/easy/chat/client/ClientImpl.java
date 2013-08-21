package com.win.tools.easy.chat.client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.win.tools.easy.chat.client.ui.ClientConfigPanel;
import com.win.tools.easy.chat.client.ui.LoginPanel;
import com.win.tools.easy.chat.client.ui.MainPanel;
import com.win.tools.easy.chat.entity.User;

/**
 * �ͻ�����ڻ���
 * 
 * @author Ԭ����
 * 
 */
public class ClientImpl extends JFrame implements Client {
	/** serialVersionUID */
	private static final long serialVersionUID = 3305234817858820448L;
	/** ��־��¼ */
	static final Logger LOGGER = Logger.getLogger(ClientImpl.class);
	/** ��¼��� */
	private LoginPanel loginPanel = null;
	/** ��ҳ */
	private MainPanel mainPanel = null;
	/** Ĭ�Ͽ�� */
	public static final int DEFAULT_WITDH = 250;
	/** Ĭ�ϸ߶� */
	public static final int DEFAULT_HEIGTH = 450;
	CardLayout contentCardLayout = null;
	JPanel cardPanel = null;

	public ClientImpl() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("XXX��˾");
		this.setSize(DEFAULT_WITDH, DEFAULT_HEIGTH);
		this.setPreferredSize(new Dimension(DEFAULT_WITDH, DEFAULT_HEIGTH));
		this.setVisible(true);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		JMenuBar menuBar = createMenuBar();
		this.setJMenuBar(menuBar);
		mainPanel = new MainPanel();
		loginPanel = new LoginPanel();
		cardPanel = new JPanel();
		contentCardLayout = new CardLayout();
		cardPanel.setLayout(contentCardLayout);
		cardPanel.add(mainPanel, "mainPanel");
		cardPanel.add(loginPanel, "loginPanel");

		contentPane.add(cardPanel, BorderLayout.CENTER);
	}

	/**
	 * �����˵�
	 * 
	 * @return
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu filemenu = new JMenu("�ļ�");
//		menuBar.add(filemenu);
		
		JMenuItem configmenuItem = new JMenuItem("ϵͳ����");
		configmenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ClientConfigPanel currFrame =  new ClientConfigPanel();
				ClientConfigPanel.setFrame(currFrame);
				currFrame.setVisible(true);
			}
		});
		
		
//		JMenu configmenu = new JMenu();
		filemenu.add(configmenuItem);
		
		menuBar.add(filemenu);
		return menuBar;
	}

	@Override
	public void moveToMainPage() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// ��ǰ�ѵ�¼
				if (ClientContext.getInstance().isLogin()) {
					contentCardLayout.show(cardPanel, "mainPanel");
				} else {
					contentCardLayout.show(cardPanel, "loginPanel");
				}
				ClientImpl.this.pack();
			}
		});
	}

	@Override
	public void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane.showMessageDialog(null, message);
			}
		});
	}

	@Override
	public void showUsers(final Collection<User> users) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DefaultListModel listModel = ClientFactory.getUserListModel();
				listModel.clear();
				if (null != users && users.size() != 0) {
					for (User u : users) {
						listModel.addElement(u);
					}
				}
			}
		});
	}
}
