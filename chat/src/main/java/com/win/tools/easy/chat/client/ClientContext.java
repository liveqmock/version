package com.win.tools.easy.chat.client;

import com.win.tools.easy.chat.entity.User;

/**
 * �ͻ���������
 * 
 * @author Ԭ����
 * 
 */
public class ClientContext {
	/** �������� */
	private final static ClientContext context = new ClientContext();
	private User user;

	/**
	 * �ж��Ƿ��ѵ�¼
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
	 * ��ȡ����
	 * 
	 * @return
	 */
	public static ClientContext getInstance() {
		return context;
	}

	/**
	 * ��ȡ��ǰ�û���Ϣ
	 * 
	 * @return
	 */
	public synchronized User getUser() {
		return user;
	}

	/**
	 * ���õ�ǰ�û���Ϣ��һ���ɵ�¼����ִ��
	 * 
	 * @param user
	 */
	public synchronized void setUser(User user) {
		this.user = user;
	}

}
