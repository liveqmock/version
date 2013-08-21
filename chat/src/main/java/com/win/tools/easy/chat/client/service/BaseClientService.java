package com.win.tools.easy.chat.client.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.win.tools.easy.chat.client.ClientContext;
import com.win.tools.easy.chat.client.action.BaseClientAction;
import com.win.tools.easy.chat.common.Action;
import com.win.tools.easy.chat.common.ChatUtils;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.entity.User;
import com.win.tools.easy.chat.server.action.LoginAction;

/**
 * �ͻ��˷�������
 * 
 * @author Ԭ����
 * 
 */
public class BaseClientService {
	/** LOGGER */
	static final Logger LOGGER = Logger.getLogger(BaseClientService.class);
	/** ��������� */
	private Socket socket = null;

	/**
	 * ��ȡ���������
	 * 
	 * @return
	 */
	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	/**
	 * �����ͻ�������
	 * 
	 * @param serverActionClass
	 * @return
	 */
	protected Request createRequest(Class<? extends Action> serverActionClass) {
		Request request = new Request(LoginAction.class);
		User user = ClientContext.getInstance().getUser();
		if (null != user) {
			request.setUserId(user.getId());
		}
		return request;
	}

	/**
	 * �����ͻ�������
	 * 
	 * @param serverActionClass
	 * @return
	 */
	protected Request createRequest(Class<? extends Action> serverActionClass,
			Class<? extends BaseClientAction> clientActionClass) {
		Request request = new Request(LoginAction.class);
		request.setClientActionClass(clientActionClass);
		User user = ClientContext.getInstance().getUser();
		if (null != user) {
			request.setUserId(user.getId());
		}
		return request;
	}

	/**
	 * �������󵽷����
	 * 
	 * @param request
	 */
	protected void sendRequest(Request request) {
		OutputStream os = null;
		try {
			os = socket.getOutputStream();
			byte[] requestBytes = ChatUtils.encode(request);
			os.write(requestBytes);
			os.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
