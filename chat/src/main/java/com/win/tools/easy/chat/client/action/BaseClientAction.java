package com.win.tools.easy.chat.client.action;

import com.win.tools.easy.chat.client.Client;
import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.common.BaseAction;
import com.win.tools.easy.chat.common.ChatCode;
import com.win.tools.easy.chat.common.Response;

/**
 * �ͻ���Action����
 * 
 * @author Ԭ����
 * 
 */
public abstract class BaseClientAction extends BaseAction {
	/** �ͻ���UI�ӿ� */
	private Client client;

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	/**
	 * ������Ӧ���ɼ����̵߳���
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
	 * actionִ��
	 * 
	 * @param response
	 */
	public abstract void doExecute(Response response);

}
