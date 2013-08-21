package com.win.tools.easy.chat.client;

import java.util.Collection;

import com.win.tools.easy.chat.entity.User;

public interface Client {
	/**
	 * 打开主页
	 */
	void moveToMainPage();

	/**
	 * 弹出消息
	 * 
	 * @param message
	 */
	void showMessage(String message);

	/**
	 * 刷新用户
	 * 
	 * @param users
	 */
	void showUsers(Collection<User> users);
}
