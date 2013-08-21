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
 * 客户端服务层基类
 * 
 * @author 袁晓冬
 * 
 */
public class BaseClientService {
	/** LOGGER */
	static final Logger LOGGER = Logger.getLogger(BaseClientService.class);
	/** 服务端链接 */
	private Socket socket = null;

	/**
	 * 获取服务端链接
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
	 * 创建客户端请求
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
	 * 创建客户端请求
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
	 * 发送请求到服务端
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
