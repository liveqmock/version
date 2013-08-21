package com.win.tools.easy.chat.server.service;

import com.win.tools.easy.chat.entity.User;
import com.win.tools.easy.chat.server.ServerFactory;

/**
 * 用户服务
 * 
 * @author 袁晓冬
 * 
 */
public class UserService {
	/**
	 * 根据用户名查询用户
	 * 
	 * @param userName
	 * @return
	 */
	public User getUserByName(String userName) {
		return ServerFactory.getUserDAO().getUserByName(userName);
	}
}
