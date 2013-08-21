package com.win.tools.easy.chat.common;


/**
 * Action执行过滤
 * 
 * @author 袁晓冬
 * 
 */
public class ServerActionExecuteFilter implements Filter {

	@Override
	public void init() {

	}

	@Override
	public void doFilter(Request request, Response response, FilterChain chain) {
		// 得到Server处理类
		Action action = ChatFactory.getAction(request);

		// 如果找不到对应的Action, 返回错误信息, 找到则执行Action
		if (action == null) {
			response.setStatus(ChatCode.COMMAND_NOT_FOUND);
		} else {
			int result = action.execute(request, response);
			if (result < 0) {
				// 未设置错误代码时指定为同一错误
				if ("0".equals(response.getStatus())) {
					response.setStatus(ChatCode.ACTION_ERROR);
				}
			}
		}
		chain.doFilter(request, response);
	}
}
