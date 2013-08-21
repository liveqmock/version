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
 * 客户端服务层工厂
 * 
 * @author 袁晓冬
 * 
 */
public class ClientFactory {
	/** 日志记录 */
	private static final Logger LOGGER = Logger.getLogger(ClientFactory.class);
	/** 服务端链接 */
	private static Socket socket = null;
	/** 是否已初始化链接 */
	private static boolean isInited = false;
	/** 保存已创建的聊天窗口 */
	private static Map<String, ChatWindow> chatWinMap = new HashMap<String, ChatWindow>();

	/**
	 * 打开与user用户聊天的窗口
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
	 * 根据好友ID获取聊天窗口
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
	 * 从缓存中删除聊天窗口
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

	/** 用户服务 */
	private static UserService userService;

	/** 客户端UI接口 */
	private static Client client;
	/** 用户显示模型 */
	private static DefaultListModel userListModel;

	/**
	 * 获取用户服务
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
	 * 初始化链接
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
	 * 获取服务端链接
	 * 
	 * @return
	 */
	public synchronized static Socket getSocket() {
		return socket;
	}

	/**
	 * 获取客户端对象
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
	 * 用户列表模型
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
