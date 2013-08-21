package com.win.tools.easy.chat.server.action;

import java.util.Collection;

import com.win.tools.easy.chat.client.action.CRefreshUsersAction;
import com.win.tools.easy.chat.common.BaseAction;
import com.win.tools.easy.chat.common.ChatUtils;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.common.Response;
import com.win.tools.easy.chat.entity.User;
import com.win.tools.easy.chat.server.ServerContext;

/**
 * 刷新好友
 * 
 * @author 袁晓冬
 * 
 */
public class RefreshUsersAction extends BaseAction {

	public int execute(Request request, Response response) {
		// 从请求参数中得到新登录用户的用户名
		String userId = (String) request.getParameter("uid");
		// 通知已登录用户
		noticeUsers(userId);
		// 设置请求已发送
		response.setSend(true);
		return 0;
	}

	/**
	 * 通知所有用户刷新列表
	 */
	private void noticeUsers(String userId) {
		// 单用户刷新
		if (null != userId && userId.length() > 0) {
			noticeUser(ServerContext.getInstance().getUserById(userId));
			return;
		}
		// 全用户刷新
		for (User u : ServerContext.getInstance().getAllUser()) {
			noticeUser(u);
		}
	}

	/**
	 * 刷新一个用户的用户列表
	 * 
	 * @param user
	 */
	private void noticeUser(User user) {
		Collection<User> users = ServerContext.getInstance().getAllUser();
		Response response = new Response(CRefreshUsersAction.class);
		users.remove(user);
		response.setData("users", users);
		ChatUtils.sendResponseToUser(user, response);
	}
}
