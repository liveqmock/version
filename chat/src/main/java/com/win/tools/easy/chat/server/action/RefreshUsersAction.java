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
 * ˢ�º���
 * 
 * @author Ԭ����
 * 
 */
public class RefreshUsersAction extends BaseAction {

	public int execute(Request request, Response response) {
		// ����������еõ��µ�¼�û����û���
		String userId = (String) request.getParameter("uid");
		// ֪ͨ�ѵ�¼�û�
		noticeUsers(userId);
		// ���������ѷ���
		response.setSend(true);
		return 0;
	}

	/**
	 * ֪ͨ�����û�ˢ���б�
	 */
	private void noticeUsers(String userId) {
		// ���û�ˢ��
		if (null != userId && userId.length() > 0) {
			noticeUser(ServerContext.getInstance().getUserById(userId));
			return;
		}
		// ȫ�û�ˢ��
		for (User u : ServerContext.getInstance().getAllUser()) {
			noticeUser(u);
		}
	}

	/**
	 * ˢ��һ���û����û��б�
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
