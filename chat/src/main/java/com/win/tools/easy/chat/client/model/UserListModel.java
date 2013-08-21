package com.win.tools.easy.chat.client.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import com.win.tools.easy.chat.entity.User;

public class UserListModel extends DefaultListModel {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2779229371342488201L;

	/** 列表中的用户 */
	private List<User> userList = new ArrayList<User>();

	@Override
	public int getSize() {
		return userList.size();
	}

	@Override
	public Object getElementAt(int index) {
		return userList.get(index);
	}

	public void addUser(User user) {

	}

}
