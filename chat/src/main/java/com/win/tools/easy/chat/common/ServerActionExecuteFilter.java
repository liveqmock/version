package com.win.tools.easy.chat.common;


/**
 * Actionִ�й���
 * 
 * @author Ԭ����
 * 
 */
public class ServerActionExecuteFilter implements Filter {

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
		} else {
			int result = action.execute(request, response);
			if (result < 0) {
				// δ���ô������ʱָ��Ϊͬһ����
				if ("0".equals(response.getStatus())) {
					response.setStatus(ChatCode.ACTION_ERROR);
				}
			}
		}
		chain.doFilter(request, response);
	}
}
