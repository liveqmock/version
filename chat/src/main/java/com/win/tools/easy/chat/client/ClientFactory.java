package com.win.tools.easy.chat.client;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.win.tools.easy.chat.client.service.UserService;
import com.win.tools.easy.chat.client.ui.ChatWindow;
import com.win.tools.easy.chat.entity.User;

/**
 * �ͻ��˷���㹤��
 * 
 * @author Ԭ����
 * 
 */
public class ClientFactory {
	/** ��־��¼ */
	private static final Logger LOGGER = Logger.getLogger(ClientFactory.class);
	/** ��������� */
	private static Socket socket = null;
	/** �Ƿ��ѳ�ʼ������ */
	private static boolean isInited = false;
	/** �����Ѵ��������촰�� */
	private static Map<String, ChatWindow> chatWinMap = new HashMap<String, ChatWindow>();

	/**
	 * ����user�û�����Ĵ���
	 * 
	 * @param user
	 * @return
	 */
	public static ChatWindow openChatWindow(User user) {
		ChatWindow win = chatWinMap.get(user.getId());
		if (null == win) {
			win = new ChatWindow();
			win.setUser(user);
			chatWinMap.put(user.getId(), win);
			win.setVisible(true);
		}
		return win;
	}

	/**
	 * ���ݺ���ID��ȡ���촰��
	 * 
	 * @param userId
	 * @return
	 */
	public static ChatWindow openChatWindow(String userId) {
		ChatWindow win = chatWinMap.get(userId);
		if (null == win) {
			win = new ChatWindow();
			chatWinMap.put(userId, win);
			win.setVisible(true);
		}
		return win;
	}

	/**
	 * �ӻ�����ɾ�����촰��
	 * 
	 * @param user
	 */
	public static void removeChatWindow(User user) {
		LOGGER.debug("remove chat window:" + user.getName());
		ChatWindow win = chatWinMap.get(user.getId());
		if (null != win) {
			chatWinMap.remove(user.getId());
		}
	}

	/** �û����� */
	private static UserService userService;

	/** �ͻ���UI�ӿ� */
	private static Client client;
	/** �û���ʾģ�� */
	private static DefaultListModel userListModel;

	/**
	 * ��ȡ�û�����
	 * 
	 * @return
	 */
	public static UserService getUserService() {
		if (userService == null) {
			userService = new UserService();
			userService.setSocket(socket);
		}
		return userService;
	}

	/**
	 * ��ʼ������
	 * 
	 * @param socket
	 */
	public synchronized static void setSocket(Socket socket) {
		if (!isInited) {
			ClientFactory.socket = socket;
			isInited = true;
		}
	}

	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	public synchronized static Socket getSocket() {
		return socket;
	}

	/**
	 * ��ȡ�ͻ��˶���
	 * 
	 * @return
	 */
	public synchronized static Client getClient() {
		if (null == client) {
			LOGGER.debug("create client!");
			client = new ClientImpl();
		}
		return client;
	}

	/**
	 * �û��б�ģ��
	 * 
	 * @return
	 */
	public static DefaultListModel getUserListModel() {
		if (userListModel == null) {
			userListModel = new DefaultListModel();
		}
		return userListModel;
	}
}
