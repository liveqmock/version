package com.win.tools.easy.chat.server.service;

import com.win.tools.easy.chat.entity.User;
import com.win.tools.easy.chat.server.ServerFactory;

/**
 * �û�����
 * 
 * @author Ԭ����
 * 
 */
public class UserService {
	/**
	 * �����û�����ѯ�û�
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserByName(String userName) {
		return ServerFactory.getUserDAO().getUserByName(userName);
	}
}
