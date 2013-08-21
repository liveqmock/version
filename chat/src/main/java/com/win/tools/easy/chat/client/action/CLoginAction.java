package com.win.tools.easy.chat.client.action;

import com.win.tools.easy.chat.client.Client;
import com.win.tools.easy.chat.client.ClientContext;
import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.common.ChatCode;
import com.win.tools.easy.chat.common.Response;
import com.win.tools.easy.chat.entity.User;

/**
 * �ͻ��˵�¼����
 * 
 * @author Ԭ����
 * 
 */
public class CLoginAction extends BaseClientAction {

	@Override
	public void doExecute(Response response) {
		Client client = ClientFactory.getClient();
		if (response.getStatus() > 0) {
			client.showMessage(ChatCode.getMessageByCode(response.getStatus()));
			return;
		}
		User user = (User) response.getData("user");
		// ��¼ʧ��
		if (null != user && user.getId() != null && user.getId().length() != 0) {
			// ��¼�ɹ�
			ClientContext.getInstance().setUser(user);
			client.moveToMainPage();
		}
	}
}
