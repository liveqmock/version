package com.win.tools.easy.chat.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.win.tools.easy.chat.entity.User;

/**
 * 服务器上下文，用户保存服务器信息
 * 
 * @author 袁晓冬
 * 
 */
public class ServerContext {
	/** 日志记录 */
	final static Logger LOGGER = Logger.getLogger(ServerContext.class);
	/** 单例对象 */
	private final static ServerContext context = new ServerContext();

	/** 登录用户集合 */
	private static Map<String, User> users = new HashMap<String, User>();

	private ThreadLocal<Socket> socketLocal = new ThreadLocal<Socket>();

	public void setCurrentSocket(Socket socket) {
		socketLocal.set(socket);
	}

	public Socket getCurrentSocket() {
		return socketLocal.get();
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static ServerContext getInstance() {
		return context;
	}

	/**
	 * 根据用户ID查询用户
	 * 
	 * @param userid
	 * @return
	 */
	public synchronized User getUserById(String userid) {
		return users.get(userid);
	}

	/**
	 * 添加登录用户
	 * 
	 * @param user
	 * @return -1 用户不合法，0 添加用户，1 用户已经登录
	 */
	public synchronized int addUser(User user) {
		if (null == user || user.getId() == null || user.getId().length() == 0) {
			return -1;
		}

		// 用户已登录
		if (users.containsKey(user.getId())) {
			return 1;
		}
		// 不保留密码
		user.setPassword("");
		// 设置socket
		user.setSocket(getCurrentSocket());
		users.put(user.getId(), user);
		return 0;
	}

	/**
	 * 查询所有在线用户
	 * 
	 * @return
	 */
	public synchronized Collection<User> getAllUser() {
		List<User> userList = new ArrayList<User>();
		for (User u : users.values()) {
			userList.add(u);
		}
		return userList;
	}

	/**
	 * 获取当前线程的用户信息
	 * 
	 * @return
	 */
	public User getCurrentUser() {
		Socket socket = getCurrentSocket();
		String userId = null;
		for (User u : users.values()) {
			if (u.getSocket() == socket) {
				userId = u.getId();
				break;
			}
		}
		if (userId != null && !"".equals(userId)) {
			return users.get(userId);
		}
		return null;

	}

	/**
	 * 用户登出处理
	 * 
	 * @return
	 */
	public User logout() {
		User user = getCurrentUser();
		if (null != user) {
			return users.remove(user.getId());
		}
		return null;

	}

}
