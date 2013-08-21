package com.win.tools.easy.chat.client.action;

import java.util.Collection;

import com.win.tools.easy.chat.client.ClientContext;
import com.win.tools.easy.chat.client.ClientFactory;
import com.win.tools.easy.chat.common.Response;
import com.win.tools.easy.chat.entity.User;

/**
 * 客户端登录后处理
 * 
 * @author 袁晓冬
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
			// 不显示自己
			users.remove(currUser);
		}
		ClientFactory.getClient().showUsers(users);
	}
}
