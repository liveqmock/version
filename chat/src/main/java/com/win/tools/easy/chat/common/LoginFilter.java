package com.win.tools.easy.chat.common;

import java.util.ArrayList;
import java.util.List;

import com.win.tools.easy.chat.server.ServerContext;
import com.win.tools.easy.chat.server.action.LoginAction;

public class LoginFilter implements Filter {

	private static final List<Class<? extends Action>> UNFILTER = new ArrayList<Class<? extends Action>>();
	static {
		UNFILTER.add(LoginAction.class);
	}

	@Override
	public void init() {

	}

	@Override
	public void doFilter(Request request, Response response, FilterChain chain) {
		// �õ�Server������
		Action action = ChatFactory.getAction(request);

		// ����Ҳ�����Ӧ��Action, ���ش�����Ϣ, �ҵ���ִ��Action
		if (action == null) {
			response.setStatus(ChatCode.COMMAND_NOT_FOUND);
			return;
		}
		// ��Ҫ���е�¼��֤
		if (!UNFILTER.contains(action.getClass())) {
			String requestUserId = request.getUserId();
			if (null == requestUserId
					|| "".equals(requestUserId)
					|| null == ServerContext.getInstance().getUserById(
							requestUserId)) {
				response.setStatus(ChatCode.USER_UNLOGIN);
				return;
			}
		}
		chain.doFilter(request, response);
	}
}
