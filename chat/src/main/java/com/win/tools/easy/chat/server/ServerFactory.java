package com.win.tools.easy.chat.server;

import com.win.tools.easy.chat.server.dao.UserDAO;
import com.win.tools.easy.chat.server.service.UserService;

/**
 * bean manager
 * 
 * @author Ԭ����
 * 
 */
public class ServerFactory {
	private static UserService userService = null;
	private static UserDAO userDAO = null;

	/**
	 * ������û�����
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
	 * ������û�DAO
	 * @return
	 */
	public static UserDAO getUserDAO() {
		if (null == userDAO) {
			userDAO = new UserDAO();
		}
		return userDAO;
	}
}
