package com.win.tools.easy.chat.client.action;

import com.win.tools.easy.chat.client.Client;
import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.common.BaseAction;
import com.win.tools.easy.chat.common.ChatCode;
import com.win.tools.easy.chat.common.Response;

/**
 * 客户端Action基类
 * 
 * @author 袁晓冬
 * 
 */
public abstract class BaseClientAction extends BaseAction {
	/** 客户端UI接口 */
	private Client client;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * 处理响应，由监听线程调用
	 * 
	 * @param response
	 */
	public final void execute(Response response) {
		logger.debug("do callback:" + getClass());
		Client client = ClientFactory.getClient();
		if (response.getStatus() > 0) {
			client.showMessage(ChatCode.getMessageByCode(response.getStatus()));
			return;
		}
		doExecute(response);
	}

	/**
	 * action执行
	 * 
	 * @param response
	 */
	public abstract void doExecute(Response response);

}
