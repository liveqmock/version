package com.win.tools.easy.chat.client;

import com.win.tools.easy.chat.entity.User;

/**
 * 客户端上下文
 * 
 * @author 袁晓冬
 * 
 */
public class ClientContext {
	/** 单例对象 */
	private final static ClientContext context = new ClientContext();
	private User user;

	/**
	 * 判断是否已登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		if (null == user) {
			return false;
		}
		if (null == user.getId() || "".equals(user.getId())) {
			return false;
		}
		return true;
	}

	/**
	 * 获取单例
	 * 
	 * @return
	 */
	public static ClientContext getInstance() {
		return context;
	}

	/**
	 * 获取当前用户信息
	 * 
	 * @return
	 */
	public synchronized User getUser() {
		return user;
	}

	/**
	 * 设置当前用户信息，一般由登录操作执行
	 * 
	 * @param user
	 */
	public synchronized void setUser(User user) {
		this.user = user;
	}

}
