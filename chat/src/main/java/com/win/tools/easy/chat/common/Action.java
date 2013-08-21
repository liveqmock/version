package com.win.tools.easy.chat.common;


/**
 * 服务器处理请求的接口
 * 
 * @author 袁晓冬
 * 
 */
public interface Action {

	/**
	 * Action执行
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	int execute(Request request, Response response);
}
