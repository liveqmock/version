package com.win.tools.easy.chat.server;

import com.win.tools.easy.chat.server.dao.UserDAO;
import com.win.tools.easy.chat.server.service.UserService;

/**
 * bean manager
 * 
 * @author 袁晓冬
 * 
 */
public class ServerFactory {
	private static UserService userService = null;
	private static UserDAO userDAO = null;

	/**
	 * 服务端用户服务
	 * 
	 * @return
	 */
	public static UserService getUserService() {
		if (null == userService) {
			userService = new UserService();
		}
		return userService;
	}

	/**
	 * 服务端用户DAO
	 * @return
	 */
	public static UserDAO getUserDAO() {
		if (null == userDAO) {
			userDAO = new UserDAO();
		}
		return userDAO;
	}
}
