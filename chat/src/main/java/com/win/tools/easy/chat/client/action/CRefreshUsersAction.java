package com.win.tools.easy.chat.client.action;

import java.util.Collection;

import com.win.tools.easy.chat.client.ClientContext;
import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.common.Response;
import com.win.tools.easy.chat.entity.User;

/**
 * �ͻ��˵�¼����
 * 
 * @author Ԭ����
 * 
 */
public class CRefreshUsersAction extends BaseClientAction {

	@Override
	public void doExecute(Response response) {
		@SuppressWarnings("unchecked")
		final Collection<User> users = (Collection<User>) response
				.getData("users");
		User currUser = ClientContext.getInstance().getUser();
		if (null != currUser) {
			// ����ʾ�Լ�
			users.remove(currUser);
		}
		ClientFactory.getClient().showUsers(users);
	}
}
