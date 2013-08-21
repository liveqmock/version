package com.win.tools.easy.chat.server.action;

import com.win.tools.easy.chat.client.action.CRefreshUsersAction;
import com.win.tools.easy.chat.common.BaseAction;
import com.win.tools.easy.chat.common.ChatCode;
import com.win.tools.easy.chat.common.Request;
import com.win.tools.easy.chat.common.Response;
import com.win.tools.easy.chat.entity.User;
import com.win.tools.easy.chat.server.ServerContext;
import com.win.tools.easy.chat.server.ServerFactory;

/**
 * 用户登录
 * 
 * @author 袁晓冬
 * 
 */
public class LoginAction extends BaseAction {

	public int execute(Request request, Response response) {
		// 从请求参数中得到新登录用户的用户名
		String name = (String) request.getParameter("uname");
		// 从请求参数中得到新登录用户的密码
		String pwd = (String) request.getParameter("pwd");
		if (null == name || null == pwd || "".equals(name) || "".equals(pwd)) {
			response.setStatus(ChatCode.USER_INVALID);
			return -1;

		}
		// 根据用户名查询用户
		User user = ServerFactory.getUserService().getUserByName(name);
		if (null == user) {
			return -1;
		}
		if (!pwd.equals(user.getPassword())) {
			response.setStatus(ChatCode.USER_INVALID);
			return -1;
		}
		// 加入到所有玩家中
		ServerContext context = ServerContext.getInstance();
		int res = context.addUser(user);
		if (res < 0) {// 登录失败
			return -1;
		}
		if (res > 0) {
			response.setStatus(ChatCode.USER_LOGONED);
			return -1;
		}
		// 将玩家设置到响应中
		response.setData("user", user);
		// 设置用户刷新请求
		Request refresh = new Request(RefreshUsersAction.class);
		refresh.setClientActionClass(CRefreshUsersAction.class);
		request.appendRequest(refresh);
		return 0;
	}
}
