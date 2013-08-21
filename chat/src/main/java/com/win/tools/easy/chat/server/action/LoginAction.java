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
 * �û���¼
 * 
 * @author Ԭ����
 * 
 */
public class LoginAction extends BaseAction {

	public int execute(Request request, Response response) {
		// ����������еõ��µ�¼�û����û���
		String name = (String) request.getParameter("uname");
		// ����������еõ��µ�¼�û�������
		String pwd = (String) request.getParameter("pwd");
		if (null == name || null == pwd || "".equals(name) || "".equals(pwd)) {
			response.setStatus(ChatCode.USER_INVALID);
			return -1;

		}
		// �����û�����ѯ�û�
		User user = ServerFactory.getUserService().getUserByName(name);
		if (null == user) {
			return -1;
		}
		if (!pwd.equals(user.getPassword())) {
			response.setStatus(ChatCode.USER_INVALID);
			return -1;
		}
		// ���뵽���������
		ServerContext context = ServerContext.getInstance();
		int res = context.addUser(user);
		if (res < 0) {// ��¼ʧ��
			return -1;
		}
		if (res > 0) {
			response.setStatus(ChatCode.USER_LOGONED);
			return -1;
		}
		// ��������õ���Ӧ��
		response.setData("user", user);
		// �����û�ˢ������
		Request refresh = new Request(RefreshUsersAction.class);
		refresh.setClientActionClass(CRefreshUsersAction.class);
		request.appendRequest(refresh);
		return 0;
	}
}
