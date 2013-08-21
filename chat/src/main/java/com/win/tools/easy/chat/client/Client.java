package com.win.tools.easy.chat.client;

import java.util.Collection;

import com.win.tools.easy.chat.entity.User;

public interface Client {
	/**
	 * ����ҳ
	 */
	void moveToMainPage();

	/**
	 * ������Ϣ
	 * 
	 * @param message
	 */
	void showMessage(String message);

	/**
	 * ˢ���û�
	 * 
	 * @param users
	 */
	void showUsers(Collection<User> users);
}
